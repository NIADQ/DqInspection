package application.wisefile.controller;


import application.wisefile.Main;
import application.wisefile.common.Const;
import application.wisefile.service.DataFileService;
import application.wisefile.service.FileServiceFactory;
import application.wisefile.vo.DiagnosisFileListVO;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.apache.log4j.Logger;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

public class DiagnosisDataSetSelectionController implements Initializable {

	private Logger logger = Logger.getLogger(this.getClass());


	@FXML
	private TableView<DiagnosisFileListVO> tvFileList;

	@FXML
	private TableView<DiagnosisFileListVO> tvFileList2;

	@FXML
	private TableColumn<DiagnosisFileListVO, CheckBox> fileSelect;

	@FXML
	private TableColumn<DiagnosisFileListVO, CheckBox> fileSelect2;

	@FXML
	private TableColumn<DiagnosisFileListVO, String> fileId;

	@FXML
	private TableColumn<DiagnosisFileListVO, String> fileId2;

	@FXML
	private TableColumn<DiagnosisFileListVO, String> fileNm;

	@FXML
	private TableColumn<DiagnosisFileListVO, String> fileNm2;

	@FXML
	private TableColumn<DiagnosisFileListVO, String> seq;

	@FXML
	private TableColumn<DiagnosisFileListVO, String> seq2;

	@FXML
	private TableColumn<DiagnosisFileListVO, String> offerOrganName;

	@FXML
	private TableColumn<DiagnosisFileListVO, String> offerOrganName2;

	@FXML
	private Text appVersion;



	ObservableList<DiagnosisFileListVO> fileList = FXCollections.observableArrayList();

	ObservableList<DiagnosisFileListVO> fileList2 = FXCollections.observableArrayList();

	
	private Stage window = Const.stage;


	private ProgressBar pBar1_2 = new ProgressBar(0);


	private FileServiceFactory fileServiceFactory;
	
	private Alert getAlertOpen(String tit, String cnts, AlertType at) {
		Alert alert = new Alert(at);
		alert.setHeaderText(tit);
		alert.setContentText(cnts);
		return alert;
	}

	
	@FXML
	private void btnSelectNextClick(ActionEvent event) {
		List<String> checkedList = new ArrayList<String>();

		for(DiagnosisFileListVO model : fileList) {
			if(model.getFileSelect().isSelected()) {
				checkedList.add(model.getFileId() + ":" + model.getFileNm());
			}
		}
		for(DiagnosisFileListVO model : fileList2) {
			if(model.getFileSelect().isSelected()) {
				checkedList.add(model.getFileId() + ":" + model.getFileNm());
			}
		}

		if(checkedList.size() > 0) {
			sendData(String.join("|", checkedList), event);
		} else {
			getAlertOpen("??????", "????????? ????????? ????????????!", AlertType.INFORMATION).showAndWait();
		}
	}

	private void sendData(String data, ActionEvent event)  {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/fxml/DiagnosisExecute.fxml"));

		try {
			Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			Scene scene1 = new Scene(loader.load());
			stage.setScene(scene1);
			stage.setTitle("?????? ??????");
			DiagnosisExecuteController cont = loader.getController();
			cont.initData(data);
			stage.show();
		} catch (Exception e) {
			logger.error("", e);
			getAlertOpen("??????", "??????????????? ????????? ?????????????????????.", AlertType.INFORMATION).showAndWait();
		}
	}

	@FXML
	private void btnHistoryViewClick(ActionEvent event) {
		try{
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/DiagnosisHistory.fxml"));
			Parent root = (Parent) loader.load();

			Const.popStage.setTitle("????????????");
			Const.popStage.setScene(new Scene(root));
			Const.popStage.show();
		}catch(Exception e) {
			logger.error("", e);
			getAlertOpen("??????", "???????????? ??? ????????? ?????????????????????.", AlertType.INFORMATION).showAndWait();
		}
	}


	@FXML
	private void btnSyncDataSetClick(ActionEvent event) {
		try {

			Task task = taskCreator1_2();
			pBar1_2.progressProperty().unbind();
			pBar1_2.progressProperty().bind(task.progressProperty());
			createProgressDialog("");

			new Thread(task).start();

		} catch (Exception e) {
			logger.error("btnSyncDataSetClick", e);
			getAlertOpen("??????", "???????????? ???????????? ????????? ?????????????????????.", AlertType.INFORMATION).showAndWait();
		}
	}


