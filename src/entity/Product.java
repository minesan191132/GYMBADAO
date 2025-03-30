/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

/**
 *
 * @author trong
 */
public class Product {
    private String id;
    private String name;
    private int price;
    private String imagePath;
    
    public Product(String id, String name, int price, String imagePath) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imagePath = imagePath;
    }
    
    // Getter methods
    public String getId() { return id; }
    public String getName() { return name; }
    public int getPrice() { return price; }
    public String getImagePath() { return imagePath; }
}