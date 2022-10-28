package application.wisefile.chk;

import application.wisefile.chk.comm.ChkAttribute;

public class ChkLogic implements ChkAttribute {

	@Override
	public ResData getChkResData(ItemVO itemVO) {
		// TODO Auto-generated method stub
		return chkItem(itemVO);
	}

	private ResData chkItem(ItemVO itemVO) {
		ResData resData = new ResData();
		boolean isCheck = false;
		String orgData = "";
		String compData = "";
		String cngData = "";
		String errMsg = "";
		String cngMsg =""; 

		try {
			orgData = itemVO.getNode();
			
			compData = itemVO.getOpt1();
			
			cngData = orgData;
			
			if (orgData.equals(compData)) {
				String dateVal = itemVO.getNodeRow()[Integer.parseInt(itemVO.getOpt2())];
				
				String[] reg = chkItemNext(dateVal);
				if (reg == null) {
					cngMsg = "컬럼 간 논리관계 일관성_논리관계가 있는 컬럼 날짜("+itemVO.getOpt2()+") 타입 아님";
				} else {
					if (ChkDate.isDateChk(dateVal, reg)) {
						isCheck = true;
					} else {
						cngMsg = "컬럼 간 논리관계 일관성_날짜("+itemVO.getOpt2()+") 유효범위 오류";
					}
				}
			} else {
				isCheck = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (!isCheck || !cngMsg.isEmpty()) {
				errMsg = "컬럼 간 논리관계 일관성_작업불가_항목";
			}
		}
		
		resData.setOrg(itemVO.getNode());
		resData.setCng(cngData);
		resData.setErrMsg(errMsg);
		resData.setCngMsg(cngMsg);
		
		return resData;
	}
	
	private String[] chkItemNext(String paramData) throws Exception {
		for(String[] reg : ChkDate.regexs) {
			if ( paramData.matches("^" + reg[2] + "$") ) return reg;
		}
		return null;
	}
	
}
