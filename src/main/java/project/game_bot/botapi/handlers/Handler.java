package project.game_bot.botapi.handlers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import project.game_bot.GameTelegramBot;
import project.game_bot.cache.UsersDataCache;
import project.game_bot.service.ReplyMessageService;

@Slf4j
public abstract class Handler {
    protected ReplyMessageService messageService;
    protected UsersDataCache usersDataCache;
    protected GameTelegramBot bot;

    protected Handler(UsersDataCache usersDataCache, ReplyMessageService messageService) {
        this.usersDataCache = usersDataCache;
        this.messageService = messageService;

    }

}
