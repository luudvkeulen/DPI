package dpirestaurant1;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jms.ServeRequestListener;


public class MainApp extends Application {
    
    private FXMLController controller;

    @Override
    public void start(Stage stage) throws Exception {          
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Scene.fxml"));
        FXMLController controller = new FXMLController();
        this.controller = controller;
        loader.setController(controller);
        Parent root = loader.load();
        //Parent root = FXMLLoader.load(getClass().getResource("/fxml/Scene.fxml"));
        
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/Styles.css");
        
        stage.setTitle("Restaurant 1");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() throws Exception {
        controller.stop();
        super.stop(); //To change body of generated methods, choose Tools | Templates.
    }
    
    

}
