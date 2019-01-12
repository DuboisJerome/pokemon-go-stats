package com.pokemongostats.controller.db.pokemon;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.util.Log;

import com.pokemongostats.controller.db.DBHelper;
import com.pokemongostats.controller.db.TableDAO;
import com.pokemongostats.controller.utils.TagUtils;
import com.pokemongostats.model.bean.Evolution;
import com.pokemongostats.model.bean.Move;
import com.pokemongostats.model.bean.PkmnDesc;
import com.pokemongostats.model.bean.PkmnMove;
import com.pokemongostats.model.table.EvolutionTable;
import com.pokemongostats.model.table.MoveTable;
import com.pokemongostats.model.table.PkmnMoveTable;
import com.pokemongostats.model.table.PokedexTable;

import java.util.ArrayList;
import java.util.List;

import static com.pokemongostats.model.table.PkmnMoveTable.MOVE_ID;
import static com.pokemongostats.model.table.PkmnMoveTable.POKEDEX_NUM;
import static com.pokemongostats.model.table.PkmnMoveTable.TABLE_NAME;

public class PkmnMoveTableDAO extends TableDAO<PkmnMove> {

    public PkmnMoveTableDAO(Context pContext) {
        super(pContext);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected PkmnMove convert(Cursor c) {
        PkmnMove pm = new PkmnMove();
        pm.setMoveId(DBHelper.getLongCheckNullColumn(c, MOVE_ID));
        pm.setPokedexNum(DBHelper.getLongCheckNullColumn(c, POKEDEX_NUM));
        pm.setForme(DBHelper.getStringCheckNullColumn(c, PkmnMoveTable.FORME));

        return pm;
    }

    /**
     * @param whereClause
     * @return select query
     */
    @Override
    protected String getSelectAllQuery(final String whereClause) {
        StringBuilder b = new StringBuilder();
        b.append("SELECT *");
        b.append(" FROM ").append(TABLE_NAME);

        if (whereClause != null && !whereClause.isEmpty()) {
            b.append(" WHERE ").append(whereClause);
        }

        Log.i(TagUtils.DB, b.toString());
        return b.toString();
    }

    @NonNull
    public List<Long> getListMoveIdFor(final PkmnDesc p){
        List<Long> results = new ArrayList<>();
        String query = getSelectAllQuery(PkmnMoveTable.POKEDEX_NUM + " = " + p.getPokedexNum());
        List<PkmnMove> list = this.selectAll(query);
        for(PkmnMove pm : list){
            results.add(pm.getMoveId());
        }
        return results;
    }

    @NonNull
    public List<Long> getListPkmnNumFor(final Move m){
        List<Long> results = new ArrayList<>();
        String query = getSelectAllQuery(PkmnMoveTable.MOVE_ID + " = " + m.getId());
        List<PkmnMove> list = this.selectAll(query);
        for(PkmnMove pm : list){
            results.add(pm.getPokedexNum());
        }
        return results;
    }

    @NonNull
    @Override
    public List<PkmnMove> selectAll() {
        final String numPkmnCond = "("+ PkmnMoveTable.TABLE_NAME + "." + PkmnMoveTable.POKEDEX_NUM +"<=494 OR "+PkmnMoveTable.TABLE_NAME + "." + PkmnMoveTable.POKEDEX_NUM +" >= 808)";
        final String formeCond = PkmnMoveTable.TABLE_NAME + "." + PkmnMoveTable.FORME +" in ('NORMAL','ALOLAN')";
        return selectAll(getSelectAllQuery(numPkmnCond + " AND " + formeCond));
    }
}