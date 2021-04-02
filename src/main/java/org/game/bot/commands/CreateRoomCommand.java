package org.game.bot.commands;

import org.game.bot.Rooms;
import org.game.bot.service.ReplyMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

@BotCommand(name="createroom")
public class CreateRoomCommand extends Command {

    public CreateRoomCommand(String args) {
        super(args);
    }

    @Override
    public SendMessage execute(AbsSender sender, User user) {
        Long chatID = user.getId();
        if (Rooms.checkUser(chatID) != -1) {
            return ReplyMessageService.getReplyMessage(chatID, "createRoomException");
        }
        int roomID = Rooms.addRoom();
        Rooms.addUser(roomID, chatID);
        return ReplyMessageService.getReplyMessage(chatID, "joinPerson", roomID);
    }
}