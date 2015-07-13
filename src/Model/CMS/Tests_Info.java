package Model.CMS;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/*
 * This is a model file for storing data about the various types of tests, the doctor can store of the patient.
 * Author: Devanshu Jain
 */

public class Tests_Info 
{
	private StringProperty test_id;
	private StringProperty test_name;
	
	/*
	 *  Constructors
	 */
	
	public Tests_Info()
	{
		
	}
	
	public Tests_Info(String test_name)
	{
		this.test_name = new SimpleStringProperty(test_name);
	}
	
	/*
	 * Getters and setters
	 */
	
	public void setID(String id)
	{
		this.test_id = new SimpleStringProperty(id);
	}
	
	public void setName(String name)
	{
		this.test_name = new SimpleStringProperty(name);
	}
	
	public StringProperty get_test_id()
	{
		return this.test_id;
	}
	
	public StringProperty get_test_name()
	{
		return this.test_name;
	}
	
	public static Tests_Info clone(Tests_Info test_info)
	{
		Tests_Info test = new Tests_Info();
		if(test_info.get_test_id() != null)
		{
			test.setID(test_info.get_test_id().getValue());
		}
		if(test_info.get_test_name() != null)
		{
			test.setName(test_info.get_test_name().getValue());
		}
		return test;
	}
}
