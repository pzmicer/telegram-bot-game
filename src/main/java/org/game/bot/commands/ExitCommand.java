package org.game.bot.commands;

import org.game.bot.exceptions.InvalidCommandFormatException;
import org.game.bot.models.Room;
import org.game.bot.service.ReplyMessageService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.ArrayList;
import java.util.List;


public class ExitCommand extends Command {

    public ExitCommand(String args) throws InvalidCommandFormatException {
        noArgsRequired(args);
    }

    @Override
    public List<SendMessage> execute(User user, ReplyMessageService service) {
        var entry = Room.findUser(user);
        if(entry.isEmpty()) {
            return List.of(service.getMessage(user.getId(), "notInRoomException"));
        }
        Room.rooms.get(entry.get().getKey()).removeUser(user);
        if (Room.rooms.get(entry.get().getKey()).getUsers().size() == 0) {
            Room.rooms.remove(entry.get().getKey());
        }
        List<SendMessage> result = new ArrayList<>();
        for (var _user : entry.get().getValue().getUsers()) {
            if (_user.equals(user))
                result.add(service.getMessage(_user.getId(), "exitPerson"));
            else
                result.add(service.getMessage(_user.getId(), "exitNotification", user.getUserName()));
        }
        return result;
    }
}
