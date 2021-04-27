package org.game.bot.commands;

import org.game.bot.models.Room;
import org.game.bot.service.ReplyMessageService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ExitCommand extends Command {

    public ExitCommand(ReplyMessageService service) {
        super(service);
    }

    @Override
    public List<SendMessage> execute(User user, String args) {
        return noArgsRequired(user, args)
            .orElseGet(() -> Room.findUser(user)
                .map(entry -> proceed(user, entry))
                .orElseGet(() -> List.of(service.getMessage(user, "notInRoomException"))));
    }

    private List<SendMessage> proceed(User user, Map.Entry<String, Room> entry) {
        Room room = entry.getValue();
        room.removeUser(user);
        List<SendMessage> result = new ArrayList<>();
        result.add(service.getMessage(user, "exitPerson"));
        if (room.getUsers().size() == 0) {
            Room.rooms.remove(entry.getKey());
        } else {
            for (var _user : room.getUsers()) {
                result.add(service.getMessage(_user, "exitNotification", user.getUserName()));
            }
        }
        if (room.getUsers().size() < 3) {
            for (var _user : room.getUsers()) {
                result.add(service.getMessage(_user, "notEnoughUsersException", user.getUserName()));
            }
            if (room.isInGame()) {
                room.endGame();
                for (var _user : room.getUsers()) {
                    result.add(service.getMessage(_user, "endGame"));
                }
            }
        }
        return result;
    }
}
