package Controller.IndoorPatient;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import org.controlsfx.dialog.Dialogs;


import application.Main;
//import application.Main;
import Model.Patient.Patient_Info;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.stage.Stage;
import javafx.util.Callback;

@SuppressWarnings("deprecation")
public class HomeController implements Initializable
{
	private Main mainApp;
	public static Patient_Info pat_info = new Patient_Info();
	
	public static String dateID, timeID, medName, testName, value;
	
	private ObservableList<Patient_Info> patientList = FXCollections.observableArrayList();
	private ObservableList<Patient_Info> allPatients = FXCollections.observableArrayList();
	//private Main mainApp;
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
	
	@FXML Button btn_report;
        @FXML Button btn_chart;
        @FXML Button btn_test;
	
	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
            try {
                getFromDB();
            } catch (SQLException ex) {
                Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
            }
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
					String str1=newValue.toUpperCase();
                                        
					for(Patient_Info p : allPatients)
					{
						if(p.getFirst_name().getValue().toUpperCase().contains(str1))
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
    private void get_chart(ActionEvent e) throws IOException
    {
        // check for valid patient
        if(table_view.getSelectionModel().getSelectedIndex() < 0)
        {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Something is not good");
            alert.setHeaderText("No Patient Selected");
            alert.setContentText("Please select a patient!");

            alert.showAndWait();
        }
        else{
        Parent chart = FXMLLoader.load(getClass().getResource("/View/IndoorPatient/chart.fxml"));
        Scene scene_chart = new Scene(chart);
        Stage stage_chart = (Stage) btn_chart.getScene().getWindow();
        stage_chart.setScene(scene_chart);
        stage_chart.show();
    }}
     @FXML
    private void get_reports(ActionEvent e) throws IOException
    {
        if(table_view.getSelectionModel().getSelectedIndex() < 0)
        {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Something is not good");
            alert.setHeaderText("No Patient Selected");
            alert.setContentText("Please select a patient!");

            alert.showAndWait();
        }
        else
        {
	        // check for valid patient
	        Parent chart = FXMLLoader.load(getClass().getResource("/View/IndoorPatient/TestReports.fxml"));
	        Scene scene_chart = new Scene(chart);
	        Stage stage_chart = (Stage) btn_chart.getScene().getWindow();
	        stage_chart.setScene(scene_chart);
	        stage_chart.show();
        }
    }
    
		
	private void refreshTableView() 
	{
		for(TableColumn tc : table_view.getColumns())
		{
			tc.setVisible(false);
			tc.setVisible(true);
		}
	}
	
	private void getFromDB() throws SQLException, ClassNotFoundException
	{
	
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
			PreparedStatement stmt = null;
			try
			{
				String query = "SELECT * FROM Patient p WHERE p.pat_ID = (SELECT ip.pat_ID FROM Indoor_patient ip);";
				System.out.println(query);
				stmt = con.prepareStatement(query);
				ResultSet rs = stmt.executeQuery();
				while(rs.next())
				{
					Patient_Info pat_info = new Patient_Info();
	                String temp=rs.getString("name");
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
	                System.out.println(temp);
	                patientList.add(pat_info);
	                allPatients.add(pat_info);
				}
				stmt.close();
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
            this.pat_info = new Patient_Info();
            this.pat_info.setPat_id(pat_info.getPat_id());
			
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
			
            System.out.println(pat_info.getSex());
			name.setText(pat_info.getFirst_name().getValue() + " " + pat_info.getMiddle_name().getValue() + " " + pat_info.getLast_name().getValue());
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
	}
	
	
	
}
