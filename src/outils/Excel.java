package outils;

import View.View;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JLabel;
import model.Stagiaire;

/**
 * @author Ould_Hamdi
 */
public class Excel {
    public static void tableExcel(DefaultListModel<Stagiaire> liste,String nomFichier,String[] colonnes,View view){
            try (FileWriter fichier = new FileWriter(nomFichier)) {
                for(int i=0;i<colonnes.length;i++){
                    fichier.write("\""+colonnes[i]+"\"");
                    if(i<colonnes.length-1)fichier.write(";");
                }
                JDialog message=new JDialog(view);
                message.setTitle("Excel");
                JLabel label1=new JLabel(I18n.texte("excel.ecriture"));
                message.setSize(400, 100);
                message.add(label1);
                message.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
                message.setLocationRelativeTo(null);
                message.setVisible(true);
                fichier.write("\n");
                for(int i=0;i<liste.size();i++){
                    fichier.write(liste.getElementAt(i).toExcel());
                    fichier.write("\n");
                }
                message.dispose();
            }catch (IOException ex) {}
    }
}
