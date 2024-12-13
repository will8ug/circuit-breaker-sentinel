package net.will.circuitbreaker.domain;

import org.springframework.hateoas.RepresentationModel;

public class Account extends RepresentationModel<Account> {
    public static Account getInstance(String id, String username) {
        Account obj = new Account();
        obj.setId(id);
        obj.setUsername(username);
        return obj;
    }

    private String id;
    private String username;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
