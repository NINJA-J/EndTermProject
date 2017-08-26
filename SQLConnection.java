package Jonathan;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
//import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

import Jonathan.User;

//String url = "jdbc:mysql://localhost:3306";
//String dataBase = "DocManager";
//String usrName = "root";//"614e2a1794894986bbfba809b6a0ee46";
//String usrPswd = "";//5aa5185cdf504b45b07bd33ef261c065";

public class SQLConnection {
	Connection con;
	Statement st;
	java.sql.PreparedStatement pst;
	ResultSet rs;
	
	public static int SUCCESS 		= 0;
	public static int FAILURE 		= 1;
	public static int TITLE_EXIST 	= 2;
	public static int USER_NO_FOUND	= 3;
//	public static int 
	public static int USER_ERROR 	= 10;
	public static int PSWD_ERROR 	= 11;
	
	SQLConnection con2 = null;
	
	//Connection
	//初始化
	public SQLConnection() throws ClassNotFoundException, SQLException{
		super();
		Class.forName("com.mysql.jdbc.Driver");
		con2 = new SQLConnection( false );
	}
	
	public SQLConnection( boolean createCopy ) throws ClassNotFoundException, SQLException{
		super();
		Class.forName("com.mysql.jdbc.Driver");
		if( createCopy )
			con2 = new SQLConnection( false );
	}
	
	//连接数据库，返回值：
	//true  - 成功
	//false - 失败
	public boolean connectToDatabase( String url, String baseName, String user, String pswd ) throws SQLException{
		con = DriverManager.getConnection( 
				"jdbc:mysql://" + url + "/" + baseName,
				user,
				pswd );
		if( con.isClosed() )
			return false;
		st = con.createStatement();
	
		return ( con2 == null ? true : con2.connectToDatabase(url, baseName, user, pswd ) );
	}
	
	//关闭当前连接
	public void closeConn() throws SQLException{
		if( !con.isClosed() ){
			con.close();
			st.close();
			rs.close();
			pst.close();
		}
		
	}
	
	//Login
	//查询用户名密码存在存在，返回值：
	//0 - 存在且正确
	//1 - 密码错误
	//2 - 用户名不存在
	public int chkLoginInfo( String uName, String uPswd ) throws SQLException{
		boolean fPerson = false;
		st = con.createStatement();  //构造语句对象
		rs = st.executeQuery( "select * from LoginInfo where UName=\"" + uName + "\"" );
		while( rs.next() ){
			fPerson = true;
			if( rs.getString("Pswd").equals(uPswd) )
				return 0;
		}
		if( fPerson )
			return 1;
		return 2;
	}
	
	//立即注册用户名密码，返回值：
	//0 - 注册成功
	//1 - 用户名已存在
	public int addLoginInfo( String uName, String uPswd ) throws SQLException{
		int cnt;
		rs = st.executeQuery( "select * from LoginInfo where UName=\"" + uName + "\"" );
		if( rs.next() ) 
			return 1;
		System.out.println("Creating new user");
		rs = st.executeQuery( "select count(*) as totalitem from LoginInfo" );
		rs.next();
		cnt = rs.getInt( 1 );
		
		pst = con.prepareStatement( "insert into LoginInfo ( UserId, UName, Pswd, Email ) values (?,?,?,?)");
		pst.setInt( 1,  cnt + 1 );
		pst.setString( 2, uName );
		pst.setString( 3, uPswd );
		pst.setString( 4, uName );
		pst.executeUpdate();
		return 0;
	}
	
	//创建java.sql.Date，表示当天时间
	public Calendar createCalendarForNow(){
		return Calendar.getInstance();
	}
	
	//创建设立的时间
	public Calendar createCalendar( int year, int month, int day ){
		Calendar c = Calendar.getInstance();
		c.set(year, month, day);
		return c;
	}
	
