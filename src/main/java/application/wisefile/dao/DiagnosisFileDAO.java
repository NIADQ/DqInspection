package application.wisefile.dao;

import application.wisefile.vo.DiagnosisFileVO;

import java.util.List;
import java.util.Map;

public interface DiagnosisFileDAO {
    public List<Map> selectFileList(Map param);

    public List<Map> selectHistoryList(Map param);

    public Map selectOrganName();

    public int updateOrganName(Map param);

    public int deleteHistory(Map param);

    public int deleteDiagHistory(Map param);

    public int deleteDiagResult(Map param);

    public int deleteFileHistory(Map param);
    public int deleteFixedHistory(Map param);

    public Long selectNextHistoryId();

    public int updateNextHistoryId();

    public int insertFileHistory(Map param);

    public int insertHistory(Map param);

}
