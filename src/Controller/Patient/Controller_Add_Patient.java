package Controller.Patient;

import java.net.URL;
import java.util.ResourceBundle;

import Model.Patient.Patient_Info;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import org.controlsfx.dialog.Dialogs;

import application.Main;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;

@SuppressWarnings("deprecation")
public class Controller_Add_Patient implements Initializable  
{
	private Patient_Info pat_info, original_pat_info;
	private Stage stage;
	private boolean isDone = false;
	private int ADD=0, EDIT=1, OTHER=2;
	private int mode = OTHER;
  
    public static String select;
    @FXML
    private Label label;
    @FXML
    public TextField textfirst;
    @FXML
    private TextField textmiddle;
    @FXML
    private Button btn_save;
    @FXML
    private Label textID;
    @FXML
    public TextField textlast;
    @FXML
    private Button btn_cancel;
    @FXML
    private Button btn_clear;
    @FXML
    private DatePicker textbirth;
    @FXML
    private TextField textaddress;
    @FXML
    private TextField textemerphone;
    @FXML
    private DatePicker textdate;
    @FXML
    private TextField textphone;
    @FXML
    private RadioButton textmale;
    @FXML
    private RadioButton textfemale;
    @FXML
     public TextField textemer;
    @FXML
    private TextField textrel;
    @FXML
    private TextField textstate;
    @FXML
    private Image img_logo;
    @FXML
    private TextField textcity;
    @FXML
    private ComboBox textmarital;
    @FXML
    private ListView list = new ListView();
   
    @FXML
    
