package project.game_bot.cache;

import lombok.Getter;
import org.springframework.stereotype.Component;
import project.game_bot.botapi.BotState;

import java.util.HashMap;
import java.util.Map;

@Component
public class UsersDataCache implements DataCache {
    @Getter
    private Map<Long, BotState> usersBotStates = new HashMap<>();

    @Override
    public void setUserCurrentBotState(long userId, BotState botState) {
        usersBotStates.put(userId, botState);
    }

    @Override
    public BotState getUserCurrentBotState(long userId) {
        BotState botState = usersBotStates.get(userId);
        if (botState == null) {
            botState = BotState.START;
        }
        return botState;
    }


}
