package application.wisefile.controller;


import application.wisefile.Main;
import application.wisefile.common.Const;
import application.wisefile.vo.DiagnosisHistoryListVO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import java.net.URL;
import java.util.*;

public class DiagnosisHistoryController implements Initializable {

	private Logger logger = Logger.getLogger(this.getClass());


	@FXML
	private TableView<DiagnosisHistoryListVO> tvFileList;

	@FXML
	private TableColumn<DiagnosisHistoryListVO, Button> buttonDel;


	@FXML
	private TableColumn<DiagnosisHistoryListVO, String> historyId;

	@FXML
	private TableColumn<DiagnosisHistoryListVO, String> createdAt;


	ObservableList<DiagnosisHistoryListVO> fileList = null;

	

	List<Map> historylist = null;
	
	private Alert getAlertOpen(String tit, String cnts, AlertType at) {
		Alert alert = new Alert(at);
		alert.setHeaderText(tit);
		alert.setContentText(cnts);
		return alert;
	}

	@FXML
	private void btnCloseClick(ActionEvent event) {
		Const.popStage.close();
	}


	@Override
	public void initialize(URL location, ResourceBundle resource) {
		selectHistoryList();
	}

	private void selectHistoryList() {
		int count = 0;
		fileList = FXCollections.observableArrayList();
		try {
			historylist = Main.diagnosisFileDAO.selectHistoryList(new HashMap());
		} catch (Exception e) {
			getAlertOpen("DB연동실패", "DB연결에 실패했습니다.", AlertType.INFORMATION).showAndWait();
			return;
		}
		for(Map map : historylist) {
			count++;
			Button btn = new Button("삭제");
			btn.setOnAction(e -> deleteHistory(String.valueOf(map.get("historyId"))));
			fileList.add(new DiagnosisHistoryListVO(btn, String.valueOf(map.get("historyId")), String.valueOf(map.get("createdAt"))));
		}

		historyId.setCellValueFactory(new PropertyValueFactory<>("historyId"));
		createdAt.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
		buttonDel.setCellValueFactory(new PropertyValueFactory<>("buttonDel"));
		tvFileList.setItems(fileList);

		tvFileList.setOnMouseClicked((MouseEvent) -> {
			Object ob = tvFileList.getSelectionModel().getSelectedItem();
			if(ob == null) return;
			DiagnosisHistoryListVO model = (DiagnosisHistoryListVO)ob;
			logger.info("model.getHistoryId():" + model.getHistoryId());
			sendData(model.getHistoryId());
		});
	}


	private void sendData(String data) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/fxml/DiagHistory.fxml"));
			Parent root = (Parent) loader.load();
			Scene scene = new Scene(root);
			DiagnosisHistoryPopupController pop = loader.getController();
			pop.initData(data);

			Const.diagHistoryStage.setTitle("진단이력상세");
			Const.diagHistoryStage.setScene(scene);
			Const.diagHistoryStage.show();
		} catch (Exception e) {
			logger.error("sendData", e);
			getAlertOpen("알림", "진단이력상세 페이지를 여는데 실패했습니다.", AlertType.INFORMATION).showAndWait();
		}
	}

	private void deleteHistory(String historyId) {
		try {
			Map param = new HashMap();
			param.put("historyId", historyId);
			Main.diagnosisFileDAO.deleteHistory(param);
			Main.diagnosisFileDAO.deleteDiagHistory(param);
			Main.diagnosisFileDAO.deleteDiagResult(param);
			Main.diagnosisFileDAO.deleteFileHistory(param);
			Main.diagnosisFileDAO.deleteFixedHistory(param);
		} catch (Exception e) {
			getAlertOpen("DB연동실패", "DB연결에 실패했습니다.", AlertType.INFORMATION).showAndWait();
			return;
		}
		selectHistoryList();
	}


}
