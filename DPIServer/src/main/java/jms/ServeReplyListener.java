package jms;

import com.google.gson.Gson;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import dpiserver.FXMLController;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.OrderReply;
import model.ServeReply;

public class ServeReplyListener {
    private static final String QUEUE_NAME = "serve_replies";
    private static final Gson GSON = new Gson();
    private final FXMLController controller;
    private Gateway gateway;
    private final OrderReplyProducer orderReplyProducer;

    public ServeReplyListener(FXMLController controller) {
        this.controller = controller;
        this.orderReplyProducer = new OrderReplyProducer(controller);
    }
    
    public void listen() {
        try {
            gateway = new Gateway();
            gateway.channel.queueDeclare(QUEUE_NAME, true, false, false, null);

            Consumer consumer = new DefaultConsumer(gateway.channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String message = new String(body);
                    ServeReply serveReply = GSON.fromJson(message, ServeReply.class);
                    controller.addServeReply(serveReply);
                    
                    OrderReply orderReply = new OrderReply();
                    orderReply.id = serveReply.id;
                    orderReply.deliveryTime = serveReply.deliveryTime;
                    orderReply.price = serveReply.price;
                    orderReply.restaurant = serveReply.restaurant;
                    orderReplyProducer.send(orderReply);
                }
            };

            gateway.channel.basicConsume(QUEUE_NAME, true, consumer);
        } catch (IOException | TimeoutException ex) {
            Logger.getLogger(OrderRequestListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void stop() {
        orderReplyProducer.stop();
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
