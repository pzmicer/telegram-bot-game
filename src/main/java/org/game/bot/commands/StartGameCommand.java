package org.game.bot.commands;

import org.game.bot.exceptions.InvalidCommandFormatException;
import org.game.bot.models.Room;
import org.game.bot.service.ReplyMessageService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class StartGameCommand extends Command {
    protected StartGameCommand(ReplyMessageService service) {
        super(service);
    }

    /*public StartGameCommand(String args) {
        noArgsRequired(args);
    }*/

    @Override
    public List<SendMessage> execute(String args, User user) {
        /*Optional<List<SendMessage>> result2 = checkRoom()
                .or(checkAmount)
                .or(checkInGame)
                .or(proceed);*/
        var entry = Room.findUser(user);
        if (entry.isEmpty()) {
            return List.of(service.getMessage(user.getId(), "notInRoomException"));
        } else {
            var realEntry = entry.get();
            //return test(realEntry).or(() -> test2(realEntry)).get();
        }
        Room room = entry.get().getValue();
        if (room.getUsers().size() < 2) {
            return List.of(service.getMessage(user.getId(), "notEnoughUsersException"));
        }
        if (room.isInGame()) {
            return List.of(service.getMessage(user.getId(), "inGame"));
        }
        room.startGame();
        List<SendMessage> result = new ArrayList<>();
        for (var _user : room.getUsers()) {
            if (!room.getLeader().equals(_user))
                result.add(service.getMessage(_user.getId(), "startGameNotification", room.getLeader().getUserName()));
            else
                result.add(service.getMessage(_user.getId(), "leaderNotification"));
        }
        return result;
    }

    private Optional<List<SendMessage>> test(Map.Entry<String, Room> entry) {
        return Optional.empty();
    }

    private Optional<List<SendMessage>> test2(Map.Entry<String, Room> entry) {
        return Optional.empty();
    }
}
