package Controller.Database;

import javafx.concurrent.Task;

public abstract class DBTask<T> extends Task<T> 
{
	public T object;
	
	public DBTask() 
	{
		this.object = null;
	}
	
	public DBTask(T obj)
	{
		this.object = obj;
	}
}