    private void handle_btn_save(ActionEvent event) throws Exception
    {
    	if(isValid())
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
			PreparedStatement stmt = null;
			try
			{
				
				String ID = generateID(stmt, con);
			 	textID.setText(ID);

			 	String c;
			    c = textfirst.getText() + " " + textmiddle.getText() + " " + textlast.getText();
			 	
			 	String address=textaddress.getText();
			 	String phone=textphone.getText();
			 	String Emergency= textemer.getText();
			 	String relation=textrel.getText();
			 	String state=textstate.getText();
			 	String city=textcity.getText();
			 	String emerphone=textemerphone.getText();
			 	String marital=(String) textmarital.getValue();
			 	String sex;
			 	if(textmale.isSelected()== true)
			 	{
			 		sex="Male";
			 	}
			 	else
			 	{
			 		sex="Female";
			 	}
				
				if(mode == ADD)
				{
				 	String query="insert into Patient values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
					stmt = con.prepareStatement(query);
					stmt.setString(1, ID);
					stmt.setString(2, c);
					stmt.setString(3, textbirth.getValue().toString());
					stmt.setString(4, marital);
					stmt.setString(5, emerphone);
					stmt.setString(6, Emergency);
					stmt.setString(7, relation);
					stmt.setString(8, address);
					stmt.setString(9, city);
					stmt.setString(10, state);
					stmt.setString(11, phone);
					stmt.setString(12, sex);
					stmt.executeUpdate();
					
		    		isDone = true;
	
		    		original_pat_info.setFirst_name(new SimpleStringProperty(c));
		    		original_pat_info.setAddress(new SimpleStringProperty(address));
		    		original_pat_info.setState(new SimpleStringProperty(state));
		    		original_pat_info.setCity(new SimpleStringProperty(city));
		    		original_pat_info.setSex(new SimpleStringProperty(sex));
		    		original_pat_info.setEmergency_contact(new SimpleStringProperty(emerphone));
		    		original_pat_info.setEmergency_name(new SimpleStringProperty(Emergency));
		    		original_pat_info.setEmergency_relation(new SimpleStringProperty(relation));
		    		original_pat_info.setBirth_date(textbirth.getValue());
		    		original_pat_info.setMarital_status(new SimpleStringProperty(marital));
		    		original_pat_info.setPhone(new SimpleStringProperty(phone));
				}
				else
				{
					String query="update Patient set pat_id=?, name=?, Birth_date=?, Marital_status=?, Emergency_contact=?, Emergency_name=?, Emergency_relation=?, Address=?, City=?, State=?, Phone=?,  Sex=?;";
					stmt = con.prepareStatement(query);
					stmt.setString(1, ID);
					stmt.setString(2, c);
					stmt.setString(3, textbirth.getValue().toString());
					stmt.setString(4, marital);
					stmt.setString(5, emerphone);
					stmt.setString(6, Emergency);
					stmt.setString(7, relation);
					stmt.setString(8, address);
					stmt.setString(9, city);
					stmt.setString(10, state);
					stmt.setString(11, phone);
					stmt.setString(12, sex);
					stmt.executeUpdate();
					
		    		isDone = true;
	
		    		original_pat_info.setFirst_name(new SimpleStringProperty(c));
		    		original_pat_info.setAddress(new SimpleStringProperty(address));
		    		original_pat_info.setState(new SimpleStringProperty(state));
		    		original_pat_info.setCity(new SimpleStringProperty(city));
		    		original_pat_info.setSex(new SimpleStringProperty(sex));
		    		original_pat_info.setEmergency_contact(new SimpleStringProperty(emerphone));
		    		original_pat_info.setEmergency_name(new SimpleStringProperty(Emergency));
		    		original_pat_info.setEmergency_relation(new SimpleStringProperty(relation));
		    		original_pat_info.setBirth_date(textbirth.getValue());
		    		original_pat_info.setMarital_status(new SimpleStringProperty(marital));
		    		original_pat_info.setPhone(new SimpleStringProperty(phone));		
				}
	    		stage.close();
			}
			catch(SQLException E)
			{
				Dialogs.create()
	    		.owner(stage)
	    		.title(" ALERT ")
	    		.masthead(" SQlException encountered ")
	    		.message("Item could not be added ")
	    		.showWarning();
			}        
    	}
    }
    
    private String generateID(PreparedStatement stmt, Connection con)
    {
    	LocalDate date=textbirth.getValue();
	    LocalDate date1=textdate.getValue();
	    String c;
	    String s=textmiddle.getText();
	  	String[] ary= s.split("");
	  	String s1=textfirst.getText();
	  	String[] ary1= s1.split("");
  		String s2=textlast.getText();
	  	String[] ary2= s2.split("");
	  	c=s1+" "+s2+" "+s;
	  	int birthdd=date.getDayOfMonth();
	 	int datt=date1.getDayOfMonth();
	 	String ID=ary1[0].concat(ary[0]).concat(ary2[0])+birthdd+datt;
	 	System.out.println("ID is: " + ID);
	 	int flag = 0, j = 0;
	 	String ID1 = ID;
	 	while(flag == 0)
	 	{
	 		try
		 	{
	 			String query = "SELECT pat_id FROM Patient WHERE pat_id=?;";
		 		stmt = con.prepareStatement(query);
		 		stmt.setString(1, ID);
		 		ResultSet rs = stmt.executeQuery();
		 		int i = 0;
		 		while(rs.next())
		 		{
		 			ID1 = ID + "_" + j;
		 			j += 1;
		 			i = 1;
		 			break;
		 		}
		 		if(i == 0)
		 		{
		 			flag = 1;
		 			ID = ID1;
		 		}
		 	}
		 	catch(SQLException E)
		 	{
		 		System.out.println("Can't retrieve the patients");
		 	}
	 	}
	 	System.out.println("ID generated..." + ID);
	 	return ID;
    }
    
    @FXML
    private void handle_btn_cancel(ActionEvent event) throws Exception
    {
    	stage.close();
    }

    @FXML
    private void handle_btn_clear(ActionEvent event) throws Exception
    {
       textfirst.setText(null);
       textmiddle.setText(null);
       textlast.setText(null);
       textaddress.setText(null);
       textphone.setText(null);
       textemer.setText(null);
       textrel.setText(null);
       textstate.setText(null);
       textcity.setText(null);
       textdate.setValue(null);
       textbirth.setValue(null);
       textID.setText(null);
       textemerphone.setText(null);
       textmarital.getItems().clear();   
    } 

    @FXML
	private void handleButtonAction3() 
	{
       textmarital.getItems().addAll("Single","Married");
	}

    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
    	textdate.setEditable(false);
        handleButtonAction3();
        img_logo=new Image("/Resources/img_logo.png");
    }
    
    public boolean isValid()
	{
		boolean retValue = true;
		LocalDate date=textbirth.getValue();
	    LocalDate date1=textdate.getValue();
	    String c;
	    String s=textmiddle.getText();
	  	String[] ary= s.split("");
	  	String s1=textfirst.getText();
	  	String[] ary1= s1.split("");
  		String s2=textlast.getText();
	  	String[] ary2= s2.split("");
	  	c=s1+" "+s2+" "+s;
	  	int birthdd=date.getDayOfMonth();
	 	int datt=date1.getDayOfMonth();
	 	String ID=ary1[0].concat(ary[0]).concat(ary2[0])+birthdd+datt;
	 	textID.setText(ary1[0].concat(ary[0]).concat(ary2[0])+birthdd+datt);
     
	 	String address=textaddress.getText();
	 	String phone=textphone.getText();
	 	String Emergency= textemer.getText();
	 	String relation=textrel.getText();
	 	String state=textstate.getText();
	 	String city=textcity.getText();
	 	String emerphone=textemerphone.getText();
	 	String marital=(String) textmarital.getValue();
	 	String sex;
	 	if(textmale.isSelected()== true)
	 	{
	 		sex="Male";
	 	}
	 	else
	 	{
	 		sex="Female";
	 	}
	 	
	 	String errorMsg = "";
	 	
	 	if(s1.length() == 0)
	 	{
	 		retValue = false;
	 		errorMsg += "Enter a valid Name\n";
	 	}
	 	
	 	if(address.length() == 0)
	 	{
	 		retValue = false;
	 		errorMsg += "Enter a valid address\n";
	 	}
	 	
	 	if(phone.length() == 0)
	 	{
	 		retValue = false;
	 		errorMsg += "Enter a valid phone number\n";
	 	}
	 	
	 	if(state.length() == 0)
	 	{
	 		retValue = false;
	 		errorMsg += "Enter a valid State\n";
	 	}
	 	
	 	if(city.length() == 0)
	 	{
	 		retValue = false;
	 		errorMsg += "Enter a valid city\n";
	 	}
	 	
	 	if(sex.length() == 0)
	 	{
	 		retValue = false;
	 		errorMsg += "Please select a valid sex\n";
	 	}
	 	
	 	if(!retValue)
	 	{
	 		Dialogs.create()
    		.owner(stage)
    		.title(" ALERT ")
    		.masthead(" Invalid Fields ")
    		.message(errorMsg)
    		.showWarning();
	 	}
	 	
		return retValue;
	}
   
    public boolean isSaveClicked()
	{
		return isDone;
	}
	
	public void setStage(Stage stage)
	{
		this.stage = stage;
	}
	
	public void setPatient(Patient_Info pat_info, String mode)
	{
		this.original_pat_info = pat_info;
		this.pat_info = Patient_Info.clone(pat_info);
		if(mode.equals("ADD"))
		{
			this.mode = ADD;
		}
		else
		{
			this.mode = EDIT;
		}
	   
	    label.setText("");
	    textID.setText(pat_info.getPat_id().getValue());
	    String name = pat_info.getFirst_name().getValue();
	    String name_part[] = name.split("\\s+");
	    if(name_part.length == 3)
	    {
	    	textfirst.setText(name_part[0]);
	    	textmiddle.setText(name_part[1]);
	    	textlast.setText(name_part[2]);
	    }
	    if(name_part.length == 2)
	    {
	    	textfirst.setText(name_part[0]);
	    	textlast.setText(name_part[1]);
	    }
	    if(name_part.length == 1)
	    {
	    	textfirst.setText(name_part[0]);
	    }
	    textbirth.setValue(pat_info.getBirth_date());
	    textaddress.setText(pat_info.getAddress().getValue());
	    textemerphone.setText(pat_info.getPhone().getValue());
	    textemer.setText(pat_info.getEmergency_name().getValue());
	    textrel.setText(pat_info.getEmergency_relation().getValue());
	    textdate.setValue(LocalDate.now());
	    textphone.setText(pat_info.getPhone().getValue());
	    String gender = pat_info.getSex().getValue();
	    if(gender.equals("Male"))
	    {
	    	textmale.selectedProperty().set(true);
	    }
	    else
	    {
	    	textfemale.selectedProperty().set(true);
	    }
	    textstate.setText(pat_info.getState().getValue());
	    textcity.setText(pat_info.getCity().getValue());
	    textmarital.setValue(pat_info.getMarital_status().getValue());
	}  
}