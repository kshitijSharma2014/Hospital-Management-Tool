/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller.Account;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.controlsfx.dialog.Dialogs;

import application.Main;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * This is Controller for Details Of amount On the Particular date.
 * @author Shahbazz
 */
@SuppressWarnings("deprecation")
public class Controller_Details {
    @FXML private Stage primaryStage;
    @FXML
    private TreeTableView<TableData> treetableview;
    @FXML
    private TreeTableColumn col1;
    @FXML
    private TreeTableColumn col2;
    @FXML
    private TreeTableColumn col3;
    @FXML
    private TreeTableColumn col4;
    
    
    @FXML
    private Button back;
    
    @FXML
    private Button edit;
    
    @FXML
    private Button delete;
    
    @FXML
    private Label label;
     
    @FXML
    private void backtomain(ActionEvent event) throws IOException{
        Parent add_chart = FXMLLoader.load(getClass().getResource("/View/Account/Account_Info.fxml"));
            Scene scene_add_chart = new Scene(add_chart);
            Stage stage_chart = (Stage) back.getScene().getWindow();
            stage_chart.setScene(scene_add_chart);
            stage_chart.setTitle("Accounts");
            stage_chart.show();
    } 
   
    @FXML
    private void deleteinfo(ActionEvent event) throws ClassNotFoundException, SQLException {
        int selectedIndex = treetableview.getSelectionModel().getSelectedIndex();
        
		if(selectedIndex >= 0)
		{              
                        TreeItem<TableData> s= treetableview.getTreeItem(selectedIndex).getParent();
                        
                       
                        String recepid=s.getValue().getLastname();
                        String a=(String) col2.getCellData(selectedIndex);
                        System.out.println(recepid+"dd"+a);                 
                        
                        Connection con = Main.getConnection();
                		if(con == null)
                		{
                			Dialogs.create()
                    		.owner(primaryStage)
                    		.title(" ALERT ")
                    		.masthead(" Database is not setup ")
                    		.message("Please set up the connection ")
                    		.showWarning();
                			return ;
                		}
                		else
                		{
                			try
                			{
                				//String sql="delete from Receipt_fee_distribution where Receipt_ID= '"+recepid+"'and fee_ID=(Select fee_ID from Fee_type where fee_name='"+a+"');";
                				
                				String sql="delete from Receipt_fee_distribution where Receipt_ID= '"+recepid+"';";
                				String sql1="delete from Receipt where Receipt_ID= '"+recepid+"';";
                				
                				PreparedStatement stmt = con.prepareStatement(sql);
                				stmt.executeUpdate();
                				
                				stmt = con.prepareStatement(sql1);
                				stmt.executeUpdate();
                				
                				s.getParent().getChildren().remove(s);
                                treetableview.getSelectionModel().clearSelection();
                                label.setText("deleted");                        
                                System.out.println("Fees Type deleted: ");
                			}
                			catch(SQLException E)
                			{
                				Dialogs.create()
                	    		.owner(primaryStage)
                	    		.title(" ALERT ")
                	    		.masthead(" Database is not setup ")
                	    		.message("Items could not be loaded... ")
                	    		.showWarning();
                				return ;
                			}
                		}
		}
        
    }


