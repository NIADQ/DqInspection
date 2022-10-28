package application.wisefile.common;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class Const {

	public static final String APP_VERSION = "1.0.9";



	public static Stage stage = null;

	public static Stage popStage = null;

	public static Stage diagHistoryStage = null;

	public static Scene scene1 = null;

	public static String defInitialDirectory = "";

	public static final String mainCss = "/css/New.css";

	public static final String CHAR_SET = "UTF-8";
	public static final String RULE_SET_FILE = "C:/diagnosisCSV/data/제공표준룰셋.xlsx";
	public static String FIXED_FILE_PATH = "C:/diagnosisCSV/data/fixed/";

	public static final String CSV_TYPE			= "csv";
	public static final String EXCEl_TYPE		= "excel";
	public static final String CHK_STRING 		= "0";
	public static final String CHK_AMT 			= "1";
	public static final String CHK_QTY 			= "2";
	public static final String CHK_RATE 		= "3";
	public static final String CHK_FLAG 		= "4";
	public static final String CHK_DATE 		= "5";
	public static final String CHK_NO 			= "6";
	public static final String CHK_DATEORD 		= "7";
	public static final String CHK_LOGIC 		= "8";
	public static final String CHK_CALC 		= "9";

	public static final String[][] itemType = {
			{CHK_STRING, 	CHK_STRING,		"1) 문자열","문자열","","0",""},
			{CHK_AMT, 		CHK_AMT,    	"2) 금액","금액","9","1",""},
			{CHK_QTY, 		CHK_QTY,    	"3) 수량","수량","9","2",""},
			{CHK_RATE, 		CHK_RATE,    	"4) 율","율","9","3",""},
			{CHK_FLAG, 		CHK_FLAG+"-1", 	"5-1) 여부 > Y, N","여부","Y/N","4",""},
			{CHK_FLAG, 		CHK_FLAG+"-2",  "5-2) 여부 > 여부값 지정","여부","","5",""},
			{CHK_DATE, 		CHK_DATE+"-1",  "6-1) 날짜 > YYYY-MM-DD HH24:MI:SS","날짜","YYYY-MM-DD HH24:MI:SS","6","연월일 시간분초"},
			{CHK_DATE, 		CHK_DATE+"-2",  "6-2) 날짜 > YYYY-MM-DD HH24:MI","날짜","YYYY-MM-DD HH24:MI","7","연월일 시간분"},
			{CHK_DATE, 		CHK_DATE+"-3",  "6-3) 날짜 > YYYY-MM-DD HH24","날짜","YYYY-MM-DD HH24","8","연월일 시간"},
			{CHK_DATE, 		CHK_DATE+"-4",  "6-4) 날짜 > MM-DD HH24:MI","날짜","MM-DD HH24:MI","9","월일 시간분"},
			{CHK_DATE, 		CHK_DATE+"-5",  "6-5) 날짜 > HH24:MI:SS","날짜","HH24:MI:SS","10","시간분초"},
			{CHK_DATE, 		CHK_DATE+"-6",  "6-6) 날짜 > YYYY-MM-DD","날짜","YYYY-MM-DD","11","연월일"},
			{CHK_DATE, 		CHK_DATE+"-7",  "6-7) 날짜 > HH24:MI","날짜","HH24:MI","12","시간분"},
			{CHK_DATE, 		CHK_DATE+"-8",  "6-8) 날짜 > YYYY-MM","날짜","YYYY-MM","13","연월"},
			{CHK_DATE, 		CHK_DATE+"-9",  "6-9) 날짜 > MM-DD","날짜","MM-DD","14","월일"},
			{CHK_DATE, 		CHK_DATE+"-10", "6-10) 날짜 > HH24","날짜","HH24","15","시간"},
			{CHK_DATE, 		CHK_DATE+"-11", "6-11) 날짜 > YYYY","날짜","YYYY","16","연도"},
			{CHK_DATE, 		CHK_DATE+"-12", "6-12) 날짜 > DD","날짜","DD","17","일자"},
			{CHK_DATE, 		CHK_DATE+"-13", "6-13) 날짜 > MI","날짜","MI","18","분"},
			{CHK_DATE, 		CHK_DATE+"-14", "6-14) 날짜 > MM","날짜","MM","19","월"},
			{CHK_DATE, 		CHK_DATE+"-15", "6-15) 날짜 > SS","날짜","SS","20","초"},
			{CHK_DATE, 		CHK_DATE+"-16", "6-16) 날짜 > MI:SS","날짜","MI:SS","21","분초"},
			{CHK_NO, 		CHK_NO+"-1",    "7-1) 번호 > 전화번호","번호","전화번호","22",""},
			{CHK_NO, 		CHK_NO+"-2",    "7-2) 번호 > 우편번호","번호","우편번호","23",""},
			{CHK_NO, 		CHK_NO+"-3",    "7-3) 번호 > 사업자번호","번호","사업자번호","24",""},
			{CHK_NO, 		CHK_NO+"-4",    "7-4) 번호 > 패턴지정번호","번호","패턴지정번호","25",""},
			{CHK_DATEORD, 	CHK_DATEORD,    "8) 시간순서 일관성","시간순서","","26",""},
			{CHK_LOGIC, 	CHK_LOGIC,    	"9) 컬럼 간 논리관계 일관성","논리관계","","27",""},
			{CHK_CALC, 		CHK_CALC+"-1",  "10-1) 계산식 > 산식","계산식","산식","28",""},
			{CHK_CALC, 		CHK_CALC+"-2",  "10-2) 계산식 > 합계","계산식","합계","29",""}
	};

}
