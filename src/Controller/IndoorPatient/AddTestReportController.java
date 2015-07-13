/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller.IndoorPatient;

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
import javafx.stage.Stage;

import org.controlsfx.dialog.Dialogs;

import application.Main;

/**
 *
 * @author gunjan
 */
@SuppressWarnings("deprecation")
public class AddTestReportController implements Initializable 
{
    @FXML
    private ListView<String> ls;
    @FXML
    ObservableList<String> testdata = FXCollections.observableArrayList();
    @FXML
    private DatePicker textDate;
    @FXML
    private TextField textTime;
    @FXML
    private TextField textName;
    @FXML
    private TextArea textDetail;
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
    	//TODO
        textDate.setPromptText("Choose Date");
        textName.setPromptText("Enter Test Name");
        textDetail.setPromptText("Enter Test Report Details ...");
        ls.setVisible(false);
        addHandlers();
    } 
   
    @FXML
    public void saveEntry(ActionEvent e) throws IOException
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
            while(rs.next())
            {
                String q1 = "Insert into Patient_Tests values(?, ?, ?, ?, ?);";
                stmt = con.prepareStatement(q1);
                stmt.setString(1, rs.getString("test_ID"));
                stmt.setString(2, HomeController.pat_info.getPat_id().getValue());
                stmt.setString(3, textDetail.getText());
                stmt.setString(4, date);
                stmt.setString(5, textTime.getText());
                int no = stmt.executeUpdate();
                System.out.println("No of rows affected: " + no);
            }
            home();
		}
		catch(SQLException E)
		{
			Dialogs.create()
    		.title(" ALERT ")
    		.masthead(" SQlException encountered ")
    		.message("Items could not be deleted ")
    		.showWarning();
			return ;
		}
    }
    public void home() throws IOException
    {
        Parent home = FXMLLoader.load(getClass().getResource("TestReports.fxml"));
        Scene scene_home = new Scene(home);
        Stage stage_home = (Stage) buttonCancel.getScene().getWindow();
        stage_home.setScene(scene_home);
        stage_home.show();
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
    					String query = "select test_name from Test_Info where test_name like '%?%'";
    					stmt = con.prepareStatement(query);
    					stmt.setString(1, newValue);
    					ResultSet rs = stmt.executeQuery();
                        while(rs.next())
                        {
                            testdata.add(rs.getString("test_name"));
                        }
                        ls.setItems(testdata);
    				}
    				catch(SQLException E)
    				{
    					Dialogs.create()
    		    		.title(" ALERT ")
    		    		.masthead(" SQlException encountered ")
    		    		.message("Items could not be deleted ")
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
