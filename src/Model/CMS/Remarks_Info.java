package Model.CMS;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/*
 * This is a model file for storing data about the various types of remarks, the doctor can store to suggest to the patients. It is so that, he does not have to type them everytime. It will be a dropdown.
 * Author: Devanshu Jain
 */

public class Remarks_Info 
{
	private StringProperty remark_id;
	private StringProperty remark_english; // In english language
	private StringProperty remark_gujarati; // In Gujarati (transliterated) language
	private StringProperty remark_context; // Context of the remark
	
	/*
	 * Constructors
	 */
	
	public Remarks_Info()
	{
		this.remark_context = new SimpleStringProperty("");
		this.remark_english = new SimpleStringProperty("");
		this.remark_gujarati = new SimpleStringProperty("");
		this.remark_id = new SimpleStringProperty("");
	}
	
	public Remarks_Info(String english, String gujarati, String context)
	{
		this.remark_english = new SimpleStringProperty(english);
		this.remark_gujarati = new SimpleStringProperty(gujarati);
		this.remark_context = new SimpleStringProperty(context);
	}
	
	/*
	 * Getters and setters
	 */
	
	
	public void setRemarkID(String id)
	{
		this.remark_id = new SimpleStringProperty(id);
	}
	
	public void setEnglishText(String name)
	{
		this.remark_english = new SimpleStringProperty(name);
	}
	
	public void setGujaratiText(String name)
	{
		this.remark_gujarati = new SimpleStringProperty(name);
	}
	
	public void setContext(String name)
	{
		this.remark_context = new SimpleStringProperty(name);
	}
	
	public StringProperty getRemarkID()
	{
		return this.remark_id;
	}
	
	public StringProperty get_english_text()
	{
		return this.remark_english;
	}
	
	public StringProperty get_gujarati_text()
	{
		return this.remark_gujarati;
	}
	
	public StringProperty get_context()
	{
		return this.remark_context;
	}
	
	public static Remarks_Info clone(Remarks_Info remarks_info)
	{
		Remarks_Info rem_info = new Remarks_Info();
		if(remarks_info.get_context() != null)
		{
			rem_info.setContext(remarks_info.get_context().getValue());
		}
		if(remarks_info.get_english_text() != null)
		{
			rem_info.setContext(remarks_info.get_english_text().getValue());
		}
		if(remarks_info.get_gujarati_text() != null)
		{
			rem_info.setContext(remarks_info.get_gujarati_text().getValue());
		}
		if(remarks_info.getRemarkID() != null)
		{
			rem_info.setRemarkID(remarks_info.getRemarkID().getValue());
		}
		return rem_info;
	}
	
	@Override
	public String toString()
	{
		return this.remark_english.getValue();
	}
}
