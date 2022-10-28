package application.wisefile.service;

import application.wisefile.vo.ParamVO;
import application.wisefile.vo.DiagnosisDataVO;
import application.wisefile.vo.DiagnosisRuleSetVO;

import javax.swing.tree.ExpandVetoException;
import java.util.List;

public interface DataFileService {

    Integer syncRuleSet();

    List<DiagnosisDataVO> readData(ParamVO paramVO, int colCount);
    String writeData(ParamVO paramVO, List<DiagnosisDataVO> result, List<DiagnosisRuleSetVO> ruleSetVOList);

    void writeFixedData(ParamVO paramVO);

}
