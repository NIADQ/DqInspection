package application.wisefile.vo;

import lombok.Data;

@Data
public class DiagnosisFixedDataVO {

    private Long id;
    private Long historyId;
    private String fileId;
    private Integer ruleSetId;
    private Integer rowNum;
    private String origin;
    private String fixed;
    private String fixedMsg;
    private String errorMsg;
    private String fixedYn;

}
