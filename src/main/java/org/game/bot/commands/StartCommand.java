package org.game.bot.commands;

import org.game.bot.service.ReplyMessageService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

@BotCommand(name="start")
public class StartCommand extends Command {

    public StartCommand(String args) {
        super(args);
    }

    @Override
    public SendMessage execute(AbsSender sender, User user) {
        SendMessage sendMessage = ReplyMessageService.getReplyMessage(user.getId(), "start");
        return sendMessage;
    }
}
