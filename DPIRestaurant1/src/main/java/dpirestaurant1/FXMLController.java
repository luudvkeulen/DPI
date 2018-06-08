package dpirestaurant1;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import model.ServeRequest;

public class FXMLController implements Initializable {

    @FXML
    private ListView listServeRequest;

    @FXML
    private Spinner spinnerPrice;

    public void addServeRequest(final ServeRequest serveRequest) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                listServeRequest.getItems().add(serveRequest.id + " - " + serveRequest.type + " " + serveRequest.subType);
            }
        });
    }

    @Override

    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
}
