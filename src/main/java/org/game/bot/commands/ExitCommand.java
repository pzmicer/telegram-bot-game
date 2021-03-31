package org.game.bot.commands;

import org.game.bot.Rooms;
import org.game.bot.service.ReplyMessageService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

@BotCommand(name= "exit")
public class ExitCommand extends Command {
    public ExitCommand(String args) {
        super(args);
    }

    @Override
    public SendMessage execute(AbsSender sender, User user) {
        int roomID = Rooms.checkUser(user.getId());
        if(roomID == -1) {
            return ReplyMessageService.getReplyMessage(user.getId(), "exitException");
        }
        Rooms.deleteUser(roomID, user.getId());
        return ReplyMessageService.getReplyMessage(user.getId(), "exitPerson");
    }
}
