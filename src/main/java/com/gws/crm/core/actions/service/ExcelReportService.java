package com.gws.crm.core.actions.service;

import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xddf.usermodel.chart.*;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExcelReportService {

    public byte[] generateCrmExcelReport() throws Exception {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {

            XSSFSheet dataSheet = workbook.createSheet("CRM Data");
            XSSFSheet chartSheet = workbook.createSheet("Charts");

            List<Activity> activities = getStaticData();

            // ====== Styles ======
            XSSFCellStyle headerStyle = workbook.createCellStyle();
            XSSFFont font = workbook.createFont();
            font.setBold(true);
            headerStyle.setFont(font);
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // ====== Table Header ======
            String[] headers = {"User", "Action", "Date"};
            XSSFRow headerRow = dataSheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                XSSFCell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // ====== Table Data ======
            int rowIndex = 1;
            for (Activity act : activities) {
                XSSFRow row = dataSheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(act.user);
                row.createCell(1).setCellValue(act.action);
                row.createCell(2).setCellValue(act.timestamp.toString());
            }
            for (int i = 0; i < headers.length; i++) dataSheet.autoSizeColumn(i);

            // ====== Summary Stats ======
            Map<String, Long> actionCount = new LinkedHashMap<>();
            Map<LocalDate, Long> dateCount = new LinkedHashMap<>();
            for (Activity a : activities) {
                actionCount.put(a.action, actionCount.getOrDefault(a.action, 0L) + 1);
                dateCount.put(a.timestamp, dateCount.getOrDefault(a.timestamp, 0L) + 1);
            }

            // ====== Write Data for Charts ======
            int chartDataStart = 0;

            // -- Action Distribution Data
            int row = chartDataStart;
            for (Map.Entry<String, Long> entry : actionCount.entrySet()) {
                XSSFRow r = chartSheet.createRow(row++);
                r.createCell(0).setCellValue(entry.getKey());
                r.createCell(1).setCellValue(entry.getValue());
            }

            // -- Draw Pie Chart
            XSSFDrawing drawing = chartSheet.createDrawingPatriarch();
            XSSFClientAnchor anchor1 = drawing.createAnchor(0, 0, 0, 0, 3, 1, 10, 20);
            XSSFChart pieChart = drawing.createChart(anchor1);
            pieChart.setTitleText("Action Distribution");
            pieChart.setTitleOverlay(false);

            XDDFChartLegend legend = pieChart.getOrAddLegend();
            legend.setPosition(LegendPosition.RIGHT);

            XDDFCategoryDataSource cat = XDDFDataSourcesFactory.fromStringCellRange(chartSheet,
                    new CellRangeAddress(chartDataStart, chartDataStart + actionCount.size() - 1, 0, 0));
            XDDFNumericalDataSource<Double> val = XDDFDataSourcesFactory.fromNumericCellRange(chartSheet,
                    new CellRangeAddress(chartDataStart, chartDataStart + actionCount.size() - 1, 1, 1));

            XDDFChartData pieData = pieChart.createData(ChartTypes.PIE, null, null);
            pieData.addSeries(cat, val);
            pieChart.plot(pieData);

            // -- Date-wise Activity Count
            int barStartRow = chartDataStart + actionCount.size() + 5;
            row = barStartRow;
            for (Map.Entry<LocalDate, Long> entry : dateCount.entrySet()) {
                XSSFRow r = chartSheet.createRow(row++);
                r.createCell(0).setCellValue(entry.getKey().toString());
                r.createCell(1).setCellValue(entry.getValue());
            }

            XSSFClientAnchor anchor2 = drawing.createAnchor(0, 0, 0, 0, 12, 1, 20, 20);
            XSSFChart barChart = drawing.createChart(anchor2);
            barChart.setTitleText("Activity per Day");

            XDDFCategoryAxis bottomAxis = barChart.createCategoryAxis(AxisPosition.BOTTOM);
            bottomAxis.setTitle("Date");

            XDDFValueAxis leftAxis = barChart.createValueAxis(AxisPosition.LEFT);
            leftAxis.setTitle("Count");

            XDDFCategoryDataSource barCat = XDDFDataSourcesFactory.fromStringCellRange(chartSheet,
                    new CellRangeAddress(barStartRow, barStartRow + dateCount.size() - 1, 0, 0));
            XDDFNumericalDataSource<Double> barVal = XDDFDataSourcesFactory.fromNumericCellRange(chartSheet,
                    new CellRangeAddress(barStartRow, barStartRow + dateCount.size() - 1, 1, 1));

            XDDFChartData barData = barChart.createData(ChartTypes.BAR, bottomAxis, leftAxis);
            barData.addSeries(barCat, barVal);
            barChart.plot(barData);

            // ====== Export Byte Array ======
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out.toByteArray();
        }
    }

    private List<Activity> getStaticData() {
        return List.of(
                new Activity("user1@gmail.com", "Login", LocalDate.of(2025, 7, 25)),
                new Activity("user2@gmail.com", "Logout", LocalDate.of(2025, 7, 25)),
                new Activity("admin@gmail.com", "Create Lead", LocalDate.of(2025, 7, 25)),
                new Activity("admin@gmail.com", "Calls", LocalDate.of(2025, 7, 26)),
                new Activity("sales@gmail.com", "Login", LocalDate.of(2025, 7, 26)),
                new Activity("sales@gmail.com", "Edit Lead", LocalDate.of(2025, 7, 26)),
                new Activity("user1@gmail.com", "Add Note", LocalDate.of(2025, 7, 27)),
                new Activity("user2@gmail.com", "Meeting", LocalDate.of(2025, 7, 27)),
                new Activity("user2@gmail.com", "Login", LocalDate.of(2025, 7, 28)),
                new Activity("user3@gmail.com", "Follow Up", LocalDate.of(2025, 7, 28))
        );
    }

    private static class Activity {
        String user;
        String action;
        LocalDate timestamp;

        Activity(String user, String action, LocalDate timestamp) {
            this.user = user;
            this.action = action;
            this.timestamp = timestamp;
        }
    }
}
