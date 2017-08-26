package Jonathan;

import java.util.Calendar;

public class FileBasic {
	String title;
	String content;
	Calendar upload;
	Calendar deadline;
	
	public FileBasic( String title, String content, Calendar upload, Calendar deadline ){
		this.title = title;
		this.content = content;
		this.upload = upload;
		this.deadline = deadline;
	}
	
	public String getTitle(){ return title; }
	
	public String getContent(){ return content; }
	
	public Calendar getUploadDate(){ return upload; }
	
	public Calendar getDeadline(){ return deadline; }
	
}
