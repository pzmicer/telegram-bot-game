package org.game.bot.database.models;

import javax.persistence.*;

@Entity
@Table(name = "games", schema = "game_schema", catalog = "dfffjmt2tbqtpp")
public class GamesEntity {
    private Integer gameId;
    private Integer leaderId;
    private String keyword;
    private Integer associationsGuessed;

    public GamesEntity(Integer leaderId, String keyword, Integer associationsGuessed) {
        this.leaderId = leaderId;
        this.keyword = keyword;
        this.associationsGuessed = associationsGuessed;
    }

    public GamesEntity() {

    }

    @Id
    @Column(name = "game_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

    @Basic
    @Column(name = "leader_id", nullable = false)
    public Integer getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(Integer leaderId) {
        this.leaderId = leaderId;
    }

    @Basic
    @Column(name = "keyword", nullable = false, length = -1)
    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    @Basic
    @Column(name = "associations_guessed", nullable = true)
    public Integer getAssociationsGuessed() {
        return associationsGuessed;
    }

    public void setAssociationsGuessed(Integer associationsGuessed) {
        this.associationsGuessed = associationsGuessed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GamesEntity that = (GamesEntity) o;

        if (gameId != null ? !gameId.equals(that.gameId) : that.gameId != null) return false;
        if (leaderId != null ? !leaderId.equals(that.leaderId) : that.leaderId != null) return false;
        if (keyword != null ? !keyword.equals(that.keyword) : that.keyword != null) return false;
        if (associationsGuessed != null ? !associationsGuessed.equals(that.associationsGuessed) : that.associationsGuessed != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = gameId != null ? gameId.hashCode() : 0;
        result = 31 * result + (leaderId != null ? leaderId.hashCode() : 0);
        result = 31 * result + (keyword != null ? keyword.hashCode() : 0);
        result = 31 * result + (associationsGuessed != null ? associationsGuessed.hashCode() : 0);
        return result;
    }
}
