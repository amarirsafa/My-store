package com.example.mystore.Classes;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Order {
    private Item itemToOrder;
    private User user;
    private String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(Calendar.getInstance().getTime());
    private String orderId;
    private int randomNum = 200 + (int)(Math.random() * 900);
    public Order(){}


    public Item getItemToOrder() { return itemToOrder; }
    public void setItemToOrder(Item itemToOrder) { this.itemToOrder = itemToOrder; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getCurrentDate() { return currentDate; }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    public int getRandomNum(){return this.randomNum;}
}
