package outils;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * @author Ould_Hamdi
 */
public class ImagePanel extends JPanel {
    private Image image;
    public ImagePanel(String photo){
        image=new ImageIcon(photo).getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(image!=null){
            g.drawImage(image, 0, 0,getWidth(),getHeight(), this);
        }
    }
    
}
