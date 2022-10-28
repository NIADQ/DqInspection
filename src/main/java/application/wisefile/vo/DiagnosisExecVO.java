package application.wisefile.vo;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;


public class DiagnosisExecVO {
	private String dataSet;
	private String errYn;
	private String fileNm;
	private String filePath;

	private String fileType;
	private String proStat;
	private String fixedFile;
	private Long historyId;
	private String fileId;

	private Button buttonChoose;

	private CheckBox dupCheck;

	public DiagnosisExecVO(Button buttonChoose, String dataSet, String errYn, String fileNm, String fileType, String proStat, String fixedFile, String fileId, String filePath, Long historyId, CheckBox dupCheck) {
		this.dataSet   = dataSet;
		this.errYn     = errYn;
		this.fileNm    = fileNm;
		this.fileType  = fileType;
		this.proStat   = proStat;
		this.fixedFile = fixedFile;
		this.fileId = fileId;
		this.buttonChoose = buttonChoose;
		this.filePath = filePath;
		this.historyId = historyId;
		this.dupCheck = dupCheck;
	}

	public CheckBox getDupCheck() {
		return dupCheck;
	}

	public void setDupCheck(CheckBox dupCheck) {
		this.dupCheck = dupCheck;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
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

	public Long getHistoryId() {
		return historyId;
	}

	public void setHistoryId(Long historyId) {
		this.historyId = historyId;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public Button getButtonChoose() {
		return buttonChoose;
	}

	public void setButtonChoose(Button buttonChoose) {
		this.buttonChoose = buttonChoose;
	}
}
