package org.game.bot.commands;

import org.game.bot.models.Room;
import org.game.bot.service.ReplyMessageService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class JoinCommand extends Command {

    private String roomID;

    public JoinCommand(ReplyMessageService service) {
        super(service);
    }

    @Override
    public List<SendMessage> execute(User user, String args) {
        return argsRequired(user, args)
            .orElseGet(() -> Room.findUser(user)
            .map(entry -> List.of(service.getMessage(user, "inRoomException")))
            .orElseGet(() -> proceed(user)));
    }

    @Override
    protected Optional<List<SendMessage>> argsRequired(User user, String args) {
        this.roomID = args;
        return super.argsRequired(user, args);
    }

    //NumberFormatException???
    private List<SendMessage> proceed(User user) {
        if (!Room.rooms.containsKey(roomID)) {
            return List.of(service.getMessage(user, "invalidRoomID"));
        }
        var result = new ArrayList<SendMessage>();
        for(var _user : Room.rooms.get(roomID).getUsers()) {
            result.add(service.getMessage(_user, "joinNotification", user.getUserName()));
        }
        Room.rooms.get(roomID).addUser(user);
        result.add(service.getMessage(user, "joinPerson", roomID));
        return result;
    }
}