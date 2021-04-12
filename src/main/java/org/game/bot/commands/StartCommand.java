package org.game.bot.commands;

import org.game.bot.service.ReplyMessageService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.List;


public class StartCommand extends Command {

    public StartCommand(String args) {
        noArgsRequired(args);
    }

    @Override
    public List<SendMessage> execute(User user, ReplyMessageService service) {
        return List.of(service.getMessage(user.getId(), "start"));
    }
}