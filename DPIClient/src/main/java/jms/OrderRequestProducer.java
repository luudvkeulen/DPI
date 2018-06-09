package jms;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.OrderRequest;

public class OrderRequestProducer {

    private static final String QUEUE_NAME = "order_requests";
    private Gateway gateway;
    private Gson gson = new Gson();

    public OrderRequestProducer() {
        try {
            this.gateway = new Gateway();
            gateway.channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        } catch (IOException | TimeoutException ex) {
            Logger.getLogger(OrderRequestProducer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void send(OrderRequest orderRequest) {
        try {
            String orderRequestJson = gson.toJson(orderRequest);
            this.gateway.channel.basicPublish("", QUEUE_NAME, null, orderRequestJson.getBytes());
        } catch (IOException ex) {
            Logger.getLogger(OrderRequestProducer.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(OrderReplyListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
