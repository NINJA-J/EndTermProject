package Jonathan;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Driver;

import Jonathan.Proposal;
import Jonathan.User;

public class SQLConnection {
	Connection con;
	Statement st;
	java.sql.PreparedStatement pst;
	ResultSet rs;
	
	String url = "jdbc:mysql://localhost:3306";
	String dataBase = "DocManager";
	String usrName = "root";//"614e2a1794894986bbfba809b6a0ee46";
	String usrPswd = "";//5aa5185cdf504b45b07bd33ef261c065";
	//Connection
	public SQLConnection() throws ClassNotFoundException, SQLException{
		super();
		Class.forName("com.mysql.jdbc.Driver");
		con = DriverManager.getConnection( 
					url + "/" + dataBase,
					usrName,
					usrPswd );
		st = con.createStatement();
	}
	
	public void closeConn() throws SQLException{
		con.close();
	}
	//Login
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
	//Proposal
	public ArrayList<Proposal> chkProposalByUName( String uName ) throws SQLException{
		ArrayList<Proposal> pList = new ArrayList<Proposal>();
		st = con.createStatement();
		rs = st.executeQuery( "select * from UserInfo where Name=\"" + uName + "\"" );
		
		if( rs.next() ){
			int uId = rs.getInt( "UserId" );
			rs = st.executeQuery( "select * from Proposal where WriterId=" + uId + " isPro=\'T\'" );
			while( rs.next() ){
				pList.add( new Proposal(
						uName,
						rs.getString("Title"),
						rs.getString("Content"),
						rs.getDate("UploadDate"),
						rs.getDate("Deadline"),
						true ));
			}
		}
		
		return pList;
	}
	
	public ArrayList<Proposal> chkProposal() throws SQLException{
		ArrayList<Proposal> pList = new ArrayList<Proposal>();
		st = con.createStatement();
		rs = st.executeQuery( "select * from Proposal where isPro=\'T\'" );
		while( rs.next() ){
			System.out.println("Found a proposal");
			pList.add( new Proposal(
					rs.getString("WriterId"),
					rs.getString("Title"),
					rs.getString("Content"),
					rs.getDate("UploadDate"),
					rs.getDate("Deadline"),
					true ) );
		}
		return pList;
	}
	
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
	
	public ArrayList<Proposal> chkStandardByUName( String uName ) throws SQLException{
		ArrayList<Proposal> pList = new ArrayList<Proposal>();
		rs = st.executeQuery( "select * from UserInfo where Name=\"" + uName + "\"" );
		
		if( rs.next() ){
			int uId = rs.getInt( "UserId" );
			rs = st.executeQuery( "select * from Proposal where WriterId=" + uId + " isPro=\'F\'" );
			while( rs.next() ){
				pList.add( new Proposal(
						uName,
						rs.getString("Title"),
						rs.getString("Content"),
						rs.getDate("UploadDate"),
						rs.getDate("Deadline"),
						true ));
			}
		}
		
		return pList;
	}
	
	public ArrayList<Proposal> chkStandard() throws SQLException {
		ArrayList<Proposal> pList = new ArrayList<Proposal>();
		rs = st.executeQuery( "select * from Proposal where isPro=\'F\'" );
		while( rs.next() ){
			pList.add( new Proposal(
					rs.getString("WriterId"),
					rs.getString("Title"),
					rs.getString("Content"),
					rs.getDate("UploadDate"),
					rs.getDate("Deadline"),
					true ));
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
	//User
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
