package model;

public class ServeReply {

    public String id;
    public String restaurant;
    public int deliveryTime;
    public double price;

    public ServeReply() {
    }

    public ServeReply(String id, String restaurant, int deliveryTime, double price) {
        this.id = id;
        this.restaurant = restaurant;
        this.deliveryTime = deliveryTime;
        this.price = price;
    }

}
