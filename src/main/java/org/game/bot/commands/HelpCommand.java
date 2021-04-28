package org.game.bot.commands;

import org.game.bot.service.ReplyMessageService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.List;


public class HelpCommand extends Command {

    public HelpCommand(ReplyMessageService service) {
        super(service);
    }

    @Override
    public List<SendMessage> execute(User user, String args) {
        StringBuilder builder = new StringBuilder();
//        for(var entry : COMMANDS.values())
//            builder.append("/").append(entry.name()).append("\n");
        return List.of(new SendMessage(user.getId().toString(), builder.toString()));
    }
}