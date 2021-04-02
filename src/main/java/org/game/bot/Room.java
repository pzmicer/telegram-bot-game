package org.game.bot;

import lombok.Getter;
import org.telegram.telegrambots.meta.api.objects.User;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class Room {

    @Getter
    private ArrayList<User> users;
    @Getter
    User leader;

    boolean inGame;

    public Room() {
        this.users = new ArrayList<>();
        this.inGame = false;
    }

    public void startGame() {
        inGame = true;
        leader = users.get(new Random().nextInt(users.size()));
    }

    public boolean addUser(User user) {
        return users.add(user);
    }

    public boolean removeUser(User user) {
        return users.remove(user);
    }


    public static final ConcurrentHashMap<String, Room> rooms = new ConcurrentHashMap<>();

    public static String createRoom() {
        SecureRandom random = new SecureRandom();
        byte[] array = new byte[10];
        random.nextBytes(array);
        String id = new String(array, StandardCharsets.UTF_8);
        rooms.put(id, new Room());
        return id;
    }

    public static Optional<Map.Entry<String, Room>> checkUser(User user) {
        return rooms.entrySet().stream().filter(item -> item.getValue().getUsers().contains(user)).findFirst();
    }

    public ArrayList<User> getUsers() { return users; }

    public User getLeader() { return leader; }

    public boolean isInGame() { return inGame; }
}
