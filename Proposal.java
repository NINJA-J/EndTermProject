package Jonathan;

import java.sql.Date;
import Jonathan.User;

public class Proposal {
	User writer;
	String title;
	String content;
	Date upload;
	Date deadline;
	int agree;
	int disagree;
	boolean isPro;
	public Proposal( User writer, String title, String content, Date upload, Date deadline, boolean isPro ){
		this.writer = writer;
		this.title = title;
		this.content = content;
		this.upload = upload;
		this.deadline = deadline;
		this.isPro = isPro;
	}
	public Proposal(){
		
	}
	
	public User getWriter(){ return writer; }
	
	public String getTitle(){ return title; }
	
	public String getContent(){ return content; }
	
	public Date getUploadDate(){ return upload; }
	
	public Date getDeadline(){ return deadline; }
	
	public boolean isProposal(){ return isPro == true; }
	
	public boolean isStandard(){ return isPro == false; }
	
	public String toString( String preBlock ){
		return  preBlock + ( isPro ? "Proposal" : "Standard" ) + " :\n" +
				preBlock + "    Title    : " + title + "\n" +
				preBlock + "    Writer   : " + writer.toString( "    " ) + "\n" +
				preBlock + "    Upload   : " + upload + "\n" +
				preBlock + "    Deadline : " + deadline + "\n" +
				preBlock + "    Content  :\n" +
				preBlock + "        " + content + "\n";
	}
	public String toString(){
		return this.toString( "" );
	}
}
