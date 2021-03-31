package org.game.bot.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Service
public class ReplyMessageService {

    public static SendMessage getReplyMessage(Long chatID, String replyMessage) {
        return getReplyMessage(chatID, replyMessage, (Object[]) null);
    }

    public static SendMessage getReplyMessage(Long chatID, String replyMessage, Object... args) {
        SendMessage message = new SendMessage(chatID.toString(), LocaleMessageService.getMessage(replyMessage, args));
        //message.enableMarkdown(true);
        return message;
    }
}
