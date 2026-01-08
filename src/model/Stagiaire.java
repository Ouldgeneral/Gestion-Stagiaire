package model;

import java.util.Date;

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
    
}
