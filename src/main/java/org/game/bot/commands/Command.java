package org.game.bot.commands;

import org.game.bot.models.Room;
import org.game.bot.service.ReplyMessageService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.List;
import java.util.Optional;


public abstract class Command {

    protected ReplyMessageService service;

    public Command(ReplyMessageService service) {
        this.service = service;
    }

    public abstract List<SendMessage> execute(User user, String args);

    protected Optional<List<SendMessage>> noArgsRequired(User user, String args) {
        if (args != null)
            return Optional.of(List.of(service.getMessage(user, "invalidArgs")));
        return Optional.empty();
    }

    protected Optional<List<SendMessage>> argsRequired(User user, String args) {
        if (args == null)
            return Optional.of(List.of(service.getMessage(user, "invalidArgs")));
        return Optional.empty();
    }

    protected Optional<List<SendMessage>> inGameRequired(User user, Room room) {
        if (!room.isInGame())
            return Optional.of(List.of(service.getMessage(user, "notInGame")));
        return Optional.empty();
    }

    protected Optional<List<SendMessage>> notInGameRequired(User user, Room room) {
        if (room.isInGame())
            return Optional.of(List.of(service.getMessage(user, "inGame")));
        return Optional.empty();
    }

    protected Optional<List<SendMessage>> notLeaderRequired(User user, Room room) {
        if (user.equals(room.getLeader()))
            return Optional.of(List.of(service.getMessage(user, "leaderException")));
        return Optional.empty();
    }

    protected Optional<List<SendMessage>> leaderRequired(User user, Room room) {
        if (!user.equals(room.getLeader()))
            return Optional.of(List.of(service.getMessage(user, "notLeaderException")));
        return Optional.empty();
    }

    protected Optional<List<SendMessage>> checkCountdown(User user, Room room) {
        if (room.isCountdown())
            return Optional.of(List.of(service.getMessage(user, "countdownException")));
        return Optional.empty();
    }
}