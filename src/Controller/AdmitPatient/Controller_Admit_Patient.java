package Controller.AdmitPatient;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

import org.controlsfx.dialog.Dialogs;

import application.Main;
import Model.Patient.Indoor_Patient;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

@SuppressWarnings("deprecation")
public class Controller_Admit_Patient implements Initializable
{
	private Indoor_Patient ind_pat_info, original_ind_pat_info;
	private Integer ADD=0, EDIT=1, OTHER=2;
	private Integer mode = OTHER;
	private boolean isDone;
	private Stage stage;
	
	@FXML TextField pat_id = new TextField();
	@FXML DatePicker date_admit = new DatePicker();
	@FXML TextField time_admit = new TextField();
	@FXML TextField room_no = new TextField();
	@FXML TextField pat_name = new TextField();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) 
	{
		pat_id.setEditable(false);
		pat_name.setEditable(false);
	}
	
	private boolean storeToDB()
	{
		Connection con = Main.getConnection();
		if(con == null)
		{
			Main.setConnection(null);
			Main.setUsername("");
			Main.setPort("");
			Main.setpassword("");
			Main.setDbName("");
			Main.setIP("");
			
			Dialogs.create()
    		.owner(stage)
    		.title(" ALERT ")
    		.masthead(" Database is not setup ")
    		.message("Please set up the connection ")
    		.showWarning();
			return false;
		}
		
		if(mode == ADD)
		{			
			ind_pat_info.setDate_of_discharge(new SimpleStringProperty(""));
			ind_pat_info.setTime_of_discharge(new SimpleStringProperty(""));
			ind_pat_info.setDate_of_admission(new SimpleStringProperty(date_admit.getValue().toString()));
			ind_pat_info.setTime_of_admission(new SimpleStringProperty(time_admit.getText()));
			ind_pat_info.setRoomNo(new SimpleStringProperty(room_no.getText()));
			try
			{
				String query = "SELECT pat_ID from Indoor_patient;";
				PreparedStatement stmt = con.prepareStatement(query);
				ResultSet rs = stmt.executeQuery();
				while(rs.next())
				{
					System.out.println("Admiting : " + ind_pat_info.getDate_of_discharge().getValue() + "<<<<<<<<,,");
					if(rs.getString("pat_ID").equals(ind_pat_info.getPat_id().getValue()) && ind_pat_info.getDate_of_discharge().getValue().length() == 0)
					{
						Dialogs.create()
			    		.owner(stage)
			    		.title(" ALERT ")
			    		.masthead(" Patient already is admitted ")
			    		.message("Can't add him again ")
			    		.showWarning();
						return false;
					}
				}
								
				query = "INSERT INTO Indoor_patient VALUES(?, ?, ?, ?, ?, ?)";
				stmt = con.prepareStatement(query);
				stmt.setString(1, ind_pat_info.getPat_id().getValue());
				stmt.setString(2, ind_pat_info.getDate_of_admission().getValue());
				stmt.setString(3, ind_pat_info.getTime_of_admission().getValue());
				System.out.println("Date is: " + ind_pat_info.getDate_of_discharge().getValue());
				stmt.setString(4, null);
				stmt.setString(5, null);
				stmt.setString(6, ind_pat_info.getRoomNo().getValue());
				int no = stmt.executeUpdate();
				original_ind_pat_info.setPat_id(ind_pat_info.getPat_id());
				original_ind_pat_info.setDate_of_admission(ind_pat_info.getDate_of_admission());
				original_ind_pat_info.setTime_of_admission(ind_pat_info.getTime_of_admission());
				original_ind_pat_info.setRoomNo(ind_pat_info.getRoomNo());
				System.out.println("No of rows inserted: " + no);
				stmt.close();
			}
			catch(SQLException E)
			{
				E.printStackTrace();
				Dialogs.create()
	    		.owner(stage)
	    		.title(" ALERT ")
	    		.masthead(" SQlException encountered ")
	    		.message("Item could not be added... ")
	    		.showWarning();
				return false;
			}
		}
		else if(mode == EDIT)
		{
			try
			{
				String query = "UPDATE Indoor_patient SET Date_of_admission=? Time_of_admission=?, Room_no=? WHERE pat_ID=? AND Date_of_admission=? AND Time_of_admission=?;";
				PreparedStatement stmt = con.prepareStatement(query);
				
				stmt.setString(1, ind_pat_info.getDate_of_admission().getValue());
				stmt.setString(2, ind_pat_info.getTime_of_admission().getValue());
				stmt.setString(3, ind_pat_info.getRoomNo().getValue());
				stmt.setString(4, ind_pat_info.getPat_id().getValue());
				stmt.setString(5, original_ind_pat_info.getDate_of_admission().getValue());
				stmt.setString(6, original_ind_pat_info.getTime_of_admission().getValue());
								
				int no = stmt.executeUpdate();
				original_ind_pat_info.setPat_id(ind_pat_info.getPat_id());
				original_ind_pat_info.setDate_of_admission(ind_pat_info.getDate_of_admission());
				original_ind_pat_info.setTime_of_admission(ind_pat_info.getTime_of_admission());
				original_ind_pat_info.setRoomNo(ind_pat_info.getRoomNo());
				System.out.println("No of rows updated: " + no);
				stmt.close();
			}
			catch(SQLException E)
			{
				Dialogs.create()
	    		.owner(stage)
	    		.title(" ALERT ")
	    		.masthead(" SQlException encountered ")
	    		.message("Information could not be saved... ")
	    		.showWarning();
				return false;
			}
		}
		else
		{
			System.out.println("Mode is not valid...");
			return false;
		}
		return true;
	}
	
	public void setDetails(Indoor_Patient ind_pat_info, String name, String mode)
	{
		original_ind_pat_info = ind_pat_info;
		this.ind_pat_info = Indoor_Patient.clone(ind_pat_info);
		if(mode.equals("ADD"))
		{
			this.mode = ADD;
		}
		else
		{
			this.mode = EDIT;
		}
		this.ind_pat_info = ind_pat_info;
		this.pat_name.setText(name);
		this.pat_id.setText(ind_pat_info.getPat_id().getValue());
		this.date_admit.setValue(LocalDate.parse(ind_pat_info.getDate_of_admission().getValue()));
		this.time_admit.setText(ind_pat_info.getTime_of_admission().getValue());
		this.room_no.setText(ind_pat_info.getRoomNo().getValue());
	}
	
	@FXML
	public void handle_btn_save()
	{
		if(isValid())
		{
			isDone = true;
			ind_pat_info.setDate_of_admission(new SimpleStringProperty(date_admit.getValue().toString()));
			ind_pat_info.setTime_of_admission(new SimpleStringProperty(time_admit.getText()));
			ind_pat_info.setRoomNo(new SimpleStringProperty(room_no.getText()));
			isDone = storeToDB();
			stage.close();
		}
	}
	
	@FXML
	public void handle_btn_clear()
	{
		if(mode == ADD)
		{
			time_admit.clear();
			room_no.clear();			
		}
		else
		{
			date_admit.setValue(LocalDate.parse(original_ind_pat_info.getDate_of_admission().getValue()));
			time_admit.setText(original_ind_pat_info.getTime_of_admission().getValue());
			room_no.setText(original_ind_pat_info.getRoomNo().getValue());
		}
	}
	
	@FXML
	public void handle_btn_cancel()
	{
		stage.close();
	}
	
	public boolean isSaveClicked()
	{
		return isDone;
	}
	
	public void setStage(Stage stage)
	{
		System.out.println("Hi!! 2 time\n");
		this.stage = stage;
	}
	
	private boolean isValid()
	{
		boolean retValue = true;
		String errormsg = "";
		if(date_admit.getValue() == null || date_admit.getValue().toString().length() == 0)
		{
			retValue = false;
			errormsg += "Admission Date is invalid";
		}
		if(time_admit.getText().length() == 0)
		{
			retValue = false;
			errormsg += "Time of admission is invalid";
		}
		if(room_no.getText().length() == 0)
		{
			retValue = false;
			errormsg += "Room No. is empty";
		}
		if(!retValue)
		{
			Dialogs.create()
    		.owner(stage)
    		.title(" ALERT ")
    		.masthead(" Invalid Input ")
    		.message(errormsg)
    		.showWarning();
			return false;
		}
		return retValue;
	}

}
