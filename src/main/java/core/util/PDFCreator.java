package core.util;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class PDFCreator {

    public Font.FontFamily family = Font.FontFamily.COURIER;
    public int size = 16;
    public int style = Font.ITALIC;
    public BaseColor background = BaseColor.DARK_GRAY;
    private String nameFile, sub, title;
    private Font fontTitle, fontSub;
    private boolean openPDF = true;

    public PDFCreator(String nameFile, String title, String sub) {
        this.nameFile = nameFile;
        this.title = title;
        this.sub = sub;
    }

    public void crearPDF(int numColumns, PDFTabla pdfTabla) throws FileNotFoundException, DocumentException {
        Document documento = new Document();
        PdfWriter.getInstance(documento, new FileOutputStream(nameFile));
        documento.open();
        Paragraph title = new Paragraph(this.title, fontTitle);
        title.setPaddingTop(10);
        documento.add(title);
        Paragraph sub = new Paragraph(this.sub, fontSub);
        sub.setPaddingTop(10);
        documento.add(sub);
        /*documento.add(new Paragraph(segundoParrafo, FontFactory.getFont(arial, tamaño, estilo, color)));*/
        PdfPTable tabla = new PdfPTable(numColumns);
        pdfTabla.addCellTable(tabla);
        /*for (int i = 0; i < size; i++) {
            tabla.addCell("celda " + i);
        }*/
        documento.add(tabla);
        documento.close();
        System.out.println("Hola");
        openPDF();
    }

    private void openPDF() {
        if (Desktop.isDesktopSupported() && openPDF) try {
            File myFile = new File(this.nameFile);
            Desktop.getDesktop().open(myFile);
        } catch (IOException ex) {
            System.out.println("No se pudo crear");
        }
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