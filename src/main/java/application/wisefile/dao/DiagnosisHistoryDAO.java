package application.wisefile.dao;

import application.wisefile.vo.*;

import java.util.List;
import java.util.Map;

public interface DiagnosisHistoryDAO {

    List<Map> selectDiagnosisData(ParamVO paramVO);
    List<Map> selectDiagnosisResultData(ParamVO paramVO);
    List<DiagnosisDataVO> selectDiagnosisResultVO(ParamVO paramVO);

    List<DiagnosisRuleSetVO> selectFileRuleSet(String fileId);

    Integer insertDiagnosisFixedHistory(DiagnosisFixedDataVO diagnosisFixedDataVO);

    Integer insertDiagnosisResult(DiagnosisDataVO diagnosisDataVO);

    Integer insertDiagnosisHistory(DiagnosisDataVO diagnosisDataVO);

    Integer updateFileHistory(DiagnosisFileHistoryVO diagnosisFileHistoryVO);

    DiagnosisFileHistoryVO selectFileHistory(ParamVO paramVO);

    List<CommCodeVO> selectCommCode(String codeId);

    Integer deleteRuleSet();

    Integer insertRuleSet(DiagnosisRuleSetVO diagnosisRuleSetVO);

    List<Map> selectDaigHistoryList(Map param);

    Integer deleteFile();

    Integer insertFile(DiagnosisFileVO diagnosisFileVO);
}
