package application.wisefile.service;

import application.wisefile.common.Const;
import application.wisefile.service.impl.CSVFileService;
import application.wisefile.service.impl.ExcelFileService;
import org.apache.commons.lang3.StringUtils;

public class FileServiceFactory {

    public DataFileService getFileService(String fileType) {
        if (StringUtils.isEmpty(fileType)) {
            return  null;
        }

        if (fileType.equalsIgnoreCase(Const.CSV_TYPE)) {
            return new CSVFileService();
        }
        else if (fileType.equalsIgnoreCase(Const.EXCEl_TYPE)) {
            return new ExcelFileService();
        }
        return null;
    }
}
