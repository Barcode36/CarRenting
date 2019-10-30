import javafx.collections.*;
import java.lang.*;

import com.mysql.cj.util.*;

import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;


public class EmployerInterface {
    
    
    private final static int    TITLE_SIZE = 30,
                                SECTION_SIZE = 9, 
                                DX_PANEL_SPACE = 3; 
//	------ LABELS ------
    private final Label title;
    private final Text errorMsg;
    private final Label insertTitle;
    final Label licensePlate,vendor,seatNumber,location,kilometers,price;
    private final Label feedbackTitle;
    private final Label userTitle;
    private final Label carTitle;
//	------ TEXT FIELDS ------
    private final TextField fieldLicensePlate; 
    private final TextField fieldVendor; 
    private final TextField fieldKm; 
    private final TextField fieldPrice;
//	------ COMBO BOXES ------
    private final ComboBox fieldLocation;
    private final ComboBox fieldSeats;
    private final ComboBox<String> tableChoose;
//	------ TABLES ------
    private final VisualTableFeedback tableFeedback;
    private final VisualTableUser tableUser;
    private final VisualTableCar tableCar;
//	------ BUTTONS ------
    final Button insertButton;
    final Button deleteButton;
    final Button logOutButton;
//	------ BOXES ------
    private final VBox insertPanel;
    private final VBox userPanel;
    private final VBox carPanel;
    private final VBox feedbackPanel;
    private final AnchorPane box;
    
    private int table = 1; //1-> Car Manager, 2 -> User Table, 3 -> Feedback Table
     
    public EmployerInterface() {
//    	------ LABELS ------
        title = new Label("Employer Management");
        errorMsg = new Text();
        insertTitle = new Label("Insert a new car");
        licensePlate = new Label("License Plate:");
        vendor = new Label("Vendor:");
        location = new Label("Location:");
        kilometers = new Label("Kilometers:");
        price = new Label("Price:");
        seatNumber = new Label("Seat Number:");
        feedbackTitle = new Label("FEEDBACKS");
        userTitle = new Label("USERS");
        carTitle = new Label("CARS");
//    	------ TEXT FIELDS ------
        fieldLicensePlate = new TextField();
        fieldVendor = new TextField();
        fieldKm = new TextField();
        fieldPrice = new TextField();
//		------ COMBO BOXES ------
        ObservableList<String> loc = 
                FXCollections.observableArrayList (
                    "Firenze","Pisa");
        fieldLocation = new ComboBox(loc);
        fieldLocation.setValue("Firenze");
        ObservableList<String> seats = 
                FXCollections.observableArrayList (
                    "2","4","5","6");
        fieldSeats = new ComboBox(seats);
        fieldSeats.setValue("4");
        ObservableList<String>  choose = //ComboBox per la scelta della tabella da visualizzare
        		FXCollections.observableArrayList (
        				"Car Manager", "User Table", "Feedback Table");
        tableChoose = new ComboBox(choose);
        tableChoose.setValue("Car Manager"); //tabella CAR editabile
//    	------ TABLES ------
        tableFeedback = new VisualTableFeedback();
        tableUser = new VisualTableUser(); //tabella per visualizzare gli users
        tableCar = new VisualTableCar(false);
//    	------ BUTTONS ------
        insertButton = new Button("INSERT");
        deleteButton = new Button("DELETE");
        logOutButton = new Button("LOG OUT");
//    	------ BOXES ------
        insertPanel = new VBox(3);
        insertPanel.getChildren().addAll(insertTitle, errorMsg,licensePlate,fieldLicensePlate,vendor,
                fieldVendor,seatNumber,fieldSeats, location,
                fieldLocation,kilometers, fieldKm, price, 
                fieldPrice, insertButton, deleteButton);
        feedbackPanel = new VBox(DX_PANEL_SPACE);
        feedbackPanel.getChildren().addAll(feedbackTitle, tableFeedback);
        userPanel = new VBox(DX_PANEL_SPACE); 
        userPanel.getChildren().addAll(userTitle, tableUser);
        carPanel = new VBox(DX_PANEL_SPACE);
        carPanel.getChildren().addAll(carTitle, tableCar, deleteButton);
        box = new AnchorPane();
        box.getChildren().addAll(logOutButton,title, insertPanel, tableChoose, carPanel);
    }
    
