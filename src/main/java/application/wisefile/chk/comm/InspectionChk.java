package application.wisefile.chk.comm;

import application.wisefile.chk.ItemVO;
import application.wisefile.chk.ResData;
import application.wisefile.common.Const;
import application.wisefile.chk.*;


public enum InspectionChk {

	
	CHK_STRING 	(Const.CHK_STRING, "1) 문자열", new ChkString()),
	CHK_AMT 	(Const.CHK_AMT, "2) 금액", new ChkAmt()),
	CHK_QTY 	(Const.CHK_QTY, "3) 수량", new ChkQty()),
	CHK_RATE 	(Const.CHK_RATE, "4) 율", new ChkRate()),
	CHK_FLAG	(Const.CHK_FLAG, "5) 여부", new ChkFlag()),
	CHK_DATE	(Const.CHK_DATE, "6) 날짜", new ChkDate()),
	CHK_NO	    (Const.CHK_NO, "7) 번호", new ChkNo()),
	CHK_DATEORD	(Const.CHK_DATEORD, "8) 시간순서 일관성", new ChkDateOrd()),
	CHK_LOGIC	(Const.CHK_LOGIC, "9) 컬럼 간 논리관계 일관성", new ChkLogic()),
	CHK_CALC	(Const.CHK_CALC, "10) 계산식", new ChkCalc());

	private String type;
	private String desc;
	private ChkAttribute chkItem;
	
	InspectionChk (String type, String desc, ChkAttribute chkItem) {
		this.type = type;
		this.desc = desc;
		this.chkItem = chkItem;
	}

	public ChkAttribute getChkItem() {
		return chkItem;
	}

	public ResData getChkResData(ItemVO itemVO) {
		return chkItem.getChkResData(itemVO);
	}

	public String getType() {
		return type;
	}

	public String getDesc() {
		return desc;
	}

}
