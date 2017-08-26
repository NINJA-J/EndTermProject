package Jonathan;

import java.util.Calendar;

public class AuditedFile extends FileBasic {
	User writer;
	int agree;
	int disagree;
	
	public AuditedFile( User writer, String title, String content, Calendar upload, Calendar deadline, int agree, int disagree ){
		super( title, content, upload, deadline );
		this.writer = writer;
		this.agree = agree;
		this.disagree = disagree;
	}
	
	public User getWriter(){ return writer; }
	
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
