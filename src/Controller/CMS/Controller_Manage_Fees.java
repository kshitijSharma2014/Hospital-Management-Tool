package Controller.CMS;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import org.controlsfx.dialog.Dialogs;

import application.Main;
import Model.CMS.Fees_Info;
import javafx.beans.property.ReadOnlyObjectWrapper;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

@SuppressWarnings("deprecation")
public class Controller_Manage_Fees implements Initializable
{
	private Main mainApp;
	private Stage primaryStage;
	private ObservableList<Fees_Info> feesList = FXCollections.observableArrayList();
	
	@FXML AnchorPane anchor_pane = new AnchorPane();
	
	@FXML TableView<Fees_Info> table_view = new TableView<Fees_Info>();
	
	@FXML TableColumn<Fees_Info, String> s_no_col = new TableColumn<Fees_Info, String> ();
	
	@FXML TableColumn<Fees_Info, String> fees_type_col = new TableColumn<Fees_Info, String> ();
	
	@FXML Label fees_id_label = new Label();
	
	@FXML Label fees_type_label = new Label();
	
	@FXML TextArea fees_id = new TextArea();
	
	@FXML TextArea fees_type = new TextArea();
	
	@FXML Button btn_add = new Button();
	
	@FXML Button btn_edit = new Button();
	
	@FXML Button btn_del = new Button();

	@Override
	public void initialize(URL location, ResourceBundle resources) 
	{
		getFromDB();
		fees_id.setEditable(false);
		fees_type.setEditable(false);
		s_no_col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Fees_Info,String>, ObservableValue<String>>() 
		{
			@Override
			public ObservableValue<String> call(CellDataFeatures<Fees_Info, String> param) 
			{
				return new ReadOnlyObjectWrapper(table_view.getItems().indexOf(param.getValue())+1);
			}
		});
		s_no_col.setSortable(false);
		fees_type_col.setCellValueFactory(cellData -> cellData.getValue().get_fees_name());
		table_view.setItems(feesList);
		showSelectedFees(null);
		table_view.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showSelectedFees(newValue));
	}
	
	private void showSelectedFees(Fees_Info fee_info)
	{
		if(fee_info == null)
		{
			fees_id_label.setVisible(false);
			fees_type_label.setVisible(false);
			fees_id.setVisible(false);
			fees_type.setVisible(false);
		}
		else
		{
			fees_id_label.setVisible(true);
			fees_type_label.setVisible(true);
			fees_id.setVisible(true);
			fees_type.setVisible(true);
			
			fees_id.setText(fee_info.get_fees_id().getValue());
			fees_type.setText(fee_info.get_fees_name().getValue());
			
			fees_id.setWrapText(true);
			fees_type.setWrapText(true);
		}
	}
	
	@FXML
	private void handle_btn_add()
	{
		Fees_Info fee_info = new Fees_Info();
		boolean isSaveClicked = showDialogAddFees(fee_info);
		
		if(isSaveClicked)
		{
			feesList.add(fee_info);
		}
	}

	@FXML
	private void handle_btn_edit()
	{
		int selectedIndex = table_view.getSelectionModel().getSelectedIndex();
		if(selectedIndex >= 0)
		{
			Fees_Info fee_info = table_view.getItems().get(selectedIndex);
			boolean isSaveClicked = showDialogAddFees(fee_info);
			if(isSaveClicked)
			{
				refreshTableView();
				table_view.getSelectionModel().select(fee_info);
				showSelectedFees(fee_info);
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
	
	private boolean showDialogAddFees(Fees_Info fee_info) 
	{
		boolean retValue = false;
		try
		{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/View/CMS/Dialog_Add_Fees.fxml"));
			AnchorPane page = (AnchorPane) loader.load();
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Add New Fees Type");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);
			Controller_Add_Fees controller = loader.getController();
			System.out.println("Hi!!\n");
			controller.setStage(dialogStage);
			controller.setFees(fee_info);
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
	private void handle_btn_del()
	{
		int selectedIndex = table_view.getSelectionModel().getSelectedIndex();
		if(selectedIndex >= 0)
		{
			Fees_Info fee = table_view.getItems().get(selectedIndex);
			
			boolean db_del = DelDB(fee);
			if(db_del == false)
			{
				return ;
			}

			Fees_Info fee_info = table_view.getItems().remove(selectedIndex);
			System.out.println("Fees Type deleted: " + fee_info.get_fees_name().getValue());
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
	
	/*
	 * --------------------------------------------------------------------------------------
	 * DATABSE OPERATIONS
	 * @author: Devanshu Jain
	 * --------------------------------------------------------------------------------------
	 */
	
	private boolean DelDB(Fees_Info fee) 
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
		PreparedStatement stmt = null;
		try
		{
			String query = "DELETE FROM Fee_type WHERE fee_id=?;";
			stmt = con.prepareStatement(query);
			stmt.setString(1, fee.get_fees_id().getValue());
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

	private void getFromDB()
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
				String query = "SELECT * FROM Fee_type;";
				PreparedStatement stmt = con.prepareStatement(query);
				ResultSet rs = stmt.executeQuery();
				while(rs.next())
				{
					Fees_Info fee_info = new Fees_Info(rs.getString("fee_name"));
					fee_info.setID(rs.getString("fee_id"));
					feesList.add(fee_info);
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
