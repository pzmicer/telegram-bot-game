package org.game.bot.commands;

import org.apache.tomcat.util.json.ParseException;
import org.game.bot.service.ReplyMessageService;
import org.game.bot.service.RoomService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class CommandHandler {

    private static HashMap<String, Command> handlers;

    public CommandHandler(ReplyMessageService service, RoomService roomService, AbsSender sender) {
        handlers = new HashMap<>() {{
            put("createroom", new CreateRoomCommand(service, roomService));
            put("exit", new ExitCommand(service, roomService));
            put("guess", new GuessCommand(service, roomService, sender));
            put("help", new HelpCommand(service, roomService));
            put("join", new JoinCommand(service, roomService));
            put("association", new MakeAssociationCommand(service, roomService));
            put("setkeyword", new SetKeywordCommand(service, roomService));
            put("start", new StartCommand(service, roomService));
            put("startgame", new StartGameCommand(service, roomService));
            put("all", new ChatCommand(service, roomService));
            put("show", new ShowRoomCommand(service, roomService));
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
                return handlers.get(command).execute(user, args);
            }
            throw new ParseException();
        } catch (Exception e) {
            throw new ParseException();
        }
    }

    public static Set<String> getCommands() {
        return handlers.keySet();
    }
}
