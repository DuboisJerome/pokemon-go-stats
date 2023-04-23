package fr.pokemon.parser.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
public class PkmnMove implements Serializable {

    /**
     *
     */
    @Serial
    private static final long serialVersionUID = -1645998904713356956L;

    public long pokedexNum;
    public String pkmnIdStr;
    public String form;
    public long moveId = -1;
    public String moveIdStr;
    public boolean isElite;
    public String moveType;


    public String getUniquePkmnId() {
        return this.pokedexNum + "_" + this.form;
    }

    @Override
    public String toString() {
        return "PkmnMove{" +
                "pokedexNum=" + pokedexNum +
                ", pkmnIdStr='" + pkmnIdStr + '\'' +
                ", form='" + form + '\'' +
                ", moveId=" + moveId +
                ", moveIdStr='" + moveIdStr + '\'' +
                ", isElite=" + isElite +
                ", moveType='" + moveType + '\'' +
                '}';
    }
}