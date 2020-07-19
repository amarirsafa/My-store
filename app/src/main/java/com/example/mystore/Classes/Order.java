package com.example.mystore.Classes;

import java.util.ArrayList;

public class Order {
    private ArrayList<Item> itemsToOrder;
    private int orderID;

    public Order(){}
    public Order(ArrayList<Item> itemsToOrder,int orderID){
        setItemsToOrder(itemsToOrder);
        setOrderID(orderID);
    }

    public ArrayList<Item> getItemsToOrder() { return itemsToOrder; }
    public void setItemsToOrder(ArrayList<Item> itemsToOrder) { this.itemsToOrder = itemsToOrder; }

    public int getOrderID() { return orderID; }
    public void setOrderID(int orderID) { this.orderID = orderID; }
}
