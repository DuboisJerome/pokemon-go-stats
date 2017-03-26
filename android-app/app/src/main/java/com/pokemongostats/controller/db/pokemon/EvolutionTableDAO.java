package com.pokemongostats.controller.db.pokemon;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.pokemongostats.controller.db.DBHelper;
import com.pokemongostats.controller.db.TableDAO;
import com.pokemongostats.controller.utils.TagUtils;
import com.pokemongostats.model.bean.Evolution;

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
        e.setPokedexNum(DBHelper.getLongCheckNullColumn(c, POKEDEX_NUM));

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
}