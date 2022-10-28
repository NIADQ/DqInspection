package application.wisefile.vo;

import javafx.scene.control.Button;


public class DiagnosisHistoryListVO {
	private Button buttonDel;
	private String historyId;
	private String createdAt;



	public DiagnosisHistoryListVO(Button buttonDel, String historyId, String createdAt) {
		this.buttonDel = buttonDel;
		this.historyId = historyId;
		this.createdAt = createdAt;
	}


	public Button getButtonDel() {
		return buttonDel;
	}

	public void setButtonDel(Button buttonDel) {
		this.buttonDel = buttonDel;
	}

	public String getHistoryId() {
		return historyId;
	}

	public void setHistoryId(String historyId) {
		this.historyId = historyId;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
}
