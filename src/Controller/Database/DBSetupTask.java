package Controller.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import application.Main;

public class DBSetupTask extends DBTask 
{
	public static Connection con;
	
	@Override 
	protected Void call() 
	{
		con = getConnection();
		return null;
	}
	
	private Connection getConnection() 
	{
		try
		{
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			return DriverManager.getConnection("jdbc:sqlserver://" + Main.getIP() + "\\SQLEXPRESS" + ":" + Main.getPort() + ";databaseName=" + Main.getDBName(), Main.getUserName(), Main.getPassword());
		}
		catch(ClassNotFoundException E)
		{
			System.out.println("Driver not found...");
		}
		catch(SQLException E)
		{
			System.out.println("Connection can't be established...");
		}
		return null;
	}  
}
