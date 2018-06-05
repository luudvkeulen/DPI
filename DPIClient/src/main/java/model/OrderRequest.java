package model;

import java.util.Date;
import java.util.UUID;

public class OrderRequest {

    public String id;
    public String type;
    public String subType;
    public Date time;

    public OrderRequest() {

    }

    public OrderRequest(String type, String subType) {
        this.id = UUID.randomUUID().toString();
        this.type = type;
        this.subType = subType;
        this.time = new Date();
    }
}
