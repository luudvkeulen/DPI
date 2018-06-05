package dpiserver;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import model.OrderRequest;

public class FXMLController implements Initializable {

    @FXML
    private ListView listOrderRequest;

    @FXML
    private ListView listServeRequest;

    @FXML
    private ListView listOrderReply;

    @FXML
    private ListView listserveReply;

    public void addOrderRequest(final OrderRequest orderRequest) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                listOrderRequest.getItems().add(orderRequest.type + " " + orderRequest.subType + " - " + orderRequest.time);
            }
        });
        //this.listOrderRequest.getItems().add(orderRequest);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
}
