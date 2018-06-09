package dpirestaurant1;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import jms.ServeRequestListener;
import model.ServeRequest;

public class FXMLController implements Initializable {

    @FXML
    private ListView listServeRequest;

    @FXML
    private TextField inputPrice;

    @FXML
    private TextField inputName;

    @FXML
    private CheckBox checkPizza;

    @FXML
    private CheckBox checkSoep;

    public FXMLController() {
    }

    private ServeRequestListener serveRequestListener;

    public void addServeRequest(final ServeRequest serveRequest) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                listServeRequest.getItems().add(serveRequest.id + " - " + serveRequest.type + " " + serveRequest.subType);
            }
        });
    }

    @FXML
    public void onBtnStartClick(ActionEvent e) {
        List<String> exchanges = new ArrayList<>();
        if(checkPizza.isSelected()) {
            exchanges.add("Pizza");
        }
        
        if(checkSoep.isSelected()) {
            exchanges.add("Soep");
        }
        
        checkPizza.setDisable(true);
        checkSoep.setDisable(true);
        inputName.setDisable(true);
        serveRequestListener = new ServeRequestListener(this, exchanges, inputName.getText());
        serveRequestListener.listen();
    }

    public Double getPrice() {
        return Double.parseDouble(inputPrice.getText());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void stop() {
        this.serveRequestListener.stop();
    }
}
