package dpiclient;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jms.OrderReplyListener;

public class MainApp extends Application {

    private OrderReplyListener orderReplyListener;
    private FXMLController controller;

    @Override
    public void start(Stage stage) throws Exception {
        //Parent root = FXMLLoader.load(getClass().getResource("/fxml/Scene.fxml"));

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Scene.fxml"));
        FXMLController controller = new FXMLController();
        loader.setController(controller);
        Parent root = loader.load();

        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/Styles.css");

        stage.setTitle("DPI Client");
        stage.setScene(scene);
        stage.show();

        this.controller = controller;
        
        orderReplyListener = new OrderReplyListener(controller);
        orderReplyListener.listen();
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
        orderReplyListener.stop();
        controller.stop();
        super.stop();
    }

}
