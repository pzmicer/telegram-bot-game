package org.game.bot.commands;

import org.game.bot.service.ReplyMessageService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

public class NotCommand extends Command {

    public NotCommand(String args) {
        super(args);
    }

    @Override
    public SendMessage execute(AbsSender sender, User user) {
        return ReplyMessageService.getReplyMessage(user.getId(),"exception");
    }
}
