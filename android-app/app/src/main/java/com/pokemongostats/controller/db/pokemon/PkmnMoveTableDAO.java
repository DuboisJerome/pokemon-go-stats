package com.pokemongostats.controller.db.pokemon;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.pokemongostats.controller.db.DBHelper;
import com.pokemongostats.controller.db.TableDAO;
import com.pokemongostats.controller.utils.TagUtils;
import com.pokemongostats.model.bean.PkmnMove;

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
}