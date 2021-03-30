package org.game.bot.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Service
public class ReplyMessageService {

    private final LocaleMessageService localeMessageService;

    public ReplyMessageService(LocaleMessageService messageService) {
        this.localeMessageService = messageService;
    }

    public SendMessage getReplyMessage(String chatId, String replyMessage) {
        SendMessage message = new SendMessage(chatId, localeMessageService.getMessage(replyMessage));
        message.enableMarkdown(true);
        return message;
    }

    public SendMessage getReplyMessage(String chatId, String replyMessage, Object... args) {
        SendMessage message = new SendMessage(chatId, localeMessageService.getMessage(replyMessage, args));
        message.enableMarkdown(true);
        return message;
    }
}
