package application.wisefile.chk;

import application.wisefile.chk.comm.ChkAttribute;

import java.text.DecimalFormat;

public class ChkAmt implements ChkAttribute {
	
	private static final String[] regexs = {
			"(-?[0-9]{1,}\\.?[0-9]*)"
	};
	
	public ResData getChkResData(ItemVO itemVO) {
		// TODO Auto-generated method stub
		return chkItem(itemVO);
	}

	private ResData chkItem(ItemVO itemVO) {
		ResData resData = new ResData();
		boolean isCheck = false;
		String orgData = itemVO.getNode();
		String compData = "";
		String cngData = "";
		String errMsg = "";
		String cngMsg =""; 
		
		try {
			
			if (orgData.trim().length() == 0 || orgData.matches("^" + regexs[0] + "$") ) {
				isCheck = true;
			} else {
				boolean isPercent	 = orgData.matches(".*[\\%]{1,}.*");
				boolean isBracket	 = orgData.matches(".*[\\(\\)\\[\\])]{1,}.*");
				boolean isBlank		 = orgData.matches(".*[\\s]{1,}.*");
				boolean isAsterisk	 = orgData.matches(".*[\\*]{1,}.*");
				boolean isDecimal	 = orgData.matches("^\\..*");
				boolean isHyphen	 = orgData.matches(".{1,}\\-.*");
				boolean isComma		 = orgData.indexOf(",") > 0;
				
				compData = orgData.replaceAll("[^-\\.\\,0-9]", "");
				
				if (isComma && compData.replaceAll("[^0-9]", "").matches("^.*[0-9]{1,}.*$")) {
					cngData = new DecimalFormat("#,###").format(Long.parseLong(compData.replaceAll("[^0-9]", "")));
				}
				if (cngData.isEmpty()) {
					if( compData.matches("^" + regexs[0] + "$") ) {
						cngData = compData;
					} else if (compData.matches(".*[0-9]{1,}.*")) {
						cngData = compData.replaceAll("[^0-9]", "");
					}
				}
				cngMsg = "금액"
						+ (isPercent ? "_퍼센트" : "") 
						+ (isBracket ? "_괄호" : "") 
						+ (isBlank ? "_공백" : "") 
						+ (isAsterisk ? "_별표" : "") 
						+ (isDecimal ? "_소수점" : "")
						+ (isHyphen ? "_하이픈" : "") 
						+ (isComma ? "_콤마" : "")
						+ ":형식변경::" + orgData + "::" + cngData;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (!isCheck || !cngMsg.isEmpty()) {
				errMsg = "금액_작업불가_항목";
			}
		}
		
		resData.setOrg(itemVO.getNode());
		resData.setCng(cngData);
		resData.setErrMsg(errMsg);
		resData.setCngMsg(cngMsg);
		
		return resData;
	}
	
}
