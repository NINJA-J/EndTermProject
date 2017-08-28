package Jonathan;

import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
//import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	
	public static final int SUCCESS 			= 0;
	public static final int DB_OPER_FAILURE 	= 1;
	public static final int TITLE_EXIST 		= 2;
	public static final int TITLE_INEXIST		= 3;
	public static final int USER_EXIST 			= 4;
	public static final int USER_INEXIST		= 5;
	public static final int REFERRER_EXIST		= 6;
	public static final int REFERRER_INEXIST	= 7;
	public static final int DATE_FORMAT_ERROR 	= 8;
	public static final int FAILURE 			= 9;
	public static final int REFER_RECORD_EXIST 	= 10;
	public static final int PSWD_ERROR 			= 11;
	public static final int AUTH_LIMITED		= 12;
	
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
	//SQLConnection.SUCCESS			存在且正确
	//SQLConnection.PSWD_ERROR		密码错误
	//SQLConnection.USER_INEXIST	用户名不存在
	public int chkLoginInfo( String uName, String uPswd ) throws SQLException{
		boolean fPerson = false;
		st = con.createStatement();  //构造语句对象
		rs = st.executeQuery( "select * from LoginInfo where UName=\"" + uName + "\"" );
		while( rs.next() ){
			fPerson = true;
			if( rs.getString("Pswd").equals(uPswd) )
				return SQLConnection.SUCCESS;
		}
		if( fPerson )
			return SQLConnection.PSWD_ERROR;
		return SQLConnection.USER_INEXIST;
	}
	
	public int getLoginId( String name ) throws SQLException{
		st = con.createStatement();
		rs = st.executeQuery( "select UserId from LoginInfo where UName=\"" + name + "\"" );
		
		if( !rs.next() )
			return -1;
		return rs.getInt( "UserId" );
	}
	
	//立即注册用户名密码，返回值：
	//SQLConnection.USER_EXIST		注册成功
	//SQLConnection.SUCCESS			用户名已存在
	public int addLoginInfo( String uName, String uPswd ) throws SQLException{
		int cnt;
		rs = st.executeQuery( "select * from LoginInfo where UName=\"" + uName + "\"" );
		if( rs.next() ) 
			return SQLConnection.USER_EXIST;
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
		return SQLConnection.SUCCESS;
	}
	
	//创建java.sql.Date，表示当天时间
	public Calendar createCalendarForNow(){
		return Calendar.getInstance();
	}
	
	//创建设立的时间
	public Calendar createCalendar( int year, int month, int day ){
		Calendar c = Calendar.getInstance();
		c.set( year, month - 1, day, 0, 0, 0 ); 
		return c;
	}
	
	public Calendar createCalendarByString( String date ) throws ParseException{
		SimpleDateFormat sdf= new SimpleDateFormat("yyyy/MM/dd");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime( sdf.parse(date) );
		return calendar;
	}
	//Proposal Operation
	//查询所有uName用户填写的提案
	public ArrayList<Proposal> getProposalByUName( String uName ) throws SQLException{
		ArrayList<Proposal> pList = new ArrayList<Proposal>();
		st = con.createStatement();
		rs = st.executeQuery( "select * from UserInfo where Name=\"" + uName + "\"" );
		
		if( rs.next() ){
			int uId = rs.getInt( "UserId" );
			rs = st.executeQuery( "select * from Proposal where WriterId=" + uId + " and isPro=\'T\'" );
			while( rs.next() ){
				Calendar upload = Calendar.getInstance();	upload.setTime( new Date( rs.getDate( "UploadDate" ).getTime() ) );
				Calendar deadline = Calendar.getInstance(); deadline.setTime( new Date( rs.getDate( "Deadline" ).getTime() ) );
				pList.add( new Proposal(
						rs.getInt( "FileId" ),
						con2.chkUserById( rs.getInt( "WriterId" ) ),
						rs.getString( "Title" ),
						rs.getString( "Content" ),
						upload,
						deadline,
						rs.getInt( "Agree" ), 
						rs.getInt( "Disagree" ),
						rs.getString( "Status" ).charAt( 0 ) ) );
			}
		}
		
		return pList;
	}
	
	public ArrayList<Proposal> getProposalByUId( int uId ) throws SQLException{
		ArrayList<Proposal> pList = new ArrayList<Proposal>();
		st = con.createStatement();
		rs = st.executeQuery( "select * from UserInfo where UserId=\"" + uId + "\"" );
		
		if( rs.next() ){
			rs = st.executeQuery( "select * from Proposal where WriterId=" + uId + " and isPro=\'T\'" );
			while( rs.next() ){
				Calendar upload = Calendar.getInstance();	upload.setTime( new Date( rs.getDate( "UploadDate" ).getTime() ) );
				Calendar deadline = Calendar.getInstance(); deadline.setTime( new Date( rs.getDate( "Deadline" ).getTime() ) );
				pList.add( new Proposal(
						rs.getInt( "FileId" ),
						con2.chkUserById( rs.getInt( "WriterId" ) ),
						rs.getString( "Title" ),
						rs.getString( "Content" ),
						upload,
						deadline,
						rs.getInt( "Agree" ), 
						rs.getInt( "Disagree" ),
						rs.getString( "Status" ).charAt( 0 ) ) );
			}
		}
		
		return pList;
	}
	//查询Id为pId的提案
	public Proposal getProposalById( int pId ) throws SQLException{
		rs = st.executeQuery( "select * from Proposal where FileId=" + pId + " and isPro=\'T\'" );
		
		if( rs.next() ){
			Calendar upload = Calendar.getInstance();	upload.setTime( new Date( rs.getDate( "UploadDate" ).getTime() ) );
			Calendar deadline = Calendar.getInstance(); deadline.setTime( new Date( rs.getDate( "Deadline" ).getTime() ) );
			return new Proposal(
					rs.getInt( "FileId" ),
					con2.chkUserById( rs.getInt( "WriterId" ) ),
					rs.getString( "Title" ),
					rs.getString( "Content" ),
					upload,
					deadline,
					rs.getInt( "Agree" ),
					rs.getInt( "Disagree" ),
					rs.getString( "Status" ).charAt( 0 ) );
		}
		
		return null;
	}
	
	//查询标题为title的提案
	public ArrayList<Proposal> getProposalByTitle( String title ) throws SQLException{
		ArrayList<Proposal> pList = new ArrayList<Proposal>();
		rs = st.executeQuery( "select * from Proposal where Title=" + title + " and isPro=\'T\'" );
		
		while( rs.next() ){
			Calendar upload = Calendar.getInstance();	upload.setTime( new Date( rs.getDate( "UploadDate" ).getTime() ) );
			Calendar deadline = Calendar.getInstance(); deadline.setTime( new Date( rs.getDate( "Deadline" ).getTime() ) );
			
			pList.add( new Proposal(
					rs.getInt( "FileId" ),
					con2.chkUserById( rs.getInt( "WriterId" ) ),
					rs.getString( "Title" ),
					rs.getString( "Content" ),
					upload,
					deadline,
					rs.getInt( "Agree" ),
					rs.getInt( "Disagree" ),
					rs.getString( "Status" ).charAt( 0 ) ) );
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
					rs.getInt( "FileId" ),
					con2.chkUserById( rs.getInt( "WriterId" ) ),
					rs.getString( "Title"),
					rs.getString( "Content"),
					upload,
					deadline,
					rs.getInt( "Agree" ), 
					rs.getInt( "Disagree" ),
					rs.getString( "Status" ).charAt( 0 ) ) );
		}
		return pList;
	}
	
	/* 新建一份提案，返回值
	 * SQLConnection.TITLE_EXIST	该标题已存在
	 * SQLConnection.USER_NO_FOUND	写者不存在
	 * SQLConnection.SUCCESS		成功
	 * */
	public int addProposal( String uName, Calendar date, Calendar endline, String title, String content ) throws SQLException{
		rs = st.executeQuery( "select * from Proposal where Title=\"" + title + "\" and isPro=\'T\'" );
		if( rs.next() )
			return SQLConnection.TITLE_EXIST;
		
		rs = st.executeQuery( "select * from UserInfo where Name=\"" + uName + "\"" );
		if( !rs.next() ) 
			return SQLConnection.USER_INEXIST;
		int uId = rs.getInt( "UserId" );
		
		rs = st.executeQuery( "select count(*) as totalitem from Proposal" );
		rs.next();
		int cnt = rs.getInt(1);
		pst = con.prepareStatement( "insert into Proposal " + 
				"( FileId, Title, WriterId, UploadDate, Deadline, Content, Status, Agree, Disagree, isPro ) "+
				"values ( ?,?,?,?,?,?,?,?,?,? )" );
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
			rs = st.executeQuery( "select * from Proposal where WriterId=" + uId + " and isPro=\'F\'" );
			while( rs.next() ){
				Calendar upload = Calendar.getInstance();	upload.setTime( new Date( rs.getDate( "UploadDate" ).getTime() ) );
				Calendar deadline = Calendar.getInstance(); deadline.setTime( new Date( rs.getDate( "Deadline" ).getTime() ) );
				
				pList.add( new Standard(
						rs.getInt( "FileId" ),
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
		rs = st.executeQuery( "select * from Proposal where FileId=" + sId + " and isPro=\'F\'" );
		
		if( rs.next() ){
			Calendar upload = Calendar.getInstance();	upload.setTime( new Date( rs.getDate( "UploadDate" ).getTime() ) );
			Calendar deadline = Calendar.getInstance(); deadline.setTime( new Date( rs.getDate( "Deadline" ).getTime() ) );
			
			return new Standard(
					rs.getInt( "FileId" ),
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
		rs = st.executeQuery( "select * from Proposal where Title=" + title + " and isPro=\'F\'" );
		
		while( rs.next() ){
			Calendar upload = Calendar.getInstance();	upload.setTime( new Date( rs.getDate( "UploadDate" ).getTime() ) );
			Calendar deadline = Calendar.getInstance(); deadline.setTime( new Date( rs.getDate( "Deadline" ).getTime() ) );
			
			pList.add( new Standard(
					rs.getInt( "FileId" ),
					con2.chkUserById( rs.getInt( "WriterId" ) ),
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
					rs.getInt( "FileId" ),
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
			return SQLConnection.USER_INEXIST;
		int uId = rs.getInt( "UserId" );
		
		rs = st.executeQuery( "select count(*) as totalitem from Proposal" );
		rs.next();
		int cnt = rs.getInt(1);
		pst = con.prepareStatement( "insert into Proposal " + 
				"( FileId, Title, WriterId, UploadDate, Deadline, Content, Status, Agree, Disagree, isPro ) "+
				"values ( ?,?,?,?,?,?,?,?,?,? )" );
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
	
	/* 插入一个系统规范，返回值
	 * SQLConnection.USER_INEXIST		用户不存在
	 * SQLConnection.AUTH_LIMITED		权限不足
	 * SQLConnection.TITLE_EXIST		标题已存在
	 * SQLConnection.DB_OPER_FAILURE	数据库操作失败
	 * SQLConnection.SUCCESS			插入成功
	 * */
	public int addSystemStandard( int writerId, String title, String content, Calendar deadline ) {
		try {
			rs = st.executeQuery( "select * from UserInfo where UserId=" + writerId );
			if( !rs.next() )
				return SQLConnection.USER_INEXIST;
			if( rs.getString( "Feature").charAt( 0 ) != User.FEATURE_BOSS )
				return SQLConnection.AUTH_LIMITED;
			
			rs = st.executeQuery( "select * from SysStantard where title=\"" + title + "\"" );
			if( rs.next() )
				return SQLConnection.TITLE_EXIST;
			
			pst = con.prepareStatement( "insert into SysStandard ( WriterId, Title, Content, Deadline ) values ( ?,?,?,? )" );
			pst.setInt( 1, writerId );
			pst.setString( 2, title );
			pst.setString( 3, content );
			pst.setDate( 4, new java.sql.Date( deadline.getTime().getTime() ) );
			pst.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return SQLConnection.DB_OPER_FAILURE;
		}
		
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
				" ProposalId=" + proposalId + " and" +
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
					rs.getInt( "UserId" ),
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
					rs.getInt( "UserId" ),
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
	
	/* 添加一个用户的个人信息，返回值
	 * SQLConnection.USER_EXIST			同名（实名）用户已存在
	 * SQLConnection.REFERRER_NO_FOUND	推荐人不存在
	 * SQLConnection.DB_OPER_FAILURE	日期格式错误
	 * SQLConnection.DB_OPER_FAILURE	数据库操作失败
	 * SQLConnection.SUCCESS			成功
	 * */
	public int addUser( int uId, String name, String gender, String bDate, String address, String tel, String referrer, String industry, String committee ) throws SQLException{
		User user = chkUserByName( name );
		User refer = null;
		if( user != null )
			return SQLConnection.USER_EXIST;
		
		if( referrer != "" ){
			refer = chkUserByName( referrer );
			if( refer == null )
				return SQLConnection.REFERRER_INEXIST;
		}
		
		Calendar birth = null;
		try {
			birth = createCalendarByString(bDate);
		} catch (ParseException e) {
			return SQLConnection.DATE_FORMAT_ERROR;
		}
		
		pst = con.prepareStatement( "insert into UserInfo " + 
						"( UserId, Name, Gender, Birth, Address, Tel, ReferrerId, CommitteeId, IndustryId, Feature ) " + 
						"values ( ?,?,?,?,?,?,?,?,?,? )" );
		pst.setInt( 1,  uId );
		pst.setString( 2, name );
		pst.setString( 3, gender );
		pst.setDate( 4, new java.sql.Date( birth.getTime().getTime() ) );
		pst.setString( 5,  address );
		pst.setString( 6, tel );
		pst.setInt( 7, ( refer == null ? -1 : refer.getId() ) );
		pst.setInt( 8, Integer.valueOf( committee ) );
		pst.setInt( 9, Integer.valueOf( industry ) );
		pst.setString(10, String.valueOf( User.FEATURE_NORMAL ) );
		pst.executeUpdate();
		
		pst = con.prepareStatement( "insert into EnrollRequest ( UserId, CommitteeId, IndustryId ) values ( ?,?,? )" );
		pst.setInt( 1, uId );
		pst.setInt( 2, Integer.valueOf(committee));
		pst.setInt( 3, Integer.valueOf(industry) );
		pst.executeUpdate();
		
		return SQLConnection.SUCCESS;
	}
	
	public int getProporsalAmountByUId( int uId ) throws SQLException{
		rs = st.executeQuery( "select count(*) as totalItem from Proposal where WriterId=" + uId + " and isPro=\'T\'" );
		rs.next();
		
		return rs.getInt( 1 );
	}
	
	public int getStandardAmountByUId( int uId ) throws SQLException{
		rs = st.executeQuery( "select count(*) as totalItem from Proposal where WriterId=" + uId + " and isPro=\'F\'" );
		rs.next();
		
		return rs.getInt( 1 );
	}
	
	//查询ID为uId的用户的个人信息
	public User chkUserById( int uId ) throws SQLException{
		rs = st.executeQuery( "select * from UserInfo where UserId=" + uId );
		if( rs.next() ){
			Calendar cBirthDay = Calendar.getInstance();
			cBirthDay.setTime( new java.util.Date( rs.getDate( "Birth" ).getTime() ) );
			return new User(
					rs.getInt( "UserId" ),
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
	
	//查询admin可操作的所有入会请求
	public ArrayList<EnrollRequest> getEnrollRequestFor( User admin ) throws SQLException{
		ArrayList<EnrollRequest> eList = new ArrayList<EnrollRequest>();
		String fStr = "";
		switch( admin.getFeature() ){
		case User.FEATURE_INDUSTRY_MANAGER:  fStr = "IndustryId <> -1";  break;
		case User.FEATURE_COMMITTEE_MANAGER: fStr = "CommitteeId <> -1"; break;
		case User.FEATURE_SEMINAR_MANAGER:	 fStr = "SeminarId <> -1";   break;
		default: return eList;
		}
		rs = st.executeQuery( "select * from EnrollRequest where " + fStr );
		while( rs.next() ){
			eList.add( new EnrollRequest( 
					new User( con2.chkUserById( rs.getInt( "UserId" ) ) ),
					rs.getInt( "IndustryId" ),
					rs.getInt( "CommitteeId" ),
					-1 ) );
		}
		return eList;
	}
	
	//由admin批准req请求发起者入会
	public int permitEnrollRequest( User admin, EnrollRequest req ) throws SQLException{
		String fStr, operStr;
		switch( admin.getFeature() ){
		case User.FEATURE_INDUSTRY_MANAGER:  fStr = "IndustryId";  break;
		case User.FEATURE_COMMITTEE_MANAGER: fStr = "CommitteeId"; break;
		case User.FEATURE_SEMINAR_MANAGER:	 fStr = "SeminarId";   break;
		default: return SQLConnection.FAILURE;
		}
		rs = st.executeQuery( "select * from EnrollRequest where UserId=" + req.getId() + " and " + fStr + " <> -1" );
		if( !rs.next() )
			return SQLConnection.FAILURE;
		
		pst = con.prepareStatement( "update UserInfo set " + fStr + "=? , Feature=? where UserId=?" );
		pst.setInt( 1, req.getProperReqId( admin ) );
		pst.setString( 2, String.valueOf( admin.getFeature() ) );
		pst.setInt( 3, req.getUser().getId() );
		pst.executeUpdate();
		
		pst = con.prepareStatement( "delete from EnrollRequest where UserId=?" );
		pst.setInt( 1, req.getUser().getId() );
		pst.executeUpdate();
		
		return SQLConnection.SUCCESS;
	}
	
	//由admin拒绝req请求发起者入会
	public int rejectEnrollRequest( User admin, EnrollRequest req ) throws SQLException{
		String fStr;
		switch( admin.getFeature() ){
		case User.FEATURE_INDUSTRY_MANAGER:  fStr = "IndustryId";  break;
		case User.FEATURE_COMMITTEE_MANAGER: fStr = "CommitteeId"; break;
		case User.FEATURE_SEMINAR_MANAGER:	 fStr = "SeminarId";   break;
		default: return SQLConnection.FAILURE;
		}
		
		rs = st.executeQuery( "select * from EnrollRequest where UserId=" + req.getId() + " and " + fStr + " <> -1" );
		if( !rs.next() )
			return SQLConnection.SUCCESS;
		
		pst = con.prepareStatement( "delete from EnrollRequest where UserId=?" );
		pst.setInt( 1, req.getUser().getId() );
		pst.executeUpdate();
		
		return SQLConnection.SUCCESS;
	}
	
	/* 添加一个用户的个人信息，返回值
	 * SQLConnection.USER_EXIST			同名（实名）用户已存在
	 * SQLConnection.REFERRER_NO_FOUND	推荐人不存在
	 * SQLConnection.DB_OPER_FAILURE	日期格式错误
	 * SQLConnection.DB_OPER_FAILURE	数据库操作失败
	 * SQLConnection.SUCCESS			成功
	 * */
	public int addReferInfo( String userName, int adminName, String profession, String office, String duty, String reason ) throws SQLException{
		User user = chkUserByName( userName );
		User admin = chkUserById( adminName );
		
		if( user == null )
			return SQLConnection.USER_INEXIST;
		
		if( admin == null )
			return SQLConnection.REFERRER_INEXIST;
		
		rs = st.executeQuery( "select * from ReferrerList where UserId=" + user.getId() + " and ReferrerId=" + admin.getId() );
		if( rs.next() )
			return SQLConnection.REFER_RECORD_EXIST;
		
		pst = con.prepareStatement( "insert into ReferrerList ( UserId, ReferrerId, Profession, Office, Duty, Reason ) values ( ?,?,?,?,?,? )" );
		pst.setInt( 1, user.getId() );
		pst.setInt( 2, admin.getId() );
		pst.setString( 3, profession );
		pst.setString( 4, office );
		pst.setString( 5, duty );
		pst.setString( 6,  reason );
		pst.executeUpdate();
		
		return SQLConnection.SUCCESS;
	}
	
	//主函数（用不到）
	public static void main( String[] args ) throws ClassNotFoundException, SQLException{
		Scanner sin = new Scanner( System.in );
		
//		SQLConnection con = new SQLConnection();
//		if( !con.connectToDatabase( 
//				"localhost:3306", 
//				"DocManager", 
//				"root", 
//				"" ) ){
//			System.out.println("Fail Connecting To Database !!!");
//			return;
//		}
		
		String s = new String( "我是人".getBytes(), Charset.forName("latin2") );
//		System.out.println( s );
		
//		System.out.println( "Proposal for writter No.0 = " + con.getProporsalAmountByUId( 0 ) );
//		System.out.println( "Standard for writter No.0 = " + con.getStandardAmountByUId( 0 ) );
//		System.out.println( "Proposal for writter No.1 = " + con.getProporsalAmountByUId( 1 ) );
//		System.out.println( "Standard for writter No.1 = " + con.getStandardAmountByUId( 1 ) );
	}
}
