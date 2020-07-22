package com.example.mystore.Classes;

import java.util.ArrayList;

public class Order {
    private Item itemToOrder;
    private User user;

    public Order(){}
    public Order(Item itemToOrder,User user){
        setItemToOrder(itemToOrder);
        setUser(user);
    }

    public Item getItemToOrder() { return itemToOrder; }
    public void setItemToOrder(Item itemToOrder) { this.itemToOrder = itemToOrder; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
