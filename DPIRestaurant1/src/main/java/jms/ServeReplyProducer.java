/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jms;

import com.google.gson.Gson;
import dpirestaurant1.FXMLController;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.ServeReply;

public class ServeReplyProducer {

    private static final String QUEUE_NAME = "serve_replies";
    private Gateway gateway;
    private Gson gson = new Gson();
    private final FXMLController controller;
    private final String name;

    public ServeReplyProducer(FXMLController controller, String name) {
        this.controller = controller;
        this.name = name;
        
        try {
            this.gateway = new Gateway();
            gateway.channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        } catch (IOException | TimeoutException ex) {
            Logger.getLogger(ServeReplyProducer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void send(ServeReply serveReply) {
        try {
            String orderRequestJson = gson.toJson(serveReply);
            this.gateway.channel.basicPublish("", QUEUE_NAME, null, orderRequestJson.getBytes());
        } catch (IOException ex) {
            Logger.getLogger(ServeReplyProducer.class.getName()).log(Level.SEVERE, null, ex);
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
