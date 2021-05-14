package org.game.bot.database.models;

import jdk.jfr.Enabled;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class Player {
    @Id
    private Long id;
    @ColumnDefault("1")
    private Integer gamesFinished;
    private String username;
}
