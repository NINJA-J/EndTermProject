package Jonathan;

import java.sql.Date;

public class User {
	String name;
	char gender;
	Date birthDate;
	String address;
	String tel;
	User referrer;
	int industryId;
	int committeeId;
	public User( String name, char gender, Date bDate, String address, String tel, User referrer, int iName, int cName ){
		this.name = name;
		this.gender = gender;
		this.birthDate = bDate;
		this.address = address;
		this.tel = tel;
		this.referrer = referrer;
		this.industryId = iName;
		this.committeeId = cName;
	}
	
	public String getName(){ return name; }
	
	public boolean isMale(){ return gender == 'M'; }
	
	public boolean isFemale(){ return gender == 'F'; }
	
	public Date getBirthDate(){ return birthDate; }
	
	public String getAddress(){ return address; }
	
	public String getTel(){ return tel; }
	
	public User getReferrer(){ return referrer; }
	
	public int getIndustry(){ return industryId; }
	
	public int getCommitteeId(){ return committeeId; }
	
	public String toString( String preBlock ){
		return  preBlock + name + " : \n"+
				preBlock + "    Gender    : " + gender + "\n" +
				preBlock + "    BirthDate : " + birthDate + "\n" +
				preBlock + "    Address   : " + address + "\n" +
				preBlock + "    Tel       : " + tel + "\n" +
				preBlock + "    Referrer  : " + referrer.getName() + "\n" +
				preBlock + "    Industry  : " + industryId + "\n" +
				preBlock + "    Committee : " + committeeId + "\n";
	}
	public String toString(){
		return this.toString("");
	}
}
