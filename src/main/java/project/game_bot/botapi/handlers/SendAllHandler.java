package project.game_bot.botapi.handlers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import project.game_bot.botapi.BotState;
import project.game_bot.cache.UsersDataCache;
import project.game_bot.service.ReplyMessageService;


@Component
@Slf4j
public class SendAllHandler extends Handler implements InputMessageHandler {
    public SendAllHandler(UsersDataCache usersDataCache, ReplyMessageService messageService) {
        super(usersDataCache, messageService);
    }

    @Override
    public SendMessage handle(Message message) {
        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SEND_ALL;
    }

    private SendMessage processUsersInput(Message inputMsg) {
        long userId = inputMsg.getFrom().getId();
        String chatId = inputMsg.getChatId().toString();
        SendMessage replyToUser = null;
        BotState botState = usersDataCache.getUserCurrentBotState(userId);
        usersDataCache.getUsersBotStates().entrySet().forEach(item -> {
            if(userId != item.getKey() && item.getValue().equals(BotState.SEND_ALL)){
                //replyToUser = new SendMessage(item.getKey(),inputMsg);
                //TODO bot.execute()
                // execute(new SendMessage(chatId, inputMsg.getText());
            }
        });
        return replyToUser;
    }
}
