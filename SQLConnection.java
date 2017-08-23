package Jonathan;

import java.sql.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Driver;

import Jonathan.Proposal;
import Jonathan.User;

public class SQLConnection {
	Connection con;
	Statement st;
	ResultSet rs;
	
	String url = "jdbc:mysql://localhost:3306";
	String dataBase = "DocManager";
	String usrName = "root";//"614e2a1794894986bbfba809b6a0ee46";
	String usrPswd = "";//5aa5185cdf504b45b07bd33ef261c065";
	
	public SQLConnection() throws ClassNotFoundException, SQLException{
		super();
		Class.forName("com.mysql.jdbc.Driver");
		con = DriverManager.getConnection( 
					url + "/" + dataBase,
					usrName,
					usrPswd );
	}
	
	public void closeConn() throws SQLException{
		con.close();
	}
	
	public int chkUserInfo( String uName, String uPswd ) throws SQLException{
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
	
	public int addUserInfo( String uName, String uPswd ) throws SQLException{
		int cnt;
		st = con.createStatement();  //构造语句对象
		rs = st.executeQuery( "select * from LoginInfo where UName=\"" + uName + "\"" );
		if( rs.next() ) 
			return 1;
		
		rs = st.executeQuery( "select count(*) as totalitem from LoginInfo" );
		rs.next();
		cnt = rs.getInt( "count(*)" );
		
		rs = st.executeQuery(
				"insert into LoginInfo ( UserId, UName, Pswd, Email ) values ( " + 
				( cnt + 1 ) + ", " +
				"\"" + uName + "\", " +
				"\"" + uPswd + "\", " +
				"\"" + uName + "\" )" );
		return 0;
	}
	
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
	
	public void addProposal( String uName, Date date, String title, String content ){
		
	}
	
	public ArrayList<Proposal> chkStandardByUName( String uName ) throws SQLException{
		ArrayList<Proposal> pList = new ArrayList<Proposal>();
		st = con.createStatement();
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
		st = con.createStatement();
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
	
	public void addStandard( String uName, Date date, String title, String content ){
		
	}
	
	public ArrayList<User> chkUserStatus() throws SQLException{
		ArrayList<User> uList = new ArrayList<User>();
		st = con.createStatement();
		rs = st.executeQuery( "select * from UserInfo" );
		while( rs.next() ){
			uList.add( new User(
					rs.getString("UName"),
					rs.getString("Gender").charAt(0),
					rs.getDate("birthDate"),
					rs.getString("Address"),
					rs.getString("Tel"),
					rs.getString("ReferrerId"),
					rs.getString("IndustryId"),
					rs.getString("CommitteeId") ) );
		}
		return uList;
	}
	
	public static void main( String[] args ) throws ClassNotFoundException, SQLException{
		SQLConnection con = new SQLConnection();
		ArrayList<Proposal> pList = con.chkProposal();
		for( Proposal p : pList ){
			System.out.println( p.toString() );
		}
		con.closeConn();
	}
}
