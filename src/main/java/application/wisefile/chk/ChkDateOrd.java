package application.wisefile.chk;

import application.wisefile.chk.comm.ChkAttribute;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChkDateOrd implements ChkAttribute {

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
			
			compData = itemVO.getNodeRow()[Integer.parseInt(itemVO.getOpt1())];
			
			if (orgData.trim().length() == 0 && compData.trim().length() == 0) {
				isCheck = true;
			} else {
				
				String[] orgDataReg = chkItemNext(orgData);
				boolean isOrgDateChk = orgDataReg != null ? ChkDate.isDateChk(orgData, orgDataReg) : false;
				
				String[] compDataReg = chkItemNext(compData);
				boolean isCompDateChk = compDataReg != null ? ChkDate.isDateChk(compData, compDataReg) : false;

				if (orgData.trim().length() > 0 && orgDataReg == null) {
					cngMsg = "시간순서일관성_지정 컬럼 날짜("+orgData+") 타입 아님";
				} else if (orgData.trim().length() > 0 && !isOrgDateChk) {
					cngMsg = "시간순서일관성_지정 컬럼 날짜("+orgData+") 유효범위 오류";
				}

				if (cngMsg.isEmpty()) {
					if (compData.trim().length() > 0 && compDataReg == null) {
						cngMsg = "시간순서일관성_비교대상 컬럼 날짜("+compData+") 타입 아님";
					} else if (compData.trim().length() > 0 && !isCompDateChk) {
						cngMsg = "시간순서일관성_비교대상 컬럼 날짜("+compData+") 유효범위 오류";
					}
				}

				if (cngMsg.isEmpty()) {
					if (orgData.trim().length() > 0 && compData.trim().length() > 0 && !orgDataReg[0].equals(compDataReg[0])) {
						cngMsg = "시간순서일관성_지정 컬럼 날짜 ("+orgData+") 타입 과 비교대상 컬럼 날짜 ("+compData+") 타입 틀림";
					}
				}

				if (cngMsg.isEmpty()) {
					if (orgData.trim().length() == 0 && compData.trim().length() > 0) {
						orgDataReg = null;
						orgDataReg = compDataReg;
						orgData = new SimpleDateFormat(orgDataReg[1]).format(getMaxDate());
					}
					if (compData.trim().length() == 0 && orgData.trim().length() > 0) {
						compDataReg = null;
						compDataReg = orgDataReg;
						compData = new SimpleDateFormat(compDataReg[1]).format(getMaxDate());
					}
					
					int rdoIdx = Integer.parseInt(itemVO.getOpt2());
					
					long orgVal = Long.parseLong(orgData.replaceAll("[^0-9]", ""));
					long cngVal = Long.parseLong(compData.replaceAll("[^0-9]", ""));
					if (rdoIdx == 1) {
						if (orgVal >= cngVal) {
							isCheck = true;
						} else {
							cngMsg = "시간순서일관성_지정컬럼("+orgData+") >= 비교대상 컬럼("+compData+") 크거나 같아야 함";
						}
					} else if (rdoIdx == 2) {
						if (orgVal > cngVal) {
							isCheck = true;
						} else {
							cngMsg = "시간순서일관성_지정컬럼("+orgData+") > 비교대상 컬럼("+compData+") 커야 함";
						}
					} else if (rdoIdx == 3) {
						if (orgVal <= cngVal) {
							isCheck = true;
						} else {
							cngMsg = "시간순서일관성_지정컬럼("+orgData+") <= 비교대상 컬럼("+compData+") 작거나 같아야 함";
						}
					} else if (rdoIdx == 4) {
						if (orgVal < cngVal) {
							isCheck = true;
						} else {
							cngMsg = "시간순서일관성_지정컬럼("+orgData+") < 비교대상 컬럼("+compData+") 작아야 함";
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (!isCheck || !cngMsg.isEmpty()) {
				errMsg = "시간순서일관성_작업불가_항목";
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

	private Date getMaxDate() {
		try {
			return new SimpleDateFormat("yyyyMMddHHmmss").parse("99991231235959");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
}
