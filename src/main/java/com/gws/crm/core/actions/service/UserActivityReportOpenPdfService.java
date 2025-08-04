package com.gws.crm.core.actions.service;

import com.gws.crm.core.actions.dtos.UserActivityDTO;
import com.lowagie.text.Font;
import com.lowagie.text.Rectangle;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.*;

@Service("openpdf")
public class UserActivityReportOpenPdfService implements UserActivityReportGenerator {

    private static final Font TITLE_FONT = new Font(Font.HELVETICA, 26, Font.BOLD, new Color(30, 30, 60));
    private static final Font HEADER_FONT = new Font(Font.HELVETICA, 16, Font.BOLD, Color.WHITE);
    private static final Font NORMAL_FONT = new Font(Font.HELVETICA, 12, Font.NORMAL, new Color(50, 50, 50));
    private static final Color HEADER_BG = new Color(60, 120, 180);

    @Override
    public void generatePdfReport(List<UserActivityDTO> activities, OutputStream out) throws Exception {
        Document doc = new Document(PageSize.A4, 40, 40, 50, 40);
        PdfWriter writer = PdfWriter.getInstance(doc, out);
        writer.setViewerPreferences(PdfWriter.PageLayoutSinglePage);
        doc.open();
        addCover(doc);
        addActivityDetailsTable(doc, activities);
        addSummaryStatistics(doc, activities);
        addChartsSection(doc, activities);
        addInsightsSection(doc, activities);
        addApprovalSection(doc);
        doc.close();
    }

    private void addCover(Document doc) throws DocumentException {
        PdfPTable cover = new PdfPTable(1);
        cover.setWidthPercentage(100);
        PdfPCell title = new PdfPCell(new Phrase("User Activity Report", TITLE_FONT));
        title.setHorizontalAlignment(Element.ALIGN_CENTER);
        title.setVerticalAlignment(Element.ALIGN_MIDDLE);
        title.setPadding(30);
        title.setBorder(Rectangle.NO_BORDER);
        cover.addCell(title);
        PdfPCell genDate = new PdfPCell(new Phrase("Generated on: " + new Date(), NORMAL_FONT));
        genDate.setHorizontalAlignment(Element.ALIGN_CENTER);
        genDate.setBorder(Rectangle.NO_BORDER);
        genDate.setPaddingBottom(20);
        cover.addCell(genDate);
        doc.add(cover);
        doc.newPage();
    }

    private void addActivityDetailsTable(Document doc, List<UserActivityDTO> acts) throws DocumentException {
        Paragraph header = new Paragraph("Activity Details", HEADER_FONT);
        header.setSpacingAfter(10);
        doc.add(header);

        PdfPTable table = new PdfPTable(new float[]{3, 6, 4});
        table.setWidthPercentage(100);
        addCell(table, "User", HEADER_FONT, HEADER_BG);
        addCell(table, "Action", HEADER_FONT, HEADER_BG);
        addCell(table, "Timestamp", HEADER_FONT, HEADER_BG);

        for (UserActivityDTO dto : acts) {
            addCell(table, "safwat@gmail.com", NORMAL_FONT, null);
            addCell(table, dto.getAction(), NORMAL_FONT, null);
            addCell(table, dto.getTimestampFormatted(), NORMAL_FONT, null);
        }

        table.setSpacingAfter(15);
        doc.add(table);
    }

