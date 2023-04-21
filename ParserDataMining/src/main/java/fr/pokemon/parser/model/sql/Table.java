package fr.pokemon.parser.model.sql;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Getter
public class Table<T> {
    @Setter
    private String name;
    private final Set<Column<T, ?>> columns = new LinkedHashSet<>();

    public Table(String name) {
        this.name = name;
    }

    public <R> void addCol(Column<T, R> col) {
        columns.add(col);
    }

    public <R> void addCol(String colName, Function<T, R> toVal) {
        columns.add(new Column<T, R>(colName, false, false, toVal, null));
    }

    public <R> void addColKey(String colName, Function<T, R> toVal) {
        columns.add(new Column<T, R>(colName, true, false, toVal, null));
    }

    public <R> void addColString(String colName, Function<T, R> toVal) {
        columns.add(new Column<T, R>(colName, false, true, toVal, null));
    }

    public <R> void addColKeyString(String colName, Function<T, R> toVal) {
        columns.add(new Column<T, R>(colName, true, true, toVal, null));
    }

    public <R> void addCol(String colName, Function<T, R> toVal, Function<R, String> toSql) {
        columns.add(new Column<T, R>(colName, false, false, toVal, toSql));
    }

    public <R> void addColKey(String colName, Function<T, R> toVal, Function<R, String> toSql) {
        columns.add(new Column<T, R>(colName, true, false, toVal, toSql));
    }

    public <R> void addColString(String colName, Function<T, R> toVal, Function<R, String> toSql) {
        columns.add(new Column<T, R>(colName, false, true, toVal, toSql));
    }

    public <R> void addColKeyString(String colName, Function<T, R> toVal, Function<R, String> toSql) {
        columns.add(new Column<T, R>(colName, true, true, toVal, toSql));
    }

    public <R> Set<Column<T, ?>> getColumnsKey() {
        return this.columns.stream().filter(Column::isPK).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public Set<Column<T, ?>> getColumnsNonKey() {
        return this.columns.stream().filter(Predicate.not(Column::isPK)).collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
