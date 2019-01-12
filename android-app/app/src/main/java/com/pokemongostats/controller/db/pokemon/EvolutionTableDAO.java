package com.pokemongostats.controller.db.pokemon;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.util.Log;

import com.pokemongostats.controller.db.DBHelper;
import com.pokemongostats.controller.db.TableDAO;
import com.pokemongostats.controller.utils.TagUtils;
import com.pokemongostats.model.bean.Evolution;
import com.pokemongostats.model.bean.PkmnDesc;
import com.pokemongostats.model.table.EvolutionTable;
import com.pokemongostats.model.table.PokedexTable;

import java.util.List;

import static com.pokemongostats.model.table.EvolutionTable.EVOLUTION_ID;
import static com.pokemongostats.model.table.EvolutionTable.POKEDEX_NUM;
import static com.pokemongostats.model.table.EvolutionTable.TABLE_NAME;

public class EvolutionTableDAO extends TableDAO<Evolution> {

    public EvolutionTableDAO(Context pContext) {
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
    protected Evolution convert(Cursor c) {
        Evolution e = new Evolution();
        e.setEvolutionId(DBHelper.getLongCheckNullColumn(c, EVOLUTION_ID));
        e.setFormeEvolution(DBHelper.getStringCheckNullColumn(c, EvolutionTable.FORME_EVOLUTION));
        e.setPokedexNum(DBHelper.getLongCheckNullColumn(c, POKEDEX_NUM));
        e.setForme(DBHelper.getStringCheckNullColumn(c, EvolutionTable.FORME));
        return e;
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
    @Override
    public List<Evolution> selectAll() {
        final String numPkmnCond1 = "("+ EvolutionTable.TABLE_NAME + "." + EvolutionTable.POKEDEX_NUM +"<=494 OR "+EvolutionTable.TABLE_NAME + "." + EvolutionTable.POKEDEX_NUM +" >= 808)";
        final String numPkmnCond2 = "("+ EvolutionTable.TABLE_NAME + "." + EvolutionTable.EVOLUTION_ID +"<=494 OR "+EvolutionTable.TABLE_NAME + "." + EvolutionTable.EVOLUTION_ID +" >= 808)";
        final String formeCond1 = EvolutionTable.TABLE_NAME + "." + EvolutionTable.FORME +" in ('NORMAL','ALOLAN')";
        final String formeCond2 = EvolutionTable.TABLE_NAME + "." + EvolutionTable.FORME_EVOLUTION +" in ('NORMAL','ALOLAN')";
        return selectAll(getSelectAllQuery(numPkmnCond1 + " AND " + numPkmnCond2 + " AND " +formeCond1 + " AND " +formeCond2));
    }
}