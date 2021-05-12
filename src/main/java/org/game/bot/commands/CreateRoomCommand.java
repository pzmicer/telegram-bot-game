package org.game.bot.commands;

import org.game.bot.models.Room;
import org.game.bot.service.ReplyMessageService;
import org.game.bot.service.RoomService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.List;


public class CreateRoomCommand extends Command {

    public CreateRoomCommand(ReplyMessageService service, RoomService roomService) {
        super(service, roomService);
    }

    private List<SendMessage> proceed(User user) {
        String roomID = roomService.createRoom();
        roomService.addUser(roomService.rooms.get(roomID), user);
        return List.of(service.getMessage(user, "joinPerson", roomID));
    }

    @Override
    public List<SendMessage> execute(User user, String args) {
        return noArgsRequired(user, args)
            .orElseGet(() -> roomService.findUser(user)
                .map(entry -> List.of(service.getMessage(user, "inRoomException")))
                .orElseGet(() -> proceed(user)));
    }
}