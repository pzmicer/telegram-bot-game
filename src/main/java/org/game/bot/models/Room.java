package org.game.bot.models;

import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Getter @Setter
public class Room {
    private final ArrayList<User> users;
    private ConcurrentHashMap<User, Association> associations;
    private User leader;
    private boolean inGame;
    private String keyword;
    private int currentLetterIndex;
    private boolean countdown;

    public void setKeyword(String keyword) {
        this.keyword = keyword;
        currentLetterIndex = 0;
    }

    public Room() {
        this.users = new ArrayList<>();
        this.inGame = false;
    }
}
