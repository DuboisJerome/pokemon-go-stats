package com.pokemongostats.model.bean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FiltrePrefPkmn {
	private boolean isAvecLegendaire;
	private boolean isAvecFabuleux;
	private boolean isAvecMegaEvol;
	private boolean isAvecUltraChimere;
	private boolean isSeulementPkmnEnJeu;
	private boolean isSeulementDerniereEvol;
}