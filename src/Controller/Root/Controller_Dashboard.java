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
 * @author Devanshu Jain
 */
public class Controller_Dashboard implements Initializable 
{
	private Main mainApp;
	private Stage primaryStage;
	private Employee_Info employee_info;
	
    @FXML private Label employee_name;
    @FXML Image img_logo, img_account, img_search, img_add, img_patient;
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
    @FXML
    private void handle_btn_manage_employee() 
    {
        System.out.println("edit profile is clicked!!!");
        mainApp.manageEmployee();
    }
    @FXML
    private void handle_btn_manage_fee() 
    {
        System.out.println("edit profile is clicked!!!");
        mainApp.showFees();
    }
    @FXML
    private void handle_btn_manage_medicine() 
    {
        System.out.println("edit profile is clicked!!!");
        mainApp.showMedicines();
    }
    @FXML
    private void handle_btn_manage_test() 
    {
        System.out.println("edit profile is clicked!!!");
        mainApp.showTests();
    }
    @FXML
    private void handle_btn_manage_remark() 
    {
        System.out.println("edit profile is clicked!!!");
        mainApp.showRemarks();
    }
    @FXML
    private void handle_btn_manage_account() 
    {
        System.out.println("edit profile is clicked!!!");
        mainApp.manageAccount();
    }
    @FXML
    private void handle_btn_manage_prescription() 
    {
        System.out.println("edit profile is clicked!!!");
        mainApp.managePrescription();
    }
    @FXML
    private void handle_btn_manage_receipt() 
    {
        System.out.println("edit profile is clicked!!!");
        mainApp.manageReceipt();
    }
    
    @FXML
    private void handle_btn_manage_indoor_patient()
    {
    	mainApp.manage_indoor_patient();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
    	img_logo = new Image("/Resources/img_logo.png");
    	img_add = new Image("/Resources/img_add.png");
    	img_patient = new Image("/Resources/img_patient_related_info.png");
    	img_account = new Image("/Resources/img_account_management.png");
    	img_search = new Image("/Resources/img_search.png");
    }
    
    public void setMainApp(Main mainApp, Employee_Info employee_info)
    {
    	this.mainApp = mainApp;
    	this.primaryStage = mainApp.getStage();
    	this.employee_info = employee_info;
    	String str = "Welcome, " + employee_info.getFirst_name().getValue() + " " + employee_info.getLast_name().getValue();
    	Text text = new Text(str);
    	double width = text.getLayoutBounds().getWidth();
    	employee_name.setMinWidth(width);
    	employee_name.setText(str);
    }
    
}
