package application.wisefile.vo;

import lombok.Data;

@Data
public class DiagnosisFileVO {
    private String fileId;
    private String fileName;
    private Integer columnCount;
    private Integer seq;
    private String offerOrganName;
}
