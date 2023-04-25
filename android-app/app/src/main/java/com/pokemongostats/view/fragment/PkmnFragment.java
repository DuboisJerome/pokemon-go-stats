package com.pokemongostats.view.fragment;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pokemongostats.R;
import com.pokemongostats.controller.dao.PokedexDAO;
import com.pokemongostats.controller.utils.CollectionUtils;
import com.pokemongostats.controller.utils.EffectivenessUtils;
import com.pokemongostats.databinding.CardViewEvolPkmnBinding;
import com.pokemongostats.databinding.FragmentPkmnDescBinding;
import com.pokemongostats.model.bean.Effectiveness;
import com.pokemongostats.model.bean.PkmnMoveComplet;
import com.pokemongostats.model.bean.Type;
import com.pokemongostats.model.bean.bdd.Evolution;
import com.pokemongostats.model.bean.bdd.Move;
import com.pokemongostats.model.bean.bdd.PkmnDesc;
import com.pokemongostats.model.comparators.MoveComparators;
import com.pokemongostats.model.comparators.PkmnMoveComparators;
import com.pokemongostats.model.parcalables.PclbPkmnDesc;
import com.pokemongostats.view.adapter.MoveAdapter;
import com.pokemongostats.view.adapter.PkmnMoveAdapter;
import com.pokemongostats.view.adapter.TypeRecyclerViewAdapter;
import com.pokemongostats.view.listitem.TypeListItemView;
import com.pokemongostats.view.rows.MoveHeader;
import com.pokemongostats.view.rows.PkmnMoveHeader;
import com.pokemongostats.view.viewholder.LstTypeViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Activity to add a gym at the current date to the database
 *
 * @author Zapagon
 */
public class PkmnFragment extends Fragment {

	private static final String PKMN_SELECTED_KEY = "PKMN_SELECTED_KEY";
	// adapter
	private final Map<Double,TypeRecyclerViewAdapter> mapTypeEffectivenessAdapter = new TreeMap<>(Collections.reverseOrder());
	// liste view
	private final Map<Double,TextView> mapTypeEffectivenessTitre = new TreeMap<>(Collections.reverseOrder());
	private final Map<Double,ViewGroup> mapTypeEffectivenessListView = new TreeMap<>(Collections.reverseOrder());

	// selected pkmn
	private PkmnMoveAdapter adapterQuickMoves;
	private PkmnMoveAdapter adapterChargeMoves;
	private FragmentPkmnDescBinding binding;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//
		this.adapterQuickMoves = new PkmnMoveAdapter();
		this.adapterQuickMoves.setMoveType(Move.MoveType.QUICK);
		//
		this.adapterChargeMoves = new PkmnMoveAdapter();
		this.adapterChargeMoves.setMoveType(Move.MoveType.CHARGE);

		this.mapTypeEffectivenessAdapter.clear();
		this.mapTypeEffectivenessTitre.clear();
		this.mapTypeEffectivenessListView.clear();

		addAdapter(Effectiveness.SUPER_EFFECTIVE, Effectiveness.SUPER_EFFECTIVE);
		addAdapter(Effectiveness.SUPER_EFFECTIVE, Effectiveness.NORMAL);
		addAdapter(Effectiveness.SUPER_EFFECTIVE, Effectiveness.NOT_VERY_EFFECTIVE);
		addAdapter(Effectiveness.SUPER_EFFECTIVE, Effectiveness.IMMUNE);

		addAdapter(Effectiveness.NORMAL, Effectiveness.SUPER_EFFECTIVE);
		addAdapter(Effectiveness.NORMAL, Effectiveness.NORMAL);
		addAdapter(Effectiveness.NORMAL, Effectiveness.NOT_VERY_EFFECTIVE);
		addAdapter(Effectiveness.NORMAL, Effectiveness.IMMUNE);

		addAdapter(Effectiveness.NOT_VERY_EFFECTIVE, Effectiveness.SUPER_EFFECTIVE);
		addAdapter(Effectiveness.NOT_VERY_EFFECTIVE, Effectiveness.NORMAL);
		addAdapter(Effectiveness.NOT_VERY_EFFECTIVE, Effectiveness.NOT_VERY_EFFECTIVE);
		addAdapter(Effectiveness.NOT_VERY_EFFECTIVE, Effectiveness.IMMUNE);

