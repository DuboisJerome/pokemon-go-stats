package fr.pokemon.parser.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
public class PkmnDesc implements Serializable {

    /**
     *
     */
    @Serial
    private static final long serialVersionUID = -2768420569277625730L;

    public static final String DEFAULT_FORM = "NORMAL";

    public String templateId;
    public long id = -1;
    public String idStr;
    public String form = DEFAULT_FORM;

    public Type type1;
    public Type type2;
    public double kmsPerCandy;

    public int stamina = -1;
    public int attack = -1;
    public int defense = -1;


    public String name;

    public Set<String> tags = new HashSet<>();

    public PkmnDesc() {
    }

    public PkmnDesc(PkmnDesc p) {
        this.templateId = p.templateId;
        this.id = p.id;
        this.idStr = p.idStr;
        this.form = p.form;
        this.type1 = p.type1;
        this.type2 = p.type2;
        this.kmsPerCandy = p.kmsPerCandy;
        this.stamina = p.stamina;
        this.attack = p.attack;
        this.defense = p.defense;
        this.name = p.name;
        this.tags.addAll(p.tags);
    }

    //private double kmsPerEgg;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PkmnDesc pkmnDesc)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(getUniqueId(), pkmnDesc.getUniqueId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getUniqueId());
    }

    public String getUniqueId() {
        return getId() + "_" + getForm();
    }

    @Override
    public String toString() {
        return "PkmnDesc{" +
                "templateId='" + templateId + '\'' +
                ", id=" + id +
                ", idStr='" + idStr + '\'' +
                ", form='" + form + '\'' +
                ", type1=" + type1 +
                ", type2=" + type2 +
                ", kmsPerCandy=" + kmsPerCandy +
                ", stamina=" + stamina +
                ", attack=" + attack +
                ", defense=" + defense +
                ", name='" + name + '\'' +
                '}';
    }

    public String toStringLight() {
        return "PkmnDesc{" +
                "id=" + id +
                ", idStr='" + idStr + '\'' +
                ", form='" + form + '\'' +
                ", type1=" + type1 +
                ", type2=" + type2 +
                '}';
    }
}