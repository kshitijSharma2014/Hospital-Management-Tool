package Controller.Prescription;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ResourceBundle;

import org.controlsfx.dialog.Dialogs;

import application.Main;
import Model.CMS.Medicine_Info;
import Model.Patient.Medicine_Prescribed;
import Model.Patient.Patient_Info;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

@SuppressWarnings("deprecation")
public class Controller_Add_Prescribed_Medicines implements Initializable
{
	private static int flag = 0;
	private Stage stage;
	private boolean isDone = false;
	private Medicine_Prescribed med_pres_info, original_med_pres_info;
	private Integer ADD = 0, EDIT = 1, OTHER = 2;
	private Integer mode = OTHER;

	private Medicine_Info med_info = new Medicine_Info();
	
	private ObservableList<Medicine_Info> medicineList = FXCollections.observableArrayList();
	private ObservableList<Medicine_Info> medicineShown = FXCollections.observableArrayList();
	
	@FXML private Button btn_save = new Button();
	@FXML private Button btn_clear = new Button();
	@FXML private Button btn_cancel = new Button();
	
	@FXML private TextField patient_name = new TextField();
	@FXML private TextField patient_id = new TextField();
	
	@FXML private TextField medicine_name = new TextField();
	@FXML private ListView<Medicine_Info> medicine_name_list = new ListView<Medicine_Info> ();
	
	@FXML private TextArea med_remarks = new TextArea();
	
	@FXML private TextArea date = new TextArea();
	@FXML private TextArea time = new TextArea();
	@FXML private TextArea morning_amt = new TextArea();
	@FXML private TextArea noon_amt = new TextArea();
	@FXML private TextArea evening_amt = new TextArea();
	
	@FXML private RadioButton morning_after_meal = new RadioButton();
	@FXML private RadioButton morning_before_meal = new RadioButton();
	
	@FXML private RadioButton noon_after_meal = new RadioButton();
	@FXML private RadioButton noon_before_meal = new RadioButton();
	
	@FXML private RadioButton evening_after_meal = new RadioButton();
	@FXML private RadioButton evening_before_meal = new RadioButton();
		
	@FXML 
	private void handle_btn_save()
	{
		if(isInputValid())
		{
			isDone = true;
			Medicine_Info m1 = medicine_name_list.getSelectionModel().getSelectedItem();
			if(m1 == null)
			{
				System.out.println("WHY??--------------------");
			}

			med_pres_info.setMedicine(med_info);
			med_pres_info.setMorning_amt(new SimpleStringProperty(morning_amt.getText()));
			med_pres_info.setNoon_amt(new SimpleStringProperty(noon_amt.getText()));
			med_pres_info.setEvening_amt(new SimpleStringProperty(evening_amt.getText()));
			
			if(morning_after_meal.selectedProperty().getValue())
			{
				med_pres_info.setMorning_meal(new SimpleStringProperty("true"));
			}
			else
			{
				med_pres_info.setMorning_meal(new SimpleStringProperty("false"));
			}
			
			if(noon_after_meal.selectedProperty().getValue())
			{
				med_pres_info.setNoon_meal(new SimpleStringProperty("true"));
			}
			else
			{
				med_pres_info.setNoon_meal(new SimpleStringProperty("false"));
			}
			
			if(evening_after_meal.selectedProperty().getValue())
			{
				med_pres_info.setEvening_meal(new SimpleStringProperty("true"));
			}
			else
			{
				med_pres_info.setEvening_meal(new SimpleStringProperty("false"));
			}
			
			isDone = storeToDB();
			stage.close();
		}
	}
	
