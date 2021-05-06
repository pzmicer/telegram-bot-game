package org.game.bot.models;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.game.bot.service.ReplyMessageService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class Room {

    @Getter
    private final ArrayList<User> users;

    @Getter
    private ConcurrentHashMap<User, Association> associations;

    @Getter
    private User leader;

    @Getter
    private boolean inGame;

    @Getter
    private String keyword;

    @Getter
    private int currentLetterIndex;

    @Getter @Setter
    private boolean countdown;

    private ScheduledExecutorService service;

    public void setKeyword(String keyword) {
        this.keyword = keyword;
        currentLetterIndex = 0;
    }

    public Room() {
        this.users = new ArrayList<>();
        this.inGame = false;
    }

    public void startGame() {
        inGame = true;
        leader = users.get(new Random().nextInt(users.size()));
        associations = new ConcurrentHashMap<>();
    }

    public void endGame() {
        inGame = false;
        leader = null;
        associations = null;
        keyword = null;
    }

    public boolean addUser(User user) {
        return users.add(user);
    }

    public boolean removeUser(User user) {
        return users.remove(user);
    }

    public static final ConcurrentHashMap<String, Room> rooms = new ConcurrentHashMap<>();

    public static String createRoom() {
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

    public static Optional<Map.Entry<String, Room>> findUser(User user) {
        return rooms.entrySet().stream().filter(item -> item.getValue().getUsers().contains(user)).findFirst();
    }

    public String getCurrentPrefix() {
        return keyword.substring(0, currentLetterIndex + 1);
    }

    public String openNextLetter() {
        currentLetterIndex++;
        return getCurrentPrefix();
    }

    public void startCountdown(int repeats, String word, Association association, User associationCreator,
                               AbsSender sender, ReplyMessageService messageService, User user) {
        this.countdown = true;
        service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(new TimerTask() {
            private int rep = repeats;
            @SneakyThrows
            @Override
            public void run() {
                if (rep == repeats) {
                    for (var _user : getUsers())
                        sender.execute(messageService.getMessage(_user,"guessNotification",
                                user.getUserName(), associationCreator.getUserName()));
                }
                if (rep == 0) {
                    Room.this.countdown = false;

                    if (word.equals(association.getWord())) {
                        getAssociations().clear();
                        String newPrefix = openNextLetter();
                        if (newPrefix.equals(getKeyword())) {
                            endGame();
                        }
                        for(var _user : getUsers()) {
                            sender.execute(messageService.getMessage(_user, "playersGuessed",
                                    user.getUserName(), associationCreator.getUserName()));
                            if (!isInGame()) {
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
                    for(var user : Room.this.getUsers()) {
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

    public void stopCountdown() {
        service.shutdown();
        this.countdown = false;
    }
}
