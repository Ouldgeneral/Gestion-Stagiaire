package model;

import java.util.Date;
import outils.I18n;

/**
 * @author Ould_Hamdi
 */
public class Stagiaire {
    private String nom;
    private String prenom;
    private String lieuNaissance;
    private Date dateNaissance;
    private String email;
    private long numero;
    private String adresse;
    private String matricule;
    private String specialite;
    private int groupe;
    private int semestre;
    private String genre;
    private String modeApprentissage;
    public Stagiaire(){
        
    }
    public Stagiaire(String nom, String prenom, String lieuNaissance, Date dateNaissance, String email, long numero, String adresse, String matricule, String specialite, int groupe, int semestre, String genre, String modeApprentissage) {
        this.nom = nom;
        this.prenom = prenom;
        this.lieuNaissance = lieuNaissance;
        this.dateNaissance = dateNaissance;
        this.email = email;
        this.numero = numero;
        this.adresse = adresse;
        this.matricule = matricule;
        this.specialite = specialite;
        this.groupe = groupe;
        this.semestre = semestre;
        this.genre = genre;
        this.modeApprentissage = modeApprentissage;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getLieuNaissance() {
        return lieuNaissance;
    }

    public void setLieuNaissance(String lieuNaissance) {
        this.lieuNaissance = lieuNaissance;
    }

    public Date getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getNumero() {
        return numero;
    }

    public void setNumero(long numero) {
        this.numero = numero;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getSpecialite() {
        return specialite;
    }

    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }

    public int getGroupe() {
        return groupe;
    }

    public void setGroupe(int groupe) {
        this.groupe = groupe;
    }

    public int getSemestre() {
        return semestre;
    }

    public void setSemestre(int semestre) {
        this.semestre = semestre;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getModeApprentissage() {
        return modeApprentissage;
    }

    public void setModeApprentissage(String modeApprentissage) {
        this.modeApprentissage = modeApprentissage;
    }
    @Override
    public String toString() {
        return nom+" "+prenom;
    }
    public void mettreAJour(Stagiaire nouveauStagiaire){
        setNom(nouveauStagiaire.getNom());
        setPrenom(nouveauStagiaire.getPrenom());
        setMatricule(nouveauStagiaire.getMatricule());
        setDateNaissance(nouveauStagiaire.getDateNaissance());
        setLieuNaissance(nouveauStagiaire.getLieuNaissance());
        setAdresse(nouveauStagiaire.getAdresse());
        setSemestre(nouveauStagiaire.getSemestre());
        setSpecialite(nouveauStagiaire.getSpecialite());
        setGroupe(nouveauStagiaire.getGroupe());
        setGenre(nouveauStagiaire.getGenre());
        setModeApprentissage(nouveauStagiaire.getModeApprentissage());
        setEmail(nouveauStagiaire.getEmail());
        setNumero(nouveauStagiaire.getNumero());
        
    }
    public String toExcel(){
        String formatText="\"%s\";";
        String mode=I18n.texte("radio.cos");
        switch(modeApprentissage){
            case "residentiel"->mode=I18n.texte("radio.reside");
            case "apprentissage"->mode=I18n.texte("radio.apprenti");
        }
        StringBuilder textExcel=new StringBuilder();
        textExcel.append(formatText.formatted(matricule.replace("\"", "\"\"")));
        textExcel.append(formatText.formatted(nom.replace("\"", "\"\"")));
        textExcel.append(formatText.formatted(prenom.replace("\"", "\"\"")));
        textExcel.append(formatText.formatted(dateNaissance.toString().replace("\"", "\"\"")));
        textExcel.append(formatText.formatted(lieuNaissance.replace("\"", "\"\"")));
        textExcel.append("\"").append(I18n.texte("radio."+genre).replace("\"", "\"\"")).append("\";");
        textExcel.append(formatText.formatted(specialite.replace("\"", "\"\"")));
        textExcel.append(formatText.formatted(semestre+""));
        textExcel.append(formatText.formatted(groupe+""));
        textExcel.append(formatText.formatted(mode));
        textExcel.append(formatText.formatted(numero+""));
        textExcel.append(formatText.formatted(email));
        formatText="\"%s\"";
        textExcel.append(formatText.formatted(adresse));
        
        return textExcel.toString();
    }
    
}
