package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Random;

/**
 * Product class.
 * @author Dylan Stahl
 */
public class Product {
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    /**
     * Product constructor.
     * @param id
     * @param name
     * @param price
     * @param stock
     * @param min
     * @param max
     */
    public Product(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /**
     * Adds associated part to product.
     * @param part
     */
    public void addAssociatedPart(Part part) {
        associatedParts.add(part);
    }

    /**
     * Returns associatedParts observable array list for a product.
     * @return
     */
    public ObservableList<Part> getAllAssociatedParts() {
        return associatedParts;
    }

    /**
     * Used in the addProduct menu and the modifyProduct menu. Allows user to remove associated parts for a product.
     * @param selectedAssociatedPart
     * @return
     */
    public boolean deleteAssociatedPart(Part selectedAssociatedPart){
        if(!associatedParts.isEmpty()) {
            for(int i = 0; i < associatedParts.size(); i++)  {
                if(associatedParts.get(i).getId() == selectedAssociatedPart.getId()) {
                    associatedParts.remove(i);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Used to generate a random number for acceptableProductId().
     * @return
     */
    public static int generateRandomNum() {
        Random rand = new Random();
        int randNum = rand.nextInt(1000);
        return randNum;
    }

    /**
     * Returns a product id to generateProductId(). The method calls generateRandomNum() to find a candidate id. The
     * candidate is then compared with all product's ids to make sure no product already uses this id. If there is no
     * match, the id is used. If there is a match, the loop will find an id until it doesn't match.
     * @return
     */
    public static int acceptableProductId() {
        boolean acceptableId = true;
        int foundId = 0;

        do {
            int idCandidate = generateRandomNum();
            for (Product product : Inventory.getAllProducts()) {
                if (product.getId() == idCandidate) {
                    acceptableId = false;
                }
            }
            foundId = idCandidate;
        }while(acceptableId == false);

        return foundId;
    }

    /**
     * Generates product id.
     * @return
     */
    public static int generateProductId() {
        int id = 1;
        if(!Inventory.getAllProducts().isEmpty()){
            id = acceptableProductId();
        }
        return id;
    }

    /**
     * Getter for product id.
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * Setter for product id.
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Getter for product name.
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for product name.
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for product price.
     * @return
     */
    public double getPrice() {
        return price;
    }

    /**
     * Setter for product price.
     * @param price
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Getter for product inventory/stock.
     * @return
     */
    public int getStock() {
        return stock;
    }

    /**
     * Setter for product inventory/stock.
     * @param stock
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * Getter for product minimum inventory.
     * @return
     */
    public int getMin() {
        return min;
    }

    /**
     * Setter for product minimum inventory.
     * @param min
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * Getter for product max inventory.
     * @return
     */
    public int getMax() {
        return max;
    }

    /**
     * Setter for product max inventory.
     * @param max
     */
    public void setMax(int max) {
        this.max = max;
    }
}
