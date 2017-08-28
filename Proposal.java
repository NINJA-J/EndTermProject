package Jonathan;

import java.util.Calendar;

public class Proposal extends AuditedFile{
	char status;
	public Proposal( int fId, User writer, String title, String content, Calendar upload, Calendar deadline, int agree, int disagree, char status ){
		super( fId, writer, title, content, upload, deadline, agree, disagree );
		this.status = status;
	}
	
	public Proposal( Proposal p, char status ){
		super( p.getId(),
				p.getWriter(),
				p.getTitle(),
				p.getContent(),
				p.getUploadDate(),
				p.getDeadline(),
				p.getAgreeAmnt(),
				p.getDisagreeAmnt() );
		this.status = status;
	};
	
	public char getStatus(){ return status; }
	
	public String toString( String preBlock ){
		return 	preBlock + "Proposal :\n" + super.toString( preBlock );
	}
	
	public String toString(){
		return toString("");
	}
}
