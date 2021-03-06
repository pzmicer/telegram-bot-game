package org.game.bot.commands;

import lombok.extern.slf4j.Slf4j;
import org.game.bot.models.Association;
import org.game.bot.models.Room;
import org.game.bot.service.ReplyMessageService;
import org.game.bot.service.RoomService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Slf4j
public class GuessCommand extends Command {

    private String word;

    private int index;

    private AbsSender sender;

    public GuessCommand(ReplyMessageService service, RoomService roomService, AbsSender sender) {
        super(service, roomService);
        this.sender = sender;
    }

    @Override
    public List<SendMessage> execute(User user, String args) {
        log.info("After handle " + user.getUserName());
        return argsRequired(user, args)
            .orElseGet(() -> roomService.findUser(user)
            .map(entry -> inGameRequired(user, entry.getValue())
            .orElseGet(() -> proceed(user, entry.getValue())))
            .orElseGet(() -> List.of(service.getMessage(user, "notInRoomException"))));
    }

    @Override
    protected Optional<List<SendMessage>> argsRequired(User user, String args) {
        try {
            String[] argsArray = args.split("\\s+");
            this.index = Integer.parseInt(argsArray[0]);
            this.word = argsArray[1];
            return Optional.empty();
        } catch (Exception e) {
            return Optional.of(List.of(service.getMessage(user, "invalidArgs")));
        }
    }

    private List<SendMessage> proceed(User user, Room room) {
        if (index > room.getUsers().size() || index < 0) {
            return List.of(service.getMessage(user, "userNotFoundException"));
        }
        var associationEntry = findAssociation(room);
        if (associationEntry.isEmpty()) {
            return List.of(service.getMessage(user, "associationException"));
        }
        Association association = associationEntry.get().getValue();
        User associationCreator = associationEntry.get().getKey();
        List<SendMessage> result = new ArrayList<>();
        if (user.equals(room.getLeader())) {
            if (word.equals(association.getWord())) {
                room.getAssociations().remove(associationCreator);
                roomService.stopCountdown(room);
                for(var _user : room.getUsers()) {
                    result.add(service.getMessage(_user, "leaderGuessed", word));
                    result.add(service.getMessage(_user, "currentWord", roomService.getCurrentPrefix(room)));
                }
            } else {
                return List.of(service.getMessage(user, "guessFailure"));
            }
        } else if (!user.equals(associationCreator)) {
            if (!room.isCountdown()) {
                roomService.startCountdown(room, 20, user, word, association, associationCreator, sender, service);
            } else {
                return List.of(service.getMessage(user, "countdownException"));
            }
        } else {
            return List.of(service.getMessage(user, "authorException"));
        }
        return result;
    }

    private Optional<Map.Entry<User, Association>> findAssociation(Room room) {
        return room.getAssociations().entrySet().stream()
                .filter(item -> item.getKey().equals(room.getUsers().get(index))).findFirst();
    }
}
