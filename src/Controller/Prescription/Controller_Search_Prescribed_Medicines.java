package Controller.Prescription;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.controlsfx.dialog.Dialogs;

import Model.CMS.Medicine_Info;
import Model.Patient.Medicine_Prescribed;
import Model.Patient.Patient_Info;
import Model.Patient.Prescription;
import application.Main;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

@SuppressWarnings("deprecation")
public class Controller_Search_Prescribed_Medicines implements Initializable
{

	private Prescription pres_info;
	private Patient_Info pat_info;
	private ObservableList<Medicine_Prescribed> prescriptionList = FXCollections.observableArrayList();
	private ObservableList<Medicine_Info> medicineList = FXCollections.observableArrayList();
	private Stage stage;
	private Main mainApp;
	
	@FXML GridPane grid_pane = new GridPane();
	@FXML TableView<Medicine_Prescribed> table_view = new TableView<Medicine_Prescribed> ();
	@FXML TableColumn<Medicine_Prescribed, String> s_no_col = new TableColumn<Medicine_Prescribed, String> ();
	@FXML TableColumn<Medicine_Prescribed, String> med_name_col = new TableColumn<Medicine_Prescribed, String> ();
	
	@FXML TextField date = new TextField();
	@FXML TextField time = new TextField();
	
	@FXML TextArea med_name = new TextArea();
	@FXML TextArea morning_amt = new TextArea();
	@FXML TextArea noon_amt = new TextArea();
	@FXML TextArea evening_amt = new TextArea();
	
	@FXML Label morning_meal = new Label();
	@FXML Label noon_meal = new Label();
	@FXML Label evening_meal = new Label();
	
	@FXML Button btn_add = new Button();
	@FXML Button btn_edit = new Button();
	@FXML Button btn_del = new Button();
	
	@FXML Button btn_back = new Button();
	
	@FXML TextField patient_name = new TextField(), patient_id = new TextField();
	
	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		date.setEditable(false);
		time.setEditable(false);
		patient_name.setEditable(false);
		patient_id.setEditable(false);
		
		morning_amt.setEditable(false);
		noon_amt.setEditable(false);
		evening_amt.setEditable(false);
		med_name.setEditable(false);
		