	@FXML
	private void handle_btn_clear()
	{
		medicineShown = medicineList;
		if(mode == ADD)
		{
			morning_amt.setText("0");
			noon_amt.setText("0");;
			evening_amt.setText("0");;
			
			morning_after_meal.setSelected(false);
			morning_before_meal.setSelected(false);
			
			noon_after_meal.setSelected(false);
			noon_before_meal.setSelected(false);
			
			evening_after_meal.setSelected(false);
			evening_before_meal.setSelected(false);
		}
		else
		{
			medicine_name_list.getSelectionModel().select(original_med_pres_info.getMedicine());
			medicine_name.setText(medicine_name_list.getSelectionModel().getSelectedItem().get_med_name().getValue());
			morning_amt.setText(original_med_pres_info.getMorning_amt().getValue());
			noon_amt.setText(original_med_pres_info.getNoon_amt().getValue());
			evening_amt.setText(original_med_pres_info.getEvening_amt().getValue());
			
			if(original_med_pres_info.getMorning_meal().getValue() == "true")
			{
				morning_after_meal.setSelected(true);
			}
			else
			{
				morning_before_meal.setSelected(true);
			}
			if(original_med_pres_info.getNoon_meal().getValue() == "true")
			{
				noon_after_meal.setSelected(true);
			}
			else
			{
				noon_before_meal.setSelected(true);
			}
			if(original_med_pres_info.getEvening_meal().getValue() == "true")
			{
				evening_after_meal.setSelected(true);
			}
			else
			{
				evening_before_meal.setSelected(true);
			}
		}
	}
	