	//Proposal Operation
	//查询所有uName用户填写的提案
	public ArrayList<Proposal> getProposalByUName( String uName ) throws SQLException{
		ArrayList<Proposal> pList = new ArrayList<Proposal>();
		st = con.createStatement();
		rs = st.executeQuery( "select * from UserInfo where Name=\"" + uName + "\"" );
		
		if( rs.next() ){
			int uId = rs.getInt( "UserId" );
			rs = st.executeQuery( "select * from Proposal where WriterId=" + uId + " isPro=\'T\'" );
			while( rs.next() ){
				Calendar upload = Calendar.getInstance();	upload.setTime( new Date( rs.getDate( "UploadDate" ).getTime() ) );
				Calendar deadline = Calendar.getInstance(); deadline.setTime( new Date( rs.getDate( "Deadline" ).getTime() ) );
				pList.add( new Proposal(
						chkUserById( rs.getInt( "WriterId" ) ),
						rs.getString( "Title" ),
						rs.getString( "Content" ),
						upload,
						deadline,
						rs.getInt( "Agree" ), 
						rs.getInt( "Disagree" ) ) );
			}
		}
		
		return pList;
	}
	
	//查询Id为pId的提案
	public Proposal getProposalById( int pId ) throws SQLException{
		rs = st.executeQuery( "select * from Proposal where FileId=" + pId + " isPro=\'T\'" );
		
		if( rs.next() ){
			Calendar upload = Calendar.getInstance();	upload.setTime( new Date( rs.getDate( "UploadDate" ).getTime() ) );
			Calendar deadline = Calendar.getInstance(); deadline.setTime( new Date( rs.getDate( "Deadline" ).getTime() ) );
			return new Proposal(
					chkUserById( rs.getInt( "WriterId" ) ),
					rs.getString( "Title" ),
					rs.getString( "Content" ),
					upload,
					deadline,
					rs.getInt( "Agree" ),
					rs.getInt( "Disagree" ) );
		}
		
		return null;
	}
	
	//查询标题为title的提案
	public ArrayList<Proposal> getProposalByTitle( String title ) throws SQLException{
		ArrayList<Proposal> pList = new ArrayList<Proposal>();
		rs = st.executeQuery( "select * from Proposal where Title=" + title + " isPro=\'T\'" );
		
		while( rs.next() ){
			Calendar upload = Calendar.getInstance();	upload.setTime( new Date( rs.getDate( "UploadDate" ).getTime() ) );
			Calendar deadline = Calendar.getInstance(); deadline.setTime( new Date( rs.getDate( "Deadline" ).getTime() ) );
			
			pList.add( new Proposal(
					chkUserById( rs.getInt( "WriterId" ) ),
					rs.getString( "Title" ),
					rs.getString( "Content" ),
					upload,
					deadline,
					rs.getInt( "Agree" ),
					rs.getInt( "Disagree" ) ) );
		}
		
		return pList;
	}

	//查询所有提案
	public ArrayList<Proposal> getProposalForAll() throws SQLException{
		ArrayList<Proposal> pList = new ArrayList<Proposal>();
		st = con.createStatement();
		rs = st.executeQuery( "select * from Proposal where isPro=\'T\'" );
		while( rs.next() ){
			Calendar upload = Calendar.getInstance();	upload.setTime( new Date( rs.getDate( "UploadDate" ).getTime() ) );
			Calendar deadline = Calendar.getInstance(); deadline.setTime( new Date( rs.getDate( "Deadline" ).getTime() ) );
			
			pList.add( new Proposal(
					con2.chkUserById( rs.getInt( "WriterId" ) ),
					rs.getString( "Title"),
					rs.getString( "Content"),
					upload,
					deadline,
					rs.getInt( "Agree"),
					rs.getInt( "Disagree") ) );
		}
		return pList;
	}
	
	//新建一份提案
	public int addProposal( String uName, Calendar date, Calendar endline, String title, String content ) throws SQLException{
		rs = st.executeQuery( "select * from Proposal where Title=\"" + title + "\" and isPro=\'T\'" );
		if( rs.next() )
			return SQLConnection.TITLE_EXIST;
		
		rs = st.executeQuery( "select * from UserInfo where Name=\"" + uName + "\"" );
		if( !rs.next() ) 
			return SQLConnection.USER_NO_FOUND;
		int uId = rs.getInt( "UserId" );
		
		rs = st.executeQuery( "select count(*) as totalitem from Proposal" );
		rs.next();
		int cnt = rs.getInt(1);
		pst = con.prepareStatement( "insert into Proposal " + 
				"( FileId, Title, WriterId, UploadDate, Deadline, Content, Status, Agree, Disagree, isPro ) "+
				"value ( ?,?,?,?,?,?,?,?,?,? )" );
		pst.setInt( 1, cnt + 1 );
		pst.setString( 2, title );
		pst.setInt( 3, uId );
		pst.setDate( 4, new java.sql.Date( date.getTime().getTime() ) );
		pst.setDate( 5, new java.sql.Date( endline.getTime().getTime() ) );
		pst.setString( 6, content );
		pst.setString( 7, "W");
		pst.setInt( 8, 0 );
		pst.setInt( 9, 0 );
		pst.setString( 10, "T" );
		pst.executeUpdate();
		return SQLConnection.SUCCESS;
	}
	
