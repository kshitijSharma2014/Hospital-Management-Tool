package Controller.CMS;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import org.controlsfx.dialog.Dialogs;

import application.Main;
import Model.CMS.Tests_Info;
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
public class Controller_Manage_Tests implements Initializable
{
	private Main mainApp;
	private Stage primaryStage;
	private ObservableList<Tests_Info> testsList = FXCollections.observableArrayList();
	
	@FXML AnchorPane anchor_pane = new AnchorPane();
	
	@FXML TableView<Tests_Info> table_view = new TableView<Tests_Info>();
	
	@FXML TableColumn<Tests_Info, String> s_no_col = new TableColumn<Tests_Info, String> ();
	
	@FXML TableColumn<Tests_Info, String> tests_type_col = new TableColumn<Tests_Info, String> ();
	
	@FXML Label tests_id_label = new Label();
	
	@FXML Label tests_type_label = new Label();
	
	@FXML TextArea tests_id = new TextArea();
	
	@FXML TextArea tests_type = new TextArea();
	
	@FXML Button btn_add = new Button();
	
	@FXML Button btn_edit = new Button();
	
	@FXML Button btn_del = new Button();

	@Override
	public void initialize(URL location, ResourceBundle resources) 
	{
		getFromDB();
		tests_id.setEditable(false);
		tests_type.setEditable(false);
		s_no_col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Tests_Info,String>, ObservableValue<String>>() 
		{
			@Override
			public ObservableValue<String> call(CellDataFeatures<Tests_Info, String> param) 
			{
				return new ReadOnlyObjectWrapper(table_view.getItems().indexOf(param.getValue())+1);
			}
		});
		s_no_col.setSortable(false);
		tests_type_col.setCellValueFactory(cellData -> cellData.getValue().get_test_name());
		table_view.setItems(testsList);
		showSelectedTest(null);
		table_view.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showSelectedTest(newValue));
	}
	
	private void showSelectedTest(Tests_Info tests_info)
	{
		if(tests_info == null)
		{
			tests_id_label.setVisible(false);
			tests_type_label.setVisible(false);
			tests_id.setVisible(false);
			tests_type.setVisible(false);
		}
		else
		{
			tests_id_label.setVisible(true);
			tests_type_label.setVisible(true);
			tests_id.setVisible(true);
			tests_type.setVisible(true);
			
			tests_id.setText(tests_info.get_test_id().getValue());
			tests_type.setText(tests_info.get_test_name().getValue());
			
			tests_id.setWrapText(true);
			tests_type.setWrapText(true);
		}
	}
	
	@FXML
	private void handle_btn_add()
	{
		Tests_Info tests_info = new Tests_Info();
		boolean isSaveClicked = showDialogAddTests(tests_info);
		
		if(isSaveClicked)
		{
			testsList.add(tests_info);
		}
	}

	@FXML
	private void handle_btn_edit()
	{
		int selectedIndex = table_view.getSelectionModel().getSelectedIndex();
		if(selectedIndex >= 0)
		{
			Tests_Info test_info = table_view.getItems().get(selectedIndex);
			boolean isSaveClicked = showDialogAddTests(test_info);
			if(isSaveClicked)
			{
				refreshTableView();
				table_view.getSelectionModel().select(test_info);
				showSelectedTest(test_info);
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
	
	private boolean showDialogAddTests(Tests_Info test_info) 
	{
		boolean retValue = false;
		try
		{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/View/CMS/Dialog_Add_Tests.fxml"));
			AnchorPane page = (AnchorPane) loader.load();
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Add New Fees Type");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);
			Controller_Add_Tests controller = loader.getController();
			System.out.println("Hi!!\n");
			controller.setAppStage(dialogStage);
			controller.setTest(test_info);
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
			Tests_Info test = table_view.getItems().get(selectedIndex);
			
			boolean db_del = DelDB(test);
			if(db_del == false)
			{
				return ;
			}

			Tests_Info test_info = table_view.getItems().remove(selectedIndex);
			System.out.println("Fees Type deleted: " + test_info.get_test_name().getValue());
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
				String query = "SELECT * FROM Test_Info;";
				PreparedStatement stmt = con.prepareStatement(query);
				ResultSet rs = stmt.executeQuery();
				while(rs.next())
				{
					Tests_Info test_info = new Tests_Info(rs.getString("test_name"));
					test_info.setID(rs.getString("test_id"));
					testsList.add(test_info);
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
	
	private boolean DelDB(Tests_Info test) 
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
			String query = "DELETE FROM Test_Info WHERE test_id=?;";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, test.get_test_id().getValue());
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

	public void setMainApp(Main main)
	{
		this.mainApp = main;
		this.primaryStage = mainApp.getStage();
	}

}
