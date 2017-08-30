package Jonathan;

import java.util.Calendar;

public class Standard extends AuditedFile{
	
	int proposalId;
	
	public Standard( int fId, User writer, String title, String content, Calendar upload, Calendar deadline, int agree, int disagree, char status, int pId ){
		super( fId, writer, title, content, upload, deadline, agree, disagree, status );
		this.proposalId = pId;
	}
	
	public int getProposalIdLinked(){ return proposalId; }
	
	public String toString( String preBlock ){
		return 	preBlock + "Standard :\n" + super.toString( preBlock );
	}
	
	public String toString(){
		return toString("");
	}
}
