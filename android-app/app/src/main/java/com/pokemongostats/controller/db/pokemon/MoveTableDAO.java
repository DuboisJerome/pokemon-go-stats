package com.pokemongostats.controller.db.pokemon;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.pokemongostats.controller.db.DBHelper;
import com.pokemongostats.controller.db.TableDAO;
import com.pokemongostats.controller.utils.TagUtils;
import com.pokemongostats.model.bean.Move;
import com.pokemongostats.model.bean.Move.MoveType;
import com.pokemongostats.model.bean.Type;

import java.util.List;

import static com.pokemongostats.model.table.AbstractTable.LANG;
import static com.pokemongostats.model.table.MoveTable.CRITICAL_CHANCE;
import static com.pokemongostats.model.table.MoveTable.DURATION;
import static com.pokemongostats.model.table.MoveTable.DURATION_PVP;
import static com.pokemongostats.model.table.MoveTable.ENERGY_DELTA;
import static com.pokemongostats.model.table.MoveTable.ENERGY_PVP;
import static com.pokemongostats.model.table.MoveTable.ID;
import static com.pokemongostats.model.table.MoveTable.MOVE_TYPE;
import static com.pokemongostats.model.table.MoveTable.NAME;
import static com.pokemongostats.model.table.MoveTable.POWER;
import static com.pokemongostats.model.table.MoveTable.POWER_PVP;
import static com.pokemongostats.model.table.MoveTable.STAMINA_LOSS_SCALAR;
import static com.pokemongostats.model.table.MoveTable.TABLE_NAME;
import static com.pokemongostats.model.table.MoveTable.TABLE_NAME_I18N;
import static com.pokemongostats.model.table.MoveTable.TYPE;

public class MoveTableDAO extends TableDAO<Move> {

    public MoveTableDAO(Context pContext) {
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
    protected Move convert(Cursor c) {
        // num dex
        long id = DBHelper.getLongCheckNullColumn(c, ID);
        // name
        String name = DBHelper.getStringCheckNullColumn(c, NAME);
        // type
        Type type = Type
                .valueOfIgnoreCase(DBHelper.getStringCheckNullColumn(c, TYPE));
        // move type
        MoveType moveType = Move.MoveType.valueOfIgnoreCase(
                DBHelper.getStringCheckNullColumn(c, MOVE_TYPE));

        double criticalChance = DBHelper.getDoubleCheckNullColumn(c,
                CRITICAL_CHANCE);
        int duration = DBHelper.getIntCheckNullColumn(c, DURATION);
        double staminaLossScalar = DBHelper.getDoubleCheckNullColumn(c,
                STAMINA_LOSS_SCALAR);
        int energyDelta = DBHelper.getIntCheckNullColumn(c, ENERGY_DELTA);
        int power = DBHelper.getIntCheckNullColumn(c, POWER);
        int powerPvp = DBHelper.getIntCheckNullColumn(c, POWER_PVP);
        int energyPvp = DBHelper.getIntCheckNullColumn(c, ENERGY_PVP);
        int durationPvp = DBHelper.getIntCheckNullColumn(c, DURATION_PVP);

        Move m = new Move();
        m.setId(id);
        m.setType(type);
        m.setMoveType(moveType);
        m.setCriticalChance(criticalChance);
        m.setDuration(duration);
        m.setEnergyDelta(energyDelta);
        m.setName(name);
        m.setPower(power);
        m.setStaminaLossScalar(staminaLossScalar);
        m.setPowerPvp(powerPvp);
        m.setEnergyPvp(energyPvp);
        m.setDurationPvp(durationPvp);

        return m;
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

        // join pokedex lang
        b.append(" JOIN ").append(TABLE_NAME_I18N).append(" ON ");
        b.append(TABLE_NAME).append(".").append(ID);
        b.append("=");
        b.append(TABLE_NAME_I18N).append(".").append(ID);

        b.append(" WHERE ");
        b.append(TABLE_NAME_I18N).append(".").append(LANG);
        b.append(" LIKE ").append(DBHelper.toStringWithQuotes(DBHelper.getLang(getContext())));

        if (whereClause != null && !whereClause.isEmpty()) {
            b.append(" AND ").append(whereClause);
        }

        Log.i(TagUtils.DB, b.toString());
        return b.toString();
    }

    public Move getMove(final long id) {
        List<Move> results = selectAll(getSelectAllQuery(ID + "=" + id));
        return results.isEmpty() ? null : results.get(0);
    }
}