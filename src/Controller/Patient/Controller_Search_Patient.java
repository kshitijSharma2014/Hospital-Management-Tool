package Controller.Patient;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import org.controlsfx.dialog.Dialogs;

import application.Main;
import Controller.CMS.Controller_Add_Fees;
import Model.CMS.Fees_Info;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

@SuppressWarnings("deprecation")
public class Controller_Search_Patient implements Initializable
{
	private ObservableList<Patient_Info> patientList = FXCollections.observableArrayList();
	private ObservableList<Patient_Info> allPatients = FXCollections.observableArrayList();
	private Stage stage;
	private Main mainApp;
	private int count = 1;
	
	@FXML TextField search;
	@FXML TableView<Patient_Info> table_view;
	@FXML TableColumn<Patient_Info, String> s_no_col;
	@FXML TableColumn<Patient_Info, String> patient_name_col;
	
	@FXML TextField name;
	@FXML Label id_placeholder;
	@FXML TextArea birth_date, sex, marital_status, phone;
	@FXML TextArea emergency_name, emergency_relation, emergency_contact;
	@FXML TextArea address, city, state;	
	
	@FXML Button btn_choose;
	
	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		getFromDB();
		s_no_col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Patient_Info,String>, ObservableValue<String>>() 
		{
			@Override
			public ObservableValue<String> call(CellDataFeatures<Patient_Info, String> param) 
			{
				return new ReadOnlyObjectWrapper(table_view.getItems().indexOf(param.getValue())+1);
			}
		});
		s_no_col.setSortable(false);
		patient_name_col.setCellValueFactory(cellData -> cellData.getValue().getFirst_name());
		table_view.setItems(patientList);
		showSelectedPatient(null);
		table_view.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showSelectedPatient(newValue));
		
		search.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) 
			{
				if(newValue.length() >= 3 || newValue.length() < oldValue.length())
				{
					System.out.println("More than 3");
					
					patientList.clear();
					System.out.println(patientList.size());
					System.out.println("New value: " + newValue);
					for(Patient_Info p : allPatients)
					{
						System.out.println(p.getFirst_name().getValue());
						if(p.getFirst_name().getValue().toLowerCase().contains(newValue.toLowerCase()))
						{
							patientList.add(p);
						}
					}
					
					System.out.println(patientList.size());
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
	
	@FXML
	private void handle_btn_add()
	{
		Patient_Info pat_info = new Patient_Info();
		boolean isSaveClicked = showDialogAddPatient(pat_info, "ADD");
		
		if(isSaveClicked)
		{
			patientList.add(pat_info);
		}
	}
	
	private boolean showDialogAddPatient(Patient_Info pat_info, String mode)
	{
		boolean retValue = false;
		try
		{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/View/Patient/Dialog_Add_Patient.fxml"));
			AnchorPane page = (AnchorPane) loader.load();
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Add New Patient");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(stage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);
			Controller_Add_Patient controller = loader.getController();
			System.out.println("Hi!!\n");
			controller.setStage(dialogStage);
			controller.setPatient(pat_info, mode);
			dialogStage.showAndWait();
			return controller.isSaveClicked();
			
		}
		catch(Exception E)
		{
			E.printStackTrace();
		}
		return retValue;
	}
	
	@FXML
	private void handle_btn_edit()
	{
		int selectedindex = table_view.getSelectionModel().getSelectedIndex();
		if(selectedindex < 0)
		{
			Dialogs.create()
    		.owner(stage)
    		.title(" ALERT ")
    		.masthead(" Nothing is selected ")
    		.message("Please select a patient to edit... ")
    		.showWarning();
			return ;
		}
		else
		{
			Patient_Info pat_info = table_view.getSelectionModel().getSelectedItem();
			boolean isSaved = showDialogAddPatient(pat_info, "EDIT");
			if(isSaved)
			{
				refreshTableView();
				table_view.getSelectionModel().select(pat_info);
				showSelectedPatient(pat_info);
			}
		}
	}
	
	@FXML
	private void handle_btn_del()
	{
		int selectedIndex = table_view.getSelectionModel().getSelectedIndex();
		if(selectedIndex >= 0)
		{
			Patient_Info pat = table_view.getItems().get(selectedIndex);
			
			boolean db_del = DelDB(pat);
			if(db_del == false)
			{
				return ;
			}

			Patient_Info pat_info = table_view.getItems().remove(selectedIndex);
			System.out.println("Patient deleted: " + pat_info.getFirst_name().getValue());
		}
		else
		{
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(stage);
			alert.setTitle("Nothing has been selected...");
			alert.setHeaderText("No Patient Selected");
			alert.setContentText("Please select a patient in the table.");
			
			alert.showAndWait();
		}
	}
		
	private boolean DelDB(Patient_Info pat) 
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
			String query = "DELETE FROM Patient WHERE pat_id=?;";
			stmt = con.prepareStatement(query);
			stmt.setString(1, pat.getPat_id().getValue());
			int no = stmt.executeUpdate();
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

	private void refreshTableView() 
	{
		for(TableColumn tc : table_view.getColumns())
		{
			tc.setVisible(false);
			tc.setVisible(true);
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
			PreparedStatement stmt = null;
			try
			{
				String query = "SELECT * FROM Patient;";
				stmt = con.prepareStatement(query);
				ResultSet rs = stmt.executeQuery();
				while(rs.next())
				{
					System.out.println("hello");
					Patient_Info pat_info = new Patient_Info();
					pat_info.setFirst_name(new SimpleStringProperty(rs.getString("name")));
	                pat_info.setAddress(new SimpleStringProperty(rs.getString("Address")));
	                pat_info.setBirth_date((rs.getDate("Birth_date").toLocalDate()));
	                pat_info.setCity(new SimpleStringProperty(rs.getString("City")));
	                pat_info.setState(new SimpleStringProperty(rs.getString("State")));
	                pat_info.setEmergency_contact(new SimpleStringProperty(rs.getString("Emergency_contact")));
	                pat_info.setEmergency_name(new SimpleStringProperty(rs.getString("Emergency_name")));
	                pat_info.setEmergency_relation(new SimpleStringProperty(rs.getString("Emergency_relation")));
	                pat_info.setMarital_status(new SimpleStringProperty(rs.getString("Marital_status")));
	                pat_info.setPhone(new SimpleStringProperty(rs.getString("Phone")));
	                pat_info.setSex(new SimpleStringProperty(rs.getString("Sex")));
	                pat_info.setPat_id(new SimpleStringProperty(rs.getString("pat_ID")));
					allPatients.add(pat_info);
				}
				patientList.addAll(allPatients);
				System.out.println(patientList.size());
			}
			catch(SQLException E)
			{
				Dialogs.create()
	    		.owner(stage)
	    		.title(" ALERT ")
	    		.masthead(" Database is not setup ")
	    		.message("Items could not be loaded... ")
	    		.showWarning();
				return ;
			}
		}
	}

	private void showSelectedPatient(Patient_Info pat_info)
	{
		if(pat_info == null)
		{
			name.setVisible(false);
			id_placeholder.setVisible(false);
			birth_date.setVisible(false);
			sex.setVisible(false);
			marital_status.setVisible(false);
			phone.setVisible(false);
			emergency_name.setVisible(false);
			emergency_contact.setVisible(false);
			emergency_relation.setVisible(false);
			address.setVisible(false);
			city.setVisible(false);
			state.setVisible(false);
		}
		else
		{
			name.setVisible(true);
			id_placeholder.setVisible(true);
			birth_date.setVisible(true);
			sex.setVisible(true);
			marital_status.setVisible(true);
			phone.setVisible(true);
			emergency_name.setVisible(true);
			emergency_contact.setVisible(true);
			emergency_relation.setVisible(true);
			address.setVisible(true);
			city.setVisible(true);
			state.setVisible(true);
			
			name.setText(pat_info.getFirst_name().getValue() + ", " + pat_info.getMiddle_name().getValue() + ", " + pat_info.getLast_name().getValue());
			id_placeholder.setText(pat_info.getPat_id().getValue());
			birth_date.setText(pat_info.getBirth_date().toString());
			sex.setText(pat_info.getSex().getValue());
			marital_status.setText(pat_info.getMarital_status().getValue());
			phone.setText(pat_info.getPhone().getValue());
			emergency_name.setText(pat_info.getEmergency_name().getValue());
			emergency_contact.setText(pat_info.getEmergency_contact().getValue());
			emergency_relation.setText(pat_info.getEmergency_relation().getValue());
			address.setText(pat_info.getAddress().getValue());
			city.setText(pat_info.getCity().getValue());
			state.setText(pat_info.getState().getValue());			
		}
	}
	
	public void setMainApp(Main main)
	{
		this.mainApp = main;
		this.stage = mainApp.getStage();
	}
	
}
