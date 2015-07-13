package Controller.Employee;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

import org.controlsfx.dialog.Dialogs;

import application.Main;
import Controller.CMS.Controller_Add_Fees;
import Model.CMS.Fees_Info;
import Model.CMS.Tests_Info;
import Model.Employee.Employee_Info;
import Model.Patient.Patient_Info;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

@SuppressWarnings("deprecation")
public class Controller_Search_Employee implements Initializable
{
	private ObservableList<Employee_Info> employeeList = FXCollections.observableArrayList();
	private Main mainApp;
	private Stage stage;
	
	private int count = 1;
	
	@FXML TextField text_field = new TextField();
	@FXML TableView<Employee_Info> table_view = new TableView<Employee_Info>();
	@FXML TableColumn<Employee_Info, String> s_no_col = new TableColumn<Employee_Info, String>();
	@FXML TableColumn<Employee_Info, String> emp_name_col = new TableColumn<Employee_Info, String>();
	
	@FXML TextField emp_id = new TextField();
	@FXML TextField emp_name = new TextField();
	@FXML TextField emp_category = new TextField();
	@FXML TextField emp_date_birth = new TextField();
	@FXML TextField emp_marital = new TextField();
	@FXML TextField emp_salary = new TextField();
	@FXML TextField emp_date_joining = new TextField();
	@FXML TextField emp_contact = new TextField();
	@FXML TextField emp_address = new TextField();
	@FXML TextField emp_city = new TextField();
	@FXML TextField emp_state = new TextField();
	@FXML TextField emp_username = new TextField();
	
	@FXML Button btn_add = new Button();
	@FXML Button btn_edit = new Button();
	@FXML Button btn_delete = new Button();
	
