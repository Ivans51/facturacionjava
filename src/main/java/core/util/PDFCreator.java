package core.util;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PDFCreator {

    public Font.FontFamily family = Font.FontFamily.COURIER;
    public int size = 16;
    public int style = Font.ITALIC;
    public BaseColor background = BaseColor.DARK_GRAY;
    private String image;
    private String nameFile, sub, title;
    private Font fontTitle, fontSub;
    private boolean openPDF = true;
    private float[] columnWidth = new float[]{72, 216};

    public PDFCreator(String nameFile, String title, String sub, String imagePath) {
        this.nameFile = nameFile;
        this.title = title;
        this.sub = sub;
        this.image = imagePath;
    }

    public void crearPDF(int numColumns, PDFTabla pdfTabla) throws IOException, DocumentException {
        Document documento = new Document();
        PdfWriter.getInstance(documento, new FileOutputStream(nameFile));
        documento.open();
        setParagraph(documento);
        PdfPTable tablaOne = new PdfPTable(numColumns);
        if (columnWidth != null && columnWidth.length > 0) {
            tablaOne.setTotalWidth(columnWidth);
            tablaOne.setLockedWidth(true);
        }
        pdfTabla.addCellTable(tablaOne);
        documento.add(tablaOne);
        documento.close();
        System.out.println("Imprisión correcta");
        openPDF();
    }

    public void crearPDF(int numColumns, PDFTabla pdfTablaOne, int numColumsTwo, PDFTabla pdfTablaTwo, int numColumsThree, PDFTabla pdfTablaThree) throws IOException, DocumentException {
        Document documento = new Document();
        PdfWriter.getInstance(documento, new FileOutputStream(nameFile));
        documento.open();
        setParagraph(documento);

        PdfPTable tablaTwo = new PdfPTable(numColumsTwo);
        tablaTwo.setPaddingTop(10);
        pdfTablaTwo.addCellTable(tablaTwo);
        documento.add(tablaTwo);

        PdfPTable tablaOne = new PdfPTable(numColumns);
        if (columnWidth != null && columnWidth.length > 0) {
            tablaOne.setTotalWidth(columnWidth);
            tablaOne.setWidthPercentage(100);
            tablaOne.setLockedWidth(true);
        }
        pdfTablaOne.addCellTable(tablaOne);
        documento.add(tablaOne);

        PdfPTable pdfTableThree = new PdfPTable(numColumsThree);
        pdfTablaThree.addCellTable(pdfTableThree);
        documento.add(pdfTableThree);

        documento.close();
        System.out.println("Imprisión correcta");
        openPDF();
    }

    private void setParagraph(Document documento) throws DocumentException, IOException {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setWidths(new int[]{1, 2});

        PdfPCell cellLeft = new PdfPCell();
        if (image != null) {
            Image img = Image.getInstance(image);
            img.scaleToFit(150, 100);
            img.setAlignment(Element.ALIGN_LEFT);
            cellLeft.addElement(img);

            Paragraph sub = new Paragraph(this.sub, fontSub);
            sub.setAlignment(Element.ALIGN_LEFT);
            sub.setPaddingTop(10);
            cellLeft.addElement(sub);

            cellLeft.setVerticalAlignment(Element.ALIGN_TOP);
            cellLeft.setBorder(Rectangle.NO_BORDER);
            documento.add(cellLeft);
        }
        PdfPCell cellRight = new PdfPCell();
        Paragraph p = new Paragraph(this.title, fontTitle);
        p.setPaddingTop(10);
        p.setAlignment(Element.ALIGN_RIGHT);
        cellRight.addElement(p);
        cellRight.setVerticalAlignment(Element.ALIGN_BOTTOM);
        cellRight.setBorder(Rectangle.NO_BORDER);
        table.addCell(cellLeft);
        table.addCell(cellRight);
        documento.add(table);
    }

    public PdfPCell setStyleCell(String value){
        Font titleFont = FontFactory.getFont(FontFactory.COURIER, 11, BaseColor.DARK_GRAY);
        Paragraph subTitle = new Paragraph(value, titleFont);
        PdfPCell cell = new PdfPCell(subTitle);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        return cell;
    }

    private void openPDF() {
        if (Desktop.isDesktopSupported() && openPDF) try {
            File myFile = new File(this.nameFile);
            Desktop.getDesktop().open(myFile);
        } catch (IOException ex) {
            System.out.println("No se pudo crear");
        }
    }

    public void setColumnWidth(float[] columnWidth) {
        this.columnWidth = columnWidth;
    }

    public void setFontTitle(Font.FontFamily family, int size, int style, BaseColor background) {
        fontTitle = new Font(family, size, style, background);
    }

    public void setFontSub(Font.FontFamily family, int size, int style, BaseColor background) {
        fontSub = new Font(family, size, style, background);
    }

    public void setOpenPDF(boolean openPDF) {
        this.openPDF = openPDF;
    }

    public interface PDFTabla {
        void addCellTable(PdfPTable tabla);
    }
}