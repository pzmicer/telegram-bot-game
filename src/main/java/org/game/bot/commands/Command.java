package org.game.bot.commands;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.apache.tomcat.util.json.ParseException;
import org.game.bot.exceptions.InvalidCommandFormatException;
import org.game.bot.exceptions.NotInGameException;
import org.game.bot.exceptions.NotInRoomException;
import org.game.bot.models.Room;
import org.game.bot.service.ReplyMessageService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;


public abstract class Command {

    protected ReplyMessageService service;

    public Command(ReplyMessageService service) {
        this.service = service;
    }

    public abstract List<SendMessage> execute(String args, User user);

    protected void noArgsRequired(String args) {
        if (args != null) {

        }
    }

    protected void argsRequired(String args) {
        if (args == null) {

        }
    }

    protected void inGameRequired(Room room) throws NotInGameException {
        if (!room.isInGame())
            throw new NotInGameException();
    }

    protected enum COMMANDS { createroom, exit, help, join, start, guess, association, setkeyword, startgame }
}