package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.*;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.Optional;

public class mainMenuController {
    Stage stage;
    Parent scene;

    private static Product productToModify;
    private static Part partToModify;
    private static ObservableList<Part> associatedParts;

    @FXML
    private TextField searchPartsField;
    @FXML
    private TextField searchProductsField;
    @FXML
    private TableView<Part> partTableView;
    @FXML
    private TableColumn<Part, Integer> partIdCol;
    @FXML
    private TableColumn<Part, String> partNameCol;
    @FXML
    private TableColumn<Part, Integer> partInvLvlCol;
    @FXML
    private TableColumn<Part, Double> partPriceCostPerUnitCol;
    @FXML
    private TableView<Product> productTableView;
    @FXML
    private TableColumn<?, ?> productIdCol;
    @FXML
    private TableColumn<?, ?> productNameCol;
    @FXML
    private TableColumn<?, ?> productInvLvlCol;
    @FXML
    private TableColumn<?, ?> productPricePerUnitCol;

    /**
     * Displays addPart menu.
     * @param event
     * @throws IOException
     */
    @FXML
    void onActionAddPart(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/addPart.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Method is used in addProductController so that the associatedParts field in the product object can iterate
     * through each object added to this observable array list and then add it to the associatedParts field.
     * @return associatedPartsToBeAdded
     */
    public static ObservableList<Part> createEmptyObservableList() {
        ObservableList<Part> associatedPartsToBeAdded = FXCollections.observableArrayList();
        return associatedPartsToBeAdded;
    }

    /**
     * Displays the addProduct menu.
     * @param event
     * @throws IOException
     */
    @FXML
    void onActionAddProduct(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/addProduct.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Searches parts based on id or name.
     * FUTURE ENHANCEMENT - create binary search algorithm to search parts.
     * @param event
     */
    @FXML
    void onActionSearchParts(ActionEvent event) {
        String searchField = searchPartsField.getText().trim();
        partTableView.getSelectionModel().clearSelection();
        //create an observable array list and append each matched part to it
        ObservableList<Part> matchedParts = FXCollections.observableArrayList();

        boolean match = true;
        int numOfResults = 0;

        if(searchField.length() == 0) {
            partTableView.setItems(Inventory.getAllParts());
        }

        else if(searchField.length() >= 1) {
            for (Part part : Inventory.getAllParts()) {
                if (String.valueOf(part.getId()).equalsIgnoreCase(searchField)) {
                    partTableView.getSelectionModel().select(part);
                    numOfResults++;
                }

                int searchFieldSize = searchField.length();

                if((part.getName().length() >= searchFieldSize)) {
                    for (int i = 0; i < searchFieldSize && i < part.getName().length(); i++) {
                        char partChar = part.getName().charAt(i);
                        char searchFieldChar = searchField.charAt(i);
                        char partCharUpper = Character.toUpperCase(partChar);
                        char searchFieldCharUpper = Character.toUpperCase(searchFieldChar);

                        if (partCharUpper == searchFieldCharUpper) {
                            match = true;
                        } else if (partCharUpper != searchFieldCharUpper) {
                            match = false;
                            break;
                        }
                    }
                }
                else if(part.getName().length() < searchFieldSize) {
                    match = false;
                }
                if (match == true) {
                    numOfResults = numOfResults + 1;
                    matchedParts.add(part);
                }
                //Sets match to true so next product is originally set to true as the first is.
                match = true;
            }

            if (matchedParts.size() == 1) {
                partTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
                for (Part part : matchedParts) {
                    partTableView.getSelectionModel().select(part);
                }
            } else if (matchedParts.size() > 1) {
                partTableView.setItems(matchedParts);
            }
        }
        if(numOfResults == 0 && searchField.length() >= 1) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialogue");
            alert.setContentText("No part found!");
            alert.showAndWait();
        }

    }

    /**
     * Searches products based on id or name.
     * FUTURE ENHANCEMENT - create binary search algorithm to search products.
     * @param event
     */
    @FXML
    void onActionSearchProducts(ActionEvent event) {
        String searchField = searchProductsField.getText().trim();
        productTableView.getSelectionModel().clearSelection();
        //create an observable array list and append each matched product to it
        ObservableList<Product> matchedProducts = FXCollections.observableArrayList();

        boolean match = true;
        int numOfResults = 0;

        if(searchField.length() == 0) {
            productTableView.setItems(Inventory.getAllProducts());
        }

        else if(searchField.length() >= 1) {
            for (Product product : Inventory.getAllProducts()) {
                if (String.valueOf(product.getId()).equalsIgnoreCase(searchField)) {
                    productTableView.getSelectionModel().select(product);
                    numOfResults++;
                }

                int searchFieldSize = searchField.length();

                if((product.getName().length() >= searchFieldSize)) {
                    for (int i = 0; i < searchFieldSize && i < product.getName().length(); i++) {
                        char productChar = product.getName().charAt(i);
                        char searchFieldChar = searchField.charAt(i);
                        char productCharUpper = Character.toUpperCase(productChar);
                        char searchFieldCharUpper = Character.toUpperCase(searchFieldChar);

                        if (productCharUpper == searchFieldCharUpper) {
                            match = true;
                        } else if (productCharUpper != searchFieldCharUpper) {
                            match = false;
                            break;
                        }
                    }
                }
                else if(product.getName().length() < searchFieldSize) {
                    match = false;
                }

                if (match == true) {
                    numOfResults = numOfResults + 1;
                    matchedProducts.add(product);
                }
                //Sets match to true so next product is originally set to true as the first is.
                match = true;
            }

            if (matchedProducts.size() == 1) {
                productTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
                for (Product product : matchedProducts) {
                    productTableView.getSelectionModel().select(product);
                }
            } else if (matchedProducts.size() > 1) {
                productTableView.setItems(matchedProducts);
            }
        }
        if(numOfResults == 0 && searchField.length() >= 1) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialogue");
            alert.setContentText("No product found!");
            alert.showAndWait();
        }
    }


