package controller;

import View.View;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.FlatClientProperties;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ItemEvent;
import java.io.File;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.AbstractButton;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import model.Model;
import model.Stagiaire;
import org.jdesktop.swingx.JXDialog;
import outils.I18n;
import outils.ImagePanel;
import outils.ProgressionSuppression;
/**
 * @author Ould_Hamdi
 */
public class Controller{
    View view;
    Model model;
    File photo;
    DefaultListModel<Stagiaire> liste;
    DefaultComboBoxModel<String> listeSpecialite;
    DefaultComboBoxModel<String> listeSemestre;
    String semestre=I18n.texte("combo.text");
    String specialite=I18n.texte("combo.text");
    String mode=I18n.texte("combo.text");
    public Controller(View view,Model model){
        this.view=view;
        this.model=model;
        listeSemestre=new DefaultComboBoxModel<>();
        listeSpecialite=new DefaultComboBoxModel<>();
        liste=model.chargerListe(listeSemestre, listeSpecialite);
        view.getListeStagiaire().setModel(liste);
        view.getListeSemestre().setModel(listeSemestre);
        view.getListeSpecialite().setModel(listeSpecialite);
        initialiserBoutton();
        initialiserRecherche();
        initialiserFiltrage();
    }
    public void demarrer(){
        
        view.ajouterDocumentListener();
        model.chargerLangue(view);
        FlatLightLaf.setup();
        UIManager.put("Button.arc", 20);
        UIManager.put("Component.arc", 20);
        UIManager.put("TextComponent.arc", 990);
        UIManager.put("Panel.background", new Color(245,247,250));
        view.textIndication();
        view.getDocument().putClientProperty(FlatClientProperties.COMPONENT_ROUND_RECT, 20);
        SwingUtilities.updateComponentTreeUI(view);
        view.setVisible(true);
    }
    private void initialiserFiltrage(){
        view.getListeStagiaire().setAutoCreateRowSorter(true);
        view.getListeSemestre().addItemListener(e->{
            if(e.getStateChange()==ItemEvent.SELECTED){
                semestre=(String)e.getItem();
                filtrer();
            }
        });
        view.getListeSpecialite().addItemListener(e->{
            if(e.getStateChange()==ItemEvent.SELECTED){
                specialite=(String)e.getItem();
                filtrer();
            }
            
        });
        view.getListeModeStage().addItemListener(e->{
            if(e.getStateChange()==ItemEvent.SELECTED){
                mode=(String)e.getItem();
                filtrer();
            }
            
        });
    }
    private void filtrer(){
        DefaultListModel<Stagiaire> listeFiltrer=new DefaultListModel<>();
        if(!semestre.equals(I18n.texte("combo.text")) && specialite.equals(I18n.texte("combo.text"))){
            for(int i=0;i<liste.size();i++){
                if((liste.getElementAt(i).getSemestre()+"").equals(semestre)){
                    listeFiltrer.addElement(liste.getElementAt(i));
                }
            }
        }
        else if(semestre.equals(I18n.texte("combo.text")) && !specialite.equals(I18n.texte("combo.text"))){
            for(int i=0;i<liste.size();i++){
                if((liste.getElementAt(i).getSpecialite()).equalsIgnoreCase(specialite)){
                    listeFiltrer.addElement(liste.getElementAt(i));
                }
            }
        }
        else if(!semestre.equals(I18n.texte("combo.text")) && !specialite.equals(I18n.texte("combo.text"))){
            for(int i=0;i<liste.size();i++){
                if((liste.getElementAt(i).getSpecialite()).equalsIgnoreCase(specialite) &&
                   (liste.getElementAt(i).getSemestre()+"").equalsIgnoreCase(semestre)     
                        ){
                    listeFiltrer.addElement(liste.getElementAt(i));
                }
            }
        }else{
            listeFiltrer=liste;
        }
        DefaultListModel<Stagiaire> liste2Filtrer=new DefaultListModel<>();
        if(!mode.equals(I18n.texte("combo.text"))){
            for(int i=0;i<listeFiltrer.size();i++){
                if(listeFiltrer.getElementAt(i).getModeApprentissage().equalsIgnoreCase(mode)){
                    liste2Filtrer.addElement(listeFiltrer.getElementAt(i));
                }
            }
            listeFiltrer=liste2Filtrer;
        }
        view.getListeStagiaire().setModel(listeFiltrer);
        
    }
    private void initialiserRecherche(){
        view.getListeStagiaire().setAutoCreateRowSorter(true);
        view.getListeStagiaire().getSelectionModel().addListSelectionListener(e->{
            Stagiaire stagiaire=(Stagiaire)view.getListeStagiaire().getSelectedValue();
            if(stagiaire!=null){
                infoStagiaire(stagiaire);
                view.getBtnSupprimerStagiaire().setEnabled(view.getListeStagiaire().getSelectedValue()!=null);
            }
        });
        view.getRechercher().getDocument().addDocumentListener(new DocumentListener(){
            @Override
            public void insertUpdate(DocumentEvent e) {rechercher();}

            @Override
            public void removeUpdate(DocumentEvent e) {rechercher();}

            @Override
            public void changedUpdate(DocumentEvent e) {rechercher();}
            
        });
    }
    public void rechercher(){
        String texte=view.getRechercher().getText().toLowerCase();
        if(texte.isEmpty()){
            view.getListeStagiaire().setModel(liste);
        }
        DefaultListModel<Stagiaire> listeFiltrer=new DefaultListModel<>();
        for(int i=0;i<liste.size();i++){
            String texteFiltrer=liste.getElementAt(i).toString();
            if(texteFiltrer.toLowerCase().contains(texte)){
                listeFiltrer.addElement(liste.getElementAt(i));
            }
        }
        if(!listeFiltrer.isEmpty()){
            view.getListeStagiaire().setModel(listeFiltrer);
        }else{
            view.getListeStagiaire().setModel(liste);
        }
    }
    public void enregistrer(){
        Stagiaire stagiaire=validerSyntaxe();
        if(stagiaire!=null){
            if(matriculeValide(stagiaire.getMatricule())!=-1){
                message(I18n.texte("message.mat2"), I18n.texte("message.mat2.titre"));
                return ;
            }
            view.getListeStagiaire().setModel(liste);
            liste.addElement(stagiaire);
            model.ajouterStagiaire(stagiaire);
            if(photo!=null)model.enregistrerImage(photo,stagiaire.getMatricule());
            if(listeSpecialite.getIndexOf(stagiaire.getSpecialite())==-1)listeSpecialite.addElement(stagiaire.getSpecialite());
            if(listeSemestre.getIndexOf(stagiaire.getSemestre()+"")==-1)listeSemestre.addElement(stagiaire.getSemestre()+"");
        }
    }
    public void mettreAJour(){
        Stagiaire stagiaire=(Stagiaire)view.getListeStagiaire().getSelectedValue();
        if(stagiaire==null){
            message(I18n.texte("message.sel"), I18n.texte("message.sel.titre"));
            return;
        }
        String ancienMatricule=stagiaire.getMatricule();
        stagiaire=validerSyntaxe();
        if(stagiaire!=null){
            int indexAncien=matriculeValide(ancienMatricule);
            int indexNouveau=matriculeValide(stagiaire.getMatricule());
            if(indexNouveau==indexAncien || (indexNouveau==-1 && indexAncien!=-1)){
                liste.getElementAt(indexAncien).mettreAJour(stagiaire);
                if(listeSpecialite.getIndexOf(stagiaire.getSpecialite())==-1)listeSpecialite.addElement(stagiaire.getSpecialite());
                if(listeSemestre.getIndexOf(stagiaire.getSemestre()+"")==-1)listeSemestre.addElement(stagiaire.getSemestre()+"");
                view.getListeStagiaire().clearSelection();
                model.supprimerImage(ancienMatricule);
                if(photo!=null){
                    model.enregistrerImage(photo, stagiaire.getMatricule());
                }
                model.mettreAJourStagiaire(stagiaire, ancienMatricule);
                message( I18n.texte("message.MAJ"), I18n.texte("message.MAJ.titre"));
            }else{
                message(I18n.texte("message.mat2"), I18n.texte("message.mat2.titre"));
            }
        }
    }
    private void initialiserBoutton(){
        view.getBtnEnregistrer().addActionListener(e->{
            enregistrer();
        });
        view.getBtnMAJ().addActionListener(e->{
            mettreAJour();
        });
        view.getBtnChanger().addActionListener(e->{
            JFileChooser ouvrirImage=new JFileChooser();
            ouvrirImage.setDialogTitle(I18n.texte("filechooser.titre"));
            ouvrirImage.setFileFilter(new FileNameExtensionFilter(I18n.texte("filechooser.type"), "png","jpg","jpeg"));
            ouvrirImage.showDialog(view, I18n.texte("filechooser.open"));
            photo=ouvrirImage.getSelectedFile();
            if(photo==null)return;
            JPanel image=new ImagePanel(ouvrirImage.getSelectedFile().getAbsolutePath());
            view.getPhoto().removeAll();
            view.getPhoto().add(image,BorderLayout.CENTER);
            view.getPhoto().revalidate();
            view.getPhoto().repaint();
            
        });
        view.getBtnSupprimer().addActionListener(e->{
            view.getPhoto().removeAll();
            view.getPhoto().revalidate();
            view.getPhoto().repaint();
            photo=null;
        });
        view.getBtnVider().addActionListener(e->{
            view.getMatricule().setText("");
            view.getSemestre().setText("");
            view.getSpecialite().setText("");
            view.getGroupe().setText("");
            view.getAdresse().setText("");
            view.getNom().setText("");
            view.getPrenom().setText("");
            view.getLieu().setText("");
            view.getEmail().setText("");
            view.getTelephone().setText("");
            view.getModeStage().clearSelection();
            view.getGenre().clearSelection();
            view.getDateNaissance().setDate(null);
            view.getBtnSupprimer().doClick();
            view.getListeStagiaire().clearSelection();
            view.actualiserQr();
        });
        view.getBtnSupprimerStagiaire().addActionListener(e->{
            if(view.getListeStagiaire().getSelectedValue()!=null){
                JLabel textmessage=new JLabel(I18n.texte("option.sup"));
                textmessage.setFont(textmessage.getFont().deriveFont(24f));
                int reponse=JOptionPane.showConfirmDialog(view, textmessage,I18n.texte("option.sup.titre"),JOptionPane.OK_CANCEL_OPTION);
                if(reponse==JOptionPane.OK_OPTION){
                    ProgressionSuppression prog=new ProgressionSuppression(view, true, model, liste,I18n.texte("prog.sup"));
                    prog.setLocationRelativeTo(null);
                    prog.demarreProgression();
                    prog.setVisible(true);
                    view.getBtnSupprimerStagiaire().setEnabled(false);
                }
            }else{
                message(I18n.texte("message.sup"),I18n.texte("message.sup.titre"));
            }
        });
        view.getBtnImprimDoc().addActionListener(e->{
            if(view.getListeStagiaire().getSelectedValue()!=null){
                Object[] stagiaireSelectionne=view.getListeStagiaire().getSelectedValues();
                for(Object o:stagiaireSelectionne){
                    Stagiaire stagiaire=(Stagiaire)o;
                    infoStagiaire(stagiaire);
                    view.getBtnSupprimer().setVisible(false);
                    view.getBtnChanger().setVisible(false);
                    view.getBtnVider().setVisible(false);
                    view.getBtnMAJ().setVisible(false);
                    view.getBtnEnregistrer().setVisible(false);
                    model.imprimerDocument(view.getDocument(), view.getMatricule().getText());
                }
                view.getBtnSupprimer().setVisible(true);
                view.getBtnChanger().setVisible(true);
                view.getBtnVider().setVisible(true);
                view.getBtnMAJ().setVisible(true);
                view.getBtnEnregistrer().setVisible(true);
                message(I18n.texte("message.imp"),I18n.texte("message.imp.titre"));
            }
            else{
                message(I18n.texte("message.rmp"),I18n.texte("message.rmp.titre"));
            }
        });
        view.getaPropos().addActionListener(e->{
            message(I18n.texte("message.apr"),I18n.texte("message.apr.titre"));
        });
        view.getQuitter().addActionListener(e->view.dispose());
        view.getArabe().addActionListener(e->{
            model.changerLangue(new Locale("ar","DZ"), view);
        });
        view.getFrancais().addActionListener(e->{
            model.changerLangue(Locale.FRENCH, view);
        });
        view.getTabExcel().addActionListener(e->{
            if(liste.isEmpty()){
                message(I18n.texte("excel.error"), "Excel");
                return;
            }
            JFileChooser sauverFichier=new JFileChooser();
            sauverFichier.setDialogTitle(I18n.texte("fileSaver.titre"));
            int selection=sauverFichier.showSaveDialog(view);
            if(selection==JFileChooser.APPROVE_OPTION){
                model.exporterExcel(liste,sauverFichier.getSelectedFile().getAbsolutePath()+".csv",view);
                message(I18n.texte("message.save"),"Excelُ");
            }
        });
        view.getFicExcel().addActionListener(e->{
            JFileChooser ouvrirFichier=new JFileChooser();
            ouvrirFichier.setDialogTitle(I18n.texte("filechooser.titre2"));
            ouvrirFichier.setFileFilter(new FileNameExtensionFilter(I18n.texte("filechooser.type2"), "csv"));
            ouvrirFichier.showDialog(view, I18n.texte("filechooser.open"));
            File fichier=ouvrirFichier.getSelectedFile();
            if(fichier!=null){
                model.importerExcel(liste, fichier,view);
                message(I18n.texte("message.add"),"Excelُ");
            }
        });
    }
    
