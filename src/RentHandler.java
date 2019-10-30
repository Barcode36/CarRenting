
import java.time.*;
import java.util.*;


public class RentHandler {
    
    //register method
    public String register(User regUser) {
    	int ret;
        String outcome = "";
        if(regUser!=null) {
        	if(regUser.getFiscalCode().equals("") == false && regUser.getNickName().equals("") == false && regUser.getName().equals("") == false && regUser.getSurname().equals("") == false && 
        			regUser.getEmail().equals("") == false && regUser.getPassword().equals("") == false) {
        		//1) se l'utente esiste gi�, 2) database error, 0) inserimento riuscito
                ret = JPAHandleDB.create(regUser);
                switch (ret){
                    case 0:
                        System.out.println("Successfull registration!");
                        outcome = "You've been registered!";
                        return outcome;
                    case 1:
                        System.out.println("This user already exists! ");
                        outcome = "OOps! You are already registered!";
                        return outcome;
                    default:
                        System.out.println("Database Error");
                        outcome = "OOps! Something went wrong!:(";
                        return outcome;
                }
        	}
        }
        System.out.println("The registration fields are not correctly inserted");
        outcome = "OOps! You didn't complete "+'\n'+"    the registration fields";
        return outcome;
        
        } 
            
    //login method
    public String login(User loggedUser){
        String outcome = "";
        int ret;
        if(loggedUser!=null) {
        	if(loggedUser.getEmail().equals("") == false && loggedUser.getPassword() != null) {
        		//1) se l'utente ha già una prenotazione, 2) database error, 0) inserimento riuscito
        		ret = JPAHandleDB.logIn(loggedUser);
                switch (ret){
                    case 0:
                        System.out.println("Successfull login!");
                        outcome = "Success!";
                        return outcome;
                    case 1:
                        System.out.println("The user attempt to insert wrong credentials! ");
                        outcome = "OOps! Wrong credentials";
                        return outcome;
                    default:
                        System.out.println("Database Error");
                        outcome = "OOps! Something went wrong!:(";
                        return outcome;
                }
        	}
        }
        System.out.println("The login fields are not correctly inserted");
        outcome = "OOps! You didn't insert "+'\n'+"        the login fields";
        return outcome;
    }
    // employer interface method
    public String insertCar(Car car) {
        String outcome = "";
        int ret;
        if(car!=null) {
        	if(car.getLicencePlate().equals("") == false && car.getVendor().equals("") == false) {
        		//1) se l'utente ha già una prenotazione, 2) database error, 0) inserimento riuscito
        		ret = JPAHandleDB.create(car);
                switch (ret){
                        case 0:
                            System.out.println("Successfull insertion!");
                            outcome = "Success!";
                            return outcome;
                        case 1:
                            System.out.println("The employer attempt to insert an existing car! ");
                            outcome = "OOps! This car already exists!";
                            return outcome;
                        default:
                            System.out.println("Database Error");
                            outcome = "OOps! Something went wrong!:(";
                            return outcome;
                } 
        	}
        } 
        System.out.println("The login fields are not correctly inserted");
        outcome = "OOps! You didn't insert "+'\n'+"        the login fields";
        return outcome;
    }
    
    // The function deletes a car if it has no active reservation, otherwise it returns an error.
    public String deleteCar(Car car) {
    	String outcome = "";
    	int ret = JPAHandleDB.existsCarActiveReservations(car);
    	switch (ret){
	        case 0: // There is no reservation for this car
	        	if (JPAHandleDB.delete(car)) {
	        		System.out.println("Successfull delation!");
		            outcome = "Success!";
		            return outcome;
	        	} else {
	        		System.out.println("Database Error");
		            outcome = "OOps! Something went wrong!:(";
		            return outcome;
	        	}
	        case 1: // There is an active reservation for this car
	            System.out.println("The employer attempt to delete a car that has an active reservation! ");
	            outcome = "OOps! This car has an active reservation!";
	            return outcome;
	        default: // Database error
	            System.out.println("Database Error");
	            outcome = "OOps! Something went wrong!:(";
	            return outcome;
    	}
    }

    public List<Feedback> showFeedbacks() {
        System.out.println("Feedbacks updated");
        List<Feedback> feedbacks = JPAHandleDB.selectAllFeedbacks();
        return feedbacks;
    }
    
    public List<Feedback> showFeedbacks(int mark) {
        System.out.println("Feedbacks updated");
        List<Feedback> feedbacks = JPAHandleDB.selectAllFeedbacks(mark);
        return feedbacks;
    }
    
    public List<User> showCustomers(){
    	List<User> customers = JPAHandleDB.selectAllCustomers();
    	return customers;
    }
    
    public String deleteReservation(User user) {
        boolean succ = JPAHandleDB.deleteReservation(user);
        if(succ) {
            System.out.println("Reservation deleted");
            return "Your reservation has been deleted!";
        }
        else {
            System.out.println("Database error");
            return "OOps! Something went wrong, please try later";
        }
    }
    
    public List<Car> showAvailableCar(LocalDate pickUpDate, LocalDate deliveryDate, String locality, String seats, StringBuilder out) {
    	int numSeats = Integer.parseInt(seats);
        List<Car> carList = JPAHandleDB.findAvailableCars(pickUpDate, deliveryDate, locality, numSeats);
        if(carList == null){
           System.out.println("Database Error!"); 
           out.append("Try Again Later. Server not Found");
           carList = new ArrayList<>();
        }
        else{
           System.out.println("List retrived"); 
           out.append(""); 
        }
        return carList;
    }
      
    public String addReservation(User user, Car selectedCar, LocalDate pickUpDate, LocalDate deliveryDate) {
    	Reservation res = new Reservation(pickUpDate, deliveryDate, user, selectedCar);
        String outcome = "";
        //1) if user already has an active reservation, 2) database error, 0) inserting done
        int ret = JPAHandleDB.create(res);
            switch (ret){
                case 0:
                    System.out.println("Successfull Reservation!");
                    outcome = "Success!";
                    return outcome;
                case 1:
                    System.out.println("The user attempt to insert another reservation. There's already an existing one! ");
                    outcome = "OOps! There's already an active reservation for you";
                    return outcome;
                default:
                    System.err.println("Database Error");
                    outcome = "OOps! Something went wrong!:(";
                    return outcome;
            }                
    }
    
    public boolean addFeedback(User user, String comment, String mark) {
    	int intMark = Integer.parseInt(mark);
    	Feedback feedback = new Feedback(intMark, comment, LocalDate.now(), user);
    	int result = JPAHandleDB.create(feedback);
    	if (result == 0) {
    		return true;
    	} else {
    		return false;
    	}
    }
}
