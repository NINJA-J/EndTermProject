package Jonathan;

import java.sql.Date;

public class Proposal {
	String name;
	String title;
	String content;
	Date upload;
	Date deadline;
	boolean isPro;
	public Proposal( String uName, String title, String content, Date upload, Date deadline, boolean isPro ){
		name = uName;
		this.title = title;
		this.content = content;
		this.upload = upload;
		this.deadline = deadline;
		this.isPro = isPro;
	}
	public Proposal(){
		
	}
	public String toString(){
		return ( isPro ? "Proposal" : "Standard" ) + " :\n" +
				"    Title    : " + title + "\n" +
				"    Writer   : " + name + "\n" +
				"    Upload   : " + upload + "\n" +
				"    Deadline : " + deadline + "\n" +
				"    Content  :\n" +
				"        " + content + "\n";
	}
}
