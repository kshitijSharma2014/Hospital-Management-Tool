/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller.Account;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import Model.Account.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import org.controlsfx.dialog.Dialogs;

import application.Main;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

/**
 *  This is a Controller for Account Details.Get the amount for Particular date. 
 * @author Shahbaaz & Avinash
 */
@SuppressWarnings("deprecation")
public class Controller_Account_Info implements Initializable {    

	static String seeel=null;

	
	@FXML
    private Label label; 
    
   @FXML
    private TableView<Table> table;
    
   @FXML
    TableColumn<Table,String> date;
 
   @FXML
    TableColumn<Table,String> amount;
   
   @FXML
    TableColumn<Table,Boolean> checkbox;
    
   
   
   @FXML
     ObservableList<Table> data  =FXCollections.observableArrayList();
   
   
    
    
    @FXML
    Stage primarystage;
    @FXML
    private TextField text;
    @FXML
    private Button viewdetails;
    @FXML
    private DatePicker start_date;
    @FXML
    private DatePicker end_date;
    @FXML
    private Button Search; 
    @FXML
    private Button del; 
    
    private void datequery(int s_date,int s_month,int s_year,int e_date,int e_month,int e_year) throws SQLException, ClassNotFoundException {
                long start=365*s_year+30*s_month+s_date;
               long end=365*e_year+30*e_month+e_date;
		String [] date3 = new String [10000];
                int [] date2 = new int [10000];
                int [] month2 = new int [10000];
		        int [] year2 = new int [10000];
                int x=0;
    
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
        		else
        		{
        			try
        			{
        				String query = "SELECT r.date FROM Receipt r;";
        				PreparedStatement stmt = con.prepareStatement(query);
        				ResultSet rs = stmt.executeQuery();
        				while(rs.next())
        				{
        					date3[x] = rs.getString("date");                       
        	                System.out.println("sds"+date3[0]);
        					String arr[] = date3[x].split("-");
                            date2[x]=Integer.parseInt(arr[2]);
                            month2[x]=Integer.parseInt(arr[1]);
			                year2[x] =Integer.parseInt(arr[0]);
                            System.out.println(date2[x]+"    "+month2[x]+"    "+year2[x]);
                            x++;
        				}
        				
        				int i,total_amount=0;
        	            for(i=0;i<x;i++){
        	                    long temp=365*year2[i]+30*month2[i]+date2[i];
        	                                       if(start<=temp && end>=temp)
        	                                       {
        	                                	    System.out.println("sds"+date3[i]);
        	                                        String s=date3[i];
        	                                        String query1 = "SELECT SUM(rfd.amount) as summ FROM Receipt_fee_distribution rfd, Receipt r WHERE rfd.Receipt_ID = r.Receipt_ID AND r.date='" + s + "';";
        	                                        stmt = con.prepareStatement(query1);
        	                                        ResultSet ss = stmt.executeQuery();
        	                                        System.out.println(ss);
        	                                    if(ss != null)
        	                                    {
        	                                        while(ss.next())
        	                                        {  
        	                                           int sum=ss.getInt("summ");
        	                                           
        	                                           Table tbl;
        	                                           if(sum > 0)
        	                                           {
        	                                        	   tbl = new Table(date2[i]+"/"+month2[i]+"/"+year2[i],Integer.toString(sum));
        	                                        	   data.add(tbl);
        	                                        	   total_amount+=sum;
        	                                           }
        	                                           System.out.println(sum);
        	                                           
        	                                          }
        	                                    }}
        	                      }
        	            if(data.size()==0)
        	            {
        	     			Alert alert = new Alert(Alert.AlertType.WARNING);
        	     			alert.initOwner(primarystage);
        	     			alert.setTitle("No result returned...");
        	     			alert.setHeaderText("Sorry there is no transaction between this period");
        	     			alert.setContentText("Please select a diferent start and end date. ");			
        	     			alert.showAndWait();
        	     		}
        	         String z=""+total_amount;
        	         label.setText("Search Completed");
        	         text.setText(z);	
        			
        		}
        			catch(SQLException E)
        			{
        				E.printStackTrace();
        				Dialogs.create()
        	    		.title(" ALERT ")
        	    		.masthead(" Database is not setup ")
        	    		.message("Items could not be loaded... ")
        	    		.showWarning();
        				return ;
        			}
        		}
    }    
    
