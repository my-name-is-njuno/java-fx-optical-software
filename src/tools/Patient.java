package tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import static java.util.Calendar.*;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Patient {
	
	private SimpleIntegerProperty id;
	private SimpleStringProperty name;
	private SimpleStringProperty addedBy;
	private SimpleStringProperty contact;
	private SimpleStringProperty email;
	private SimpleStringProperty dob;
	private SimpleStringProperty age;
	private SimpleStringProperty adress;
	private SimpleStringProperty withins;
	public int getId() {
		return id.get();
	}
	public String getName() {
		return name.get();
	}
	public String getIns() {
		return addedBy.get();
	}
	public String getContact() {
		return contact.get();
	}
	public String getEmail() {
		return email.get();
	}
	public String getDob() {
		return dob.get();
	}
	public String getAdress() {
		return adress.get();
	}
	public String getWithins() {
		return withins.get();
	}
	public Patient(int id, String name, String addedBy, String contact, String email, String dob, String adress, String withins ) {
		super();
		this.id = new SimpleIntegerProperty(id);
		this.name = new SimpleStringProperty(name);
		this.addedBy =  new SimpleStringProperty(addedBy);
		this.contact =  new SimpleStringProperty(contact);
		this.email =  new SimpleStringProperty(email);
		this.dob =  new SimpleStringProperty(dob);
		this.adress =  new SimpleStringProperty(adress);
		this.withins =  new SimpleStringProperty(withins);
	}
	
	
	
	
	
	
	
	
	public static Calendar getCalendar(Date date) {
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(date);
	    return cal;
	}
	
	
	
	public String calcualteAge() {
		
		String dif = "";
		
		Date dobb;
		try {
			dobb = new SimpleDateFormat("yyyy-mm-dd").parse(this.getDob());
			Calendar b = getCalendar(dobb);
			String date  = new Date().toString();
			Date x = new SimpleDateFormat("yyyy-mm-dd").parse(date);
			Calendar a = getCalendar(x);
			
			int diff = a.get(YEAR) - b.get(YEAR);
		    if (a.get(MONTH) > b.get(MONTH) || 
		        (a.get(MONTH) == b.get(MONTH) && a.get(DATE) > b.get(DATE))) {
		        diff--;
		    }
		    
		   dif = diff + " years old";
		   return dif;
		    
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		this.age = new SimpleStringProperty(dif);
		return dif;
		
	}
	
	
	
	
	

}
