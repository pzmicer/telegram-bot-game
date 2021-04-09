package org.game.bot.commands;

import org.game.bot.Room;
import org.game.bot.service.ReplyMessageService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;

import java.security.InvalidParameterException;
import java.util.List;

public class SetKeywordCommand extends Command {

    String keyword;

    public SetKeywordCommand(String args) {
        String[] argsArray = args.split("\\s+");
        if (argsArray.length != 1 || argsArray[0].length() < 2)
            throw new IllegalArgumentException();
        this.keyword = argsArray[0];
    }

    @Override
    public List<SendMessage> execute(User user, ReplyMessageService service) {
        var rem = Room.checkUser(user);
        if (rem.isPresent()) {
            if (rem.get().getValue().getLeader().equals(user)) {
                rem.get().getValue().setKeyword(keyword);
            }
        }
    }
}
