package org.game.bot.database.models;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class Game {
    @Id
    private Long id;
    private Integer leaderId;
    private String keyword;
    private Integer associationsGuessed;
}
