package org.game.bot.commands;

import org.apache.tomcat.util.json.ParseException;
import org.game.bot.service.ReplyMessageService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.HashMap;
import java.util.List;

public class CommandHandler {

    private final ReplyMessageService service;

    private final HashMap<String, Command> handlers;

    public CommandHandler(ReplyMessageService service) {
        this.service = service;
        this.handlers = new HashMap<>() {{
            put("start", new StartGameCommand(service));
        }};
    }

    public List<SendMessage> handle(String text, User user) throws ParseException {
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
                return handlers.get(command).execute(args, user);
                /*switch (Command.COMMANDS.valueOf(command)) {
                    case createroom:
                        return roomHandler.createRoom(args);
                    case exit:
                        return roomHandler.exitRoom(args);
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
                }*/
            }
            throw new ParseException();
        } catch (Exception e) {
            throw new ParseException();
        }
    }
}
