package org.game.bot.commands;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.json.ParseException;
import org.atteo.classindex.ClassIndex;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.lang.reflect.InvocationTargetException;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Component
@Slf4j
public abstract class Command {

    protected String args;

    //PARSE SECTION

    public static Command parseCommand(String text) throws ParseException {
        if (text == null)
            throw new ParseException();
        String trimText = text.trim();
        if (trimText.startsWith("/")) {
            int spaceIndex = trimText.indexOf(" ");
            String command;
            String message = "";
            if(spaceIndex < 0)
                command = trimText.substring(1);
            else {
                command = trimText.substring(1, spaceIndex);
                message = trimText.substring(spaceIndex + 1);
            }
            try {
                return findClass(command, message);
            } catch (Exception e) {
                throw new ParseException();
            }
        }
        throw new ParseException();
    }

    private static Command findClass(String name, String args) throws
            NoSuchMethodException,
            IllegalAccessException,
            InvocationTargetException,
            InstantiationException,
            ClassNotFoundException {
        for(Class<?> klass : ClassIndex.getAnnotated(BotCommand.class)) {
            if (klass.getAnnotation(BotCommand.class).name().equals(name))
                return (Command) klass.getConstructor(String.class).newInstance(args);
        }
        throw new ClassNotFoundException();
    }

    public abstract SendMessage execute(AbsSender sender, User user);
}