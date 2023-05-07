package com.pokemongostats.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

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
import com.pokemongostats.model.bean.ClePkmn;
import com.pokemongostats.model.bean.Effectiveness;
import com.pokemongostats.model.bean.PkmnMoveComplet;
import com.pokemongostats.model.bean.Type;
import com.pokemongostats.model.bean.bdd.Evolution;
import com.pokemongostats.model.bean.bdd.Move;
import com.pokemongostats.model.bean.bdd.PkmnDesc;
import com.pokemongostats.model.comparators.PkmnMoveComparators;
import com.pokemongostats.model.parcalables.PclbPkmnDesc;
import com.pokemongostats.view.EffectivenessTypeView;
import com.pokemongostats.view.adapter.PkmnMoveAdapter;
import com.pokemongostats.view.adapter.TypeRecyclerViewAdapter;
import com.pokemongostats.view.viewholder.LstTypeViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import fr.commons.generique.controller.utils.Log;

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

		EffectivenessUtils.getSetRoundEff().forEach(this::addAdapter);
	}

	@Override
	public void onDestroy() {
		this.mapTypeEffectivenessAdapter.clear();
		super.onDestroy();
	}

	private void addAdapter(double roundEff) {
		if (roundEff == Effectiveness.NORMAL.getMultiplier() || this.mapTypeEffectivenessAdapter.containsKey(roundEff)) {
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

		typeAdapter.setOnClickItemListener(t -> {
			PkmnFragmentDirections.ActionToType action = PkmnFragmentDirections.actionToType();
			action.setType1(t.name());
			Navigation.findNavController(getView()).navigate(action);
		});

		this.mapTypeEffectivenessAdapter.put(roundEff, typeAdapter);
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
		this.binding.typeResistances.removeAllViews();
		this.binding.typeWeaknesses.removeAllViews();
		EffectivenessUtils.getSetRoundEff().forEach(roundEff -> {
			TypeRecyclerViewAdapter adapter = this.mapTypeEffectivenessAdapter.get(roundEff);
			if (adapter == null) {
				return;
			}
			EffectivenessTypeView etv = new EffectivenessTypeView(getContext(), roundEff);
			etv.setNbColonnesType(2);
			etv.setAdapter(adapter);
			if (roundEff < Effectiveness.NORMAL.getMultiplier()) {
				this.binding.typeResistances.addView(etv);
			} else if (roundEff > Effectiveness.NORMAL.getMultiplier()) {
				this.binding.typeWeaknesses.addView(etv);
			}
			etv.setVisibility(View.GONE);
			adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {

				private void managerVisibility() {
					etv.setVisibility(adapter.getCount() <= 0 ? View.GONE : View.VISIBLE);
				}

				@Override
				public void onItemRangeInserted(int positionStart, int itemCount) {
					super.onItemRangeInserted(positionStart, itemCount);
					managerVisibility();
				}

				@Override
				public void onItemRangeRemoved(int positionStart, int itemCount) {
					super.onItemRangeRemoved(positionStart, itemCount);
					managerVisibility();
				}
			});
		});

		com.pokemongostats.view.fragment.PkmnFragmentArgs args = PkmnFragmentArgs.fromBundle(getArguments());
		long idPkmn = args.getPkmnId();
		String form = args.getPkmnForm();
		PkmnDesc pkmn = PokedexDAO.getInstance().getPokemon(idPkmn, form);
		setPkmn(pkmn);

		return this.binding.getRoot();
	}

	private static double roundEff(double eff) {
		return EffectivenessUtils.roundEff(eff);
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

	public void updateEvolutions(PkmnDesc pkmn) {
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
					PkmnDesc pkmnFound = PokedexDAO.getInstance().getPokemon(ev.getBasePkmnId(), ev.getBasePkmnForm());
					addEvol(pkmnFound, pkmn);
					addSeparateurEvol();
				}
			}

			addEvol(pkmn, pkmn);

			if (nextEvol != null) {
				Map<ClePkmn,List<Evolution>> evolByBase = nextEvol.stream().collect(Collectors.toMap(ClePkmn::getCleBaseEvol, ev -> {
					List<Evolution> l = new ArrayList<>();
					l.add(ev);
					return l;
				}, (l1, l2) -> {
					l1.addAll(l2);
					return l1;
				}, LinkedHashMap::new));
				evolByBase.forEach((_id, l) -> {

					addSeparateurEvol();
					if (l.size() > 1) {
						LinearLayout layout = new LinearLayout(getContext());
						layout.setOrientation(LinearLayout.VERTICAL);
						layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
						this.binding.pkmnDescEvolutions.addView(layout);
						for (Evolution ev : l) {
							PkmnDesc pkmnFound = PokedexDAO.getInstance().getPokemon(ev.getEvolutionId(), ev.getEvolutionForm());
							addEvol(pkmnFound, pkmn, layout);
						}
					} else {
						// == 1 pas besoin de linear layout
						for (Evolution ev : l) {
							PkmnDesc pkmnFound = PokedexDAO.getInstance().getPokemon(ev.getEvolutionId(), ev.getEvolutionForm());
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
		if (pkmnFound.equals(currentPkmn)) {
			int idColor = ResourcesCompat.getColor(getResources(), R.color.thrid_color, requireContext().getTheme());
			evolution.getRoot().setBackgroundColor(idColor);
		} else {
			evolution.getRoot().setOnClickListener(v ->
					setPkmn(pkmnFound)
			);
		}
	}

	public void updateTypeEff(PkmnDesc pkmn) {
		Map<Double,List<Type>> map = new HashMap<>();
		for (Type t : Type.values()) {
			double eff = roundEff(EffectivenessUtils.getTypeEffOnPokemon(t, pkmn));
			map.computeIfAbsent(eff, k -> new ArrayList<>()).add(t);
		}
		for (Map.Entry<Double,TypeRecyclerViewAdapter> entry : this.mapTypeEffectivenessAdapter.entrySet()) {
			double baseEff = entry.getKey();
			List<Type> lstTypeFilter = map.getOrDefault(baseEff, new ArrayList<>());
			TypeRecyclerViewAdapter adapter = entry.getValue();
			assert lstTypeFilter != null;
			adapter.filter(lstTypeFilter);
		}
	}

	public void updateMoves(PkmnDesc pkmn) {
		Comparator<PkmnMoveComplet> comparatorMove = PkmnMoveComparators.getComparatorByPps().reversed();
		Map<Move.MoveType,List<PkmnMoveComplet>> map = CollectionUtils.groupBy(PokedexDAO.getInstance().getLstPkmnMoveCompletFor(pkmn), pm -> {
			Move move = pm.getMove();
			if (move != null) {
				return pm.getMove().getMoveType();
			}
			Log.warn("Pas de move pour le PkmnMove : " + pm);
			return null;
		});
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