package org.game.bot.commands;

import org.game.bot.service.ReplyMessageService;
import org.game.bot.service.RoomService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.List;


public class HelpCommand extends Command {

    public HelpCommand(ReplyMessageService service, RoomService roomService) {
        super(service, roomService);
    }

    @Override
    public List<SendMessage> execute(User user, String args) {
        StringBuilder builder = new StringBuilder();
        for(var entry : CommandHandler.getCommands())
            builder.append("/").append(entry).append("\n");
        return List.of(new SendMessage(user.getId().toString(), builder.toString()));
    }
}