	@FXML
	private void handle_btn_cancel()
	{
		stage.close();
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
			try
			{
				String query = "INSERT INTO Medicine_prescribed VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				PreparedStatement stmt = con.prepareStatement(query);
				stmt.setString(1, med_pres_info.getPat_id().getValue());
				stmt.setString(2, med_pres_info.getDate().toString());
				stmt.setString(3, med_pres_info.getTime().getValue());
				stmt.setString(4, med_pres_info.getMedicine().get_med_id().getValue().toString());
				stmt.setString(5, med_pres_info.getMorning_amt().getValue());
				stmt.setString(6, med_pres_info.getNoon_amt().getValue());
				stmt.setString(7, med_pres_info.getEvening_amt().getValue());
				stmt.setString(8, med_pres_info.getMorning_meal().getValue());
				stmt.setString(9, med_pres_info.getNoon_meal().getValue());
				stmt.setString(10, med_pres_info.getEvening_meal().getValue());
				System.out.println("INSERTING........................");
				int no = stmt.executeUpdate();
				
				original_med_pres_info.setMedicine(med_pres_info.getMedicine());
				original_med_pres_info.setMorning_amt(med_pres_info.getMorning_amt());
				original_med_pres_info.setNoon_amt(med_pres_info.getNoon_amt());
				original_med_pres_info.setEvening_amt(med_pres_info.getEvening_amt());
				original_med_pres_info.setMorning_meal(med_pres_info.getMorning_meal());
				original_med_pres_info.setNoon_meal(med_pres_info.getNoon_meal());
				original_med_pres_info.setEvening_meal(med_pres_info.getEvening_meal());
				
				System.out.println("No of rows updated: " + no);
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
				String query = "UPDATE Medicine_prescribed SET medicine_id=?, morning_amt=?, noon_amt=?, evening_amt=?, is_morning=?, is_noon=?, is_evening=? WHERE pat_ID=? AND date=? AND time=? AND medicine_id=?;";
				PreparedStatement stmt = con.prepareStatement(query);
				System.out.println(med_pres_info.getMedicine().get_med_id().getValue());
				stmt.setString(1, med_info.get_med_id().getValue());
				stmt.setString(2, med_pres_info.getMorning_amt().getValue());
				stmt.setString(3, med_pres_info.getNoon_amt().getValue());
				stmt.setString(4, med_pres_info.getEvening_amt().getValue());
				stmt.setString(5, med_pres_info.getMorning_meal().getValue());
				stmt.setString(6, med_pres_info.getNoon_meal().getValue());
				stmt.setString(7, med_pres_info.getEvening_meal().getValue());
				stmt.setString(8, med_pres_info.getPat_id().getValue());
				stmt.setString(9, med_pres_info.getDate().toString());
				stmt.setString(10, med_pres_info.getTime().getValue());
				stmt.setString(11, original_med_pres_info.getMedicine().get_med_id().getValue());
				System.out.println("EDITTING...........................");
				int no = stmt.executeUpdate();

				original_med_pres_info.setMedicine(med_pres_info.getMedicine());
				original_med_pres_info.setMorning_amt(med_pres_info.getMorning_amt());
				original_med_pres_info.setNoon_amt(med_pres_info.getNoon_amt());
				original_med_pres_info.setEvening_amt(med_pres_info.getEvening_amt());
				original_med_pres_info.setMorning_meal(med_pres_info.getMorning_meal());
				original_med_pres_info.setNoon_meal(med_pres_info.getNoon_meal());
				original_med_pres_info.setEvening_meal(med_pres_info.getEvening_meal());

				System.out.println("No of rows updated: " + no);
				stmt.close();
			}
			catch(SQLException E)
			{
				E.printStackTrace();
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
	
	public boolean isSaveClicked()
	{
		return isDone;
	}
	
	public void setStage(Stage dialogStage) 
	{
		this.stage = dialogStage;
	}

	public void setPrescription(Medicine_Prescribed med_pres_info, Patient_Info pat_info, String mode)
	{
		System.out.println("Starting for " + mode);
		System.out.println("Medicine id: " + med_pres_info.getMedicine().get_med_id().getValue());
		this.date.setText(med_pres_info.getDate().toString());
		this.time.setText(med_pres_info.getTime().getValue());
		
		this.patient_id.setText(pat_info.getPat_id().getValue());
		this.patient_name.setText(pat_info.getFirst_name().getValue());
		
		System.out.println("Setting....");
		this.original_med_pres_info = med_pres_info;
		this.med_pres_info = Medicine_Prescribed.clone(original_med_pres_info);
		System.out.println(med_pres_info.getMedicine().get_med_id().getValue());
		for(Medicine_Info mi : medicineList)
		{
			if(mi.get_med_id().getValue().equals(med_pres_info.getMedicine().get_med_id().getValue()))
			{
				System.out.println("Selecting............................................");
				this.medicine_name_list.getSelectionModel().select(mi);
				med_info = mi;
				break;
			}
		}
		this.medicine_name.setText(med_pres_info.getMedicine().get_med_name().getValue());
		this.med_remarks.setText(med_pres_info.getMedicine().get_med_remarks().getValue());
		
		if(med_pres_info.getMorning_amt() != null)
		{
			morning_amt.setText(med_pres_info.getMorning_amt().getValue());
		}
		if(med_pres_info.getNoon_amt() != null)
		{
			noon_amt.setText(med_pres_info.getNoon_amt().getValue());
		}
		if(med_pres_info.getEvening_amt() != null)
		{
			evening_amt.setText(med_pres_info.getEvening_amt().getValue());
		}
		if(med_pres_info.getMorning_meal() != null)
		{
			if(med_pres_info.getMorning_meal().getValue().equals("true"))
			{
				morning_after_meal.selectedProperty().set(true);
			}
			else
			{
				morning_before_meal.selectedProperty().set(true);
			}
		}
		if(med_pres_info.getNoon_meal() != null)
		{
			if(med_pres_info.getNoon_meal().getValue().equals("true"))
			{
				noon_after_meal.selectedProperty().set(true);
			}
			else
			{
				noon_before_meal.selectedProperty().set(true);
			}
		}
		if(med_pres_info.getEvening_meal() != null)
		{
			if(med_pres_info.getEvening_meal().getValue().equals("true"))
			{
				evening_after_meal.selectedProperty().set(true);
			}
			else
			{
				evening_before_meal.selectedProperty().set(true);
			}
		}
		
		if(mode.equals("ADD"))
		{
			this.mode = ADD;
		}
		else
		{
			this.mode = EDIT;
		}
		System.out.println("Bye/////");
	}
	
	@FXML
	private void handle_mouse_click()
	{
		System.out.println("hello");
		Medicine_Info mi = medicine_name_list.getSelectionModel().getSelectedItem();
		med_info = mi;
		medicine_name.setText(mi.get_med_name().getValue());
		med_remarks.setText(mi.get_med_remarks().getValue());
	}

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		date.setText(LocalDate.now().toString());
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		String time = sdf.format(cal.getTime());
		this.time.setText(time);
		
		this.date.setEditable(false);
		this.time.setEditable(false);
		
		this.patient_name.setEditable(false);
		this.patient_id.setEditable(false);
		
		this.medicine_name.setEditable(true);
		
		this.medicine_name.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) 
			{ 
				medicineShown.clear();
				for(Medicine_Info mi : medicineList)
				{
					if(mi.get_med_name().getValue().contains(newValue))
					{
						medicineShown.add(mi);
					}
				}
			}
			
		});
		
		
				
