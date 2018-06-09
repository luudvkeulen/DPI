package jms;

import com.google.gson.Gson;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import dpirestaurant1.FXMLController;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.ServeReply;
import model.ServeRequest;

public class ServeRequestListener {

    private final List<String> exchanges;
    private static final Gson GSON = new Gson();
    private Gateway gateway;
    private final FXMLController controller;
    private final ServeReplyProducer serveReplyProducer;
    private final Random r = new Random();
    private final String name;

    public ServeRequestListener(FXMLController controller, List<String> exchanges, String name) {
        this.controller = controller;
        //this.exchanges.add("Pizza");
        //this.exchanges.add("Soep");
        this.exchanges = exchanges;
        this.name = name;
        this.serveReplyProducer = new ServeReplyProducer(controller, name);

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
            for(String exchange : this.exchanges) {
                gateway.channel.queueBind(queueName, exchange, "");
            }
            //gateway.channel.queueBind(queueName, "Soep", "");

            Consumer consumer = new DefaultConsumer(gateway.channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String message = new String(body);
                    ServeRequest serveRequest = GSON.fromJson(message, ServeRequest.class);
                    controller.addServeRequest(serveRequest);
                    
                    ServeReply serveReply = new ServeReply();
                    serveReply.restaurant = name;
                    serveReply.id = serveRequest.id;
                    serveReply.price = controller.getPrice();
                    serveReply.deliveryTime = (r.nextInt(25) + 5);
                    serveReplyProducer.send(serveReply);
                }
            };

            gateway.channel.basicConsume(queueName, true, consumer);
        } catch (IOException ex) {
            Logger.getLogger(ServeRequestListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void stop() {
        serveReplyProducer.stop();
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
