package Controller.Root;

import java.net.URL;
import java.util.ResourceBundle;

import Model.Employee.Employee_Info;
import application.Main;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

public class Controller_Root_Layout implements Initializable 
{
	private Main mainApp;
	
	@FXML MenuBar menu_bar = new MenuBar();
	
	@FXML Menu menu_general = new Menu();
	@FXML MenuItem dashboard_menu = new MenuItem();
	@FXML MenuItem database_menu = new MenuItem();

	@FXML Menu menu_cms = new Menu();	
	@FXML MenuItem manage_remarks_menu = new MenuItem();
	@FXML MenuItem manage_medicines_menu = new MenuItem();
	@FXML MenuItem manage_fees_menu = new MenuItem();
	@FXML MenuItem manage_tests_menu = new MenuItem();
	
	@FXML Menu menu_patient = new Menu();
	@FXML MenuItem add_patient_menu = new MenuItem();
	@FXML MenuItem search_patient_menu = new MenuItem();
	@FXML MenuItem manage_prescriptions_menu = new MenuItem();
	@FXML MenuItem manage_receipts_menu = new MenuItem();
	@FXML MenuItem manage_indoor_patients_menu = new MenuItem();
	
	@FXML Menu menu_account = new Menu();
	@FXML MenuItem manage_account_menu = new MenuItem();
	
	@FXML Menu menu_employee_mgmt = new Menu();
	@FXML MenuItem manage_employees_menu = new MenuItem();

	public void setStage(Main mainApp)
	{
		this.mainApp = mainApp;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) 
	{
		EventHandler<ActionEvent> action = handle_event();
		
		dashboard_menu.setOnAction(action);
		database_menu.setOnAction(action);
		
		manage_remarks_menu.setOnAction(action);
		manage_medicines_menu.setOnAction(action);
		manage_fees_menu.setOnAction(action);
		manage_tests_menu.setOnAction(action);
		
		add_patient_menu.setOnAction(action);
		search_patient_menu.setOnAction(action);
		manage_prescriptions_menu.setOnAction(action);
		manage_receipts_menu.setOnAction(action);
		manage_indoor_patients_menu.setOnAction(action);
		
		manage_account_menu.setOnAction(action);
		
		manage_employees_menu.setOnAction(action);
	}

	@FXML
	private EventHandler<ActionEvent> handle_event() 
	{
		return new EventHandler<ActionEvent>() 
		{
			@Override
			public void handle(ActionEvent event) 
			{
				MenuItem mItem = (MenuItem) event.getSource();
				String mitem_str = mItem.getText();
				System.out.println("String is : " + mitem_str);
				if(mitem_str.equalsIgnoreCase("Go to dashboard"))
				{
					System.out.println("Calling it");
					mainApp.showDashboard();
				}
				else if(mitem_str.equalsIgnoreCase("Database Connectivity"))
				{
					mainApp.showDatabase();
				}
				
				else if(mitem_str.equalsIgnoreCase("Manage Remarks"))
				{
					mainApp.showRemarks();
				}
				else if(mitem_str.equalsIgnoreCase("Manage Medicines"))
				{
					mainApp.showMedicines();
				}
				else if(mitem_str.equalsIgnoreCase("Manage Fees"))
				{
					mainApp.showFees();
				}
				else if(mitem_str.equalsIgnoreCase("Manage Tests"))
				{
					mainApp.showTests();
				}
				
				else if(mitem_str.equalsIgnoreCase("Add a Patient"))
				{
					mainApp.showAddPatient();
				}
				else if(mitem_str.equalsIgnoreCase("Search for a Patient"))
				{
					mainApp.showSearchPatient();
				}
				else if(mitem_str.equalsIgnoreCase("Manage Prescriptions"))
				{
					mainApp.managePrescription();
				}
				else if(mitem_str.equalsIgnoreCase("Manage Receipts"))
				{
					mainApp.manageReceipt();
				}
				else if(mitem_str.equalsIgnoreCase("Manage Indoor Patients"))
				{
					mainApp.manage_indoor_patient();
				}
				
				else if(mitem_str.equalsIgnoreCase("Manage Account"))
				{
					mainApp.manageAccount();
				}
				
				else if(mitem_str.equalsIgnoreCase("Manage Employees"))
				{
					mainApp.manageEmployee();
				}

				else
				{
					System.out.println("False alarm...");
				}
			}
		};
	}

}
