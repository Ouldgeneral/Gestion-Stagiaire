package outils;
import java.io.File;
import java.sql.*;
import java.text.SimpleDateFormat;
import javax.swing.DefaultListModel;
import model.Stagiaire;
/**
 * @author Ould_Hamdi
 */
public class GestionStageBD {
    Connection con;
    public GestionStageBD(String emplacement){
        new File(emplacement).getParentFile().mkdirs();
        try{
            con=DriverManager.getConnection("jdbc:sqlite:"+emplacement);
            if(con!=null){
                String sql="""
                           CREATE TABLE IF NOT EXISTS STAGIAIRES(
                           MATRICULE VARCHAR(50) PRIMARY KEY NOT NULL,
                           NOM VARCHAR(50) ,
                           PRENOM VARCHAR(50),
                           DATENAISSANCE TEXT,
                           LIEUNAISSANCE VARCHAR(50),
                           ADRESSE VARCHAR(50),
                           TELEPHONE INT,
                           GENRE CHAR(5),
                           SPECIALITE VARCHAR(50),
                           SEMESTRE INT,
                           GROUPE INT,
                           MODESTAGE VARCHAR(50),
                           EMAIL VARCHAR(50)
                           )
                           """;
                Statement st=con.createStatement();
                st.execute(sql);
            }
        }catch(SQLException e){
        }
    }
    public void mettreStagiaireAJour(Stagiaire stagiaire,String ancienMatricule){
        supprimerStagiaire(ancienMatricule);
        ajouterStagiaires(stagiaire);
    }
    public void supprimerStagiaire(String matricule){
        String sql="DELETE FROM STAGIAIRES WHERE MATRICULE='"+matricule+"'";
        try {
            Statement st=con.createStatement();
            st.executeUpdate(sql);
        } catch (SQLException ex) {
        }
        
    }
    public void ajouterStagiaires(Stagiaire stagiaire){
        String sql="INSERT INTO STAGIAIRES(MATRICULE,NOM,PRENOM,DATENAISSANCE,"
                + "LIEUNAISSANCE,ADRESSE,TELEPHONE,GENRE,SPECIALITE,SEMESTRE,GROUPE,MODESTAGE,EMAIL)"
                + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement ps=con.prepareStatement(sql);
            ps.setString(1,stagiaire.getMatricule());
            ps.setString(2,stagiaire.getNom());
            ps.setString(3,stagiaire.getPrenom());
            ps.setLong(4,stagiaire.getDateNaissance().getTime());
            ps.setString(5,stagiaire.getLieuNaissance());
            ps.setString(6,stagiaire.getAdresse());
            ps.setString(7,stagiaire.getNumero()+"");
            ps.setString(8,stagiaire.getGenre());
            ps.setString(9,stagiaire.getSpecialite());
            ps.setString(10,stagiaire.getSemestre()+"");
            ps.setString(11,stagiaire.getGroupe()+"");
            ps.setString(12,stagiaire.getModeApprentissage());
            ps.setString(13,stagiaire.getEmail());
            ps.executeUpdate();
        } catch (SQLException ex) {
            
        }
    }
    public DefaultListModel<Stagiaire> chargerListe(){
        String matricule,nom,prenom,lieu,adresse,genre,specialite,mode,email;
        int semestre,numero,groupe;
        Date dateNaissance;
        DefaultListModel<Stagiaire> liste=new DefaultListModel<>();
        String sql="SELECT * FROM STAGIAIRES";
        Statement st;
        try {
            st = con.createStatement();
            ResultSet rs=st.executeQuery(sql);
            while(rs.next()){
                matricule=rs.getString(1);
                nom=rs.getString(2);
                prenom=rs.getString(3);
                dateNaissance=new Date(rs.getLong("DATENAISSANCE"));
                lieu=rs.getString(5);
                adresse=rs.getString(6);
                numero=rs.getInt(7);
                genre=rs.getString(8);
                specialite=rs.getString(9);
                semestre=rs.getInt(10);
                groupe=rs.getInt(11);
                mode=rs.getString(12);
                email=rs.getString(13);
                liste.addElement(new Stagiaire(nom, prenom, lieu, dateNaissance,
                        email, numero, adresse, matricule, specialite, groupe, semestre, genre, mode));
            }
        } catch (SQLException ex) {
        }
        return liste;
    }
}
