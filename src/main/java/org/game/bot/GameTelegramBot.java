package org.game.bot;

import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.json.ParseException;
import org.game.bot.commands.Command;
import org.game.bot.commands.CommandHandler;
import org.game.bot.service.ReplyMessageService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.concurrent.CompletableFuture;


@Setter
@Slf4j
public class GameTelegramBot extends TelegramWebhookBot {

    private String webHookPath;

    private String botUserName;

    private String botToken;

    private ReplyMessageService service;

    private CommandHandler commandHandler;

    public GameTelegramBot(DefaultBotOptions botOptions, ReplyMessageService service) {
        super(botOptions);
        this.service = service;
        this.commandHandler = new CommandHandler(service, this);
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
        if (update.getMessage() == null)
            return null;
        Long chatID = update.getMessage().getChatId();
        String inputText = update.getMessage().getText();
        try {
            log.info("Handle: " + update.getMessage().getText());
            for (SendMessage msg : commandHandler.handle(inputText, update.getMessage().getFrom()))
                execute(msg);
        } catch (ParseException e) {
            log.error("Can't parse command: " + inputText);
            return service.getMessage(chatID,"error");
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
        return null;
    }
}
