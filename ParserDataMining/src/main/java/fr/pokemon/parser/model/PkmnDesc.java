package fr.pokemon.parser.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

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
    }

    //private double kmsPerEgg;

    //private boolean isLegendary;

    // 6 stats
//	private int physicalAttack;
//	private int physicalDefense;
//	private int specialAttack;
//	private int specialDefense;
//	private int pv;
//	private int speed;
//	public double getBaseAttack() {
//		return this.attack >= 0 ? this.attack : getBaseAttack(isNerf());
//	}

//	private double getBaseAttack(boolean isNerf) {
//		return this.attack >= 0 ? this.attack : Math.round(getScaledAttack() * getSpeedMod() * (isNerf ? 0.91 : 1.0));
//	}

//	private double getScaledAttack() {
//		int higher = Math.max(this.physicalAttack, this.specialAttack);
//		int lower = Math.min(this.physicalAttack, this.specialAttack);
//		double higherScaled = ((7.0 / 8.0) * higher);
//		double lowerScaled = ((1.0 / 8.0) * lower);
//		return Math.round(2.0 * (higherScaled + lowerScaled));
//	}

//	public double getBaseDefense() {
//		return this.defense >= 0 ? this.defense : getBaseDefense(isNerf());
//	}

//	private double getBaseDefense(boolean isNerf) {
//		return this.defense >= 0
//				? this.defense : Math.round(getScaledDefense() * getSpeedMod() * (isNerf ? 0.91 : 1.0));
//	}

//	private double getScaledDefense() {
//		int higher = Math.max(this.physicalDefense, this.specialDefense);
//		int lower = Math.min(this.physicalDefense, this.specialDefense);
//		double higherScaled = ((5.0 / 8.0) * higher);
//		double lowerScaled = ((3.0 / 8.0) * lower);
//		return Math.round(2.0 * (higherScaled + lowerScaled));
//	}

//	private double getSpeedMod() {
//		return 1.0 + ((this.speed - 75.0) / 500.0);
//	}
//
//	public double getBaseStamina() {
//		return this.stamina >= 0 ? this.stamina : getBaseStamina(isNerf());
//	}
//
//	private double getBaseStamina(boolean isNerf) {
//		return this.stamina >= 0 ? this.stamina : Math.floor(((this.pv * 1.75) + 50.0) * (isNerf ? 0.91 : 1.0));
//	}

//	public double getMaxCP() {
//		return isNerf() ? getMaxCPNerf() : getMaxCPNormal();
//	}
//
//	private boolean isNerf() {
//		return getMaxCPNormal() > 4000.0;
//	}
//
//	private double getMaxCPNormal() {
//		double a = getBaseAttack(false) + 15.0;
//		double d = Math.pow((getBaseDefense(false) + 15.0), 0.5);
//		double s = Math.pow((getBaseStamina(false) + 15.0), 0.5);
//		double multiplier40 = Math.pow(0.7903001, 2.0);
//		return (a * d * s * multiplier40) / 10.0;
//	}
//
//	private double getMaxCPNerf() {
//		double a = getBaseAttack(true) + 15.0;
//		double d = Math.pow((getBaseDefense(true) + 15.0), 0.5);
//		double s = Math.pow((getBaseStamina(true) + 15.0), 0.5);
//		double multiplier40 = Math.pow(0.7903001, 2.0);
//		return (a * d * s * multiplier40) / 10.0;
//	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PkmnDesc)) return false;
        if (!super.equals(o)) return false;
        PkmnDesc pkmnDesc = (PkmnDesc) o;
        return Objects.equals(getUniqueId(), pkmnDesc.getUniqueId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getUniqueId());
    }

    public String getUniqueId() {
        return getId() + "_" + getForm();
    }
}