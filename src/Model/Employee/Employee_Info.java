package Model.Employee;

import java.time.LocalDate;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Employee_Info 
{
	private StringProperty id, first_name, middle_name, last_name;
	private StringProperty username, password;
	private StringProperty designation, salary;
	private LocalDate date_of_joining;
	private StringProperty category;
	private StringProperty birth_date;
	private StringProperty marital_status;
	private StringProperty contact_no, state, city, address;
	
	public Employee_Info()
	{
		this.first_name = new SimpleStringProperty("");
		this.last_name = new SimpleStringProperty("");
		this.username = new SimpleStringProperty("");
		this.password = new SimpleStringProperty("");
		this.id = new SimpleStringProperty("");
		this.middle_name = new SimpleStringProperty("");
		this.designation = new SimpleStringProperty("");
		this.salary = new SimpleStringProperty("");
		this.category = new SimpleStringProperty("");
		this.birth_date = new SimpleStringProperty("");
		this.marital_status = new SimpleStringProperty("");
		this.contact_no = new SimpleStringProperty("");
		this.state = new SimpleStringProperty("");
		this.city = new SimpleStringProperty("");
		this.address = new SimpleStringProperty("");
		this.date_of_joining = LocalDate.MIN;
	}

	public StringProperty getId() {
		return id;
	}

	public void setId(StringProperty id) {
		this.id = id;
	}

	public StringProperty getFirst_name() {
		return first_name;
	}

	public void setFirst_name(StringProperty first_name) {
		this.first_name = first_name;
	}

	public StringProperty getMiddle_name() {
		return middle_name;
	}

	public void setMiddle_name(StringProperty middle_name) {
		this.middle_name = middle_name;
	}

	public StringProperty getLast_name() {
		return last_name;
	}

	public void setLast_name(StringProperty last_name) {
		this.last_name = last_name;
	}

	public StringProperty getUsername() {
		return username;
	}

	public void setUsername(StringProperty username) {
		this.username = username;
	}

	public StringProperty getPassword() {
		return password;
	}

	public void setPassword(StringProperty password) {
		this.password = password;
	}

	public StringProperty getDesignation() {
		return designation;
	}

	public void setDesignation(StringProperty designation) {
		this.designation = designation;
	}

	public StringProperty getSalary() {
		return salary;
	}

	public void setSalary(StringProperty salary) {
		this.salary = salary;
	}

	public LocalDate getDate_of_joining() {
		return date_of_joining;
	}

	public void setDate_of_joining(LocalDate date_of_joining) {
		this.date_of_joining = date_of_joining;
	}

	public StringProperty getCategory() {
		return category;
	}

	public void setCategory(StringProperty category) {
		this.category = category;
	}

	public StringProperty getBirth_date() {
		return birth_date;
	}

	public void setBirth_date(StringProperty birth_date) {
		this.birth_date = birth_date;
	}

	public StringProperty getMarital_status() {
		return marital_status;
	}

	public void setMarital_status(StringProperty marital_status) {
		this.marital_status = marital_status;
	}

	public StringProperty getContact_no() {
		return contact_no;
	}

	public void setContact_no(StringProperty contact_no) {
		this.contact_no = contact_no;
	}

	public StringProperty getState() {
		return state;
	}

	public void setState(StringProperty state) {
		this.state = state;
	}

	public StringProperty getCity() {
		return city;
	}

	public void setCity(StringProperty city) {
		this.city = city;
	}

	public StringProperty getAddress() {
		return address;
	}

	public void setAddress(StringProperty address) {
		this.address = address;
	}
	
	
	
}
