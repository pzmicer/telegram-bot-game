package org.game.bot.commands;

import org.atteo.classindex.ClassIndex;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

@BotCommand(name="help")
public class HelpCommand extends Command {

    public HelpCommand(String args) {
        super(args);
    }

    @Override
    public SendMessage execute(AbsSender sender, User user) {
        StringBuilder helpMessage = new StringBuilder();
        for(var klass : ClassIndex.getAnnotated(BotCommand.class)) {
            helpMessage.append("/").append(klass.getAnnotation(BotCommand.class).name()).append("\n");
        }
        return new SendMessage(user.getId().toString(), helpMessage.toString());
    }
}
