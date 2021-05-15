package org.game.bot.database.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "players")
public class Player {

    @Id
    @Column(name = "player_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter
    @Getter
    private Integer id;

    @Column(name = "name",length = 20, nullable = false, unique = true)
    @Setter
    @Getter
    private String name;

    @Column(name = "games_finished")
    @Setter
    @Getter
    private Integer gamesFinished = 1;
}
