package application.wisefile.controller;

import application.wisefile.Main;
import application.wisefile.dao.DiagnosisHistoryDAO;
import application.wisefile.vo.DiagnosisHistoryVO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class DiagnosisHistoryPopupController implements Initializable{
		protected  Logger logger = Logger.getLogger(this.getClass());
		private Stage stage;
		public static DiagnosisHistoryDAO diagnosisHistoryDAO;
		@FXML
		private TableView<DiagnosisHistoryVO> diagHistoryTable;
		  
	    @FXML
	    private TableColumn<DiagnosisHistoryVO, String> dataSet;

	    @FXML
	    private TableColumn<DiagnosisHistoryVO, String> errYn;
	    @FXML
	    private TableColumn<DiagnosisHistoryVO, String> fileNm;

	    @FXML
	    private TableColumn<DiagnosisHistoryVO, String> fileType;
	    
	    @FXML
	    private TableColumn<DiagnosisHistoryVO, String> proStat;

	    @FXML
	    private TableColumn<DiagnosisHistoryVO, String> fixedFile;
	    
	    @FXML
	    private TableColumn<DiagnosisHistoryVO, String> historyId;
	    
	    @FXML
	    private TableColumn<DiagnosisHistoryVO, CheckBox> fileSelect;

	    @FXML
	    private Button diagResultBtn;

	    @FXML
	    private Button repairResultBtn;
	    
	    
	    ObservableList<DiagnosisHistoryVO> diagHistoryList = FXCollections.observableArrayList();
	    
	    public DiagnosisHistoryPopupController() {
	    	String[] configLocations = new String[] { "applicationContext-standalone.xml" };
			Main.context = new ClassPathXmlApplicationContext(configLocations);
			diagnosisHistoryDAO = Main.context.getBean(DiagnosisHistoryDAO.class);
	    }
	    /*???????????? ?????? ?????????*/
	    @FXML
	    void goDiagResultPage(ActionEvent event) throws IOException{
	    	logger.info(":::::::::::::::::::::::::: ???????????? ???????????? ????????? ?????? ::::::::::::::::::::::::::");
	    	try {
	    		
	    		List<String> checkedList = new ArrayList<String>();
	    		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/DiagResult.fxml"));
	    		for(DiagnosisHistoryVO model : diagHistoryList) {
	    			if(model.getFileSelect().isSelected()) {
	    				checkedList.add(model.getHistoryId());
	    			}
	    		}
	    		
	    		Alert alert = new Alert(Alert.AlertType.INFORMATION);
	    		alert.getDialogPane().setPrefWidth(300);
	    		alert.setTitle("??????");
	    		alert.setHeaderText(null);
	    		if(checkedList.size() > 0) {
	    			if(checkedList.size() > 1) {
	    				alert.setContentText("????????? ?????? ??? ?????? ????????? ?????????.");
	    				alert.showAndWait();
	    			}else {
	    				stage = (Stage)((Node)event.getSource()).getScene().getWindow();
	    		        Scene scene1 = new Scene(loader.load());
	    		        stage.setScene(scene1);
	    	    		stage.setTitle("?????? ?????? ??????");
				    	DiagnosisResultController cont = loader.getController();
				    	cont.initData(checkedList.get(0));
	    	    		stage.show();
	    			}
	    		}else {
	    			alert.setContentText("??????????????? ????????? ?????????.");
	    			alert.showAndWait();
	    		}
	    		

			} catch (Exception e) {
				e.printStackTrace();
			}

	    }
	    /*???????????? ?????? ?????????*/
	    @FXML
	    void goRepairResultPage(ActionEvent event) {
	    	logger.info(":::::::::::::::::::::::::: ???????????? ???????????? ????????? ?????? ::::::::::::::::::::::::::");
	    	try {
	    		List<String> checkedList = new ArrayList<String>();
	    		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/RepairResult.fxml"));
	    		for(DiagnosisHistoryVO model : diagHistoryList) {
	    			if(model.getFileSelect().isSelected()) {
	    				checkedList.add(model.getHistoryId());
	    			}
	    		}
	    		
	    		Alert alert = new Alert(Alert.AlertType.INFORMATION);
	    		alert.getDialogPane().setPrefWidth(300);
	    		alert.setTitle("??????");
	    		alert.setHeaderText(null);
	    		if(checkedList.size() > 0) {
	    			if(checkedList.size() > 1) {
	    				alert.setContentText("????????? ?????? ??? ?????? ????????? ?????????.");
	    				alert.showAndWait();
	    			}else {
	    				stage = (Stage)((Node)event.getSource()).getScene().getWindow();
	    				Scene scene1 = new Scene(loader.load());
	    		        stage.setScene(scene1);
	    	    		stage.setTitle("?????? ?????? ??????");
				    	DiagnosisRepairResultController cont = loader.getController();
				    	cont.initData(checkedList.get(0));
	    	    		stage.show();
	    			}
	    		}else {
	    			alert.setContentText("??????????????? ????????? ?????????.");
	    			alert.showAndWait();
	    		}
			} catch (Exception e) {
				System.out.println("????????? ?????????????????? : "+e.getMessage());
				logger.error("reader FileNotFoundException");
				e.printStackTrace();
			}

	    }
	    
	    
	    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		dataSet.setCellValueFactory(new PropertyValueFactory<>("dataSet"));
		errYn.setCellValueFactory(new PropertyValueFactory<>("errYn"));
		fileNm.setCellValueFactory(new PropertyValueFactory<>("fileNm"));
		fileType.setCellValueFactory(new PropertyValueFactory<>("fileType"));
		proStat.setCellValueFactory(new PropertyValueFactory<>("proStat"));
		fixedFile.setCellValueFactory(new PropertyValueFactory<>("fixedFile"));
		fileSelect.setCellValueFactory(new PropertyValueFactory<>("fileSelect"));
		historyId.setCellValueFactory(new PropertyValueFactory<>("historyId"));
	}
	public void initData(String data) {
		logger.info("historyId:"+data);
		logger.info(":::::::::::::::::::::::::: ?????? ?????? ?????? ?????? ::::::::::::::::::::::::::");
		
		HashMap<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("historyId", data);
		List<Map> list = diagnosisHistoryDAO.selectDaigHistoryList(paramMap);
		
		for(Map map : list) {
			CheckBox chk = new CheckBox();
			diagHistoryList.add(new DiagnosisHistoryVO(chk,(String) map.get("dataSet"),(String) map.get("errYn"),(String) map.get("fileNm"),(String) map.get("fileType"),(String) map.get("proStat"),(String) map.get("fixedFile"),Integer.toString((int)map.get("historyId"))));
		}
		diagHistoryTable.setItems(diagHistoryList);
	}

}
