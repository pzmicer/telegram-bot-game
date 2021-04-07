package org.game.bot.commands;

import org.apache.tomcat.util.json.ParseException;
import org.game.bot.service.ReplyMessageService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.List;


public abstract class Command {

    public abstract List<SendMessage> execute(User user, ReplyMessageService service);

    protected Command(String args) {
        if (args != null)
            throw new IllegalArgumentException();
    }

    protected Command() {

    }

    protected enum COMMANDS { createroom, exit, help, join, start };

    public static Command parseCommand(String text) throws ParseException {
        try {
            String trimText = text.trim();
            if (trimText.startsWith("/")) {
                int spaceIndex = trimText.indexOf(" ");
                String command;
                String args = null;
                if(spaceIndex < 0)
                    command = trimText.substring(1);
                else {
                    command = trimText.substring(1, spaceIndex);
                    args = trimText.substring(spaceIndex + 1);
                }
                switch (COMMANDS.valueOf(command)) {
                    case createroom:
                        return new CreateRoomCommand(args);
                    case exit:
                        return new ExitCommand(args);
                    case help:
                        return new HelpCommand(args);
                    case join:
                        return new JoinCommand(args);
                    case start:
                        return new StartCommand(args);
                }
            }
            throw new ParseException();
        } catch (Exception e) {
            throw new ParseException();
        }
    }
}