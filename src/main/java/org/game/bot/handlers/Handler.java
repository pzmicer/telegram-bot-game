package org.game.bot.handlers;

import org.game.bot.GameTelegramBot;
import org.game.bot.command.Command;
import org.game.bot.service.ReplyMessageService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public abstract class Handler {
    protected ReplyMessageService replyMessageService;

    Handler(ReplyMessageService replyMessageService) {
        this.replyMessageService = replyMessageService;
    }

    public abstract SendMessage handle(String chatId, Command command, Update update);
}
