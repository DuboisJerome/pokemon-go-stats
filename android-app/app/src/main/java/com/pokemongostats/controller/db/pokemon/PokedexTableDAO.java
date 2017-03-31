package com.pokemongostats.controller.db.pokemon;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.pokemongostats.controller.db.DBHelper;
import com.pokemongostats.controller.db.TableDAO;
import com.pokemongostats.controller.utils.ConstantsUtils;
import com.pokemongostats.controller.utils.TagUtils;
import com.pokemongostats.model.bean.PkmnDesc;
import com.pokemongostats.model.bean.PkmnMove;
import com.pokemongostats.model.bean.Type;
import com.pokemongostats.view.PkmnGoStatsApplication;

import java.util.ArrayList;
import java.util.List;

import static com.pokemongostats.model.table.AbstractTable.LANG;
import static com.pokemongostats.model.table.PokedexTable.BASE_ATTACK;
import static com.pokemongostats.model.table.PokedexTable.BASE_DEFENSE;
import static com.pokemongostats.model.table.PokedexTable.BASE_STAMINA;
import static com.pokemongostats.model.table.PokedexTable.CANDY_TO_EVOLVE;
import static com.pokemongostats.model.table.PokedexTable.DESCRIPTION;
import static com.pokemongostats.model.table.PokedexTable.FAMILY;
import static com.pokemongostats.model.table.PokedexTable.KMS_PER_CANDY;
import static com.pokemongostats.model.table.PokedexTable.KMS_PER_EGG;
import static com.pokemongostats.model.table.PokedexTable.MAX_CP;
import static com.pokemongostats.model.table.PokedexTable.NAME;
import static com.pokemongostats.model.table.PokedexTable.POKEDEX_NUM;
import static com.pokemongostats.model.table.PokedexTable.TABLE_NAME;
import static com.pokemongostats.model.table.PokedexTable.TABLE_NAME_I18N;
import static com.pokemongostats.model.table.PokedexTable.TYPE1;
import static com.pokemongostats.model.table.PokedexTable.TYPE2;

public class PokedexTableDAO extends TableDAO<PkmnDesc> {

    private List<PkmnMove> allPkmnMoves = null;

    public PokedexTableDAO(Context pContext) {
        super(pContext);
        PkmnGoStatsApplication app = ((PkmnGoStatsApplication) pContext
                .getApplicationContext());
        this.allPkmnMoves = app.getAllPkmnMoves();
    }

    public PokedexTableDAO(Context pContext, List<PkmnMove> pkmnMoves) {
        super(pContext);
        this.allPkmnMoves = pkmnMoves;
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
    protected PkmnDesc convert(Cursor c) {
        // num dex
        long pokedexNum = DBHelper.getLongCheckNullColumn(c, POKEDEX_NUM);
        // name
        String name = DBHelper.getStringCheckNullColumn(c, NAME);
        // type1
        Type type1 = Type
                .valueOfIgnoreCase(DBHelper.getStringCheckNullColumn(c, TYPE1));
        // type2
        Type type2 = Type
                .valueOfIgnoreCase(DBHelper.getStringCheckNullColumn(c, TYPE2));

        double kmsPerCandy = DBHelper.getDoubleCheckNullColumn(c,
                KMS_PER_CANDY);

        double kmsPerEgg = DBHelper.getDoubleCheckNullColumn(c, KMS_PER_EGG);

        double baseAtt = DBHelper.getDoubleCheckNullColumn(c, BASE_ATTACK);
        double baseDef = DBHelper.getDoubleCheckNullColumn(c, BASE_DEFENSE);
        double baseStamina = DBHelper.getDoubleCheckNullColumn(c, BASE_STAMINA);

        int candyToEvolve = DBHelper.getIntCheckNullColumn(c, CANDY_TO_EVOLVE);

        double maxCP = DBHelper.getDoubleCheckNullColumn(c, MAX_CP);

        // i18n
        String family = DBHelper.getStringCheckNullColumn(c, FAMILY);
        String description = DBHelper.getStringCheckNullColumn(c, DESCRIPTION);

        PkmnGoStatsApplication app = ((PkmnGoStatsApplication) getContext()
                .getApplicationContext());

        // retrieve evolutions
        List<Long> evolutionIds = new ArrayList<>();
        evolutionIds.addAll(app.getFamillePokemon(pokedexNum));

        // retrieve moves
        List<Long> moveIds = new ArrayList<>();
        if (allPkmnMoves != null && !allPkmnMoves.isEmpty()) {
            for (PkmnMove pm : allPkmnMoves) {
                if (pokedexNum == pm.getPokedexNum()) {
                    moveIds.add(pm.getMoveId());
                }
            }
        }

        PkmnDesc p = new PkmnDesc();
        p.setPokedexNum(pokedexNum);
        p.setName(name);
        p.setType1(type1);
        p.setType2(type2);
        p.setFamily(family);
        p.setDescription(description);
        p.setEvolutionIds(evolutionIds);
        p.setMoveIds(moveIds);
        p.setKmsPerCandy(kmsPerCandy);
        p.setKmsPerEgg(kmsPerEgg);
        p.setBaseAttack(baseAtt);
        p.setBaseDefense(baseDef);
        p.setBaseStamina(baseStamina);
        p.setCandyToEvolve(candyToEvolve);
        p.setMaxCP(maxCP);

        return p;
    }

    @Override
    public Long[] insertOrReplace(final PkmnDesc... bos) {
        List<Long> result = new ArrayList<>();
        return result.toArray(new Long[result.size()]);
    }

    @Override
    public int removeFromObjects(PkmnDesc... objects) {
        return 0;
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
        b.append(" ( ");
        b.append(TABLE_NAME).append(".").append(POKEDEX_NUM);
        b.append("=");
        b.append(TABLE_NAME_I18N).append(".").append(POKEDEX_NUM);
        b.append(" AND ");
        b.append(TABLE_NAME_I18N).append(".").append(LANG);
        b.append(" LIKE ").append(DBHelper.toStringWithQuotes(ConstantsUtils.FR));
        b.append(" ) ");

        if (whereClause != null && !whereClause.isEmpty()) {
            b.append(" WHERE ").append(whereClause);
        }

        Log.i(TagUtils.DB, b.toString());
        return b.toString();
    }
}