    public void setEmpInterfaceStyle() { 
//    	------ LABELS ------
        title.setFont(Font.font("Calibri", 11 + TITLE_SIZE)); 
        title.setLayoutX(340);
        title.setLayoutY(10);
        insertTitle.setFont(Font.font("Calibri", 16));
        errorMsg.setFont(Font.font("Calibri", 16));
        errorMsg.setFill(Color.RED);
//    	------ TABLES ------
        tableFeedback.setTableFeedbackStyle();
        tableUser.setTableUserStyle(); //tabella User
        tableCar.setTableCarStyle();
//    	------ BOXES ------
        insertPanel.setLayoutX(60);
        insertPanel.setLayoutY(100);
        insertPanel.setPrefSize(300,450);
        insertPanel.setAlignment(Pos.CENTER);
        
        feedbackPanel.setLayoutX(400);
        feedbackPanel.setLayoutY(100);
        feedbackPanel.setPrefSize(500,450);
        feedbackPanel.setAlignment(Pos.CENTER);
        feedbackPanel.fillWidthProperty();
        
        userPanel.setLayoutX(400);
        userPanel.setLayoutY(100);
        userPanel.setPrefSize(500,450);
        userPanel.setAlignment(Pos.CENTER);
        userPanel.fillWidthProperty();
        
        carPanel.setLayoutX(400);
        carPanel.setLayoutY(100);
        carPanel.setPrefSize(500,450);
        carPanel.setAlignment(Pos.CENTER);
        carPanel.fillWidthProperty();
        
        box.setStyle("-fx-background-color: khaki"); 
        box.setPadding(new Insets(0,5,0,0));
        box.setPrefWidth(890);
        box.setPrefHeight(660);
        box.setTopAnchor(insertPanel,129.0);
        /*box.setLeftAnchor(title, 90.0);
        box.setTopAnchor(title, 30.0);
        box.setLeftAnchor(logOutButton,430.0);
        box.setTopAnchor(logOutButton,5.0);
        box.setBottomAnchor(deleteButton, 70.0);
        box.setBottomAnchor(insertButton, 50.0);*/
    }
    
    // listen the events 
    void empEventHandler(RentHandler rh, CarRenting carR){ 
    	// "initialization" phase
    	tableFeedback.ListFeedbackUpdate(rh.showFeedbacks());
    	tableUser.UserListUpdate(rh.showCustomers());
    	
    	// logout
        logOutButton.setOnAction((ActionEvent e)-> {
            errorMsg.setText("");
            clearAll();
            carR.setScene("logout");
        });
        
        tableChoose.setOnAction((ActionEvent ev)-> { 
        	if(tableChoose.getValue() == "User Table") {
                if(table == 1) {	
                	box.getChildren().removeAll(carPanel);
                }
                else if(table == 3) { 
                	box.getChildren().removeAll(feedbackPanel);
                }
                table = 2;
                box.getChildren().addAll(userPanel); 
        	}
        	
        	else if(tableChoose.getValue() == "Car Manager") {
        		if(table == 2) {
        			box.getChildren().removeAll(userPanel);
        		}
        		else if(table == 3) {
        			box.getChildren().removeAll(feedbackPanel);
        		}
        		table = 1;
        		box.getChildren().addAll(carPanel);
        	}
        	
        	else if(tableChoose.getValue() == "Feedback Table") {
        		if(table == 1) {
        			box.getChildren().removeAll(carPanel);
        		}
        		else if(table == 2) {
        			box.getChildren().removeAll(userPanel);
        		}
        		table = 3;
        		box.getChildren().addAll(feedbackPanel);
        	}
        });
        
        
        // insert car
        insertButton.setOnAction((ActionEvent ev)-> {
        	// takes field from the form fields
            String loc = fieldLocation.getValue().toString();
            int seats = Integer.parseInt(fieldSeats.getValue().toString());
            // Check if all the fields are correct
            String outcome = "";
            if(fieldLicensePlate.getText().equals("") == false && fieldVendor.getText().equals("") == false &&
            		fieldKm.getText().matches("^\\d+$") == true && 
            		fieldPrice.getText().matches("^\\d+\\.?[0-9]*$") == true ) {
            	Car car = new Car(fieldVendor.getText(),seats,loc,Integer.parseInt(fieldKm.getText()),
            			Double.parseDouble(fieldPrice.getText()),fieldLicensePlate.getText(), false);
            	// try to insert new car in the DB
            	outcome = rh.insertCar(car);
                if(outcome.equals("Success!")) {
                	errorMsg.setFill(Color.GREEN);
                    clearAll();
                } else 
                	errorMsg.setFill(Color.RED);
            } else {
            	errorMsg.setFill(Color.RED);
            	outcome = "You have to insert all the fields correctly";
            	System.out.println("Insert car form is not correct");
            }
            errorMsg.setText(outcome);
        });
        
    }
    
    // reset the fields in the Employer Interface
    public void clearAll() {
        
        fieldLicensePlate.clear();
        fieldVendor.clear();
        fieldLocation.setValue("Firenze");
        fieldSeats.setValue("4");
        fieldKm.clear();
        fieldPrice.clear();
    }
    
    public AnchorPane getBox() {return box;}
}  
