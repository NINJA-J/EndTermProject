package Jonathan;

import java.util.Calendar;

public class SysStandard extends FileBasic {

	public SysStandard( int fId, String title, String content, Calendar upload, Calendar deadline) {
		super( fId, title, content, upload, deadline);
	}
	
	public String toString( String preBlock ){
		return  preBlock + "System Standard : " + 
				preBlock + "    Title    : " + title + "<br>\n" +
				preBlock + "    Upload   : " + upload.getTime() + "<br>\n" +
				preBlock + "    Deadline : " + deadline.getTime() + "<br>\n" +
				preBlock + "    Content  :<br>\n" +
				preBlock + "        " + content + "<br>\n";
	}
	
	public String toString(){
		return this.toString( "" );
	}

}
