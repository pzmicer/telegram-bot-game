package org.game.bot.commands;

import org.game.bot.models.Room;
import org.game.bot.service.ReplyMessageService;
import org.game.bot.service.RoomService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SetKeywordCommand extends Command {

    private String keyword;

    public SetKeywordCommand(ReplyMessageService service, RoomService roomService) {
        super(service, roomService);
    }

    @Override
    public List<SendMessage> execute(User user, String args) {
        return argsRequired(user, args)
            .orElseGet(() -> roomService.findUser(user)
                .map(entry -> inGameRequired(user, entry.getValue())
                    .or(() -> checkCountdown(user, entry.getValue()))
                    .or(() -> leaderRequired(user, entry.getValue()))
                    .orElseGet(() -> proceed(user, entry.getValue())))
                .orElseGet(() -> List.of(service.getMessage(user, "notInRoomException"))));
    }

    @Override
    protected Optional<List<SendMessage>> argsRequired(User user, String args) {
        if (args == null)
            return Optional.of(List.of(service.getMessage(user, "invalidArgs")));
        String[] argsArray = args.split("\\s+");
        if (argsArray.length != 1 || argsArray[0].length() < 2)
            return Optional.of(List.of(service.getMessage(user, "invalidArgs")));
        this.keyword = argsArray[0];
        return Optional.empty();
    }

    private List<SendMessage> proceed(User user, Room room) {
        if (room.getKeyword() != null)
            return List.of(service.getMessage(user, "notNullKeyword"));
        room.setKeyword(keyword);
        List<SendMessage> result = new ArrayList<>();
        for(var _user : room.getUsers()) {
            result.add(service.getMessage(_user, "keywordSet"));
            result.add(service.getMessage(_user, "currentWord", roomService.getCurrentPrefix(room)));
        }
        return result;
    }
}
