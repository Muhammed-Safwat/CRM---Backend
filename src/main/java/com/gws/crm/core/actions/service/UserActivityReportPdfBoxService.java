package com.gws.crm.core.actions.service;

import com.gws.crm.core.actions.dtos.UserActivityDTO;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("pdfbox")
public class UserActivityReportPdfBoxService implements UserActivityReportGenerator {

    private static final float MARGIN = 50;

    @Override
    public void generatePdfReport(List<UserActivityDTO> activities, OutputStream out) throws Exception {
        try (PDDocument document = new PDDocument()) {
            addCoverPage(document);
            addActivityTable(document, activities);
            addPieChartPage(document, activities);

            document.save(out);
        }
    }

    private void addCoverPage(PDDocument document) throws Exception {
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);

        try (PDPageContentStream cs = new PDPageContentStream(document, page)) {
            cs.setNonStrokingColor(new Color(40, 85, 150));
            cs.addRect(0, 700, page.getMediaBox().getWidth(), 100);
            cs.fill();

            cs.beginText();
            cs.setNonStrokingColor(Color.WHITE);
            cs.setFont(PDType1Font.HELVETICA_BOLD, 26);
            cs.newLineAtOffset(MARGIN, 740);
            cs.showText("GWS CRM - User Activity Report");
            cs.endText();

            cs.beginText();
            cs.setFont(PDType1Font.HELVETICA, 14);
            cs.setNonStrokingColor(Color.DARK_GRAY);
            cs.newLineAtOffset(MARGIN, 650);
            cs.showText("Generated: " + java.time.LocalDate.now());
            cs.endText();
        }
    }

    private void addActivityTable(PDDocument document, List<UserActivityDTO> activities) throws Exception {
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);

        PDPageContentStream cs = new PDPageContentStream(document, page);
        float y = 750;
        float rowHeight = 20;
        float[] colWidths = {120, 100, 320};
        float tableWidth = colWidths[0] + colWidths[1] + colWidths[2];

        cs.setFont(PDType1Font.HELVETICA_BOLD, 16);
        cs.beginText();
        cs.newLineAtOffset(MARGIN, y);
        cs.showText("Activity Table");
        cs.endText();
        y -= 30;

        drawTableHeader(cs, y, colWidths);
        y -= 25;

        int index = 0;
        for (UserActivityDTO activity : activities) {
            if (y < 60) {
                cs.close(); // ✅ أغلق الـ stream القديم
                page = new PDPage(PDRectangle.A4);
                document.addPage(page);
                cs = new PDPageContentStream(document, page); // ✅ أنشئ stream جديد للصفحة الجديدة
                y = 750;

                cs.setFont(PDType1Font.HELVETICA_BOLD, 16);
                cs.beginText();
                cs.newLineAtOffset(MARGIN, y);
                cs.showText("Activity Table (Continued)");
                cs.endText();
                y -= 30;

                drawTableHeader(cs, y, colWidths);
                y -= 25;
            }

            drawTableRow(cs, activity, y, index++, colWidths);
            y -= rowHeight;
        }

        cs.close(); // ✅ أغلق الـ stream الأخير بعد الانتهاء
    }

    private void drawTableHeader(PDPageContentStream cs, float y, float[] widths) throws Exception {
        float x = MARGIN;

        cs.setNonStrokingColor(new Color(200, 200, 200));
        cs.addRect(x, y, widths[0] + widths[1] + widths[2], 25);
        cs.fill();

        cs.setNonStrokingColor(Color.BLACK);
        cs.setFont(PDType1Font.HELVETICA_BOLD, 12);

        cs.beginText();
        cs.newLineAtOffset(x + 5, y + 7);
        cs.showText("Timestamp");
        cs.endText();

        x += widths[0];

        cs.beginText();
        cs.newLineAtOffset(x + 5, y + 7);
        cs.showText("Action");
        cs.endText();

        x += widths[1];

        cs.beginText();
        cs.newLineAtOffset(x + 5, y + 7);
        cs.showText("Description");
        cs.endText();
    }

    private void drawTableRow(PDPageContentStream cs, UserActivityDTO dto, float y, int index, float[] widths) throws Exception {
        float x = MARGIN;

        if (index % 2 == 0) {
            cs.setNonStrokingColor(new Color(245, 245, 245));
            cs.addRect(x, y, widths[0] + widths[1] + widths[2], 20);
            cs.fill();
        }

        cs.setFont(PDType1Font.HELVETICA, 11);
        cs.setNonStrokingColor(Color.BLACK);

        cs.beginText();
        cs.newLineAtOffset(x + 5, y + 5);
        cs.showText(dto.getTimestampFormatted());
        cs.endText();

        x += widths[0];

        cs.beginText();
        cs.newLineAtOffset(x + 5, y + 5);
        cs.showText(dto.getAction());
        cs.endText();

        x += widths[1];

        cs.beginText();
        cs.newLineAtOffset(x + 5, y + 5);
        cs.showText(dto.getDescription());
        cs.endText();
    }

    private void addPieChartPage(PDDocument document, List<UserActivityDTO> activities) throws Exception {
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);

        // Count actions
        Map<String, Integer> actionCounts = new HashMap<>();
        for (UserActivityDTO dto : activities) {
            actionCounts.put(dto.getAction(), actionCounts.getOrDefault(dto.getAction(), 0) + 1);
        }

        // Dataset
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        for (Map.Entry<String, Integer> entry : actionCounts.entrySet()) {
            dataset.setValue(entry.getKey(), entry.getValue());
        }

        // Create chart
        JFreeChart chart = ChartFactory.createPieChart("User Action Distribution", dataset, true, true, false);
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setSectionPaint("LOGIN", new Color(40, 85, 150));
        plot.setBackgroundPaint(Color.WHITE);

        BufferedImage chartImage = chart.createBufferedImage(500, 400);

        // Save as image
        PDImageXObject pdImage = PDImageXObject.createFromByteArray(document, toByteArray(chartImage), "chart");
        try (PDPageContentStream cs = new PDPageContentStream(document, page)) {
            cs.drawImage(pdImage, 50, 250, 500, 400);
        }
    }

    private byte[] toByteArray(BufferedImage image) throws Exception {
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        ImageIO.write(image, "PNG", baos);
        return baos.toByteArray();
    }

}
