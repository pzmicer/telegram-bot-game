package org.game.bot.commands;

import org.game.bot.models.Room;
import org.game.bot.service.ReplyMessageService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.ArrayList;
import java.util.List;


public class JoinCommand extends Command {

    private final String roomID;

    public JoinCommand(String args) {
        argsRequired(args);
        this.roomID = args;
    }

    @Override
    public List<SendMessage> execute(User user, ReplyMessageService service) {
        var messages = new ArrayList<SendMessage>();
        if (Room.findUser(user).isPresent()) {
            return List.of(service.getMessage(user.getId(), "joinException"));
        }
        try {
            if (!Room.rooms.containsKey(roomID))
                return List.of(service.getMessage(user.getId(), "invalidArgs"));
            for(var id : Room.rooms.get(roomID).getUsers()) {
                messages.add(service.getMessage(id.getId(), "joinNotification", user.getUserName()));
            }
            Room.rooms.get(roomID).addUser(user);
            messages.add(service.getMessage(user.getId(), "joinPerson", roomID));
            return messages;
        } catch (NumberFormatException e) {
            return List.of(service.getMessage(user.getId(), "invalidArgs"));
        }
    }
}