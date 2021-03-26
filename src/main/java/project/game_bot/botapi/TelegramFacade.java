package project.game_bot.botapi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import project.game_bot.cache.UsersDataCache;

@Component
@Slf4j
public class TelegramFacade {
    private  BotStateContext botStateContext;
    private UsersDataCache usersDataCache;

    public TelegramFacade(BotStateContext botStateContext, UsersDataCache usersDataCache) {
        this.botStateContext = botStateContext;
        this.usersDataCache = usersDataCache;
    }

    public SendMessage handleUpdate(Update update) {
        SendMessage replyMessage = null;
        Message message = update.getMessage();
        if(message != null && message.hasText()) {
            log.info("New message from User:{}, chatId:{}, with text: {}",
                    message.getFrom().getUserName(), message.getChatId(), message.getText());
            replyMessage = handleInputMessage(message);
        }
        return replyMessage;
    }

    private SendMessage handleInputMessage(Message message) {
        String inputMessage = message.getText();
        long userId = message.getFrom().getId();
        BotState botState;
        SendMessage replyMessage;

        switch (inputMessage) {
            case "/start":
                botState = BotState.START;
                break;
            case "/help":
                botState = BotState.HELP;
                break;
            case "/verify id":
                botState = BotState.JOIN;
                break;
            default:
                botState = usersDataCache.getUserCurrentBotState(userId);
        }
        usersDataCache.setUserCurrentBotState(userId, botState);

        replyMessage = botStateContext.processInputMessage(botState, message);

        return replyMessage;
    }
}
