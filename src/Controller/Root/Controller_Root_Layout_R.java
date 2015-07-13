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

public class Controller_Root_Layout_R implements Initializable 
{
	private Main mainApp;
	
	@FXML MenuBar menu_bar = new MenuBar();
	
	@FXML Menu menu_general = new Menu();
	@FXML MenuItem dashboard_menu = new MenuItem();
	@FXML MenuItem database_menu = new MenuItem();

	@FXML Menu menu_patient = new Menu();
	@FXML MenuItem add_patient_menu = new MenuItem();
	@FXML MenuItem search_patient_menu = new MenuItem();
	
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
				
		add_patient_menu.setOnAction(action);
		search_patient_menu.setOnAction(action);
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
					mainApp.showDashboard_R();
				}
				else if(mitem_str.equalsIgnoreCase("Database Connectivity"))
				{
					mainApp.showDatabase();
				}
				
				else if(mitem_str.equalsIgnoreCase("Add a Patient"))
				{
					mainApp.showAddPatient();
				}
				else if(mitem_str.equalsIgnoreCase("Search for a Patient"))
				{
					mainApp.showSearchPatient();
				}

				else
				{
					System.out.println("False alarm...");
				}
			}
		};
	}

}
