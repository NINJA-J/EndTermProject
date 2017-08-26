package Jonathan;

import java.util.Calendar;

public class SysStandard extends FileBasic {

	public SysStandard(String title, String content, Calendar upload, Calendar deadline) {
		super(title, content, upload, deadline);
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
