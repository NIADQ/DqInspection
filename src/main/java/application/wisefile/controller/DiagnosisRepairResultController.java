package application.wisefile.controller;

import application.wisefile.Main;
import application.wisefile.dao.DiagnosisRepairResultDAO;
import application.wisefile.service.DiagnosisService;
import application.wisefile.vo.DiagnosisRepairResultVO;
import application.wisefile.vo.ParamVO;
import com.ibm.icu.text.SimpleDateFormat;
import com.ibm.icu.util.Calendar;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.*;

public class DiagnosisRepairResultController implements Initializable{
	protected  Logger logger = Logger.getLogger(this.getClass());
	private Stage stage;
	private String historyId;
	
	private static DiagnosisRepairResultDAO diagnosisRepairResultDAO;
	private DiagnosisService diagnosisService;
	@FXML
	private Button diagResultBtn;
	@FXML
    private Button diagHistoryBtn;
	@FXML
    private Button fixBtn;
	@FXML
    private Button excelBtn;
	@FXML
    private TableColumn<DiagnosisRepairResultVO, String> id;
	
	@FXML
    private TableColumn<DiagnosisRepairResultVO, String> rowNum;
	
	@FXML
    private TableColumn<DiagnosisRepairResultVO, String> histId;
	
	@FXML
    private TableColumn<DiagnosisRepairResultVO, String> fileId;
	
	@FXML
    private TableColumn<DiagnosisRepairResultVO, String> columnIndx;
	
	@FXML
    private TableColumn<DiagnosisRepairResultVO, String> colNm;
	
    @FXML
    private TableColumn<DiagnosisRepairResultVO, String> dataSet;
    
    @FXML
    private TableColumn<DiagnosisRepairResultVO, String> orgData;

    @FXML
    private TableView<DiagnosisRepairResultVO> repairResultTable;

    @FXML
    private TableColumn<DiagnosisRepairResultVO, String> fixedData;
    
    @FXML
    private TableColumn<DiagnosisRepairResultVO, String> errMsg;

    @FXML
    private TableColumn<DiagnosisRepairResultVO, String> fixedYn;

    @FXML
    private TableColumn<DiagnosisRepairResultVO, String> ruleSet;
    
    @FXML
    private TableColumn<DiagnosisRepairResultVO, String> fileSelect;

    

    ObservableList<DiagnosisRepairResultVO> repairResultList = FXCollections.observableArrayList();
    public DiagnosisRepairResultController() {
    	String[] configLocations = new String[] { "applicationContext-standalone.xml" };
		Main.context = new ClassPathXmlApplicationContext(configLocations);
		diagnosisRepairResultDAO = Main.context.getBean(DiagnosisRepairResultDAO.class);
    }
    
    private Alert getAlertOpen(String tit, String cnts, Alert.AlertType at) {
		Alert alert = new Alert(at);
		alert.setHeaderText(tit);
		alert.setContentText(cnts);
		return alert;
	}

	@FXML
	void goDiagResultPage(ActionEvent event) throws IOException{
		logger.info(":::::::::::::::::::::::::: 제공표준 진단 결과 페이지 이동 START ::::::::::::::::::::::::::");
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/DiagResult.fxml"));
		try {
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			Scene scene1 = new Scene(loader.load());
			stage.setScene(scene1);
			stage.setTitle("진단 결과 보기");
			DiagnosisResultController cont = loader.getController();
			cont.initData(historyId);
			stage.show();
		} catch (Exception e) {
			// TODO: handle exception
		}

	}
	/*진단 이력 페이지 이동 버튼*/
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
	    
