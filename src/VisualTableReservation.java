import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class VisualTableReservation extends TableView<Reservation>{
	 private ObservableList<Reservation> reservationsList;
	    
     public VisualTableReservation(){
       
        TableColumn columnFiscalCode = new TableColumn("FISCAL CODE");
        columnFiscalCode.setCellValueFactory(new PropertyValueFactory<>("fiscalCode"));	
        TableColumn columnLicencePlate = new TableColumn("LICENCE PLATE");
        columnLicencePlate.setCellValueFactory(new PropertyValueFactory<>("licencePlate"));
        TableColumn columnPickUpDate = new TableColumn("PICKUP DATE");
        columnPickUpDate.setCellValueFactory(new PropertyValueFactory<>("pickUpDate"));
        TableColumn columnDeliveryDate = new TableColumn("DELIVERY DATE");
        columnDeliveryDate.setCellValueFactory(new PropertyValueFactory<>("deliveryDate"));
        
        reservationsList = FXCollections.observableArrayList();
        setItems(reservationsList);
        getColumns().addAll(columnFiscalCode, columnLicencePlate, columnPickUpDate, columnDeliveryDate);
    }
    
    public void setTableReservationStyle() {
        setFixedCellSize(40);
        setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        setPrefWidth(90);
        setMaxHeight(480);
    }
    
    public void ListReservationUpdate(List<Reservation> reservations){
    	reservationsList.clear();
    	reservationsList.addAll(reservations);
    }
    
    public ObservableList<Reservation> getReservations() {return reservationsList;}
    
}


