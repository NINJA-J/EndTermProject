package Jonathan;

import java.util.Calendar;

public class Standard extends AuditedFile{
	public Standard( int fId, User writer, String title, String content, Calendar upload, Calendar deadline, int agree, int disagree ){
		super( fId, writer, title, content, upload, deadline, agree, disagree );
	}
	public String toString( String preBlock ){
		return 	preBlock + "Standard :\n" + super.toString( preBlock );
	}
	
	public String toString(){
		return toString("");
	}
}
