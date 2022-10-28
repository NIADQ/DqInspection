package application.wisefile.chk;

import application.wisefile.chk.comm.ChkAttribute;

import java.text.DecimalFormat;

public class ChkQty implements ChkAttribute {
	
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
			if (orgData.trim().length() == 0) {
				isCheck = true;
			} else {
				if ( orgData.matches("^[0-9]{1,}$") ) {
					isCheck = true;
				} else {
					boolean isComma		 = orgData.indexOf(",") > 0;

					compData = orgData.replaceAll("[^-\\.\\,0-9]", "");
					
					if (isComma && compData.replaceAll("[^0-9]", "").matches("^.*[0-9]{1,}.*$")) {
						cngData = new DecimalFormat("#,###").format(Long.parseLong(compData.replaceAll("[^0-9]", "")));
					}

					if (cngData.isEmpty()) {
						if( orgData.matches(".*[0-9]{1,}.*")) {
							cngData = compData.replaceAll("[^0-9]", "");
						}
					}
					cngMsg = "수량"
							+ (isComma ? "_콤마" : "")
							+ ":형식변경::" + orgData + "::" + cngData;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (!isCheck || !cngMsg.isEmpty()) {
				errMsg = "수량_작업불가_항목";
			}
		}
		
		resData.setOrg(itemVO.getNode());
		resData.setCng(cngData);
		resData.setErrMsg(errMsg);
		resData.setCngMsg(cngMsg);
		
		return resData;
	}
	
}
