package Model.Patient;

import java.time.LocalDate;

import Model.CMS.Remarks_Info;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Prescription 
{
	private StringProperty pat_id;
	private LocalDate date, follow_up_date;
	private StringProperty time;
	private StringProperty disease;
	private StringProperty remarks;
	
	public Prescription()
	{
		this.pat_id = new SimpleStringProperty("");
		this.date = LocalDate.now();
		this.follow_up_date = LocalDate.now();
		this.time = new SimpleStringProperty("");
		this.disease = new SimpleStringProperty("");
		this.remarks = new SimpleStringProperty("");
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

	public LocalDate getFollow_up_date() {
		return follow_up_date;
	}

	public void setFollow_up_date(LocalDate follow_up_date) {
		this.follow_up_date = follow_up_date;
	}

	public StringProperty getTime() {
		return time;
	}

	public void setTime(StringProperty time) {
		this.time = time;
	}

	public StringProperty getDisease() {
		return disease;
	}

	public void setDisease(StringProperty disease) {
		this.disease = disease;
	}

	public StringProperty getRemarks() {
		return remarks;
	}

	public void setRemarks(StringProperty remarks) {
		this.remarks = remarks;
	}
	
	@Override
	public String toString()
	{
		if(this.date == LocalDate.MIN)
		{
			return "Dates";
		}
		return this.date + " at, " + this.time.getValue();
	}

	public static Prescription clone(Prescription original_pres_info)
	{
		Prescription pres = new Prescription();
		pres.setDate(original_pres_info.getDate());
		pres.setTime(original_pres_info.getTime());
		pres.setDisease(original_pres_info.getDisease());
		pres.setRemarks(original_pres_info.getRemarks());
		pres.setFollow_up_date(original_pres_info.getFollow_up_date());
		pres.setPat_id(original_pres_info.getPat_id());
		return pres;		
	}
	
	
}