	//按照ID为提案增加评论
	public void addCommentForProposal( int proposalId, String comment, int writerId ) throws SQLException{
		pst = con.prepareStatement( "insert into Comments ( FileId, WriterId, TimeStamp, Content ) values ( ?,?,?,? )" );
		pst.setInt( 1, proposalId );
		pst.setInt( 2, writerId );
		pst.setDate( 3, new java.sql.Date( Calendar.getInstance().getTime().getTime() ) );
		pst.setString( 4, comment );
		pst.executeUpdate();
	}
	
	//查询ID为proposalId的提案的所有评论
	public ArrayList<Comment> getCommentForProposal( int proposalId ) throws SQLException{
		ArrayList<Comment> cList = new ArrayList<Comment>();
		rs = st.executeQuery( "select * from Comments where ProposalId=" + proposalId );
		
		while( rs.next() ){
			cList.add( new Comment(
					proposalId,
					con2.chkUserById( rs.getInt("WriterId") ),
					rs.getDate("Upload"),
					rs.getString("Content") ) );
		}
		return cList;
	}
	
	//Standard Operation 功能同上
	public ArrayList<Standard> getStandardByWriter( String uName ) throws SQLException{
		ArrayList<Standard> pList = new ArrayList<Standard>();
		rs = st.executeQuery( "select * from UserInfo where Name=\"" + uName + "\"" );
		
		if( rs.next() ){
			int uId = rs.getInt( "UserId" );
			rs = st.executeQuery( "select * from Proposal where WriterId=" + uId + " isPro=\'F\'" );
			while( rs.next() ){
				Calendar upload = Calendar.getInstance();	upload.setTime( new Date( rs.getDate( "UploadDate" ).getTime() ) );
				Calendar deadline = Calendar.getInstance(); deadline.setTime( new Date( rs.getDate( "Deadline" ).getTime() ) );
				
				pList.add( new Standard(
						con2.chkUserById( rs.getInt( "WriterId" ) ),
						rs.getString( "Title" ),
						rs.getString( "Content" ),
						upload,
						deadline,
						rs.getInt( "Agree" ),
						rs.getInt( "Disagree" ) ) );
			}
		}
	
		return pList;
	}
	
	public Standard getStandardById( int sId ) throws SQLException{
		rs = st.executeQuery( "select * from Proposal where FileId=" + sId + " isPro=\'F\'" );
		
		if( rs.next() ){
			Calendar upload = Calendar.getInstance();	upload.setTime( new Date( rs.getDate( "UploadDate" ).getTime() ) );
			Calendar deadline = Calendar.getInstance(); deadline.setTime( new Date( rs.getDate( "Deadline" ).getTime() ) );
			
			return new Standard(
					con2.chkUserById( rs.getInt( "WriterId" ) ),
					rs.getString( "Title" ),
					rs.getString( "Content" ),
					upload,
					deadline,
					rs.getInt( "Agree" ),
					rs.getInt( "Disagree" ) );
		}
		
		return null;
	}
	
	public ArrayList<Standard> getStangardByTitle( String title ) throws SQLException{
		ArrayList<Standard> pList = new ArrayList<Standard>();
		rs = st.executeQuery( "select * from Proposal where Title=" + title + " isPro=\'F\'" );
		
		while( rs.next() ){
			Calendar upload = Calendar.getInstance();	upload.setTime( new Date( rs.getDate( "UploadDate" ).getTime() ) );
			Calendar deadline = Calendar.getInstance(); deadline.setTime( new Date( rs.getDate( "Deadline" ).getTime() ) );
			
			pList.add( new Standard(
					chkUserById( rs.getInt( "WriterId" ) ),
					rs.getString( "Title" ),
					rs.getString( "Content" ),
					upload,
					deadline,
					rs.getInt( "Agree" ),
					rs.getInt( "Disagree" ) ) );
		}
		
		return pList;
	}
	
