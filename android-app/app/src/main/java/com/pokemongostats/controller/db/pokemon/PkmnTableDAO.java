package com.pokemongostats.controller.db.pokemon;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import androidx.annotation.NonNull;

import com.pokemongostats.controller.dao.PokedexDAO;
import com.pokemongostats.controller.db.DBHelper;
import com.pokemongostats.controller.db.TableDAO;
import com.pokemongostats.controller.utils.TagUtils;
import com.pokemongostats.model.bean.PkmnDesc;
import com.pokemongostats.model.bean.Type;
import com.pokemongostats.model.table.PkmnTable;

import java.util.ArrayList;
import java.util.List;


public class PkmnTableDAO extends TableDAO<PkmnDesc> {

    public PkmnTableDAO(Context pContext) {
        super(pContext);
        PokedexDAO dao = new PokedexDAO(pContext);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTableName() {
        return PkmnTable.TABLE_NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected PkmnDesc convert(Cursor c) {
        // num dex
        long pokedexNum = DBHelper.getLongCheckNullColumn(c, PkmnTable.ID);

        String form = DBHelper.getStringCheckNullColumn(c, PkmnTable.FORM);
        // name
        String name = DBHelper.getStringCheckNullColumn(c, PkmnTable.NAME);
        // type1
        Type type1 = Type
                .valueOfIgnoreCase(DBHelper.getStringCheckNullColumn(c, PkmnTable.TYPE1));
        // type2
        Type type2 = Type
                .valueOfIgnoreCase(DBHelper.getStringCheckNullColumn(c, PkmnTable.TYPE2));

        int physicalAttack = DBHelper.getIntCheckNullColumn(c, PkmnTable.PHYSICAL_ATTACK);
        int physicalDefense = DBHelper.getIntCheckNullColumn(c, PkmnTable.PHYSICAL_DEFENSE);
        int specialAttack = DBHelper.getIntCheckNullColumn(c, PkmnTable.SPECIAL_ATTACK);
        int specialDefense = DBHelper.getIntCheckNullColumn(c, PkmnTable.SPECIAL_DEFENSE);
        int pv = DBHelper.getIntCheckNullColumn(c, PkmnTable.PV);
        int speed = DBHelper.getIntCheckNullColumn(c, PkmnTable.SPEED);

        int stamina = DBHelper.getIntCheckNullColumn(c, PkmnTable.STAMINA);
        int attack = DBHelper.getIntCheckNullColumn(c, PkmnTable.ATTACK);
        int defense = DBHelper.getIntCheckNullColumn(c, PkmnTable.DEFENSE);

        double kmsPerCandy = DBHelper.getDoubleCheckNullColumn(c,
                PkmnTable.KMS_PER_CANDY);

        double kmsPerEgg = DBHelper.getDoubleCheckNullColumn(c, PkmnTable.KMS_PER_EGG);

        boolean isLegendary = DBHelper.getIntCheckNullColumn(c, PkmnTable.IS_LEGENDARY) == 1;

        // i18n
        String family = DBHelper.getStringCheckNullColumn(c, PkmnTable.FAMILY);
        String description = DBHelper.getStringCheckNullColumn(c, PkmnTable.DESCRIPTION);

        PkmnDesc p = new PkmnDesc();
        p.setPokedexNum(pokedexNum);
        p.setForm(form);
        p.setName(name);
        p.setType1(type1);
        p.setType2(type2);
        p.setFamily(family);
        p.setDescription(description);
        p.setKmsPerCandy(kmsPerCandy);
        p.setKmsPerEgg(kmsPerEgg);
        p.setLegendary(isLegendary);

        p.setStamina(stamina);
        p.setAttack(attack);
        p.setDefense(defense);

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
        return result.toArray(new Long[0]);
    }

    @Override
    public int removeFromObjects() {
        return 0;
    }

    @NonNull
    @Override
    public List<PkmnDesc> selectAll() {
        return selectAll(getSelectAllQuery(null));
    }

    /**
     * @param whereClause w
     * @return select query
     */
    @Override
    protected String getSelectAllQuery(final String whereClause) {
        StringBuilder b = new StringBuilder();
        b.append("SELECT *");
        b.append(" FROM ").append(PkmnTable.TABLE_NAME);

        // join pokedex lang
        b.append(" JOIN ").append(PkmnTable.TABLE_NAME_I18N).append(" ON ");
        b.append(" ( ");
        b.append(PkmnTable.TABLE_NAME).append(".").append(PkmnTable.ID);
        b.append("=");
        b.append(PkmnTable.TABLE_NAME_I18N).append(".").append(PkmnTable.ID);
        b.append(" AND ");
        b.append(PkmnTable.TABLE_NAME).append(".").append(PkmnTable.FORM);
        b.append("=");
        b.append(PkmnTable.TABLE_NAME_I18N).append(".").append(PkmnTable.FORM);
        b.append(" AND ");
        b.append(PkmnTable.TABLE_NAME_I18N).append(".").append(PkmnTable.LANG);
        b.append(" LIKE ").append(DBHelper.toStringWithQuotes(DBHelper.getLang(getContext())));
        b.append(" ) ");

        if (whereClause != null && !whereClause.isEmpty()) {
            b.append(" WHERE ").append(whereClause);
        }
        b.append(" ORDER BY ");
        b.append(PkmnTable.TABLE_NAME).append(".").append(PkmnTable.ID).append(",");
        b.append(PkmnTable.TABLE_NAME).append(".").append(PkmnTable.FORM).append(" DESC");

        Log.i(TagUtils.DB, b.toString());
        return b.toString();
    }

    public PkmnDesc getPkmnDesc(final long pokedexNum) {
        List<PkmnDesc> results = selectAll(getSelectAllQuery(PkmnTable.ID + "=" + pokedexNum));
        return results.isEmpty() ? null : results.get(0);
    }
}