package application.wisefile.controller;

import application.wisefile.Main;
import application.wisefile.common.Const;
import application.wisefile.service.DiagnosisService;
import application.wisefile.vo.DiagnosisExecVO;
import application.wisefile.vo.ParamVO;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.apache.log4j.Logger;

import java.io.File;
import java.net.URL;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

public class DiagnosisExecuteController implements Initializable {
		protected  Logger logger = Logger.getLogger(this.getClass());
		private Stage stage;

		private Stage window = Const.stage;

		@FXML
		private TableView<DiagnosisExecVO> diagnosisExecuteTable;

	    @FXML
	    private TableColumn<DiagnosisExecVO, String> dataSet;

	    @FXML
	    private TableColumn<DiagnosisExecVO, String> errYn;
	    @FXML
	    private TableColumn<DiagnosisExecVO, String> fileNm;

	    @FXML
	    private TableColumn<DiagnosisExecVO, String> fileType;

	    @FXML
	    private TableColumn<DiagnosisExecVO, String> proStat;

	    @FXML
	    private TableColumn<DiagnosisExecVO, String> fixedFile;

	    @FXML
	    private TableColumn<DiagnosisExecVO, String> fileId;


	    @FXML
		private TableColumn<DiagnosisExecVO, Button> buttonChoose;

		@FXML
		private TableColumn<DiagnosisExecVO, CheckBox> dupCheck;

	    @FXML
	    private Button diagExecuteBtn;

	    @FXML
	    private Button resetBtn;


		public static void progress() {

		}

	    ObservableList<DiagnosisExecVO> diagExecuteList = FXCollections.observableArrayList();

		private ProgressBar pBar1_2 = new ProgressBar(0);


		private List<Map> list;

		private DiagnosisService diagnosisService;

		private Alert getAlertOpen(String tit, String cnts, Alert.AlertType at) {
			Alert alert = new Alert(at);
			alert.setHeaderText(tit);
			alert.setContentText(cnts);
			return alert;
		}


	    @FXML
	    void diagExecuteBtnClick(ActionEvent event) {
	    	logger.info(":::::::::::::::::::::::::: 진단 실행 ::::::::::::::::::::::::::");
	    	try {

				Task task = taskCreator1_2();
				pBar1_2.progressProperty().unbind();
				pBar1_2.progressProperty().bind(task.progressProperty());
				createProgressDialog("");

				new Thread(task).start();

			} catch (Exception e) {
				logger.error("diagExecuteBtnClick", e);
				getAlertOpen("알림", "진단실행시 오류가 발생했습니다.", Alert.AlertType.INFORMATION).showAndWait();
			}

	    }
	    
	    
	    @FXML
	    void goDataSetPage(ActionEvent event) {
	    	logger.info("초기화 버튼 클릭!!");
	    	try {
	    		Parent scene1_fxml = FXMLLoader.load(getClass().getResource("/fxml/DiagDataSetSelection.fxml"));
		    	stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		    	Scene scene1 = new Scene(scene1_fxml);
		        stage.setScene(scene1);
	    		stage.setTitle("표준데이터셋 진단 도구");
	    		stage.show();
				
			} catch (Exception e) {
				System.out.println("오류가 발생했습니다 : "+e.getMessage());
				logger.error("reader FileNotFoundException");
				e.printStackTrace();
			}
	    	
	    }
	    
	    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		logger.info("initialize");
		diagnosisService = Main.context.getBean(DiagnosisService.class);
		list = new ArrayList<>();
		
