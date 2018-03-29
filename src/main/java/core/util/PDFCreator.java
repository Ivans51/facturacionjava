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
import java.util.ArrayList;

public class PDFCreator {

    public Font.FontFamily family = Font.FontFamily.COURIER;
    public BaseColor background = BaseColor.DARK_GRAY;
    private String image;
    private String nameFile, sub, title, otherParragraph;
    private Font fontTitle, fontSub;
    private float[] columnWidthOne;
    private float[] columnWidthTwo;
    private float[] columnWidthThree;

    public PDFCreator(String nameFile, String title, String sub, String imagePath) {
        this.nameFile = nameFile;
        this.title = title;
        this.sub = sub;
        this.image = imagePath;
    }

    public void crearPDF() throws IOException, DocumentException {
        Document documento = new Document();
        PdfWriter.getInstance(documento, new FileOutputStream(nameFile));
        documento.open();
        setParagraph(documento, 500, 300);
        documento.close();
        System.out.println("Imprisión correcta");
        openPDF();
    }

    public void crearPDF(int numColumns, PDFTabla pdfTabla) throws IOException, DocumentException {
        Document documento = new Document();
        PdfWriter.getInstance(documento, new FileOutputStream(nameFile));
        documento.open();
        setParagraph(documento, 150, 100);
        PdfPTable tablaOne = new PdfPTable(numColumns);
        if (columnWidthOne != null && columnWidthOne.length > 0) {
            tablaOne.setTotalWidth(columnWidthOne);
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
        setParagraph(documento, 150, 100);

        Paragraph p1 = new Paragraph("Datos del Cliente \n ", fontTitle);
        p1.setPaddingTop(10);
        documento.add(p1);

        PdfPTable tablaTwo = new PdfPTable(numColumsTwo);
        if (columnWidthTwo != null && columnWidthTwo.length > 0) {
            tablaTwo.setTotalWidth(columnWidthTwo);
            tablaTwo.setWidthPercentage(100);
            tablaTwo.setLockedWidth(true);
        }
        pdfTablaTwo.addCellTable(tablaTwo);
        tablaTwo.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        tablaTwo.setTableEvent(new BorderEvent());
        documento.add(tablaTwo);

        PdfPTable tablaOne = new PdfPTable(numColumns);
        if (columnWidthOne != null && columnWidthOne.length > 0) {
            tablaOne.setTotalWidth(columnWidthOne);
            tablaOne.setWidthPercentage(100);
            tablaOne.setLockedWidth(true);
        }
        pdfTablaOne.addCellTable(tablaOne);
        documento.add(tablaOne);

        Paragraph p2 = new Paragraph("", fontTitle);
        p2.setPaddingTop(10);
        documento.add(p2);

        PdfPTable pdfTableThree = new PdfPTable(numColumsThree);
        if (columnWidthThree != null && columnWidthThree.length > 0) {
            pdfTableThree.setTotalWidth(columnWidthThree);
            pdfTableThree.setWidthPercentage(100);
            pdfTableThree.setLockedWidth(true);
        }
        pdfTablaThree.addCellTable(pdfTableThree);
        documento.add(pdfTableThree);

        Paragraph p3 = new Paragraph(otherParragraph, fontTitle);
        p3.setPaddingTop(10);
        documento.add(p3);

        documento.close();
        System.out.println("Imprisión correcta");
        openPDF();
    }

    public PDFCreator(String nameFile) {
        this.nameFile = nameFile;
    }

    public void createPDF(Elements element) throws IOException, DocumentException {
        Document documento = new Document();
        PdfWriter.getInstance(documento, new FileOutputStream(nameFile));
        documento.open();
        // addPart(documento, element);
        element.add(documento);
        documento.close();
        System.out.println("Imprisión correcta");
        openPDF();
    }

    public interface Elements{
        void add(Document documento) throws DocumentException, IOException;
    }

    private void addPart(Document documento, ArrayList<Element> element) throws DocumentException {
        for (Element el : element)
            documento.add(el);
    }

    /**
     * @param columnWidth - array of a size 520
     * @param pdfTablaTwo - Interface to add information to cell
     */
    public PdfPTable setTablePDF(int numColumsTwo, float[] columnWidth, PDFTabla pdfTablaTwo, boolean noBorder) throws DocumentException, IOException {
        PdfPTable tablaTwo = new PdfPTable(numColumsTwo);
        if (columnWidth != null && columnWidth.length > 0) {
            tablaTwo.setTotalWidth(columnWidth);
            tablaTwo.setWidthPercentage(100);
            tablaTwo.setLockedWidth(true);
        }
        if (noBorder) {
            tablaTwo.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            tablaTwo.setTableEvent(new BorderEvent());
        }
        pdfTablaTwo.addCellTable(tablaTwo);
        return tablaTwo;
    }

    /**
     * Import! - Add Cell at the setTablePDF and after array elements
     * @param align  - For example: Element.ALIGN_LEFT
     * @param border - For example: Rectangle.NO_BORDER
     */
    public PdfPCell setCellPDF(int align, int border, Element... elements) throws IOException, DocumentException {
        PdfPCell cellLeft = new PdfPCell();
        cellLeft.setVerticalAlignment(align);
        cellLeft.setBorder(border);
        for (Element el : elements)
            cellLeft.addElement(el);
        return cellLeft;
    }

    /**
     * Import! - Add Cell at the Documento
     * @param witdh  - Example: 150
     * @param height - Example: 100
     * @param align  - For example: Element.ALIGN_LEFT
     */
    public Image setImagePDF(String path, float witdh, float height, int align) throws IOException, BadElementException {
        Image img = Image.getInstance(path);
        img.setAlignment(align);
        img.scaleToFit(witdh, height);
        return img;
    }

    /**
     * Import! - Add Cell at the Documento
     * @param align  - For example: Element.ALIGN_LEFT
     */
    public Paragraph setParagraph(String value, int align, int paddingTop, int size, int style) {
        Paragraph paragraph = new Paragraph(value, new Font(family, size, style, background));
        paragraph.setAlignment(align);
        paragraph.setPaddingTop(paddingTop);
        return paragraph;
    }

    private void setParagraph(Document documento, int witdh, int height) throws DocumentException, IOException {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setWidths(new int[]{1, 2});

        PdfPCell cellLeft = getPdfPCellLeft(documento, witdh, height);
        PdfPCell cellRight = getPdfPCellRight();
        table.addCell(cellLeft);
        table.addCell(cellRight);
        documento.add(table);
    }

    private PdfPCell getPdfPCellLeft(Document documento, float witdh, float height) throws IOException, DocumentException {
        PdfPCell cellLeft = new PdfPCell();
        if (image != null) {
            Image img = Image.getInstance(image);
            img.setAlignment(Element.ALIGN_LEFT);
            img.scaleToFit(witdh, height);
            cellLeft.addElement(img);

            Paragraph sub = new Paragraph(this.sub, fontSub);
            sub.setAlignment(Element.ALIGN_LEFT);
            sub.setPaddingTop(10);
            cellLeft.addElement(sub);

            cellLeft.setVerticalAlignment(Element.ALIGN_TOP);
            cellLeft.setBorder(Rectangle.NO_BORDER);
            documento.add(cellLeft);
        }
        return cellLeft;
    }

    private PdfPCell getPdfPCellRight() {
        PdfPCell cellRight = new PdfPCell();
        Paragraph p = new Paragraph(this.title, fontTitle);
        p.setPaddingTop(10);
        p.setAlignment(Element.ALIGN_RIGHT);
        cellRight.addElement(p);
        cellRight.setVerticalAlignment(Element.ALIGN_BOTTOM);
        cellRight.setBorder(Rectangle.NO_BORDER);
        return cellRight;
    }

    public PdfPCell setStyleCellTable(String value) {
        Font titleFont = FontFactory.getFont(FontFactory.COURIER, 11, BaseColor.DARK_GRAY);
        Paragraph subTitle = new Paragraph(value, titleFont);
        PdfPCell cell = new PdfPCell(subTitle);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        return cell;
    }

    private void openPDF() {
        if (Desktop.isDesktopSupported()) try {
            File myFile = new File(this.nameFile);
            Desktop.getDesktop().open(myFile);
        } catch (IOException ex) {
            System.out.println("No se pudo crear");
        }
    }

    /**
     * @param family: Font.FontFamily.COURIER
     * @param size: 14
     * @param style: Font.BOLD
     * @param background: BaseColor.DARK_GRAY
     */
    public void setFontTitle(Font.FontFamily family, int size, int style, BaseColor background) {
        fontTitle = new Font(family, size, style, background);
    }

    /**
     * @param family: Font.FontFamily.COURIER
     * @param size: 14
     * @param style: Font.BOLD
     * @param background: BaseColor.DARK_GRAY
     */
    public void setFontSub(Font.FontFamily family, int size, int style, BaseColor background) {
        fontSub = new Font(family, size, style, background);
    }

    public void setColumnWidthOne(float[] columnWidthOne) {
        this.columnWidthOne = columnWidthOne;
    }

    public void setColumnWidthTwo(float[] columnWidthTwo) {
        this.columnWidthTwo = columnWidthTwo;
    }

    public void setColumnWidthThree(float[] columnWidthThree) {
        this.columnWidthThree = columnWidthThree;
    }

    public void setOtherParragraph(String otherParragraph) {
        this.otherParragraph = otherParragraph;
    }

    public interface PDFTabla {
        void addCellTable(PdfPTable tabla) throws IOException, DocumentException;
    }
}