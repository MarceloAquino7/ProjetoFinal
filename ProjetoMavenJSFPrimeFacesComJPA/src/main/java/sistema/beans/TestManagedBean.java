package sistema.beans;




import sistema.modelos.Contents;
import sistema.modelos.Choice;
import sistema.modelos.Questions;
import sistema.modelos.Test;
import sistema.modelos.TrueOrFalse;
import sistema.service.ContentService;
import sistema.service.QuestionService;
import sistema.service.TestService;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.ReorderEvent;
import org.primefaces.event.RowEditEvent;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;


@ManagedBean(name="testManagedBean")
@ViewScoped

public class TestManagedBean 
{
	
	private Test test = new Test();
	private List<Test> lstTests;
	private TestService tService = new TestService();
	private Questions question;
	private List<Questions> lstQuestions;
	private ContentService cService = new ContentService();
	private QuestionService qService = new QuestionService();
	private Contents content;
	private List<Contents> lstContents;
	private List<Contents> lstContentsSelected;	
	
	public void save(){
		List<Questions> lstSelecteds = qService.getAllQuestions();
		int cont = 0;
		
		for(int i = 0; i < lstSelecteds.size(); i++){		
			if(lstSelecteds.get(i) .getLevel() <= lstSelecteds.get(i).getLevel() || cont < test.getCountQuestions()){
				test.addQuestion(lstSelecteds.get(i));
				cont++;
			}			
		}
		if(test.getLstQuestions().size() < test.getCountQuestions() && lstSelecteds.size() >= test.getCountQuestions())
		{
			int falta = 0;
			
			falta = test.getCountQuestions() - test.getLstQuestions().size();
			
			for(int i = 0; i < falta; i++){
				if(lstSelecteds.get(i).getLevel() > test.getLevelTest()){
					test.addQuestion(lstSelecteds.get(i));
				}
			}
		}
		
		for(int i = 0; i < lstContentsSelected.size(); i++)			
 			test.addContent(lstContentsSelected.get(i));
  		
		test = tService.save(test);
		if (lstTests != null)
			lstTests.add(test);
		test = new Test();
	}
	@SuppressWarnings("deprecation")
	public void generateTest() {		
        Document document = new Document();
        String stringContents = "";
       	        
        try {           
        	 PdfWriter.getInstance(document, 
        		        new FileOutputStream("C:\\Users\\M\\Desktop\\"+ test.getIdTest()+"-"+test.getNameTest()+".pdf"));
        	 document.open();
           
            for(int i = 0; i < test.getLstContents().size(); i++){
         	   if(test.getLstContents().size() == 1){
         		  stringContents += test.getLstContents().get(i).getName();
        	   }
         	   else{
         		  stringContents += test.getLstContents().get(i).getName() + ", ";
         	   }
            }
            document.add(new Paragraph("Colleague: " + test.getNameColleague()));
            document.add(new Paragraph("Class: " + test.getNameClass()));
            document.add(new Paragraph("Course: " + test.getCourse()));            
            document.add(new Paragraph("Test Date: " + test.getTestDate().getDay()
         		   +"/" + test.getTestDate().getMonth()+"/"+test.getTestDate().getYear()));
            document.add(new Paragraph("Contents: " + stringContents));
                       
            for(int i = 0; i < test.getLstQuestions().size(); i++){
         	   		document.add(new Paragraph(""+(i + 1)+") " + test.getLstQuestions().get(i).getHeader()));
         	   		document.add(new Paragraph(""+ "Minimum Estimated Time: "+  test.getLstQuestions().get(i).getAnswerTime() +", Test Level: " + 
         	   				test.getLstQuestions().get(i).getLevel()));
         	   		document.add( Chunk.NEWLINE );
         	   		
         	   if(test.getLstQuestions().get(i) instanceof Choice){
         	   	  Choice chc = (Choice)test.getLstQuestions().get(i);
     
         		int cont = 1;
         	   	for(int j = 0; j < 5; j++){
         			   document.add(new Paragraph(""+ cont + ") "+ chc.getLstChoices().get(j)));
         			   cont++;
         			   document.add( Chunk.NEWLINE );
         		   }
         		  
         	   }
         	   else if(test.getLstQuestions().get(i) instanceof TrueOrFalse)
         	   {
         		  TrueOrFalse tof = (TrueOrFalse)test.getLstQuestions().get(i);
           			int cont = 1;
           			for(int j = 0; j < 5; j++)
           				{
           					document.add(new Paragraph(""+ cont + ") "+"(  )" + tof.getLstOptions().get(j)));
           					cont++;
           					document.add( Chunk.NEWLINE );
           				}
         	   }
         	   
         	   		
         		   document.add(new Paragraph("Answer:"));
         		   document.add(Chunk.NEWLINE);
        		   document.add(Chunk.NEWLINE);
         		   document.add(Chunk.NEWLINE);
            }
            
        }
        catch(DocumentException de) {
            System.err.println(de.getMessage());
            }
        catch(IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        document.close();
    }   
	
	
	
	public List<Test> getTests() {
		if (lstTests == null)
			lstTests = tService.getAllTests();
		return lstTests;
	}
	
	
	
	public List<Questions> getQuestions(){
		if(lstQuestions == null)
			return qService.getAllQuestions();
		return lstQuestions;
	}
	
	public List<Contents> getContents() {
		if(lstContents == null)
			lstContents = cService.getContents();
		return lstContents;
	}	
	
	public Test getTest() {
		return test;
	}
	public void setTest(Test test) {
		this.test = test;
	}
	public List<Test> getLstTests() {
		return lstTests;
	}
	public void setLstTests(List<Test> lstTests) {
		this.lstTests = lstTests;
	}
	public Questions getQuestion() {
		return question;
	}
	public void setQuestion(Questions question) {
		this.question = question;
	}
	public List<Questions> getLstQuestions() {
		return lstQuestions;
	}
	public void setLstQuestions(List<Questions> lstQuestions) {
		this.lstQuestions = lstQuestions;
	}
	public Contents getContent() {
		return content;
	}
	public void setContent(Contents content) {
		this.content = content;
	}
	public List<Contents> getLstContents() {
		return lstContents;
	}
	public void setLstContents(List<Contents> lstContents) {
		this.lstContents = lstContents;
	}
	public List<Contents> getLstContentsSelected() {
		return lstContentsSelected;
	}
	public void setLstContentsSelected(List<Contents> lstContentsSelected) {
		this.lstContentsSelected = lstContentsSelected;
	}
	public void remove(Test test) {
		tService.remove(test);
		lstTests.remove(test);
	}
	public void onRowEdit(RowEditEvent event) {
		Test t = ((Test) event.getObject());
		tService.changeTest(t);
	}
	
	public void onRowReorder(ReorderEvent event) {
        FacesMessage msg = new FacesMessage();
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
	
	
}