package fr.pokemon.parser.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
public class Evolution implements Serializable {

    /**
     *
     */
    @Serial
    private static final long serialVersionUID = -1645998904713356956L;

    public long basePkmnId = -1;
    public String basePkmnForm;
    public String basePkmnIdStr;
    public long evolutionId = -1;
    public String evolutionForm;
    public String evolutionIdStr;
    public int candyToEvolve;
    public boolean isTemporaire = false;

    public Evolution() {
    }

    public Evolution(long basePkmnId, String basePkmnForm, long evolutionId,
                     String evolutionForm) {
        this.basePkmnId = basePkmnId;
        this.basePkmnForm = basePkmnForm;
        this.evolutionId = evolutionId;
        this.evolutionForm = evolutionForm;
    }

    public long getBasePkmnId() {
        return this.basePkmnId;
    }

    public void setBasePkmnId(long basePkmnId) {
        this.basePkmnId = basePkmnId;
    }

    public String getBasePkmnForm() {
        return this.basePkmnForm;
    }

    public void setBasePkmnForm(String basePkmnForm) {
        this.basePkmnForm = basePkmnForm;
    }

    public long getEvolutionId() {
        return this.evolutionId;
    }

    public void setEvolutionId(long evolutionId) {
        this.evolutionId = evolutionId;
    }

    public String getEvolutionForm() {
        return this.evolutionForm;
    }

    public void setEvolutionForm(String evolutionForm) {
        this.evolutionForm = evolutionForm;
    }

    public boolean isFrom(PkmnDesc p) {
        return this.getUniqueIdBase().equals(p.getUniqueId());
    }

    public boolean isTo(PkmnDesc p) {
        return this.getUniqueIdEvol().equals(p.getUniqueId());
    }

    public String getUniqueIdBase() {
        return this.basePkmnId + "_" + this.basePkmnForm;
    }

    public String getUniqueIdEvol() {
        return this.evolutionId + "_" + this.evolutionForm;
    }

    @Override
    public String toString() {
        return "Evolution{" +
                "basePkmnId=" + basePkmnId +
                ", basePkmnForm='" + basePkmnForm + '\'' +
                ", basePkmnIdStr='" + basePkmnIdStr + '\'' +
                ", evolutionId=" + evolutionId +
                ", evolutionForm='" + evolutionForm + '\'' +
                ", evolutionIdStr='" + evolutionIdStr + '\'' +
                ", candyToEvolve=" + candyToEvolve +
                ", isTemporaire=" + isTemporaire +
                '}';
    }
}