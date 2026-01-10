package model;

import View.View;
import java.awt.BorderLayout;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import outils.Excel;
import outils.GestionStageBD;
import outils.I18n;
import outils.ImagePanel;
import outils.Imprimerie;



/**
 * @author Ould_Hamdi
 */
public class Model {
    String emplacementBaseApplication;
    String emplacementBaseApplicationImage;
    String emplacementBaseApplicationConfiguration;
    GestionStageBD baseDonnee;
    public Model(){
        emplacementBaseApplication=System.getenv("APPDATA")+"\\GestonnaireDeStage\\baseDonnee\\gestionSTG.db";
        emplacementBaseApplicationImage=System.getenv("APPDATA")+"\\GestonnaireDeStage\\images\\";
        emplacementBaseApplicationConfiguration=System.getenv("APPDATA")+"\\GestonnaireDeStage\\conf\\lang.conf";
        baseDonnee=new GestionStageBD(emplacementBaseApplication);
    }
    public void chargerLangue(View view){
        new File(emplacementBaseApplicationConfiguration).getParentFile().mkdirs();
        File lang=new File(emplacementBaseApplicationConfiguration);
        if(lang.exists()){
            try {
                char codeLangue=(char)(new FileReader(lang).read());
                if(codeLangue=='a')changerLangue(new Locale("ar","DZ"), view);
                else changerLangue(Locale.FRANCE, view);
            } catch (FileNotFoundException ex) {
            } catch (IOException ex) {
            }
        }
    }
    public void changerLangue(Locale langue,View view){
        new File(emplacementBaseApplicationConfiguration).getParentFile().mkdirs();
        if(view.getLocale().getLanguage().equalsIgnoreCase(langue.getLanguage()))return;
        try {
            Files.write(Paths.get(emplacementBaseApplicationConfiguration),(langue.getLanguage().charAt(0)+"").getBytes());
        } catch (IOException ex) {
        }
        I18n.changerLangue(langue);
        view.setLocale(langue);
        view.appliquerLangue();
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
        listeSemestre.addElement(I18n.texte("combo.text"));
        listeSpecialite.addElement(I18n.texte("combo.text"));
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
    public void exporterExcel(DefaultListModel liste,String nomFichier){
        String[] colonnes={
            I18n.texte("label.matricule"),I18n.texte("label.nom"),I18n.texte("label.prenom"),I18n.texte("tooltip.date"),I18n.texte("tooltip.lieu"),
            I18n.texte("label.genre"),I18n.texte("label.spec"),I18n.texte("label.semestre"),I18n.texte("label.groupe"),I18n.texte("label.modeStage"),
            I18n.texte("label.numero"),I18n.texte("label.email"),I18n.texte("label.adresse")
        };
        for(int i=0;i<colonnes.length;i++){
            colonnes[i]=colonnes[i].replace(":", "");
        }
        Object[][] lignes=new Object[liste.size()][colonnes.length];
        for(int i=0;i<liste.size();i++){
            Stagiaire stagiaire=(Stagiaire) liste.getElementAt(i);
            lignes[i][0]=stagiaire.getMatricule();
            lignes[i][1]=stagiaire.getNom();
            lignes[i][2]=stagiaire.getPrenom();
            lignes[i][3]=stagiaire.getDateNaissance().toString();
            lignes[i][4]=stagiaire.getLieuNaissance();
            lignes[i][5]=I18n.texte("radio."+stagiaire.getGenre());
            lignes[i][6]=stagiaire.getSpecialite();
            lignes[i][7]=stagiaire.getSemestre();
            lignes[i][8]=stagiaire.getGroupe();
            lignes[i][10]=stagiaire.getNumero();
            lignes[i][11]=stagiaire.getEmail();
            lignes[i][12]=stagiaire.getAdresse();
            String mode=I18n.texte("radio.cos");
            switch(stagiaire.getModeApprentissage()){
                case "residentiel"->mode=I18n.texte("radio.reside");
                case "apprentissage"->mode=I18n.texte("radio.apprenti");
            }
            lignes[i][9]=mode;
        }
        DefaultTableModel model=new DefaultTableModel(lignes, colonnes);
        Excel.tableExcel(model, nomFichier);
    }
    public void importerExcel(DefaultListModel liste,File fichier){
        try {
            String contenuFichier=Files.readString(Paths.get(fichier.getAbsolutePath()));
            if(contenuFichier.isEmpty())return;
            String[] colonnes=new String[13];
            Pattern p=Pattern.compile(".*\\R");
            Matcher m=p.matcher(contenuFichier);
            if(m.find()){
                String[] enTete=m.group().replace("\"", "").split(";");
                if(enTete.length>13){
                    return;
                }
                System.arraycopy(enTete, 0, colonnes, 0, enTete.length);
            }
            boolean erreur=false;
            DefaultTableModel model=new DefaultTableModel(colonnes,0);
            while(m.find()){
                String[] ligne=m.group().replace("\"", "").split(";");
                Object o=validerSyntaxe(ligne,liste);
                if(o instanceof Stagiaire stagiaire){
                    liste.addElement(o);
                    ajouterStagiaire(stagiaire);
                }else{
                    erreur=true;
                    model.addRow((Object[])o);
                }
                
            }
            if(erreur){
                
                JFrame erreurFrame=new JFrame(I18n.texte("error.table"));
                erreurFrame.setLayout(new BorderLayout());
                erreurFrame.setSize(700,700);
                JTable table=new JTable(model);
                JScrollPane pane=new JScrollPane(table);
                JLabel label=new JLabel(I18n.texte("erreur.desc"));
                label.setFont(label.getFont().deriveFont(18f));
                erreurFrame.add(label,BorderLayout.NORTH);
                erreurFrame.add(pane,BorderLayout.CENTER);
                erreurFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                erreurFrame.setVisible(true);
            }
        } catch (IOException ex) {
        }
    }
        private Object validerSyntaxe(String[] ligne,DefaultListModel liste){
        Date d=null;
        try {
            DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
            d = df.parse(ligne[3]);
            if((new Date().getYear())-d.getYear()<15 || (new Date().getYear())-d.getYear()>35){
                  ligne[3]=ligne[3]+"("+I18n.texte("message.age")+")";
            }
        } catch (ParseException ex) {
            ligne[3]=ligne[3]+"("+I18n.texte("error.date")+")";
        }
        Pattern p=Pattern.compile("[^a-zA-Z0-9]");
        Matcher m=p.matcher(ligne[0]);
        if(m.find()){
            ligne[0]=ligne[0]+"("+I18n.texte("message.mat")+")";
        }
        int numeroTel=0;
        try{
            numeroTel=Integer.parseInt(ligne[10]);
            if(numeroTel<0)throw new NumberFormatException();
        }catch(NumberFormatException ex){
           ligne[10]=ligne[10]+"("+I18n.texte("message.num")+")";
        }
        int semestreStagiaire=0;
        try{
            semestreStagiaire=Integer.parseInt(ligne[7]);
            if(semestreStagiaire<=0)throw  new NumberFormatException();
        }catch(NumberFormatException ex){
            ligne[7]=ligne[7]+"("+I18n.texte("message.sem")+")";
        }
        int groupe=0;
        try{
            groupe=Integer.parseInt(ligne[8]);
        }catch(NumberFormatException ex){
            ligne[8]=ligne[8]+"("+I18n.texte("message.groupe")+")";
        }
        String nom=ligne[1];
        String prenom=ligne[2];
        String lieuNaissance=ligne[4];
        String email=ligne[11];
        String adresse=ligne[12];
        String matricule=ligne[0];
        String specialiteStagiare=ligne[6];
        String genre=null;
        if(ligne[5].equalsIgnoreCase("homme") || ligne[5].equals("ذكر"))genre="homme";
        else if(ligne[5].equalsIgnoreCase("femme") || ligne[5].equals("أنثى"))genre="femme";
        else ligne[5]=ligne[5]+"("+I18n.texte("error.genre")+")";
        String modeStage=null;
        if(ligne[9].equalsIgnoreCase("Cours de Soir") || ligne[9].equals("دروس مسائية"))modeStage="cours de Soir";
        else if(ligne[9].equalsIgnoreCase("Residentiel") || ligne[9].equals("حضوري"))modeStage="residentiel";
        else if(ligne[9].equalsIgnoreCase("Apprentissage") || ligne[9].equals("تمهين"))modeStage="apprentissage";
        else ligne[9]=ligne[9]+"("+I18n.texte("error.modeStage")+")";
        if(groupe==0 || semestreStagiaire==0 || genre==null || modeStage==null || d==null)return ligne;
        if(matriculeValide(matricule, liste)!=-1){
            ligne[0]=ligne[0]+"("+I18n.texte("message.mat2")+")";
            return ligne;
        }
        return new Stagiaire(nom, prenom, lieuNaissance, d, email, numeroTel, adresse, matricule, specialiteStagiare, groupe, semestreStagiaire, genre, modeStage);
    }
    private int matriculeValide(String matricule,DefaultListModel<Stagiaire> liste){
        for(int i=0;i<liste.size();i++){
            if(liste.getElementAt(i).getMatricule().equalsIgnoreCase(matricule))return i;
        }
        return -1;
    }
}
