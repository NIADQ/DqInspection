package application.wisefile.chk;

import java.util.ArrayList;
import java.util.List;

public class ItemVO {
	
	private int seq = 0;
	private String key = "";
	private String key2 = "";
	private String val = "";
	private String node = "";
	private String[] nodeRow = null;
	private String opt1 = "";
	private String opt2 = "";
	private String opt3 = "";
	private String opt4 = "";
	private String diagType = "";
	private String diagRule = "";
	private List<String> optList = new ArrayList<String>();
	
	public String getKey() {
		return key;
	}
	public String[] getNodeRow() {
		return nodeRow;
	}
	public void setNodeRow(String[] nodeRow) {
		this.nodeRow = nodeRow;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getVal() {
		return val;
	}
	public void setVal(String val) {
		this.val = val;
	}
	public String getKey2() {
		return key2;
	}
	public void setKey2(String key2) {
		this.key2 = key2;
	}
	public int getSeq() {
		return seq;
	}
	public void setSeq(int seq) {
		this.seq = seq;
	}
	public String getOpt1() {
		return opt1;
	}
	public void setOpt1(String opt1) {
		this.opt1 = opt1;
	}
	public String getOpt2() {
		return opt2;
	}
	public void setOpt2(String opt2) {
		this.opt2 = opt2;
	}
	public String getOpt3() {
		return opt3;
	}
	public void setOpt3(String opt3) {
		this.opt3 = opt3;
	}
	public String getOpt4() {
		return opt4;
	}
	public void setOpt4(String opt4) {
		this.opt4 = opt4;
	}
	public List<String> getOptList() {
		return optList;
	}
	public void setOptList(List<String> optList) {
		this.optList = optList;
	}
	public String getNode() {
		return node;
	}
	public void setNode(String node) {
		this.node = node;
	}
	
	public String getDiagType() {
		return diagType;
	}
	public void setDiagType(String diagType) {
		this.diagType = diagType;
	}
	public String getDiagRule() {
		return diagRule;
	}
	public void setDiagRule(String diagRule) {
		this.diagRule = diagRule;
	}
	public String toString() {
		return getVal();
	}
	public String toStringAll() {
		return "{seq:"+seq+", key:"+key+", key2:"+key2+", val:"+val+", node:"+node+", nodeRow:"+(nodeRow == null ? null : nodeRow.toString())+", opt1:"+opt1+", opt2:"+opt2+", opt3:"+opt3+", opt4:"+opt4+", diagType:"+ diagType+",  diagRule:"+diagRule+", optList:"+optList.toString()+"}";
	}
	
}
