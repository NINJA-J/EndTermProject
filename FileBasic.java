package Jonathan;

import java.util.Calendar;

public class FileBasic {
	int id;
	String title;
	String content;
	Calendar upload;
	Calendar deadline;
	
	public FileBasic( int fId, String title, String content, Calendar upload, Calendar deadline ){
		this.id = fId;
		this.title = title;
		this.content = content;
		this.upload = upload;
		this.deadline = deadline;
	}
	
	public int getId(){ return id; }
	
	public String getTitle(){ return title; }
	
	public String getContent(){ return content; }
	
	public Calendar getUploadDate(){ return upload; }
	
	public Calendar getDeadline(){ return deadline; }
	
}
