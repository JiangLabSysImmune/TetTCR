

public class faUnit{
	private String faName;
	private String faSeq;
	private int groupNum; 
	public faUnit(String name, String seq) {
		this.faName = name;
		this.faSeq = seq;
	}
	
	public String getName() {
		return faName;
	}
	
	public String getSeq() {
		return faSeq;
	}
	
	public int getGroupNum() {
		return groupNum;
	}
}
