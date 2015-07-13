package Model.CMS;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/*
 * This is a model file for storing data about the various types of fees, the doctor can charge to the patient.
 * Author: Devanshu Jain
 */

public class Fees_Info 
{
	private StringProperty fees_id;
	private StringProperty fee_name;
	
	/*
	 * Constructors
	 */
	
	public Fees_Info()
	{
		
	}
	
	public Fees_Info(String fee_name)
	{
		this.fee_name = new SimpleStringProperty(fee_name);
	}
	
	/*
	 * Getters and setters
	 */
	
	public void setID(String id)
	{
		this.fees_id = new SimpleStringProperty(id);
	}
	
	public void setName(String name)
	{
		this.fee_name = new SimpleStringProperty(name);
	}
	
	public StringProperty get_fees_id()
	{
		return this.fees_id;
	}
	
	public StringProperty get_fees_name()
	{
		return this.fee_name;
	}
	
	public static Fees_Info clone(Fees_Info fee_info)
	{
		Fees_Info fee = new Fees_Info();
		if(fee_info.get_fees_name() != null)
		{
			fee.setName(fee_info.get_fees_name().getValue());
		}
		if(fee_info.get_fees_id() != null)
		{
			fee.setID(fee_info.get_fees_id().getValue());
		}
		return fee;
		
	}
}