    @FXML
    private void editinfo(ActionEvent event) throws IOException {
        int selectedIndex = treetableview.getSelectionModel().getSelectedIndex();
		if(selectedIndex >= 0)
		{       //System.out.println("abc");       
                        TreeItem<TableData> s= treetableview.getTreeItem(selectedIndex).getParent();
                        //s.getValue().get;
                        System.out.println("Blah blah blah: " + s.getValue().getLastname()+s.getValue().getLastname1());
                        String x= s.getValue().getLastname();
                      
                            //System.out.println("abcdef");
                            col2.setEditable(true);
                            col3.setEditable(true);
                            col4.setEditable(true);
                           // col2.setCellValueFactory(new TreeItemPropertyValueFactory<>("firstname"));
                            col3.setCellValueFactory(new TreeItemPropertyValueFactory<>("lastname1"));
                            //col4.setCellValueFactory(new TreeItemPropertyValueFactory<>("firstname1"));
                            //col2.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
                            col3.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
                            //col4.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());                       
                            label.setText("editing");
                col3.setOnEditCommit(new EventHandler<TreeTableColumn.CellEditEvent<TableData, String>> () {

                @Override
                public void handle(TreeTableColumn.CellEditEvent<TableData, String> event) {
                     System.out.println("here"+col3.getCellData(selectedIndex));
                   System.out.println("old"+event.getOldValue());
                   System.out.println("new"+event.getNewValue());
                     label.setText("Saving");
                     String a=(String) col2.getCellData(selectedIndex);
                     String b=(String) col4.getCellData(selectedIndex);
                     String c=(String) col3.getCellData(selectedIndex);
                     System.out.println(a+"========"+b);
                      ResultSet as = null; 
                     String recepid=s.getValue().getLastname();
                     System.out.println(recepid);
                    
                     int amount=Integer.parseInt((String) event.getNewValue());
                     System.out.println("amount"+amount);
                               
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
                 		.owner(primaryStage)
                 		.title(" ALERT ")
                 		.masthead(" Database is not setup ")
                 		.message("Please set up the connection ")
                 		.showWarning();
             		}
             		PreparedStatement stmt = null;
             		try
             		{
             			String sqlq="update Receipt_fee_distribution set amount="+amount+"where Receipt_ID= '"+recepid+"'and fee_ID=(Select fee_ID from Fee_type where fee_name='"+a+"');"; 
             			stmt = con.prepareStatement(sqlq);
             			int no = stmt.executeUpdate();
             			System.out.println("No.of rows deleted: " + no);
             		}
             		catch(SQLException E)
             		{
             			Dialogs.create()
                 		.owner(primaryStage)
                 		.title(" ALERT ")
                 		.masthead(" SQlException encountered ")
                 		.message("Item could not be deleted ")
                 		.showWarning();
             		}
                }
                });
		}
		else
		{
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.initOwner(primaryStage);
			alert.setTitle("Nothing has been selected...");
			alert.setHeaderText("No Transaction Selected");
			alert.setContentText("Please select any transaction");
			
			alert.showAndWait();
		}
                  
    }

 
    public void initialize() throws SQLException, ClassNotFoundException {
        /**
         * tell columns where data comes from
         * 
         */
    
        treetableview.setEditable(true); 
        treetableview.editableProperty();
        col1.setCellValueFactory(new TreeItemPropertyValueFactory<>("lastname"));
        col2.setCellValueFactory(new TreeItemPropertyValueFactory<>("firstname"));
        col3.setCellValueFactory(new TreeItemPropertyValueFactory<>("lastname1"));
        col4.setCellValueFactory(new TreeItemPropertyValueFactory<>("firstname1"));
        col1.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
        col2.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
       
        col3.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
        col4.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
        col1.setEditable(true);
        col2.setEditable(true);
        
        
        
        
        
 
        /**
         * COMMENT OUT FROM HERE
         */
//        /*
        col1.setCellFactory(new Callback<TreeTableColumn<TableData, String>, TreeTableCell<TableData, String>>() {
            @Override
            public TreeTableCell<TableData, String> call(TreeTableColumn<TableData, String> param) {
 
                TreeTableCell<TableData, String> cell = new TreeTableCell<TableData, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            setText(item);
                        }
                    }
                };
                cell.setAlignment(Pos.CENTER);
                return cell;
            }
        });
 
        col2.setCellFactory(new Callback<TreeTableColumn<TableData, String>, TreeTableCell<TableData, String>>() {
            @Override
            public TreeTableCell<TableData, String> call(TreeTableColumn<TableData, String> param) {
                TreeTableCell<TableData, String> cell = new TreeTableCell<TableData, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            setText(item);
                        }
                    }
                };
                cell.setAlignment(Pos.CENTER);
                return cell;
            }
        });
        col3.setCellFactory(new Callback<TreeTableColumn<TableData, String>, TreeTableCell<TableData, String>>() {
            @Override
            public TreeTableCell<TableData, String> call(TreeTableColumn<TableData, String> param) {
 
                TreeTableCell<TableData, String> cell = new TreeTableCell<TableData, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            setText(item);
                        }
                    }
                };
                cell.setAlignment(Pos.CENTER);
                return cell;
            }
        });
        
        col4.setCellFactory(new Callback<TreeTableColumn<TableData, String>, TreeTableCell<TableData, String>>() {
            @Override
            public TreeTableCell<TableData, String> call(TreeTableColumn<TableData, String> param) {
 
                TreeTableCell<TableData, String> cell = new TreeTableCell<TableData, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            setText(item);
                        }
                    }
                };
                cell.setAlignment(Pos.CENTER);
                return cell;
            }
        });
        
