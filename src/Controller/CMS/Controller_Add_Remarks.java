package Controller.CMS;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import org.controlsfx.dialog.Dialogs;

import application.Main;
import Model.CMS.Remarks_Info;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Font;
import javafx.stage.Stage;

@SuppressWarnings("deprecation")
public class Controller_Add_Remarks implements Initializable
{
	private Stage stage;
	private boolean isDone = false;
	private Remarks_Info remark_info, original_remark_info;
	private Integer ADD = 0, EDIT = 1, OTHER = 2;
	private Integer mode = OTHER;
	
	@FXML private TextArea remark_eng = new TextArea();
	
	@FXML private TextArea remark_guj = new TextArea();
	
	@FXML private TextArea remark_trans = new TextArea();
	
	@FXML private TextArea remark_context = new TextArea();
	
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
			remark_info.setEnglishText(remark_eng.getText());
			remark_info.setGujaratiText(remark_guj.getText());
			remark_info.setContext(remark_context.getText());
			isDone = storeToDB(remark_info);
			stage.close();
		}
	}
	
	private boolean storeToDB(Remarks_Info remark_info) 
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
			remark_info.setRemarkID(id);
			
			try
			{
				String query = "INSERT INTO Remarks VALUES(?, ?, ?, ?);";
				PreparedStatement stmt = con.prepareStatement(query);
				stmt.setString(1, remark_info.getRemarkID().getValue());
				stmt.setString(2, remark_info.get_english_text().getValue());
				stmt.setString(3, remark_info.get_gujarati_text().getValue());
				stmt.setString(4, remark_info.get_context().getValue());
				System.out.println(stmt);
				original_remark_info.setEnglishText(remark_info.get_english_text().getValue());
				original_remark_info.setGujaratiText(remark_info.get_gujarati_text().getValue());
				original_remark_info.setContext(remark_info.get_context().getValue());
				original_remark_info.setRemarkID(remark_info.getRemarkID().getValue());
				int no = stmt.executeUpdate();
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
				String query = "UPDATE Remarks SET English=?, Gujarati=?, Context=? WHERE remark_ID=?;";
				PreparedStatement stmt = con.prepareStatement(query);
				stmt.setString(4, remark_info.getRemarkID().getValue());
				stmt.setString(1, remark_info.get_english_text().getValue());
				stmt.setString(2, remark_info.get_gujarati_text().getValue());
				stmt.setString(3, remark_info.get_context().getValue());
				System.out.println(query);
				int no = stmt.executeUpdate();
				original_remark_info.setEnglishText(remark_info.get_english_text().getValue());
				original_remark_info.setGujaratiText(remark_info.get_gujarati_text().getValue());
				original_remark_info.setContext(remark_info.get_context().getValue());
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
		String query = "SELECT remark_id FROM Remarks ORDER BY remark_id;";
		System.out.println(query);
		int i = 1;
		try
		{
			PreparedStatement stmt = Main.getConnection().prepareStatement(query);
			ResultSet rs = stmt.executeQuery();
			System.out.println("Hello");
			while(rs.next())
			{
				System.out.println(rs.getString("remark_id"));
				if(Integer.parseInt(rs.getString("remark_id")) > i)
				{
					return Integer.toString(i);
				}
				System.out.println(rs.getString(1) + "\t" + i);
				i += 1;
			}
			stmt.close();
		}
		catch(SQLException E)
		{
			System.out.println("Exception1");
			E.printStackTrace();
		}
		return Integer.toString(i);
	}

	@FXML
	private void handle_btn_clear()
	{
		remark_eng.clear();
		remark_guj.clear();
		remark_trans.clear();
		remark_context.clear();
	}
		
	@FXML
	private void handle_btn_cancel()
	{
		stage.close();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) 
	{
		Font gujju_font = Font.loadFont(getClass().getResourceAsStream("/Resources/Gujrati-Saral-1.ttf"), 20);
		remark_trans.setFont(gujju_font);
		remark_trans.setEditable(false);
	}
	
	public boolean isSaveClicked()
	{
		return isDone;
	}
	
	public void setRemark(Remarks_Info remark_info, String mode) 
	{
		System.out.println("Setting....");
		this.original_remark_info = remark_info;
		this.remark_info = Remarks_Info.clone(original_remark_info);
		if(remark_info.get_english_text() != null)
		{
			remark_eng.setText(remark_info.get_english_text().getValue());
		}
		if(remark_info.get_gujarati_text() != null)
		{
			remark_guj.setText(remark_info.get_gujarati_text().getValue());
		}
		if(remark_info.get_context() != null)
		{
			remark_context.setText(remark_info.get_context().getValue());
		}
		if(mode.equals("ADD"))
		{
			this.mode = ADD;
		}
		else
		{
			this.mode = EDIT;
		}
	}
	
	@FXML
	private void handle_guj_change()
	{
		remark_trans.setText(remark_guj.getText());
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
		if(remark_eng.getText() == null || remark_eng.getText().length() == 0)
		{
			
			errorMsg += "Remark English is empty\n";
			retValue = false;
		}
		System.out.println("1");
		if(remark_guj.getText() == null || remark_guj.getText().length() == 0)
		{
			errorMsg += "Remark Gujarati is empty\n";
			retValue = false;
		}
		System.out.println("2");
		if(remark_context.getText() == null || remark_context.getText().length() == 0)
		{
			errorMsg += "Remark context is empty\n";
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