    @FXML
    private void Searchinfo(ActionEvent event) throws SQLException, ClassNotFoundException{
         data.clear();
    	LocalDate s_date=start_date.getValue();
    	
        LocalDate e_date=end_date.getValue();
        if(s_date==null)
        {
 			Alert alert = new Alert(Alert.AlertType.WARNING);
 			alert.initOwner(primarystage);
 			alert.setTitle("Nothing has been selected...");
 			alert.setHeaderText("No Start Date Selected");
 			alert.setContentText("Please select a date from the date picker.");			
 			alert.showAndWait();
 		}	
        if(e_date==null)
        {
 			Alert alert = new Alert(Alert.AlertType.WARNING);
 			alert.initOwner(primarystage);
 			alert.setTitle("Nothing has been selected...");
 			alert.setHeaderText("No End date  Selected");
 			alert.setContentText("Please select a date  from  the date picker.");			
 			alert.showAndWait();
 		}
        if(s_date.isAfter(e_date))
        {
 			Alert alert = new Alert(Alert.AlertType.WARNING);
 			alert.initOwner(primarystage);
 			alert.setTitle("Bad command ");
 			alert.setHeaderText("End date cannot be before Start date");
 			alert.setContentText("Please select a valid date .");			
 			alert.showAndWait();
 		}
        
        int date4=s_date.getDayOfMonth();
        int month=s_date.getMonthValue();
        int year=s_date.getYear();
        int date1=e_date.getDayOfMonth();
        int month1=e_date.getMonthValue();
        int year1=e_date.getYear();
        datequery(date4, month, year, date1, month1, year1);
    }  
    
    @FXML
    private void Printinfo(ActionEvent event) {
        System.out.println("You clicked me!");
        label.setText("Printing");
    }
    
    @FXML
    private void View_Details(ActionEvent event)throws IOException{
    	 int selectedIndex = table.getSelectionModel().getSelectedIndex();
    	 if(selectedIndex<0)

    	 {
 			Alert alert = new Alert(Alert.AlertType.WARNING);
 			alert.initOwner(primarystage);
 			alert.setTitle("Nothing has been selected...");
 			alert.setHeaderText("No transaction Selected");
 			alert.setContentText("Please select a transaction in the table.");			
 			alert.showAndWait();
 		}
    	 else
    	 {
             seeel=data.get(selectedIndex).getRID();
             Parent add_chart = FXMLLoader.load(getClass().getResource("/View/Account/Details.fxml"));
             Scene scene_add_chart = new Scene(add_chart);
             Stage stage_chart = (Stage) viewdetails.getScene().getWindow();
             stage_chart.setScene(scene_add_chart);
             stage_chart.setTitle("Details");
             stage_chart.show();
            
    	 }
    }
    @FXML
    private void deleteinfo(ActionEvent event) {
        int selectedIndex = table.getSelectionModel().getSelectedIndex();
		if(selectedIndex >= 0)
		{     
                    table.getSelectionModel().clearSelection();
                    data.remove(selectedIndex);
                    System.out.println("Fees Type deleted: ");
		}
		else
		{
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.initOwner(primarystage);
			alert.setTitle("Nothing has been selected...");
			alert.setHeaderText("No transaction Selected");
			alert.setContentText("Please select a transaction in the table.");			
			alert.showAndWait();
		}
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        date.setCellValueFactory(new PropertyValueFactory<>("rID"));
        amount.setCellValueFactory(new PropertyValueFactory<>("rName"));
        table.setItems(data);
        date.setCellFactory(TextFieldTableCell.forTableColumn());
        amount.setCellFactory(TextFieldTableCell.forTableColumn());
        table.setEditable(true);
        date.setEditable(false);
        amount.setEditable(false);
               
    }    
 }
    