	@FXML
	void fixEvent(ActionEvent event) {
		logger.info(":::::::::::::::::::::::::: 정비 이벤트 START ::::::::::::::::::::::::::");
		try {
			String fileDesc  = "";
			String fileId 	 = "";
			String historyId = "";

			for(DiagnosisRepairResultVO model : repairResultTable.getItems()) {
				if(model.getFileSelect().isSelected()) {
					if(!model.getFixedYn().equals("Y")) {
						HashMap<String,String> paramMap = new HashMap<String, String>();
						paramMap.put("id", model.getId());
						paramMap.put("fixedData", model.getFixedData());
						paramMap.put("fileId", model.getFileId());
						paramMap.put("histId", model.getHistId());
						paramMap.put("columnIndx",model.getColumnIndx());
						paramMap.put("dataSet",model.getDataSet());

						diagnosisRepairResultDAO.updateRepairData(paramMap);
						diagnosisRepairResultDAO.updateDiagnosisResult(paramMap);
						fileDesc = model.getDataSet();
						fileId   = model.getFileId();
						historyId= model.getHistId();
					}
				}
			}
			Map resultMap = Main.diagnosisFileDAO.selectOrganName();
			ParamVO param = ParamVO.builder()
					.fileDesc(fileDesc)
					.fileName("")
					.historyId(Long.parseLong(historyId))
					.fileId(fileId)
					.organName(resultMap.get("organName").toString())
					.build();

			diagnosisService.writeFixedData(param);

			getAlertOpen("알림", "저장에 성공했습니다.", Alert.AlertType.INFORMATION).showAndWait();

			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/RepairResult.fxml"));
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			Scene scene1 = new Scene(loader.load());
			stage.setScene(scene1);
			stage.setTitle("정비 결과 보기");

			DiagnosisRepairResultController cont = loader.getController();
			cont.initData(historyId);
			stage.show();


		} catch (Exception e) {
			logger.error("fixEvent", e);
			getAlertOpen("알림", "정비실행시 오류가 발생했습니다.", Alert.AlertType.INFORMATION).showAndWait();
		}


	}
	    
	    
	@FXML
	void excelDown(ActionEvent event) throws IOException{
		logger.info(":::::::::::::::::::::::::: EXCEL DOWNLOAD START ::::::::::::::::::::::::::");
		DirectoryChooser directoryChooser = new DirectoryChooser();
		File file = directoryChooser.showDialog(stage);
		String path ="";
		if (file == null) {
			return;
		}else {
			path = file.getPath();
		}
		

		SimpleDateFormat format1 = new SimpleDateFormat ( "yyyyMMdd_HHmmss");
		Calendar time = Calendar.getInstance();
		String format_time1 = format1.format(time.getTime());
		SXSSFWorkbook wb = new SXSSFWorkbook();
		CellStyle headStyle = wb.createCellStyle();
		headStyle.setAlignment(HorizontalAlignment.CENTER);
		headStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		headStyle.setBorderRight(BorderStyle.THIN);
		headStyle.setBorderLeft(BorderStyle.THIN);
		headStyle.setBorderTop(BorderStyle.THIN);
		headStyle.setBorderBottom(BorderStyle.THIN);
		headStyle.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
		headStyle.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
		headStyle.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
		headStyle.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());

		Font font = wb.createFont();
		font.setFontName("맑은 고딕");
		font.setFontHeight((short)(13*20));
		font.setBold(true);
		headStyle.setFont(font);

		Sheet sh = wb.createSheet();
		sh.setColumnWidth(0, 4000);
		sh.setColumnWidth(1, 4000);
		sh.setColumnWidth(2, 4000);
		sh.setColumnWidth(3, 10000);
		sh.setColumnWidth(4, 10000);
		sh.setColumnWidth(5, 15000);
		sh.setColumnWidth(6, 4000);

		Row row = sh.createRow(0);
		int cellnum = 0;

		Cell cell = row.createCell(cellnum);

		List<String> headers = Arrays.asList(new String[]{"데이터셋","컬럼명","룰셋","원본 값","정비 값","정비 메시지","정비여부"});

		for (String header : headers) {
			cell.setCellValue(header);
			cell.setCellStyle(headStyle);
			cell = row.createCell(++cellnum);

		}

		HashMap<String,String> paramMap = new HashMap<String, String>();
		paramMap.put("historyId", historyId);
		List<Map> list = diagnosisRepairResultDAO.selectRepairResultList(paramMap);
		String fileName = (String)list.get(0).get("dataSet");
		int rownum = 1;
		for(Map map : list) {
			CellStyle bodyStyle = wb.createCellStyle();
			bodyStyle.setAlignment(HorizontalAlignment.CENTER);
			bodyStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			bodyStyle.setBorderRight(BorderStyle.THIN);
			bodyStyle.setBorderLeft(BorderStyle.THIN);
			bodyStyle.setBorderTop(BorderStyle.THIN);
			bodyStyle.setBorderBottom(BorderStyle.THIN);
			bodyStyle.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
			bodyStyle.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
			bodyStyle.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
			bodyStyle.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
			Font bodyFont = wb.createFont();
			bodyFont.setFontName("맑은 고딕");

			String fixedYn = (String) map.get("fixedYn");
			if(fixedYn.equals("Y")){
				bodyFont.setColor(IndexedColors.BLUE.index);
			}else {
				bodyFont.setColor(IndexedColors.RED.index);

			}
			bodyStyle.setFont(bodyFont);
			row = sh.createRow(rownum);
			cell = row.createCell(0);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue((String) map.get("dataSet"));
			cell = row.createCell(1);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue((String) map.get("colNm"));
			cell = row.createCell(2);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue((String) map.get("ruleSet"));
			cell = row.createCell(3);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue((String) map.get("orgData"));
			cell = row.createCell(4);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue((String) map.get("fixedData"));
			cell = row.createCell(5);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue((String) map.get("errMsg"));
			cell = row.createCell(6);
			cell.setCellStyle(bodyStyle);
			cell.setCellValue((String) map.get("fixedYn"));
			rownum++;

		}

