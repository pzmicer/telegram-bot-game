package org.game.bot.commands;

import org.game.bot.models.Association;
import org.game.bot.models.Room;
import org.game.bot.service.ReplyMessageService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GuessCommand extends Command {

    private String word;

    private int index;

    public GuessCommand(String args) {
        argsRequired(args);
        String[] argsArray = args.split("\\s+");
        this.index = Integer.parseInt(argsArray[0]);
        this.word = argsArray[1];
    }

    @Override
    public List<SendMessage> execute(User user, ReplyMessageService service) {
        var rem = Room.findUser(user);
        if (rem.isEmpty()) {
            return List.of(service.getMessage(user.getId(), "roomException"));
        }
        Room room = rem.get().getValue();
        if (!room.isInGame()) {
            return List.of(service.getMessage(user.getId(), "notInGame"));
        }
        var associationEntry = room.getAssociations().entrySet().stream()
                .filter(entry -> entry.getKey().equals(room.getUsers().get(index))).findFirst();
        if (associationEntry.isEmpty()) {
            return List.of(service.getMessage(user.getId(), "associationException"));
        }
        Association association = associationEntry.get().getValue();
        User associationCreator = associationEntry.get().getKey();
        List<SendMessage> result = new ArrayList<>();
        if (user.equals(room.getLeader())) {
            if (word.equals(association.getWord())) {
                room.getAssociations().remove(associationEntry.get().getKey());
                for(var _user : room.getUsers()) {
                    result.add(service.getMessage(_user.getId(), "leaderGuessed"));
                }
            } else {
                return List.of(service.getMessage(user.getId(), "guessFailure"));
            }
        } else {
            if (word.equals(association.getWord())) {
                room.getAssociations().remove(associationCreator);
                for(var _user : room.getUsers()) {
                    result.add(service.getMessage(_user.getId(), "playersGuessed", user.getUserName(), associationCreator.getUserName()));
                    result.add(service.getMessage(_user.getId(), "currentWord", room.openNextLetter()));
                }
            } else {
                return List.of(service.getMessage(user.getId(), "guessFailure"));
            }
        }
        return result;
    }
}
