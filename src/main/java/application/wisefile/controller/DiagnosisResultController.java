package application.wisefile.controller;

import application.wisefile.Main;
import application.wisefile.dao.DiagnosisResultDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.MapValueFactory;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class DiagnosisResultController implements Initializable{
		protected  Logger logger = Logger.getLogger(this.getClass());
		private Stage stage;
		private String historyId;
		public static DiagnosisResultDAO diagnosisResultDAO;
	    @FXML
	    private TableView<Map<String, String>> diagResultTable;
	    
	    @FXML
	    private Button repairResultBtn;
	    @FXML
	    private Button diagHistoryBtn;
	    
	    
	    public DiagnosisResultController() {
	    	String[] configLocations = new String[] { "applicationContext-standalone.xml" };
			Main.context = new ClassPathXmlApplicationContext(configLocations);
			diagnosisResultDAO = Main.context.getBean(DiagnosisResultDAO.class);
	    }
	    @FXML
	    void goRepairResultPage(ActionEvent event) throws Exception{
	    	logger.info(":::::::::::::::::::::::::: 제공표준 정비결과 페이지 이동 ::::::::::::::::::::::::::");
	    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/RepairResult.fxml"));
	    	try {
				stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		        Scene scene1 = new Scene(loader.load());
		        stage.setScene(scene1);
	    		stage.setTitle("정비 결과 보기");
	    		DiagnosisRepairResultController cont = loader.getController();
	    		cont.initData(historyId);
	    		stage.show();
			} catch (Exception e) {
				e.printStackTrace();
			}
	    }
	    
	    @FXML
	    void goDiagHistoryPage(ActionEvent event) {
	    	logger.info(":::::::::::::::::::::::::: 제공표준 진단 이력 페이지 이동 ::::::::::::::::::::::::::");
	    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/DiagHistory.fxml"));
	    	try {
				stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		        Scene scene1 = new Scene(loader.load());
		        stage.setScene(scene1);
	    		stage.setTitle("진단 이력 보기");
	    		DiagnosisHistoryPopupController cont = loader.getController();
	    		cont.initData(historyId);
	    		stage.show();
			} catch (Exception e) {
				e.printStackTrace();
			}
	    }
	    
	    
		@Override
		public void initialize(URL location, ResourceBundle resources) {
		}
		
		
		public void initData(String data) {
			logger.info("historyId:"+historyId);
			historyId = data;
			addColumn(historyId);
			addRow(historyId);
		}
		
		public void addColumn(String historyId) {
			logger.info("table column 추가 시작");
			int colNum = 1;
			Map<String,String> map = new HashMap<String,String>();
			map.put("historyId", historyId);
			List<Map> columnList 	 = diagnosisResultDAO.selectDaigResultColumn(map);
			logger.info("column info:::"+columnList.toString());
			TableColumn<Map<String, String>,String> column = new TableColumn<>("삭제여부");
			column.setId("dup_del_yn");
			column.setSortable(false);
			column.setStyle("-fx-alignment: BASELINE_CENTER");
			column.setCellValueFactory(new MapValueFactory("dup_del_yn"));
			diagResultTable.getColumns().add(column);
			
			for(Map<String,String> columnMap : columnList) {
				column = new TableColumn<>(String.valueOf(columnMap.get("colNm")));
				column.setId("col"+colNum);
				column.setSortable(false);
				column.setCellValueFactory(new MapValueFactory("col"+colNum));
				diagResultTable.getColumns().add(column);
				colNum++;
			}
		}
		
		public void addRow(String historyId) {
			logger.info("table row data 추가 시작");
			int colNum2 = 1;
			HashMap<String,String> map = new HashMap<String,String>();
			map.put("historyId", historyId);
			List<Map> columnUnitList = diagnosisResultDAO.selectDaigResultColumnUnit(map);
			List<Map> diagResultList = diagnosisResultDAO.selectDaigResultList(map);
			logger.info("columnUnit info:::"+columnUnitList.toString());
			map = new HashMap<String,String>();
			map.put("dup_del_yn", "Y/N");
			for(Map columnUnitMap : columnUnitList) {
				map.put("col"+colNum2,String.valueOf(columnUnitMap.get("columnUnit")));
				colNum2++;
			}
			diagResultTable.getItems().add(map);
			
			for(Map diagResultMap : diagResultList) {
				map = new HashMap<String,String>();
				colNum2 = 1;
				map.put("dup_del_yn",String.valueOf(diagResultMap.get("dup_del_yn")));
				for(int i =0; i <= columnUnitList.size();i++) {
					map.put("col"+colNum2,String.valueOf(diagResultMap.get("col"+colNum2)));
					colNum2++;
				}
				diagResultTable.getItems().add(map);
			}
			
			
		}
}
