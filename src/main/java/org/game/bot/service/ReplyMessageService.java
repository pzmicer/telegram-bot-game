package org.game.bot.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;

@Service
public class ReplyMessageService {

    private final LocaleMessageService messageService;

    public ReplyMessageService(LocaleMessageService messageService) {
        this.messageService = messageService;
    }

    public SendMessage getMessage(User user, String replyMessage) {
        return getMessage(user, replyMessage, (Object[]) null);
    }

    public SendMessage getMessage(User user, String replyMessage, Object... args) {
        return new SendMessage(user.getId().toString(), messageService.getMessage(replyMessage, args));
    }

    public SendMessage getMessage(Long chatID, String replyMessage) {
        return getMessage(chatID, replyMessage, (Object[]) null);
    }

    public SendMessage getMessage(Long chatID, String replyMessage, Object... args) {
        return new SendMessage(chatID.toString(), messageService.getMessage(replyMessage, args));
    }
}
