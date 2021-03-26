package project.game_bot.botapi.handlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import project.game_bot.botapi.BotState;
import project.game_bot.cache.UsersDataCache;
import project.game_bot.service.ReplyMessageService;

@Slf4j
@Component
public class StartHandler extends Handler implements InputMessageHandler {
    public StartHandler(UsersDataCache usersDataCache, ReplyMessageService messageService) {
        super(usersDataCache, messageService);
    }

    @Override
    public SendMessage handle(Message message) {
        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.START;
    }

    private SendMessage processUsersInput(Message inputMsg) {
        long userId = inputMsg.getFrom().getId();
        String chatId = inputMsg.getChatId().toString();

        SendMessage replyToUser = null;
        BotState botState = usersDataCache.getUserCurrentBotState(userId);
        if(botState.equals(BotState.START)) {
            replyToUser = messageService.getReplyMessage(chatId, "start");
            usersDataCache.setUserCurrentBotState(userId, BotState.START);
        }
        return replyToUser;
    }
}