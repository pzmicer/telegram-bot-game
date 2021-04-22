package org.game.bot.commands;

import org.game.bot.exceptions.InvalidCommandFormatException;
import org.game.bot.models.Association;
import org.game.bot.models.Room;
import org.game.bot.service.ReplyMessageService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.ArrayList;
import java.util.List;

public class MakeAssociationCommand extends Command {

    private final Association association;

    public MakeAssociationCommand(String args) throws InvalidCommandFormatException {
        argsRequired(args);
        int spaceIndex = args.indexOf(' ');
        String word = args.substring(0, spaceIndex);
        String description = args.substring(spaceIndex+1);
        this.association = new Association(word, description);
    }

    @Override
    public List<SendMessage> execute(User user, ReplyMessageService service) {
        var entry = Room.findUser(user);
        //entry.
        if (entry.isEmpty()) {
            return List.of(service.getMessage(user.getId(), "notInRoomException"));
        }
        Room room = entry.get().getValue();
        if (!room.isInGame()) {
            return List.of(service.getMessage(user.getId(), "notInGame"));
        }
        if (user.equals(room.getLeader())) {
            return List.of(service.getMessage(user.getId(), "notLeaderCommand"));
        }
        if (room.getKeyword() == null) {
            return List.of(service.getMessage(user.getId(), "nullKeywordException"));
        }
        if (association.getWord().equals(room.getKeyword())) {
            room.endGame();
            List<SendMessage> result = new ArrayList<>();
            for (var _user : room.getUsers()) {
                result.add(service.getMessage(_user.getId(), "endGame"));
            }
            return result;
        } else if (association.getWord().startsWith(room.getCurrentPrefix())) {
            room.getAssociations().put(user, association);
            List<SendMessage> result = new ArrayList<>();
            for (var _user : room.getUsers()) {
                result.add(service.getMessage(_user.getId(), "makeAssociation",
                        user.getUserName() + "("+room.getUsers().indexOf(user)+")", association.getDescription()));
            }
            return result;
        } else {
            return List.of(service.getMessage(user.getId(), "invalidAssociation"));
        }
    }
}
