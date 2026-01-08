package model;

import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JPanel;
import outils.GestionStageBD;
import outils.ImagePanel;
import outils.Imprimerie;



/**
 * @author Ould_Hamdi
 */
public class Model {
    String emplacementBaseApplication;
    String emplacementBaseApplicationImage;
    GestionStageBD baseDonnee;
    public Model(){
        emplacementBaseApplication=System.getenv("APPDATA")+"\\GestonnaireDeStage\\baseDonnee\\gestionSTG.db";
        emplacementBaseApplicationImage=System.getenv("APPDATA")+"\\GestonnaireDeStage\\images\\";
        baseDonnee=new GestionStageBD(emplacementBaseApplication);
    }
    public void ajouterStagiaire(Stagiaire stagiaire){
        baseDonnee.ajouterStagiaires(stagiaire);
    }
    public void supprimerStagiaire(String matricule){
        baseDonnee.supprimerStagiaire(matricule);
    }
    public void mettreAJourStagiaire(Stagiaire stagiaire,String ancienMatricule){
        baseDonnee.mettreStagiaireAJour(stagiaire, ancienMatricule);
    }
    public DefaultListModel<Stagiaire> chargerListe(DefaultComboBoxModel listeSemestre,DefaultComboBoxModel listeSpecialite){
        DefaultListModel<Stagiaire> liste=baseDonnee.chargerListe();
        listeSemestre.addElement("Tout");
        listeSpecialite.addElement("Tout");
        for(int i=0;i<liste.size();i++){
            String specialite=liste.getElementAt(i).getSpecialite();
            int semestre=liste.getElementAt(i).getSemestre();
            if(listeSemestre.getIndexOf(semestre+"")==-1){
                listeSemestre.addElement(semestre+"");
            }
            if(listeSpecialite.getIndexOf(specialite)==-1){
                listeSpecialite.addElement(specialite);
            }
        }
        return liste;
    }
    public void enregistrerImage(File image,String nom){
        Path source=Path.of(image.getAbsolutePath());
        Path imageProfile=Path.of(emplacementBaseApplicationImage+nom+".png");
        Path emplacement=imageProfile.getParent();
        try {
            if(emplacement!=null){
                Files.createDirectories(emplacement);
            }
            Files.copy(source, imageProfile, StandardCopyOption.REPLACE_EXISTING,StandardCopyOption.COPY_ATTRIBUTES);
        } catch (IOException ex) {
        }
    }
    public ImagePanel retrouverImage(String nom,File photo){
        File image=new File(emplacementBaseApplicationImage+nom+".png");
        if(image.exists()){
            photo=image;
            return new ImagePanel(emplacementBaseApplicationImage+nom+".png");
        }
        return null;
    }
    public void supprimerImage(String nom){
        File image=new File(emplacementBaseApplicationImage+nom+".png");
        if(image.exists())image.delete();
    }
    public void imprimerDocument(JPanel document,String titreDocument){
        PrinterJob imprimante=PrinterJob.getPrinterJob();
        imprimante.setJobName(titreDocument);
        imprimante.setPrintable(new Imprimerie(document));
        if(imprimante.printDialog()){
            try{
                imprimante.print();
            }catch(PrinterException e){
                
            }
        }
    }
}
