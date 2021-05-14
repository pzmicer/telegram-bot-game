package org.game.bot.commands;

import org.game.bot.models.Room;
import org.game.bot.service.ReplyMessageService;
import org.game.bot.service.RoomService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ChatCommand extends Command {

    private String message;

    public ChatCommand(ReplyMessageService service, RoomService roomService) {
        super(service, roomService);
    }

    @Override
    public List<SendMessage> execute(User user, String args) {
        return argsRequired(user, args)
            .orElseGet(() -> roomService.findUser(user)
            .map(entry -> proceed(user, entry.getValue()))
            .orElseGet(() -> List.of(service.getMessage(user, "notInRoomException"))));
    }

    @Override
    protected Optional<List<SendMessage>> argsRequired(User user, String args) {
        return super.argsRequired(user, args)
        .or(() -> {
            message = args;
            return Optional.empty();
        });
    }

    private List<SendMessage> proceed(User user, Room room) {
        List<SendMessage> result = new ArrayList<>();
        for(var _user : room.getUsers()) {
            if (!user.equals(_user)) {
                result.add(new SendMessage(_user.getId().toString(), message));
            }
        }
        return result;
    }
}
