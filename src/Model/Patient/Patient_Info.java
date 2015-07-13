package Model.Patient;

import java.time.LocalDate;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Patient_Info 
{
	private StringProperty pat_id;
	private StringProperty first_name, middle_name, last_name;
	private LocalDate birth_date;
	private StringProperty sex, marital_status, phone;
	private StringProperty emergency_name, emergency_relation, emergency_contact;
	private StringProperty address, city, state;
	
	public Patient_Info()
	{
		this.pat_id = new SimpleStringProperty("");
		this.first_name = new SimpleStringProperty("");
		this.middle_name = new SimpleStringProperty("");
		this.last_name = new SimpleStringProperty("");
		this.birth_date = LocalDate.now();
		this.sex = new SimpleStringProperty("");
		this.marital_status = new SimpleStringProperty("");
		this.phone = new SimpleStringProperty("");
		this.emergency_contact = new SimpleStringProperty("");
		this.emergency_name = new SimpleStringProperty("");
		this.emergency_relation = new SimpleStringProperty("");
		this.address = new SimpleStringProperty("");
		this.city = new SimpleStringProperty("");
		this.state = new SimpleStringProperty("");
	}

	public StringProperty getPat_id() {
		return pat_id;
	}

	public void setPat_id(StringProperty pat_id) {
		this.pat_id = pat_id;
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

	public LocalDate getBirth_date() {
		return birth_date;
	}

	public void setBirth_date(LocalDate birth_date) {
		this.birth_date = birth_date;
	}

	public StringProperty getSex() {
		return sex;
	}

	public void setSex(StringProperty sex) {
		this.sex = sex;
	}

	public StringProperty getMarital_status() {
		return marital_status;
	}

	public void setMarital_status(StringProperty marital_status) {
		this.marital_status = marital_status;
	}

	public StringProperty getPhone() {
		return phone;
	}

	public void setPhone(StringProperty phone) {
		this.phone = phone;
	}

	public StringProperty getEmergency_name() {
		return emergency_name;
	}

	public void setEmergency_name(StringProperty emergency_name) {
		this.emergency_name = emergency_name;
	}

	public StringProperty getEmergency_relation() {
		return emergency_relation;
	}

	public void setEmergency_relation(StringProperty emergency_relation) {
		this.emergency_relation = emergency_relation;
	}

	public StringProperty getEmergency_contact() {
		return emergency_contact;
	}

	public void setEmergency_contact(StringProperty emergency_contact) {
		this.emergency_contact = emergency_contact;
	}

	public StringProperty getAddress() {
		return address;
	}

	public void setAddress(StringProperty address) {
		this.address = address;
	}

	public StringProperty getCity() {
		return city;
	}

	public void setCity(StringProperty city) {
		this.city = city;
	}

	public StringProperty getState() {
		return state;
	}

	public void setState(StringProperty state) {
		this.state = state;
	}

	public static Patient_Info clone(Patient_Info pat_info) 
	{
		Patient_Info pat_info_dup = new Patient_Info();
		pat_info_dup.setFirst_name(pat_info.getFirst_name());
		pat_info_dup.setPat_id(pat_info.getPat_id());
		pat_info_dup.setAddress(pat_info.getAddress());
		pat_info_dup.setBirth_date(pat_info.getBirth_date());
		pat_info_dup.setCity(pat_info.getCity());
		pat_info_dup.setEmergency_contact(pat_info.getEmergency_contact());
		pat_info_dup.setEmergency_name(pat_info.getEmergency_name());
		pat_info_dup.setEmergency_relation(pat_info.getEmergency_relation());
		pat_info_dup.setMarital_status(pat_info.getMarital_status());
		pat_info_dup.setState(pat_info.getState());
		pat_info_dup.setSex(pat_info.getSex());
		pat_info_dup.setPhone(pat_info.getPhone());
		return pat_info_dup;
	}
	
		
	
}
