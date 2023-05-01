package com.pokemongostats.model.bean;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import lombok.Getter;
import lombok.Setter;

@Getter
public class UpdateStatus extends BaseObservable {

	private int progress;
	@Setter
	private String mainMsg;
	private int secondaryProgress;
	@Setter
	private String secondaryMsg;

	public void publishMainProgress(String msg, int p){
		this.mainMsg = msg;
		this.progress = p;
		notifyChange();
	}

	public void publishSecondaryProgress(String msg, int p){
		this.secondaryMsg = msg;
		this.secondaryProgress = p;
		notifyChange();
	}

	public void finnish(){
		this.progress = 100;
		this.secondaryProgress = 100;
		notifyChange();
	}
	public void finnishSecondary(){
		this.secondaryProgress = 100;
		notifyChange();
	}
}