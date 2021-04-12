package org.game.bot.commands;

import org.game.bot.models.Room;
import org.game.bot.service.ReplyMessageService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.ArrayList;
import java.util.List;

public class SetKeywordCommand extends Command {

    String keyword;

    public SetKeywordCommand(String args) {
        argsRequired(args);
        String[] argsArray = args.split("\\s+");
        if (argsArray.length != 1 || argsArray[0].length() < 2)
            throw new IllegalArgumentException();
        this.keyword = argsArray[0];
    }

    @Override
    public List<SendMessage> execute(User user, ReplyMessageService service) {
        var rem = Room.findUser(user);
        if (rem.isEmpty()) {
            return List.of(service.getMessage(user.getId(), "roomException"));
        }
        Room room = rem.get().getValue();
        if (!room.isInGame()) {
            return List.of(service.getMessage(user.getId(), "notInGame"));
        }
        if (!room.getLeader().equals(user)) {
            return List.of(service.getMessage(user.getId(), "leaderException"));
        }
        room.setKeyword(keyword);
        List<SendMessage> result = new ArrayList<>();
        for(var _user : room.getUsers()) {
            result.add(service.getMessage(_user.getId(), "keywordSet"));
        }
        return result;
    }
}