		addAdapter(Effectiveness.IMMUNE, Effectiveness.SUPER_EFFECTIVE);
		addAdapter(Effectiveness.IMMUNE, Effectiveness.NORMAL);
		addAdapter(Effectiveness.IMMUNE, Effectiveness.NOT_VERY_EFFECTIVE);
		addAdapter(Effectiveness.IMMUNE, Effectiveness.SUPER_EFFECTIVE);
	}

	private int getEffColor(int colorId) {
		Resources.Theme theme = Objects.requireNonNull(getContext()).getTheme();
		return getResources().getColor(colorId, theme);
	}

	private void addAdapter(Effectiveness effSurType1, Effectiveness effSurType2) {
		double eff = effSurType1.getMultiplier() * effSurType2.getMultiplier();
		double roundEff = roundEff(eff);
		if (roundEff == Effectiveness.NORMAL.getMultiplier() ||this.mapTypeEffectivenessAdapter.containsKey(roundEff)) {
			return;
		}

		TypeRecyclerViewAdapter typeAdapter = new TypeRecyclerViewAdapter(true) {
			@NonNull
			@Override
			public LstTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
				LstTypeViewHolder l = super.onCreateViewHolder(parent, viewType);
				int heightPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 28, getResources().getDisplayMetrics());
				l.getBinding().idType.setHeight(heightPx);
				return l;
			}
		};
		this.mapTypeEffectivenessAdapter.put(roundEff, typeAdapter);

		LinearLayout.LayoutParams txtParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

		int idColor;
		if (roundEff > Effectiveness.NORMAL.getMultiplier()) {
			if (roundEff >= Effectiveness.SUPER_EFFECTIVE.getMultiplier()) {
				idColor = R.color.super_weakness;
			} else {
				idColor = R.color.weakness;
			}
		} else {
			if (roundEff < Effectiveness.NOT_VERY_EFFECTIVE.getMultiplier()) {
				idColor = R.color.super_resistance;
			} else {
				idColor = R.color.resistance;
			}
		}
		int color = getEffColor(idColor);

		TextView titre = new TextView(getContext());
		titre.setLayoutParams(txtParams);
		titre.setText("Dgt x " + roundEff);
		titre.setTextColor(color);
		this.mapTypeEffectivenessTitre.put(roundEff, titre);

		TypeListItemView listWeak = new TypeListItemView(getContext(), 4);
		listWeak.setAdapter(typeAdapter);
		typeAdapter.setOnClickItemListener(t -> {
			PkmnFragmentDirections.ActionToType action = PkmnFragmentDirections.actionToType();
			action.setType1(t.name());
			Navigation.findNavController(getView()).navigate(action);
		});

		LinearLayout linearLayout = new LinearLayout(getContext());
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		linearLayout.addView(titre);
		linearLayout.addView(listWeak);

		this.mapTypeEffectivenessListView.put(roundEff, linearLayout);
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {

		// Inflate the layout for this fragment
		this.binding = FragmentPkmnDescBinding.inflate(inflater, container, false);

		if (savedInstanceState != null && getPkmn() == null) {
			setPkmn(savedInstanceState.getParcelable(PKMN_SELECTED_KEY));
		}

		this.binding.pkmnDescQuickmovesHeader.setVisibility(R.id.move_pve_dpe, View.GONE);
		this.binding.pkmnDescQuickmovesHeader.setVisibility(R.id.move_pvp_dpe, View.GONE);
		this.binding.pkmnDescChargemovesHeader.setVisibility(R.id.move_pve_dps, View.GONE);
		this.binding.pkmnDescChargemovesHeader.setVisibility(R.id.move_pve_eps, View.GONE);

		Consumer<PkmnMoveComplet> onMoveClicked = pm -> {
			PkmnFragmentDirections.ActionToMove action = PkmnFragmentDirections.actionToMove(pm.getMoveId());
			Navigation.findNavController(getView()).navigate(action);
		};

		RecyclerView quickMoves = this.binding.pkmnDescQuickmoves;
		LinearLayoutManager llm = new LinearLayoutManager(getContext());
		llm.setOrientation(LinearLayoutManager.VERTICAL);
		quickMoves.setLayoutManager(llm);
		quickMoves.setAdapter(this.adapterQuickMoves);
		this.adapterQuickMoves.setOnClickItemListener(onMoveClicked);

		RecyclerView chargeMoves = this.binding.pkmnDescChargemoves;
		llm = new LinearLayoutManager(getContext());
		llm.setOrientation(LinearLayoutManager.VERTICAL);
		chargeMoves.setLayoutManager(llm);
		chargeMoves.setAdapter(this.adapterChargeMoves);
		this.adapterChargeMoves.setOnClickItemListener(onMoveClicked);

		// weaknesses
		LinearLayout layoutWeakness = this.binding.typeWeaknesses;
		for (View list : this.mapTypeEffectivenessListView.values()) {
			if (list.getParent() != null) {
				((ViewGroup) list.getParent()).removeView(list);
			}
			layoutWeakness.addView(list);
		}
		this.mapTypeEffectivenessListView.clear();

		String idPkmn = PkmnFragmentArgs.fromBundle(getArguments()).getPkmnId();
		PkmnDesc pkmn = PokedexDAO.getInstance().getPokemonWithId(idPkmn);
		setPkmn(pkmn);

		return this.binding.getRoot();
	}

	private static double roundEff(double eff) {
		return Math.round(eff * 1000.0) / 1000.0;
	}

	public PkmnDesc getPkmn() {
		return this.binding.getPkmn();
	}

	private static View creerSeparateurEvol(Context ctx) {
		ImageView img = new ImageView(ctx);
		img.setImageDrawable(ResourcesCompat.getDrawable(ctx.getResources(), R.drawable.ic_baseline_forward_24, ctx.getTheme()));
		img.setMaxHeight(24);
		img.setMaxWidth(24);
		FrameLayout layout = new FrameLayout(ctx);
		layout.addView(img);
		layout.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
		return layout;
	}

	private void addSeparateurEvol() {
		this.binding.pkmnDescEvolutions.addView(creerSeparateurEvol(getContext()));
	}

	public void setPkmn(PkmnDesc pkmn) {
		this.binding.setPkmn(pkmn);
		if (pkmn != null) {
			updateEvolutions(pkmn);
			updateTypeEff(pkmn);
			updateMoves(pkmn);
		}
	}

	public void updateEvolutions(PkmnDesc pkmn){
		Map<String,List<Evolution>> mapBasesAndEvols = PokedexDAO.getInstance().findBasesEtEvolsPokemons(pkmn.getPokedexNum(), pkmn.getForm());
		List<Evolution> basesEvol = mapBasesAndEvols.get("BASE");
		List<Evolution> nextEvol = mapBasesAndEvols.get("EVOL");
		// default evolIds contains p.pokedexNum then size == 1
		if ((basesEvol != null && !basesEvol.isEmpty()) || (nextEvol != null && !nextEvol.isEmpty())) {
			// list of evolutions
			this.binding.pkmnDescEvolutionsTitle.setVisibility(View.VISIBLE);
			this.binding.pkmnDescEvolutions.setVisibility(View.VISIBLE);
			this.binding.pkmnDescEvolutions.removeAllViews();

			if (basesEvol != null) {
				for (Evolution ev : basesEvol) {
					PkmnDesc pkmnFound = PokedexDAO.getInstance().getPokemonWithId(ev.getBasePkmnId(), ev.getBasePkmnForm());
					addEvol(pkmnFound, pkmn);
					addSeparateurEvol();
				}
			}

			addEvol(pkmn, pkmn);

			if (nextEvol != null) {
				Map<String,List<Evolution>> evolByBase = nextEvol.stream().collect(Collectors.toMap(Evolution::getUniqueIdBase, ev -> {
					List<Evolution> l = new ArrayList<>();
					l.add(ev);
					return l;
				}, (l1, l2) -> {
					l1.addAll(l2);
					return l1;
				}, TreeMap::new));
				evolByBase.forEach((id, l) -> {

					addSeparateurEvol();
					if (l.size() > 1) {
						LinearLayout layout = new LinearLayout(getContext());
						layout.setOrientation(LinearLayout.VERTICAL);
						layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
						this.binding.pkmnDescEvolutions.addView(layout);
						for (Evolution ev : l) {
							PkmnDesc pkmnFound = PokedexDAO.getInstance().getPokemonWithId(ev.getEvolutionId(), ev.getEvolutionForm());
							addEvol(pkmnFound, pkmn, layout);
						}
					} else {
						// == 1 pas besoin de linear layout
						for (Evolution ev : l) {
							PkmnDesc pkmnFound = PokedexDAO.getInstance().getPokemonWithId(ev.getEvolutionId(), ev.getEvolutionForm());
							addEvol(pkmnFound, pkmn);
						}
					}

				});
			}
		} else {
			this.binding.pkmnDescEvolutionsTitle.setVisibility(View.GONE);
			this.binding.pkmnDescEvolutions.setVisibility(View.GONE);
		}
	}

	private void addEvol(PkmnDesc pkmnFound, PkmnDesc currentPkmn) {
		addEvol(pkmnFound, currentPkmn, this.binding.pkmnDescEvolutions);
	}

	private void addEvol(PkmnDesc pkmnFound, PkmnDesc currentPkmn, ViewGroup parent) {
		CardViewEvolPkmnBinding evolution = CardViewEvolPkmnBinding.inflate(LayoutInflater.from(getContext()), parent, true);
		evolution.setPkmndesc(pkmnFound);
		if (pkmnFound.getUniqueId().equals(currentPkmn.getUniqueId())) {
			int idColor = ResourcesCompat.getColor(getResources(), R.color.row_item_focus, requireContext().getTheme());
			evolution.getRoot().setBackgroundColor(idColor);
		} else {
			evolution.getRoot().setOnClickListener(v ->
					setPkmn(pkmnFound)
			);
		}
	}
	public void updateTypeEff(PkmnDesc pkmn){
		for (Map.Entry<Double,TypeRecyclerViewAdapter> entry : this.mapTypeEffectivenessAdapter.entrySet()) {

			double baseEff = entry.getKey();

			List<Type> lstTypeFilter = new ArrayList<>();
			for (Type t : Type.values()) {
				double eff = roundEff(EffectivenessUtils.getTypeEffOnPokemon(t, pkmn));
				if (eff == baseEff) {
					lstTypeFilter.add(t);
				}
			}
			TypeRecyclerViewAdapter adapter = entry.getValue();
			adapter.filter(lstTypeFilter);
			View titre = this.mapTypeEffectivenessTitre.get(baseEff);
			if (titre != null) {
				titre.setVisibility(lstTypeFilter.size() > 0 ? View.VISIBLE : View.GONE);
			}
			adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
				@Override
				public void onChanged() {
					if (titre != null) {
						titre.setVisibility(adapter.getCount() > 0 ? View.VISIBLE : View.GONE);
					}
				}
			});
		}
	}

	public void updateMoves(PkmnDesc pkmn){
		Comparator<PkmnMoveComplet> comparatorMove = PkmnMoveComparators.getComparatorByPps().reversed();
		Map<Move.MoveType,List<PkmnMoveComplet>> map = CollectionUtils.groupBy(PokedexDAO.getInstance().getLstPkmnMoveCompletFor(pkmn), pm -> pm.getMove().getMoveType());
		List<PkmnMoveComplet> listQuickMove = map.get(Move.MoveType.QUICK);
		if (listQuickMove == null) {
			listQuickMove = new ArrayList<>();
		}
		this.adapterQuickMoves.setComparator(comparatorMove);
		this.adapterQuickMoves.setList(listQuickMove);
		List<PkmnMoveComplet> listChargeMove = map.get(Move.MoveType.CHARGE);
		if (listChargeMove == null) {
			listChargeMove = new ArrayList<>();
		}
		this.adapterChargeMoves.setComparator(comparatorMove);
		this.adapterChargeMoves.setList(listChargeMove);
	}

	@Override
	public void onSaveInstanceState(@NonNull Bundle outState) {
		super.onSaveInstanceState(outState);
		if (getPkmn() != null) {
			outState.putParcelable(PKMN_SELECTED_KEY,
					new PclbPkmnDesc(getPkmn()));
		}
	}

	/********************
	 * LISTENERS / CALLBACK
	 ********************/

	public static String getNameFormat(PkmnDesc p) {
		return "# " + p.getPokedexNum() + System.getProperty("line.separator") + p.getName();
	}
}