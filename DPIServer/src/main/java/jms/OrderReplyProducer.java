package jms;

import com.google.gson.Gson;
import dpiserver.FXMLController;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.OrderReply;

public class OrderReplyProducer {

    private static final String QUEUE_NAME = "order_replies";
    private Gateway gateway;
    private Gson gson = new Gson();
    private FXMLController controller;

    public OrderReplyProducer(FXMLController controller) {
        this.controller = controller;
        try {
            this.gateway = new Gateway();
            gateway.channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        } catch (IOException | TimeoutException ex) {
            Logger.getLogger(OrderReplyProducer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void send(OrderReply orderReply) {
        try {
            controller.addOrderReply(orderReply);
            String orderReplyJson = gson.toJson(orderReply);
            this.gateway.channel.basicPublish("", QUEUE_NAME, null, orderReplyJson.getBytes());
        } catch (IOException ex) {
            Logger.getLogger(OrderReplyProducer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void stop() {
        try {
            if (gateway.channel.isOpen()) {
                gateway.channel.close();
            }

            if (gateway.connection.isOpen()) {
                gateway.connection.close();
            }
        } catch (IOException | TimeoutException ex) {
            Logger.getLogger(OrderRequestListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
