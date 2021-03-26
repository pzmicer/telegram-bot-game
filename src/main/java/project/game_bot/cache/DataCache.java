package project.game_bot.cache;

import project.game_bot.botapi.BotState;

public interface DataCache {
    void setUserCurrentBotState(long userId, BotState botState);
    BotState getUserCurrentBotState(long userId);
}
