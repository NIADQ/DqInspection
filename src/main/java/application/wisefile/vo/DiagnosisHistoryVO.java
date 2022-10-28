package application.wisefile.vo;

import javafx.scene.control.CheckBox;

public class DiagnosisHistoryVO {
	private String dataSet;
	private String errYn;
	private String fileNm;
	private String fileType;
	private String proStat;
	private String fixedFile;
	private String historyId;
	private CheckBox fileSelect;
	
	public DiagnosisHistoryVO(CheckBox chk, String dataSet, String errYn, String fileNm, String fileType, String proStat, String fixedFile, String historyId) {
		this.dataSet   = dataSet;
		this.errYn     = errYn;
		this.fileNm    = fileNm;
		this.fileType  = fileType;
		this.proStat   = proStat;
		this.fixedFile = fixedFile;
		this.fileSelect = chk;
		this.historyId = historyId;
	}

	public String getDataSet() {
		return dataSet;
	}

	public void setDataSet(String dataSet) {
		this.dataSet = dataSet;
	}

	public String getErrYn() {
		return errYn;
	}

	public void setErrYn(String errYn) {
		this.errYn = errYn;
	}

	public String getFileNm() {
		return fileNm;
	}

	public void setFileNm(String fileNm) {
		this.fileNm = fileNm;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getProStat() {
		return proStat;
	}

	public void setProStat(String proStat) {
		this.proStat = proStat;
	}

	public String getFixedFile() {
		return fixedFile;
	}

	public void setFixedFile(String fixedFile) {
		this.fixedFile = fixedFile;
	}


	public CheckBox getFileSelect() {
		return fileSelect;
	}

	public void setFileSelect(CheckBox fileSelect) {
		this.fileSelect = fileSelect;
	}

	public String getHistoryId() {
		return historyId;
	}

	public void setHistoryId(String historyId) {
		this.historyId = historyId;
	}

	
	
}
