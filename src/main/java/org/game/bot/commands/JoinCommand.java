package org.game.bot.commands;

import lombok.extern.slf4j.Slf4j;
import org.game.bot.Room;
import org.game.bot.service.ReplyMessageService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.ArrayList;
import java.util.List;

@BotCommand(name="join")
@Slf4j
public class JoinCommand extends Command {
    public JoinCommand(String args) {
        super(args);
    }

    @Override
    public List<SendMessage> execute(User user, ReplyMessageService service) {
        var messages = new ArrayList<SendMessage>();
        if (Room.checkUser(user).isPresent()) {
            return List.of(service.getReplyMessage(user.getId(), "joinException"));
        }
        try {
            if (!Room.rooms.containsKey(args))
                return List.of(service.getReplyMessage(user.getId(), "invalidArgs"));
            for(var id : Room.rooms.get(args).getUsers()) {
                messages.add(service.getReplyMessage(id.getId(), "joinNotification", user.getUserName()));
            }
            Room.rooms.get(args).addUser(user);
            messages.add(service.getReplyMessage(user.getId(), "joinPerson", args));
            return messages;
        } catch (NumberFormatException e) {
            return List.of(service.getReplyMessage(user.getId(), "invalidArgs"));
        }
    }
}