    private void addSummaryStatistics(Document doc, List<UserActivityDTO> acts) throws DocumentException {
        Set<String> users = new HashSet<>();
        Set<String> actions = new HashSet<>();
        Map<String, Integer> actionCounts = new HashMap<>();
        Map<String, Integer> daily = new TreeMap<>();
        for (UserActivityDTO dto : acts) {
            users.add("safwat");
            actions.add(dto.getAction());
            actionCounts.merge(dto.getAction(), 1, Integer::sum);
            String d = dto.getTimestampFormatted().substring(0, 10);
            daily.merge(d, 1, Integer::sum);
        }

        PdfPTable t = new PdfPTable(2);
        t.setWidthPercentage(70);
        t.setSpacingBefore(20);
        t.setSpacingAfter(15);
        addCell(t, "Total Users", HEADER_FONT, HEADER_BG);
        addCell(t, String.valueOf(users.size()), NORMAL_FONT, null);
        addCell(t, "Unique Actions", HEADER_FONT, HEADER_BG);
        addCell(t, String.valueOf(actions.size()), NORMAL_FONT, null);
        addCell(t, "Total Activities", HEADER_FONT, HEADER_BG);
        addCell(t, String.valueOf(acts.size()), NORMAL_FONT, null);
        String most = actionCounts.entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse("N/A");
        String least = actionCounts.entrySet().stream().min(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse("N/A");
        addCell(t, "Most Common Action", HEADER_FONT, HEADER_BG);
        addCell(t, most, NORMAL_FONT, null);
        addCell(t, "Least Common Action", HEADER_FONT, HEADER_BG);
        addCell(t, least, NORMAL_FONT, null);
        doc.add(new Paragraph("Summary Statistics", HEADER_FONT));
        doc.add(t);
    }

    private void addChartsSection(Document doc, List<UserActivityDTO> acts) throws Exception {
        doc.add(new Paragraph("Charts & Visualizations", HEADER_FONT));
        embedChart(doc, generatePieChart(acts));
        embedChart(doc, generateBarChart(acts));
        embedChart(doc, generateLineChart(acts));
    }

    private JFreeChart generatePieChart(List<UserActivityDTO> acts) {
        Map<String, Integer> counts = new HashMap<>();
        for (UserActivityDTO dto : acts) counts.merge(dto.getAction(), 1, Integer::sum);
        DefaultPieDataset ds = new DefaultPieDataset();
        counts.forEach(ds::setValue);
        JFreeChart chart = ChartFactory.createPieChart("Action Distribution", ds, true, true, false);
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);
        return chart;
    }

    private JFreeChart generateBarChart(List<UserActivityDTO> acts) {
        DefaultCategoryDataset ds = new DefaultCategoryDataset();
        Map<String, Integer> ac = new HashMap<>();
        for (UserActivityDTO dto : acts) ac.merge(dto.getAction(), 1, Integer::sum);
        ac.forEach((k, v) -> ds.addValue(v, "Count", k));
        return ChartFactory.createBarChart("Action Counts", "Action", "Count", ds);
    }

    private JFreeChart generateLineChart(List<UserActivityDTO> acts) {
        TimeSeries series = new TimeSeries("Daily Activity");
        Map<String, Integer> counts = new TreeMap<>();
        for (UserActivityDTO dto : acts) {
            String d = dto.getTimestampFormatted().substring(0, 10);
            counts.merge(d, 1, Integer::sum);
        }
        for (Map.Entry<String, Integer> e : counts.entrySet()) {
            String[] p = e.getKey().split("-");
            series.add(new Day(Integer.parseInt(p[2]), Integer.parseInt(p[1]), Integer.parseInt(p[0])), e.getValue());
        }
        TimeSeriesCollection ds = new TimeSeriesCollection(series);
        return ChartFactory.createTimeSeriesChart("Activities Over Time", "Date", "Count", ds);
    }

    private void embedChart(com.lowagie.text.Document doc, JFreeChart chart) throws Exception {
        BufferedImage img = chart.createBufferedImage(450, 300);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(img, "png", baos);
        baos.flush();
        byte[] imageBytes = baos.toByteArray();
        com.lowagie.text.Image image = com.lowagie.text.Image.getInstance(imageBytes);
        image.setAlignment(com.lowagie.text.Image.MIDDLE);
        image.scaleToFit(450, 300);
        doc.add(image);
        doc.add(com.lowagie.text.Chunk.NEWLINE);
    }


    private void addInsightsSection(Document doc, List<UserActivityDTO> acts) throws DocumentException {
        Map<Integer, Integer> hourly = new TreeMap<>();
        for (UserActivityDTO dto : acts) {
            int h = Integer.parseInt(dto.getTimestampFormatted().substring(11, 13));
            hourly.merge(h, 1, Integer::sum);
        }
        int peak = hourly.entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse(-1);
        doc.add(new Paragraph("Insights & Observations", HEADER_FONT));
        doc.add(new Paragraph("Most Active Hour: " + (peak >= 0 ? String.format("%02d:00", peak) : "N/A"), NORMAL_FONT));
    }

    private void addApprovalSection(Document doc) throws DocumentException {
        doc.newPage();
        doc.add(new Paragraph("Approval", HEADER_FONT));
        doc.add(new Paragraph("Name: ____________________________", NORMAL_FONT));
        doc.add(new Paragraph("Position: _________________________", NORMAL_FONT));
        doc.add(new Paragraph("Date: ____________________________", NORMAL_FONT));
    }

    private void addCell(PdfPTable table, String text, Font font, Color bg) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        if (bg != null) cell.setBackgroundColor(bg);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(8);
        table.addCell(cell);
    }
}
