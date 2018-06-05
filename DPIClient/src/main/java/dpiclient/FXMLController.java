package dpiclient;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import jms.OrderRequestProducer;
import model.OrderRequest;
import model.SubType;
import model.Type;

public class FXMLController implements Initializable {
    
    @FXML
    private ComboBox comboType;
    
    @FXML
    private ComboBox comboSubType;
    
    @FXML
    private ListView orderList;
    
    @FXML
    private ListView replyList;
    
    private final OrderRequestProducer orderRequestProducer = new OrderRequestProducer();
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        this.orderList.getItems().add(comboType.getValue() + " - " + comboSubType.getValue());
        OrderRequest orderRequest = new OrderRequest(comboType.getValue().toString(), comboSubType.getValue().toString());
        orderRequestProducer.send(orderRequest);
    }
    
    @FXML
    private void handleComboTypeAction(ActionEvent event) {
        this.comboSubType.setDisable(false);
        List<SubType> subTypes = ((Type)comboType.getValue()).subTypes;
        this.comboSubType.getItems().clear();
        this.comboSubType.getItems().addAll(subTypes);
        if(!subTypes.isEmpty()) {
            this.comboSubType.setValue(subTypes.get(0));
        }
    }
    
    @FXML
    private void handleBtnAcceptAction(ActionEvent event) {
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Type type1 = new Type("Pizza");
        type1.subTypes.add(new SubType("Margharita"));
        type1.subTypes.add(new SubType("Salami"));
        type1.subTypes.add(new SubType("Ham"));
        comboType.getItems().add(type1);
        Type type2 = new Type("Soep");
        type2.subTypes.add(new SubType("Kip"));
        type2.subTypes.add(new SubType("Groenten"));
        type2.subTypes.add(new SubType("Tomaten"));
        comboType.getItems().add(type2);
    }    
}
