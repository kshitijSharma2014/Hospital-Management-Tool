package application;

import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;


//import java.awt.Font;
import java.awt.print.PrinterJob;

import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.Image;

import java.awt.print.PrinterException;

import javax.print.PrintService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.controlsfx.dialog.Dialogs;

@SuppressWarnings("deprecation")
public class Reportpdf  {
 
    /* 
     * Path to the resulting PDF file. 
     */
    
	public static String Report = "report.pdf";
    public static String Receipt = "receipt.pdf";
    public static String Medication = "medication.pdf";
    public static String Prescription = "prescription.pdf";
    
    public static void setDirectory(String dir)
    {
    	Receipt = dir + Receipt;
    	Report = dir + Report;
    	Medication = dir + Medication;
    	Prescription = dir + Prescription;
    }

    /*
     * Method to print test reports
     */
    public static void createReport(String filename,String patientid) throws DocumentException, IOException, PrinterException, SQLException, ClassNotFoundException
    {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(filename));
        document.open();
        BaseFont bf = BaseFont.createFont("/Resources/Gujrati-Saral-1.ttf",BaseFont.CP1250,BaseFont.NOT_EMBEDDED);
        Font font = new Font(bf,12);
        Paragraph title = new Paragraph("REPORT",font);
        Paragraph header = new Paragraph("SHARDA HOSPITAL",font);
        header.setAlignment(Element.ALIGN_CENTER);
        title.setAlignment(Element.ALIGN_CENTER);
        
