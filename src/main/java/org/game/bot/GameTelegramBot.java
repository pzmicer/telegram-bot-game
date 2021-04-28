package org.game.bot;

import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.json.ParseException;
import org.game.bot.commands.Command;
import org.game.bot.commands.CommandHandler;
import org.game.bot.service.ReplyMessageService;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

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
        log.debug("Analysing Update...");
        if (update.getMessage() == null)
            return null;
        Long chatID = update.getMessage().getChatId();
        String inputText = update.getMessage().getText();
        Command command = null;
        try {
            log.info("Before handle " + update.getMessage().getFrom().getUserName());
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
