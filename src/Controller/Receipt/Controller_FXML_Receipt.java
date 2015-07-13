/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller.Receipt;

import Model.Patient.Patient_Info;
import application.Main;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.controlsfx.dialog.Dialogs;

/**
 *
 * @author Kshitij
 */
public class Controller_FXML_Receipt implements Initializable {

    public static final String Report
            = "report.pdf";
    private final ObservableList<chargesTable> data = FXCollections.observableArrayList();
    @FXML
    private Label patID = new Label();
    @FXML
    private Label valueID = new Label();
    @FXML
    private Label patName = new Label();
    @FXML
    private Label valueName = new Label();
    @FXML
    private TableView<chargesTable> table = new TableView<chargesTable>();
    @FXML
    private ComboBox CB = new ComboBox();
    @FXML
    private TextField addCharges = new TextField();
    @FXML
    private Button Add = new Button();
    @FXML
    private Button del = new Button();
    @FXML
    private Button save = new Button();
    @FXML
    private Button cancel = new Button();
    @FXML
    private Button print = new Button();
    @FXML
    private Label total = new Label();
    @FXML
    private TextField valueTotal = new TextField();
    @FXML
    private DatePicker date = new DatePicker();
    @FXML
    private TableColumn<chargesTable, String> feeTypeCol = new TableColumn<chargesTable, String>();
    @FXML
    private TableColumn<chargesTable, String> chargesCol = new TableColumn<chargesTable, String>();
	
    private Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
            // TODO
            valueID.setText(FinalReceipt.PID);
            valueName.setText(FinalReceipt.name);
           
