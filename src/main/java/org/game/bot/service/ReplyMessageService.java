package org.game.bot.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Service
public class ReplyMessageService {

    private final LocaleMessageService messageService;

    public ReplyMessageService(LocaleMessageService messageService) {
        this.messageService = messageService;
    }

    public SendMessage getMessage(Long chatID, String replyMessage) {
        return getMessage(chatID, replyMessage, (Object[]) null);
    }

    public SendMessage getMessage(Long chatID, String replyMessage, Object... args) {
        SendMessage message = new SendMessage(chatID.toString(), messageService.getMessage(replyMessage, args));
        return message;
    }
}
