package application.wisefile.dao;

import java.util.List;
import java.util.Map;

public interface DiagnosisResultDAO {
	public List<Map> selectDaigResultList(Map param);
	public List<Map> selectDaigResultColumn(Map param);
	public List<Map> selectDaigResultColumnUnit(Map param);
}
