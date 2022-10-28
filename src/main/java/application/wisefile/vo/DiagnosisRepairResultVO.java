package application.wisefile.vo;

import javafx.scene.control.CheckBox;

public class DiagnosisRepairResultVO {
	private String id;
	private String histId;
	private String fileId;
	private String colNm;
	private String dataSet;
	private String orgData;
	private String fixedData;
	private String errMsg;
	private String fixedYn;
	private String ruleSet;
	private CheckBox fileSelect;
	private String columnIndx;
	private Integer    rowNum;
	
	public DiagnosisRepairResultVO(CheckBox chk,Integer rowNum, String colNm, String dataSet, String errMsg, String ruleSet, String orgData, String fixedData, String fixedYn, String id, String histId, String fileId, String columnIndx) {
		this.colNm     = colNm;
		this.dataSet   = dataSet;
		this.errMsg  = errMsg;
		this.ruleSet    = ruleSet;
		this.orgData    = orgData;
		this.fixedData = fixedData;
		this.fixedYn   = fixedYn;
		this.fileSelect = chk;
		this.id = id;
		this.histId = histId;
		this.fileId = fileId;
		this.columnIndx = columnIndx;
		this.rowNum = rowNum;
	}
	
	
	public String getColumnIndx() {
		return columnIndx;
	}


	public void setColumnIndx(String columnIndx) {
		this.columnIndx = columnIndx;
	}


	public String getOrgData() {
		return orgData;
	}
	public void setOrgData(String orgData) {
		this.orgData = orgData;
	}
	
	public String getRuleSet() {
		return ruleSet;
	}

	public void setRuleSet(String ruleSet) {
		this.ruleSet = ruleSet;
	}


	public String getColNm() {
		return colNm;
	}


	public void setColNm(String colNm) {
		this.colNm = colNm;
	}


	public String getDataSet() {
		return dataSet;
	}


	public void setDataSet(String dataSet) {
		this.dataSet = dataSet;
	}


	public String getFixedData() {
		return fixedData;
	}


	public void setFixedData(String fixedData) {
		this.fixedData = fixedData;
	}



	public String getErrMsg() {
		return errMsg;
	}


	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}


	public String getFixedYn() {
		return fixedYn;
	}


	public void setFixedYn(String fixedYn) {
		this.fixedYn = fixedYn;
	}


	public CheckBox getFileSelect() {
		return fileSelect;
	}


	public void setFileSelect(CheckBox fileSelect) {
		this.fileSelect = fileSelect;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getHistId() {
		return histId;
	}


	public void setHistId(String histId) {
		this.histId = histId;
	}


	public String getFileId() {
		return fileId;
	}


	public void setFileId(String fileId) {
		this.fileId = fileId;
	}


	public Integer getRowNum() {
		return rowNum;
	}


	public void setRowNum(Integer rowNum) {
		this.rowNum = rowNum;
	}

	

}
