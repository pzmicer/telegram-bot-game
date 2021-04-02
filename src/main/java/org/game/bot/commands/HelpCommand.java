package org.game.bot.commands;

import org.atteo.classindex.ClassIndex;
import org.game.bot.service.ReplyMessageService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.List;

@BotCommand(name="help")
public class HelpCommand extends Command {

    public HelpCommand(String args) {
        super(args);
    }

    @Override
    public List<SendMessage> execute(User user, ReplyMessageService service) {
        StringBuilder builder = new StringBuilder();
        for(var entry : commands.entrySet())
            builder.append("/").append(entry.getKey()).append("\n");
        return List.of(new SendMessage(user.getId().toString(), builder.toString()));
    }
}