            Connection con = Main.getConnection();
    		if(con == null)
    		{
    			Dialogs.create()
        		.title(" ALERT ")
        		.masthead(" Database is not setup ")
        		.message("Please set up the connection ")
        		.showWarning();
    			return ;
    		}
    		else
    		{
    			PreparedStatement stmt = null;
    			try
    			{
    				String query = "SELECT * FROM Fee_type;";
    				stmt = con.prepareStatement(query);
    				ResultSet rs = stmt.executeQuery();
    	            ObservableList<String> options = FXCollections.observableArrayList((String) null);
    				while(rs.next())
    				{
    					options.add(rs.getString("fee_name"));
    	                System.out.println(rs.getString("fee_name"));
    				}
    	            CB.setItems(options);
    			}
    			catch(SQLException E)
    			{
    				Dialogs.create()
    	    		.title(" ALERT ")
    	    		.masthead(" Database is not setup ")
    	    		.message("Items could not be loaded... ")
    	    		.showWarning();
    				return ;
    			}
    		}

    }

    public void saveButtonClick() {
    	
    			String erroMsg = "";
    			boolean retValue = true;
    			if(date.getValue() == null)
    			{
    				retValue = false;
    				erroMsg += "Select a date\n";
    			}
            
                if (data.size() == 0) {
                	retValue = false;
    				erroMsg += "Please add some data in table\n";
                    
                } 
                if(!retValue)
                {
                	Dialogs.create()
                    .owner(stage)
                    .title("Warning Dialog")
                    .masthead("Error")
                    .message(erroMsg)
                    .showWarning();
                }
                else {
                    int size = data.size();
                    int i = 0;
                    for (i = 0; i < size; ++i) {
                        System.out.println("fee type" + data.get(i).feeType);
                        System.out.println("charges" + data.get(i).Charges);
                    }
                    System.out.println("1111111111111111111111111111111111");
                    Connection con = Main.getConnection();
            		if(con == null)
            		{
            			Dialogs.create()
                		.title(" ALERT ")
                		.masthead(" Database is not setup ")
                		.message("Please set up the connection ")
                		.showWarning();
            			return ;
            		}
            		else
            		{
            			PreparedStatement stmt = null;
            			try
            			{
            				DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                            Calendar cal = Calendar.getInstance();
                            String time = dateFormat.format(cal.getTime()).toString();
                            System.out.println(dateFormat.format(cal.getTime()));
                            String query;
                            String receipt_id = valueID.getText() + "_" + date.getValue().toString() + time;
                            query = "INSERT into Receipt Values(" + "'" + valueID.getText() + "'" + "," + "'" + receipt_id + "','" + date.getValue().toString() + "'" + "," + "'" + time + "');";
                            
                            System.out.println(query);
                            stmt = con.prepareStatement(query);
                            System.out.println("222222222222222222222222222222222222");
                            stmt.executeUpdate();
                            for (i = 0; i < size; ++i) {
                                System.out.println("fee type" + data.get(i).feeType);
                                System.out.println("charges" + data.get(i).Charges);
                                query = "Select fee_ID,fee_name from Fee_type where fee_name='" + data.get(i).feeType.getValue() + "'";
                                 
                                System.out.println("1: " + query);
                                stmt = con.prepareStatement(query);
                                ResultSet rs = stmt.executeQuery();
                                System.out.println("333333333333333333333333333333333333333333333333");
                                String fid = "";
                                while(rs.next())
                                {
                                	fid = rs.getString("fee_ID");
                                }
                                int amount;
                                amount = Integer.parseInt(data.get(i).getCharges());
                                query = "Insert into Receipt_fee_distribution values('" + valueID.getText() + "','" + receipt_id + "','" + fid + "','" + amount + "')";
                                System.out.println("2  :" + query);
                                stmt = con.prepareStatement(query);
                                stmt.executeUpdate();
                            }
                            stage.close();
            			}
            			catch(SQLException E)
            			{
            				E.printStackTrace();
            				Dialogs.create()
            	    		.title(" ALERT ")
            	    		.masthead(" Database is not setup ")
            	    		.message("Items could not be loaded... ")
            	    		.showWarning();
            				return ;
            			}
            		}
                }
            }
    

    public void cancelButton() {
        cancel.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                try {
                    Parent chart = FXMLLoader.load(getClass().getResource("home.fxml"));
                    Scene scene_chart = new Scene(chart);
                    Stage stage_chart = (Stage) cancel.getScene().getWindow();
                    stage_chart.setScene(scene_chart);
                    stage_chart.show();
                } catch (IOException ex) {
                    Logger.getLogger(Controller_FXML_Receipt.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        });
    }

    public void addButtonClick() {

       if (CB.getValue() == null || addCharges.getText() == null) {
                    Stage stage = (Stage) Add.getScene().getWindow();
                    Dialogs.create()
                            .owner(stage)
                            .title("Warning Dialog")
                            .masthead("No Data to add")
                            .message("Please add the data in the given fields")
                            .showWarning();
                }

                data.add(new chargesTable(
                        CB.getValue().toString(),
                        addCharges.getText()
                ));
                feeTypeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getfeeType()));
                chargesCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCharges()));
                System.out.println("Printing data: " + data.get(0).getfeeType());
                System.out.println(CB.getValue().toString());
                valueTotal.setText(String.valueOf(chargesTable.total));
                addCharges.clear();
                CB.setPromptText("Fee Type");

            
       
        table.setItems(data);
    }
    
    public void setStage(Stage stage)
    {
    	this.stage = stage;
    }

    public void deletebutton() {
        del.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                // get Selected Item

                if (table.getItems().isEmpty()) {
                    valueTotal.setText(String.valueOf(0));
                    chargesTable.total = (float) 0.0;
                    Stage stage = (Stage) del.getScene().getWindow();
                    Dialogs.create()
                            .owner(stage)
                            .title("Warning Dialog")
                            .masthead("No Data")
                            .message("No data in Table")
                            .showWarning();

                } else {
                    chargesTable currentrow = (chargesTable) table.getSelectionModel().getSelectedItem();
                    if (currentrow == null) {
                        Stage stage = (Stage) del.getScene().getWindow();
                        Dialogs.create()
                                .owner(stage)
                                .title("Warning Dialog")
                                .masthead("No Data Selected")
                                .message("Select a Data row to be Deleted")
                                .showWarning();
                    } else {
                        String trial = valueTotal.getText();
                        float u = Float.parseFloat(trial);
                        valueTotal.setText(String.valueOf(u - Float.valueOf(currentrow.getCharges())));
                        chargesTable.total = u - Float.valueOf(currentrow.getCharges());
                        data.remove(currentrow);
                        if (table.getItems().isEmpty()) {
                            valueTotal.setText(String.valueOf(0));
                            chargesTable.total = (float) 0.0;
                        }
                    }
                }
            }
        });
    }

    public void createReceipt(String filename, String patientid) throws DocumentException, IOException, PrinterException {
        {

            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            Document document = new Document();
            String receipt_id = null;
            String patientname = null;
            String date = null;
            String total_sum = null;
            try {
                try {
                    PdfWriter.getInstance(document, new FileOutputStream(filename));
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(Controller_FXML_Receipt.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (DocumentException ex) {
                Logger.getLogger(Controller_FXML_Receipt.class.getName()).log(Level.SEVERE, null, ex);
            }
            document.open();
            Font font = new Font(FontFamily.TIMES_ROMAN, 10, Font.BOLD);
            //getFont("c:/windows/fonts/Shruti.ttf",
            //BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 10);
            //document.add(new Paragraph("english",font));
            com.itextpdf.text.Image img = null;
//            try {
//                img = com.itextpdf.text.Image.getInstance("C:");
//                img.scalePercent(80f);
//            } catch (BadElementException ex) {
//                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (IOException ex) {
//                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
//            }

            /*try {
             document.add(img);
             } catch (DocumentException ex) {
             Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
             }*/
            Paragraph title = new Paragraph("RECEIPT", font);
            Paragraph header = new Paragraph("SHARDA HOSPITAL");
            header.setAlignment(Element.ALIGN_CENTER);
            title.setAlignment(Element.ALIGN_CENTER);
            Paragraph receiptid = new Paragraph("R.No" + receipt_id, font);
            receiptid.setAlignment(Element.ALIGN_LEFT);
            Paragraph Date = new Paragraph("Date :" + date, font);
            Date.setAlignment(Element.ALIGN_RIGHT);
            try {
                document.add(header);
            } catch (DocumentException ex) {
                Logger.getLogger(Controller_FXML_Receipt.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                document.add(title);
            } catch (DocumentException ex) {
                Logger.getLogger(Controller_FXML_Receipt.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                document.add(receiptid);
            } catch (DocumentException ex) {
                Logger.getLogger(Controller_FXML_Receipt.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                document.add(Date);
            } catch (DocumentException ex) {
                Logger.getLogger(Controller_FXML_Receipt.class.getName()).log(Level.SEVERE, null, ex);
            }
            String DOJ = null;
            String diagnosis = null;
            String no_of_days = null;
            String DOD = null;
            Paragraph patient_details = new Paragraph("Received "
                    + total_sum + "from Mr./Mrs. " + patientname
                    + " towards indoor/outdoor charges detailed as below."
                    + " He/She was admitted in hospital/under treatment from"
                    + DOJ + "to " + DOD + ". He/She is suffering from " + diagnosis + "."
                    + " He/She has to take future medicie for" + no_of_days + ".", font);
            try {
                document.add(patient_details);
            } catch (DocumentException ex) {
                Logger.getLogger(Controller_FXML_Receipt.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Chaitanya");
            Object newValue = null;
            //Check whether item is selected and set value of selected item to Label
            if (table.getSelectionModel().getSelectedItem() != null) {
                TableView.TableViewSelectionModel selectionModel = table.getSelectionModel();
                ObservableList selectedCells = selectionModel.getSelectedCells();
                TablePosition tablePosition = (TablePosition) selectedCells.get(0);
                Object val1 = tablePosition.getTableColumn().getCellData(newValue);
                System.out.println("Fee Type Value" + val1);
                TablePosition tablePosition1 = (TablePosition) selectedCells.get(1);
                Object val2 = tablePosition1.getTableColumn().getCellData(newValue);
                System.out.println("Charges Value" + val2);
            }

            document.close();
            String pdfFile = "report.pdf";
            try {
                printPDF(pdfFile);
            } catch (IOException ex) {
                Logger.getLogger(Controller_FXML_Receipt.class.getName()).log(Level.SEVERE, null, ex);
            } catch (PrinterException ex) {
                Logger.getLogger(Controller_FXML_Receipt.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void printPDF(String fileName) throws IOException, PrinterException {

        //To change body of generated methods, choose Tools | Templates.
        PrinterJob job = PrinterJob.getPrinterJob();
        javax.print.PrintService printer = null;
        if (job.printDialog()) {
            printer = (javax.print.PrintService) job.getPrintService();
        } else {
            System.out.println("No printer found");
        }

        job.setPrintService(printer);
        PDDocument doc = PDDocument.load(fileName);
        doc.silentPrint(job);
    }

    public void printreceipt() {
        print.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                String patientid = null;
                try {
                    createReceipt(Report, patientid);
                } catch (DocumentException ex) {
                    Logger.getLogger(Controller_FXML_Receipt.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Controller_FXML_Receipt.class.getName()).log(Level.SEVERE, null, ex);
                } catch (PrinterException ex) {
                    Logger.getLogger(Controller_FXML_Receipt.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        });
    }

    public static class chargesTable {

        private SimpleStringProperty feeType;
        private SimpleStringProperty Charges;
        private static float total;

        public void chargesTable() {
            chargesTable.total = 0;
            this.Charges = new SimpleStringProperty("");
            this.feeType = new SimpleStringProperty("");
        }

        private chargesTable(String feetype, String charges) {
            this.feeType = new SimpleStringProperty(feetype);
            this.Charges = new SimpleStringProperty(charges);
            chargesTable.total += Float.valueOf(charges);

        }

        public String getfeeType() {
            return feeType.get();
        }

        public void setfeeType(String feetype) {
            feeType.equals(feeType);
        }

        public String getCharges() {
            return Charges.get();
        }

        public void setcharges(String charges) {
            Charges.set(charges);
        }

        public float getTotal() {
            return total;
        }

        public void setTotal(float value) {
            total = value;
        }
    }

}
