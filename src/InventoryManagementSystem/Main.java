package InventoryManagementSystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.*;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../view/mainMenu.fxml"));
        primaryStage.setTitle("Inventory Management System");
        primaryStage.setScene(new Scene(root, 850, 400));
        primaryStage.show();
    }

/** Main Method
 *  Adds initial data to the program.
 *  JavaDoc located in src folder.
 * @author Dylan Stahl
 */
    public static void main(String[] args) {

        // Adding products and parts to the program so that when the program starts there is pre populated data.
        Part Brakes = new InHouse(1,"Brakes",12.99,15,1,50,4);
        Part Tire = new InHouse(2,"Tire",14.99,15,1,50,3);
        Part Rim = new OutSourced(3,"Rim",56.99,15,1,50,"Jake's");
        Product GiantBike = new Product(1,"Giant Bicycle",299.99,15,1,50);
        Product ScottBike = new Product(2,"Scott Bicycle",199.99,15,1,50);
        Product GTBike = new Product(3,"GT Bike",99.99,15,1,50);

        Inventory.addPart((InHouse)Brakes);
        Inventory.addPart((InHouse)Tire);
        Inventory.addPart((OutSourced)Rim);

        Inventory.addProduct(GiantBike);
        Inventory.addProduct(ScottBike);
        Inventory.addProduct(GTBike);


        launch(args);


    }
}
