package org.game.bot.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class LocaleMessageService {

    private static Locale locale;
    private static MessageSource messageSource;

    public LocaleMessageService(@Value("${localeTag}") String localeTag, MessageSource messageSource) {
        LocaleMessageService.messageSource = messageSource;
        locale = Locale.forLanguageTag(localeTag);
    }

    public static String getMessage(String message) {
        return messageSource.getMessage(message,null,locale);
    }

    public static String getMessage(String message, Object... args) {
        return messageSource.getMessage(message, args, locale);
    }
}
