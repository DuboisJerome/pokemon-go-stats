package com.pokemongostats.model.bean;

import lombok.Getter;

/**
 * parent.progressionEtape = this.progressionGlobale
 * parent.descEtape = this.nomEtape - this.descEtape [ this.progressionEtape ]
 */
@Getter
public class UpdateStatusSecondaire extends UpdateStatus {

	private final UpdateStatus parent;

	public UpdateStatusSecondaire(UpdateStatus parent){
		this.parent = parent;
	}

	public void updateProgressionGlobale(int newProgression){
		super.updateProgressionGlobale(newProgression);
		parent.updateProgressionEtape(newProgression);
	}

	public void startingEtape(String newNomEtape){
		super.startingEtape(newNomEtape);
		updateParentDescEtape();
	}

	public void updateDescEtape(String newDescEtape){
		super.updateDescEtape(newDescEtape);
		updateParentDescEtape();
	}

	public void updateProgressionEtape(int newProgressionEtape){
		super.updateProgressionEtape(newProgressionEtape);
		updateParentDescEtape();
	}

	public void finnishEtape(int newProgressionGlobal){
		super.finnishEtape(newProgressionGlobal);
		updateParentDescEtape();
		parent.updateProgressionEtape(newProgressionGlobal);
	}

	private void updateParentDescEtape(){
		var sb = new StringBuilder(this.nomEtape);
		if(this.descEtape != null && !this.descEtape.isEmpty()){
			sb.append(" - ").append(descEtape);
		}
		if(progressionEtape > 0 && progressionEtape < 100){
			sb.append(" [").append(progressionEtape).append(" %]");
		}
		parent.updateDescEtape(sb.toString());
	}
}