//        */
        /**
         * END COMMENT
         */
        /**
         * manufacture some data
         */
 
        TreeItem<TableData> root = new TreeItem<TableData>(new TableData("Dates", "","",""));
        treetableview.setRoot(root);
        root.setExpanded(true);
        
        String [] date3 = new String [1000000];
        String [] Receipt_id = new String [1000000];
        String [] Fee_Type = new String [1000000];
        String [] Amount = new String [1000000];
        String [] Time = new String [1000000];
        String Pat_Name = null;
        String Pat_id = null;
        int x=0,y=0,z=0,w=0,v=0;
        
        String r=Controller_Account_Info.seeel;
        String [] date = new String [10000];
        date=r.split("/");
        String q=date[2]+"-"+date[1]+"-"+date[0];
        System.out.println(q);	
        
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
     		.owner(primaryStage)
     		.title(" ALERT ")
     		.masthead(" Database is not setup ")
     		.message("Please set up the connection ")
     		.showWarning();
 		}
 		PreparedStatement stmt = null;
 		try
 		{
 			String sql = "SELECT r.date FROM Receipt r WHERE r.date='"+q+"';";
 			stmt = con.prepareStatement(sql);
 			ResultSet rs = stmt.executeQuery();
 			while(rs.next())
            {               date3[x] = rs.getString("date");                       
                            x++;
            }
 			
 			
 			for(int k=0;k<x;k++){
 	            
 	            TreeItem<TableData> item = new TreeItem<TableData>(new TableData(date3[k], "","",""));
 	            root.getChildren().add(item);
 	            sql = "SELECT r.Receipt_ID FROM Receipt r WHERE r.date='"+date3[k]+"';";
 	            stmt = con.prepareStatement(sql);
 	            ResultSet ss = stmt.executeQuery();
 	            if(ss != null)
 	            {
 	                
 	                while(ss.next())
 	                {               Receipt_id[y] = ss.getString("Receipt_ID");                       
 	                                y++;
 	                }
 	            }
 	            for(int j=0;j<y;j++){
 	            TreeItem<TableData> item1 = new TreeItem<TableData>(new TableData(Receipt_id[j], "","",""));
 	            item.getChildren().add(item1);
 	            sql = "SELECT p.name FROM Receipt r,Patient p WHERE r.Receipt_ID='"+Receipt_id[j]+"' AND r.pat_id=p.pat_id;";
	            stmt = con.prepareStatement(sql);
	            ResultSet ts = stmt.executeQuery();
 	            
 	            if(ts != null)
 	            {
 	                
 	                while(ts.next())
 	                {               Pat_Name = ts.getString("name"); 
 	                }
 	            }
 	            TreeItem<TableData> item2 = new TreeItem<TableData>(new TableData("Patient_Name = "+Pat_Name, "","",""));
 	            item1.getChildren().add(item2);
 	            
 	            sql = "SELECT p.pat_id FROM Receipt r,Patient p WHERE r.Receipt_ID='"+Receipt_id[j]+"' AND r.pat_id=p.pat_id;";
	            stmt = con.prepareStatement(sql);
	            ResultSet zs = stmt.executeQuery();
 	            
 	            if(zs != null)
 	            {
 	                 while(zs.next())
 	                {               Pat_id = zs.getString("pat_id"); 
 	                }
 	            }
 	            item2 = new TreeItem<TableData>(new TableData("Patient_ID = "+Pat_id, "","",""));
 	            item1.getChildren().add(item2);
 	            
 	            sql = "SELECT f.fee_name FROM Receipt_fee_distribution r,Fee_Type f WHERE r.Receipt_ID='"+Receipt_id[j]+"' AND r.fee_id=f.fee_id;";
	            stmt = con.prepareStatement(sql);
	            ResultSet as = stmt.executeQuery();
 	            
 	            
 	            if(as != null)
 	            {
 	                 while(as.next())
 	                {               Fee_Type[z] = as.getString("fee_name"); 
 	                                z++;
 	                }
 	            }
 	            
 	            sql = "SELECT r.amount FROM Receipt_fee_distribution r WHERE r.Receipt_ID='"+Receipt_id[j]+"';";
 	           	stmt = con.prepareStatement(sql);
	            ResultSet bs = stmt.executeQuery();
	            
 	            
 	            if(bs != null)
 	            {
 	                 while(bs.next())
 	                {               int g = bs.getInt("amount"); 
 	                                Amount[w]=""+g;    
 	                
 	                                w++;
 	                }
 	            }
 	            
 	            sql = "SELECT r.time FROM Receipt r WHERE r.Receipt_ID='"+Receipt_id[j]+"';";
	           	stmt = con.prepareStatement(sql);
	            ResultSet cs = stmt.executeQuery();
	            
 	            
 	            if(cs != null)
 	            {
 	                 while(cs.next())
 	                {               String h =cs.getString("time");                    
 	                                Time[v] =h; 
 	                                v++;
 	                }
 	            }
 	            for(int m=0;m<z;m++){
 	            TreeItem<TableData> item3 = new TreeItem<TableData>(new TableData("",Fee_Type[m],Amount[m],Time[0]));
 	            item1.getChildren().add(item3); 
 	            }
 	          }
 	        }
 			
 		}
 		catch(SQLException E)
 		{
 			Dialogs.create()
     		.owner(primaryStage)
     		.title(" ALERT ")
     		.masthead(" SQlException encountered ")
     		.message("Item could not be deleted ")
     		.showWarning();
 		}
    
        
        
    }
 
    
    
    /**
     * Class to store info for tree table view
     */
    public class TableData {
 
        private final StringProperty firstname = new SimpleStringProperty();
        private final StringProperty lastname = new SimpleStringProperty();
        private final StringProperty firstname1 = new SimpleStringProperty();
        private final StringProperty lastname1 = new SimpleStringProperty();
 
        public TableData(String lastname, String firstname,String lastname1, String firstname1) {
            this.firstname.setValue(firstname);
            this.lastname.setValue(lastname);
            this.firstname1.setValue(firstname1);
            this.lastname1.setValue(lastname1);
            
        }
 
        public String getFirstname() {
               System.out.println("hellollllllll" + firstname.getValue() + "<----");
            return firstname.getValue();
        }
        public void setFirstname(String x) {
            firstname.set(x);
        }
 
        public StringProperty firstnameProperty() {
            return firstname;
        }
 
        public String getLastname() {
                           System.out.println("hellollllllll" + lastname.getValue() + "<----");

            return lastname.getValue();
        }
 
        public StringProperty lastnameProperty() {
            return lastname;
        }
        public String getFirstname1() {
            System.out.println("hellollllllll");
            return firstname1.getValue();
        }
 
        public StringProperty firstname1Property() {
            return firstname1;
        }
 
        public String getLastname1() {
            return lastname1.getValue();
        }
 
        public StringProperty lastname1Property() {
            return lastname1;
        }

        void Firstname(String text) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        void setID(String id) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        Object getfirstname() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        Object get_fees_name() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }
}