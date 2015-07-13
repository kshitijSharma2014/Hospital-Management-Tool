package Controller.CMS;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import org.controlsfx.dialog.Dialogs;

import application.Main;
import Model.CMS.Medicine_Info;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

@SuppressWarnings("deprecation")
public class Controller_Manage_Medicine implements Initializable
{
	private ObservableList<Medicine_Info> medicineList = FXCollections.observableArrayList();
	private Stage primaryStage;
	private Main mainApp;
	
	@FXML AnchorPane anchor_pane = new AnchorPane();
	
	@FXML SplitPane split_pane = new SplitPane();
	
	@FXML TableView<Medicine_Info> table_view;
	
	@FXML TableColumn<Medicine_Info, String> med_name_col = new TableColumn<Medicine_Info, String> ();
	
	@FXML TableColumn<Medicine_Info, String> med_company_col = new TableColumn<Medicine_Info, String>();
	
	@FXML Button btn_add = new Button("Add New");
	
	@FXML Button btn_edit = new Button("Edit");
	
	@FXML Button btn_del = new Button("Delete");
	
	@FXML GridPane grid_pane = new GridPane();
	
	@FXML TextArea med_id = new TextArea();
	
	@FXML TextArea med_name = new TextArea();
	
	@FXML TextArea med_company = new TextArea();
	
	@FXML TextArea med_remarks = new TextArea();
	
	@FXML Label med_id_label = new Label();
	
	@FXML Label med_name_label = new Label();
	
	@FXML Label med_company_label = new Label();
	
	@FXML Label med_remarks_label = new Label();
	
	private boolean DelDB(Medicine_Info med_info) 
	{
		Connection con = Main.getConnection();
		if(con == null)
		{
				
			Dialogs.create()
    		.owner(primaryStage)
    		.title(" ALERT ")
    		.masthead(" Database is not setup ")
    		.message("Please set up the connection ")
    		.showWarning();
			return false;
		}
		try
		{
			String query = "DELETE FROM Medicine WHERE medicine_id=?;";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, med_info.get_med_id().getValue());
			int no = stmt.executeUpdate();
			System.out.println("No.of rows deleted: " + no);
			stmt.close();
		}
		catch(SQLException E)
		{
			Dialogs.create()
    		.owner(primaryStage)
    		.title(" ALERT ")
    		.masthead(" SQlException encountered ")
    		.message("Item could not be deleted ")
    		.showWarning();
			return false;
		}
		return true;
	}
	
	public void getFromDB()
	{
		
		Connection con = Main.getConnection();
		if(con == null)
		{
			Dialogs.create()
    		.owner(primaryStage)
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
				String query = "SELECT * FROM Medicine;";
				PreparedStatement stmt = con.prepareStatement(query);
				ResultSet rs = stmt.executeQuery();
				while(rs.next())
				{
					Medicine_Info med_info = new Medicine_Info(rs.getString("medicine_name"), rs.getString("company"), rs.getString("other_remarks"));
					med_info.set_med_id(rs.getString("medicine_ID"));
					medicineList.add(med_info);
				}
				stmt.close();
			}
			catch(SQLException E)
			{
				Dialogs.create()
	    		.owner(primaryStage)
	    		.title(" ALERT ")
	    		.masthead(" Database is not setup ")
	    		.message("Items could not be loaded.. ")
	    		.showWarning();
				return ;
			}
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) 
	{		
		getFromDB();
		
		med_id.setEditable(false);
		med_name.setEditable(false);
		med_company.setEditable(false);
		med_remarks.setEditable(false);
		
		med_name_col.setCellValueFactory(cellData -> cellData.getValue().get_med_name());
		med_company_col.setCellValueFactory(cellData -> cellData.getValue().get_med_company());
		
		table_view.setItems(medicineList);
		
		showSelectedMedicine(null);
		
		table_view.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showSelectedMedicine(newValue));

	}
	
	private void showSelectedMedicine(Medicine_Info med_info)
	{
		if(med_info == null)
		{
			med_id_label.setVisible(false);
			med_name_label.setVisible(false);
			med_company_label.setVisible(false);
			med_remarks_label.setVisible(false);
			
			med_id.setVisible(false);
			med_name.setVisible(false);
			med_company.setVisible(false);
			med_remarks.setVisible(false);
		}
		else
		{
			med_id_label.setVisible(true);
			med_name_label.setVisible(true);
			med_company_label.setVisible(true);
			med_remarks_label.setVisible(true);
			
			med_id.setVisible(true);
			med_name.setVisible(true);
			med_company.setVisible(true);
			med_remarks.setVisible(true);
						
			med_id.setText(med_info.get_med_id().getValue());
			med_name.setText(med_info.get_med_name().getValue());
			med_company.setText(med_info.get_med_company().getValue());
			
			med_remarks.setText(med_info.get_med_remarks().getValue());
			
			med_id_label.setWrapText(true);
			med_name_label.setWrapText(true);
			med_company_label.setWrapText(true);
			med_remarks_label.setWrapText(true);
			
			med_id.setWrapText(true);
			med_name.setWrapText(true);
			med_company.setWrapText(true);
			med_remarks.setWrapText(true);
		}
	}
	
	@FXML
	private void handle_btn_add()
	{
		Medicine_Info med_info = new Medicine_Info();
		boolean isSaveClicked = showDialogAddMedicine(med_info);
		
		if(isSaveClicked)
		{
			medicineList.add(med_info);
		}
	}
	
	private boolean showDialogAddMedicine(Medicine_Info med_info) 
	{
		boolean retValue = false;
		try
		{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/View/CMS/Dialog_Add_Medicine.fxml"));
			AnchorPane page = (AnchorPane) loader.load();
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Add New Medicine");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);
			Controller_Add_Medicine controller = loader.getController();
			System.out.println("Hi!!\n");
			controller.setStage(dialogStage);
			controller.setMedicine(med_info);
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
			Medicine_Info med_info = table_view.getItems().get(selectedIndex);
			boolean isSaveClicked = showDialogAddMedicine(med_info);
			if(isSaveClicked)
			{
				refreshTableView();
				table_view.getSelectionModel().select(med_info);
				showSelectedMedicine(med_info);
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
			Medicine_Info med = table_view.getItems().get(selectedIndex);
			
			boolean db_del = DelDB(med);
			if(db_del == false)
			{
				return ;
			}

			Medicine_Info med_info = table_view.getItems().remove(selectedIndex);
			System.out.println("Fees Type deleted: " + med_info.get_med_name().getValue());
		}
		else
		{
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(primaryStage);
			alert.setTitle("Nothing has been selected...");
			alert.setHeaderText("No Medicine Selected");
			alert.setContentText("Please select a medicine in the table.");
			
			alert.showAndWait();
		}
	}

	public void setMainApp(Main main) 
	{
		this.mainApp = main;
		this.primaryStage = mainApp.getStage();
	}
	
}