	public ArrayList<Standard> getStandardForAll() throws SQLException {
		ArrayList<Standard> pList = new ArrayList<Standard>();
		rs = st.executeQuery( "select * from Proposal where isPro=\'F\'" );
		while( rs.next() ){
			Calendar upload = Calendar.getInstance();	upload.setTime( new Date( rs.getDate( "UploadDate" ).getTime() ) );
			Calendar deadline = Calendar.getInstance(); deadline.setTime( new Date( rs.getDate( "Deadline" ).getTime() ) );
			
			pList.add( new Standard(
					con2.chkUserById( rs.getInt( "WriterId") ),
					rs.getString( "Title" ),
					rs.getString( "Content" ),
					upload,
					deadline,
					rs.getInt( "Agree" ),
					rs.getInt( "Disagree" ) ) );
		}
		return pList;
	}
	
	public int addStandard( String uName, Calendar date, Calendar endline, String title, String content ) throws SQLException{
		rs = st.executeQuery( "select * from Proposal where Title=\"" + title + "\" and isPro=\'F\'" );
		if( rs.next() )
			return SQLConnection.TITLE_EXIST;
		
		rs = st.executeQuery( "select * from UserInfo where Name=\"" + uName + "\"" );
		if( !rs.next() ) 
			return SQLConnection.USER_NO_FOUND;
		int uId = rs.getInt( "UserId" );
		
		rs = st.executeQuery( "select count(*) as totalitem from Proposal" );
		rs.next();
		int cnt = rs.getInt(1);
		pst = con.prepareStatement( "insert into Proposal " + 
				"( FileId, Title, WriterId, UploadDate, Deadline, Content, Status, Agree, Disagree, isPro ) "+
				"value ( ?,?,?,?,?,?,?,?,?,? )" );
		pst.setInt( 1, cnt + 1 );
		pst.setString( 2, title );
		pst.setInt( 3, uId );
		pst.setDate( 4, new java.sql.Date( date.getTime().getTime() ) );
		pst.setDate( 5, new java.sql.Date( endline.getTime().getTime() ) );
		pst.setString( 6, content );
		pst.setString( 7, "W");
		pst.setInt( 8, 0 );
		pst.setInt( 9, 0 );
		pst.setString( 10, "F" );
		pst.executeUpdate();
		return SQLConnection.SUCCESS;
	}
	
	public void addCommentForStandard( int standardId, String comment, int writerId ) throws SQLException{
		pst = con.prepareStatement( "insert into Comments ( FileId, WriterId, TimeStamp, Content ) values ( ?,?,?,? )" );
		pst.setInt( 1, standardId );
		pst.setInt( 2, writerId );
		pst.setDate( 3, new java.sql.Date( createCalendarForNow().getTime().getTime() )  );
		pst.setString( 4, comment );
		pst.executeUpdate();
	}
	
	public ArrayList<Comment> getCommentForStandard( int standardId ) throws SQLException{
		ArrayList<Comment> cList = new ArrayList<Comment>();
		rs = st.executeQuery( "select * from Comments where FileId=" + standardId );
		
		while( rs.next() ){
			cList.add( new Comment(
					standardId,
					con2.chkUserById( rs.getInt("WriterId") ),
					rs.getDate("Upload"),
					rs.getString("Content") ) );
		}
		return cList;
	}
	
	//Link Standard To Proposal
	//为ID为proposalId的提案增加ID为standardId的规范
	public int addStandardToProposal( int standardId, int proposalId ) throws SQLException{
		rs = st.executeQuery( "select * from StdsForPro where" + 
				" ProposalId=" + proposalId + 
				" StandardId=" + standardId );
		if( rs.next() )
			return 1;
		
		pst = con.prepareStatement( "insert into StdsForPro ( StandardId, Proposal ) values ( ?, ? )" );
		pst.setInt( 1, standardId );
		pst.setInt( 2, proposalId );
		pst.executeUpdate();
		
		return 0;
	}
	
	//查询ID为pId的提案的所有规范
	public ArrayList<Standard> chkAllStandardUnderProposal( int pId ) throws SQLException{
		ArrayList<Standard> sList = new ArrayList<Standard>();
		rs = st.executeQuery( "select StandardId from StdsForPro where ProposalId=" + pId );
		
		while( rs.next() ){
			sList.add( con2.getStandardById( rs.getInt( "StandardId" ) ) );
		}
		
		return sList;
	}
	
