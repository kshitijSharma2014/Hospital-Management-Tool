package Controller.Employee;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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

import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;

import application.Main;
import Model.Employee.Employee_Info;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.DatePicker;
import javafx.scene.image.Image;
import javafx.stage.Stage;

@SuppressWarnings("deprecation")
public class Controller_Add_Employee implements Initializable 
{
	
	private Employee_Info employee_info;
	
	private static int ADD=1, EDIT=2, OTHER=3;
	private static int mode = OTHER;
	
	@FXML private TextField emp_name;	
	@FXML private TextField emp_username;
	@FXML private PasswordField emp_password, emp_password_confirm;
	@FXML private ComboBox emp_category;	
	@FXML private DatePicker emp_date_of_joining;	
	@FXML private DatePicker emp_date_birth;
	@FXML private ComboBox emp_marital_status;
	@FXML private TextField emp_salary, emp_contact_no;
	@FXML private ComboBox emp_state;
	@FXML private TextField emp_city;
	@FXML private TextArea emp_address;
	
	@FXML private Button btn_save, btn_clear, btn_cancel;
	
	@FXML private Label password_label;
	@FXML private Label contact_no_label;
	
	@FXML private Image img_add_user;
	    
	private Stage stage;  

	@FXML
    public void handle_btn_clear(ActionEvent event) 
    {
		Action response = Dialogs.create()
			.owner(stage)
	        .title("Confirmation ")
	        .masthead(" This will clear all the data inputed by you ")
	        .message("Are you sure you want to clear the data? ")
	        .actions(Dialog.ACTION_YES, Dialog.ACTION_NO)
	        .showConfirm();

		if (response == Dialog.ACTION_YES) 
		{
			emp_name.setText("");
			
			emp_username.setText("");
			emp_password.setText("");
			emp_password_confirm.setText("");
			emp_date_birth.setValue(null);
			emp_date_of_joining.setValue(null);
			emp_category.setValue(null);
			emp_marital_status.setValue(null);
			emp_state.setValue(null);
			emp_city.setText("");
			emp_salary.setText("");
			emp_contact_no.setText("");
			emp_address.setText("");
			
			password_label.setText("");
			emp_contact_no.setText("");
			emp_password.setText("");
			emp_password_confirm.setText("");
		}
	    else 
	    {
	    	// Do nothing
	    }        
    }
	
    @FXML
    private void handle_btn_save()
    {
    	if(isValid())
    	{
    		this.employee_info.setFirst_name(new SimpleStringProperty(emp_name.getText()));
    		this.employee_info.setAddress(new SimpleStringProperty(emp_address.getText()));
    		this.employee_info.setBirth_date(new SimpleStringProperty(emp_date_birth.getValue().toString()));
    		this.employee_info.setMarital_status(new SimpleStringProperty(emp_marital_status.getValue().toString()));
    		this.employee_info.setSalary(new SimpleStringProperty(emp_salary.getText()));
    		this.employee_info.setDate_of_joining(emp_date_of_joining.getValue());
    		this.employee_info.setContact_no(new SimpleStringProperty(emp_contact_no.getText()));
    		this.employee_info.setCity(new SimpleStringProperty(emp_city.getText()));
    		this.employee_info.setState(new SimpleStringProperty(emp_state.getValue().toString()));
    		this.employee_info.setUsername(new SimpleStringProperty(emp_username.getText()));
    		this.employee_info.setPassword(new SimpleStringProperty(emp_password.getText()));
    		
    		System.out.println("STORING...");
    		boolean isDone = storeToDB();
    		
    		stage.close();
    	}
    }
    
    private String generateID() 
    {
    	Connection con = Main.getConnection();
    	if(con == null)
    	{
    		Dialogs.create()
    		.owner(stage)
    		.title(" ALERT ")
    		.masthead(" Database is not setup ")
    		.message("Please set up the connection ")
    		.showWarning();
			return null;
    	}
    	try
    	{
    		String query = "SELECT emp_id FROM Employee ORDER BY emp_id";
    		PreparedStatement stmt = con.prepareStatement(query);
    		ResultSet rs = stmt.executeQuery();
    		int i = 1;
    		while(rs.next())
    		{
    			String ID = rs.getString("emp_id");
    			if(ID.equals(Integer.toString(i)))
    			{
    				i += 1;
    			}
    			else
    			{
    				break;
    			}
    		}
    		query = "SELECT username FROM Employee where username=?;";
    		stmt = con.prepareStatement(query);
    		stmt.setString(1, emp_username.getText());
    		rs = stmt.executeQuery();
    		int flag = 0;
    		while(rs.next())
    		{
    			flag = 1;
    			break;
    		}
    		if(flag == 1)
    		{
    			Dialogs.create()
        		.owner(stage)
        		.title(" ALERT ")
        		.masthead(" Error ")
        		.message("Username exeists.. Choose another one")
        		.showWarning();
    			return null;
    		}
    		return Integer.toString(i);
    	}
    	catch(SQLException E)
    	{
    		E.printStackTrace();
    		Dialogs.create()
    		.owner(stage)
    		.title(" ALERT ")
    		.masthead(" Database is not setup ")
    		.message("ID could not be generated.. ")
    		.showWarning();
    	}
    	return null;
	}

