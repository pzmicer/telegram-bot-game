package org.game.bot;

import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.game.bot.command.Command;
import org.game.bot.command.CommandParser;
import org.game.bot.command.CommandType;
import org.game.bot.service.ReplyMessageService;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.game.bot.handlers.DefaultHandler;
import org.game.bot.handlers.Handler;
import org.game.bot.handlers.SystemHandler;

@Setter
@Slf4j
public class GameTelegramBot extends TelegramWebhookBot {
    private String webHookPath;
    private String botUserName;
    private String botToken;

    private CommandParser parser;
    private ReplyMessageService service;

    public GameTelegramBot(DefaultBotOptions botOptions, ReplyMessageService service, CommandParser parser) {
        super(botOptions);
        this.service = service;
        this.parser = parser;
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
        Long chatId = update.getMessage().getChatId();
        String inputText = update.getMessage().getText();
        Command command = parser.parseCommand(inputText);
        Handler handlerForCommand = getHandlerForCommand(command.getCommand(), this, service);
        return handlerForCommand.handle(chatId.toString(), command, update);
    }

    private Handler getHandlerForCommand(CommandType command, GameTelegramBot bot, ReplyMessageService service) {
        if (command == null) {
            log.warn("Null command accepted. This is not good scenario.");
            return new DefaultHandler(bot, service);
        }
        switch (command) {
            case START:
            case HELP:
            case ID:
                SystemHandler systemHandler = new SystemHandler(bot, service);
                log.info("Handler for command[" + command.toString() + "] is: " + systemHandler);
                return systemHandler;
            default:
                log.info("Handler for command[" + command.toString() + "] not Set. Return DefaultHandler");
                return new DefaultHandler(bot, service);
        }
    }
}
