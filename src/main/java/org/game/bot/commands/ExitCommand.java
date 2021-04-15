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
        Room room = entry.get().getValue();
        room.removeUser(user);
        List<SendMessage> result = new ArrayList<>();
        result.add(service.getMessage(user.getId(), "exitPerson"));
        if (room.getUsers().size() == 0) {
            Room.rooms.remove(entry.get().getKey());
        } else {
            for (var _user : room.getUsers()) {
                result.add(service.getMessage(_user.getId(), "exitNotification", user.getUserName()));
            }
        }
        if (room.getUsers().size() < 3) {
            for (var _user : room.getUsers()) {
                result.add(service.getMessage(_user.getId(), "notEnoughUsersException", user.getUserName()));
            }
            if (room.isInGame()) {
                room.endGame();
                for (var _user : room.getUsers()) {
                    result.add(service.getMessage(_user.getId(), "endGame"));
                }
            }
        }
        return result;
    }
}
