package Controller.CMS;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import org.controlsfx.dialog.Dialogs;

import application.Main;
import Model.CMS.Remarks_Info;
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
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

@SuppressWarnings("deprecation")
public class Controller_Manage_Remarks implements Initializable
{
	private ObservableList<Remarks_Info> remarksList = FXCollections.observableArrayList();
	private Stage primaryStage;
	private Main mainApp;
	
	@FXML AnchorPane anchor_pane = new AnchorPane();
	
	@FXML SplitPane split_pane = new SplitPane();
	
	@FXML TableView<Remarks_Info> table_view;
	
	@FXML TableColumn<Remarks_Info, String> remark_eng_col = new TableColumn<Remarks_Info, String> ();
	
	@FXML TableColumn<Remarks_Info, String> remark_guj_col = new TableColumn<Remarks_Info, String>();
	
	@FXML Button btn_add = new Button("Add New");
	
	@FXML Button btn_edit = new Button("Edit");
	
	@FXML Button btn_del = new Button("Delete");
	
	@FXML GridPane grid_pane = new GridPane();
	
	@FXML TextArea remark_id = new TextArea();
	
	@FXML TextArea remark_eng = new TextArea();
	
	@FXML TextArea remark_guj = new TextArea();
	
	@FXML TextArea remark_context = new TextArea();
	
	@FXML Label remark_id_label = new Label();
	
	@FXML Label remark_eng_label = new Label();
	
	@FXML Label remark_guj_label = new Label();
	
	@FXML Label remark_context_label = new Label();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) 
	{		
		getFromDB();
		
		remark_id.setEditable(false);
		remark_eng.setEditable(false);
		
		Font gujju_font = Font.loadFont(getClass().getResourceAsStream("/Resources/Gujrati-Saral-1.ttf"), 20);
		remark_guj.setFont(gujju_font);
		remark_guj.setEditable(false);
		
		remark_context.setEditable(false);
		
		remark_eng_col.setCellValueFactory(cellData -> cellData.getValue().get_english_text());
		remark_guj_col.setCellValueFactory(cellData -> cellData.getValue().get_gujarati_text());
		
		table_view.setItems(remarksList);
		
		showSelectedRemark(null);
		
		table_view.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showSelectedRemark(newValue));

	}
	
	private void showSelectedRemark(Remarks_Info remark_info)
	{
		if(remark_info == null)
		{
			remark_id_label.setVisible(false);
			remark_eng_label.setVisible(false);
			remark_guj_label.setVisible(false);
			remark_context_label.setVisible(false);
			
			remark_id.setVisible(false);
			remark_eng.setVisible(false);
			remark_guj.setVisible(false);
			remark_context.setVisible(false);
		}
		else
		{
			remark_id_label.setVisible(true);
			remark_eng_label.setVisible(true);
			remark_guj_label.setVisible(true);
			remark_context_label.setVisible(true);
			
			remark_id.setVisible(true);
			remark_eng.setVisible(true);
			remark_guj.setVisible(true);
			remark_context.setVisible(true);
						
			remark_id.setText(remark_info.getRemarkID().getValue());
			remark_eng.setText(remark_info.get_english_text().getValue());
			remark_guj.setText(remark_info.get_gujarati_text().getValue());
			remark_context.setText(remark_info.get_context().getValue());
			
			remark_id_label.setWrapText(true);
			remark_eng_label.setWrapText(true);
			remark_guj_label.setWrapText(true);
			remark_context_label.setWrapText(true);
			
			remark_id.setWrapText(true);
			remark_eng.setWrapText(true);
			remark_guj.setWrapText(true);
			remark_context.setWrapText(true);
		}
	}
	
	@FXML
	private void handle_btn_add()
	{
		Remarks_Info remark_info = new Remarks_Info();
		boolean isSaveClicked = showDialogAddRemark(remark_info, "ADD");
		
		if(isSaveClicked)
		{
			remarksList.add(remark_info);
		}
	}
	
	private boolean showDialogAddRemark(Remarks_Info remark_info, String mode) 
	{
		boolean retValue = false;
		try
		{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/View/CMS/Dialog_Add_Remarks.fxml"));
			AnchorPane page = (AnchorPane) loader.load();
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Add New Remark");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);
			Controller_Add_Remarks controller = loader.getController();
			System.out.println("Hi!!\n");
			controller.setStage(dialogStage);
			controller.setRemark(remark_info, mode);
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
			Remarks_Info rem_info = table_view.getItems().get(selectedIndex);
			boolean isSaveClicked = showDialogAddRemark(rem_info, "EDIT");
			if(isSaveClicked)
			{
				refreshTableView();
				table_view.getSelectionModel().select(rem_info);
				showSelectedRemark(rem_info);
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
			Remarks_Info rem = table_view.getItems().get(selectedIndex);
			
			boolean db_del = DelDB(rem);
			if(db_del == false)
			{
				return ;
			}

			Remarks_Info rem_info = table_view.getItems().remove(selectedIndex);
			System.out.println("Reamrk deleted: " + rem_info.get_english_text().getValue());
		}
		else
		{
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(primaryStage);
			alert.setTitle("Nothing has been selected...");
			alert.setHeaderText("No Medicine Selected");
			alert.setContentText("Please select a remark in the table.");
			
			alert.showAndWait();
		}
	}

	private boolean DelDB(Remarks_Info rem) 
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
    		.owner(primaryStage)
    		.title(" ALERT ")
    		.masthead(" Database is not setup ")
    		.message("Please set up the connection ")
    		.showWarning();
			return false;
		}
		try
		{
			String query = "DELETE FROM Remarks WHERE remark_id=?;";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, rem.getRemarkID().getValue());
			int no = stmt.executeUpdate();
			System.out.println("No.of rows deleted: " + no);
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
				String query = "SELECT * FROM Remarks;";
				PreparedStatement stmt = con.prepareStatement(query);
				ResultSet rs = stmt.executeQuery();
				while(rs.next())
				{
					Remarks_Info rem_info = new Remarks_Info(rs.getString("English"), rs.getString("Gujarati"), rs.getString("Context"));
					rem_info.setRemarkID(rs.getString("remark_id"));
					remarksList.add(rem_info);
				}
				stmt.close();
			}
			catch(SQLException E)
			{
				Dialogs.create()
	    		.owner(primaryStage)
	    		.title(" ALERT ")
	    		.masthead(" Database is not setup ")
	    		.message("Items could not be loaded... ")
	    		.showWarning();
				return ;
			}
		}
	}


	public void setMainApp(Main main) 
	{
		this.mainApp = main;
		this.primaryStage = mainApp.getStage();
	}
	
}
