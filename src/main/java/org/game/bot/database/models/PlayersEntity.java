package org.game.bot.database.models;

import org.hibernate.annotations.Generated;

import javax.persistence.*;

@Entity
@Table(name = "players", schema = "game_schema", catalog = "dfffjmt2tbqtpp")
public class PlayersEntity {
    private Integer playerId;
    private Integer gamesFinished;
    private String username;

    public PlayersEntity(String username) {
        this.username = username;
    }

    public PlayersEntity() {

    }

    @Id
    @Column(name = "player_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Integer playerId) {
        this.playerId = playerId;
    }

    @Basic
    @Column(name = "games_finished", nullable = true)
    public Integer getGamesFinished() {
        return gamesFinished;
    }

    public void setGamesFinished(Integer gamesFinished) {
        this.gamesFinished = gamesFinished;
    }

    @Basic
    @Column(name = "username", nullable = true, length = -1)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlayersEntity that = (PlayersEntity) o;

        if (playerId != null ? !playerId.equals(that.playerId) : that.playerId != null) return false;
        if (gamesFinished != null ? !gamesFinished.equals(that.gamesFinished) : that.gamesFinished != null)
            return false;
        if (username != null ? !username.equals(that.username) : that.username != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = playerId != null ? playerId.hashCode() : 0;
        result = 31 * result + (gamesFinished != null ? gamesFinished.hashCode() : 0);
        result = 31 * result + (username != null ? username.hashCode() : 0);
        return result;
    }
}
