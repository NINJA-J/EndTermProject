package Jonathan;

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
//	public static final int USER_ERROR 			= 10;
	public static final int PSWD_ERROR 			= 11;
	
	SQLConnection con2 = null;
	
	//Connection
	//初始化
	public SQLConnection() throws ClassNotFoundException, SQLException;
	
	public SQLConnection( boolean createCopy ) throws ClassNotFoundException, SQLException;
	
	//连接数据库，返回值：
	//true  - 成功
	//false - 失败
	public boolean connectToDatabase( String url, String baseName, String user, String pswd ) throws SQLException;
	
	//关闭当前连接
	public void closeConn() throws SQLException;
	
	//Login
	//查询用户名密码存在存在，返回值：
	//SQLConnection.SUCCESS			存在且正确
	//SQLConnection.PSWD_ERROR		密码错误
	//SQLConnection.USER_INEXIST	用户名不存在
	public int chkLoginInfo( String uName, String uPswd ) throws SQLException;
	
	//立即注册用户名密码，返回值：
	//SQLConnection.USER_EXIST		注册成功
	//SQLConnection.SUCCESS			用户名已存在
	public int addLoginInfo( String uName, String uPswd ) throws SQLException;
	
	//创建java.sql.Date，表示当天时间
	public Calendar createCalendarForNow();
	
	//创建设立的时间
	public Calendar createCalendar( int year, int month, int day );
	
	public Calendar createCalendarByString( String date ) throws ParseException;
  
	//Proposal Operation
	//查询所有uName用户填写的提案
	public ArrayList<Proposal> getProposalByUName( String uName ) throws SQLException;
	
	//查询Id为pId的提案
	public Proposal getProposalById( int pId ) throws SQLException;
	
	//查询标题为title的提案
	public ArrayList<Proposal> getProposalByTitle( String title ) throws SQLException;

	//查询所有提案
	public ArrayList<Proposal> getProposalForAll() throws SQLException;
	
	/* 新建一份提案，返回值
	 * SQLConnection.TITLE_EXIST	该标题已存在
	 * SQLConnection.USER_NO_FOUND	写者不存在
	 * SQLConnection.SUCCESS		成功
	 * */
	public int addProposal( String uName, Calendar date, Calendar endline, String title, String content ) throws SQLException;
	
	//按照ID为提案增加评论
	public void addCommentForProposal( int proposalId, String comment, int writerId ) throws SQLException;
	
	//查询ID为proposalId的提案的所有评论
	public ArrayList<Comment> getCommentForProposal( int proposalId ) throws SQLException;
	
	//Standard Operation 功能同上
	public ArrayList<Standard> getStandardByWriter( String uName ) throws SQLException;
	
	public Standard getStandardById( int sId ) throws SQLException;
	
	public ArrayList<Standard> getStangardByTitle( String title ) throws SQLException;
	
	public ArrayList<Standard> getStandardForAll() throws SQLException;
	
	public int addStandard( String uName, Calendar date, Calendar endline, String title, String content ) throws SQLException;
	
	public void addCommentForStandard( int standardId, String comment, int writerId ) throws SQLException;
	
	public ArrayList<Comment> getCommentForStandard( int standardId ) throws SQLException;
	
	//Link Standard To Proposal
	//为ID为proposalId的提案增加ID为standardId的规范
	public int addStandardToProposal( int standardId, int proposalId ) throws SQLException;
	
	//查询ID为pId的提案的所有规范
	public ArrayList<Standard> chkAllStandardUnderProposal( int pId ) throws SQLException;
	
	//User
	//查询所有用户个人信息
	public ArrayList<User> chkUserForAll() throws SQLException;
	
	//查询名称为name的用户的个人信息
	public User chkUserByName( String name ) throws SQLException;
	
	/* 添加一个用户的个人信息，返回值
	 * SQLConnection.USER_EXIST			同名（实名）用户已存在
	 * SQLConnection.REFERRER_NO_FOUND	推荐人不存在
	 * SQLConnection.DB_OPER_FAILURE	日期格式错误
	 * SQLConnection.DB_OPER_FAILURE	数据库操作失败
	 * SQLConnection.SUCCESS			成功
	 * */
	public int addUser( String name, String gender, String bDate, String address, String tel, String referrer, String industry, String committee, char feature ) throws SQLException;
	
	//查询ID为uId的用户的个人信息
	public User chkUserById( int uId ) throws SQLException;
	
	//查询admin可操作的所有入会请求
	public ArrayList<EnrollRequest> getEnrollRequestFor( User admin ) throws SQLException;
	
	//由admin批准req请求发起者入会
	public int permitEnrollRequest( User admin, EnrollRequest req ) throws SQLException;
	
	//由admin拒绝req请求发起者入会
	public int rejectEnrollRequest( User admin, EnrollRequest req ) throws SQLException;
	
	//主函数（用不到）
	public static void main( String[] args ) throws ClassNotFoundException, SQLException;
}
