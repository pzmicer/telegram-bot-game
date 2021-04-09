package org.game.bot.commands;

import org.game.bot.Room;
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
        var rem = Room.checkUser(user);
        if (rem.isPresent()) {
            rem.get().getValue().startGame();
            List<SendMessage> result = new ArrayList<>();
            for (var item : rem.get().getValue().getUsers()) {
                if (!rem.get().getValue().getLeader().equals(item))
                    result.add(service.getReplyMessage(item.getId(), "startGameNotification"));
                else
                    result.add(service.getReplyMessage(item.getId(), "leaderNotification"));
            }
            return result;
        }
        return List.of(service.getReplyMessage(user.getId(), "startGameException"));
    }
}
