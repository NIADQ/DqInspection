package application.wisefile.chk;

import application.wisefile.common.Const;
import application.wisefile.chk.comm.ChkAttribute;

public class ChkNo implements ChkAttribute {

	private static final String[][] regexs_tel = {
			{"5",  "014\\d{2}", ""},
			{"7",  "(\\d{3})(\\d{4})", "$1-$2"},
			{"8",  "(\\d{4})(\\d{4})", "$1-$2"},
			{"9",  "(02)(\\d{3})(\\d{4})", "$1-$2-$3"},
			{"10", "(02)(\\d{4})(\\d{4})", "$1-$2-$3"},
			{"10", "(031|032|033|041|042|043|044|051|052|053|054|055|061|062|063|064|010|011|012|016|017|018|019|030|050|060|070|080)(\\d{3})(\\d{4})", "$1-$2-$3"},
			{"11", "(031|032|033|041|042|043|044|051|052|053|054|055|061|062|063|064|010|011|012|016|017|018|019|030|050|060|070|080)(\\d{4})(\\d{4})", "$1-$2-$3"},
			{"12", "(050\\d|013\\d)(\\d{4})(\\d{4})", "$1-$2-$3"}
	};
	
	@Override
	public ResData getChkResData(ItemVO itemVO) {
		// TODO Auto-generated method stub
		if ((Const.CHK_NO + "-2").equals(itemVO.getKey2())) {
			return chkItem_post(itemVO);
		} else if ((Const.CHK_NO + "-3").equals(itemVO.getKey2())) {
			return chkItem_biz(itemVO);
		}
		return chkItem_tel(itemVO);
	}

	private ResData chkItem_tel(ItemVO itemVO) {
		ResData resData = new ResData();
		boolean isCheck = false;
		String orgData = itemVO.getNode();
		String compData = orgData.replaceAll("[^0-9]", "");
		String cngData = "";
		String errMsg = "";
		String cngMsg =""; 
		
		try {
			if (orgData.trim().length() == 0) {
				isCheck = true;
			}
			for(String[] reg : regexs_tel) {
				if ( compData.length() == Integer.parseInt(reg[0]) && compData.matches(reg[1])) {
					cngData = reg[2].isEmpty() ? compData : compData.replaceAll(reg[1], reg[2]);
					if(orgData.equals(cngData)) {
						isCheck = true;
					} else {
						cngMsg = "번호_전화번호변경::" + orgData + "::" + cngData;
					}
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (!isCheck || !cngMsg.isEmpty()) {
				errMsg ="번호_전화번호_형식아님";
			}
		}

		resData.setOrg(itemVO.getNode());
		resData.setCng(cngData);
		resData.setErrMsg(errMsg);
		resData.setCngMsg(cngMsg);
		
		return resData;
	}
	
	private ResData chkItem_post(ItemVO itemVO) {
		ResData resData = new ResData();
		boolean isCheck = false;
		String orgData = itemVO.getNode();
		String compData = orgData.replaceAll("[^0-9]", "");
		String cngData = "";
		String errMsg = "";
		String cngMsg =""; 
		
		try {
			if (orgData.trim().length() == 0) {
				isCheck = true;
			} else {
				if (compData.matches("(\\d{3})(\\d{3})")) {
					cngData =  compData.replaceAll("(\\d{3})(\\d{3})", "$1-$2");
					if( orgData.equals(cngData)) {
						isCheck = true;
					} else {
						cngMsg = "번호_구우편번호변경::" + orgData + "::" + cngData;
					}
				}
				else if(compData.matches("\\d{5}")) {
					cngData =  compData;
					if( orgData.equals(cngData)) {
						isCheck = true;
					} else {
						cngMsg = "번호_신우편번호변경::" + orgData + "::" + cngData;
					}
				}
				else if(compData.matches("\\d{4}")) {
					cngData =  "0" + compData;
					if( orgData.equals(cngData)) {
						isCheck = true;
					} else {
						cngMsg = "번호_신우편번호변경::" + orgData + "::" + cngData;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (!isCheck || !cngMsg.isEmpty()) {
				errMsg = "번호_우편번호포맷아님";
			}
		}
		
		resData.setOrg(itemVO.getNode());
		resData.setCng(cngData);
		resData.setErrMsg(errMsg);
		resData.setCngMsg(cngMsg);
		
		return resData;
	}

	private ResData chkItem_biz(ItemVO itemVO) {
		ResData resData = new ResData();
		boolean isCheck = false;
		String orgData = itemVO.getNode();
		String cngData = "";
		String cngMsg = "";
		String errMsg = "";
		String compData = orgData.replaceAll("[^0-9]", "");

		try {
			if (orgData.trim().length() == 0) {
				isCheck = true;
			} else {
				if (compData.matches("^\\d{10}$")) {
					cngData = compData.replaceAll("(\\d{3})(\\d{2})(\\d{5})", "$1-$2-$3");
					if(orgData.equals(cngData)) {
						isCheck = true;
					} else {
						cngMsg = "번호_사업자번호변경::"+orgData+"::"+cngData;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (!isCheck || !cngMsg.isEmpty()) {
				errMsg =  "번호_사업자번호포맷아님"; 
			}
		}

		resData.setOrg(itemVO.getNode());
		resData.setCng(cngData);
		resData.setErrMsg(errMsg);
		resData.setCngMsg(cngMsg);
		
		return resData;
	}


}
