package org.game.bot.handlers;

import org.game.bot.GameTelegramBot;
import org.game.bot.command.Command;
import org.game.bot.service.ReplyMessageService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class DefaultHandler extends Handler {

    public DefaultHandler(ReplyMessageService replyMessageService) {
        super(replyMessageService);
    }

    @Override
    public SendMessage handle(String chatId, Command command, Update update) {
        return replyMessageService.getReplyMessage(update.getMessage().getChatId().toString(),"exception");
    }
}