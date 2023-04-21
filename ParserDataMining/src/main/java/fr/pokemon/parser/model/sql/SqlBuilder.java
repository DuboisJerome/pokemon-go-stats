package fr.pokemon.parser.model.sql;

import java.util.stream.Collectors;

public class SqlBuilder<T> {
    public static final int MODE_INSERT = 0;
    public static final int MODE_UPDATE = 1;
    public static final int MODE_BOTH = 2;

    Table<T> table;
    int mode;

    public SqlBuilder(Table<T> tbl, int mode) {
        this.table = tbl;
        this.mode = mode;
    }

    public String toAssign(T rec, Column<T, ?> col) {
        return col.getName() + "=" + col.toSql(rec);
    }

    public String buildUpdate(T rec) throws SqlBuilderException {
        var colsExistValuesPK = this.table.getColumnsKey();
        var colsExistValuesNoPK = this.table.getColumnsNonKey();
        if (colsExistValuesNoPK.isEmpty()) {
            throw new SqlBuilderException("La table " + table.getName() + " n'a pas de colonnes clées");
        }
        var valToSetStr = colsExistValuesNoPK.stream().map(c -> this.toAssign(rec, c)).collect(Collectors.joining(","));
        var conds = colsExistValuesPK.stream().map(c -> this.toAssign(rec, c)).collect(Collectors.joining(" AND "));
        var whereClause = !conds.isEmpty() ? (" WHERE " + conds) : "";
        return "UPDATE " + this.table.getName() + " SET " + valToSetStr + whereClause + ";";
    }

    public String buildInsert(T rec) throws SqlBuilderException {
        var colsPKNoValues = this.table.getColumnsKey().stream().filter(c -> c.toVal(rec) == null).toList();
        if (colsPKNoValues.size() > 0) {
            throw new SqlBuilderException("Pas de PK dans le record");
        }
        var colsValues = this.table.getColumns().stream().filter(c -> c.toVal(rec) != null).toList();
        var colsName = colsValues.stream().map(Column::getName).collect(Collectors.joining(","));
        var values = colsValues.stream().map(c -> c.toSql(rec)).collect(Collectors.joining(","));
        return "INSERT OR IGNORE INTO " + this.table.getName() + " (" + colsName + ") VALUES (" + values + ");";
    }

    public String buildReq(T rec) throws SqlBuilderException {
        switch (this.mode) {
            case MODE_INSERT:
                return this.buildInsert(rec);
            case MODE_UPDATE:
                return this.buildUpdate(rec);
            default:
                var insertPart = this.buildInsert(rec);
                var updatePart = this.buildUpdate(rec);
                return insertPart + "\n" + updatePart;
        }
    }
}