        addEmptyLine(title, 4);
        document.add(header);
        addEmptyLine(header, 4);
        document.add(title);
        addEmptyLine(title,4);
        Paragraph patient_id = new Paragraph(patientid,font); 
        patient_id.setAlignment(Element.ALIGN_LEFT);
        
        
        try
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
	    		.title(" ALERT ")
	    		.masthead(" Database is not setup ")
	    		.message("Please set up the connection ")
	    		.showWarning();
			}
			String query = "SELECT ti.test_name,pt.date,pt.test_value "
	                + "FROM Patient_Tests pt,Test_Info ti "
	                + "WHERE pt.pat_ID = '"+patientid+"' AND pt.test_ID = ti.test_ID;";

			PreparedStatement stmt = con.prepareStatement(query);
	        System.out.println(query);
	        ResultSet rs = stmt.executeQuery();
	                 
	        PdfPTable table = new PdfPTable(4);
	        PdfPCell c1 = new PdfPCell(new Phrase("S.No",font));
	        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(c1);

	        c1 = new PdfPCell(new Phrase("Test",font));
	        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(c1);

	        c1 = new PdfPCell(new Phrase("Date",font));
	        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(c1);
	        c1 = new PdfPCell(new Phrase("Result",font));
	        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(c1);

	        int i = 1;
	        while(rs.next()){
	            String k = Integer.toString(i);
	            table.addCell(k);
	            table.addCell(rs.getString("testname"));
	            table.addCell(rs.getString("test_value"));
	            table.addCell(rs.getString("date"));
	            i++;
	        }
	        
	        document.add(table);
	        Paragraph footer1 = new Paragraph("Director",font);
	        footer1.setAlignment(Element.ALIGN_RIGHT);
	        footer1.setIndentationLeft(100);

	        Paragraph footer2 = new Paragraph("SHARDA HOSPITAL",font);
	        footer2.setAlignment(Element.ALIGN_RIGHT);
	        
	        document.add(footer1);
	        document.add(footer2);
	        document.close();
			
		}
		catch(Exception E)
		{
			Dialogs.create()
    		.title(" ALERT ")
    		.masthead(" SQlException encountered ")
    		.message("Item could not be added... ")
    		.showWarning();
		}
    }
    
    /*
     * create pdf for receipts
     */
    public void createReceipt(String filename,String receiptid)throws DocumentException, IOException, SQLException, ClassNotFoundException
    {
        Document document = new Document();

        String patientname = null;
        String date = null;
        String total_sum = null;
        PdfWriter.getInstance(document, new FileOutputStream(filename));
        document.open();
        Font font = new Font(FontFamily.TIMES_ROMAN,20,Font.BOLD);
        
        document.setMargins(36,36,54,72);
        Paragraph title = new Paragraph("RECEIPT",font);
        Paragraph header = new Paragraph("SHARDA HOSPITAL");
        header.setAlignment(Element.ALIGN_CENTER);
        title.setAlignment(Element.ALIGN_CENTER); 
        
        
        try
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
	    		.title(" ALERT ")
	    		.masthead(" Database is not setup ")
	    		.message("Please set up the connection ")
	    		.showWarning();
			}
			PreparedStatement stmt = null;
			String query = "SELECT ft.fee_name,rfd.amount "
	                + "FROM Receipt_fee_distribution rfd, fee_Type ft "
	                + "WHERE rfd.Receipt_ID = '"+receiptid+"' AND rfd.fee_ID=ft.fee_ID;";
	        System.out.println(query);
	        stmt = con.prepareStatement(query);
	        ResultSet rs = stmt.executeQuery();

	        Paragraph receipt_id = new Paragraph("R.No"+receiptid,font);
	        receipt_id.setAlignment(Element.ALIGN_LEFT);
	        Paragraph Date = new Paragraph("Date :"+date,font);
	        Date.setAlignment(Element.ALIGN_RIGHT);
	        document.add(header);
	        document.add(title);
	        document.add(receipt_id);
	        document.add(Date);
	        addEmptyLine(Date,4);
	        String DOJ = null;
	        String diagnosis = null;
	        String no_of_days = null;
	        String DOD = null;
	        Paragraph patient_details = new Paragraph("Received "+
	                total_sum+"from Mr./Mrs. "+ patientname +
	                " towards indoor/outdoor charges detailed as below."
	                + " He/She was admitted in hospital/under treatment from"
	                + DOJ+ "to "+DOD+". He/She is suffering from "+diagnosis+"."+
	                " He/She has to take future medicie for"+no_of_days+".",font);
	        document.add(patient_details);
	        PdfPTable table = new PdfPTable(3);
	        PdfPCell c1 = new PdfPCell(new Phrase("S.No",font));
	        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(c1);
	        c1 = new PdfPCell(new Phrase("Fee Type",font));
	        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(c1);
	        c1 = new PdfPCell(new Phrase("Amount",font));
	        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(c1);
	        int i =1;
	        while(rs.next()){
	            table.addCell(Integer.toString(i));
	            table.addCell(rs.getString("fee_name"));
	            table.addCell(Integer.toString(rs.getInt("amount")));
	            i++;
	        }
	        rs.close();
	        document.add(table);
	        document.close();
			
		}
		catch(Exception E)
		{
			Dialogs.create()
    		.title(" ALERT ")
    		.masthead(" SQlException encountered ")
    		.message("Item could not be added... ")
    		.showWarning();
		}
        
    }
    
    /*
     * pdf for medication chart
     */
    
    public static void createMedication(String filename,String patientid)throws DocumentException, IOException, SQLException, ClassNotFoundException{
        
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(filename));
        document.open();
        com.itextpdf.text.Font font = new Font(FontFamily.TIMES_ROMAN,
        10, Font.BOLD);

        Paragraph title = new Paragraph("MEDICATION CHART",font);
        Paragraph header = new Paragraph("SHARDA HOSPITAL");
        header.setAlignment(Element.ALIGN_CENTER);
        title.setAlignment(Element.ALIGN_CENTER); 
        
        addEmptyLine(title, 4);
        document.add(header);
        addEmptyLine(header, 4);
        document.add(title);
        addEmptyLine(title,4);
        
        
        try
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
	    		.title(" ALERT ")
	    		.masthead(" Database is not setup ")
	    		.message("Please set up the connection ")
	    		.showWarning();
			}
			PreparedStatement stmt = null;
			String query = "SELECT md.date,md.time,m.medicine_name,md.amount "
	                + "FROM Medication md,Medicine m "
	                + "WHERE md.pat_ID = '"+patientid+"' AND md.medicine_id = m.medicine_id";
	        stmt = con.prepareStatement(query);
			System.out.println(query);
	        ResultSet rs = stmt.executeQuery();
	        PdfPTable table = new PdfPTable(5);
	        PdfPCell c1 = new PdfPCell(new Phrase("S.No",font)); 
	        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(c1);

	        c1 = new PdfPCell(new Phrase("Date",font));
	        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(c1);

	        c1 = new PdfPCell(new Phrase("Time",font));
	        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(c1);
	        
	        c1 = new PdfPCell(new Phrase("Medicine Name",font));
	        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(c1);
	        c1 = new PdfPCell(new Phrase("Dosage",font));
	        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(c1);
	       
	        int i =1;
	        while(rs.next()){
	            table.addCell(Integer.toString(i));
	            table.addCell(rs.getString("date"));
	            table.addCell(rs.getString("time"));
	            table.addCell(rs.getString("medicine_name"));
	            table.addCell(Integer.toString(rs.getInt("amount")));
	            
	            i++;
	            
	        }
	        document.add(table);
	        Paragraph footer1 = new Paragraph("Director",font);
	        footer1.setAlignment(Element.ALIGN_RIGHT);
	        footer1.setIndentationLeft(50);
	        footer1.setSpacingBefore(25);
	        Paragraph footer2 = new Paragraph("SHARDA HOSPITAL",font);
	        footer2.setAlignment(Element.ALIGN_RIGHT);
	        
	        document.add(footer1);
	        document.add(footer2);
	        document.close();
			
		}
		catch(Exception E)
		{
			Dialogs.create()
    		.title(" ALERT ")
    		.masthead(" SQlException encountered ")
    		.message("Item could not be added... ")
    		.showWarning();
		}
        
    }
    
    /*
     * create prescription
     */
    
    public static void createPrescription(String filename,String patientid) throws DocumentException, IOException, SQLException, ClassNotFoundException{
        
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(filename));
        document.open();
        com.itextpdf.text.Font font = new Font(FontFamily.TIMES_ROMAN,
        10,Font.BOLD);
        Image img = Image.getInstance("C:\\Users\\Chaitanya Allu\\Desktop\\title.png");
        img.scalePercent(80f);
        document.add(img);
        Paragraph title = new Paragraph("PRESCRIPTION",font);
        Paragraph header = new Paragraph("SHARDA HOSPITAL",font);
        header.setAlignment(Element.ALIGN_CENTER);
        title.setAlignment(Element.ALIGN_CENTER); 
        
        addEmptyLine(title, 4);

        document.add(title);
        Paragraph addr = new Paragraph("DR.JAYESH D.SHAH",font);
        addr.setAlignment(Element.ALIGN_RIGHT);
        document.add(addr); 
        
        String prescription_no = null;
        Paragraph prescriptionno = new Paragraph("No"+ prescription_no,font);
        prescriptionno.setAlignment(Element.ALIGN_LEFT);
        document.add(prescriptionno);
        String t_date = null;
        Paragraph date =  new Paragraph("Date: "+t_date);
        date.setAlignment(Element.ALIGN_RIGHT);
        addEmptyLine(date,4);
        document.add(date);
        addEmptyLine(date,4);
       
        
        try
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
	    		.title(" ALERT ")
	    		.masthead(" Database is not setup ")
	    		.message("Please set up the connection ")
	    		.showWarning();
			}
			PreparedStatement stmt = null;
			String query = "SELECT M.medicine_name,mp.morning_amt,mp.noon_amt,mp.evening_amt "
	                + "FROM Medicine M ,Medicine_prescribed mp "
	                + "WHERE mp.pat_ID = '"+patientid+"' AND mp.medicine_ID = M.medicine_ID;";
			stmt = con.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();
	        PdfPTable table = new PdfPTable(5);
	        PdfPCell c1 = new PdfPCell(new Phrase("S.No",font));
	        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(c1);
	        c1 = new PdfPCell(new Phrase("Medicine name ",font));
	        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(c1);
	        c1 = new PdfPCell(new Phrase("M",font));
	        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(c1);
	        c1 = new PdfPCell(new Phrase("N",font));
	        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(c1);
	        c1 = new PdfPCell(new Phrase("E",font));
	        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(c1);
	        
	        int i =1;
	        while(rs.next()){
	            
	            String k = Integer.toString(i);
	            table.addCell(k);
	            table.addCell(rs.getString("medicine_name"));
	            table.addCell(Integer.toString(rs.getInt("morning_amt")));
	            table.addCell(Integer.toString(rs.getInt("noon_amt")));
	            table.addCell(Integer.toString(rs.getInt("evening_amt")));
	            i++;
	        }
	        
	        document.add(table);
	        document.close();    
			
		}
		catch(Exception E)
		{
			Dialogs.create()
    		.title(" ALERT ")
    		.masthead(" SQlException encountered ")
    		.message("Item could not be added... ")
    		.showWarning();
		}
    }
    
    /*
     * adding empty lines
     */
    private static void addEmptyLine(Paragraph paragraph, int number)
    {
	    for (int i = 0; i < number; i++)
	    {
	    	paragraph.add(new Paragraph(" "));
	    }  
    }
    
    /*
     * Send location....It will print
     */
    public static void printPDF(String fileName) throws IOException, PrinterException 
    {
	    PrinterJob job = PrinterJob.getPrinterJob();
	    PrintService printer = null;
	    if(job.printDialog())
	    {
	      printer =   job.getPrintService();
	    } 
	    else
	    {
	        System.out.println("No printer found");
	    }
	        
	    job.setPrintService(printer);
	    PDDocument doc = PDDocument.load(fileName);
	    doc.silentPrint(job);
    
    }
} 