	private boolean storeToDB()
    {
		String id = employee_info.getId().getValue();
		if(mode == ADD)
		{
			id = generateID();
			if(id == null)
			{
				return false;
			}
			employee_info.setId(new SimpleStringProperty(id));
		}
		
		String category = employee_info.getCategory().getValue();
		String First_Name = employee_info.getFirst_name().getValue();
		String Birth_Date = employee_info.getBirth_date().getValue();
		String Marital_status = employee_info.getMarital_status().getValue();
		String salary = employee_info.getSalary().getValue();
		String date_of_joining = employee_info.getDate_of_joining().toString();
		String phone = employee_info.getContact_no().getValue();
		String address = employee_info.getAddress().getValue();
		String city = employee_info.getCity().getValue();
		String state = employee_info.getState().getValue();
		String emp_post = "";
		String username = employee_info.getUsername().getValue();
		String password = employee_info.getPassword().getValue();
		
		if(mode == ADD)
		{
			String insertEmployee_query = "INSERT INTO Employee VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
			System.out.println("query is: " + insertEmployee_query);
			
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
				PreparedStatement stmt = con.prepareStatement(insertEmployee_query);
				
				stmt.setString(1, id);
				stmt.setString(2, category);
				stmt.setString(3, First_Name);
				stmt.setString(4, Birth_Date);
				stmt.setString(5, Marital_status);
				stmt.setString(6, salary);
				stmt.setString(7, date_of_joining);
				stmt.setString(8, phone);
				stmt.setString(9, address);
				stmt.setString(10, city);
				stmt.setString(11, state);
				stmt.setString(12, emp_post);
				stmt.setString(13, username);
				
				stmt.setString(14, password);
				
				stmt.executeUpdate();
			}
			catch(Exception E)
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
			return true;
    	}
		else if(mode == EDIT)
		{
			String insertEmployee_query = "UPDATE Employee SET category=?, Name=?, Birth_Date=?, Marital_status=?, salary=?, date_of_joining=?, phone=?, address=?, city=?, state=?, emp_post=?, username=?, password=? WHERE emp_id=?;";
			
			System.out.println("query is: " + insertEmployee_query);
			
			try
			{
				Connection con = Main.getConnection();
				if(con == null)
				{
					
					if(password.length() >= 1)
					{
						String salt1 = getSalt();
						String pass_hash = get_SHA_1_SecurePassword(password, salt1);
						password = pass_hash;
					}
					else
					{
						password = employee_info.getPassword().getValue();
					}
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
				PreparedStatement stmt = con.prepareStatement(insertEmployee_query);
				
				stmt.setString(14, id);
				stmt.setString(1, category);
				stmt.setString(2, First_Name);
				stmt.setString(3, Birth_Date);
				stmt.setString(4, Marital_status);
				stmt.setString(5, salary);
				stmt.setString(6, date_of_joining);
				stmt.setString(7, phone);
				stmt.setString(8, address);
				stmt.setString(9, city);
				stmt.setString(10, state);
				stmt.setString(11, emp_post);
				stmt.setString(12, username);
				stmt.setString(13, password);
				
				stmt.executeUpdate();
			}
			catch(Exception E)
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
			return true;
		}
		else
		{
			return false;
		}
    }

	@FXML
    private void handle_btn_cancel()
    {
    	stage.close();
    }
    
    private boolean isValid() 
    {
    	boolean retValue = true;
    	
        String pass = emp_password.getText();
        String pass_confirm = emp_password_confirm.getText();

        if(!pass.equals(pass_confirm))
        {
           password_label.setText("*Passwords do not match"); 
           retValue = false;
        }
        
        int len = emp_contact_no.getText().length();
        
        if(!(len==10))
        {
             contact_no_label.setText("*Enter 10 digits Mobile number"); 
             retValue = false;
        }
        
        String firstName = emp_name.getText();
        String userName = emp_username.getText();
        String stateName = (String) emp_state.getValue();
        String cityName = emp_city.getText();
        String addressVal = emp_address.getText();
        
        String categoryName = (String) emp_category.getValue();
        LocalDate dateOfJoin = emp_date_of_joining.getValue();
        
        String birthDate = emp_date_birth.getValue().toString();
        
        String salaryVal = emp_salary.getText();
        
        if(userName.equals("") || firstName.equals("") || stateName.equals("") || cityName.equals("") || addressVal.equals("") || categoryName.equals("") || dateOfJoin.equals("") || birthDate.equals("") || salaryVal.equals(""))
        {
        	retValue = false;
        	Dialogs.create()
        		.owner(stage)
        		.title(" ALERT ")
        		.masthead(" Missing out something ")
        		.message("Mandatory field is empty ")
        		.showWarning();
        }
        if(pass.equals("") && mode == ADD)
        {
        	retValue = false;
        }
       
        return retValue;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
    	img_add_user = new Image("/Resources/add-user-icon.png");
    }

	public void setStage(Stage dialogStage) 
	{
		this.stage = dialogStage;
	}

	public void setEmployee(Employee_Info employee_info, String mode)
	{
		
		if(mode.equals("ADD"))
		{
			this.mode = ADD;
		}
		else
		{
			this.mode = EDIT;
			System.out.println("Hello1");
			this.employee_info = employee_info;
			if(employee_info == null)
			{
				System.out.println("WTF");
			}
			System.out.println(employee_info.getFirst_name());
			emp_name.setText(employee_info.getFirst_name().getValue());
			
			emp_username.setText(employee_info.getUsername().getValue());
			emp_password.setText(employee_info.getPassword().getValue());
			
			emp_salary.setText(employee_info.getSalary().getValue());
			emp_category.setValue(employee_info.getCategory().getValue());
						
			emp_marital_status.setValue(employee_info.getMarital_status().getValue());
			emp_contact_no.setText(employee_info.getContact_no().getValue());
			emp_state.setValue(employee_info.getState().getValue());
			emp_city.setText(employee_info.getCity().getValue());
			emp_address.setText(employee_info.getAddress().getValue());
			
			emp_date_birth.setValue(LocalDate.parse(employee_info.getBirth_date().getValue()));
			emp_date_of_joining.setValue(employee_info.getDate_of_joining());
		}
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

	public boolean isSaveClicked() 
	{
		return false;
	} 
}
