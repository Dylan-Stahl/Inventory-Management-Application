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
import java.util.Random;

public class addPartController {
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
     * Changes company name text field to machine id when in houseradio button is clicked.
     * @param event
     */
    @FXML
    void onActionDisplayInHouse(ActionEvent event) {
        changeMe.setText("Machine ID");
    }

    /**
     * Changes machine id text field to company name when outsourced radio button is clicked.
     * @param event
     */
    @FXML
    void onActionDisplayOutSourced(ActionEvent event) {

        changeMe.setText("Company Name");
    }

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

    //I will create a method that generates the id and will then call Part.setId to set it

    /**
     * Method is used by acceptablePartId to find a random number up to 1000.
     * @return
     */
    public static int generateRandomNum() {
        Random rand = new Random();
        int randNum = rand.nextInt(1000);
        return randNum;
    }

    /**
     * Calls generateRandomNum to create a random number. If no other parts in the inventory have the same id, the id is
     * used for the part. If the generated random number happens to match an existing part's id, then generateRandomNum
     * will be called until the generated id does not match an existing part's id.
     * @return
     */
    public static int acceptablePartId() {
        boolean acceptableId = true;
        int foundId = 0;

        do {
            int idCandidate = generateRandomNum();
            for (Part part : Inventory.getAllParts()) {
                if (part.getId() == idCandidate) {
                    acceptableId = false;
                }
            }
            foundId = idCandidate;
        }while(acceptableId == false);

        return foundId;
    }

    /**
     * Generates part id using the acceptablePartId method which uses the generateRandomNum method.
     * @return the generated id
     */
    public static int generatePartId() {
        int id = 1;
        if(!Inventory.getAllParts().isEmpty()) {
            id = acceptablePartId();
        }
        return id;
    }

    /**
     * Creates part object when save is clicked using text field data.
     * @param event
     * @throws IOException
     */
    @FXML
    void onActionSavePart(ActionEvent event) throws IOException {
        //Creates part object based on radiobutton selection
        //Error dialogue tells user if invalid entries are typed
        try {
            if (inHouseRBtn.isSelected()) {
                int id = generatePartId();
                String name = partName.getText();
                int stock = Integer.parseInt(partInv.getText());
                double price = Double.parseDouble(partPriceCost.getText());
                int max = Integer.parseInt(partMax.getText());
                int min = Integer.parseInt(partMin.getText());
                int machineId = Integer.parseInt(partMachineId.getText());

                if(partName.getText().isEmpty()) {
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

                //This last if statement verifies if every criteria is met, if so, the part is added to the inventory.
                if((min <= stock && stock <= max) && (min < max) && (!(partName.getText().isEmpty()))) {
                    Inventory.addPart(new InHouse( id,  name,  price,  stock,  min,  max,  machineId));

                    stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                    scene = FXMLLoader.load(getClass().getResource("/view/mainMenu.fxml"));
                    stage.setScene(new Scene(scene));
                    stage.show();
                }
            }
            else if(outsourcedRBtn.isSelected()){
                int id = generatePartId();
                String name = partName.getText();
                int stock = Integer.parseInt(partInv.getText());
                double price = Double.parseDouble(partPriceCost.getText());
                int max = Integer.parseInt(partMax.getText());
                int min = Integer.parseInt(partMin.getText());
                String companyName = partMachineId.getText();

                if(partName.getText().isEmpty()) {
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

                //This last if statement verifies if every criteria is met, if so, the part is added to the inventory.
                if((min <= stock && stock <= max) && (min < max) && (!(partName.getText().isEmpty()))) {
                    Inventory.addPart(new OutSourced( id,  name,  price,  stock,  min,  max,  companyName));

                    stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                    scene = FXMLLoader.load(getClass().getResource("/view/mainMenu.fxml"));
                    stage.setScene(new Scene(scene));
                    stage.show();
                }
            }
        }
        catch(NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialogue");
            alert.setContentText("Enter valid values in text fields!");
            alert.showAndWait();
        }


    }

}
