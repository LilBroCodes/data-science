package org.lilbrocodes.data_science.common.dataforge;

import org.lilbrocodes.composer_reloaded.api.util.Vec2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dataset {
    private final List<String> columns;
    private final List<Row> rows;

    public Dataset(List<String> columns) {
        this.columns = new ArrayList<>(columns);
        this.rows = new ArrayList<>();
    }

    public List<String> getColumns() {
        return columns;
    }

    public List<Row> getRows() {
        return rows;
    }

    public Dataset head(int n) {
        Dataset out = new Dataset(columns);
        if (n > getRows().size()) n = getRows().size() - 1;

        for (int i = 0; i < n; i++) {
            out.addRow(rows.get(i));
        }

        return out;
    }

    public Dataset tail(int n) {
        Dataset out = new Dataset(columns);
        int size = rows.size();

        if (n > size) n = size - 1;
        if (n < 0) n = 0;

        int start = size - n;
        for (int i = start; i < size; i++) {
            out.addRow(rows.get(i));
        }

        return out;
    }

    public Vec2 shape() {
        return new Vec2(rows.size(), columns.size());
    }

    public List<Integer> luaShape() {
        return List.of(rows.size(), columns.size());
    }

    public void addRow(Row row) {
        if (row.size() != columns.size()) {
            throw new IllegalArgumentException("Row size does not match column count");
        }
        rows.add(row);
    }

    public static class Row {
        private final List<String> values;
        private final Map<String, String> columnMap;

        public Row(List<String> values, List<String> columns) {
            if (values.size() != columns.size()) {
                throw new IllegalArgumentException("Values size must match columns size");
            }
            this.values = new ArrayList<>(values);
            this.columnMap = new HashMap<>();
            for (int i = 0; i < columns.size(); i++) {
                columnMap.put(columns.get(i), values.get(i));
            }
        }

        public String get(int index) {
            return values.get(index);
        }

        public String get(String columnName) {
            return columnMap.get(columnName);
        }

        public int size() {
            return values.size();
        }

        @Override
        public String toString() {
            return values.toString();
        }
    }
}

