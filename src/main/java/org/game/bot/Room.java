package org.game.bot;

import lombok.Getter;

import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class Room {

    @Getter
    private ArrayList<Long> users;
    @Getter
    Long leader;
    boolean inGame;

    public Room() {
        this.users = new ArrayList<>();
        this.inGame = false;
    }

    public void startGame() {
        inGame = true;
        leader = users.get(new Random().nextInt(users.size()));
    }

    public boolean addUser(Long userID) {
        return users.add(userID);
    }

    public boolean removeUser(Long userID) {
        return users.remove(userID);
    }


    public static final ConcurrentHashMap<String, Room> rooms = new ConcurrentHashMap<>();

    public static String createRoom() {
        SecureRandom random = new SecureRandom();
        byte[] seed = random.generateSeed(10);
        String id = Arrays.toString(seed);
        rooms.put(id, new Room());
        return id;
    }

    public static Optional<Map.Entry<String, Room>> checkUser(Long userID) {
        return rooms.entrySet().stream().filter(item -> item.getValue().getUsers().contains(userID)).findFirst();
    }
}
