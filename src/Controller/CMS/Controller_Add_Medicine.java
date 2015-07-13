package Controller.CMS;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import org.controlsfx.dialog.Dialogs;

import application.Main;
import Model.CMS.Medicine_Info;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

@SuppressWarnings("deprecation")
public class Controller_Add_Medicine implements Initializable
{
	private Stage stage;
	private boolean isDone = false;
	private Medicine_Info medicine_details, original_medicine_details;
	private Integer ADD = 0, EDIT = 1, OTHER = 2;
	private Integer mode = OTHER;

	
	@FXML private TextArea medicine_name = new TextArea();
	
	@FXML private TextArea medicine_company = new TextArea();
	
	@FXML private TextArea medicine_remarks = new TextArea();
	
	@FXML private Label med_name_label = new Label();
	
	@FXML private Label med_company_label = new Label();
	
	@FXML private Label med_remarks_label = new Label();
	
	@FXML private Button btn_save = new Button();
	
	@FXML private Button btn_clear = new Button();
	
	@FXML private Button btn_cancel = new Button();
	
	@FXML
	private void handle_btn_save()
	{
		if(isInputValid())
		{
			isDone = true;
			medicine_details.set_med_name(medicine_name.getText());
			medicine_details.set_med_cmpy(medicine_company.getText());
			medicine_details.set_med_remarks(medicine_remarks.getText());
			isDone = storeToDB();
			stage.close();
		}
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
				String id = generateID();
				medicine_details.set_med_id(id);
				String query = "INSERT INTO Medicine VALUES(?, ?, ?, ?)";
				PreparedStatement stmt = con.prepareStatement(query);
				stmt.setString(1, medicine_details.get_med_id().getValue());
				stmt.setString(2, medicine_details.get_med_name().getValue());
				stmt.setString(3, medicine_details.get_med_company().getValue());
				stmt.setString(4, medicine_details.get_med_remarks().getValue());
				int no = stmt.executeUpdate();
				original_medicine_details.set_med_name(medicine_details.get_med_name().getValue());
				original_medicine_details.set_med_cmpy(medicine_details.get_med_company().getValue());
				original_medicine_details.set_med_remarks(medicine_details.get_med_remarks().getValue());
				original_medicine_details.set_med_id(medicine_details.get_med_id().getValue());
				System.out.println("No of rows updated: " + no);
				stmt.close();
			}
			catch(SQLException E)
			{
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
				String query = "UPDATE Medicine SET medicine_name=?, company=?, other_remarks=? WHERE medicine_id=?;";
				PreparedStatement stmt = con.prepareStatement(query);
				stmt.setString(1, medicine_details.get_med_name().getValue());
				stmt.setString(2, medicine_details.get_med_company().getValue());
				stmt.setString(3, medicine_details.get_med_remarks().getValue());
				stmt.setString(4, medicine_details.get_med_id().getValue());
				System.out.println("Statements is : " + stmt);
				int no = stmt.executeUpdate();
				original_medicine_details.set_med_name(medicine_details.get_med_name().getValue());
				original_medicine_details.set_med_cmpy(medicine_details.get_med_company().getValue());
				original_medicine_details.set_med_remarks(medicine_details.get_med_remarks().getValue());
				original_medicine_details.set_med_id(medicine_details.get_med_id().getValue());
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

	private String generateID()
	{
		String query = "SELECT medicine_id FROM Medicine ORDER BY medicine_id;";
		int i = 1;
		try
		{
			PreparedStatement stmt = Main.getConnection().prepareStatement(query);
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next())
			{
				if(Integer.parseInt(rs.getString(1)) != i)
				{
					return Integer.toString(i);
				}
				i += 1;
			}
		}
		catch(SQLException E)
		{
			E.printStackTrace();
		}
		return Integer.toString(i);
	}

	@FXML
	private void handle_btn_clear()
	{
		medicine_name.clear();
		medicine_company.clear();
		medicine_remarks.clear();
	}
		
	@FXML
	private void handle_btn_cancel()
	{
		stage.close();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) 
	{
		
	}
	
	public boolean isSaveClicked()
	{
		return isDone;
	}
	
	public void setMedicine(Medicine_Info med_info) 
	{
		System.out.println("Setting....");
		this.original_medicine_details = med_info;
		this.medicine_details = Medicine_Info.clone(original_medicine_details);
		if(med_info.get_med_name() != null)
		{
			medicine_name.setText(med_info.get_med_name().getValue());			
		}
		if(med_info.get_med_company() != null)
		{
			medicine_company.setText(med_info.get_med_company().getValue());			
		}
		if(med_info.get_med_remarks() != null)
		{
			medicine_remarks.setText(med_info.get_med_remarks().getValue());			
		}		
		
		if(med_info.get_med_id() == null)
		{
			mode = ADD;
		}
		else
		{
			mode = EDIT;
		}
	}
		
	public void setStage(Stage stage)
	{
		System.out.println("Hi!! 2 time\n");
		this.stage = stage;
	}
	
	private boolean isInputValid()
	{
		System.out.println("Checking for error");
		String errorMsg = "";
		boolean retValue = true;
		if(medicine_name.getText() == null || medicine_name.getText().length() == 0)
		{
			
			errorMsg += "Medicine Name is empty\n";
			retValue = false;
		}
		System.out.println("1");
		if(medicine_company.getText() == null || medicine_company.getText().length() == 0)
		{
			errorMsg += "Medicine Company Name is empty\n";
			retValue = false;
		}
		System.out.println("2");
		if(medicine_remarks.getText() == null || medicine_remarks.getText().length() == 0)
		{
			errorMsg += "Remarks for Medicine is empty\n";
			retValue = false;
		}
		System.out.println("nananana" + errorMsg + "\n");
		
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
