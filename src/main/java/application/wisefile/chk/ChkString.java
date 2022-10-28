package application.wisefile.chk;

import application.wisefile.chk.comm.ChkAttribute;

public class ChkString implements ChkAttribute {
	
	@Override
	public ResData getChkResData(ItemVO itemVO) {
		// TODO Auto-generated method stub
		return chkItem(itemVO);
	}

	private ResData chkItem(ItemVO itemVO) {
		ResData resData = new ResData();
		resData.setOrg(itemVO.getNode());
		return resData;
	}
	
}