		diagnosisExecuteTable.setOnMouseClicked(new EventHandler <MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.getClickCount() == 2) {
		            Node node = ((Node) event.getTarget()).getParent();
		            TableRow row;
		            if (node instanceof TableRow) {
		                row = (TableRow) node;
		            } else {
		                row = (TableRow) node.getParent();
		            }
		            
		            DiagnosisExecVO model = (DiagnosisExecVO)row.getItem();
		            if(model == null) return;
		            
		            if(model.getProStat() != null) {
						if("진단완료".equals(model.getProStat())) {
							logger.info("model.getHistoryId():" + model.getHistoryId());
							try {
								FXMLLoader loader = new FXMLLoader();
								loader.setLocation(getClass().getResource("/fxml/DiagHistory.fxml"));
								Parent root = (Parent) loader.load();
								Scene scene = new Scene(root);
								DiagnosisHistoryPopupController pop = loader.getController();
								pop.initData(Long.toString(model.getHistoryId()));
		
								Const.diagHistoryStage.setTitle("진단이력상세");
								Const.diagHistoryStage.setScene(scene);
								Const.diagHistoryStage.show();
							} catch (Exception e) {
								logger.error("sendData", e);
								getAlertOpen("알림", "진단이력상세 페이지를 여는데 실패했습니다.", AlertType.INFORMATION).showAndWait();
							}
						}
					}
		        }
			}
		});
	}




	public void initData(String data) {
		logger.info("param:"+data);

		if(data.indexOf("|") > 0) {
			String arrStr[] = data.split("\\|");
			for(String str : arrStr) {
				String arrSub[] = str.split(":");
				Map<String,String> param = new HashMap<String,String>();
				param.put("fileId", arrSub[0]);
				param.put("dataSet", arrSub[1]);
				list.add(param);
				logger.info(param.toString());
			}
		} else {
			String arrSub[] = data.split(":");
			Map<String,String> param = new HashMap<String,String>();
			param.put("fileId", arrSub[0]);
			param.put("dataSet", arrSub[1]);
			list.add(param);
			logger.info(param.toString());
		}

		logger.info(":::::::::::::::::::::::::: 진단 실행 화면 시작 ::::::::::::::::::::::::::");
		dataSet.setCellValueFactory(new PropertyValueFactory<>("dataSet"));
		errYn.setCellValueFactory(new PropertyValueFactory<>("errYn"));
		fileNm.setCellValueFactory(new PropertyValueFactory<>("fileNm"));
		fileType.setCellValueFactory(new PropertyValueFactory<>("fileType"));
		proStat.setCellValueFactory(new PropertyValueFactory<>("proStat"));
		fixedFile.setCellValueFactory(new PropertyValueFactory<>("fixedFile"));
		dupCheck.setCellValueFactory(new PropertyValueFactory<>("dupCheck"));
		fileId.setCellValueFactory(new PropertyValueFactory<>("fileId"));
		buttonChoose.setCellValueFactory(new PropertyValueFactory<>("buttonChoose"));

		dataSet.setStyle("-fx-alignment: baseline-left");
		errYn.setStyle("-fx-alignment: baseline-center");
		fileNm.setStyle("-fx-alignment: baseline-left");
		fileType.setStyle("-fx-alignment: baseline-center");
		proStat.setStyle("-fx-alignment: baseline-center");
		fixedFile.setStyle("-fx-alignment: baseline-left");
		dupCheck.setStyle("-fx-alignment: baseline-center");
		buttonChoose.setStyle("-fx-alignment: baseline-center");

		for(Map map : list) {
			Button btn = new Button("...");
			btn.setOnAction(e -> fileChooseClick(String.valueOf(map.get("fileId")), (String)map.get("dataSet") ));
			CheckBox dupCheck = new CheckBox();
			dupCheck.setOnAction(e -> dupCheckClick(e, String.valueOf(map.get("fileId"))));
			boolean checkYn = map.get("dupCheck") == null ? false : (boolean)map.get("dupCheck");
			dupCheck.setSelected(checkYn);
			diagExecuteList.add(new DiagnosisExecVO(btn,(String) map.get("dataSet"),(String) map.get("errYn"),(String) map.get("fileNm"),(String) map.get("fileType")
					,(String) map.get("proStat"),(String) map.get("fixedFile"),(String) map.get("fileId"),(String) map.get("filePath"), (Long)map.get("historyId"), dupCheck));
		}
		diagnosisExecuteTable.setItems(diagExecuteList);
	}

	private void dupCheckClick(ActionEvent e, String fileId) {
		Node node = ((Node) e.getTarget()).getParent();
		TableRow row;
		if (node instanceof TableRow) {
			row = (TableRow) node;
		} else {
			row = (TableRow) node.getParent();
		}
		DiagnosisExecVO model = (DiagnosisExecVO)row.getItem();
		if(model == null) return;

		for(Map map : list) {
			if(map.get("fileId").equals(fileId)) {
				map.put("dupCheck", model.getDupCheck().isSelected());
				break;
			}
		}
	}

	private void fileChooseClick(String fileId, String dataName) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("EXCEL Files", "*.xlsx"));
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("EXCEL Files", "*.xls"));

		File openFile;


		try {
			try {
				if (Const.defInitialDirectory != null) {
					if (Const.defInitialDirectory.indexOf("/") > 0) {
						fileChooser.setInitialDirectory(new File(Const.defInitialDirectory.substring(0, Const.defInitialDirectory.lastIndexOf("/"))));
					} else if (Const.defInitialDirectory.indexOf("\\") > 0) {
						fileChooser.setInitialDirectory(new File(Const.defInitialDirectory.substring(0, Const.defInitialDirectory.lastIndexOf("\\"))));
					}
				}
			} catch(Exception fe) {
				fe.printStackTrace();
			}

			openFile = fileChooser.showOpenDialog(stage);

			if (openFile != null) {
				if (openFile.length() >= (1024L * 1024L)*50) {
					getAlertOpen("진단파일 용량 확인", "진단대상 파일의 적정크기는 작업자 PC메모리 및 성능 에 따라 차이는 있겠으나  50메가 이하를 권장합니다.", Alert.AlertType.WARNING).showAndWait();
					return;
				}

				String openFileNm = openFile.getName();
				String openFilePath = openFile.getPath();
				Const.defInitialDirectory = openFile.getPath();
				logger.info("openFileNm="+openFileNm);
				logger.info("openFilePath="+openFilePath);

				String filenameHead = "";
				if(openFileNm.lastIndexOf(".")>-1) {
					filenameHead = openFileNm.substring(0, openFileNm.lastIndexOf("."));
				}

				if(filenameHead.lastIndexOf("_")>-1) {
					String[] arr = filenameHead.split("_");
					if (arr.length < 3) {
						getAlertOpen("점검파일 형식 확인", "점검파일 형식이 일치하지 않습니다. \n파일형식 예: 서울특별시_중구_주차장정보_20140501.csv", Alert.AlertType.WARNING).showAndWait();
						return;
					}

					String fileNm = Normalizer.normalize(arr[arr.length-2], Normalizer.Form.NFC);
					if(!fileNm.equals(dataName)) {
						getAlertOpen("표준데이터셋 명 확인", "표준데이터셋 명이 일치하지 않습니다.", Alert.AlertType.WARNING).showAndWait();
						return;
					}
					String date = arr[arr.length-1];
					String pattern = "^(19|20)\\d{2}(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[0-1])$";
					boolean result = Pattern.matches(pattern, date);
					if(!result) {
						getAlertOpen("점검파일 날짜 형식 확인", "점검파일 날짜 형식이 일치하지 않습니다.\n파일형식 예: 서울특별시_중구_주차장정보_20140501.csv", Alert.AlertType.WARNING).showAndWait();
						return;
					}
				} else {
					getAlertOpen("점검파일 형식 확인", "점검파일 형식이 일치하지 않습니다. \n파일형식 예: 서울특별시_중구_주차장정보_20140501.csv", Alert.AlertType.WARNING).showAndWait();
					return;
				}

				diagExecuteList.clear();
				String ext = "";
				for(Map map : list) {
					Button btn = new Button("...");
					btn.setOnAction(e -> fileChooseClick(String.valueOf(map.get("fileId")), (String)map.get("dataSet") ));
					CheckBox dupCheck = new CheckBox();
					dupCheck.setOnAction(e -> dupCheckClick(e, String.valueOf(map.get("fileId"))));
					boolean checkYn = map.get("dupCheck") == null ? false : (boolean)map.get("dupCheck");
					dupCheck.setSelected(checkYn);
					if(map.get("fileId").equals(fileId)) {
						map.put("fileNm", openFileNm);
						ext = openFileNm.substring(openFileNm.lastIndexOf(".") + 1);
						map.put("fileType", ext);
						map.put("proStat", "진단대기");
						map.put("filePath", openFilePath);
					}
					diagExecuteList.add(new DiagnosisExecVO(btn,(String) map.get("dataSet"),(String) map.get("errYn"),(String) map.get("fileNm"),(String) map.get("fileType")
							,(String) map.get("proStat"),(String) map.get("fixedFile"),(String) map.get("fileId"), (String) map.get("filePath"), (Long)map.get("historyId"), dupCheck));
				}
				diagnosisExecuteTable.setItems(diagExecuteList);
			}

		}catch(Exception eev) {
			logger.error("EXCEPTION THROW------------------", eev);
		}
	}


	private Task taskCreator1_2() {
		return new Task() {
			@Override
			protected Object call() throws Exception {
				logger.info("!!!!!!!!!! Star:"+new SimpleDateFormat("HH:mm:ss").format(new Date()));

				String filePath = "";
				String fileId = "";
				String fileName = "";
				String filePathDir = "";
				String proStat = "";
				String dataSet = "";
				int cnt = 0;
				try {
					for (Map map : list) {
						filePath = (String) map.get("filePath");
						fileId = (String) map.get("fileId");
						proStat = (String) map.get("proStat");
						dataSet = (String) map.get("dataSet");
						if (filePath != null && proStat != null && !"진단완료".equals(proStat) && !"진단실패".equals(proStat)) {
							cnt++;
							fileName = filePath.substring(filePath.lastIndexOf("\\") + 1);
							filePathDir = filePath.substring(0, filePath.lastIndexOf("\\") + 1);

							diagExecuteList.clear();
							for(Map submap : list) {
								Button btn = new Button("...");
								btn.setOnAction(e -> fileChooseClick(String.valueOf(submap.get("fileId")), (String)submap.get("dataSet") ));
								CheckBox dupCheck = new CheckBox();
								dupCheck.setOnAction(e -> dupCheckClick(e, String.valueOf(submap.get("fileId"))));
								boolean checkYn = submap.get("dupCheck") == null ? false : (boolean)submap.get("dupCheck");
								dupCheck.setSelected(checkYn);

								if(submap.get("fileId").equals(fileId)) {
									submap.put("proStat", "진단중...");
								}
								diagExecuteList.add(new DiagnosisExecVO(btn,(String) submap.get("dataSet"),(String) submap.get("errYn"),(String) submap.get("fileNm"),(String) submap.get("fileType")
										,(String) submap.get("proStat"),(String) submap.get("fixedFile"),(String) submap.get("fileId"), (String) submap.get("filePath"), (Long)submap.get("historyId"), dupCheck));
							}
							diagnosisExecuteTable.setItems(diagExecuteList);

							Long nextHistoryId = Main.getNextHistoryId();
							Map param = new HashMap();
							param.put("historyId", nextHistoryId +"");
							param.put("fileId", fileId);
							param.put("fileType", map.get("fileType"));
							param.put("filePath", filePathDir);
							param.put("fileName", fileName);
							param.put("status", "진단중");
							param.put("errorYn", "");
							param.put("fixedFile", "");
							Main.diagnosisFileDAO.insertFileHistory(param);
							Main.diagnosisFileDAO.insertHistory(param);

							Map tempMap = Main.diagnosisFileDAO.selectOrganName();
							boolean checkYn = map.get("dupCheck") == null ? false : (boolean)map.get("dupCheck");
							logger.debug("dataSet:" + dataSet + ", checkYn" + checkYn);
							ParamVO paramVO = ParamVO.builder()
									.fileDesc(dataSet)
									.fileName(filePath)
									.historyId(nextHistoryId)
									.fileId(fileId)
									.organName(tempMap.get("organName").toString())
									.dupChkYn(checkYn)
									.fileType(map.get("fileType") + "")
									.build();

							Map resultMap = diagnosisService.diagnosis(paramVO);

							diagExecuteList.clear();
							for(Map submap : list) {
								Button btn = new Button("...");
								btn.setOnAction(e -> fileChooseClick(String.valueOf(submap.get("fileId")), (String)submap.get("dataSet") ));
								CheckBox dupCheck = new CheckBox();
								dupCheck.setOnAction(e -> dupCheckClick(e, String.valueOf(submap.get("fileId"))));
								boolean subCheckYn = submap.get("dupCheck") == null ? false : (boolean)submap.get("dupCheck");
								dupCheck.setSelected(subCheckYn);
								if(submap.get("fileId").equals(fileId)) {
									submap.put("proStat", "진단완료");
									submap.put("historyId", nextHistoryId);
									if("Y".equals(resultMap.get("isError"))) {
										submap.put("errYn", "Y");
										submap.put("fixedFile", resultMap.get("fixedFileName"));
									}
								}
								diagExecuteList.add(new DiagnosisExecVO(btn,(String) submap.get("dataSet"),(String) submap.get("errYn"),(String) submap.get("fileNm"),(String) submap.get("fileType")
										,(String) submap.get("proStat"),(String) submap.get("fixedFile"),(String) submap.get("fileId"), (String) submap.get("filePath"), (Long)submap.get("historyId"), dupCheck));
							}
							diagnosisExecuteTable.setItems(diagExecuteList);

						}
					}

					if(cnt == 0) {
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								try {
									popCSVfileErrorDisplay(new Exception("진단할 대상이 없습니다."));
								} catch (Exception e) {
									logger.error("", e);
								}
							}
						});
					}
				} catch (Exception e) {
 					updateProgress(1, 1);

					diagExecuteList.clear();
					for(Map map : list) {
						Button btn = new Button("...");
						btn.setOnAction(ee -> fileChooseClick(String.valueOf(map.get("fileId")), (String)map.get("dataSet") ));
						CheckBox dupCheck = new CheckBox();
						dupCheck.setOnAction(ee -> dupCheckClick(ee, String.valueOf(map.get("fileId"))));
						boolean checkYn = map.get("dupCheck") == null ? false : (boolean)map.get("dupCheck");
						dupCheck.setSelected(checkYn);
						if(map.get("fileId").equals(fileId)) {
							map.put("proStat", "진단실패");
						}
						diagExecuteList.add(new DiagnosisExecVO(btn,(String) map.get("dataSet"),(String) map.get("errYn"),(String) map.get("fileNm"),(String) map.get("fileType")
								,(String) map.get("proStat"),(String) map.get("fixedFile"),(String) map.get("fileId"), (String) map.get("filePath"), (Long) map.get("historyId"), dupCheck));
					}
					diagnosisExecuteTable.setItems(diagExecuteList);

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
		logger.info("popCSVfileErrorDisplay --조회 시작 ");

		Stage dialog = new Stage(StageStyle.UNDECORATED);
		dialog.initModality(Modality.WINDOW_MODAL);
		dialog.initOwner(window);
		dialog.setTitle("확인");

		VBox vBox = (VBox) FXMLLoader.load(Class.forName("application.wisefile.Main").getResource("/fxml/CSVFileErrorDialog.fxml"));

		((Label) vBox.lookup("#lblTit")).setText("※ 오류 내역");
		((Label) vBox.lookup("#lblInfo")).setText(e.getMessage());

		HBox hbxMidCnts = (HBox)vBox.lookup("#hbxMidCnts");

		Label lblFlag_CompTxt = (Label) vBox.lookup("#lblCompTxt");

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
		prgressDialog.setTitle("확인");

		VBox vBox = (VBox) FXMLLoader.load(Class.forName("application.wisefile.Main").getResource("/fxml/ProgressDialog.fxml"));

		((Label) vBox.lookup("#lblTit")).setFont(Font.font("NanumGothicExtraBold",20));
		((Label) vBox.lookup("#lblInfo")).setFont(Font.font("NanumGothicExtraBold",18));
		((Label) vBox.lookup("#lblTit")).setText("[로딩진행 안내]");
		((Label) vBox.lookup("#lblInfo")).setText("선택한 파일의 데이터를 로딩 및 진단중입니다....");


		Text txtstate1_2 = new Text();
		txtstate1_2.setFont(Font.font("NanumGothicExtraBold",20));
		txtstate1_2.setFill(Color.BLUE);
		pBar1_2.setPrefWidth(400);
		//ProgressBar pBar1_2 = new ProgressBar(0);
		pBar1_2.indeterminateProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
				txtstate1_2.setText( "처리중입니다.");
			}
		});
		pBar1_2.progressProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
				System.out.println("TBAR:"+t1.doubleValue());
				if (t1.doubleValue() == 1) {
					((Label) vBox.lookup("#lblInfo")).setText("1. 선택한 파일의 데이터를 로딩 및 진단 완료됐습니다.");
					txtstate1_2.setText("진 단 완 료");
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