	//User
	//查询所有用户个人信息
	public ArrayList<User> chkUserForAll() throws SQLException{
		ArrayList<User> uList = new ArrayList<User>();
		ResultSet rs = st.executeQuery( "select * from UserInfo" );
		while( rs.next() ){
			Calendar cBirthDay = Calendar.getInstance();
			cBirthDay.setTime( new java.util.Date( rs.getDate( "Birth" ).getTime() ) );
			uList.add ( new User(
					rs.getString( "Name" ),
					rs.getString( "Gender" ).charAt(0),
					cBirthDay,
					rs.getString( "Address" ),
					rs.getString( "Tel" ),
					rs.getInt( "ReferrerId" ),
					rs.getInt( "IndustryId" ),
					rs.getInt( "CommitteeId" ),
					rs.getString( "Feature" ).charAt(0) ) );
		}
		rs.close();
		return uList;
	}
	
	//查询名称为name的用户的个人信息
	public User chkUserByName( String name ) throws SQLException{
		rs = st.executeQuery( "select * from UserInfo where Name=\"" + name + "\"" );
		if( rs.next() ){
			Calendar cBirthDay = Calendar.getInstance();
			cBirthDay.setTime( new java.util.Date( rs.getDate( "Birth" ).getTime() ) );
			return new User(
					rs.getString( "Name" ),
					rs.getString( "Gender" ).charAt(0),
					cBirthDay,
					rs.getString( "Address" ),
					rs.getString( "Tel" ),
					rs.getInt( "ReferrerId" ),
					rs.getInt( "IndustryId" ),
					rs.getInt( "CommitteeId" ),
					rs.getString( "Feature" ).charAt(0));
		}
		return null;
	}
	
	//查询ID为uId的用户的个人信息
	public User chkUserById( int uId ) throws SQLException{
		ResultSet rs = st.executeQuery( "select * from UserInfo where UserId=" + uId );
		if( rs.next() ){
			Calendar cBirthDay = Calendar.getInstance();
			cBirthDay.setTime( new java.util.Date( rs.getDate( "Birth" ).getTime() ) );
			return new User(
					rs.getString( "Name" ),
					rs.getString( "Gender" ).charAt(0),
					cBirthDay,
					rs.getString( "Address" ),
					rs.getString( "Tel" ),
					rs.getInt( "ReferrerId" ),
					rs.getInt( "IndustryId" ),
					rs.getInt( "CommitteeId" ),
					rs.getString( "Feature" ).charAt(0));
		}
		rs.close();
		return null;
	}
	
	//主函数（用不到）
	public static void main( String[] args ) throws ClassNotFoundException, SQLException{
		Scanner sin = new Scanner( System.in );
		
		SQLConnection con = new SQLConnection( true );
		if( !con.connectToDatabase( 
				"localhost:3306", 
				"DocManager", 
				"root", 
				"" ) ){
			System.out.println("Fail Connecting To Database !!!");
			return;
		}
		
//		System.out.print("Username : ");
//		String user = sin.next();
//		System.out.print("Password : ");
//		String pswd = sin.next();
//		if( con.chkLoginInfo( user, pswd) != 0 ){
//			System.out.println("Fail To Login");
//			return;
//		}
//		System.out.println( "You Have Successfully Logged In, " + user );
		
		System.out.println( con.chkUserForAll() );
		System.out.println("---------------------------------");
		System.out.println( con.chkUserById( 0 ) );
		System.out.println("---------------------------------");
		System.out.println( con.chkUserByName( "Jonathan" ) );
		System.out.println("---------------------------------");
		System.out.println( con.chkUserById( con.chkUserByName( "LuJianXi" ).getReferrer() ) );
		
		con.addStandard( 
				"Jonathan", 
				( new SQLConnection() ).createCalendarForNow(), 
				( new SQLConnection() ).createCalendar( 2020, 12, 20), 
				"Do I Look Like A Title?",
				"There Is No Content" );
		con.addStandard( 
				"Jonathan", 
				( new SQLConnection() ).createCalendarForNow(), 
				( new SQLConnection() ).createCalendar( 2047, 8, 22), 
				"It's an Important Day",
				"Remind yourself whose birthday it is" );
		
		System.out.println( con.getStandardForAll() );
	}
}
