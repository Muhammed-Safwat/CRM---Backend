package com.gws.crm.common.utils;


import com.gws.crm.common.entities.ExcelFileColumn;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ExcelFileUtils {

    public static List<ExcelFileColumn> generateHeader(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .map(ExcelFileUtils::createColumn)
                .collect(Collectors.toList());
    }

    private static ExcelFileColumn createColumn(Field field) {
        return ExcelFileColumn.builder()
                .header(capitalize(field.getName()))
                .key(field.getName())
                .width(45)
                .build();
    }

    private static String capitalize(String name) {
        if (name == null || name.isEmpty()) {
            return name;
        }
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }
}
