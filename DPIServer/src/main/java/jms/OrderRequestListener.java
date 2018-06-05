package jms;

import com.google.gson.Gson;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.OrderRequest;

public class OrderRequestListener {

    private static final String QUEUE_NAME = "order_requests";
    private static final Gson GSON = new Gson();
    private Gateway gateway;

    public void listen() {
        try {
            gateway = new Gateway();
            gateway.channel.queueDeclare(QUEUE_NAME, true, false, false, null);

            System.out.println("Listening for order requests...");

            Consumer consumer = new DefaultConsumer(gateway.channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String message = new String(body);
                    OrderRequest orderRequest = GSON.fromJson(message, OrderRequest.class);
                    System.out.println("Received order: " + orderRequest.type + " " + orderRequest.subType);
                }
            };
            
            gateway.channel.basicConsume(QUEUE_NAME, true, consumer);
        } catch (IOException | TimeoutException ex) {
            Logger.getLogger(OrderRequestListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void stop() {
        try {
            gateway.channel.close();
            gateway.connection.close();
        } catch (IOException | TimeoutException ex) {
            Logger.getLogger(OrderRequestListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