		s_no_col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Medicine_Prescribed,String>, ObservableValue<String>>() 
		{
			@Override
			public ObservableValue<String> call(CellDataFeatures<Medicine_Prescribed, String> param) 
			{
				return new ReadOnlyObjectWrapper(table_view.getItems().indexOf(param.getValue())+1);
			}
		});
		s_no_col.setSortable(false);
		med_name_col.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMedicine().get_med_name().getValue()));
		table_view.setItems(prescriptionList);
		showSelectedPrescription(null);
		table_view.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showSelectedPrescription(newValue));		
	}
	
	@FXML
	private void handle_btn_add()
	{
		Medicine_Prescribed med_pres_info = new Medicine_Prescribed();
		med_pres_info.setPat_id(pres_info.getPat_id());
		med_pres_info.setDate(pres_info.getDate());
		med_pres_info.setTime(pres_info.getTime());
		med_pres_info.setMedicine(new Medicine_Info("", "", ""));
		boolean isSaveClicked = showDialogAddPrescription(med_pres_info, "ADD");
		
		if(isSaveClicked)
		{
			prescriptionList.add(med_pres_info);
		}
	}
	
	private boolean showDialogAddPrescription(Medicine_Prescribed med_pres_info, String mode)
	{
		boolean retValue = false;
		try
		{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/View/Prescription/Add_Medicines.fxml"));
			AnchorPane page = (AnchorPane) loader.load();
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Add New Fees Type");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(stage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);
			Controller_Add_Prescribed_Medicines controller = loader.getController();
			System.out.println("Hi!!\n");
			controller.setStage(dialogStage);			
			controller.setPrescription(med_pres_info, pat_info, mode);
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
		int selectedIndex = table_view.getSelectionModel().getSelectedIndex();
		if(selectedIndex >= 0)
		{
			Medicine_Prescribed med_pres_info = table_view.getItems().get(selectedIndex);
			System.out.println("Medicine ID: " + med_pres_info.getMedicine().get_med_id().getValue());
			boolean isSaveClicked = showDialogAddPrescription(med_pres_info, "EDIT");
			if(isSaveClicked)
			{
				refreshTableView();
				table_view.getSelectionModel().select(med_pres_info);
				showSelectedPrescription(med_pres_info);
			}
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
	
	@FXML
	private void handle_btn_del()
	{
		int selectedIndex = table_view.getSelectionModel().getSelectedIndex();
		if(selectedIndex >= 0)
		{
			Medicine_Prescribed med_pres = table_view.getItems().get(selectedIndex);
			
			boolean db_del = DelDB(med_pres);
			if(db_del == false)
			{
				return ;
			}

			Medicine_Prescribed med_pres_info = table_view.getItems().remove(selectedIndex);
			System.out.println("Deleted info: " + med_pres_info.getDate().toString());
		}
		else
		{
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(stage);
			alert.setTitle("Nothing has been selected...");
			alert.setHeaderText("No Medicine Selected");
			alert.setContentText("Please select a medicine in the table.");
			
			alert.showAndWait();
		}
	}
	
	private boolean DelDB(Medicine_Prescribed med_pres) 
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
			String query = "DELETE FROM Medicine_prescribed WHERE pat_ID=? AND date=? AND time=? AND medicine_id=?;";
			stmt = con.prepareStatement(query);
			stmt.setString(1, med_pres.getPat_id().getValue());
			stmt.setString(2, med_pres.getDate().toString());
			stmt.setString(3, med_pres.getTime().getValue());
			stmt.setString(4, med_pres.getMedicine().get_med_id().getValue());
			int no = stmt.executeUpdate();
			System.out.println("No.of rows deleted: " + no);
		}
		catch(SQLException E)
		{
			E.printStackTrace();
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
	
	private void showSelectedPrescription(Medicine_Prescribed med_pres_info)
	{
		if(med_pres_info == null)
		{
			grid_pane.setVisible(false);
		}
		else
		{
			grid_pane.setVisible(true);
			
			med_name.setText(med_pres_info.getMedicine().toString());
			morning_amt.setText(med_pres_info.getMorning_amt().getValue());
			noon_amt.setText(med_pres_info.getNoon_amt().getValue());
			evening_amt.setText(med_pres_info.getEvening_amt().getValue());
			
			med_name.setWrapText(true);
			morning_amt.setWrapText(true);
			noon_amt.setWrapText(true);
			evening_amt.setWrapText(true);
			
			if(med_pres_info.getMorning_meal().getValue().equals("true"))
			{
				morning_meal.setText("After Meal");
			}
			else
			{
				morning_meal.setText("Before Meal");
			}
			
			if(med_pres_info.getNoon_meal().getValue().equals("true"))
			{
				noon_meal.setText("After Meal");
			}
			else
			{
				noon_meal.setText("Before Meal");
			}
			
			if(med_pres_info.getEvening_meal().getValue().equals("true"))
			{
				evening_meal.setText("After Meal");
			}
			else
			{
				evening_meal.setText("Before Meal");
			}
		}
	}
	
	public void setPrescription(Prescription pres_info, Patient_Info pat_info)
	{
		this.pres_info = pres_info;
		this.pat_info = pat_info;
		
		getFromDB();

		System.out.println("Setting");
		date.setText(pres_info.getDate().toString());
		time.setText(pres_info.getTime().getValue());
		patient_name.setText(pat_info.getFirst_name().getValue() + ", " + pat_info.getLast_name().getValue());
		patient_id.setText(pat_info.getPat_id().getValue());
	}
	
	public void setMainApp(Main main)
	{
		this.mainApp = main;
		this.stage = mainApp.getStage();
	}

	private void getFromDB()
	{
//		Medicine_Prescribed mp1 = new Medicine_Prescribed();
//		mp1.setDate(LocalDate.of(1994, 10, 18));
//		mp1.setPat_id(new SimpleStringProperty("1"));
//		mp1.setMedicine(new Medicine_Info("blah", "blah", "hlaj"));
//				
//		prescriptionList.add(mp1);
		
		Connection con = Main.getConnection();
		if(con == null)
		{
			Dialogs.create()
    		.owner(stage)
    		.title(" ALERT ")
    		.masthead(" Database is not setup ")
    		.message("Please set up the connection ")
    		.showWarning();
		}
		else
		{
			String query_med = "SELECT * FROM Medicine;";
			String query_prescribed_med = "SELECT * FROM Medicine_Prescribed WHERE pat_ID=?";
			try
			{
				PreparedStatement stmt_med = con.prepareStatement(query_med);
				PreparedStatement stmt_prescribed_med = con.prepareStatement(query_prescribed_med);
				
				ResultSet rs = stmt_med.executeQuery();
				while(rs.next())
				{
					Medicine_Info mi = new Medicine_Info();
					mi.set_med_id(rs.getString("medicine_ID"));
					mi.set_med_name(rs.getString("medicine_name"));
					mi.set_med_cmpy(rs.getString("company"));
					mi.set_med_remarks(rs.getString("other_remarks"));
					medicineList.add(mi);
				}
				System.out.println("its 1");
				
				stmt_prescribed_med.setString(1, pat_info.getPat_id().getValue());
				System.out.println(stmt_prescribed_med.toString());
				ResultSet rs2 = stmt_prescribed_med.executeQuery();
				while(rs2.next())
				{
					Medicine_Prescribed mp = new Medicine_Prescribed();
					System.out.println("its 1.1");
					mp.setDate(rs2.getDate("date").toLocalDate());
					System.out.println("its 1.2");
					mp.setTime(new SimpleStringProperty(rs2.getString("time")));
					mp.setPat_id(new SimpleStringProperty(rs2.getString("pat_ID")));
					System.out.println("In the database: " + rs2.getString("medicine_ID"));
					for(Medicine_Info mi : medicineList)
					{
						if(mi.get_med_id().getValue().equals(rs2.getString("medicine_ID")))
						{
							System.out.println(mi.get_med_id().getValue());
							System.out.println("Its equal!!!");
							mp.setMedicine(mi);
						}
					}
					System.out.println("its 2");
					mp.setMorning_amt(new SimpleStringProperty(rs2.getString("morning_amt")));
					mp.setNoon_amt(new SimpleStringProperty(rs2.getString("noon_amt")));
					mp.setEvening_amt(new SimpleStringProperty(rs2.getString("evening_amt")));
					mp.setMorning_meal(new SimpleStringProperty(rs2.getString("is_morning")));
					mp.setNoon_meal(new SimpleStringProperty(rs2.getString("is_noon")));
					mp.setEvening_meal(new SimpleStringProperty(rs2.getString("is_evening")));
					prescriptionList.add(mp);
				}
			}
			catch(SQLException E)
			{
				E.printStackTrace();
				Dialogs.create()
	    		.owner(stage)
	    		.title(" ALERT ")
	    		.masthead(" Problem in the connection ")
	    		.message("Please set up the connection again.")
	    		.showWarning();
				System.out.println("Setting nullllll");
				Main.setConnection(null);
				Main.setDbName("");
				Main.setUsername("");
				Main.setpassword("");
				Main.setPort("");
			}
		}
	}
	
	@FXML
	private void handle_btn_back()
	{
		try
		{
			FXMLLoader loader = new FXMLLoader();
			System.out.println("1");
			loader.setLocation(Main.class.getResource("/View/Prescription/Search_Prescription.fxml"));
			System.out.println("2");
			AnchorPane anchor_pane = (AnchorPane) loader.load();
			Main.getRootLayout().setCenter(anchor_pane);
			Controller_Search_Prescription controller = loader.getController();
			controller.setMainApp(mainApp);
			controller.setPatient(pat_info, "MANAGING_PRESCRIPTIONS");
			Main.getRootLayout().setCenter(anchor_pane);
		}
		catch(Exception E)
		{
			E.printStackTrace();
		}
	}
	
}
