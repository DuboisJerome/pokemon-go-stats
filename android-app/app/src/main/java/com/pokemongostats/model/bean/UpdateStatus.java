package com.pokemongostats.model.bean;

import androidx.databinding.BaseObservable;

import com.pokemongostats.controller.external.Log;

import lombok.Getter;
import lombok.Setter;

@Getter
public class UpdateStatus extends BaseObservable {

	protected int progressionGlobale;
	@Setter
	protected String nomEtape;
	protected int progressionEtape;
	protected String descEtape;

	public UpdateStatus(){

	}

	public void updateProgressionGlobale(double newProgression){
		this.progressionGlobale = (int)newProgression;
		notifyChange();
	}

	public void startingEtape(String nomEtape){
		Log.info("Start "+nomEtape);
		this.nomEtape = nomEtape;
		this.progressionEtape = 0;
		notifyChange();
	}

	public void updateDescEtape(String newDescEtape){
		Log.info("Start "+nomEtape+" - "+newDescEtape);
		this.descEtape = newDescEtape;
		notifyChange();
	}

	public void updateProgressionEtape(double newProgressionEtape){
		this.progressionEtape = (int)newProgressionEtape;
		notifyChange();
	}
	public void updateProgressionEtape(int cpt, int count){
		updateProgressionEtape(cpt*100D/count);
	}

	public void finnishEtape(double newProgressionGlobal){
		Log.info("End "+nomEtape);
		this.descEtape = "";
		this.progressionEtape = 100;
		this.progressionGlobale = (int)newProgressionGlobal;
		notifyChange();
	}

	@Override
	public void notifyChange() {
		super.notifyChange();
	}

	public void finnish(){
		finnishEtape(100);
	}

}