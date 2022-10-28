package application.wisefile.dao;

import java.util.List;
import java.util.Map;

public interface DiagnosisRepairResultDAO {
	public List<Map> selectRepairResultList(Map param);
	public int updateRepairData(Map param);
	public int updateDiagnosisResult(Map param);
}
