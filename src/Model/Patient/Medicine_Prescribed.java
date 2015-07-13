package Model.Patient;

import java.time.LocalDate;

import Model.CMS.Medicine_Info;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Medicine_Prescribed 
{
	private StringProperty pat_id;
	private LocalDate date;
	private StringProperty time;
	private Medicine_Info medicine;
	private StringProperty morning_amt, noon_amt, evening_amt;
	private StringProperty morning_meal, noon_meal, evening_meal;
	
	public Medicine_Prescribed()
	{
		this.pat_id = new SimpleStringProperty("");
		this.date = LocalDate.now();
		this.time = new SimpleStringProperty("");
		this.medicine = new Medicine_Info("", "", "");
		this.morning_amt = new SimpleStringProperty("");
		this.noon_amt = new SimpleStringProperty("");
		this.evening_amt = new SimpleStringProperty("");
		this.morning_meal = new SimpleStringProperty("");
		this.noon_meal = new SimpleStringProperty("");
		this.evening_meal = new SimpleStringProperty("");
	}
	
	public StringProperty getPat_id() {
		return pat_id;
	}

	public void setPat_id(StringProperty pat_id) {
		this.pat_id = pat_id;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public StringProperty getTime() {
		return time;
	}

	public void setTime(StringProperty time) {
		this.time = time;
	}

	public Medicine_Info getMedicine() {
		return medicine;
	}

	public void setMedicine(Medicine_Info medicine) {
		this.medicine = medicine;
	}

	public StringProperty getMorning_amt() {
		return morning_amt;
	}

	public void setMorning_amt(StringProperty morning_amt) {
		this.morning_amt = morning_amt;
	}

	public StringProperty getNoon_amt() {
		return noon_amt;
	}

	public void setNoon_amt(StringProperty noon_amt) {
		this.noon_amt = noon_amt;
	}

	public StringProperty getEvening_amt() {
		return evening_amt;
	}

	public void setEvening_amt(StringProperty evening_amt) {
		this.evening_amt = evening_amt;
	}

	public StringProperty getMorning_meal() {
		return morning_meal;
	}

	public void setMorning_meal(StringProperty morning_meal) {
		this.morning_meal = morning_meal;
	}

	public StringProperty getNoon_meal() {
		return noon_meal;
	}

	public void setNoon_meal(StringProperty noon_meal) {
		this.noon_meal = noon_meal;
	}

	public StringProperty getEvening_meal() {
		return evening_meal;
	}

	public void setEvening_meal(StringProperty evening_meal) {
		this.evening_meal = evening_meal;
	}
	
	public static Medicine_Prescribed clone(Medicine_Prescribed med_pres)
	{
		Medicine_Prescribed med_pres_info = new Medicine_Prescribed();
		Medicine_Info m1 = new Medicine_Info(med_pres.getMedicine().get_med_name().getValue(), med_pres.getMedicine().get_med_company().getValue(), med_pres.getMedicine().get_med_remarks().getValue());
		m1.set_med_id(med_pres.getMedicine().get_med_id().getValue());
		med_pres_info.setMedicine(m1);
		med_pres_info.setPat_id(med_pres.getPat_id());
		med_pres_info.setDate(med_pres.getDate());
		med_pres_info.setTime(med_pres.getTime());
		med_pres_info.setMorning_amt(med_pres.getMorning_amt());
		med_pres_info.setNoon_amt(med_pres.getNoon_amt());
		med_pres_info.setEvening_amt(med_pres.getEvening_amt());
		med_pres_info.setMorning_meal(med_pres.getMorning_meal());
		med_pres_info.setNoon_meal(med_pres.getNoon_meal());
		med_pres_info.setEvening_meal(med_pres.getEvening_meal());
		
		return med_pres_info;
	}
	
	public String toString()
	{
		return this.medicine.toString();
	}
	
	
}
