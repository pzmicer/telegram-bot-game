package org.game.bot.commands;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.apache.tomcat.util.json.ParseException;
import org.game.bot.exceptions.InvalidCommandFormatException;
import org.game.bot.exceptions.NotInGameException;
import org.game.bot.exceptions.NotInRoomException;
import org.game.bot.models.Room;
import org.game.bot.service.ReplyMessageService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;


public abstract class Command {

    public abstract List<SendMessage> execute(User user, ReplyMessageService service);

    protected void noArgsRequired(String args) throws InvalidCommandFormatException {
        if (args != null)
            throw new InvalidCommandFormatException();
    }

    protected void argsRequired(String args) throws InvalidCommandFormatException {
        if (args == null)
            throw new InvalidCommandFormatException();
    }

    protected void  inRoomRequired(Optional<Map.Entry<String, Room>> entry) throws NotInRoomException {
        if (entry.isEmpty())
            throw new NotInRoomException();
    }

    protected void inGameRequired(Room room) throws NotInGameException {
        if (!room.isInGame())
            throw new NotInGameException();
    }

    protected enum COMMANDS { createroom, exit, help, join, start, guess, association, setkeyword, startgame };

    public static Command createInstance(String text) throws ParseException {
        try {
            String trimText = text.trim();
            if (trimText.startsWith("/")) {
                int spaceIndex = trimText.indexOf(" ");
                String command;
                String args = null;
                if(spaceIndex < 0) {
                    command = trimText.substring(1);
                } else {
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
                    case guess:
                        return new GuessCommand(args);
                    case association:
                        return new MakeAssociationCommand(args);
                    case setkeyword:
                        return new SetKeywordCommand(args);
                    case startgame:
                        return new StartGameCommand(args);
                }
            }
            throw new ParseException();
        } catch (Exception e) {
            throw new ParseException();
        }
    }
}