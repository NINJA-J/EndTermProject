package Jonathan;

import java.util.Calendar;
import Jonathan.User;

public class FileBasic {
	User writer;
	String title;
	String content;
	Calendar upload;
	Calendar deadline;
	int agree;
	int disagree;
	public FileBasic( User writer, String title, String content, Calendar upload, Calendar deadline, int agree, int disagree ){
		this.writer = writer;
		this.title = title;
		this.content = content;
		this.upload = upload;
		this.deadline = deadline;
		this.agree = agree;
		this.disagree = disagree;
	}
	public FileBasic(){
		
	}
	
	public User getWriter(){ return writer; }
	
	public String getTitle(){ return title; }
	
	public String getContent(){ return content; }
	
	public Calendar getUploadDate(){ return upload; }
	
	public Calendar getDeadline(){ return deadline; }
	
	public int getAgreeAmnt(){ return agree; }
	
	public int getDisagreeAmnt(){ return disagree; }
	
	public String toString( String preBlock ){
		return  preBlock + "    Title    : " + title + "<br>\n" +
				preBlock + "    Writer   : " + writer.getName() + "<br>\n" +
				preBlock + "    Upload   : " + upload.getTime() + "<br>\n" +
				preBlock + "    Deadline : " + deadline.getTime() + "<br>\n" +
				preBlock + "    Agree    : " + agree + "<br>\n" +
				preBlock + "    Disagree : " + disagree + "<br>\n" +
				preBlock + "    Content  :<br>\n" +
				preBlock + "        " + content + "<br>\n";
	}
	public String toString(){
		return this.toString( "" );
	}
}
