package org.game.bot.handlers;

import org.game.bot.GameTelegramBot;
import org.game.bot.command.Command;
import org.game.bot.command.CommandType;
import org.game.bot.service.ReplyMessageService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class SystemHandler extends Handler {

    private final String END_LINE = "\n";

    public SystemHandler(GameTelegramBot bot, ReplyMessageService replyMessageService) {
        super(bot, replyMessageService);
    }

    @Override
    public SendMessage handle(String chatId, Command parsedCommand, Update update) {
        CommandType command = parsedCommand.getCommand();

        switch (command) {
            case START:
                return getMessageStart(chatId);
            case HELP:
                return getMessageHelp(chatId);
            case ID:
                return new SendMessage(chatId, "Ваш телеграм id: " + update.getMessage().getFrom().getId());
        }
        return replyMessageService.getReplyMessage(chatId, "exception");
    }

    private SendMessage getMessageHelp(String chatID) {
        SendMessage sendMessage = replyMessageService.getReplyMessage(chatID, "help");
        sendMessage.enableMarkdown(true);
        return sendMessage;
    }

    private SendMessage getMessageStart(String chatID) {
        SendMessage sendMessage = replyMessageService.getReplyMessage(chatID, "start");
        sendMessage.enableMarkdown(true);
        return sendMessage;
    }
}