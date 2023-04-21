package fr.pokemon.parser.model.sql;

import lombok.Getter;

import java.util.function.Function;

public class Column<T, R> {
    @Getter
    private String name;
    @Getter
    private boolean isPK;
    @Getter
    private boolean isString;
    private Function<T, R> toVal;
    private Function<R, String> toSql;

    public Column(String name, boolean isPK, boolean isString, Function<T, R> toVal, Function<R, String> toSql) {
        this.name = name;
        this.isPK = isPK;
        this.isString = isString;
        this.toVal = toVal;
        if (this.toVal == null) {
            this.toVal = t -> null;
        }
        this.toSql = toSql;
        if (this.toSql == null) {
            this.toSql = t -> t != null ? String.valueOf(t) : null;
        }
    }

    public R toVal(T rec) {
        return this.toVal.apply(rec);
    }

    public String toSql(T rec) {
        String valTransform = this.toSql.apply(this.toVal.apply(rec));
        return isString && valTransform != null ? ("'" + valTransform + "'") : valTransform;
    }
}
