package Jonathan;

import java.util.Calendar;

public class User {
	
	public static final char FEATURE_NORMAL 			= 'N';
	public static final char FEATURE_MEMBER 			= 'M';
	public static final char FEATURE_INDUSTRY_MANAGER 	= 'I';
	public static final char FEATURE_COMMITTEE_MANAGER 	= 'C';
	public static final char FEATURE_SEMINAR_MANAGER 	= 'S';
	public static final char FEATURE_BOSS 				= 'B';
	
	int userId;
	String name;
	char gender;
	Calendar birthDate;
	String address;
	String tel;
	int referrerId;
	int industryId;
	int committeeId;
	int seminarId;
	char feature;
	
	public User( int uId, String name, char gender, Calendar bDate, String address, String tel, int rId, int iId, int cId, int sId, char feature ){
		this.userId = uId;
		this.name = name;
		this.gender = gender;
		this.birthDate = bDate;
		this.address = address;
		this.tel = tel;
		this.referrerId = rId;
		this.industryId = iId;
		this.committeeId = cId;
		this.seminarId = sId;
		this.feature = feature;
	}
	
	public User( User u ){
		this.userId = u.getId();
		this.name = u.getName();
		this.gender = u.getGender();
		this.birthDate = u.getBirthDate();
		this.address = u.getAddress();
		this.tel = u.getTel();
		this.referrerId = u.getReferrer();
		this.industryId = u.getIndustry();
		this.committeeId = u.getCommitteeId();
		this.feature = u.getFeature();
	}
	
	public int getId(){ return userId; }
	
	public String getName(){ return name; }
	
	public boolean isMale(){ return gender == 'M'; }
	
	public boolean isFemale(){ return gender == 'F'; }
	
	public char getGender(){ return gender; }
	
	public Calendar getBirthDate(){ return birthDate; }
	
	public String getAddress(){ return address; }
	
	public String getTel(){ return tel; }
	
	public int getReferrer(){ return referrerId; }
	
	public int getIndustry(){ return industryId; }
	
	public int getCommitteeId(){ return committeeId; }
	
	public int getSeminarId(){ return seminarId; }
	
	//判断身份
	public boolean isFeatureNormal(){ return feature == User.FEATURE_NORMAL; }
	
	public boolean isFeatureMember(){ return feature == User.FEATURE_MEMBER; }
	
	public boolean isFeatureIndustryM(){ return feature == User.FEATURE_INDUSTRY_MANAGER; }
	
	public boolean isFeatureCommitteeM(){ return feature == User.FEATURE_COMMITTEE_MANAGER; }
	
	public boolean isFeatureFinalM(){ return feature == User.FEATURE_SEMINAR_MANAGER; }
	
	public boolean isFeatureBoss(){ return feature == User.FEATURE_BOSS; }
	
	public char getFeature(){ return feature; }
	
	public String toString( String preBlock ){
		return  preBlock + name + " : \n"+
				preBlock + "    Gender    : " + gender + "\n" +
				preBlock + "    BirthDate : " + birthDate + "\n" +
				preBlock + "    Address   : " + address + "\n" +
				preBlock + "    Tel       : " + tel + "\n" +
				preBlock + "    Referrer  : " + referrerId + "\n" +
				preBlock + "    Industry  : " + industryId + "\n" +
				preBlock + "    Committee : " + committeeId + "\n";
	}
	
	public String toString(){
		return this.toString("");
	}
}
