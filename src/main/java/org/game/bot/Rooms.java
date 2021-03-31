package org.game.bot;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class Rooms {

    private static final ConcurrentHashMap<Integer, ArrayList<Long>> rooms = new ConcurrentHashMap<>();
    private static int ID = 0;

    public static int addRoom() {
        int id = ++ID;
        rooms.put(id, new ArrayList<>());
        return id;
    }

    public static void deleteRoom(int id) {
        rooms.remove(id);
    }

    /**
     * @param userID user's ID
     * @return room id, otherwise -1
     */
    public static int checkUser(Long userID) {
        var result =
                rooms.entrySet().stream().filter(item -> item.getValue().contains(userID)).findFirst();
        return result.isPresent() ? result.get().getKey() : -1;
    }

    public static boolean checkRoom(Integer roomID) {
        return rooms.containsKey(roomID);
    }

    public static boolean addUser(Integer roomID, Long userID) {
        return rooms.get(roomID).add(userID);
    }

    public static boolean deleteUser(Integer roomID, Long userID) {
        return rooms.get(roomID).remove(userID);
    }

    public static ArrayList<Long> getRoom(int roomID) {
        return rooms.get(roomID);
    }
}
