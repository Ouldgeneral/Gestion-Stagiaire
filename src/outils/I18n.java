package outils;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author Ould_Hamdi
 */
public class I18n {
    private static Locale langue=Locale.getDefault();
    public static ResourceBundle source=ResourceBundle.getBundle("outils.i18n.langue",langue);
    public static void changerLangue(Locale nouvelleLangue){
        langue=nouvelleLangue;
        source=ResourceBundle.getBundle("outils.i18n.langue",langue);
    }
    public static String texte(String cle){
        return source.getString(cle);
    }
}
