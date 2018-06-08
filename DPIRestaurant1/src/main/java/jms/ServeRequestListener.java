package jms;

import com.google.gson.Gson;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import dpirestaurant1.FXMLController;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.ServeRequest;

public class ServeRequestListener {

    private final List<String> exchanges = new ArrayList<>();
    private static final Gson GSON = new Gson();
    private Gateway gateway;
    private final FXMLController controller;

    public ServeRequestListener(FXMLController controller) {
        this.controller = controller;
        this.exchanges.add("Pizza");
        this.exchanges.add("Soep");

        try {
            this.gateway = new Gateway();
            for (String exchange : exchanges) {
                gateway.channel.exchangeDeclare(exchange, BuiltinExchangeType.FANOUT);
            }
        } catch (IOException | TimeoutException ex) {
            Logger.getLogger(ServeRequestListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void listen() {
        try {
            String queueName = gateway.channel.queueDeclare().getQueue();
            gateway.channel.queueBind(queueName, "Soep", "");

            Consumer consumer = new DefaultConsumer(gateway.channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String message = new String(body);
                    ServeRequest serveRequest = GSON.fromJson(message, ServeRequest.class);
                    controller.addServeRequest(serveRequest);
                }
            };

            gateway.channel.basicConsume(queueName, true, consumer);
        } catch (IOException ex) {
            Logger.getLogger(ServeRequestListener.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(ServeRequestListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
