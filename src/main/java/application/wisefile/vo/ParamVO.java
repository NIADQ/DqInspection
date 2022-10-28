package application.wisefile.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ParamVO {

    private String fileDesc;
    private String fileName;
    private Long historyId;
    private String fileId;
    private String organName;
    private Boolean dupChkYn;
    private String fileType;
}
