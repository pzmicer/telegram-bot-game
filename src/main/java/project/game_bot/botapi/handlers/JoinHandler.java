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
public class JoinHandler extends Handler implements InputMessageHandler {
    public JoinHandler(UsersDataCache usersDataCache, ReplyMessageService messageService) {
        super(usersDataCache, messageService);
    }

    @Override
    public SendMessage handle(Message message) {
        long userId = message.getFrom().getId();
        String chatId = message.getChatId().toString();
        SendMessage replyToUser ;

        replyToUser = new SendMessage();
        usersDataCache.setUserCurrentBotState(userId, BotState.SEND_ALL);

        return replyToUser;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.JOIN;
    }
}
