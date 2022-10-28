package application.wisefile.controller;


import application.wisefile.Main;
import application.wisefile.common.Const;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class OrganNameSettionController implements Initializable {

	private Logger logger = Logger.getLogger(this.getClass());

	private Stage window = Const.stage;

	@FXML
	private TextField organName;

	@FXML
	private TextField txtFilePath;

	@FXML
	private Button filePathBtn;

	private Alert getAlertOpen(String tit, String cnts, AlertType at) {
		Alert alert = new Alert(at);
		alert.setHeaderText(tit);
		alert.setContentText(cnts);
		return alert;
	}

	@FXML
	void filePathBtnClick(ActionEvent event) throws IOException {

		Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

		DirectoryChooser directoryChooser = new DirectoryChooser();
		File file = directoryChooser.showDialog(stage);
		String path = "";
		if (file == null) {
			getAlertOpen("알림", "경로를 선택해 주세요.", AlertType.INFORMATION).showAndWait();
			return;
		} else {
			path = file.getPath() + "\\";
			txtFilePath.setText(path);
		}
	}

		@FXML
	private void btnUpdateOrganNameClick(ActionEvent event) {
		if("".equals(organName.getText().trim())) {
			getAlertOpen("알림", "기관명을 입력해 주세요.", AlertType.INFORMATION).showAndWait();
			return;
		}

		Map param = new HashMap();
		param.put("organName", organName.getText());
		param.put("fixedFilePath", txtFilePath.getText());

		Const.FIXED_FILE_PATH = txtFilePath.getText();

		if(Main.diagnosisFileDAO.updateOrganName(param) >= 1) {
			getAlertOpen("알림", "저장 되었습니다.", AlertType.INFORMATION).showAndWait();
		}
		((Stage)((Node)event.getSource()).getScene().getWindow()).close();
	}

	@Override
	public void initialize(URL location, ResourceBundle resource) {
		Map resultMap = Main.diagnosisFileDAO.selectOrganName();
		if(resultMap != null && resultMap.get("organName") != null) {
			organName.setText(resultMap.get("organName") + "");
			Const.FIXED_FILE_PATH = resultMap.get("fixedFilePath") + "";
		}
		txtFilePath.setText(Const.FIXED_FILE_PATH);
	}



}