//		Medicine_Info m1 = new Medicine_Info("paracetamol", "company_a", "beemari");
//		m1.set_med_id("1");
//		medicineList.add(m1);
//		
//		Medicine_Info m2 = new Medicine_Info("amol", "company_b", "beemari_2");
//		m2.set_med_id("2");
//		medicineList.add(m2);
		
		System.out.println("Hello");
		
		Connection con = Main.getConnection();
		if(con == null)
		{
			Dialogs.create()
    		.owner(stage)
    		.title(" ALERT ")
    		.masthead(" Database is not setup ")
    		.message("Please set up the connection ")
    		.showWarning();
		}
		else
		{
			String query = "SELECT * FROM Medicine;";
			try
			{
				PreparedStatement stmt = con.prepareStatement(query);
				ResultSet rs = stmt.executeQuery();
				while(rs.next())
				{
					Medicine_Info mi = new Medicine_Info();
					mi.set_med_id(rs.getString("medicine_ID"));
					mi.set_med_name(rs.getString("medicine_name"));
					mi.set_med_cmpy(rs.getString("company"));
					mi.set_med_remarks(rs.getString("other_remarks"));
					medicineList.add(mi);
				}
			}
			catch(SQLException E)
			{
				Dialogs.create()
	    		.owner(stage)
	    		.title(" ALERT ")
	    		.masthead(" Problem in the connection ")
	    		.message("Please set up the connection again.")
	    		.showWarning();
				Main.setConnection(null);
				Main.setDbName("");
				Main.setUsername("");
				Main.setpassword("");
				Main.setPort("");
			}
		}
		medicineShown.addAll(medicineList);
		medicine_name_list.setItems(medicineShown);
		System.out.println("beee");
	}
	
	private boolean isInputValid()
	{
		System.out.println("Checking for error");
		String errorMsg = "";
		boolean retValue = true;
		if(date.getText() == null || date.getText().length() == 0)
		{
			errorMsg += "Date field is empty\n";
			retValue = false;
		}
		if(time.getText() == null || time.getText().length() == 0)
		{
			errorMsg += "Time is empty\n";
			retValue = false;
		}
		if(morning_amt.getText() == null || morning_amt.getText().length() == 0)
		{
			errorMsg += "Morning amount field is empty\n";
			retValue = false;
		}
		if(noon_amt.getText() == null || noon_amt.getText().length() == 0)
		{
			errorMsg += "Noon amount field is empty\n";
			retValue = false;
		}
		if(evening_amt.getText() == null || evening_amt.getText().length() == 0)
		{
			errorMsg += "Evening amount field is empty\n";
			retValue = false;
		}
		if(morning_after_meal.selectedProperty().get() == false && morning_before_meal.selectedProperty().get() == false && !morning_amt.getText().equals("0"))
		{
			errorMsg += "Choose at least one : taking morning medicine after or before meal\n";
			retValue = false;
		}
		if(noon_after_meal.selectedProperty().get() == false && noon_before_meal.selectedProperty().get() == false && !noon_amt.getText().equals("0"))
		{
			errorMsg += "Choose at least one : taking noon medicine after or before meal\n";
			retValue = false;
		}
		if(evening_after_meal.selectedProperty().get() == false && evening_before_meal.selectedProperty().get() == false && !evening_amt.getText().equals("0"))
		{
			errorMsg += "Choose at least one : taking evening medicine after or before meal\n";
			retValue = false;
		}
						
		if(errorMsg.length() > 0)
		{
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(stage);
			alert.setTitle("Invalid Fields");
			alert.setHeaderText("Please correct invalid fields");
			alert.setContentText(errorMsg);
			alert.showAndWait();
			
		}
		return retValue;
	}

}
