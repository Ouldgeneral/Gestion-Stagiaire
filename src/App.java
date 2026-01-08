
import View.View;
import controller.Controller;
import model.Model;
/**
 * @author Ould_Hamdi
 */
public class App {
    public static void main(String[] args) {
       View view=new View();
       Model model =new Model();
       Controller controller=new Controller(view, model);
       controller.demarrer();
    }
}
