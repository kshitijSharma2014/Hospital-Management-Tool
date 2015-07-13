/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller.AdmitPatient;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import org.controlsfx.dialog.Dialogs;

import com.oracle.javafx.scenebuilder.kit.editor.panel.util.dialog.TextViewDialog;

import application.Main;

/**
 *
 * @author gunjan
 */
@SuppressWarnings("deprecation")
public class AddTestReportController implements Initializable 
{
    @FXML
    private ListView<String> ls = new ListView<String> ();
    @FXML
    ObservableList<String> testdata = FXCollections.observableArrayList();
    @FXML
    private DatePicker textDate;
    @FXML
    private TextField textTime;
    @FXML
    private TextField textName;
    @FXML
    private TextField testdetail = new TextField();
    @FXML
    private Button buttonSave;
    @FXML
    private Button buttonCancel;
    @FXML
    private void goHome(ActionEvent e) throws IOException
    {
    	home();   
    }
    
    public void initialize(URL url, ResourceBundle rb) 
    {
        textDate.setPromptText("Choose Date");
        textName.setPromptText("Enter Test Name");
        testdetail.setPromptText("Enter Test Report Details ...");
        ls.setVisible(false);
        addHandlers();
    } 
   
    @FXML
    public void saveEntry(ActionEvent e) throws IOException
    {
    	if(!isValid())
    	{
    		return ;
    	}
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
    		.title(" ALERT ")
    		.masthead(" Database is not setup ")
    		.message("Please set up the connection ")
    		.showWarning();
			return ;
		}
		PreparedStatement stmt = null;
		try
		{
			System.out.println( "wtf");
	        String date =  Integer.toString(textDate.getValue().getYear()) + "-" + Integer.toString(textDate.getValue().getMonthValue()) + "-" + Integer.toString(textDate.getValue().getDayOfMonth());
	        String q = "select test_ID from Test_Info where test_name = ?";
	        stmt = con.prepareStatement(q);
	        stmt.setString(1, textName.getText());
	        ResultSet rs = stmt.executeQuery();
	        int flag = 0;
            while(rs.next())
            {
            	flag = 1;
                String q1 = "Insert into Patient_Tests values(?, ?, ?, ?, ?);";
                stmt = con.prepareStatement(q1);
                stmt.setString(2, rs.getString("test_ID"));
                stmt.setString(1, Controller_Indoor_Patient.pat_info.getPat_id().getValue());
                System.out.println("Detail: " + testdetail.getText());
                stmt.setString(3, testdetail.getText());
                stmt.setString(4, date);
                stmt.setString(5, textTime.getText());
                System.out.println("executing..................");
                int no = stmt.executeUpdate();
                System.out.println("No of rows affected: " + no);
            }
            if(flag == 0)
            {
            	Dialogs.create()
        		.title(" ALERT ")
        		.masthead(" Error ")
        		.message("Test name is not a valid test ")
        		.showWarning();
    			return ;
            }
            home();
		}
		catch(SQLException E)
		{
			E.printStackTrace();
			Dialogs.create()
    		.title(" ALERT ")
    		.masthead(" SQlException encountered ")
    		.message("Items could not be saved ")
    		.showWarning();
			return ;
		}
    }
    private boolean isValid() 
    {
    	boolean retValue = true;
    	String errorMsg = "";
    	
    	if(textDate.getValue() == null)
    	{
    		errorMsg += "Please enter a date..\n";
    		retValue = false;
    	}
    	if(textTime.getText() == "")
    	{
    		errorMsg += "Please enter a time..\n";
    		retValue = false;
    	}
    	if(testdetail.getText() == "")
    	{
    		errorMsg += "Please enter a test value..\n";
    		retValue = false;
    	}
    	if(textName.getText() == "")
    	{
    		errorMsg += "Please enter a test name..\n";
    		retValue = false;
    	}
    	if(!retValue)
    	{
    		Dialogs.create()
    		.title(" ALERT ")
    		.masthead(" Error  ")
    		.message(errorMsg)
    		.showWarning();
    	}
		return retValue;
	}

	public void home() throws IOException
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/View/AdmitPatient/TestReports.fxml"));
        AnchorPane anchor_pane = loader.load();
        Main.getRootLayout().setCenter(anchor_pane);
    }

    public void addHandlers()
    {
    	textName.textProperty().addListener(new ChangeListener<String>()
    	{
    		@Override
    		public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) 
    		{
    			testdata.clear();
    			ls.setVisible(true);
    			if(textName.getText().length() >2)
    			{
    				Connection con = Main.getConnection();
    				if(con == null)
    				{
    					Dialogs.create()
    		    		.title(" ALERT ")
    		    		.masthead(" Database is not setup ")
    		    		.message("Please set up the connection ")
    		    		.showWarning();
    					return ;
    				}
    				PreparedStatement stmt = null;
    				try
    				{
    					System.out.println("llllllll");
    					System.out.println(newValue);
    					String query = "select test_name from Test_Info where test_name like '%" + newValue + "%'";
    					stmt = con.prepareStatement(query);
    					System.out.println("Hello111");
    					ResultSet rs = stmt.executeQuery();
    					System.out.println("geee");
                        while(rs.next())
                        {
                        	System.out.println("hello");
                            testdata.add(rs.getString("test_name"));
                        }
                        ls.setItems(testdata);
    				}
    				catch(SQLException E)
    				{
    					Dialogs.create()
    		    		.title(" ALERT ")
    		    		.masthead(" SQlException encountered ")
    		    		.message("Items could not be added ")
    		    		.showWarning();
    					return ;
    				}
               }
    		}
    	});
      ls.setOnMouseClicked(new EventHandler<MouseEvent>() {

               @Override
               public void handle(MouseEvent event) {
                textName.setText(ls.getSelectionModel().getSelectedItem().toString());
               ls.setVisible(false);
               }
           });
             
         }
    
}