		FileOutputStream fileoutputstream=null;
		try {
			fileoutputstream = new FileOutputStream(path+"/"+ fileName+"_정비결과_"+format_time1+".xlsx");
			wb.write(fileoutputstream);
			Desktop.getDesktop().open(new File(path+"/"));
			getAlertOpen("정비결과 저장", "정비결과 파일이 " + path+"/" + "폴더에 저장되었습니다.", Alert.AlertType.INFORMATION).showAndWait();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			try {
				fileoutputstream.close();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
	    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		diagnosisService = Main.context.getBean(DiagnosisService.class);
		repairResultTable.setEditable(true);
		fixBtn.setDisable(true);
		excelBtn.setDisable(true);
		fileSelect.setCellValueFactory(new PropertyValueFactory<>("fileSelect"));
		colNm.setCellValueFactory(new PropertyValueFactory<>("colNm"));
		dataSet.setCellValueFactory(new PropertyValueFactory<>("dataSet"));
		orgData.setCellValueFactory(new PropertyValueFactory<>("orgData"));
		orgData.setCellFactory(new Callback<TableColumn<DiagnosisRepairResultVO,String>, TableCell<DiagnosisRepairResultVO,String>>() {
			@Override
			public TableCell<DiagnosisRepairResultVO, String> call(TableColumn<DiagnosisRepairResultVO, String> param) {
				return new TableCell<DiagnosisRepairResultVO, String>() {

	                @Override
	                public void updateItem(String item, boolean empty) {
	                    super.updateItem(item, empty);
	                    if (!isEmpty()) {
	                    	DiagnosisRepairResultVO data = getTableRow().getItem();
	                    	
	                    	if(data != null && data.getFixedYn().equals("Y")) {
	                    		getTableRow().setStyle("-fx-background-color:c7ecff;");
	                    	}else {
	                    		getTableRow().setStyle("-fx-background-color:ff9999;");
	                    		
	                    		
	                    	}
	                        setText(item);
	                    }
	                }
	            };
			}
			
		});
		
		fixedData.setCellValueFactory(new PropertyValueFactory<>("fixedData"));
		fixedData.setCellFactory(TextFieldTableCell.forTableColumn());
		fixedData.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<DiagnosisRepairResultVO,String>>() {
			
			@Override
			public void handle(CellEditEvent<DiagnosisRepairResultVO, String> event) {
				DiagnosisRepairResultVO repair = event.getRowValue();
				repair.setFixedData(event.getNewValue());
			}
		});
		
		errMsg.setCellValueFactory(new PropertyValueFactory<>("errMsg"));
		fixedYn.setCellValueFactory(new PropertyValueFactory<>("fixedYn"));
		ruleSet.setCellValueFactory(new PropertyValueFactory<>("ruleSet"));
		id.setCellValueFactory(new PropertyValueFactory<>("id"));
		rowNum.setCellValueFactory(new PropertyValueFactory<>("rowNum"));
	}
	
	public void initData(String data) {
		logger.info("historyId:::"+data);
		historyId = data;

		HashMap<String,String> paramMap = new HashMap<String, String>();
		paramMap.put("historyId", historyId);
		List<Map> list = diagnosisRepairResultDAO.selectRepairResultList(paramMap);
		if(list.size() > 0) {
			excelBtn.setDisable(false);	
		}else {
			excelBtn.setDisable(true);
		}
		for(Map map : list) {
			CheckBox chk = new CheckBox();
			chk.setOnAction(event -> {
				checkEvent();
			});
			repairResultList.add(new DiagnosisRepairResultVO(chk,
					map.get("rowNum") == null ? 0 : Integer.parseInt(map.get("rowNum").toString()),
					(String) map.get("colNm"),
					(String) map.get("dataSet"),
					(String) map.get("errMsg"),
					(String) map.get("ruleSet"),
					(String) map.get("orgData"),
					(String) map.get("fixedData"),
					(String) map.get("fixedYn"),
					Integer.toString((int) map.get("id")),
					Integer.toString((int) map.get("histId")),
					(String) map.get("fileId"),
					(String) map.get("columnIndx")));
		}
		repairResultTable.setItems(repairResultList);
		
	}
	public void checkEvent() {
		ObservableList<DiagnosisRepairResultVO> items = repairResultTable.getItems();
		int result = 0;
		for(DiagnosisRepairResultVO item: items) {
			if(!item.getFixedYn().equals("Y")) {
				if(item.getFileSelect().isSelected()) {
					result += 1;
				}
			}
		}
		if(result > 0) {
			fixBtn.setDisable(false);
		}else {
			fixBtn.setDisable(true);
		}
	}

}
