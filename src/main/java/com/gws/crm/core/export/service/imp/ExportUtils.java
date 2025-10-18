package com.gws.crm.core.export.service.imp;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.util.List;

public class ExportUtils {

    public static <T> byte[] toExcel(List<T> data) throws Exception {
        if (data == null || data.isEmpty()) {
            return new byte[0];
        }

        try (XSSFWorkbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Export");
            CreationHelper creationHelper = workbook.getCreationHelper();

            // ====== Header ======
            Row headerRow = sheet.createRow(0);
            Field[] fields = data.get(0).getClass().getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(fields[i].getName());
                CellStyle style = workbook.createCellStyle();
                Font font = workbook.createFont();
                font.setBold(true);
                style.setFont(font);
                cell.setCellStyle(style);
            }

            // ====== Data Rows ======
            int rowIdx = 1;
            for (T obj : data) {
                Row row = sheet.createRow(rowIdx++);
                for (int i = 0; i < fields.length; i++) {
                    fields[i].setAccessible(true);
                    Object value = fields[i].get(obj);
                    Cell cell = row.createCell(i);
                    if (value != null) {
                        if (value instanceof Number) {
                            cell.setCellValue(((Number) value).doubleValue());
                        } else if (value instanceof Boolean) {
                            cell.setCellValue((Boolean) value);
                        } else {
                            cell.setCellValue(creationHelper.createRichTextString(value.toString()));
                        }
                    }
                }
            }

            // Auto-size columns
            for (int i = 0; i < fields.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return out.toByteArray();
        }
    }
}
