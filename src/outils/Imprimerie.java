package outils;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import javax.swing.JPanel;

/**
 * @author Ould_Hamdi
 */
public class Imprimerie implements Printable{
    JPanel document;

    public Imprimerie(JPanel document) {
        this.document = document;
    }
    
    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        if(pageIndex>0)return NO_SUCH_PAGE;
        Graphics2D g2d=(Graphics2D)graphics;
        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
        double rempliX=pageFormat.getImageableWidth()/document.getWidth();
        double rempliY=pageFormat.getImageableHeight()/document.getHeight();
        double remplissage=Math.min(rempliX,rempliY);
        g2d.scale(remplissage, remplissage);
        document.printAll(g2d);
        return PAGE_EXISTS;
    }

}
