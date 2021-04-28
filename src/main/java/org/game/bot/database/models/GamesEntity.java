package org.game.bot.database.models;

import javax.persistence.*;

@Entity
@Table(name = "games", schema = "game_schema", catalog = "contactGame")
public class GamesEntity {
    private Integer id;
    private Integer leaderId;
    private String keyWord;
    private Integer associationsGuessed;

    public GamesEntity(Integer leaderId,String keyWord, Integer associationsGuessed) {
        this.leaderId = leaderId;
        this.keyWord = keyWord;
        this.associationsGuessed = associationsGuessed;
    }

    public GamesEntity() {

    }

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
    @Column(name = "key_word", nullable = true, length = -1)
    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
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

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (leaderId != null ? !leaderId.equals(that.leaderId) : that.leaderId != null) return false;
        if (keyWord != null ? !keyWord.equals(that.keyWord) : that.keyWord != null) return false;
        if (associationsGuessed != null ? !associationsGuessed.equals(that.associationsGuessed) : that.associationsGuessed != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (leaderId != null ? leaderId.hashCode() : 0);
        result = 31 * result + (keyWord != null ? keyWord.hashCode() : 0);
        result = 31 * result + (associationsGuessed != null ? associationsGuessed.hashCode() : 0);
        return result;
    }
}
