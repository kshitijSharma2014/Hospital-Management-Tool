/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Account;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Shahbazz
 */
public class Table {
    private SimpleStringProperty rID;
    private SimpleStringProperty rName;
    private BooleanProperty Checkbox;
    
    public Table(){
        
    }
    
    public Table(String sID,String sName){
    this.rID = new SimpleStringProperty(sID);
    this.rName = new SimpleStringProperty(sName);
    this.Checkbox = new SimpleBooleanProperty(false);
    }
    

    public String getRID() {
        return rID.get();
    }
    
    public void setRID(String v) {
        rID.set(v);
    }
    
    public String getRName() {
        return rName.get();
    }
    
    public void setRName(String x) {
        rName.set(x);
    }
    public boolean isSelected() {
      return Checkbox.get();
    }
    public void setVegetarian(boolean Checkbox) {
      this.Checkbox.set(Checkbox);
    }
     public BooleanProperty CheckboxProperty() {
      return Checkbox;
    }
}
