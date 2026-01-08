package controller;

import View.View;
import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.io.File;
import java.util.Date;
import java.util.Enumeration;
import javax.swing.AbstractButton;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import model.Model;
import model.Stagiaire;
import org.jdesktop.swingx.JXDialog;
import outils.ImagePanel;
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
    String semestre="Tout";
    String specialite="Tout";
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
    }
    private void filtrer(){
        DefaultListModel<Stagiaire> listeFiltrer=new DefaultListModel<>();
        if(!semestre.equals("Tout") && specialite.equals("Tout")){
            for(int i=0;i<liste.size();i++){
                if((liste.getElementAt(i).getSemestre()+"").equals(semestre)){
                    listeFiltrer.addElement(liste.getElementAt(i));
                }
            }
        }
        else if(semestre.equals("Tout") && !specialite.equals("Tout")){
            for(int i=0;i<liste.size();i++){
                if((liste.getElementAt(i).getSpecialite()).equalsIgnoreCase(specialite)){
                    listeFiltrer.addElement(liste.getElementAt(i));
                }
            }
        }
        else if(!semestre.equals("Tout") && !specialite.equals("Tout")){
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
                message("Ce matricule appartient deja a un stagiaire veuillez corriger l'erreur ou mettre a jour les matricule", "Redondance de matricule");
                return ;
            }
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
            message("Veuillez selectionnez le stagiaire a modifier d'abord", "Aucun stagiaire precise");
            return;
        }
        String ancienMatricule=stagiaire.getMatricule();
        stagiaire=validerSyntaxe();
        if(stagiaire!=null){
            int index=matriculeValide(ancienMatricule);
            if(index!=-1){
                liste.getElementAt(index).setAdresse(stagiaire.getAdresse());
                liste.getElementAt(index).setNumero(stagiaire.getNumero());
                liste.getElementAt(index).setEmail(stagiaire.getEmail());
                liste.getElementAt(index).setNom(stagiaire.getNom());
                liste.getElementAt(index).setPrenom(stagiaire.getPrenom());
                liste.getElementAt(index).setDateNaissance(stagiaire.getDateNaissance());
                liste.getElementAt(index).setGenre(stagiaire.getGenre());
                liste.getElementAt(index).setGroupe(stagiaire.getGroupe());
                liste.getElementAt(index).setSemestre(stagiaire.getSemestre());
                liste.getElementAt(index).setLieuNaissance(stagiaire.getLieuNaissance());
                liste.getElementAt(index).setSpecialite(stagiaire.getSpecialite());
                liste.getElementAt(index).setModeApprentissage(stagiaire.getModeApprentissage());
                liste.getElementAt(index).setMatricule(stagiaire.getMatricule());
                if(listeSpecialite.getIndexOf(stagiaire.getSpecialite())==-1)listeSpecialite.addElement(stagiaire.getSpecialite());
                if(listeSemestre.getIndexOf(stagiaire.getSemestre()+"")==-1)listeSemestre.addElement(stagiaire.getSemestre()+"");
                view.getListeStagiaire().clearSelection();
                model.supprimerImage(ancienMatricule);
                if(photo!=null){
                    model.enregistrerImage(photo, stagiaire.getMatricule());
                }
                model.mettreAJourStagiaire(stagiaire, ancienMatricule);
                message("Donne mise a jour", "Modification reussi");
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
            ouvrirImage.setDialogTitle("Choisissez une photo du Stagiaire");
            ouvrirImage.setFileFilter(new FileNameExtensionFilter("Image", "png","jpg","jpeg"));
            ouvrirImage.showDialog(view, "Ouvrir");
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
            model.supprimerStagiaire(((Stagiaire)view.getListeStagiaire().getSelectedValue()).getMatricule());
            liste.removeElement(view.getListeStagiaire().getSelectedValue());
            view.getBtnSupprimerStagiaire().setEnabled(false);
        });
        view.getBtnImprimDoc().addActionListener(e->{
            if(view.getBtnEnregistrer().isEnabled()){
                view.getBtnSupprimer().setVisible(false);
                view.getBtnChanger().setVisible(false);
                view.getBtnVider().setVisible(false);
                view.getBtnMAJ().setVisible(false);
                view.getBtnEnregistrer().setVisible(false);
                model.imprimerDocument(view.getDocument(), view.getMatricule().getText());
                view.getBtnSupprimer().setVisible(true);
                view.getBtnChanger().setVisible(true);
                view.getBtnVider().setVisible(true);
                view.getBtnMAJ().setVisible(true);
                view.getBtnEnregistrer().setVisible(true);
                message("Document imprime avec succes", "Impression");
            }else{
                message("Veuillez remplir le document d'abord", "Document non empli");
            }
        });
    }
    private void message(String message,String titre){
        JXDialog dialog=new JXDialog(new JLabel(message));
        dialog.setTitle(titre);
        dialog.setModal(true);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
    private Stagiaire validerSyntaxe(){
        Date d=view.getDateNaissance().getDate();
        if((new Date().getYear())-d.getYear()<15 || (new Date().getYear())-d.getYear()>35){
            message("Age non accepter il faut etre entre 15 et 35 ans pour pouvoir s'inscrire", "Erreur d'Age");
            return null;
        }
        int numeroTel=0;
        if(!view.getTelephone().getText().isEmpty()){
            try{
                numeroTel=Integer.parseInt(view.getTelephone().getText());
                if(numeroTel<0)throw new NumberFormatException();
            }catch(NumberFormatException ex){
                message("Numero telephone invalide le numero doit etre un nombre entier superieur a 0","Numero telephone erronne");
                return null;
            }
        }
        int semestreStagiaire;
        try{
            semestreStagiaire=Integer.parseInt(view.getSemestre().getText());
            if(semestreStagiaire<=0)throw  new NumberFormatException();
        }catch(NumberFormatException ex){
            message("Le semestre doit etre un nombre entier superieur a 0", "Semestre erronne");
            return null;
        }
        int groupe;
        try{
            groupe=Integer.parseInt(view.getGroupe().getText());
        }catch(NumberFormatException ex){
            message("Numero Groupe Invalide le numero de groue est un entier superieur a 0", "Numero de Groupe erronne");
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
                if(b.getText().equalsIgnoreCase(stagiaire.getGenre())){
                    b.setSelected(true);
                    break;
                }
            }
            for(Enumeration<AbstractButton> e=view.getModeStage().getElements();e.hasMoreElements();){
                AbstractButton b=e.nextElement();
                if(b.getText().equalsIgnoreCase(stagiaire.getModeApprentissage())){
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
