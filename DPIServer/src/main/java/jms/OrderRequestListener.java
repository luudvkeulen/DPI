package jms;

import com.google.gson.Gson;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import dpiserver.FXMLController;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.OrderRequest;
import model.ServeRequest;
import java.util.Timer;
import java.util.TimerTask;
import model.OrderReply;

public class OrderRequestListener {

    private static final String QUEUE_NAME = "order_requests";
    private static final Gson GSON = new Gson();
    private Gateway gateway;
    private FXMLController controller;
    private final ServeRequestProducer serveRequestProducer;
    private final OrderReplyProducer orderReplyProducer;
    //private final List<Timer> timers = new ArrayList<>();
    public static final HashMap<String, Timer> timers = new HashMap<>();

    public OrderRequestListener(FXMLController controller) {
        this.controller = controller;
        serveRequestProducer = new ServeRequestProducer(controller);
        orderReplyProducer = new OrderReplyProducer(controller);
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
                    final OrderRequest orderRequest = GSON.fromJson(message, OrderRequest.class);
                    System.out.println("Received order: " + orderRequest.type + " " + orderRequest.subType);
                    controller.addOrderRequest(orderRequest);
                    serveRequestProducer.send(new ServeRequest(orderRequest.id, orderRequest.type, orderRequest.subType, orderRequest.time));
                    Timer t = new Timer();
                    t.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            orderReplyProducer.send(new OrderReply(orderRequest.id, "No result", 0, 0));
                        }
                    }, 10000);
                    timers.put(orderRequest.id, t);
                }
            };

            gateway.channel.basicConsume(QUEUE_NAME, true, consumer);
        } catch (IOException | TimeoutException ex) {
            Logger.getLogger(OrderRequestListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void stop() {
        serveRequestProducer.stop();
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
