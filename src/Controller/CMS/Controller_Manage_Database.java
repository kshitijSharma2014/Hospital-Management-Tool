package Controller.CMS;

import java.net.URL;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class Controller_Manage_Database implements Initializable
{
	private Main mainApp;
	private Stage stage;
	private Connection conn = null;
	
	@FXML Label ip_addr_label = new Label();
	
	@FXML Label port_no_label = new Label();
	
	@FXML Label db_name_label = new Label();
	
	@FXML Label username_label = new Label();
	
	@FXML Label password_label = new Label();
	
	@FXML TextArea ip_addr = new TextArea();
	
	@FXML TextArea port_no = new TextArea();
	
	@FXML TextArea db_name = new TextArea();
	
	@FXML TextArea username = new TextArea();
	
	@FXML PasswordField password = new PasswordField();
	
	@FXML
	private void handle_btn_save()
	{
		if(isInputValid())
		{
			Main.setIP(ip_addr.getText());
			Main.setPort(port_no.getText());
			Main.setDbName(db_name.getText());
			Main.setUsername(username.getText());
			Main.setpassword(password.getText());
			if(conn == null)
			{
				System.out.println("How????");
			}
			Main.setConnection(conn);
			stage.close();
		}
	}

	@FXML
	private void handle_btn_clear()
	{
		ip_addr.setText("");
		port_no.setText("");
		db_name.setText("");
		username.setText("");
		password.setText("");
	}
	
	@FXML
	private void handle_btn_cancel()
	{
		stage.close();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) 
	{
		this.ip_addr.setText(Main.getIP());
		this.db_name.setText(Main.getDBName());
		this.port_no.setText(Main.getPort());
		this.password.setText(Main.getPassword());
		this.username.setText(Main.getUserName());
	}
	
	private boolean isInputValid() 
	{
		boolean retValue = true;
		String errorMsg = "";
		if(ip_addr.getText() == null || ip_addr.getText().length() == 0)
		{
			errorMsg += "IP address is empty\n";
			retValue = false;
		}
		if(port_no.getText() == null || port_no.getText().length() == 0)
		{
			errorMsg += "Port No. is empty\n";
			retValue = false;
		}
		if(db_name.getText() == null || db_name.getText().length() == 0)
		{
			errorMsg += "Database Name is empty\n";
			retValue = false;
		}
		if(username.getText() == null || username.getText().length() == 0)
		{
			errorMsg += "User Name is empty\n";
			retValue = false;
		}
		if(password.getText() == null || password.getText().length() == 0)
		{
			errorMsg += "Password is empty\n";
			retValue = false;
		}
		try
		{
			String query = "jdbc:sqlserver://" + ip_addr.getText() + "\\SEN" + ":" + port_no.getText() + ";databaseName=" + db_name.getText();
			System.out.println(query);
			conn = DriverManager.getConnection(query, username.getText(), password.getText());
			System.out.println("Connected...");
			
			DatabaseMetaData md = conn.getMetaData();
			ResultSet rs = md.getTables(null, null, "%", null);
			
			while(rs.next())
			{
				System.out.println(rs.getString(3));
			}
			
		}
		catch(SQLException E)
		{
			E.printStackTrace();
			errorMsg += "SQL error\n";
			retValue = false;
		}
		
		if(!retValue)
		{	
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(stage);
			alert.setTitle("ERROR...");
			alert.setHeaderText("Please check the following");
			alert.setContentText(errorMsg);
			
			alert.showAndWait();
		}
		else
		{
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.initOwner(stage);
			alert.setTitle("SUCCESS");
			alert.setHeaderText("Hurray!!!");
			alert.setContentText("Database Connected");
			
			alert.showAndWait();
		}
		return retValue;
	}
	
	public void setStage(Stage stage)
	{
		this.stage = stage;
	}
	
	public void setForm(String ip, String port, String DBName, String username, String password)
	{
		ip_addr.setText(ip);
		port_no.setText(port);
		db_name.setText(DBName);
		this.username.setText(username);
		this.password.setText(password);
	}
}
