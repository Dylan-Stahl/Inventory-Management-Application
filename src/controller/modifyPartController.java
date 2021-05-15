package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.OutSourced;
import model.Part;

import java.io.IOException;
import java.util.Optional;

public class modifyPartController {
    Stage stage;
    Parent scene;

    @FXML
    private Label changeMe;
    @FXML
    private RadioButton inHouseRBtn;
    @FXML
    private ToggleGroup whichPartGroup;
    @FXML
    private RadioButton outsourcedRBtn;
    @FXML
    private TextField partId;
    @FXML
    private TextField partName;
    @FXML
    private TextField partInv;
    @FXML
    private TextField partPriceCost;
    @FXML
    private TextField partMax;
    @FXML
    private TextField partMachineId;
    @FXML
    private TextField partMin;

    /**
     * mainMenuController uses this method to populate the text field with the part's data.
     * @param part
     */
    public void sendPart(Part part) {
        if(part instanceof  InHouse) {
            inHouseRBtn.setSelected(true);
            partId.setText(String.valueOf(part.getId()));
            changeMe.setText("Machine ID");
            partName.setText(part.getName());
            partInv.setText(String.valueOf(part.getStock()));
            partPriceCost.setText(String.valueOf(part.getPrice()));
            partMax.setText(String.valueOf(part.getMax()));
            partMachineId.setText(String.valueOf(((InHouse) part).getMachineId()));
            partMin.setText(String.valueOf(part.getMin()));
        }
        else if(part instanceof OutSourced) {
            outsourcedRBtn.setSelected(true);
            partId.setText(String.valueOf(part.getId()));
            changeMe.setText("Company Name");
            partName.setText(part.getName());
            partInv.setText(String.valueOf(part.getStock()));
            partPriceCost.setText(String.valueOf(part.getPrice()));
            partMax.setText(String.valueOf(part.getMax()));
            partMachineId.setText(((OutSourced) part).getCompanyName());
            partMin.setText(String.valueOf(part.getMin()));
        }
    }

