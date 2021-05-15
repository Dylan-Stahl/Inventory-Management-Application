package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Inventory class holds all the parts and products in the program and has methods that update the inventory.
 * @author Dylan Stahl
 */
public class Inventory {
    //Holds the parts
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    //Holds the products
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * Adds part to inventory field allParts.
     * @param newPart
     */
    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }

    /**
     * Adds product to inventory field allProducts.
     * @param newProduct
     */
    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }

    /**
     * Method returns the allParts observable array list. This array holds all the parts in the program.
     * @return
     */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    /**
     * Method returns allProducts observable array list. This array holds all the products in the program.
     * @return
     */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }

    /**
     * Method returns a part in the inventory field allParts if the argument matches an id of one of the parts. If no
     * id matches, null is returned.
     * @param partId
     * @return
     */
    public static Part lookupPart(int partId) {
        if(!allParts.isEmpty()) {
            for (Part part : Inventory.getAllParts()) {
                if (part.getId() == partId) {
                    return part;
                }
            }
        }
        return null;
    }

    /**
     * Method returns a product in the inventory field allProducts if the argument matches an id of one of the parts.
     * If no id matches, null is returned.
     * @param productId
     * @return
     */
    public static Product lookupProduct(int productId) {
       if(!allProducts.isEmpty()) {
           for (Product product : Inventory.getAllProducts()) {
               if (product.getId() == productId) {
                   System.out.println("Found product!");
                   return product;
               }
           }
       }
        System.out.println("Product Not Found");
        return null;
    }

    /**
     * Returns part based on part name. The argument is compared to each part's name. If the name matches, the part
     * is returned, if not, null is returned.
     * @param partName
     * @return
     */
    public static ObservableList<Part> lookupPart(String partName) {
        if(!allParts.isEmpty()){
            ObservableList partsSearch = FXCollections.observableArrayList();
            for(Part part : Inventory.getAllParts()){
                if(part.getName().equalsIgnoreCase(partName)){
                    return partsSearch;
                }
            }
        }
        return null;
    }

    /**
     * Returns product based on product name. The argument is compared to each product's name. If the name matches, the
     * product is returned, if not, null is returned.
     * @param productName
     * @return
     */
    public static ObservableList<Product> lookupProduct(String productName) {
        if(!allProducts.isEmpty()){
            ObservableList productsSearch = FXCollections.observableArrayList();
            for(Product product : Inventory.getAllProducts()) {
                if(product.getName().equalsIgnoreCase(productName)){
                    return productsSearch;
                }
            }
        }
        return null;
    }

    /**
     * Updates part when given index of part to be updated and also the part.
     * @param index
     * @param updatedPart
     */
    public static void updatePart(int index, Part updatedPart) {
        int i = 0;

        if (!allParts.isEmpty()) {
            for (Part part : Inventory.getAllParts()) {
                if(i == index) {
                    Inventory.getAllParts().set(i,updatedPart);
                }
                i++;
            }
        }
    }

    /**
     * Updates product when given index of product to be updated and also the product.
     * @param index
     * @param newProduct
     */
    public static void updateProduct(int index, Product newProduct) {
        int i = 0;

        if(!allProducts.isEmpty()) {
            for (Product product : Inventory.getAllProducts()) {
                if(i==index) {
                    Inventory.getAllProducts().set(i,newProduct);
                }
                i++;
            }
        }
    }

    /**
     * Used in the main menu to delete a selected part. Given that selected part, the for loops searches through the
     * getAllParts list to find the match. When it is found, the remove method is called on the getAllParts list for
     * that part.
     * @param selectedPart
     * @return
     */
    public static boolean deletePart(Part selectedPart) {
        if(!allParts.isEmpty()) {
            for(Part part : Inventory.getAllParts()) {
                if (part.getId() == selectedPart.getId()){
                    return Inventory.getAllParts().remove(part);
                }
            }
        }
        return false;
    }

    /**
     * Used in the main menu to delete a selected product. Given that selected product, the for loops searches through
     * the getAllProducts list to find the match. When it is found, the remove method is called on the getAllProducts
     * list for that part.
     * @param selectedProduct
     * @return
     */
    public static boolean deleteProduct(Product selectedProduct) {
        if(!allProducts.isEmpty()) {
            for(Product product : Inventory.getAllProducts()) {
                if(product.getId() == selectedProduct.getId()) {
                    return Inventory.getAllProducts().remove(product);
                }
            }
        }
        return false;
    }

}