    private void message(String message,String titre){
        JLabel textMessage=new JLabel(message);
        textMessage.setFont(textMessage.getFont().deriveFont(24f));
        JXDialog dialog=new JXDialog(textMessage);
        dialog.setTitle(titre);
        dialog.setModal(true);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
    private Stagiaire validerSyntaxe(){
        Date d=view.getDateNaissance().getDate();
        LocalDate dateAujourdhui=LocalDate.now();
        LocalDate dateNaissance=d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        long age=ChronoUnit.YEARS.between(dateNaissance, dateAujourdhui);
        if(age <15 || age>35){
            message(I18n.texte("message.age"), I18n.texte("message.age.titre"));
            return null;
        }
        Pattern p=Pattern.compile("[^a-zA-Z0-9]");
        Matcher m=p.matcher(view.getMatricule().getText());
        if(m.find()){
            message(I18n.texte("message.mat"),I18n.texte("message.mat.titre"));
            return null;
        }
        int numeroTel=0;
        if(!view.getTelephone().getText().isEmpty()){
            try{
                numeroTel=Integer.parseInt(view.getTelephone().getText());
                if(numeroTel<0)throw new NumberFormatException();
            }catch(NumberFormatException ex){
                message(I18n.texte("message.num"),I18n.texte("message.num.titre"));
                return null;
            }
        }
        int semestreStagiaire;
        try{
            semestreStagiaire=Integer.parseInt(view.getSemestre().getText());
            if(semestreStagiaire<=0)throw  new NumberFormatException();
        }catch(NumberFormatException ex){
            message(I18n.texte("message.sem"), I18n.texte("message.sem.titre"));
            return null;
        }
        int groupe;
        try{
            groupe=Integer.parseInt(view.getGroupe().getText());
        }catch(NumberFormatException ex){
            message(I18n.texte("message.groupe"), I18n.texte("message.groupe.titre"));
            return null;
        }
        String nom=view.getNom().getText();
        String prenom=view.getPrenom().getText();
        String lieuNaissance=view.getLieu().getText();
        String email=view.getEmail().getText();
        String adresse=view.getAdresse().getText();
        String matricule=view.getMatricule().getText();
        String specialiteStagiare=view.getSpecialite().getText();
        String genre=view.getGenre().getSelection().getActionCommand();
        String modeStage=view.getModeStage().getSelection().getActionCommand();
        return new Stagiaire(nom, prenom, lieuNaissance, d, email, numeroTel, adresse, matricule, specialiteStagiare, groupe, semestreStagiaire, genre, modeStage);
    }
    private void infoStagiaire(Stagiaire stagiaire){
        if(stagiaire==null)return;
        try{
            view.getPhoto().removeAll();
            view.getPhoto().revalidate();
            view.getPhoto().repaint();
            view.getNom().setText(stagiaire.getNom());
            view.getPrenom().setText(stagiaire.getPrenom());
            view.getDateNaissance().setDate(stagiaire.getDateNaissance());
            view.getTelephone().setText(stagiaire.getNumero()+"");
            view.getEmail().setText(stagiaire.getEmail());
            view.getAdresse().setText(stagiaire.getAdresse());
            view.getLieu().setText(stagiaire.getLieuNaissance());
            view.getMatricule().setText(stagiaire.getMatricule());
            view.getSemestre().setText(stagiaire.getSemestre()+"");
            view.getSpecialite().setText(stagiaire.getSpecialite());
            view.getGroupe().setText(stagiaire.getGroupe()+"");
            ImagePanel image=model.retrouverImage(stagiaire.getMatricule(),photo);
            if(image!=null){
                view.getPhoto().add(image,BorderLayout.CENTER);
                view.getPhoto().revalidate();
                view.getPhoto().repaint();
            }
            for(Enumeration<AbstractButton> e=view.getGenre().getElements();e.hasMoreElements();){
                AbstractButton b=e.nextElement();
                if(b.getActionCommand().equalsIgnoreCase(stagiaire.getGenre())){
                    b.setSelected(true);
                    break;
                }
            }
            for(Enumeration<AbstractButton> e=view.getModeStage().getElements();e.hasMoreElements();){
                AbstractButton b=e.nextElement();
                if(b.getActionCommand().equalsIgnoreCase(stagiaire.getModeApprentissage())){
                    b.setSelected(true);
                    break;
                }
            }
        }catch(NullPointerException e){
        }
        
        
        view.actualiserQr();
        
    }
    private int matriculeValide(String matricule){
        for(int i=0;i<liste.size();i++){
            if(liste.getElementAt(i).getMatricule().equalsIgnoreCase(matricule))return i;
        }
        return -1;
    }
}
