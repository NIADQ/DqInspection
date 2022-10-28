package application.wisefile.service.impl;

import application.wisefile.Main;
import application.wisefile.common.Const;
import application.wisefile.dao.DiagnosisHistoryDAO;
import application.wisefile.service.DataFileService;
import application.wisefile.util.FileUtil;
import application.wisefile.vo.DiagnosisDataVO;
import application.wisefile.vo.DiagnosisRuleSetVO;
import application.wisefile.vo.ParamVO;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import org.apache.log4j.Logger;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;

public class CSVFileService implements DataFileService {

    protected Logger logger = Logger.getLogger(this.getClass());

    private final DiagnosisHistoryDAO diagnosisHistoryDAO;

    public CSVFileService() {
        diagnosisHistoryDAO = Main.context.getBean(DiagnosisHistoryDAO.class);
    }
    @Override
    public Integer syncRuleSet() {

        List<DiagnosisRuleSetVO> contentList = new ArrayList<>();

        try (InputStream is = new FileInputStream(Const.RULE_SET_FILE)) {
            assert is != null;
            try (InputStreamReader isr = new InputStreamReader(is, Const.CHAR_SET);
                 CSVReader reader = new CSVReader(isr))
            {
                String[] lineBuffer;
                int rowNum = 0;
                while ((lineBuffer = reader.readNext()) != null) {

                    if (rowNum == 0) {
                        rowNum++;
                        continue;
                    }

                    Field[] f = DiagnosisRuleSetVO.class.getDeclaredFields();
                    DiagnosisRuleSetVO vo = new DiagnosisRuleSetVO();

                    for (int i = 0; i < lineBuffer.length; i++) {
                        f[i].setAccessible(true);
                        if (f[i].getType().getName().equals("java.lang.Integer"))
                            f[i].set(vo, Integer.parseInt(lineBuffer[i]));
                        else
                            f[i].set(vo, lineBuffer[i]);
                    }

                    contentList.add(vo);
                }

                Integer deleteCount = diagnosisHistoryDAO.deleteRuleSet();

                if (deleteCount >= 0 && !contentList.isEmpty()) {
                    for (DiagnosisRuleSetVO vo : contentList) {
                        diagnosisHistoryDAO.insertRuleSet(vo);
                    }
                }

            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException("진단 룰셋 동기화 중오류가 발생했습니다.");
        }

        return 0;
    }

    @Override
    public List<DiagnosisDataVO> readData(ParamVO paramVO, int colCount) {

        FileUtil fu = new FileUtil();
        List<DiagnosisDataVO> contentList = new ArrayList<>();
        String fileCharset = fu.getFileCharset(paramVO.getFileName());
        Boolean isColCorrect = false;
        List<DiagnosisRuleSetVO> ruleSetVOList = diagnosisHistoryDAO.selectFileRuleSet(paramVO.getFileId());

        try (FileInputStream is = new FileInputStream(paramVO.getFileName());
             InputStreamReader isr = new InputStreamReader(is, fileCharset);
             CSVReader reader = new CSVReader(isr))
        {
            String[] lineBuffer;
            int rowNum = 0;
            while ((lineBuffer = reader.readNext()) != null) {
                if (rowNum == 0) {
                    int loopCnt = 0;
                    for (DiagnosisRuleSetVO vo : ruleSetVOList) {
                        if (!vo.getColumnName().equals(lineBuffer[loopCnt])) {
                            throw new RuntimeException("제공표준과 첨부파일의 " + loopCnt + "번째 컬럼명("+lineBuffer[loopCnt]+")이 일치하지 않습니다.");
                        }
                        loopCnt++;
                    }
                    rowNum++;
                    continue;
                }

                if (lineBuffer.length != colCount && !isColCorrect) {
                    if (lineBuffer.length - colCount != 2) {
                        throw new RuntimeException("제공표준과 첨부파일 형식이 일치하지 않습니다.");
                    }
                    else {
                        isColCorrect = true;
                    }
                }

                Optional<String> el = Arrays.stream(lineBuffer).filter(e -> e != "").findAny();
                if (el.get().equals(""))
                    break;

                Field[] f = DiagnosisDataVO.class.getDeclaredFields();
                DiagnosisDataVO vo = new DiagnosisDataVO();

                for (int i = 0; i < colCount; i++) {
                    f[i].setAccessible(true);
                    f[i].set(vo, lineBuffer[i]);
                }

                vo.setFileId(paramVO.getFileId());
                vo.setHistoryId(paramVO.getHistoryId());
                contentList.add(vo);
            }

            if (!contentList.isEmpty()) {
                for (DiagnosisDataVO vo : contentList) {
                    diagnosisHistoryDAO.insertDiagnosisHistory(vo);
                }
            }

        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        } catch (IllegalAccessException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        return contentList;
    }

    @Override
    public String writeData(ParamVO paramVO, List<DiagnosisDataVO> result, List<DiagnosisRuleSetVO> ruleSetVOList) {

        String[] header = ruleSetVOList.stream().map(DiagnosisRuleSetVO::getColumnName).toArray(String[]::new);

        List<String[]> content = new ArrayList<>();
        Field[] f = DiagnosisDataVO.class.getDeclaredFields();
        content.add(header);
        String fileName = FileUtil.getFileName(paramVO, "csv");

        try (CSVWriter writer = new CSVWriter(new FileWriter(Const.FIXED_FILE_PATH + fileName))) {

            for (DiagnosisDataVO p : result) {
                String[] arr = new String[ruleSetVOList.size()];
                for (int i = 0; i < arr.length; i++) {
                    f[i].setAccessible(true);
                    arr[i] = f[i].get(p).toString();
                }
                content.add(arr);
            }

            writer.writeAll(content);

        } catch (IOException e) {
            throw new RuntimeException("정정파일 생성 중 오류가 발생했습니다.");
        } catch (IllegalAccessException e) {
            throw new RuntimeException("정정파일 생성 중 오류가 발생했습니다.");
        }

        return fileName;
    }

    public void writeFixedData(ParamVO paramVO) {

        List<DiagnosisRuleSetVO> ruleSetVOList = diagnosisHistoryDAO.selectFileRuleSet(paramVO.getFileId());
        List<Map> result = diagnosisHistoryDAO.selectDiagnosisResultData(paramVO);

        String[] header = ruleSetVOList.stream().map(DiagnosisRuleSetVO::getColumnName).toArray(String[]::new);

        List<String[]> content = new ArrayList<>();
        content.add(header);

        for (Map p : result) {
            String[] arr = new String[ruleSetVOList.size()];
            for (int i = 0; i < arr.length; i++) {
                arr[i] = p.get("col"+(i+1)).toString();
            }
            content.add(arr);
        }

        try (CSVWriter writer = new CSVWriter(new FileWriter(Const.FIXED_FILE_PATH + FileUtil.getFileName(paramVO, "csv")))) {
            writer.writeAll(content);
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new RuntimeException("정정파일 생성 중 오류가 발생했습니다.");
        }
    }
}
