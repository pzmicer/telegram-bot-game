package org.game.bot.service;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.game.bot.models.Association;
import org.game.bot.models.Room;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
        return room.getCurrentPrefix();
    }

    public void startCountdown(
            Room room,
            int repeats,
            String word,
            Association association,
            User associationCreator,
            AbsSender sender,
            ReplyMessageService messageService,
            User user
    ) {
        room.setCountdown(true);
        service.scheduleAtFixedRate(new Runnable() {
            private int rep = repeats;
            @SneakyThrows
            @Override
            public void run() {
                if (rep == repeats) {
                    for (var _user : room.getUsers())
                        sender.execute(messageService.getMessage(_user,"guessNotification",
                                user.getUserName(), associationCreator.getUserName()));
                }
                if (rep == 0) {
                    room.setCountdown(false);

                    if (word.equals(association.getWord())) {
                        room.getAssociations().clear();
                        String newPrefix = room.openNextLetter();
                        if (newPrefix.equals(room.getKeyword())) {
                            RoomService.this.endGame(room);
                        }
                        for(var _user : room.getUsers()) {
                            sender.execute(messageService.getMessage(_user, "playersGuessed",
                                    user.getUserName(), associationCreator.getUserName()));
                            if (!room.isInGame()) {
                                sender.execute(messageService.getMessage(_user, "endGame"));
                            }
                            else {
                                sender.execute(messageService.getMessage(_user, "currentWord", newPrefix));
                            }
                        }
                    } else {
                        sender.execute(messageService.getMessage(user, "guessFailure"));
                    }

                    service.shutdown();
                } else {
                    for(var user : room.getUsers()) {
                        try {
                            if (sender != null)
                                sender.execute(new SendMessage(user.getId().toString(), Integer.toString(rep)));
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    }
                    rep--;
                }
            }
        }, 0L, 1000L, TimeUnit.MILLISECONDS);
    }

    /*public void stopCountdown(Room room) {
        service.shutdown();
        this.countdown = false;
    }*/
}
