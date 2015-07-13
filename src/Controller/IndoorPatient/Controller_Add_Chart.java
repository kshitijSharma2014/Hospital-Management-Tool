package Controller.IndoorPatient;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import org.controlsfx.dialog.Dialogs;

import application.Main;
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
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author gunjan
 */
@SuppressWarnings("deprecation")
public class Controller_Add_Chart implements Initializable 
{
    @FXML
    private Label med_id;
    @FXML
    private Label med_name;
    @FXML
    private Label med_comp;
    @FXML
    private Label med_detail;
    @FXML
    private ObservableList<String> meddata = FXCollections.observableArrayList();
    @FXML
    private ListView<String> ls;
    @FXML
    private DatePicker textDate;
    @FXML
    private TextField textTime;
    @FXML
    private TextField textMed;
    @FXML
    private TextField textDosage;
    @FXML
    private TextField textRemark;
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @FXML 
    private Button buttonBack;
    @FXML
    private Button buttonSave;
     @FXML
    ObservableList<String> sugg = FXCollections.observableArrayList();
    @FXML
    private void goHome(ActionEvent e) throws IOException
    {
        home();
    }
    public void saveChart() throws IOException, SQLException 
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
			String date =  Integer.toString(textDate.getValue().getYear()) + "-" + Integer.toString(textDate.getValue().getMonthValue()) + "-" + Integer.toString(textDate.getValue().getDayOfMonth());
			String[] arr = textMed.getText().split(" ");
	    	System.out.println(arr[0]);
	    	String query = "Insert Into Medication values (?, ?, ?, ?, ?, ?);";
	        stmt = con.prepareStatement(query);
	        stmt.setString(1, HomeController.pat_info.getPat_id().getValue());
	        stmt.setString(2, date);
	        stmt.setString(3, textTime.getText());
	        stmt.setString(4, arr[1]);
	        stmt.setString(5, textDosage.getText());
	        stmt.setString(6, textRemark.getText());
        	
	        int no = stmt.executeUpdate();
        	System.out.println("No of updated rows: " + no);
	        home();
	        System.out.println("wtf");
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
		return ;        
    }
    public void home() throws IOException
    {
        Parent home = FXMLLoader.load(getClass().getResource("/View/IndoorPatient/chart.fxml"));
        Scene scene_home = new Scene(home);
        Stage stage_home = (Stage) buttonBack.getScene().getWindow();
        stage_home.setScene(scene_home);
        stage_home.show();
    }
   
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        ls.setVisible(false);
        textDate.setPromptText("Choose Date");
        textMed.setPromptText("Enter Medicine Name");
        textTime.setPromptText("HH:MM:SS");
        addHandlers();
        
       
    }
    public void addHandlers()
    {
        textMed.textProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) 
            {
                meddata.clear();
                   
                if(textMed.getText().length() >2)
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
            			ls.setVisible(true);
                        String query = "select medicine_name, medicine_ID from Medicine where medicine_name like '%?%'";
                        stmt = con.prepareStatement(query);
                        stmt.setString(1, newValue);
                        ResultSet rs = stmt.executeQuery();
                        while(rs.next())
                        {
                            String id = rs.getString("medicine_ID");
                            meddata.add(rs.getString("medicine_name") + "  {" + id + "}");
                        }
                        ls.setItems(meddata);
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
            
        	ls.setOnMouseClicked(new EventHandler<MouseEvent>() 
            	{

		           @Override
		           public void handle(MouseEvent event) 
		           {
		               if(event.getClickCount() == 1)
		               {
		                   
		               }
		               textMed.setText(ls.getSelectionModel().getSelectedItem().toString());
		               ls.setVisible(false);
		           }
            	});

    }
    
    
}

