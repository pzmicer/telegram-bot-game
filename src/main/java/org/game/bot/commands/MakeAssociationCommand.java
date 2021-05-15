package org.game.bot.commands;

import org.game.bot.models.Association;
import org.game.bot.models.Room;
import org.game.bot.service.ReplyMessageService;
import org.game.bot.service.RoomService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MakeAssociationCommand extends Command {

    private Association association;

    public MakeAssociationCommand(ReplyMessageService service, RoomService roomService) {
        super(service, roomService);
    }

    @Override
    public List<SendMessage> execute(User user, String args) {
        return argsRequired(user, args)
            .orElseGet(() -> roomService.findUser(user)
            .map(entry -> inGameRequired(user, entry.getValue())
                .or(() -> checkCountdown(user, entry.getValue()))
                .or(() -> notLeaderRequired(user, entry.getValue()))
                .orElseGet(() -> proceed(user, entry.getValue())))
            .orElseGet(() -> List.of(service.getMessage(user, "notInRoomException"))));
    }

    @Override
    protected Optional<List<SendMessage>> argsRequired(User user, String args) {
        if (args == null)
            return Optional.of(List.of(service.getMessage(user, "invalidArgs")));
        int spaceIndex = args.indexOf(' ');
        String word = args.substring(0, spaceIndex);
        String description = args.substring(spaceIndex+1);
        this.association = new Association(word, description);
        return Optional.empty();
    }

    private List<SendMessage> proceed(User user, Room room) {
        if (room.getKeyword() == null) {
            return List.of(service.getMessage(user, "nullKeywordException"));
        }
        if (association.getWord().equals(room.getKeyword())) {
            roomService.endGame(room);
            List<SendMessage> result = new ArrayList<>();
            for (var _user : room.getUsers()) {
                result.add(service.getMessage(_user, "keywordGuessed",
                        roomService.getValidName(user), association.getWord()));
                result.add(service.getMessage(_user, "endGame"));
            }
            return result;
        } else if (association.getWord().startsWith(roomService.getCurrentPrefix(room))) {
            room.getAssociations().put(user, association);
            List<SendMessage> result = new ArrayList<>();
            for (var _user : room.getUsers()) {
                result.add(service.getMessage(_user, "makeAssociation",
                        roomService.getValidName(user) + " ("+room.getUsers().indexOf(user)+")",
                        association.getDescription()));
            }
            return result;
        } else {
            return List.of(service.getMessage(user, "invalidAssociation"));
        }
    }
}
