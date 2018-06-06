package model;

import java.util.Date;
import java.util.UUID;

public class ServeRequest {

    public String id;
    public String type;
    public String subType;
    public Date time;

    public ServeRequest() {

    }

    public ServeRequest(String type, String subType) {
        this.id = UUID.randomUUID().toString();
        this.type = type;
        this.subType = subType;
        this.time = new Date();
    }

    public ServeRequest(String id, String type, String subType, Date time) {
        this(type, subType);
        this.subType = subType;
        this.time = time;
    }
}
