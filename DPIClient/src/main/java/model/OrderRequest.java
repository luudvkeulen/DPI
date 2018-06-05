package model;

import java.util.Date;

public class OrderRequest {

    public String type;
    public String subType;
    public Date time;

    public OrderRequest() {

    }

    public OrderRequest(String type, String subType) {
        this.type = type;
        this.subType = subType;
        this.time = new Date();
    }
}
