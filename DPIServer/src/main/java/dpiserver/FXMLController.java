package dpiserver;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import model.OrderReply;
import model.OrderRequest;
import model.ServeReply;
import model.ServeRequest;

public class FXMLController implements Initializable {

    @FXML
    private ListView listOrderRequest;

    @FXML
    private ListView listServeRequest;

    @FXML
    private ListView listOrderReply;

    @FXML
    private ListView listServeReply;

    public void addOrderRequest(final OrderRequest orderRequest) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                listOrderRequest.getItems().add(orderRequest.type + " " + orderRequest.subType + " - " + orderRequest.time);
            }
        });
    }

    public void addOrderReply(final OrderReply orderReply) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                listOrderReply.getItems().add(orderReply.restaurant + " " + orderReply.price + " - " + orderReply.deliveryTime + " min");
            }
        });
    }

    public void addServeRequest(final ServeRequest serveRequest) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                listServeRequest.getItems().add(serveRequest.id + " - " + serveRequest.type + " " + serveRequest.subType + " - " + serveRequest.time);
            }
        });
    }
    
    public void addServeReply(final ServeReply serveReply) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                listServeReply.getItems().add(serveReply.id + " - " + serveReply.restaurant + " - " + serveReply.price + " - " + serveReply.deliveryTime);
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
}
