package jms;

import com.google.gson.Gson;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import dpiclient.FXMLController;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.OrderReply;

public class OrderReplyListener {

    private static final String QUEUE_NAME = "order_replies";
    private Gateway gateway;
    private static final Gson GSON = new Gson();
    private final FXMLController controller;

    public OrderReplyListener(FXMLController controller) {
        this.controller = controller;
    }

    public void listen() {
        try {
            gateway = new Gateway();
            gateway.channel.queueDeclare(QUEUE_NAME, true, false, false, null);

            System.out.println("Listening for order replies...");

            Consumer consumer = new DefaultConsumer(gateway.channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String message = new String(body);
                    OrderReply orderReply = GSON.fromJson(message, OrderReply.class);
                    controller.addOrderReply(orderReply);
                }
            };

            gateway.channel.basicConsume(QUEUE_NAME, true, consumer);
        } catch (IOException | TimeoutException ex) {
            Logger.getLogger(OrderReplyListener.class.getName()).log(Level.SEVERE, null, ex);
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
