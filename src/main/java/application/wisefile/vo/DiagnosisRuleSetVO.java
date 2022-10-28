package application.wisefile.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DiagnosisRuleSetVO {

    private String fileId;
    private String fileName;
    private String columnName;
    private Integer columnIdx;
    private String columnDataList;
    private String columnUnit;
    private String example;
    private String columnType;
    private String nullYn;
    private String diagnosisYn;
    private String patternYn;
    private String datePattern;
    private String codePattern;
    private String stringPattern;
    private Integer ruleSetId;

}
