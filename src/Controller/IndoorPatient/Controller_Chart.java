package Controller.IndoorPatient;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import org.controlsfx.dialog.Dialogs;

import Controller.AdmitPatient.Controller_Admit_Patient;
import Controller.AdmitPatient.Controller_Indoor_Patient;
import application.Main;
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
import javafx.scene.control.TableCell;
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
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author gunjan
 */
@SuppressWarnings("deprecation")
public class Controller_Chart implements Initializable 
{
    

    /*
    make changes between 2 comments
    */
    @FXML
    private final Node rootChart = new ImageView( new Image(getClass().getResourceAsStream("/Resources/chart.png")));
    private TreeItem<String> root = new TreeItem<String> ("All Charts" , rootChart);
    TreeItem<String> date = new TreeItem<String>("");
    TreeItem<String> time = new TreeItem<String> ("");
 
    @FXML
    private TableColumn<Meds,String> columnName;
    @FXML
    private TableColumn<Meds,String> columnDosage;
    @FXML
    private TableColumn<Meds,String> columnRemark;
    @FXML
    private TableCell name;
    @FXML
    private TableCell dosage;
    @FXML
    private TableCell remark;
    
    public Stage newStage;
    @FXML
    private Button buttonNew;
    @FXML
    private Button buttonSave;
    @FXML
    private Button buttonDelete;
    @FXML
    private Button buttonBack;
    @FXML
    private Label labelPatient;
    @FXML
    private TreeView<String> treeviewDate;
    @FXML
    private TableView<Meds> tableChart;
    @FXML
    private HBox hboxMenu;
    @FXML
    final ObservableList<Meds> data=FXCollections.observableArrayList();
    @FXML
    private void newEntry(ActionEvent e) throws IOException 
    {
    	FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("/View/AdmitPatient/addChart.fxml"));
		AnchorPane page = (AnchorPane) loader.load();
		Stage dialogStage = new Stage();
		dialogStage.setTitle("Admit Medications");
		dialogStage.initModality(Modality.WINDOW_MODAL);
		dialogStage.initOwner(buttonNew.getScene().getWindow());
		Scene scene = new Scene(page);
		dialogStage.setScene(scene);
		System.out.println("Hi!!\n");
		dialogStage.showAndWait();
	}

    @FXML
    private void deleteEntry(ActionEvent e) throws IOException
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
			String q2 = "Select medicine_ID from Medicine where medicine_name = ?";
			stmt = con.prepareStatement(q2);
			stmt.setString(1, tableChart.getSelectionModel().getSelectedItem().getMedname());
			ResultSet rs = stmt.executeQuery();
			while(rs.next())
            {
	            String query = "Delete From Medication Where pat_ID = ? and date = ? and time = ? and medicine_ID = ?;";
	            stmt = con.prepareStatement(query);
	            stmt.setString(1, HomeController.pat_info.getPat_id().getValue());
	            stmt.setString(2, HomeController.dateID);
	            stmt.setString(3, HomeController.timeID);
	            stmt.setString(4, rs.getString("medicine_ID"));
	            handle();
	            int no = stmt.executeUpdate();
	            System.out.println("Number of affected rows: " + no);
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
		return ;
    }
    @FXML
    private void goHome(ActionEvent e) throws IOException
    {
    	FXMLLoader loader = new FXMLLoader();
		System.out.println("1");
		loader.setLocation(Main.class.getResource("/View/AdmitPatient/Search_Indoor_Patient.fxml"));
		System.out.println("2");
		AnchorPane anchor_pane = (AnchorPane) loader.load();
		Main.getRootLayout().setCenter(anchor_pane);        
    }

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
   
  
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        
        treeviewDate.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent mouseEvent)
            {            
                if(mouseEvent.getClickCount() == 2)
                {
                    
                    data.clear();
                  //  tableChart.setItems(data);
                    TreeItem<String> selected_time = treeviewDate.getSelectionModel().getSelectedItem();
                    TreeItem<String>  selected_date = selected_time.getParent();
                    HomeController.dateID = selected_date.getValue();
                    HomeController.timeID = selected_time.getValue();
                    
                    
                    
                    
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
            			String q = "Select medicine_ID,amount,remark  from Medication where pat_ID = ? and date = ? and time = ?;" ;
            			stmt = con.prepareStatement(q);
            			stmt.setString(1, HomeController.pat_info.getPat_id().getValue());
            			stmt.setString(2, selected_date.getValue());
            			stmt.setString(3, selected_time.getValue());
            			ResultSet rs = stmt.executeQuery();
            			while(rs.next())
                        {
            				String q1 = "select medicine_name from Medicine where medicine_ID = ?;";
            				stmt = con.prepareStatement(q1);
                            stmt.setString(1, rs.getString("medicine_ID"));
                            ResultSet rs2 = stmt.executeQuery();
                            System.out.println(rs.getString("medicine_ID"));
                            while(rs2.next())
                            {
                                data.add(new Meds(rs2.getString("medicine_name"),rs.getString("amount"),rs.getString("remark")));
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
            		tableChart.setItems(data);
                }
            }
        });
        treeviewDate.setRoot(root);
              
        tableChart.setOnMouseClicked(new EventHandler<MouseEvent>() 
        {
            @Override
            public void handle(MouseEvent event) 
            {
                if(event.getClickCount() > 1)
                {
                   Meds m = tableChart.getSelectionModel().getSelectedItem();
                   HomeController.medName = m.medname;
                }
                
            }
        });
        columnName.setCellValueFactory(new PropertyValueFactory<>("medname"));
        columnDosage.setCellValueFactory(new PropertyValueFactory<>("dosage"));
        columnRemark.setCellValueFactory(new PropertyValueFactory<>("remark"));
   
        columnName.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Meds, String>>() 
        {
		   	@Override
	        public void handle(TableColumn.CellEditEvent<Meds, String> event)
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
					String query1 = "Select medicine_ID from Medicine where medicine_name=?;";
					stmt = con.prepareStatement(query1);
					stmt.setString(1, event.getNewValue());
	                ResultSet rs = stmt.executeQuery();
					
	                while(rs.next())
	                {
		                String query = "Update Medication set medicine_ID = ? where pat_ID = ? and date = ? and time = ?" ;
		                stmt = con.prepareStatement(query);
		                stmt.setString(1, rs.getString("medicine_ID"));
		                stmt.setString(2, HomeController.pat_info.getPat_id().getValue());
		                stmt.setString(3, HomeController.dateID);
		                stmt.setString(4, HomeController.timeID);
		                int no = stmt.executeUpdate();
		                System.out.println("No of affected rows: " + no);
	                }
				}
				catch(SQLException E)
				{
					Dialogs.create()
		    		.title(" ALERT ")
		    		.masthead(" SQlException encountered ")
		    		.message("Items could not be updated ")
		    		.showWarning();
					return ;
				}
				return ;
	        }
		});
        columnDosage.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Meds, String>>() 
        	{
        		
	            @Override
	            public void handle(TableColumn.CellEditEvent<Meds, String> event)
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
						String q = "Select medicine_ID from Medicine where medicine_name = ?;";
						stmt = con.prepareStatement(q);
						stmt.setString(1, HomeController.medName);
		                ResultSet rs = stmt.executeQuery();
	                    while(rs.next())
	                    {
		                    String query = "Update Medication set amount = ? where pat_ID = ? and date = ? and time = ? and medicine_ID = ?;" ;
		                    stmt = con.prepareStatement(query);
		                    stmt.setString(1, event.getNewValue());
		                    stmt.setString(2, HomeController.pat_info.getPat_id().getValue());
		                    stmt.setString(3, HomeController.dateID);
		                    stmt.setString(4, HomeController.timeID);
		                    stmt.setString(5, rs.getString("medicine_ID"));
		                    int no = stmt.executeUpdate();
		                    System.out.println("No of rows updated: " + no);
	                    }
					}
					catch(SQLException E)
					{
						Dialogs.create()
			    		.title(" ALERT ")
			    		.masthead(" SQlException encountered ")
			    		.message("Items could not be updated ")
			    		.showWarning();
						return ;
					}
	            }
        });
        columnRemark.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Meds, String>>() 
        	{
        		@Override
	            public void handle(TableColumn.CellEditEvent<Meds, String> event) 
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
						String q = "Select medicine_ID from Medicine where medicine_name = ?;";
						stmt = con.prepareStatement(q);
						stmt.setString(1, HomeController.medName);
	                    ResultSet rs = stmt.executeQuery();
	                    while(rs.next())
	                    {
		                    String query = "Update Medication set remark = ? where pat_ID = ? and date = ? and time = ? and medicine_ID = ?;" ;
		                    stmt = con.prepareStatement(query);
		                    stmt.setString(1, event.getNewValue());
		                    stmt.setString(2, HomeController.pat_info.getPat_id().getValue());
		                    stmt.setString(3, HomeController.dateID);
		                    stmt.setString(4, HomeController.timeID);
		                    stmt.setString(5, rs.getString("medicine_ID"));
		                    int no = stmt.executeUpdate();
		                    System.out.println("No of rows affected: " + no);
	                    }
					}
					catch(SQLException E)
					{
						Dialogs.create()
			    		.title(" ALERT ")
			    		.masthead(" SQlException encountered ")
			    		.message("Items could not be updated ")
			    		.showWarning();
						return ;
					}
	            }
        });
        columnName.setCellFactory(TextFieldTableCell.forTableColumn());
        columnDosage.setCellFactory(TextFieldTableCell.forTableColumn());
        columnRemark.setCellFactory(TextFieldTableCell.forTableColumn());
        tableChart.setEditable(true);
        getDates();
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
			String query = "Select distinct date from Medication where pat_ID=?;";
			stmt = con.prepareStatement(query);
			stmt.setString(1, HomeController.pat_info.getPat_id().getValue());
			ResultSet rs = stmt.executeQuery();
			while(rs.next())
            {
                TreeItem<String> test = new TreeItem<String>(rs.getString("date"));
                root.getChildren().add(test);
                System.out.println(rs.getString("date"));
                String q2 = "Select distinct time from Medication where pat_ID=? and date=?;";
                stmt = con.prepareStatement(q2);
                
                stmt.setString(1, HomeController.pat_info.getPat_id().getValue());
                stmt.setString(2, rs.getString("date"));
                
                ResultSet rs1 = stmt.executeQuery();
                while(rs1.next())
                {
                    TreeItem<String> time = new TreeItem<String>(rs1.getString("time"));
                    test.getChildren().add(time);
                    System.out.println(rs1.getString("time"));
                }
            }
		}
		catch(SQLException E)
		{
			Dialogs.create()
    		.title(" ALERT ")
    		.masthead(" SQlException encountered ")
    		.message("Items could not be updated ")
    		.showWarning();
			return ;
		}
    }

    public static class Meds 
    {

        public Meds(String a, String b, String c) {
            this.medname = a;
            this.dosage =b;
            this.remark = c;
            
        }
            private String medname;

        public String getMedname() {
            return medname;
        }

        public void setMedname(String medname) {
            this.medname = medname;
        }

        public String getDosage() {
            return dosage;
        }

        public void setDosage(String dosage) {
            this.dosage = dosage;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }
            private String dosage;
            private  String remark;
    }
      
        public void handle() {
            
            int ix = tableChart.getSelectionModel().getSelectedIndex();

            Meds med = (Meds) tableChart.getSelectionModel().getSelectedItem();
           

            data.remove(ix);

            if (ix != 0) {

                ix = ix -1;

            }
            tableChart.requestFocus();
            tableChart.getSelectionModel().select(ix);

            tableChart.getFocusModel().focus(ix);

        }            
        
        }