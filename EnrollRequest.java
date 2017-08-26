package Jonathan;

public class EnrollRequest extends User {
	
	int industryReqId;
	int committeeReqId;
	int seminarReqId;

	public EnrollRequest( User u, int industryReq, int committeeReq, int seminarReq ) {
		super( u );
		this.industryReqId = industryReq;
		this.committeeReqId = committeeReq;
		this.seminarReqId = seminarReq;
	}
	
	public int getIndustryReqId(){ return industryReqId; }
	
	public int getCommitteeReqId(){ return committeeReqId; }
	
	public int getSeminarReqId(){ return seminarReqId; }
	
	public int getProperReqId( User admin ){
		switch( admin.getFeature() ){
		case User.FEATURE_INDUSTRY_MANAGER:	return industryReqId;
		case User.FEATURE_COMMITTEE_MANAGER:	return committeeReqId;
		case User.FEATURE_SEMINAR_MANAGER:		return seminarReqId;
		default: return -1;
		}
	}
	
	public User getUser(){ return this; }
	
	public String toString( String preBlock ){
		return  preBlock + "Enroll Request : \n" +
				preBlock + "    Name    : " + getName() + "\n" +
				preBlock + "    Req For : \n" + 
				( committeeReqId != -1 ? preBlock + "        Committee No." + committeeReqId + "\n" : "" ) +
				( industryReqId  != -1 ? preBlock + "        Industry  No." + industryReqId  + "\n" : "" ) +
				( seminarReqId   != -1 ? preBlock + "        Seminar   No." + seminarReqId   + "\n" : "" );
	}
	
	public String toString(){
		return toString("");
	}

}
