/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller.Root;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import Controller.Employee.Controller_Add_Employee;
import Model.Employee.Employee_Info;
import application.Main;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author hppc
 */
public class Controller_Dashboard_Receptionist implements Initializable 
{
	private Main mainApp;
	private Stage primaryStage;
	private Employee_Info employee_info;
	
    @FXML private Label employee_name;
    @FXML Image img_logo;
    @FXML Button btn_edit_profile, btn_logout;
    @FXML Button btn_create_prescription, btn_search_prescription;
    @FXML Button btn_create_receipt, btn_search_receipt;
    @FXML Button btn_add_medication, btn_search_medication;
    @FXML Button btn_add_tests, btn_search_tests;
    @FXML Button btn_add_employee, btn_search_employee;
    @FXML Button btn_add_patient, btn_search_patient;
    @FXML Button btn_manage_account;
    @FXML Button btn_manage_fees, btn_manage_tests, btn_manage_medicines, btn_manage_remarks;
    
    
    @FXML
    private void handle_btn_edit_profile() 
    {
    	System.out.println("Edit profile is clicked...");
    	
    	try
		{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/View/Employee/Dialog_Add_Employee.fxml"));
			AnchorPane page = (AnchorPane) loader.load();
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Edit Employee Details");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);
			Controller_Add_Employee controller = loader.getController();
			System.out.println("Hi!!\n");
			controller.setStage(dialogStage);
			employee_info.setBirth_date(new SimpleStringProperty(LocalDate.now().toString()));
			employee_info.setDate_of_joining(LocalDate.now());
			controller.setEmployee(employee_info, "EDIT");
			dialogStage.showAndWait();			
		}
		catch(Exception E)
		{
			E.printStackTrace();
		}
    }
    @FXML
    private void handle_btn_logout() 
    {
    	Main.setEmployee(null);
    	mainApp.showLogin();
        System.out.println("logout is clicked!!!");
    }
    @FXML
    private void handle_btn_add_new_patient() 
    {
        System.out.println("add patient is clicked!!!");
        mainApp.showAddPatient();
    }
    @FXML
    private void handle_btn_manage_patient() 
    {
        System.out.println("edit profile is clicked!!!");
        mainApp.showSearchPatient();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
    	img_logo = new Image("/Resources/img_logo.png");
    }
    
    public void setMainApp(Main mainApp, Employee_Info employee_info)
    {
    	this.mainApp = mainApp;
    	this.primaryStage = mainApp.getStage();
    	this.employee_info = employee_info;
    }
    
}
