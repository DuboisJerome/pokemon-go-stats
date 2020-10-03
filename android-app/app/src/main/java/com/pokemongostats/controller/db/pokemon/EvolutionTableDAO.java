package com.pokemongostats.controller.db.pokemon;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.pokemongostats.controller.db.DBHelper;
import com.pokemongostats.controller.db.TableDAO;
import com.pokemongostats.controller.utils.TagUtils;
import com.pokemongostats.model.bean.Evolution;
import com.pokemongostats.model.table.EvolutionTable;

public class EvolutionTableDAO extends TableDAO<Evolution> {

    public EvolutionTableDAO(Context pContext) {
        super(pContext);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTableName() {
        return EvolutionTable.TABLE_NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Evolution convert(Cursor c) {
        Evolution e = new Evolution();
        e.setEvolutionId(DBHelper.getLongCheckNullColumn(c, EvolutionTable.EVOLUTION_ID));
        e.setEvolutionForm(DBHelper.getStringCheckNullColumn(c, EvolutionTable.EVOLUTION_FORM));
        e.setBasePkmnId(DBHelper.getLongCheckNullColumn(c, EvolutionTable.BASE_PKMN_ID));
        e.setBasePkmnForm(DBHelper.getStringCheckNullColumn(c, EvolutionTable.BASE_PKMN_FORM));
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
        b.append(" FROM ").append(EvolutionTable.TABLE_NAME);

        if (whereClause != null && !whereClause.isEmpty()) {
            b.append(" WHERE ").append(whereClause);
        }

        Log.i(TagUtils.DB, b.toString());
        return b.toString();
    }
}