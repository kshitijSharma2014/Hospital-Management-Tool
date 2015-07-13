package Controller.AdmitPatient;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.controlsfx.dialog.Dialogs;

import Controller.CMS.Controller_Manage_Database;
import application.Main;

/**
 *
 * @author gunjan
 */

@SuppressWarnings("deprecation")
public class TestReportsController implements Initializable
{
    @FXML
    private TableView<test_info> tableChart = new TableView<TestReportsController.test_info>();
    @FXML
    private TableColumn<test_info,String> test_name = new TableColumn<TestReportsController.test_info, String>();
    @FXML
    private TableColumn<test_info,String> test_detail = new TableColumn<TestReportsController.test_info, String>();
    @FXML
    final ObservableList<test_info> data=FXCollections.observableArrayList();
    @FXML
    private Label labelMSG;
    @FXML
    private Button buttonBack;
    @FXML
    private Button buttonSave;
    @FXML
    private Button buttonDelete;
    @FXML
    private Button buttonNew;
    @FXML
    private final Node rootChart = new ImageView( new Image(getClass().getResourceAsStream("/Resources/chart.png")));
    
    private TreeItem<String> root = new TreeItem<> ("All Reports" , rootChart);
    TreeItem<String> date = new TreeItem<String>("");
    TreeItem<String> time = new TreeItem<String> ("");
    
    @FXML
    private TreeView<String> treeviewDate;  
    @FXML
    public Stage newStage;
    @FXML
    public void deleteReport(ActionEvent e)
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
			String query = "select test_ID from Test_Info where test_name = ?;";
			stmt = con.prepareStatement(query);
			stmt.setString(1, Controller_Indoor_Patient.testName);
			ResultSet rs = stmt.executeQuery();
			while(rs.next())
            {
                String q2 = "delete from Patient_Tests where pat_ID = ? and date = ? and time = ? and test_id = ?";
                stmt = con.prepareStatement(q2);
                stmt.setString(1, Controller_Indoor_Patient.pat_info.getPat_id().getValue());
                stmt.setString(2, Controller_Indoor_Patient.dateID);
                stmt.setString(3, Controller_Indoor_Patient.timeID);
                stmt.setString(4, rs.getString("test_ID"));
                handle();
                int no = stmt.executeUpdate();
                System.out.println("No of rows affected: " + no);
                getDates();
            }
			
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
    
    @FXML
    public void goHome(ActionEvent e) throws IOException
    {
    	FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/View/AdmitPatient/Search_Indoor_Patient.fxml"));
        AnchorPane anchor_pane = loader.load();
        Main.getRootLayout().setCenter(anchor_pane);
    }
    
    @FXML
    public void newEntry(ActionEvent e) throws IOException
    {
    	FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/View/AdmitPatient/AddTestReport.fxml"));
        AnchorPane anchor_pane = loader.load();
        Main.getRootLayout().setCenter(anchor_pane);
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {    
        treeviewDate.setRoot(root);
        addHandlers();
        getDates();
    }    
    
    public void addHandlers()
	{
    	tableChart.setEditable(true);
    	labelMSG.setVisible(false);
    	test_name.setCellFactory(TextFieldTableCell.forTableColumn());
    	test_detail.setCellFactory(TextFieldTableCell.forTableColumn());
    	test_name.setCellValueFactory(new PropertyValueFactory<>("test_name"));
    	test_detail.setCellValueFactory(new PropertyValueFactory<>("test_detail"));
    	test_name.setVisible(true);
    	test_detail.setVisible(true);
    	treeviewDate.setOnMouseClicked(new EventHandler<javafx.scene.input.MouseEvent>()
    			{
    				@Override
    				public void handle(javafx.scene.input.MouseEvent mouseEvent)
    				{            
    					if(mouseEvent.getClickCount() == 2)
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
    							data.clear();
        						
    							Controller_Indoor_Patient.timeID = treeviewDate.getSelectionModel().getSelectedItem().getValue();
    							Controller_Indoor_Patient.dateID = treeviewDate.getSelectionModel().getSelectedItem().getParent().getValue();
        						System.out.println(Controller_Indoor_Patient.timeID + Controller_Indoor_Patient.dateID);
        						String q = "Select test_ID,test_value  from Patient_Tests where pat_ID = ? and date = ? and time = ?;" ;
        						stmt = con.prepareStatement(q);
        						stmt.setString(1, Controller_Indoor_Patient.pat_info.getPat_id().getValue());
        						stmt.setString(2, Controller_Indoor_Patient.dateID);
        						stmt.setString(3, Controller_Indoor_Patient.timeID);
        						ResultSet rs = stmt.executeQuery();
    							while(rs.next())
    							{
    								String q2 = "select test_name from Test_Info where test_id = ?";
    								stmt = con.prepareStatement(q2);
    								stmt.setString(1, rs.getString("test_ID"));
    								ResultSet rs2 = stmt.executeQuery();
    								while(rs2.next())
    								{
    									test_info t = new test_info(rs2.getString("test_name"), rs.getString("test_value"));
    									data.add(t);
    									System.out.println(data.get(0).getTest_name());
    								}
    							}
    							tableChart.setItems(data);        						
    						}
    						catch(SQLException E)
    						{
    							
    							return ;
    						}    	
    					}
    				}	
    			});
             