	@FXML
	private void btnOrgNameSetClick(ActionEvent event) {
		try{
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/OrganNameSetting.fxml"));
			Parent root = (Parent) loader.load();

			Stage dialog = new Stage();
			dialog.getIcons().add(new Image(Objects.requireNonNull(Main.class.getResourceAsStream("/image/icon.jpg"))));
			dialog.initModality(Modality.WINDOW_MODAL);
			dialog.initOwner(window);
			dialog.setTitle("?????? ?????? ??????");

			dialog.setScene(new Scene(root));
			dialog.show();

		}catch(Exception e) {
			logger.error("", e);
			getAlertOpen("??????", "?????? ?????? ?????? ??? ????????? ?????????????????????.", AlertType.INFORMATION).showAndWait();
		}
	}
    

	@Override
	public void initialize(URL location, ResourceBundle resource) {

		fileServiceFactory = new FileServiceFactory();

		int count = 0;
		List<Map> list = Main.diagnosisFileDAO.selectFileList(new HashMap());
		int halfSize = list.size() / 2;

		for(Map map : list) {
			count++;
			CheckBox chk = new CheckBox();
			if(count <= halfSize) {
				fileList.add(new DiagnosisFileListVO(chk, (String) map.get("fileId"), (String) map.get("fileNm"), String.valueOf(map.get("seq")), (String) map.get("offerOrganName")));
			} else {
				fileList2.add(new DiagnosisFileListVO(chk, (String) map.get("fileId"), (String) map.get("fileNm"), String.valueOf(map.get("seq")), (String) map.get("offerOrganName")));
			}
		}

		appVersion.setText("VER:" + Const.APP_VERSION);
		appVersion.setFont(Font.font("family"));


		fileNm.setStyle("-fx-alignment: baseline-left");
		fileNm.setCellValueFactory(new PropertyValueFactory<>("fileNm"));
		fileId.setCellValueFactory(new PropertyValueFactory<>("fileId"));
		seq.setCellValueFactory(new PropertyValueFactory<>("seq"));
		offerOrganName.setStyle("-fx-alignment: baseline-left");
		offerOrganName.setCellValueFactory(new PropertyValueFactory<>("offerOrganName"));
		fileSelect.setCellValueFactory(new PropertyValueFactory<>("fileSelect"));
		tvFileList.setItems(fileList);

		fileNm2.setStyle("-fx-alignment: baseline-left");
		fileNm2.setCellValueFactory(new PropertyValueFactory<>("fileNm"));
		fileId2.setCellValueFactory(new PropertyValueFactory<>("fileId"));
		seq2.setCellValueFactory(new PropertyValueFactory<>("seq"));
		offerOrganName2.setStyle("-fx-alignment: baseline-left");
		offerOrganName2.setCellValueFactory(new PropertyValueFactory<>("offerOrganName"));
		fileSelect2.setCellValueFactory(new PropertyValueFactory<>("fileSelect"));
		tvFileList2.setItems(fileList2);

	}


	public void organNameCheck() {
		Map resultMap = Main.diagnosisFileDAO.selectOrganName();

		Const.FIXED_FILE_PATH = (String) resultMap.get("fixedFilePath");

		if(resultMap == null || resultMap.get("organName") == null || "".equals(((String)resultMap.get("organName")).trim()) ) {
			btnOrgNameSetClick(null);
		}
	}


