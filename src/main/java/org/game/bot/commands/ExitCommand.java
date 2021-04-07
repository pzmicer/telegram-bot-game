package org.game.bot.commands;

import org.game.bot.Room;
import org.game.bot.service.ReplyMessageService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.List;


public class ExitCommand extends Command {

    public ExitCommand(String args) {
        super(args);
    }

    @Override
    public List<SendMessage> execute(User user, ReplyMessageService service) {
        var entry = Room.checkUser(user);
        if(entry.isEmpty()) {
            return List.of(service.getReplyMessage(user.getId(), "exitException"));
        }
        Room.rooms.get(entry.get().getKey()).removeUser(user);
        if (Room.rooms.get(entry.get().getKey()).getUsers().size() == 0){
            Room.rooms.remove(entry.get().getKey());
        }
        return List.of(service.getReplyMessage(user.getId(), "exitPerson"));
    }
}
