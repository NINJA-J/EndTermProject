package Jonathan;

import java.util.Calendar;

public class AuditedFile extends FileBasic {
	
	public static final int STATUS_SAVED		= 0;
	public static final int STATUS_COMMITTED	= 1;
	public static final int STATUS_RECOMMENDED 	= 2;
	public static final int STATUS_RECORDED		= 3;
	public static final int STATUS_DECIDED		= 4;
	
	User writer;
	int agree;
	int disagree;
	char status;
	
	public AuditedFile( int fId, User writer, String title, String content, Calendar upload, Calendar deadline, int agree, int disagree, char status ){
		super( fId, title, content, upload, deadline );
		this.writer = writer;
		this.agree = agree;
		this.disagree = disagree;
		this.status = status;
	}
	
	public User getWriter(){ return writer; }
	
	public int getAgreeAmnt(){ return agree; }
	
	public int getDisagreeAmnt(){ return disagree; }
	
	public char getStatus(){ return status; }
	
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
