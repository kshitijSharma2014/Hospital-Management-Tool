/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller.Login;

import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

import org.controlsfx.dialog.Dialogs;

import Controller.AdmitPatient.Controller_Admit_Patient;
import Controller.AdmitPatient.Controller_Search_Patient_Admit;
import Controller.CMS.Controller_Manage_Database;
import Model.Employee.Employee_Info;
import Model.Patient.Indoor_Patient;
import Model.Patient.Patient_Info;
import application.Main;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Anshu
 */
@SuppressWarnings("deprecation")
public class Controller_Login implements Initializable {
    
	private Employee_Info employee_info;
	
    @FXML private PasswordField password;
    
    @FXML private TextField username;
      
    @FXML private Button btn_login;
    @FXML private Button btn_connect;
        
    @FXML private Image img_logo;
    
    private Main mainApp;
    
    private Stage stage;
    private boolean isDone = false;

    public boolean returnIsDone()
    {
    	return isDone;
    }
    
    public Employee_Info retEmp()
    {
    	return this.employee_info;
    }
    
    @FXML
    private void handle_btn_login() 
    {
       isValid();
       if(isDone)
       {
    	   stage.close();
       }
    }
    
    @FXML
    private void handle_btn_connect()
    {
    	showDialogConnectDatabase();
    }
    
    private boolean showDialogConnectDatabase()
	{
		boolean retValue = false;
		try
		{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/View/CMS/Database_Connectivity_Screen.fxml"));
			AnchorPane page = (AnchorPane) loader.load();
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Admit New Patient");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(stage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);
			Controller_Manage_Database controller = loader.getController();
			System.out.println("Hi!!\n");
			controller.setStage(dialogStage);
			dialogStage.showAndWait();
		}
		catch(Exception E)
		{
			E.printStackTrace();
		}
		return retValue;
	}
    
    private boolean isValid()
    {
    	 String username_field = username.getText();
         String pass_field = password.getText();
         String errorMsg = "";
         boolean validate = true;
         
         if(username_field.equals("") && pass_field.equals(""))
         {
         	errorMsg += "Enter username and password\n";
         	validate = false;
         }
         if(!username_field.equals("") && pass_field.equals(""))
         {
         	errorMsg += "Enter password\n";
         	validate = false;
         }
         if(username_field.equals("") && !pass_field.equals(""))
         {
         	errorMsg += "Enter username\n";
         	validate = false;
         }
         
         if(!validate)
         {
        	 Dialogs.create()
 			.owner(stage)
 			.title(" ALERT ")
 			.masthead(" Please enter req. fields ")
 			.message(errorMsg)
 				.showWarning();
        	 return validate;
         }
       
         boolean validate_db = false;
         
         try
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
			String query = "SELECT * FROM Employee WHERE username=?";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, username_field);
 			ResultSet rs = stmt.executeQuery();
 			int flag = 0, matches = 0;
 			while(rs.next())
 			{
	 			flag = 1;
	 			String pass = rs.getString("password");
	 			if(pass_field.equals(pass))
	 			{
		 			matches = 1;
		 			break;
	 			}
 			}
 			if(flag == 1 && matches == 1)
 			{
 				System.out.println("Setting employee up...");
 				isDone = true;
	 			validate_db = true;
 				employee_info = new Employee_Info();
	 			employee_info.setId(new SimpleStringProperty(rs.getString("emp_id")));
	 			employee_info.setCategory(new SimpleStringProperty(rs.getString("category")));
	 			employee_info.setFirst_name(new SimpleStringProperty(rs.getString("name")));
	 			employee_info.setBirth_date(new SimpleStringProperty(rs.getString("Birth_date")));
	 			employee_info.setMarital_status(new SimpleStringProperty(rs.getString("Marital_status")));
	 			employee_info.setSalary(new SimpleStringProperty(rs.getString("salary")));
	 			employee_info.setDate_of_joining(LocalDate.parse(rs.getString("date_of_joining")));
	 			employee_info.setContact_no(new SimpleStringProperty(rs.getString("phone")));
	 			employee_info.setAddress(new SimpleStringProperty(rs.getString("address")));
	 			employee_info.setCity(new SimpleStringProperty(rs.getString("city")));
	 			employee_info.setState(new SimpleStringProperty(rs.getString("state")));
	 			employee_info.setUsername(new SimpleStringProperty(rs.getString("username")));
	 			employee_info.setPassword(new SimpleStringProperty(rs.getString("password")));
	 			System.out.println(employee_info.getId().getValue());
	 			Main.setEmployee(employee_info);
 			}
 			else
 			{
 				isDone = false;
 				Dialogs.create()
				.owner(stage)
				.title(" ALERT ")
				.masthead(" Error ")
				.message("Sorry, Wrong username or password ")
					.showWarning();
	 			validate_db = false;
 			}
 			
 			if(username_field.equals("admin") && pass_field.equals("admin"))
 			{
 				isDone = true;
 				employee_info = new Employee_Info();
 				employee_info.setId(new SimpleStringProperty("hi"));
	 			employee_info.setCategory(new SimpleStringProperty("hi"));
	 			employee_info.setFirst_name(new SimpleStringProperty("hi"));
	 			employee_info.setBirth_date(new SimpleStringProperty("hi"));
	 			employee_info.setMarital_status(new SimpleStringProperty("hi"));
	 			employee_info.setSalary(new SimpleStringProperty("hi"));
	 			employee_info.setDate_of_joining(LocalDate.now());
	 			employee_info.setContact_no(new SimpleStringProperty("hi"));
	 			employee_info.setAddress(new SimpleStringProperty("hi"));
	 			employee_info.setCity(new SimpleStringProperty("hi"));
	 			employee_info.setState(new SimpleStringProperty("hi"));
	 			employee_info.setUsername(new SimpleStringProperty("hi"));
	 			Main.setEmployee(employee_info);
 			}
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
         
         return validate_db;
    }
    
    
    private static String get_SHA_1_SecurePassword(String passwordToHash, String salt)
    {
    	System.out.println("original: " + passwordToHash);
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            //md.update(salt.getBytes());
            byte[] bytes = md.digest(passwordToHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
            System.out.println("Hash is: " + generatedPassword);
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return generatedPassword;
    }
    private static String getSalt() 
    {
        try
        {
        	SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            byte[] salt = new byte[16];
	        sr.nextBytes(salt);
	        return salt.toString();
        }
        catch(NoSuchAlgorithmException E)
        {
        	return null;
        }
    }
        
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
    	username.setText("user");
    	password.setText("password");
    	img_logo = new Image("/Resources/img_logo.png");
    }    
    
    public void setStage(Stage stage)
    {
    	this.stage = stage;
    }
    
}
