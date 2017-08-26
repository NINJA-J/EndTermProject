package Jonathan;

import java.util.Calendar;

public class Proposal extends FileBasic{
	public Proposal( User writer, String title, String content, Calendar upload, Calendar deadline, int agree, int disagree ){
		super( writer, title, content, upload, deadline, agree, disagree );
	}
	public String toString( String preBlock ){
		return 	preBlock + "Proposal :\n" + super.toString( preBlock );
	}
	
	public String toString(){
		return toString("");
	}
}
