package application;
	
import java.sql.Connection;

import Controller.Account.Controller_Account_Info;
import Controller.AdmitPatient.Controller_Indoor_Patient;
import Controller.CMS.*;
import Controller.Employee.Controller_Search_Employee;
import Controller.Receipt.Controller_Search_Patient_Receipt;
import Controller.Root.Controller_Dashboard;
import Controller.Root.Controller_Dashboard_Medical_Officer;
import Controller.Root.Controller_Dashboard_Receptionist;
import Controller.Root.Controller_Root_Layout;
import Controller.Root.Controller_Root_Layout_MO;
import Controller.Root.Controller_Root_Layout_R;
import Controller.Login.Controller_Login;
import Controller.Patient.Controller_Add_Patient;
import Controller.Patient.Controller_Search_Patient;
import Controller.Prescription.Controller_Search_Patient_Prescription;
import Model.Employee.Employee_Info;
import Model.Patient.Patient_Info;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class Main extends Application
{
	private static Stage primaryStage;
	private static BorderPane root_layout = null;
	private static Employee_Info employee_info;
	
	private static String USER_NAME = "user";
	private static String PASSWORD = "password";
	private static String IP = "10.100.98.54";
	private static String PORT = "1433";
	private static String DBNAME = "sharda";
	
	private Stage stage;
	
	private static Connection conn = null;
	
	public static Employee_Info getEmployee()
	{
		return Main.employee_info;
	}
	
	public static void setEmployee(Employee_Info emp_info)
	{
		if(emp_info == null)
		{
			System.out.println("knwkblblkw");
		}
		Main.employee_info = emp_info;
	}
	
	public static BorderPane getRootLayout()
	{
		return root_layout;
	}
	
	public static Connection getConnection()
	{
		return conn;
	}
	
	public static void setConnection(Connection con)
	{
		Main.conn = con;
	}
	
	public static String getUserName()
	{
		return USER_NAME;
	}
	
	public static String getIP()
	{
		return IP;
	}
	
	public static String getPort()
	{
		return PORT;
	}
	
	public static String getDBName()
	{
		return DBNAME;
	}
	
	public static String getPassword()
	{
		return PASSWORD;
	}
	
	public static void setIP(String IP)
	{
		Main.IP = IP;
	}
	
	public static void setPort(String port)
	{
		Main.PORT = port;
	}
	
	public static void setDbName(String DBName)
	{
		Main.DBNAME = DBName;
	}
	
	public static void setUsername(String username)
	{
		Main.USER_NAME = username;
	}
	
	public static void setpassword(String password)
	{
		Main.PASSWORD = password;
	}
	
	@Override
	public void start(Stage primaryStage) 
	{
		try 
		{
			Main.primaryStage = primaryStage;
			showLogin();
		} 
		catch(Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	public void initRootLayout()
	{
		System.out.println("Hello");
		
		try
		{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/View/Root/RootLayout.fxml"));
			root_layout = (BorderPane) loader.load();
			Controller_Root_Layout controller = loader.getController();
			Scene scene = new Scene(root_layout);
			primaryStage.setScene(scene);
			primaryStage.show();
			controller.setStage(this);
		}
		catch(Exception E)
		{
			E.printStackTrace();
		}
	}
	
	public void showLogin()
	{
		try
		{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/View/Auth/Login.fxml"));
			AnchorPane page = (AnchorPane) loader.load();
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Add Database Details");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(stage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);
			Controller_Login controller = loader.getController();
			System.out.println("Hi!!\n");
			controller.setStage(dialogStage);
			dialogStage.showAndWait();
			boolean isDone = controller.returnIsDone();
			//TODO
			if(isDone)
			{
				Main.employee_info = controller.retEmp();
				if(employee_info.getCategory().equals("Administrator"))
				{
					initRootLayout();
					showDashboard();
				}
				else if(employee_info.getCategory().equals("Medical Officer"))
				{
					initRootLayout_MO();
					showDashboard_MO();
				}
				else if(employee_info.getCategory().equals("Receptionist"))
				{
					initRootLayout_R();
					showDashboard_R();
				}
				else
				{
					initRootLayout();
					showDashboard();
				}
			}
		}
		catch(Exception E)
		{
			E.printStackTrace();
		}
	}
	
	public void showDashboard_R() 
	{
		if(Main.employee_info == null)
		{
			return ;
		}
		try
		{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/View/Root/dashboard_receptionist.fxml"));
			AnchorPane anchor_pane = (AnchorPane) loader.load();
			root_layout.setCenter(anchor_pane);
			Controller_Dashboard_Receptionist controller = loader.getController();
			controller.setMainApp(this, employee_info);
		}
		catch(Exception E)
		{
			E.printStackTrace();
		}
	}

	public void initRootLayout_R() 
	{
		System.out.println("Hello");
		
		try
		{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/View/Root/RootLayout_R.fxml"));
			root_layout = (BorderPane) loader.load();
			Controller_Root_Layout_R controller = loader.getController();
			Scene scene = new Scene(root_layout);
			primaryStage.setScene(scene);
			primaryStage.show();
			controller.setStage(this);
		}
		catch(Exception E)
		{
			E.printStackTrace();
		}
	}

	public void showDashboard_MO()
	{
		if(Main.employee_info == null)
		{
			return ;
		}
		try
		{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/View/Root/dashboard_medical_officer.fxml"));
			AnchorPane anchor_pane = (AnchorPane) loader.load();
			root_layout.setCenter(anchor_pane);
			Controller_Dashboard_Medical_Officer controller = loader.getController();
			controller.setMainApp(this, employee_info);
		}
		catch(Exception E)
		{
			E.printStackTrace();
		}
	}

	public void initRootLayout_MO()
	{
		System.out.println("Hello");
		
		try
		{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/View/Root/RootLayout_MO.fxml"));
			root_layout = (BorderPane) loader.load();
			Controller_Root_Layout_MO controller = loader.getController();
			Scene scene = new Scene(root_layout);
			primaryStage.setScene(scene);
			primaryStage.show();
			controller.setStage(this);
		}
		catch(Exception E)
		{
			E.printStackTrace();
		}
	}

	public void showDashboard()
	{
		if(Main.employee_info == null)
		{
			return ;
		}
		try
		{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/View/Root/dashboard_administrator.fxml"));
			AnchorPane anchor_pane = (AnchorPane) loader.load();
			root_layout.setCenter(anchor_pane);
			Controller_Dashboard controller = loader.getController();
			controller.setMainApp(this, employee_info);
		}
		catch(Exception E)
		{
			E.printStackTrace();
		}
	}
	
	public void showDatabase() 
	{
		System.out.println("Showing Database");
		try
		{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/View/CMS/Database_Connectivity_Screen.fxml"));
			AnchorPane page = (AnchorPane) loader.load();
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Add Database Details");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(stage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);
			Controller_Manage_Database controller = loader.getController();
			System.out.println("Hi!!\n");
			controller.setStage(dialogStage);
			dialogStage.showAndWait();
		}
		catch(Exception E)
		{
			E.printStackTrace();
		}
	}

	public void showRemarks()
	{
		System.out.println("Showing Remarks");
		try
		{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/View/CMS/Remarks_Info.fxml"));
			AnchorPane anchor_pane = (AnchorPane) loader.load();
			root_layout.setCenter(anchor_pane);
			Controller_Manage_Remarks controller = loader.getController();
			controller.setMainApp(this);
		}
		catch(Exception E)
		{
			E.printStackTrace();
		}
	}
	
	public void showMedicines()
	{
		System.out.println("Showing Medicines");
		try
		{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/View/CMS/Medicine_Info.fxml"));
			AnchorPane anchor_pane = (AnchorPane) loader.load();
			root_layout.setCenter(anchor_pane);
			Controller_Manage_Medicine controller = loader.getController();
			controller.setMainApp(this);
		}
		catch(Exception E)
		{
			E.printStackTrace();
		}
	}
	
	public void showFees()
	{
		System.out.println("Showing Fees");
		try
		{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/View/CMS/Fees_Info.fxml"));
			AnchorPane anchor_pane = (AnchorPane) loader.load();
			root_layout.setCenter(anchor_pane);
			Controller_Manage_Fees controller = loader.getController();
			controller.setMainApp(this);
		}
		catch(Exception E)
		{
			E.printStackTrace();
		}
	}
	
	public void showTests()
	{
		System.out.println("Showing Tests");
		try
		{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/View/CMS/Tests_Info.fxml"));
			AnchorPane anchor_pane = (AnchorPane) loader.load();
			root_layout.setCenter(anchor_pane);
			Controller_Manage_Tests controller = loader.getController();
			controller.setMainApp(this);
		}
		catch(Exception E)
		{
			E.printStackTrace();
		}
	}
	
	
	/*
	 * Patient Related items
	 * Add a patient
	 * Search for a patient
	 */
	
	public void showAddPatient() 
	{
		System.out.println("Adding patient");
		try
		{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/View/Patient/Dialog_Add_Patient.fxml"));
			AnchorPane page = (AnchorPane) loader.load();
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Add Database Details");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(stage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);
			Controller_Add_Patient controller = loader.getController();
			controller.setPatient(new Patient_Info(), "ADD");
			System.out.println("Hi!!\n");
			controller.setStage(dialogStage);
			dialogStage.showAndWait();
		}
		catch(Exception E)
		{
			E.printStackTrace();
		}		
	}

	public void showSearchPatient() 
	{
		System.out.println("Showing Database");
		try
		{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/View/Patient/Search_Patient.fxml"));
			AnchorPane anchor_pane = (AnchorPane) loader.load();
			root_layout.setCenter(anchor_pane);
			Controller_Search_Patient controller = loader.getController();
			controller.setMainApp(this);
		}
		catch(Exception E)
		{
			E.printStackTrace();
		}		
	}
	
	/*
	 * Prescription
	 * Receipts
	 * Indoor Patients
	 */

	public void managePrescription() 
	{
		System.out.println("Choose the patient");
		try
		{
			FXMLLoader loader = new FXMLLoader();
			System.out.println("1");
			loader.setLocation(Main.class.getResource("/View/Prescription/Search_Patient.fxml"));
			System.out.println("2");
			AnchorPane anchor_pane = (AnchorPane) loader.load();
			root_layout.setCenter(anchor_pane);
			Controller_Search_Patient_Prescription controller = loader.getController();
			controller.setMainApp(this);
		}
		catch(Exception E)
		{
			E.printStackTrace();
		}			
	}

	public void manageReceipt() 
	{
		System.out.println("Manage Receipt");
		try
		{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/View/Receipt/home.fxml"));
			AnchorPane anchor_pane = (AnchorPane) loader.load();
			root_layout.setCenter(anchor_pane);
			Controller_Search_Patient_Receipt controller = loader.getController();
			controller.setMainApp(this);
		}
		catch(Exception E)
		{
			E.printStackTrace();
		}	
		
	}
	
	public void manage_indoor_patient()
	{
		System.out.println("Manage Indoor patient");
		try
		{
			FXMLLoader loader = new FXMLLoader();
			System.out.println("1");
			loader.setLocation(Main.class.getResource("/View/AdmitPatient/Search_Indoor_Patient.fxml"));
			System.out.println("2");
			AnchorPane anchor_pane = (AnchorPane) loader.load();
			root_layout.setCenter(anchor_pane);
			Controller_Indoor_Patient controller = loader.getController();
			controller.setMainApp(this);
		}
		catch(Exception E)
		{
			E.printStackTrace();
		}	
	}

	/*
	 * Manage Accounts
	 */
	
	public void manageAccount() 
	{
		System.out.println("Choose the patient");
		try
		{
			FXMLLoader loader = new FXMLLoader();
			System.out.println("1");
			loader.setLocation(Main.class.getResource("/View/Account/Account_Info.fxml"));
			System.out.println("2");
			AnchorPane anchor_pane = (AnchorPane) loader.load();
			root_layout.setCenter(anchor_pane);
			Controller_Account_Info controller = loader.getController();
		}
		catch(Exception E)
		{
			E.printStackTrace();
		}		
		
	}
	
	/*
	 * Manage Employees
	 */

	public void manageEmployee()
	{
		System.out.println("Manage Employees");
		try
		{
			FXMLLoader loader = new FXMLLoader();
			System.out.println("1");
			loader.setLocation(Main.class.getResource("/View/Employee/SearchEmployee.fxml"));
			System.out.println("2");
			AnchorPane anchor_pane = (AnchorPane) loader.load();
			root_layout.setCenter(anchor_pane);
			Controller_Search_Employee controller = loader.getController();
			controller.setMainApp(this);
		}
		catch(Exception E)
		{
			E.printStackTrace();
		}	
	}
	
	public Stage getStage()
	{
		return Main.primaryStage;
	}
	
	public Main()
	{
		
	}
	
	public static void main(String[] args)
	{
		launch(args);
	}
	
}
