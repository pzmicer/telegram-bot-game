package org.game.bot.appconfig;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.game.bot.GameTelegramBot;
import org.game.bot.service.ReplyMessageService;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "telegrambot")
public class BotConfig {
    private String webHookPath;
    private String botUsername;
    private String botToken;

    @Bean
    public GameTelegramBot gameTelegramBot(ReplyMessageService service) {
        DefaultBotOptions options = new DefaultBotOptions();
        GameTelegramBot gameTelegramBot = new GameTelegramBot(options, service);
        gameTelegramBot.setWebHookPath(webHookPath);
        gameTelegramBot.setBotToken(botToken);
        gameTelegramBot.setBotUserName(botUsername);
        return gameTelegramBot;
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}
