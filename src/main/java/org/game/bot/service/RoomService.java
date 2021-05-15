package org.game.bot.service;

import lombok.AllArgsConstructor;
import org.game.bot.models.Association;
import org.game.bot.models.Room;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.*;

@AllArgsConstructor
public class RoomService {
    private final ScheduledExecutorService service;

    public void startGame(Room room) {
        room.setInGame(true);
        room.setLeader(room.getUsers().get(new Random().nextInt(room.getUsers().size())));
        room.setAssociations(new ConcurrentHashMap<>());
    }

    public void endGame(Room room) {
        room.setInGame(false);
        room.setLeader(null);
        room.setAssociations(null);
        room.setKeyword(null);
    }

    public void addUser(Room room, User user) {
        room.getUsers().add(user);
    }

    public void removeUser(Room room, User user) {
        room.getUsers().remove(user);
    }

    public final ConcurrentHashMap<String, Room> rooms = new ConcurrentHashMap<>();

    public String createRoom() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();
        String id = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        rooms.put(id, new Room());
        return id;
    }

    public Optional<Map.Entry<String, Room>> findUser(User user) {
        return rooms.entrySet().stream().filter(item -> item.getValue().getUsers().contains(user)).findFirst();
    }

    public String getCurrentPrefix(Room room) {
        return room.getKeyword().substring(0, room.getCurrentLetterIndex() + 1);
    }

    public String openNextLetter(Room room) {
        room.setCurrentLetterIndex(room.getCurrentLetterIndex() + 1);
        return getCurrentPrefix(room);
    }

    private final HashMap<Room, ScheduledFuture<?>> tasks = new HashMap<>();

    public void startCountdown(
            Room room,
            int repeats,
            User user,
            String word,
            Association association,
            User associationCreator,
            AbsSender sender,
            ReplyMessageService messageService
    ) {
        room.setCountdown(true);
        var task = new CountdownTask(
                room,
                repeats,
                user,
                word,
                association,
                associationCreator,
                sender,
                messageService,
                this
        ).start(service, 1000L, TimeUnit.MILLISECONDS);
        tasks.put(room, task);
    }

    public void stopCountdown(Room room) {
        var task = tasks.get(room);
        if (!task.isDone()) {
            task.cancel(false);
            room.setCountdown(false);
        }
    }

    public String getValidName(User user) {
        if (user.getUserName() != null)
            return user.getUserName();
        if (user.getLastName() != null)
            return user.getFirstName() + " " + user.getLastName();
        return user.getFirstName();
    }
}