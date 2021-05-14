package org.game.bot.commands;

import org.game.bot.models.Room;
import org.game.bot.service.ReplyMessageService;
import org.game.bot.service.RoomService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class StartGameCommand extends Command {

    public StartGameCommand(ReplyMessageService service, RoomService roomService) {
        super(service, roomService);
    }

    @Override
    public List<SendMessage> execute(User user, String args) {
        return noArgsRequired(user, args)
            .orElseGet(() -> roomService.findUser(user)
                .map(entry -> notInGameRequired(user, entry.getValue())
                    .orElseGet(() -> proceed(user, entry.getValue())))
                .orElseGet(() -> List.of(service.getMessage(user, "notInRoomException"))));
    }

    private List<SendMessage> proceed(User user, Room room) {
        if (room.getUsers().size() < 2) {
            return List.of(service.getMessage(user, "notEnoughUsersException"));
        }
        if (room.isInGame()) {
            return List.of(service.getMessage(user, "inGame"));
        }
        roomService.startGame(room);
        List<SendMessage> result = new ArrayList<>();
        for (var _user : room.getUsers()) {
            if (!room.getLeader().equals(_user))
                result.add(service.getMessage(_user, "startGameNotification", room.getLeader().getUserName()));
            else
                result.add(service.getMessage(_user, "leaderNotification"));
        }
        return result;
    }
}