    /**
     * The first set of if and else if statements determine if the part is outsourced or in house and what type of part
     * the user wants to change it too. Based on that, the method will appropriately update the inventory. Alerts of
     * type error are used to notify the user if they incorrectly inputted data. These alerts are as follows: minimum
     * inventory must be less than max, name field can't be empty, and the current inventory for the part must be
     * less than or equal to max and greater than or equal to minimum.
     * RUNTIME ERROR - Fixed error where when the part is changed from InHouse to OutSourced and vice versa the part id
     * is regenerated. Now it will retain the original id.
     * @param event
     * @throws IOException
     */
    @FXML
    void onActionUpdatePart(ActionEvent event) throws IOException {
        try {
            Part partToUpdate = determineWhichPart();

            if (partToUpdate instanceof InHouse && inHouseRBtn.isSelected()) {
                String name = partName.getText();
                int stock = Integer.parseInt(partInv.getText());
                double price = Double.parseDouble(partPriceCost.getText());
                int max = Integer.parseInt(partMax.getText());
                int min = Integer.parseInt(partMin.getText());
                int machineId = Integer.parseInt(partMachineId.getText());

                if(name.isEmpty()) {
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

                if (stock > max || stock < min) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Dialogue");
                    alert.setContentText("Inventory can't be greater than max or less than min!");
                    alert.showAndWait();
                }

                if((min <= stock && stock <= max) && (min < max) && (!(partName.getText().isEmpty()))) {
                    partToUpdate.setName(name);
                    partToUpdate.setStock(stock);
                    partToUpdate.setPrice(price);
                    partToUpdate.setMax(max);
                    partToUpdate.setMin(min);
                    ((InHouse) (partToUpdate)).setMachineId(machineId);

                    stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                    scene = FXMLLoader.load(getClass().getResource("/view/mainMenu.fxml"));
                    stage.setScene(new Scene(scene));
                    stage.show();
                }
            } else if (partToUpdate instanceof OutSourced && outsourcedRBtn.isSelected()) {
                String name = partName.getText();
                int stock = Integer.parseInt(partInv.getText());
                double price = Double.parseDouble(partPriceCost.getText());
                int max = Integer.parseInt(partMax.getText());
                int min = Integer.parseInt(partMin.getText());
                String companyName = partMachineId.getText();


                if(name.isEmpty()) {
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

                if (stock > max || stock < min) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Dialogue");
                    alert.setContentText("Inventory can't be greater than max or less than min!");
                    alert.showAndWait();
                }

                if((min <= stock && stock <= max) && (min < max) && (!(partName.getText().isEmpty()))) {
                    partToUpdate.setName(name);
                    partToUpdate.setStock(stock);
                    partToUpdate.setPrice(price);
                    partToUpdate.setMax(max);
                    partToUpdate.setMin(min);
                    ((OutSourced) (partToUpdate)).setCompanyName(companyName);

                    stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                    scene = FXMLLoader.load(getClass().getResource("/view/mainMenu.fxml"));
                    stage.setScene(new Scene(scene));
                    stage.show();
                }
            } else if (partToUpdate instanceof InHouse && outsourcedRBtn.isSelected()) {
                int id = Integer.parseInt(partId.getText());
                String name = partName.getText();
                int stock = Integer.parseInt(partInv.getText());
                double price = Double.parseDouble(partPriceCost.getText());
                int max = Integer.parseInt(partMax.getText());
                int min = Integer.parseInt(partMin.getText());
                String companyName = partMachineId.getText();

                if(name.isEmpty()) {
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

                if (stock > max || stock < min) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Dialogue");
                    alert.setContentText("Inventory can't be greater than max or less than min!");
                    alert.showAndWait();
                }

                if((min <= stock && stock <= max) && (min < max) && (!(partName.getText().isEmpty()))) {
                    //Program deletes the InHouse object and created an OutSourced one.
                    Inventory.deletePart(partToUpdate);
                    Part newPart = new OutSourced(id, name, price, stock, min, max, companyName);
                    Inventory.addPart(newPart);

                    stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                    scene = FXMLLoader.load(getClass().getResource("/view/mainMenu.fxml"));
                    stage.setScene(new Scene(scene));
                    stage.show();
                }
            } else if (partToUpdate instanceof OutSourced && inHouseRBtn.isSelected()) {
                int id = Integer.parseInt(partId.getText());
                String name = partName.getText();
                int stock = Integer.parseInt(partInv.getText());
                double price = Double.parseDouble(partPriceCost.getText());
                int max = Integer.parseInt(partMax.getText());
                int min = Integer.parseInt(partMin.getText());
                int machineId = Integer.parseInt(partMachineId.getText());

                if(name.isEmpty()) {
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

                if (stock > max || stock < min) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Dialogue");
                    alert.setContentText("Inventory can't be greater than max or less than min!");
                    alert.showAndWait();
                }

                if((min <= stock && stock <= max) && (min < max) && (!(partName.getText().isEmpty()))) {
                    //Program deletes OutSourced object and created an InHouse one with updated data.
                    Part newPart = new InHouse(id, name, price, stock, min, max, machineId);
                    Inventory.addPart(newPart);
                    Inventory.deletePart(partToUpdate);
                    stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                    scene = FXMLLoader.load(getClass().getResource("/view/mainMenu.fxml"));
                    stage.setScene(new Scene(scene));
                    stage.show();
                }
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialogue");
            alert.setContentText("Enter valid values in text fields!");
            alert.showAndWait();
        }
    }

    /**
     * Finds the part to update for the onActionUpdatePart based on id in the text field.
     * @return
     */
    public Part determineWhichPart() {
        for(Part part : Inventory.getAllParts()) {
            if (Integer.parseInt(partId.getText()) == (part.getId())) {
                return part;
            }
        }
        return null;
    }

    /**
     * Updates label for modifyPartMenu if the In-House radiobutton is clicked.
     * @param event
     */
    @FXML
    void onActionDisplayInHouse(ActionEvent event) {
        changeMe.setText("Machine ID");
    }

    /**
     * If the cancel button is clicked, this method asks the user if they would like to return to the main menu.
     * @param event
     * @throws IOException
     */
    @FXML
    void onActionDisplayMainMenu(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "The information inputted will not be saved, are you "
        + "sure you would like to return to the main menu?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/mainMenu.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /**
     * Updates the label to Company Name if the Outsourced radiobutton is clicked.
     * @param event
     */
    @FXML
    void onActionDisplayOutSourced(ActionEvent event) {
        changeMe.setText("Company Name");
    }
}
