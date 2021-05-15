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
import model.Inventory;
import model.Part;
import model.Product;

import java.io.IOException;
import java.util.Optional;

public class modifyProductController {
    Stage stage;
    Parent scene;

    @FXML
    private TextField partsSearchField;
    @FXML
    private TextField productId;
    @FXML
    private TextField productName;
    @FXML
    private TextField productInv;
    @FXML
    private TextField productPrice;
    @FXML
    private TextField productMax;
    @FXML
    private TextField productMin;
    @FXML
    private TableView<Part> partTableView;
    @FXML
    private TableColumn<Part, Integer> partIdCol;
    @FXML
    private TableColumn<Part, String> partNameCol;
    @FXML
    private TableColumn<Part, Integer> inventoryLevelCol;
    @FXML
    private TableColumn<Part, Double> priceCostPerUnitCol;
    @FXML
    private TableView<Part> partColAssociated;
    @FXML
    private TableColumn<Part, Integer> partIdColAssociated;
    @FXML
    private TableColumn<Part, String> partNameAssociated;
    @FXML
    private TableColumn<Part, Integer> inventoryLevelAssociated;
    @FXML
    private TableColumn<Part, Double> priceCostPerUnitAssociated;

    /**
     * If the cancel button is clicked, this method asks the user if they would like to return to the main menu.
     * @param event
     * @throws IOException
     */
    @FXML
    void onActionDisplayMainMenu(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"The information inputted will not be saved, are you "
                + "sure you would like to return to the main menu?");
        Optional<ButtonType> result = alert.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK) {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/mainMenu.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /**
     * mainMenuController uses this method to populate the text field with the product's data.
     * @param product
     */
    public void sendProduct(Product product) {
        productId.setText(String.valueOf(product.getId()));
        productName.setText(product.getName());
        productInv.setText(String.valueOf(product.getStock()));
        productPrice.setText(String.valueOf(product.getPrice()));
        productMax.setText(String.valueOf(product.getMax()));
        productMin.setText(String.valueOf(product.getMin()));

    }

    /**
     * Searches parts based on id or name.
     * @param event
     */
    @FXML
    void onActionSearchParts(ActionEvent event) {
        String searchField = partsSearchField.getText().trim();
        partTableView.getSelectionModel().clearSelection();
        partColAssociated.getSelectionModel().clearSelection();

        ObservableList<Part> matchedParts = FXCollections.observableArrayList();
        ObservableList<Part> associatedPartsMatch = FXCollections.observableArrayList();

        boolean match = true;
        int numOfResults = 0;

        if(searchField.length() == 0) {
            partTableView.setItems(Inventory.getAllParts());
            partColAssociated.setItems(mainMenuController.determineWhichProduct().getAllAssociatedParts());
        }

        //Search field must have length greater than or equal to one, if not, then nothing is selected.
        else if(searchField.length() >= 1) {
            for (Part part : Inventory.getAllParts()) {
                //This if statement selects a single part based on if the id matches.
                if (String.valueOf(part.getId()).equalsIgnoreCase(searchField)) {
                    partTableView.getSelectionModel().select(part);
                    partColAssociated.getSelectionModel().select(part);
                    numOfResults++;
                }

                int searchFieldSize = searchField.length();

                //This if statement will determine if the search field matches the part currently in the loop.
                if((part.getName().length() >= searchFieldSize)) {
                    for (int i = 0; i < searchFieldSize && i < part.getName().length(); i++) {
                        char partChar = part.getName().charAt(i);
                        char searchFieldChar = searchField.charAt(i);
                        char partCharUpper = Character.toUpperCase(partChar);
                        char searchFieldCharUpper = Character.toUpperCase(searchFieldChar);

                        if (partCharUpper == searchFieldCharUpper) {
                            match = true;
                        } else if (partCharUpper != searchFieldCharUpper) {
                            //If a character doesn't match the search field, the loop breaks and match is assigned false.
                            match = false;
                            break;
                        }
                    }
                }
                //If the search field text is longer than the current iteration's part's name, then the part does not match.
                else if(part.getName().length() < searchFieldSize) {
                    match = false;
                }
                if (match == true) {
                    numOfResults = numOfResults + 1;
                    matchedParts.add(part);
                }

                Product associatedProduct = mainMenuController.determineWhichProduct();
                for(Part associatedPart: associatedProduct.getAllAssociatedParts()) {
                    if (associatedPart == part && match == true){
                        associatedPartsMatch.add(part);
                    }
                }

                //Sets match to true so next product is originally set to true as the first is.
                match = true;
            }

            if (matchedParts.size() >= 1) {
                partTableView.setItems(matchedParts);
                partColAssociated.setItems(associatedPartsMatch);
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
     * This method programs the add button under the parts table. It adds a part to the observable list array created.
     * Once the product is saved, every object in this observable list will be added to that product's associatedParts.
     * RUNTIME ERROR - Fixed bug where the program allowed a user to add an associated part even if nothing was
     * selected. It would allow the user to add null parts to the associatedParts list. I fixed this by using the if
     * statement in the method which tests to see if a part is selected first.
     * @param event
     */
    @FXML
    void onActionAddAssociatedPart(ActionEvent event) {
        Product product = mainMenuController.determineWhichProduct();
        if(!(partTableView.getSelectionModel().getSelectedItem() == null)) {
            Part partToAssociate = partTableView.getSelectionModel().getSelectedItem();
            product.addAssociatedPart(partToAssociate);
        }
    }

    /**
     * Method removes parts added to the associated parts table. If the user selects ok, the part will dissociated.
     * @param event
     */
    @FXML
    void onActionRemoveAssociatedPart(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure you want to delete this part from the" +
                " product's associated part list?");
        alert.setTitle("Deleting Product!");

        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            Product product = mainMenuController.determineWhichProduct();
            Part partToRemove = partColAssociated.getSelectionModel().getSelectedItem();
            product.deleteAssociatedPart(partToRemove);
        }
    }

    /**
     * Method gets the strings from the text fields, converts them to appropriate data types as needed, and then
     * determines if the data is inputted incorrectly. These alerts are as follows: minimum
     * inventory must be less than max, name field can't be empty, and the current inventory for the part must be
     * less than or equal to max and greater than or equal to minimum.
     * @param event
     * @throws IOException
     */
    @FXML
    void onActionSaveModifiedPart(ActionEvent event) throws IOException {
        try {
            String pName = productName.getText();
            int pInv = Integer.parseInt(productInv.getText());
            double pPrice = Double.parseDouble(productPrice.getText());
            int pMax = Integer.parseInt(productMax.getText());
            int pMin = Integer.parseInt(productMin.getText());

            if(productName.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialogue");
                alert.setContentText("Enter name in text field!");
                alert.showAndWait();
            }
            if(pMin >= pMax) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialogue");
                alert.setContentText("Min value must be less than max!");
                alert.showAndWait();
            }
            if (pInv > pMax || pInv < pMin) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialogue");
                alert.setContentText("Inventory can't be greater than max or less than min!");
                alert.showAndWait();
            }

            if((pMin <= pInv && pInv <= pMax) && (pMin < pMax) && (!(productName.getText().isEmpty()))) {
                Product productToModify = mainMenuController.determineWhichProduct();
                productToModify.setName(pName);
                productToModify.setStock(pInv);
                productToModify.setPrice(pPrice);
                productToModify.setMax(pMax);
                productToModify.setMin(pMin);

                //returns user to main menu.
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/mainMenu.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialogue");
            alert.setContentText("Enter valid values in text fields!");
            alert.showAndWait();
        }
    }

    /**
     * Initialize populates the tables in the modifyProduct menu. If a product isn't selected in the main menu, the
     * catch exception will tell the user to select a product.
     */
    @FXML
    void initialize() {
        partTableView.setItems(Inventory.getAllParts());

        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        inventoryLevelCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        priceCostPerUnitCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        try {
            //This try block populates the associated parts table underneath the part table view. To do this, the
            //product must be known to access the associatedParts. This is done by using the determineWhichProduct
            //method in the main menu controller. Then that is saved to productAssociated and the associatedParts
            //can be accessed with the getAllAssociatedParts method.
            Product productAssociated = mainMenuController.determineWhichProduct();
            partColAssociated.setItems(productAssociated.getAllAssociatedParts());

            partColAssociated.setItems(productAssociated.getAllAssociatedParts());
            partIdColAssociated.setCellValueFactory(new PropertyValueFactory<>("id"));
            partNameAssociated.setCellValueFactory(new PropertyValueFactory<>("name"));
            inventoryLevelAssociated.setCellValueFactory(new PropertyValueFactory<>("stock"));
            priceCostPerUnitAssociated.setCellValueFactory(new PropertyValueFactory<>("price"));
        } catch(NullPointerException e) {
            System.out.println("Catch");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialogue");
            alert.setContentText("Select a product!");
            alert.showAndWait();
        }
    }

}
