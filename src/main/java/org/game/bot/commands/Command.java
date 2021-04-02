package org.game.bot.commands;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.json.ParseException;
import org.atteo.classindex.ClassIndex;
import org.game.bot.service.ReplyMessageService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public abstract class Command {

    protected String args;

    public abstract List<SendMessage> execute(User user, ReplyMessageService service);

    protected static Map<String, Class<?>> commands = new ConcurrentHashMap<>(){{
        put("createroom", CreateRoomCommand.class);
        put("exit", ExitCommand.class);
        put("help", HelpCommand.class);
        put("join", JoinCommand.class);
        put("start", StartCommand.class);
    }};

    //PARSE SECTION

    public static Command parseCommand(String text) throws ParseException {
        if (text == null)
            throw new ParseException();
        String trimText = text.trim();
        if (trimText.startsWith("/")) {
            int spaceIndex = trimText.indexOf(" ");
            String command;
            String args = "";
            if(spaceIndex < 0)
                command = trimText.substring(1);
            else {
                command = trimText.substring(1, spaceIndex);
                args = trimText.substring(spaceIndex + 1);
            }
            try {
                return (Command) commands.get(command).getConstructor(String.class).newInstance(args);
            } catch (Exception e) {
                throw new ParseException();
            }
        }
        throw new ParseException();
    }
}