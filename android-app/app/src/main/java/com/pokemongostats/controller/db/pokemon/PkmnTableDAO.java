package com.pokemongostats.controller.db.pokemon;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.pokemongostats.controller.db.DBHelper;
import com.pokemongostats.controller.db.TableDAO;
import com.pokemongostats.controller.db.trainer.TrainerTableDAO;
import com.pokemongostats.model.bean.Pkmn;

import static com.pokemongostats.model.table.AbstractTable.ID;
import static com.pokemongostats.model.table.PkmnTable.ATTACK_IV;
import static com.pokemongostats.model.table.PkmnTable.CP;
import static com.pokemongostats.model.table.PkmnTable.DEFENSE_IV;
import static com.pokemongostats.model.table.PkmnTable.HP;
import static com.pokemongostats.model.table.PkmnTable.LEVEL;
import static com.pokemongostats.model.table.PkmnTable.NICKNAME;
import static com.pokemongostats.model.table.PkmnTable.OWNER_ID;
import static com.pokemongostats.model.table.PkmnTable.POKEDEX_NUM;
import static com.pokemongostats.model.table.PkmnTable.STAMINA_IV;
import static com.pokemongostats.model.table.PkmnTable.TABLE_NAME;

public class PkmnTableDAO extends TableDAO<Pkmn> {

    public PkmnTableDAO(Context pContext) {
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
    protected Pkmn convert(Cursor c) {

        // pokemon id
        long pokedexNum = DBHelper.getLongCheckNullColumn(c, POKEDEX_NUM);

        Pkmn p = new Pkmn();
        // pokedex num
        p.setPokedexNum(pokedexNum);
        // id
        p.setId(DBHelper.getIntCheckNullColumn(c, ID));
        // name
        p.setNickname(DBHelper.getStringCheckNullColumn(c, NICKNAME));
        // cp
        p.setCP(DBHelper.getIntCheckNullColumn(c, CP));
        // hp
        p.setHP(DBHelper.getIntCheckNullColumn(c, HP));
        // defenseIV
        p.setDefenseIV(DBHelper.getIntCheckNullColumn(c, DEFENSE_IV));
        // attackIV
        p.setAttackIV(DBHelper.getIntCheckNullColumn(c, ATTACK_IV));
        // staminaIV
        p.setStaminaIV(DBHelper.getIntCheckNullColumn(c, STAMINA_IV));
        // level
        p.setLevel(DBHelper.getFloatCheckNullColumn(c, LEVEL));

        // owner id
        long ownerId = DBHelper.getLongCheckNullColumn(c, OWNER_ID);
        p.setOwner(new TrainerTableDAO(getContext()).selectFromRowID(ownerId));

        return p;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ContentValues getContentValues(final Pkmn p) {
        Long pokedexNum = p.getPokedexNum();
        Integer cp = p.getCP();
        Integer hp = p.getHP();
        Integer defenseIV = p.getDefenseIV();
        Integer attackIV = p.getAttackIV();
        Integer staminaIV = p.getStaminaIV();
        Float level = p.getLevel();
        Long ownerID = DBHelper.getIdForDB(p.getOwner());
        String nickname = p.getNickname();

        ContentValues initialValues = new ContentValues();
        initialValues.put(ID, DBHelper.getIdForDB(p));
        initialValues.put(POKEDEX_NUM, pokedexNum);
        initialValues.put(LEVEL, level);
        initialValues.put(CP, cp);
        initialValues.put(HP, hp);
        initialValues.put(DEFENSE_IV, defenseIV);
        initialValues.put(ATTACK_IV, attackIV);
        initialValues.put(STAMINA_IV, staminaIV);
        initialValues.put(LEVEL, level);
        initialValues.put(OWNER_ID, ownerID);
        initialValues.put(NICKNAME, nickname);

        return initialValues;
    }

    @Override
    public int removeFromObjects(Pkmn... objects) {
        // TODO Auto-generated method stub
        return 0;
    }

}