	@FXML GridPane grid_pane = new GridPane();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) 
	{
		getFromDB();
		
		s_no_col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Employee_Info,String>, ObservableValue<String>>() 
			{
					@Override
					public ObservableValue<String> call(CellDataFeatures<Employee_Info, String> param) 
					{
						return new ReadOnlyObjectWrapper(table_view.getItems().indexOf(param.getValue())+1);
					}
		});
		
		emp_name_col.setCellValueFactory(cellData -> cellData.getValue().getFirst_name());
		
		table_view.setItems(employeeList);
		showSelectedEmployee(null);
		table_view.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showSelectedEmployee(newValue));
		
		text_field.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) 
			{
				if(newValue.length() >= 3 || newValue.length() < oldValue.length())
				{
					System.out.println("More than 3");
					
					employeeList.clear();
					System.out.println(employeeList.size());
					
					for(Employee_Info e : employeeList)
					{
						if(e.getFirst_name().getValue().contains(newValue))
						{
							employeeList.add(e);
						}
					}
					
					System.out.println(employeeList.size());
					if(newValue.length() > oldValue.length())
					{
						count ++;
					}
					else if(newValue.length() < oldValue.length())
					{
						if(count != 1)
						{
							count --;
						}
					}
					System.out.println("count :  " + count);
					refreshTableView();
				}
			}
		});
	}
	
	private void refreshTableView() 
	{
		for(TableColumn tc : table_view.getColumns())
		{
			tc.setVisible(false);
			tc.setVisible(true);
		}
	}
	
	private void showSelectedEmployee(Employee_Info emp_info)
	{
		if(emp_info == null)
		{
			grid_pane.setVisible(false);
		}
		else
		{
			grid_pane.setVisible(true);
			emp_id.setText(emp_info.getId().getValue());
			emp_name.setText(emp_info.getFirst_name().getValue());
			emp_category.setText(emp_info.getCategory().getValue());
			emp_date_birth.setText(emp_info.getBirth_date().getValue());
			emp_address.setText(emp_info.getAddress().getValue());
			emp_city.setText(emp_info.getCity().getValue());
			emp_state.setText(emp_info.getState().getValue());
			emp_contact.setText(emp_info.getContact_no().getValue());
			emp_date_joining.setText(emp_info.getDate_of_joining().toString());
			emp_marital.setText(emp_info.getMarital_status().getValue());
			emp_salary.setText(emp_info.getSalary().getValue());
			emp_username.setText(emp_info.getUsername().getValue());
			System.out.println(emp_info.getPassword().getValue());
		}
	}

	private void getFromDB()
	{
		Connection con = Main.getConnection();
		if(con == null)
		{
			Dialogs.create()
    		.owner(stage)
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
				String query = "SELECT * FROM Employee;";
				PreparedStatement stmt = con.prepareStatement(query);
				ResultSet rs = stmt.executeQuery();
				while(rs.next())
				{
					Employee_Info emp_info = new Employee_Info();
					emp_info.setId(new SimpleStringProperty(rs.getString("emp_id")));
					emp_info.setFirst_name(new SimpleStringProperty(rs.getString("Name")));
					emp_info.setCategory(new SimpleStringProperty(rs.getString("category")));
					emp_info.setBirth_date(new SimpleStringProperty(rs.getString("Birth_date")));
					emp_info.setMarital_status(new SimpleStringProperty(rs.getString("Marital_status")));
					emp_info.setSalary(new SimpleStringProperty(rs.getString("salary")));
					emp_info.setDate_of_joining(LocalDate.parse(rs.getString("date_of_joining")));
					emp_info.setContact_no(new SimpleStringProperty(rs.getString("phone")));
					emp_info.setAddress(new SimpleStringProperty(rs.getString("address")));
					emp_info.setCity(new SimpleStringProperty(rs.getString("city")));
					emp_info.setState(new SimpleStringProperty(rs.getString("state")));
					emp_info.setUsername(new SimpleStringProperty(rs.getString("username")));
					emp_info.setPassword(new SimpleStringProperty(rs.getString("password")));
					employeeList.add(emp_info);
				}
				stmt.close();
			}
			catch(SQLException E)
			{
				E.printStackTrace();
				Dialogs.create()
	    		.owner(stage)
	    		.title(" ALERT ")
	    		.masthead(" Database is not setup ")
	    		.message("Items could not be loaded.. ")
	    		.showWarning();
				return ;
			}
		}
	}
	
	@FXML
	private void handle_btn_add()
	{
		Employee_Info emp_info = new Employee_Info();
		boolean isSaveClicked = showDialogAddEmployee(emp_info, "ADD");
		if(isSaveClicked)
		{
			employeeList.add(emp_info);
		}
	}
	
	@FXML
	private void handle_btn_edit()
	{
		int selectedIndex = table_view.getSelectionModel().getSelectedIndex();
		if(selectedIndex < 0)
		{
			Dialogs.create()
    		.owner(stage)
    		.title(" ALERT ")
    		.masthead(" Error... ")
    		.message("Please select an item ")
    		.showWarning();
			return ;
		}
		else
		{
			Employee_Info emp_info = table_view.getItems().get(selectedIndex);
			boolean isSaveClicked = showDialogAddEmployee(emp_info, "EDIT");
			if(isSaveClicked)
			{
				refreshTableView();
				table_view.getSelectionModel().select(emp_info);
				showSelectedEmployee(emp_info);
			}
		}
	}
	
	@FXML
	private void handle_btn_delete()
	{
		int selectedIndex = table_view.getSelectionModel().getSelectedIndex();
		if(selectedIndex >= 0)
		{
			Employee_Info emp = table_view.getItems().get(selectedIndex);
			
			boolean db_del = DelDB(emp);
			if(db_del == false)
			{
				return ;
			}

			Employee_Info emp_info = table_view.getItems().remove(selectedIndex);
			System.out.println("Fees Type deleted: " + emp_info.getFirst_name().getValue());
		}
		else
		{
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(stage);
			alert.setTitle("Nothing has been selected...");
			alert.setHeaderText("No Item Selected");
			alert.setContentText("Please select a item in the table.");
			
			alert.showAndWait();
		}
	}
	
	private boolean DelDB(Employee_Info emp) 
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
    		.owner(stage)
    		.title(" ALERT ")
    		.masthead(" Database is not setup ")
    		.message("Please set up the connection ")
    		.showWarning();
			return false;
		}
		PreparedStatement stmt = null;
		try
		{
			String query = "DELETE FROM Employee WHERE emp_id=?;";
			stmt = con.prepareStatement(query);
			stmt.setString(1, table_view.getSelectionModel().getSelectedItem().getId().getValue());
			int no = stmt.executeUpdate(query);
			System.out.println("No.of rows deleted: " + no);
		}
		catch(SQLException E)
		{
			Dialogs.create()
    		.owner(stage)
    		.title(" ALERT ")
    		.masthead(" SQlException encountered ")
    		.message("Item could not be deleted ")
    		.showWarning();
			return false;
		}
		return true;
	}

	private boolean showDialogAddEmployee(Employee_Info emp_info, String mode)
	{
		boolean retValue = false;
		try
		{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/View/Employee/Dialog_Add_Employee.fxml"));
			AnchorPane page = (AnchorPane) loader.load();
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Add New Fees Type");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(stage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);
			Controller_Add_Employee controller = loader.getController();
			System.out.println("Hi!!\n");
			controller.setStage(dialogStage);
			controller.setEmployee(emp_info, mode);
			dialogStage.showAndWait();
			return controller.isSaveClicked();
			
		}
		catch(Exception E)
		{
			E.printStackTrace();
		}
		return retValue;
	}
	
	public void setMainApp(Main main)
	{
		this.mainApp = main;
		this.stage = mainApp.getStage();
	}

}
