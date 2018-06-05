package dpiserver;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jms.OrderRequestListener;
import model.OrderRequest;

public class MainApp extends Application {

    private OrderRequestListener orderRequestListener;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Scene.fxml"));
        FXMLController controller = new FXMLController();
        loader.setController(controller);
        //Parent root = FXMLLoader.load(getClass().getResource("/fxml/Scene.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/Styles.css");

        stage.setTitle("DPI Server");
        stage.setScene(scene);
        stage.show();

        orderRequestListener = new OrderRequestListener(controller);
        orderRequestListener.listen();
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
        orderRequestListener.stop();
        super.stop();
    }

}
