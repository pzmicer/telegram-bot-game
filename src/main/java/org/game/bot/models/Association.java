package org.game.bot.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Association {

    private final String word;

    private final String description;

    @Setter
    private boolean guessedByLeader = false;

    public Association(String word, String description) {
        this.word = word;
        this.description = description;
    }
}
