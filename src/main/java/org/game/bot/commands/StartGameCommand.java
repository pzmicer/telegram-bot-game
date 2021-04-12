package org.game.bot.commands;

import org.game.bot.models.Room;
import org.game.bot.service.ReplyMessageService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.ArrayList;
import java.util.List;

public class StartGameCommand extends Command {

    public StartGameCommand(String args) {
        noArgsRequired(args);
    }

    @Override
    public List<SendMessage> execute(User user, ReplyMessageService service) {
        var rem = Room.findUser(user);
        if (rem.isEmpty()) {
            return List.of(service.getMessage(user.getId(), "roomException"));
        }
        Room room = rem.get().getValue();
        room.startGame();
        List<SendMessage> result = new ArrayList<>();
        for (var _user : room.getUsers()) {
            if (!room.getLeader().equals(_user))
                result.add(service.getMessage(_user.getId(), "startGameNotification", user.getUserName()));
            else
                result.add(service.getMessage(_user.getId(), "leaderNotification"));
        }
        return result;
    }
}
