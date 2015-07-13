package Controller.AdmitPatient;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.ResourceBundle;

import org.controlsfx.dialog.Dialogs;

import com.itextpdf.text.DocumentException;

import Controller.Prescription.Controller_Search_Prescription;
import Model.Patient.Indoor_Patient;
import Model.Patient.Patient_Info;
import application.Main;
import application.Reportpdf;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

@SuppressWarnings("deprecation")
public class Controller_Discharge_Patient implements Initializable
{
	private Main mainApp;
	private Stage stage; 
	private Indoor_Patient ind_pat_info, original_ind_pat_info;
	private Patient_Info pat_info;
	
	@FXML TextField pat_id = new TextField();
	@FXML DatePicker date_admit = new DatePicker();
	@FXML TextField time_admit = new TextField();
	@FXML DatePicker date_discharge = new DatePicker();
	@FXML TextField time_discharge = new TextField();
	@FXML TextField room_no = new TextField();
	
	@FXML Button btn_create_pres = new Button();
	@FXML Button btn_print_med = new Button();
	@FXML Button btn_print_test = new Button();
	@FXML Button btn_save = new Button();
	@FXML Button btn_cancel = new Button();
	@FXML Button btn_clear = new Button();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) 
	{
		this.pat_id.setEditable(false);
		this.date_admit.setEditable(false);
		this.time_admit.setEditable(false);
		this.date_discharge.setEditable(true);
		this.time_discharge.setEditable(true);
		this.room_no.setEditable(false);
		this.date_discharge.setValue(LocalDate.now());
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		String time = sdf.format(cal.getTime());
		this.time_discharge.setText(time);
	}
	
	public void setDetails(Indoor_Patient ind_pat_info, Patient_Info pat_info)
	{
		this.pat_info = pat_info;
		this.original_ind_pat_info = ind_pat_info; 
		this.ind_pat_info = Indoor_Patient.clone(ind_pat_info);
		
		this.pat_id.setText(original_ind_pat_info.getPat_id().getValue());
		this.date_admit.setValue(LocalDate.parse(original_ind_pat_info.getDate_of_admission().getValue()));
		this.time_admit.setText(original_ind_pat_info.getTime_of_admission().getValue());
		this.room_no.setText(original_ind_pat_info.getRoomNo().getValue());
	}
	
	@FXML
	private void handle_btn_create_pres()
	{
		try
		{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/View/Prescription/Search_Prescription.fxml"));
			AnchorPane page = (AnchorPane) loader.load();
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Manage Prescriptions");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(stage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);
			Controller_Search_Prescription controller = loader.getController();
			System.out.println("Hi!!\n");
			controller.setMainApp(mainApp);
			controller.setPatient(pat_info, "INDOOR_DISCHARGE");
			dialogStage.showAndWait();
			return ;			
		}
		catch(Exception E)
		{
			E.printStackTrace();
		}
		return ;
	}
	
	@FXML 
	private void handle_btn_print_med() throws ClassNotFoundException, DocumentException, IOException, SQLException
	{
		try
		{
			Reportpdf.createMedication(Reportpdf.Medication, pat_info.getPat_id().getValue());
			Reportpdf.printPDF(Reportpdf.Medication);
		}
		catch(Exception E)
		{
			System.out.println("Sorry, could not print..");
			Dialogs.create()
			.owner(stage)
			.title(" ALERT ")
			.masthead(" Printing Error ")
			.message("Sorry, Could not print.... ")
			.showWarning();
		}
		
	}
	
	@FXML
	private void handle_btn_print_test()
	{
		try
		{
			Reportpdf.createReport(Reportpdf.Report, pat_info.getPat_id().getValue());
			Reportpdf.printPDF(Reportpdf.Report);
		}
		catch(Exception E)
		{
			System.out.println("Sorry, could not print..");
			Dialogs.create()
			.owner(stage)
			.title(" ALERT ")
			.masthead(" Printing Error ")
			.message("Sorry, Could not print.... ")
			.showWarning();
		}		
	}
	
	@FXML
	private void handle_btn_save()
	{
		if(isInputValid())
		{
			ind_pat_info.setDate_of_discharge(new SimpleStringProperty(date_discharge.getValue().toString()));
			ind_pat_info.setTime_of_discharge(new SimpleStringProperty(time_discharge.getText()));
			storeToDB();
			try
			{
				FXMLLoader loader = new FXMLLoader();
				System.out.println("1");
				loader.setLocation(Main.class.getResource("/View/AdmitPatient/Search_Indoor_Patient.fxml"));
				System.out.println("2");
				AnchorPane anchor_pane = (AnchorPane) loader.load();
				Main.getRootLayout().setCenter(anchor_pane);
				Controller_Indoor_Patient controller = loader.getController();
				controller.setMainApp(mainApp);
			}
			catch(Exception E)
			{
				E.printStackTrace();
				
			}	
		}		
	}
	
	private boolean isInputValid() 
	{
		boolean returnValue = true;
		String errorMsg = "";
		if(time_discharge.getText().length() == 0)
		{
			errorMsg += "Time field is not in proper format";
			returnValue = false;
		}
		if(!returnValue)
		{
			Dialogs.create()
    		.owner(stage)
    		.title(" ALERT ")
    		.masthead(" Input is not valid...")
    		.message(errorMsg)
    		.showWarning();
		}
		return returnValue;
	}

	private void storeToDB() 
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
		}
		
		try
		{
			String query = "UPDATE Indoor_patient SET Date_of_discharge=?, Time_of_discharge=? WHERE pat_ID=? AND Date_of_admission=? AND Time_of_admission=?;";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, ind_pat_info.getDate_of_discharge().getValue());
			stmt.setString(2, ind_pat_info.getTime_of_discharge().getValue());
			stmt.setString(3, ind_pat_info.getPat_id().getValue());
			stmt.setString(4, ind_pat_info.getDate_of_admission().getValue());
			stmt.setString(5, ind_pat_info.getTime_of_admission().getValue());
			int no = stmt.executeUpdate();
			System.out.println("No of rows affeccted: " + no);
			original_ind_pat_info.setTime_of_discharge(ind_pat_info.getTime_of_discharge());
			original_ind_pat_info.setDate_of_discharge(ind_pat_info.getDate_of_discharge());
		}
		catch(SQLException E)
		{
			Dialogs.create()
	   		.owner(stage)
	   		.title(" ALERT ")
	   		.masthead(" SQlException encountered ")
	  		.message("Item could not be updated... ")
	   		.showWarning();
		}
	}

	@FXML
	private void handle_btn_cancel()
	{
		try
		{
			FXMLLoader loader = new FXMLLoader();
			System.out.println("1");
			loader.setLocation(Main.class.getResource("/View/AdmitPatient/Search_Indoor_Patient.fxml"));
			System.out.println("2");
			AnchorPane anchor_pane = (AnchorPane) loader.load();
			Main.getRootLayout().setCenter(anchor_pane);
			Controller_Indoor_Patient controller = loader.getController();
			controller.setMainApp(mainApp);
		}
		catch(Exception E)
		{
			E.printStackTrace();
		}	
	}
	
	@FXML
	private void handle_btn_clear()
	{
		date_discharge.setValue(LocalDate.now());
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		String time = sdf.format(cal.getTime());
		time_discharge.setText(time);
	}
	
	public void setMainApp(Main main)
	{
		this.mainApp = main;
		this.stage = mainApp.getStage();
	}

}
