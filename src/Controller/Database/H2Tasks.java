package Controller.Database;
 
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.*;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import application.Main;
import Model.Employee.Employee_Info;
 
public class H2Tasks
{ 
	private static final Logger logger = Logger.getLogger(H2Tasks.class.getName());

	private static ExecutorService databaseExecutor;
 
	private Future databaseSetupFuture;
	   
	public void init(String table, String query) throws Exception
	{
		databaseExecutor = Executors.newFixedThreadPool(1, new DatabaseThreadFactory());  
 		DBSetupTask setup = new DBSetupTask();
		databaseSetupFuture = databaseExecutor.submit(setup);
	}
 
	public void stop() throws Exception 
	{
		databaseExecutor.shutdown();
		if (!databaseExecutor.awaitTermination(3, TimeUnit.SECONDS)) 
		{
			System.out.println("Database execution thread timed out after 3 seconds rather than shutting down cleanly.");
		}
	}
  
	
	
//	fetchNamesTask.setOnSucceeded(t ->
//    listView.setItems(fetchNamesTask.getValue())
//);
//
//databaseExecutor.submit(fetchNamesTask);
  
	
 
	
	
	  
}