	private Task taskCreator1_2() {
		return new Task() {
			@Override
			protected Object call() throws Exception {
				logger.info("call Start:"+new SimpleDateFormat("HH:mm:ss").format(new Date()));
				try {
					DataFileService fileService = fileServiceFactory.getFileService("excel");
					fileService.syncRuleSet();
				} catch (Exception e) {
					updateProgress(1, 1);
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							try {
								popCSVfileErrorDisplay(e);
							} catch (Exception e) {
								logger.error("", e);
							}
						}
					});
				}
				updateProgress(1, 1);
				return true;
			}
		};
	}

	private void popCSVfileErrorDisplay(Exception e) throws Exception {
		logger.error("", e);
		logger.info("popCSVfileErrorDisplay --?????? ?????? ");

		Stage dialog = new Stage();
		dialog.initModality(Modality.WINDOW_MODAL);
		dialog.initOwner(window);
		dialog.setTitle("??????");

		VBox vBox = (VBox) FXMLLoader.load(Class.forName("application.wisefile.Main").getResource("/fxml/CSVFileErrorDialog.fxml"));
		((Label) vBox.lookup("#lblTit")).setText("??? ?????? ??????");
		((Label) vBox.lookup("#lblInfo")).setText(e.getMessage());

		Button btnCancel = (Button) vBox.lookup("#btnCancel");
		btnCancel.setId("cancelBtn");
		btnCancel.setOnAction(event -> {
			dialog.close();
		});
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				btnCancel.requestFocus();
			}
		});

		Scene scene = new Scene(vBox);
		scene.getStylesheets().add(Const.mainCss);
		dialog.setScene(scene);
		dialog.setResizable(false);
		dialog.show();
		dialog.setX(window.getX() + (window.getWidth() > dialog.getWidth() ? (window.getWidth() - dialog.getWidth()) / 2 : 50));
		dialog.setY(window.getY()+220);

	}


	private void createProgressDialog(String mngStr) throws Exception {
		System.out.println("!!!!!!!!!! createProgressDialog-StartDate:"+new SimpleDateFormat("HH:mm:ss").format(new Date()));
		Stage prgressDialog = new Stage(StageStyle.UNDECORATED);
		prgressDialog.initModality(Modality.WINDOW_MODAL);
		prgressDialog.initOwner(window);
		prgressDialog.setTitle("??????");

		VBox vBox = (VBox) FXMLLoader.load(Class.forName("application.wisefile.Main").getResource("/fxml/ProgressDialog.fxml"));

		((Label) vBox.lookup("#lblTit")).setFont(Font.font("NanumGothicExtraBold",20));
		((Label) vBox.lookup("#lblInfo")).setFont(Font.font("NanumGothicExtraBold",18));
		((Label) vBox.lookup("#lblTit")).setText("[?????? ??????]");
		((Label) vBox.lookup("#lblInfo")).setText("???????????? ???????????? ??????????????????....");


		Text txtstate1_2 = new Text();
		txtstate1_2.setFont(Font.font("NanumGothicExtraBold",20));
		txtstate1_2.setFill(Color.BLUE);
		pBar1_2.setPrefWidth(400);
		pBar1_2.indeterminateProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
				txtstate1_2.setText( "??????????????????.");
			}
		});
		pBar1_2.progressProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
				System.out.println("TBAR:"+t1.doubleValue());
				if (t1.doubleValue() == 1) {
					((Label) vBox.lookup("#lblInfo")).setText("???????????? ???????????? ??????????????????.");
					txtstate1_2.setText("??? ??? ??? ???");
					txtstate1_2.setFill(Color.GREEN);
					Timeline timeline = new Timeline(new KeyFrame(
							Duration.millis(1500), ae -> prgressDialog.close())
					);
					timeline.play();
				}
				else if (t1.doubleValue() == 2) {
					((Label) vBox.lookup("#lblInfo")).setText("???????????? ???????????? ??????????????????.");
					txtstate1_2.setText("??? ??? ??? ???");
					txtstate1_2.setFill(Color.GREEN);
					Timeline timeline = new Timeline(new KeyFrame(
							Duration.millis(1500), ae -> prgressDialog.close())
					);
					timeline.play();
				}
			}
		});

		HBox hbox1_2 = new HBox(15);
		hbox1_2.getChildren().addAll(pBar1_2, txtstate1_2);
		//hbox1_2.setPadding(new Insets(320,0,0,370));
		hbox1_2.setAlignment(Pos.CENTER);
		Group root1_2 = new Group();
		vBox.getChildren().addAll(hbox1_2);

		HBox hbxMidCnts = (HBox)vBox.lookup("#hbxMidCnts");

		Scene scene = new Scene(vBox);
		scene.getStylesheets().add(Const.mainCss);
		prgressDialog.setScene(scene);
		prgressDialog.setResizable(false);
		prgressDialog.show();
		prgressDialog.setX(window.getX() + (window.getWidth() > prgressDialog.getWidth() ? (window.getWidth() - prgressDialog.getWidth()) / 2 : 50));
		prgressDialog.setY(window.getY()+250);

	}

}
