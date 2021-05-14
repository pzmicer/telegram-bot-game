package org.game.bot.commands;

import org.game.bot.models.Room;
import org.game.bot.service.ReplyMessageService;
import org.game.bot.service.RoomService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.ArrayList;
import java.util.List;

public class ShowRoomCommand extends Command {

    public ShowRoomCommand(ReplyMessageService service, RoomService roomService) {
        super(service, roomService);
    }

    @Override
    public List<SendMessage> execute(User user, String args) {
        return noArgsRequired(user, args)
            .orElseGet(() -> roomService.findUser(user)
                .map(entry -> proceed(entry.getValue()))
            .orElseGet(() -> List.of(service.getMessage(user, "notInRoomException"))));
    }

    public List<SendMessage> proceed(Room room) {
        List<SendMessage> result  = new ArrayList<>();
        StringBuilder roomUsers = new StringBuilder();
        for(int i = 0; i < room.getUsers().size(); i++) {
            User user = room.getUsers().get(i);
            roomUsers
                    .append(user.getUserName()).append(" ")
                    .append(user.getFirstName()).append(" ")
                    .append(user.getLastName()).append("\n");
        }
        for(var _user : room.getUsers()) {
            result.add(new SendMessage(_user.getId().toString(), roomUsers.toString()));
        }
        return result;
    }
}
