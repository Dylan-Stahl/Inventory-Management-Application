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

public class addProductController {
    Stage stage;
    Parent scene;

    //Declare and initiate an observable list using createEmptyObservableList method in mainMenuController.
    private ObservableList<Part> associatedObservableList = mainMenuController.createEmptyObservableList();

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
    private TableView<Part> associatedTableView;
    @FXML
    private TableColumn<Part, Integer> partIdColAssociated;
    @FXML
    private TableColumn<Part, String> partNameColAssociated;
    @FXML
    private TableColumn<Part, Integer> inventoryLevelColAssociated;
    @FXML
    private TableColumn<Part, Double> priceCostPerUnitColAssociated;

    /**
     * Prompts user if they are sure they want to return to the main menu upon clicking cancel.
     * @param event
     * @throws IOException
     */
    @FXML
    void onActionDisplayMainMenu(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"This will clear all fields, do you want to continue?");
        Optional<ButtonType> result = alert.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK){
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/mainMenu.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /**
     * Searches both the partTableView and associatedTableView upon pressing enter using partsSearchField.
     * FUTURE ENHANCEMENT - create binary search algorithm to search parts.
     * @param event
     */
    @FXML
    void onActionSearchParts(ActionEvent event) {
        String searchField = partsSearchField.getText().trim();
        partTableView.getSelectionModel().clearSelection();
        associatedTableView.getSelectionModel().clearSelection();

        ObservableList<Part> matchedParts = FXCollections.observableArrayList();
        ObservableList<Part> associatedPartsMatch = FXCollections.observableArrayList();

        boolean match = true;
        int numOfResults = 0;

        if(searchField.length() == 0) {
            partTableView.setItems(Inventory.getAllParts());
            associatedTableView.setItems(associatedObservableList);
        }

        //Search field must have length greater than or equal to one, if not, then nothing is selected.
        else if(searchField.length() >= 1) {
            for (Part part : Inventory.getAllParts()) {
                //This if statement selects a single part based on if the id matches.
                if (String.valueOf(part.getId()).equalsIgnoreCase(searchField)) {
                    partTableView.getSelectionModel().select(part);
                    associatedTableView.getSelectionModel().select(part);
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

                for(Part associatedPart : associatedObservableList) {
                    if(part == associatedPart && match == true) {
                        associatedPartsMatch.add(part);
                    }
                }

                //Sets match to true so next product is originally set to true as the first is.
                match = true;
            }

            if (matchedParts.size() >= 1) {
                partTableView.setItems(matchedParts);
                associatedTableView.setItems(associatedPartsMatch);
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
     * If the fields have the correct data types, a product is created, else, an error message appears.
     * @param event
     * @throws IOException
     */
    @FXML
    void onActionAddProduct(ActionEvent event) throws IOException {
        try {
            int id = Product.generateProductId();
            String name = productName.getText();
            int inv = Integer.parseInt(productInv.getText());
            double price = Double.parseDouble(productPrice.getText());
            int max = Integer.parseInt(productMax.getText());
            int min = Integer.parseInt(productMin.getText());

            if(productName.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialogue");
                alert.setContentText("Enter name in text field!");
                alert.showAndWait();
            }

            if(min >= max) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialogue");
                alert.setContentText("Min value must be less than max!");
                alert.showAndWait();
            }

            if (inv > max || inv < min) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialogue");
                alert.setContentText("Inventory can't be greater than max or less than min!");
                alert.showAndWait();
            }

            if((min <= inv && inv <= max) && (min < max) && (!(productName.getText().isEmpty()))) {
                Product newProduct = new Product(id, name, price, inv, min, max);

                //The following for loop iterates through the observable list array that was created using the
                //createEmptyObservableList within the mainMenuController.
                //Every part that was added to the associated parts table in the bottom table of the addProduct view will
                //be added to the new product when the save button is clicked.
                for (Part part : associatedObservableList) {
                    newProduct.addAssociatedPart(part);
                }
                Inventory.addProduct(newProduct);

                //Returns user to main menu
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/mainMenu.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            }
        }
        catch(NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialogue");
            alert.setContentText("Enter valid values in text fields!");
            alert.showAndWait();
        }
    }

    /**
     * This method programs the add button under the parts table. It adds a part to the observable list array created.
     * Once the product is saved, every object in this observable list will be added to that product's associatedParts.
     * @param event
     */
    @FXML
    void onActionAddAssociatedPart(ActionEvent event) {
        associatedObservableList.add(partTableView.getSelectionModel().getSelectedItem());
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
            associatedObservableList.remove(associatedTableView.getSelectionModel().getSelectedItem());
        }
    }

    /**
     * Initializes the tables in the addProduct menu.
     */
    @FXML
    void initialize() {
        //Initializes partTableView with all parts using the Inventory getAllParts method.
        partTableView.setItems(Inventory.getAllParts());

        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        inventoryLevelCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        priceCostPerUnitCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        //Initializes the table with the empty observable list array, as parts are added, the array will append them and
        //display.
        associatedTableView.setItems(associatedObservableList);

        partIdColAssociated.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameColAssociated.setCellValueFactory(new PropertyValueFactory<>("name"));
        inventoryLevelColAssociated.setCellValueFactory(new PropertyValueFactory<>("stock"));
        priceCostPerUnitColAssociated.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

}
