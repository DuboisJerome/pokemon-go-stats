public class Move implements Serializable {
	private long id;
	private String name;
	private MoveType moveType;
	// private long animationId;
	private Type type;
	private int power;
	// private double accuracyChance = 1f;
	private double staminaLossScalar;
	//private int trainerLevelMin = 1;
	//private int trainerLevelMax = 100;
	
	/** duration in miliseconds */
	private int duration;
	private int damageWindowStart;
	private int damageWindowEnd;
	private int energyDelta;
}

public class QuickMove extends Move {
	// note : energyDelta > 0
	public QuickMove(){
		this.moveType = MoveType.QUICK;
	}
}

public class ChargeMove extends Move {
	// % from 0.00(0%) to 1.00(100%)
	private double criticalChance;
	// note : energyDelta < 0
	public ChargeMove(){
		this.moveType = MoveType.CHARGE;
	}
}

public enum MoveType {
	QUICK, CHARGE;
}

CREATE TABLE move (
	id INTEGER PRIMARY KEY,
	move_type TEXT NOT NULL,
	type TEXT NOT NULL,
	power INTEGER NOT NULL,
	stamina_loss_scalar REAL NOT NULL,
	duration REAL NOT NULL,
	energy_delta INTEGER NOT NULL,
	critical_chance REAL,
);

CREATE TABLE move_i18n (
	id INTEGER NOT NULL,
	name TEXT NOT NULL,
	lang TEXT NOT NULL,
	PRIMARY KEY(id, lang),
	FOREIGN KEY(id) REFERENCES move(id)
);