    /**
     * Deletes selected part from Inventory. If all parts are deleted, error message will tell user "No part to delete!"
     * If the user does not select a part to delete, the error message "Select a product!" will be displayed.
     * @param event
     */
    @FXML
    void onActionDeletePart(ActionEvent event) {
        try {
            if(partTableView.getSelectionModel().getSelectedItem() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialogue");
                alert.setContentText("No part to delete!");
                alert.showAndWait();
            }
            else {
                //Prompts user if they are sure they want to delete the selected part.
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure you want to delete the selected part?");
                alert.setTitle("Deleting Part!");

                Optional<ButtonType> result = alert.showAndWait();
                if(result.isPresent() && result.get() == ButtonType.OK) {
                    Inventory.deletePart(partTableView.getSelectionModel().getSelectedItem());
                }
            }
        }catch(NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialogue");
            alert.setContentText("Select a product!");
            alert.showAndWait();
        }
    }

    /**
     * Deletes selected product from Inventory. If all products are deleted, error message will tell user "No product to
     * delete!" If the user does not select a product to delete, the error message "Select a product!" will be displayed.
     * @param event
     */
    @FXML
    void onActionDeleteProduct(ActionEvent event) {
        try {
            if(productTableView.getSelectionModel().getSelectedItem() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialogue");
                alert.setContentText("No product to delete!");
                alert.showAndWait();
            }
            else {
                Product productToDelete = productTableView.getSelectionModel().getSelectedItem();
                //Tells user they are unable to delete a product with a part associated to that product.
                if(!(productToDelete.getAllAssociatedParts().isEmpty())) {
                    Alert alert1 = new Alert(Alert.AlertType.ERROR);
                    alert1.setTitle("Product Associated with Part!");
                    alert1.setContentText("Can't delete a product with a part associated with it!");
                    alert1.showAndWait();
                }
                else {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure you want to delete the selected product?");
                    alert.setTitle("Deleting Product!");
                    Optional<ButtonType> result = alert.showAndWait();
                    if(result.isPresent() && result.get() == ButtonType.OK) {
                        Inventory.deleteProduct(productToDelete);
                    }
                }
            }

        }catch(NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialogue");
            alert.setContentText("Select a product!");
            alert.showAndWait();
        }
    }

    /**
     * Confirms with the user that they would like to close the application. If user selects ok, application is exited.
     * @param event
     */
    @FXML
    void onActionExit(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure you want to exit the Inventory Management System?");
        alert.setTitle("Exiting Program");

        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            System.exit(0);
        }
    }

    @FXML
    void onActionModifyPart(ActionEvent event) throws IOException {
        partToModify = partTableView.getSelectionModel().getSelectedItem();
        if(partToModify != null) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/modifyPart.fxml"));
            loader.load();
            modifyPartController modifyPartC = loader.getController();
            modifyPartC.sendPart(partToModify);

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
            }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialogue");
            alert.setContentText("Select a part!");
            alert.showAndWait();
        }
    }

    /**
     * Displays modifyProduct menu with values of the product already in the text fields.
     * @param event
     * @throws IOException
     */
    @FXML
    void onActionModifyProduct(ActionEvent event) throws IOException {
        try {
            //Following statement is used by the sendProduct method in modifyProductController to determine what
            //should be displayed in the text fields on the modifyProduct Menu.
            productToModify = productTableView.getSelectionModel().getSelectedItem();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/modifyProduct.fxml"));
            loader.load();

            modifyProductController modifyProductC = loader.getController();
            modifyProductC.sendProduct(productToModify);

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
        catch(NullPointerException e) {
            //Catch needed here but not used because catch in modifyProductController initialize method is used if
            //product isn't selected.
        }
    }

    /**
     * Used by modifyProductController to initialize associated parts table in the modifyProduct menu.
     * @return
     */
    public static Product determineWhichProduct() {
        return productToModify;
    }

    /**
     * Sets up tables in the mainMenu with Inventory methods to determine which objects should be displayed.
     */
    @FXML
    public void initialize() {
        //Setting up product table view, tells what objects will be working with (Part)
        partTableView.setItems(Inventory.getAllParts());

        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvLvlCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCostPerUnitCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        //Setting up product table view, tells what objects will be working with (Product)
        productTableView.setItems(Inventory.getAllProducts());

        productIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInvLvlCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPricePerUnitCol.setCellValueFactory(new PropertyValueFactory<>("price"));

    }

}
