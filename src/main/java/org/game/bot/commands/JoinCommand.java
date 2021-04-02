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
        Long chatID = user.getId();
        var messages = new ArrayList<SendMessage>();
        if (Room.checkUser(chatID).isPresent()) {
            return List.of(service.getReplyMessage(chatID, "joinException"));
        }
        try {
            if (!Room.rooms.containsKey(args))
                return List.of(service.getReplyMessage(chatID, "invalidArgs"));
            for(var id : Room.rooms.get(args).getUsers()) {
                messages.add(service.getReplyMessage(id, "joinNotification", user.getUserName()));
            }
            Room.rooms.get(args).addUser(chatID);
            messages.add(service.getReplyMessage(chatID, "joinPerson", args));
            return messages;
        } catch (NumberFormatException e) {
            return List.of(service.getReplyMessage(chatID, "invalidArgs"));
        }
    }
}