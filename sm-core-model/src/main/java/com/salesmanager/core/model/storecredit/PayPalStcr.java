package com.salesmanager.core.model.storecredit;

import javax.persistence.*;

@Entity
@Table(name = "PAYPALSTCR")
public class PayPalStcr {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private long id;
    @Column(name = "customerid")
    private long customerid;
    @Column(name = "price")
    private long price;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCustomerid() {
        return customerid;
    }

    public void setCustomerid(long customerid) {
        this.customerid = customerid;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }
}