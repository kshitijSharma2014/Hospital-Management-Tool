package Controller.CMS;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import org.controlsfx.dialog.Dialogs;

import application.Main;
import Model.CMS.Tests_Info;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

@SuppressWarnings("deprecation")
public class Controller_Add_Tests implements Initializable
{
	private Stage stage;
	private boolean isDone = false;
	private Tests_Info tests_info, original_tests_info;
	private Integer ADD = 0, EDIT = 1, OTHER = 2;
	private Integer mode = OTHER;
	
	@FXML private TextArea test_type = new TextArea();
	
	@FXML private Label test_type_label = new Label();
	
	@FXML private Button btn_save = new Button();
	
	@FXML private Button btn_clear = new Button();
	
	@FXML private Button btn_cancel = new Button();
	
	@FXML
	private void handle_btn_save()
	{
		if(isInputValid())
		{
			isDone = true;
			tests_info.setName(test_type.getText());
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
			tests_info.setID(id);
			try
			{
				String query = "INSERT INTO Test_Info VALUES(?, ?);";
				PreparedStatement stmt = con.prepareStatement(query);
				stmt.setString(1, tests_info.get_test_id().getValue());
				stmt.setString(2, tests_info.get_test_name().getValue());
				int no = stmt.executeUpdate();
				original_tests_info.setName(tests_info.get_test_name().getValue());
				original_tests_info.setID(tests_info.get_test_id().getValue());
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
				String query = "UPDATE Test_Info SET test_name=? WHERE test_id=?;";
				PreparedStatement stmt = con.prepareStatement(query);
				stmt.setString(2, tests_info.get_test_id().getValue());
				stmt.setString(1, tests_info.get_test_name().getValue());
				int no = stmt.executeUpdate();
				original_tests_info.setName(tests_info.get_test_name().getValue());
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
		String query = "SELECT test_id FROM Test_Info ORDER BY test_id;";
		int i = 1;
		try
		{
			Statement stmt = Main.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
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
		test_type.clear();
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
	
	public void setTest(Tests_Info tests_info) 
	{
		System.out.println("Setting....");
		this.original_tests_info = tests_info;
		this.tests_info = Tests_Info.clone(original_tests_info);
		if(tests_info.get_test_name() != null)
		{
			test_type.setText(tests_info.get_test_name().getValue());
		}
		if(tests_info.get_test_id() == null)
		{
			mode = ADD;
		}
		else
		{
			mode = EDIT;
		}
	}
		
	public void setAppStage(Stage stage)
	{
		this.stage = stage;
	}
	
	private boolean isInputValid()
	{
		System.out.println("Checking for error");
		String errorMsg = "";
		boolean retValue = true;
		if(test_type.getText() == null || test_type.getText().length() == 0)
		{
			errorMsg += "Test Type Name is empty\n";
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
