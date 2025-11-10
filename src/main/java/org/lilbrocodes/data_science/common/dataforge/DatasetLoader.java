package org.lilbrocodes.data_science.common.dataforge;

import com.google.gson.*;
import dan200.computercraft.api.lua.LuaException;

import java.util.*;

public class DatasetLoader {

    public static Dataset fromCsv(String csvText) throws LuaException {
        String[] lines = csvText.split("\\r?\\n");
        if (lines.length == 0) {
            throw new LuaException("CSV text is empty");
        }

        String[] columnArray = lines[0].split(",");
        List<String> columns = new ArrayList<>();
        for (String col : columnArray) {
            columns.add(col.trim());
        }

        Dataset dataset = new Dataset(columns);

        for (int i = 1; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.isEmpty()) continue;

            String[] valuesArray = line.split(",", -1);
            List<String> values = new ArrayList<>();
            for (String val : valuesArray) {
                values.add(val.trim());
            }

            dataset.addRow(new Dataset.Row(values, columns));
        }

        return dataset;
    }

    public static Dataset fromJsonArray(String jsonArrayText) throws LuaException {
        JsonElement element = JsonParser.parseString(jsonArrayText);
        if (!element.isJsonArray()) {
            throw new LuaException("JSON is not an array");
        }

        JsonArray array = element.getAsJsonArray();
        if (array.isEmpty()) {
            return new Dataset(Collections.emptyList());
        }

        JsonObject firstObj = array.get(0).getAsJsonObject();
        Set<String> commonKeys = new HashSet<>(firstObj.keySet());

        for (int i = 1; i < array.size(); i++) {
            JsonObject obj = array.get(i).getAsJsonObject();
            commonKeys.retainAll(obj.keySet());
        }

        List<String> columns = new ArrayList<>(commonKeys);
        Dataset dataset = new Dataset(columns);

        for (JsonElement je : array) {
            JsonObject obj = je.getAsJsonObject();
            List<String> rowValues = new ArrayList<>();
            for (String col : columns) {
                rowValues.add(obj.get(col).getAsString());
            }
            dataset.addRow(new Dataset.Row(rowValues, columns));
        }

        return dataset;
    }

    public static String toCsv(Dataset dataset) {
        StringBuilder sb = new StringBuilder();

        sb.append(String.join(",", dataset.getColumns()));
        sb.append("\n");

        for (Dataset.Row row : dataset.getRows()) {
            List<String> values = new ArrayList<>();
            for (int i = 0; i < dataset.getColumns().size(); i++) {
                String val = row.get(i);
                if (val.contains(",") || val.contains("\"")) {
                    val = "\"" + val.replace("\"", "\"\"") + "\"";
                }
                values.add(val);
            }
            sb.append(String.join(",", values));
            sb.append("\n");
        }

        return sb.toString();
    }

    public static String toJsonArray(Dataset dataset) {
        JsonArray array = new JsonArray();

        for (Dataset.Row row : dataset.getRows()) {
            JsonObject obj = new JsonObject();
            List<String> columns = dataset.getColumns();
            for (int i = 0; i < columns.size(); i++) {
                obj.addProperty(columns.get(i), row.get(i));
            }
            array.add(obj);
        }

        return array.toString();
    }
}
