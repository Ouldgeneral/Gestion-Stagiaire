package outils;

import java.io.FileWriter;
import java.io.IOException;
import javax.swing.table.TableModel;

/**
 * @author Ould_Hamdi
 */
public class Excel {
    public static void tableExcel(TableModel model,String nomFichier){
            try (FileWriter fichier = new FileWriter(nomFichier)) {
                for(int i=0;i<model.getColumnCount();i++){
                    fichier.write("\""+model.getColumnName(i)+"\"");
                    if(i<model.getColumnCount()-1)fichier.write(";");
                }
                fichier.write("\n");
                for(int i=0;i<model.getRowCount();i++){
                    for(int j=0;j<model.getColumnCount();j++){
                        Object valeur=model.getValueAt(i, j);
                        String texte=(valeur==null)?"":String.valueOf(valeur);
                        texte=texte.replace("\"", "\"\"");
                        fichier.write("\""+texte+"\"");
                        if(j<model.getColumnCount()-1)fichier.write(";");
                    }
                    fichier.write("\n");
                }
            }catch (IOException ex) {}
    }
}
