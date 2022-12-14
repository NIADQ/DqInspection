package application.wisefile.chk;

import application.wisefile.chk.comm.ChkAttribute;
import application.wisefile.common.Const;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChkDate implements ChkAttribute {

	private static final int[] arrDay = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

	private static final String reg_YYYY = 	"(\\d{4})";
	private static final String reg_MM = 	"((?:0[1-9])|(?:1[0-2]))";
	private static final String reg_DD = 	"((?:0[1-9])|(?:1[0-9])|(?:2[0-9])|(?:3[01]))";
	private static final String reg_HH24 = 	"((?:0[0-9])|(?:1[0-9])|(?:2[0-4]))";
	private static final String reg_MI = 	"((?:0[0-9])|(?:1[0-9])|(?:2[0-9])|(?:3[0-9])|(?:4[0-9])|(?:5[0-9]))";
	private static final String reg_SS = 	"((?:0[0-9])|(?:1[0-9])|(?:2[0-9])|(?:3[0-9])|(?:4[0-9])|(?:5[0-9]))";

	public static final String[][] regexs = {
		{Const.CHK_DATE+"-1",  "yyyy-MM-dd HH:mm:ss", reg_YYYY + "-" + reg_MM + "-" + reg_DD + "\\s" + reg_HH24 + "\\:" + reg_MI + "\\:" + reg_SS}, 
		{Const.CHK_DATE+"-2",  "yyyy-MM-dd HH:mm", reg_YYYY + "-" + reg_MM + "-" + reg_DD + "\\s" + reg_HH24 + "\\:" + reg_MI},
		{Const.CHK_DATE+"-3",  "yyyy-MM-dd HH",  reg_YYYY + "-" + reg_MM + "-" + reg_DD + "\\s" + reg_HH24},
		{Const.CHK_DATE+"-4",  "MM-dd HH:mm",  reg_MM + "-" + reg_DD + "\\s" + reg_HH24 + "\\:" + reg_MI},
		{Const.CHK_DATE+"-5",  "HH:mm:ss", reg_HH24 + "\\:" + reg_MI + "\\:" + reg_SS},
		{Const.CHK_DATE+"-6",  "yyyy-MM-dd", reg_YYYY + "-" + reg_MM + "-" + reg_DD},
		{Const.CHK_DATE+"-7",  "HH:mm", reg_HH24 + "\\:" + reg_MI},
		{Const.CHK_DATE+"-8",  "yyyy-MM", reg_YYYY + "-" + reg_MM},
		{Const.CHK_DATE+"-9",  "MM-dd", reg_MM + "-" + reg_DD},
		{Const.CHK_DATE+"-10", "HH", reg_HH24},
		{Const.CHK_DATE+"-11", "yyyy", reg_YYYY},
		{Const.CHK_DATE+"-12", "dd", reg_DD},
		{Const.CHK_DATE+"-13", "mm", reg_MI},
		{Const.CHK_DATE+"-14", "MM", reg_MM},
		{Const.CHK_DATE+"-15", "ss", reg_SS}
	};

	@Override
	public ResData getChkResData(ItemVO itemVO) {
		// TODO Auto-generated method stub
		return chkItem(itemVO);
	}

	private ResData chkItem(ItemVO itemVO) {
		ResData resData = new ResData();
		boolean isCheck = false;
		String orgData = itemVO.getNode();
		String cngData = "";
		String cngMsg = "";
		String errMsg = "";
		
		String orgType = itemVO.getKey2();
		
		try {
			if (orgData.trim().length() == 0) {
				isCheck = true;
			} else {
				for(String[] reg : regexs) {
					if ( reg[0].equals(orgType) ) {
						if ( orgData.matches("^" + reg[2] + "$") ) {
							if (isDateChk(orgData, reg)) {
								isCheck = true;
							} else {
								cngMsg = "??????_??????????????????";
							}
						} else {
						}
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (!isCheck || !cngMsg.isEmpty()) {
				errMsg = "??????_????????????";
			}
		}

		resData.setOrg(itemVO.getNode());
		resData.setCng(cngData);
		resData.setCngMsg(cngMsg);
		resData.setErrMsg(errMsg);

		return resData;
	}
	
	public static boolean isDateChk(String org, String reg[]) throws Exception {
		String yy = "";
		String mm = "";
		String dd = "";
		Matcher mch = Pattern.compile("^" + reg[2] + "$").matcher(org);
		if (mch.find()) {
			int ty = Integer.parseInt(reg[0].split("-")[1]);
			switch (ty) {
			case 1: case 2: case 3: case 6:
				yy = mch.group(1);
				mm = mch.group(2);
				dd = mch.group(3);
				break;
			case 4:
				mm = mch.group(1);
				dd = mch.group(2);
				break;
			case 8:
				yy = mch.group(1);
				mm = mch.group(2);
				break;
			case 11:
				yy = mch.group(1);
				break;
			default:
				break;
			}
			
			if (mm.isEmpty()) mm = "01";
			if (dd.isEmpty()) dd = "01";
			
			boolean isYun = !yy.isEmpty() && Integer.parseInt(yy) % 4 == 0;
			return isDayChk(Integer.parseInt(mm), Integer.parseInt(dd), isYun);
		}
		return false;
	}
	
	 
	private static boolean isDayChk(int mm, int dd, boolean isYun) {
		int d = arrDay[mm-1];
		if (isYun && mm == 2) d+=1; 
		if (dd > d) return false;
		return true;
	}

}
