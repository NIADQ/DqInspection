package application.wisefile.vo;

import javafx.scene.control.CheckBox;

public class DiagnosisFileListVO {
	private CheckBox fileSelect;
	private String fileId;
	private String fileNm;

	private String seq;

	private String offerOrganName;
	
	
	public DiagnosisFileListVO(CheckBox chk, String fileId, String fileNm, String seq, String offerOrganName) {
		this.fileSelect = chk;
		this.fileId = fileId;
		this.fileNm = fileNm;
		this.seq = seq;
		this.offerOrganName = offerOrganName;
	}


	public CheckBox getFileSelect() {
		return fileSelect;
	}

	public void setFileSelect(CheckBox fileSelect) {
		this.fileSelect = fileSelect;
	}




	public String getFileId() {
		return fileId;
	}
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	public String getFileNm() {
		return fileNm;
	}
	public void setFileNm(String fileNm) {
		this.fileNm = fileNm;
	}

	public String getSeq() {
		return seq;
	}

	public void setSeq(String seq) {
		this.seq = seq;
	}

	public String getOfferOrganName() {
		return offerOrganName;
	}

	public void setOfferOrganName(String offerOrganName) {
		this.offerOrganName = offerOrganName;
	}
}