        treeviewDate.setRoot(root);
        tableChart.setOnMouseClicked(new EventHandler<MouseEvent>()
          	{
				@Override
				public void handle(MouseEvent event) 
				{
				    if(event.getClickCount() >= 1)
				    {
				       test_info t = tableChart.getSelectionModel().getSelectedItem();
				       Controller_Indoor_Patient.testName = t.test_name;
				    }
				    
				}
		    });
        test_detail.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<test_info, String>>() 
        	{

                @Override
                public void handle(TableColumn.CellEditEvent<test_info, String> event) 
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
						String q = "select test_ID from Test_Info where test_name = ?";
						stmt = con.prepareStatement(q);
						stmt.setString(1, Controller_Indoor_Patient.testName);
						ResultSet rs = stmt.executeQuery();
                        while(rs.next())
                        {
                        	String q2 = "update Patient_Tests set test_value = ? where pat_ID = ? and date = ? and time = ? and test_ID = ?;";
                        	stmt = con.prepareStatement(q2);
                        	stmt.setString(1, event.getNewValue());
                        	stmt.setString(2, Controller_Indoor_Patient.pat_info.getPat_id().getValue());
                        	stmt.setString(3, Controller_Indoor_Patient.dateID);
                        	stmt.setString(4, Controller_Indoor_Patient.timeID);
                        	stmt.setString(5, rs.getString("test_ID"));
                            int no = stmt.executeUpdate();
                            System.out.println("No of rows affected: " + no);
                        }
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
        	});
             
		}
        
       
        
        public void getDates()
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
				refreshtree();
                System.out.println("logical refersh");
                String query = "select distinct date from Patient_Tests where pat_ID = ?;";
                stmt = con.prepareStatement(query);
                stmt.setString(1, Controller_Indoor_Patient.pat_info.getPat_id().getValue());
                ResultSet rs = stmt.executeQuery();
                
                while(rs.next())
                {
                   TreeItem<String> test = new TreeItem<String>(rs.getString("date"));
                   root.getChildren().add(test);
                   String q2 = "Select distinct time from Patient_Tests where pat_ID = ? and date = ?;";
                   stmt = con.prepareStatement(q2);
                   stmt.setString(1,  Controller_Indoor_Patient.pat_info.getPat_id().getValue());
                   stmt.setString(2, rs.getString("date"));
                   ResultSet rs1 = stmt.executeQuery();
                   while(rs1.next())
                   {
                       TreeItem<String> time = new TreeItem<String>(rs1.getString("time"));
                       test.getChildren().add(time);
                   }
                }
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
 
        
public static class test_info
{
    public test_info(String a, String b)
    {
      this.test_name = a;
      this.test_detail = b;
    }
    String test_name;
    String test_detail;

        public String getTest_name() {
            return test_name;
        }

        public void setTest_name(String test_name) {
            this.test_name = test_name;
        }

        public String getTest_detail() {
            return test_detail;
        }

        public void setTest_detail(String test_detail) {
            this.test_detail = test_detail;
        }
    
}
 public void handle() {
            
            int ix = tableChart.getSelectionModel().getSelectedIndex();

            test_info t = (test_info) tableChart.getSelectionModel().getSelectedItem();
           
            data.remove(ix);

            if (ix != 0) {

                ix = ix -1;

            }
            tableChart.requestFocus();
            tableChart.getSelectionModel().select(ix);

            tableChart.getFocusModel().focus(ix);

        } 
 public void refreshtree()
 {
        treeviewDate.setVisible(false);
        treeviewDate.setVisible(true);
 }
}