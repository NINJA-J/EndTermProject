package Jonathan;

import java.sql.Date;

public class Comment {
	int fileId;
	User writer;
	Date upload;
	String content;
	
	public Comment( int fileId, User writer, Date upload, String content ) {
		this.fileId = fileId;
		this.writer = writer;
		this.upload = upload;
		this.content = content;
	}
	
	public int getFileId(){ return fileId; }
	
	public User getWriter(){ return writer; }
	
	public Date getUploadDate(){ return upload; }
	
	public String getContent(){ return content; }
}
