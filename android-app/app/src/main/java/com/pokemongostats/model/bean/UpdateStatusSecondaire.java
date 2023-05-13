package com.pokemongostats.model.bean;

import lombok.Getter;

/**
 * parent.progressionEtape = this.progressionGlobale
 * parent.descEtape = this.nomEtape - this.descEtape [ this.progressionEtape ]
 */
@Getter
public class UpdateStatusSecondaire extends UpdateStatus {

	private final UpdateStatus parent;

	public UpdateStatusSecondaire(UpdateStatus parent) {
		this.parent = parent;
	}

	public void updateProgressionGlobale(int newProgression) {
		super.updateProgressionGlobale(newProgression);
		this.parent.updateProgressionEtape(newProgression);
	}

	@Override
	public void startingEtape(String newNomEtape) {
		super.startingEtape(newNomEtape);
		updateParentDescEtape();
	}

	@Override
	public void updateDescEtape(String newDescEtape) {
		super.updateDescEtape(newDescEtape);
		updateParentDescEtape();
	}

	public void updateProgressionEtape(int newProgressionEtape) {
		super.updateProgressionEtape(newProgressionEtape);
		updateParentDescEtape();
	}

	public void finnishEtape(int newProgressionGlobal) {
		super.finnishEtape(newProgressionGlobal);
		updateParentDescEtape();
		this.parent.updateProgressionEtape(newProgressionGlobal);
	}

	private void updateParentDescEtape() {
		var sb = new StringBuilder(this.nomEtape);
		if (this.descEtape != null && !this.descEtape.isEmpty()) {
			sb.append(" - ").append(this.descEtape);
		}
		if (this.progressionEtape > 0 && this.progressionEtape < 100) {
			sb.append(" [").append(this.progressionEtape).append(" %]");
		}
		this.parent.updateDescEtape(sb.toString());
	}
}