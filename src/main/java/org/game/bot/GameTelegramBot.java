package org.game.bot;

import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.json.ParseException;
import org.game.bot.command.Command;
import org.game.bot.command.CommandType;
import org.game.bot.service.ReplyMessageService;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.game.bot.handlers.DefaultHandler;
import org.game.bot.handlers.Handler;
import org.game.bot.handlers.UserHandler;

@Setter
@Slf4j
public class GameTelegramBot extends TelegramWebhookBot {
    private String webHookPath;
    private String botUserName;
    private String botToken;

    private ReplyMessageService service;

    public GameTelegramBot(DefaultBotOptions botOptions, ReplyMessageService service) {
        super(botOptions);
        this.service = service;
    }

    @Override
    public String getBotUsername() {
        return botUserName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public String getBotPath() {
        return webHookPath;
    }

    @SneakyThrows
    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        return analyzeUpdate(update);
    }

    private SendMessage analyzeUpdate(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        String inputText = update.getMessage().getText();
        Command command = null;
        try {
            command = Command.parseCommand(inputText);
        } catch (ParseException e) {
            return service.getReplyMessage(chatId,"exception");
        }
        Handler handlerForCommand = getHandlerForCommand(command.getCommand(), this, service);
        return handlerForCommand.handle(chatId, command, update);
    }

    private Handler getHandlerForCommand(CommandType commandType, GameTelegramBot bot, ReplyMessageService service) {
        switch (commandType) {
            case START:
            case HELP:
                return new UserHandler(service);
            default:
                return new DefaultHandler(service);
        }
    }
}
