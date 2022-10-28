package application.wisefile.vo;

import lombok.Data;

@Data
public class DiagnosisFileHistoryVO {

    private Long id;
    private Long historyId;
    private String fileId;
    private String fileType;
    private String filePath;
    private String fileName;
    private String status;
    private String errorYn;
    private String fixedFile;

}
