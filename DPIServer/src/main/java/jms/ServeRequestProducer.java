package jms;

import com.google.gson.Gson;
import com.rabbitmq.client.BuiltinExchangeType;
import dpiserver.FXMLController;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.ServeRequest;

public class ServeRequestProducer {
    private Gateway gateway;
    private Gson gson = new Gson();
    private FXMLController controller;
    private final List<String> exchanges = new ArrayList<>();

    public ServeRequestProducer(FXMLController controller) {
        this.exchanges.add("Pizza");
        this.exchanges.add("Soep");
        this.controller = controller;
        try {
            this.gateway = new Gateway();
            for(String exchange : exchanges) {
                gateway.channel.exchangeDeclare(exchange, BuiltinExchangeType.FANOUT);
            }
        } catch (IOException | TimeoutException ex) {
            Logger.getLogger(OrderReplyProducer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void send(ServeRequest serveRequest) {
        try {
            controller.addServeRequest(serveRequest);
            String orderReplyJson = gson.toJson(serveRequest);
            this.gateway.channel.basicPublish(serveRequest.type, "", null, orderReplyJson.getBytes());
        } catch (IOException ex) {
            Logger.getLogger(OrderReplyProducer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
