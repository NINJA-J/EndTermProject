package Jonathan;

import java.sql.Date;
import Jonathan.User;

public class FileBasic {
	User writer;
	String title;
	String content;
	Date upload;
	Date deadline;
	int agree;
	int disagree;
	public FileBasic( User writer, String title, String content, Date upload, Date deadline, int agree, int disagree ){
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
	
	public Date getUploadDate(){ return upload; }
	
	public Date getDeadline(){ return deadline; }
	
	public int getAgreeAmnt(){ return agree; }
	
	public int getDisagreeAmnt(){ return disagree; }
	
	public String toString( String preBlock ){
		return  preBlock + "    Title    : " + title + "\n" +
				preBlock + "    Writer   : " + writer.toString( "    " ) + "\n" +
				preBlock + "    Upload   : " + upload + "\n" +
				preBlock + "    Deadline : " + deadline + "\n" +
				preBlock + "    Agree    : " + agree +
				preBlock + "    Disagree : " + disagree +
				preBlock + "    Content  :\n" +
				preBlock + "        " + content + "\n";
	}
	public String toString(){
		return this.toString( "" );
	}
}
