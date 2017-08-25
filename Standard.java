package Jonathan;

import java.sql.Date;

class Standard extends FileBasic{
	public Standard( User writer, String title, String content, Date upload, Date deadline, int agree, int disagree ){
		super( writer, title, content, upload, deadline, agree, disagree );
	}
	public String toString( String preBlock ){
		return 	preBlock + "Standard :\n" + super.toString( preBlock );
	}
	
	public String toString(){
		return toString("");
	}
}
