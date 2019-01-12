package com.pokemongostats.controller.db.pokemon;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.util.Log;

import com.pokemongostats.controller.dao.PokedexDAO;
import com.pokemongostats.controller.db.DBHelper;
import com.pokemongostats.controller.db.TableDAO;
import com.pokemongostats.controller.utils.TagUtils;
import com.pokemongostats.model.bean.Evolution;
import com.pokemongostats.model.bean.Move;
import com.pokemongostats.model.bean.PkmnDesc;
import com.pokemongostats.model.bean.PkmnMove;
import com.pokemongostats.model.bean.Type;
import com.pokemongostats.model.table.PokedexTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class PokedexTableDAO extends TableDAO<PkmnDesc> {

    private PokedexDAO dao;

    public PokedexTableDAO(Context pContext) {
        super(pContext);
        dao = new PokedexDAO(pContext);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTableName() {
        return PokedexTable.TABLE_NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected PkmnDesc convert(Cursor c) {
        // num dex
        long pokedexNum = DBHelper.getLongCheckNullColumn(c,  PokedexTable.POKEDEX_NUM);

        String forme  = DBHelper.getStringCheckNullColumn(c,  PokedexTable.FORME);
        // name
        String name = DBHelper.getStringCheckNullColumn(c,  PokedexTable.NAME);
        // type1
        Type type1 = Type
                .valueOfIgnoreCase(DBHelper.getStringCheckNullColumn(c,  PokedexTable.TYPE1));
        // type2
        Type type2 = Type
                .valueOfIgnoreCase(DBHelper.getStringCheckNullColumn(c,  PokedexTable.TYPE2));

        int physicalAttack = DBHelper.getIntCheckNullColumn(c, PokedexTable.PHYSICAL_ATTACK);
        int physicalDefense = DBHelper.getIntCheckNullColumn(c, PokedexTable.PHYSICAL_DEFENSE);
        int specialAttack = DBHelper.getIntCheckNullColumn(c, PokedexTable.SPECIAL_ATTACK);
        int specialDefense = DBHelper.getIntCheckNullColumn(c, PokedexTable.SPECIAL_DEFENSE);
        int pv = DBHelper.getIntCheckNullColumn(c, PokedexTable.PV);
        int speed = DBHelper.getIntCheckNullColumn(c, PokedexTable.SPEED);

        double kmsPerCandy = DBHelper.getDoubleCheckNullColumn(c,
                PokedexTable.KMS_PER_CANDY);

        double kmsPerEgg = DBHelper.getDoubleCheckNullColumn(c,  PokedexTable.KMS_PER_EGG);

        // i18n
        String family = DBHelper.getStringCheckNullColumn(c,  PokedexTable.FAMILY);
        String description = DBHelper.getStringCheckNullColumn(c,  PokedexTable.DESCRIPTION);

        PkmnDesc p = new PkmnDesc();
        p.setPokedexNum(pokedexNum);
        p.setForme(forme);
        p.setName(name);
        p.setType1(type1);
        p.setType2(type2);
        p.setFamily(family);
        p.setDescription(description);
        p.setKmsPerCandy(kmsPerCandy);
        p.setKmsPerEgg(kmsPerEgg);
        p.setPhysicalAttack(physicalAttack);
        p.setPhysicalDefense(physicalDefense);
        p.setSpecialAttack(specialAttack);
        p.setSpecialDefense(specialDefense);
        p.setPv(pv);
        p.setSpeed(speed);

        return p;
    }

    @Override
    public Long[] insertOrReplaceImpl(final PkmnDesc... bos) {
        List<Long> result = new ArrayList<>();
        // FIXME
        return result.toArray(new Long[result.size()]);
    }

    @Override
    public int removeFromObjects(PkmnDesc... objects) {
        return 0;
    }

    @NonNull
    @Override
    public List<PkmnDesc> selectAll() {
        final String numPkmnCond = "("+PokedexTable.TABLE_NAME + "." + PokedexTable.POKEDEX_NUM +"<=494 OR "+PokedexTable.TABLE_NAME + "." + PokedexTable.POKEDEX_NUM +" >= 808)";
        final String formeCond = PokedexTable.TABLE_NAME + "." + PokedexTable.FORME +" in ('NORMAL','ALOLAN')";
        return selectAll(getSelectAllQuery(numPkmnCond + " AND " + formeCond));
    }

    /**
     * @param whereClause
     * @return select query
     */
    @Override
    protected String getSelectAllQuery(final String whereClause) {
        StringBuilder b = new StringBuilder();
        b.append("SELECT *");
        b.append(" FROM ").append(PokedexTable.TABLE_NAME);

        // join pokedex lang
        b.append(" JOIN ").append(PokedexTable.TABLE_NAME_I18N).append(" ON ");
        b.append(" ( ");
        b.append(PokedexTable.TABLE_NAME).append(".").append(PokedexTable.POKEDEX_NUM);
        b.append("=");
        b.append(PokedexTable.TABLE_NAME_I18N).append(".").append(PokedexTable.POKEDEX_NUM);
        b.append(" AND ");
        b.append(PokedexTable.TABLE_NAME).append(".").append(PokedexTable.FORME);
        b.append("=");
        b.append(PokedexTable.TABLE_NAME_I18N).append(".").append(PokedexTable.FORME);
        b.append(" AND ");
        b.append(PokedexTable.TABLE_NAME_I18N).append(".").append(PokedexTable.LANG);
        b.append(" LIKE ").append(DBHelper.toStringWithQuotes(DBHelper.getLang(getContext())));
        b.append(" ) ");

        if (whereClause != null && !whereClause.isEmpty()) {
            b.append(" WHERE ").append(whereClause);
        }

        Log.i(TagUtils.DB, b.toString());
        return b.toString();
    }

    public PkmnDesc getPkmnDesc(final long pokedexNum){
        List<PkmnDesc> results = selectAll(getSelectAllQuery(PokedexTable.POKEDEX_NUM +"="+pokedexNum));
        return results.isEmpty() ? null : results.get(0);
    }
}