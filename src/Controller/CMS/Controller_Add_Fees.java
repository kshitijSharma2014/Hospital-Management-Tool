package Controller.CMS;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import org.controlsfx.dialog.Dialogs;

import application.Main;
import Model.CMS.Fees_Info;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/*
 * This is a controller for the dialog to add new fees type.
 * Author: Devanshu Jain
 */

@SuppressWarnings("deprecation")
public class Controller_Add_Fees implements Initializable
{
	private Stage stage;
	private boolean isDone = false;
	private Fees_Info fees_info, original_fees_info;
	private Integer ADD = 0, EDIT = 1, OTHER = 2;
	private Integer mode = OTHER;
	
	@FXML private TextArea fees_type = new TextArea();
	
	@FXML private Label fees_type_label = new Label();
	
	@FXML private Button btn_save = new Button();
	
	@FXML private Button btn_clear = new Button();
	
	@FXML private Button btn_cancel = new Button();
	
	@FXML
	private void handle_btn_save()
	{
		if(isInputValid())
		{
			isDone = true;
			fees_info.setName(fees_type.getText());
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
			String id = generateID();
			fees_info.setID(id);
			try
			{
				String query = "INSERT INTO Fee_type VALUES(?, ?)";
				PreparedStatement stmt = con.prepareStatement(query);
				stmt.setString(1, fees_info.get_fees_id().getValue());
				stmt.setString(2, fees_info.get_fees_name().getValue());
				int no = stmt.executeUpdate();
				original_fees_info.setName(fees_info.get_fees_name().getValue());
				original_fees_info.setID(fees_info.get_fees_id().getValue());
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
				String query = "UPDATE Fee_type SET fee_name=? WHERE fee_id=?;";
				PreparedStatement stmt = con.prepareStatement(query);
				stmt.setString(1, fees_info.get_fees_name().getValue());
				stmt.setString(2, fees_info.get_fees_id().getValue());
				int no = stmt.executeUpdate();
				original_fees_info.setName(fees_info.get_fees_name().getValue());
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
		String query = "SELECT fee_id FROM Fee_type ORDER BY fee_id;";
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
		fees_type.clear();
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
	
	public void setFees(Fees_Info fees_info) 
	{
		System.out.println("Setting....");
		this.original_fees_info = fees_info;
		this.fees_info = Fees_Info.clone(original_fees_info);
		if(fees_info.get_fees_name() != null)
		{
			fees_type.setText(fees_info.get_fees_name().getValue());
		}
		if(fees_info.get_fees_id() == null)
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
		if(fees_type.getText() == null || fees_type.getText().length() == 0)
		{
			errorMsg += "Fees Type Name is empty\n";
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
