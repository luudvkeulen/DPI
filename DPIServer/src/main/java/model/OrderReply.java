package model;

public class OrderReply {

    public String id;
    public String restaurant;
    public int deliveryTime;
    public double price;

    public OrderReply() {
    }

    public OrderReply(String id, String restaurant, int deliveryTime, double price) {
        this.id = id;
        this.restaurant = restaurant;
        this.deliveryTime = deliveryTime;
        this.price = price;
    }

}
