package model;

public class Product {
    private int id;
    private String name;
    private String description;
    private double price;
    private int quantity;
    private boolean status; // true = Available, false = Unavailable

    public Product(int id, String name, String description, double price, int quantity, boolean status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.status = status;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public boolean getStatus() { return status; }

    public void setStatus(boolean status) { this.status = status; }
}
