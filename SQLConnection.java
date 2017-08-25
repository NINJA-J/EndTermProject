package Jonathan;

import java.sql.*;
import java.util.ArrayList;
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
	public static int DATA_EXIST 	= 2;
	public static int USER_ERROR 	= 3;
	public static int PSWD_ERROR 	= 4;
	
	//Connection
	//初始化
	public SQLConnection() throws ClassNotFoundException, SQLException{
		super();
		Class.forName("com.mysql.jdbc.Driver");
	}
	
	//连接数据库，返回值：
	//true  - 成功
	//false - 失败
	public boolean connectToDatabase( String url, String baseName, String user, String pswd ) throws SQLException{
		con = DriverManager.getConnection( 
				url + "/" + baseName,
				user,
				pswd );
		if( con.isClosed() )
			return false;
		st = con.createStatement();
		return true;
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
				pList.add( new Proposal(
						chkUserById( rs.getInt( "WriterId" )),
						rs.getString( "Title" ),
						rs.getString( "Content" ),
						rs.getDate( "UploadDate" ),
						rs.getDate( "Deadline" ),
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
			return new Proposal(
					chkUserById( rs.getInt( "WriterId" ) ),
					rs.getString( "Title" ),
					rs.getString( "Content" ),
					rs.getDate( "UploadDate" ),
					rs.getDate( "Deadline" ),
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
			pList.add( new Proposal(
					chkUserById( rs.getInt( "WriterId" ) ),
					rs.getString( "Title" ),
					rs.getString( "Content" ),
					rs.getDate( "UploadDate" ),
					rs.getDate( "Deadline" ),
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
			System.out.println("Found a proposal");
			pList.add( new Proposal(
					chkUserById( rs.getInt( "WriterId" )),
					rs.getString( "Title"),
					rs.getString( "Content"),
					rs.getDate( "UploadDate"),
					rs.getDate( "Deadline"),
					rs.getInt( "Agree"),
					rs.getInt( "Disagree") ) );
		}
		return pList;
	}
	
	//新建一份提案
	public void addProposal( String uName, Date date, Date endline, String title, String content ) throws SQLException{
		rs = st.executeQuery( "select * from UserInfo where UName=\"" + uName + "\"" );
		if( !rs.next() ) return;
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
		pst.setDate( 4, date );
		pst.setDate( 5, endline );
		pst.setString( 6, content );
		pst.setString( 7, "W");
		pst.setInt( 8, 0 );
		pst.setInt( 9, 0 );
		pst.setString( 10, "T" );
		pst.executeUpdate();
	}
	
	//按照ID为提案增加评论
	public void addCommentForProposal( int proposalId, String comment, int writerId ) throws SQLException{
		pst = con.prepareStatement( "insert into Comments ( FileId, WriterId, TimeStamp, Content ) values ( ?,?,?,? )" );
		pst.setInt( 1, proposalId );
		pst.setInt( 2, writerId );
		pst.setDate( 3, Date.valueOf( java.time.LocalDate.now() ) );
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
					chkUserById( rs.getInt("WriterId") ),
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
				pList.add( new Standard(
						chkUserById( rs.getInt( "WriterId" ) ),
						rs.getString( "Title" ),
						rs.getString( "Content" ),
						rs.getDate( "UploadDate" ),
						rs.getDate( "Deadline" ),
						rs.getInt( "Agree" ),
						rs.getInt( "Disagree" ) ) );
			}
		}
	
		return pList;
	}
	
	public Standard getStandardById( int sId ) throws SQLException{
		rs = st.executeQuery( "select * from Proposal where FileId=" + sId + " isPro=\'F\'" );
		
		if( rs.next() ){
			return new Standard(
					chkUserById( rs.getInt( "WriterId" ) ),
					rs.getString( "Title" ),
					rs.getString( "Content" ),
					rs.getDate( "UploadDate" ),
					rs.getDate( "Deadline" ),
					rs.getInt( "Agree" ),
					rs.getInt( "Disagree" ) );
		}
		
		return null;
	}
	
	public ArrayList<Standard> getStangardByTitle( String title ) throws SQLException{
		ArrayList<Standard> pList = new ArrayList<Standard>();
		rs = st.executeQuery( "select * from Proposal where Title=" + title + " isPro=\'F\'" );
		
		while( rs.next() ){
			pList.add( new Standard(
					chkUserById( rs.getInt( "WriterId" ) ),
					rs.getString( "Title" ),
					rs.getString( "Content" ),
					rs.getDate( "UploadDate" ),
					rs.getDate( "Deadline" ),
					rs.getInt( "Agree" ),
					rs.getInt( "Disagree" ) ) );
		}
		
		return pList;
	}
	
	public ArrayList<Standard> getStandardForAll() throws SQLException {
		ArrayList<Standard> pList = new ArrayList<Standard>();
		rs = st.executeQuery( "select * from Proposal where isPro=\'F\'" );
		while( rs.next() ){
			pList.add( new Standard(
					chkUserById( rs.getInt( "WriterId") ),
					rs.getString( "Title" ),
					rs.getString( "Content" ),
					rs.getDate( "UploadDate" ),
					rs.getDate( "Deadline" ),
					rs.getInt( "Agree" ),
					rs.getInt( "Disagree" ) ) );
		}
		return pList;
	}
	
	public void addStandard( String uName, Date date, Date endline, String title, String content ) throws SQLException{
		rs = st.executeQuery( "select * from UserInfo where UName=\"" + uName + "\"" );
		if( !rs.next() ) return;
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
		pst.setDate( 4, date );
		pst.setDate( 5, endline );
		pst.setString( 6, content );
		pst.setString( 7, "W");
		pst.setInt( 8, 0 );
		pst.setInt( 9, 0 );
		pst.setString( 10, "F" );
		pst.executeUpdate();
	}
	
	public void addCommentForStandard( int standardId, String comment, int writerId ) throws SQLException{
		pst = con.prepareStatement( "insert into Comments ( FileId, WriterId, TimeStamp, Content ) values ( ?,?,?,? )" );
		pst.setInt( 1, standardId );
		pst.setInt( 2, writerId );
		pst.setDate( 3, Date.valueOf( java.time.LocalDate.now() ) );
		pst.setString( 4, comment );
		pst.executeUpdate();
	}
	
	public ArrayList<Comment> getCommentForStandard( int standardId ) throws SQLException{
		ArrayList<Comment> cList = new ArrayList<Comment>();
		rs = st.executeQuery( "select * from Comments where FileId=" + standardId );
		
		while( rs.next() ){
			cList.add( new Comment(
					standardId,
					chkUserById( rs.getInt("WriterId") ),
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
			sList.add( getStandardById( rs.getInt( "StandardId" ) ) );
		}
		
		return sList;
	}
	
	//User
	//查询所有用户个人信息
	public ArrayList<User> chkUserForAll() throws SQLException{
		ArrayList<User> uList = new ArrayList<User>();
		rs = st.executeQuery( "select * from UserInfo" );
		while( rs.next() ){
			uList.add( new User(
					rs.getString("UName"),
					rs.getString("Gender").charAt(0),
					rs.getDate("BirthDate"),
					rs.getString("Address"),
					rs.getString("Tel"),
					chkUserById( rs.getInt("ReferrerId") ),
					rs.getInt("IndustryId"),
					rs.getInt("CommitteeId") ) );
		}
		return uList;
	}
	
	//查询名称为name的用户的个人信息
	public User chkUserByName( String name ) throws SQLException{
		rs = st.executeQuery( "select * from UserInfo where UName=\"" + name + "\"" );
		if( rs.next() ){
			return new User(
					rs.getString("UName"),
					rs.getString("Gender").charAt(0),
					rs.getDate("BirthDate"),
					rs.getString("Address"),
					rs.getString("Tel"),
					chkUserById( rs.getInt("ReferrerId") ),
					rs.getInt("IndustryId"),
					rs.getInt("CommitteeId") );
		}
		return null;
	}
	
	//查询ID为uId的用户的个人信息
	public User chkUserById( int uId ) throws SQLException{
		rs = st.executeQuery( "select * from UserInfo where UserId=" + uId );
		if( rs.next() ){
			return new User(
					rs.getString("UName"),
					rs.getString("Gender").charAt(0),
					rs.getDate("BirthDate"),
					rs.getString("Address"),
					rs.getString("Tel"),
					chkUserById( rs.getInt("ReferrerId") ),
					rs.getInt("IndustryId"),
					rs.getInt("CommitteeId") );
		}
		return null;
	}
	
	//主函数（用不到）
	public static void main( String[] args ) throws ClassNotFoundException, SQLException{
		SQLConnection con = new SQLConnection();
//		ArrayList<Proposal> pList = con.chkProposal();
//		for( Proposal p : pList ){
//			System.out.println( p.toString() );
//		}
		int ans = con.addLoginInfo( "LuJianXi",  "ThisIsAPswd" );
		if( ans == 0 ) System.out.println("Create New User Success");
		if( ans == 1 ) System.out.println("User Already Exists");
		con.closeConn();
	}
}
