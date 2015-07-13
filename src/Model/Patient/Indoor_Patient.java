package Model.Patient;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Indoor_Patient
{
	private StringProperty pat_id;
	private StringProperty date_of_discharge, time_of_discharge;
	private StringProperty date_of_admission, time_of_admission;
	private StringProperty roomNo;
	
	public Indoor_Patient()
	{
		this.pat_id = new SimpleStringProperty("");
		this.date_of_admission = new SimpleStringProperty("");
		this.time_of_admission = new SimpleStringProperty("");
		this.date_of_discharge = new SimpleStringProperty("");
		this.time_of_discharge = new SimpleStringProperty("");
		this.roomNo = new SimpleStringProperty("");
	}

	public StringProperty getPat_id() {
		return pat_id;
	}

	public void setPat_id(StringProperty pat_id) {
		this.pat_id = pat_id;
	}

	public StringProperty getDate_of_discharge() {
		return date_of_discharge;
	}

	public void setDate_of_discharge(StringProperty date_of_discharge) {
		this.date_of_discharge = date_of_discharge;
	}

	public StringProperty getTime_of_discharge() {
		return time_of_discharge;
	}

	public void setTime_of_discharge(StringProperty time_of_discharge) {
		this.time_of_discharge = time_of_discharge;
	}

	public StringProperty getDate_of_admission() {
		return date_of_admission;
	}

	public void setDate_of_admission(StringProperty date_of_admission) {
		this.date_of_admission = date_of_admission;
	}

	public StringProperty getTime_of_admission() {
		return time_of_admission;
	}

	public void setTime_of_admission(StringProperty time_of_admission) {
		this.time_of_admission = time_of_admission;
	}

	public StringProperty getRoomNo() {
		return roomNo;
	}

	public void setRoomNo(StringProperty roomNo) {
		this.roomNo = roomNo;
	}

	public static Indoor_Patient clone(Indoor_Patient ind_pat_info) 
	{
		Indoor_Patient ind_pat_info_2 = new Indoor_Patient();
		ind_pat_info_2.setPat_id(ind_pat_info.getPat_id());
		ind_pat_info_2.setDate_of_admission(ind_pat_info.getDate_of_admission());
		ind_pat_info_2.setTime_of_admission(ind_pat_info.getTime_of_admission());
		ind_pat_info_2.setDate_of_discharge(ind_pat_info.getDate_of_discharge());
		ind_pat_info_2.setTime_of_discharge(ind_pat_info.getTime_of_discharge());
		ind_pat_info_2.setRoomNo(ind_pat_info.getRoomNo());
		return ind_pat_info_2;
	}
	
	
	
}
