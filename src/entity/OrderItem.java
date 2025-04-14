/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

/**
 *
 * @author trong
 */
public class OrderItem {
    private Product product;
    private double quantity;
    
    public OrderItem(Product product, double quantity) {
        this.product = product;
        this.quantity = quantity;
    }
    
    // Getter và setter
    public Product getProduct() { return product; }
    public double getQuantity() { return quantity; }
    public void setQuantity(double quantity) { this.quantity = quantity; }
}
