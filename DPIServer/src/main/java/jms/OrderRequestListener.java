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
import model.OrderRequest;
import model.ServeRequest;

public class OrderRequestListener {

    private static final String QUEUE_NAME = "order_requests";
    private static final Gson GSON = new Gson();
    private Gateway gateway;
    private FXMLController controller;
    private OrderReplyProducer orderReplyProducer;
    private final ServeRequestProducer serveRequestProducer;

    public OrderRequestListener(FXMLController controller) {
        this.controller = controller;
        orderReplyProducer = new OrderReplyProducer(controller);
        serveRequestProducer = new ServeRequestProducer(controller);
    }

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
                    controller.addOrderRequest(orderRequest);
                    orderReplyProducer.send(new OrderReply(orderRequest.id, "KFC", 15, 12.50));
                    serveRequestProducer.send(new ServeRequest(orderRequest.id, orderRequest.type, orderRequest.subType, orderRequest.time));
                }
            };

            gateway.channel.basicConsume(QUEUE_NAME, true, consumer);
        } catch (IOException | TimeoutException ex) {
            Logger.getLogger(OrderRequestListener.class.getName()).log(Level.SEVERE, null, ex);
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
