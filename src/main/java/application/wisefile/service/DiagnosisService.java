package application.wisefile.service;

import application.wisefile.common.Const;
import application.wisefile.dao.DiagnosisHistoryDAO;
import application.wisefile.util.address.Address;
import application.wisefile.util.address.AddressToken;
import application.wisefile.vo.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DiagnosisService {

    @Autowired
    private DiagnosisHistoryDAO diagnosisHistoryDAO;

    private FileServiceFactory fileServiceFactory;

    public Map diagnosis(ParamVO paramVO) throws Exception {

        String fileId = paramVO.getFileId();
        Long historyId = paramVO.getHistoryId();
        List<DiagnosisRuleSetVO> diagnosisRuleSetVOList = diagnosisHistoryDAO.selectFileRuleSet(fileId);

        fileServiceFactory = new FileServiceFactory();
        DataFileService fileService = null;

        if (paramVO.getFileType().equals(Const.CSV_TYPE)) {
            fileService = fileServiceFactory.getFileService(Const.CSV_TYPE);
        }
        else {
            fileService = fileServiceFactory.getFileService(Const.EXCEl_TYPE);
        }

        fileService.readData(paramVO, diagnosisRuleSetVOList.size());

        List<Map> diagDataList = diagnosisHistoryDAO.selectDiagnosisData(paramVO);

        List<CommCodeVO> codeVOList = diagnosisHistoryDAO.selectCommCode("COMM_CD_01");

        Integer columnCount = diagnosisRuleSetVOList.size();

        List<DiagnosisDataVO> diagnosisResultDataList = new ArrayList<>();

        List<DiagnosisFixedDataVO> diagnosisFixedDataVOList = new ArrayList<>();

        Field[] f = DiagnosisDataVO.class.getDeclaredFields();

        Boolean isError = false;

        List<String> addressColumn = codeVOList.stream().map(e -> e.getCodeValue()).collect(Collectors.toList());
        Integer rowNum = 0;
        for (Map dataMap : diagDataList) {
            DiagnosisDataVO resultVO = new DiagnosisDataVO();
            resultVO.setHistoryId(historyId);
            resultVO.setFileId(fileId);
            resultVO.setId((long) Integer.parseInt(dataMap.get("diagnosis_id").toString()));
            rowNum++;

            for (int i = 0; i < columnCount; i++) {

                String diagData = dataMap.get("col"+(i+1)).toString();
                DiagnosisRuleSetVO ruleSetVO = diagnosisRuleSetVOList.get(i);
                f[i].setAccessible(true);
                f[i].set(resultVO, diagData);

                if ("Y".equals(ruleSetVO.getNullYn())) {
                    if (StringUtils.isEmpty(diagData)) {

                        if (ruleSetVO.getFileId().equals("FILE_0100") && ruleSetVO.getColumnName().equals("소재지도로명주소")) {
                            if (!StringUtils.isEmpty(dataMap.get("col"+(i+2)).toString())) {
                                continue;
                            }
                        }

                        if (ruleSetVO.getFileId().equals("FILE_0112")) {
                            if (ruleSetVO.getColumnName().equals("도로노선번호")) {
                                String prevData = dataMap.get("col"+(i)).toString();
                                DiagnosisRuleSetVO prevRuleSetVO = diagnosisRuleSetVOList.get(i-1);
                                List<String> dataList = List.of("고속국도","일반국도","지방도","국가지원지방도");
                                if (!dataList.contains(prevData))
                                    continue;
                            }
                        }
                        DiagnosisFixedDataVO fixedDataVO = new DiagnosisFixedDataVO();
                        fixedDataVO.setId(resultVO.getId());
                        fixedDataVO.setHistoryId(historyId);
                        fixedDataVO.setRuleSetId(i+1);
                        fixedDataVO.setRowNum(rowNum);
                        fixedDataVO.setFileId(fileId);
                        fixedDataVO.setFixedYn("N");
                        fixedDataVO.setErrorMsg("공백 입력 오류 : 해당 항목은 필수입력 입니다.");
                        diagnosisFixedDataVOList.add(fixedDataVO);
                        isError = true;
                        continue;
                    }
                }
                else {
                    if (StringUtils.isEmpty(diagData)) {
                        continue;
                    }
                }

                if (addressColumn.contains(ruleSetVO.getColumnName())) {
                    AddressToken at = new AddressToken();
                    Address address = at.createVO(diagData);
                    String[] token = diagData.split(" ");

                    if (!StringUtils.isEmpty(token[0])) {
                        token[0] = address.getSiDo();
                        f[i].set(resultVO, String.join(" ", token));
                    }

                    if (address.getSiDo().isEmpty() || address.getSiGoonGoo().isEmpty() ||
                            (address.getEupMyunDong().isEmpty() && address.getDoro().isEmpty())
                    ) {
                        DiagnosisFixedDataVO fixedDataVO = new DiagnosisFixedDataVO();
                        fixedDataVO.setId(resultVO.getId());
                        fixedDataVO.setHistoryId(historyId);
                        fixedDataVO.setRuleSetId(i+1);
                        fixedDataVO.setRowNum(rowNum);
                        fixedDataVO.setFileId(fileId);
                        fixedDataVO.setOrigin(diagData);
                        fixedDataVO.setFixedYn("N");
                        fixedDataVO.setErrorMsg("주소 형식 오류(공백 띄어쓰기 확인) 예)" + ruleSetVO.getExample());
                        fixedDataVO.setFixedMsg(fixedDataVO.getErrorMsg());
                        diagnosisFixedDataVOList.add(fixedDataVO);
                        isError = true;
                    }
                    continue;
                }

                if ("Y".equals(ruleSetVO.getPatternYn())) {
                    if (!StringUtils.isEmpty(ruleSetVO.getCodePattern())) {
                            List<String> dataList = List.of(ruleSetVO.getCodePattern().split("\\^"));

                            if (!dataList.contains(diagData)) {
                                Boolean isFixed = false;
                                for (String str : dataList) {
                                    if (str.matches("^\\d") && diagData.matches("^\\d")) {
                                        if (Integer.parseInt(diagData) == Integer.parseInt(str)) {
                                            f[i].set(resultVO, str);
                                            DiagnosisFixedDataVO fixedDataVO = new DiagnosisFixedDataVO();
                                            fixedDataVO.setId(resultVO.getId());
                                            fixedDataVO.setHistoryId(historyId);
                                            fixedDataVO.setRuleSetId(i+1);
                                            fixedDataVO.setRowNum(rowNum);
                                            fixedDataVO.setFileId(fileId);
                                            fixedDataVO.setFixed(str);
                                            fixedDataVO.setOrigin(diagData);
                                            fixedDataVO.setFixedYn("Y");
                                            fixedDataVO.setFixedMsg("코드 형식 정정 예)" + ruleSetVO.getExample());
                                            diagnosisFixedDataVOList.add(fixedDataVO);
                                            isFixed = true;
                                            break;
                                        }
                                    }
                                }

                                if (!isFixed) {
                                    DiagnosisFixedDataVO fixedDataVO = new DiagnosisFixedDataVO();
                                    fixedDataVO.setId(resultVO.getId());
                                    fixedDataVO.setHistoryId(historyId);
                                    fixedDataVO.setRuleSetId(i+1);
                                    fixedDataVO.setRowNum(rowNum);
                                    fixedDataVO.setFileId(fileId);
                                    fixedDataVO.setOrigin(diagData);
                                    fixedDataVO.setFixedYn("N");
                                    fixedDataVO.setErrorMsg("코드값 불일치");
                                    diagnosisFixedDataVOList.add(fixedDataVO);
                                    isError = true;
                                }
                            }
                    }

                    if (!StringUtils.isEmpty(ruleSetVO.getStringPattern())) {

                        if (ruleSetVO.getStringPattern().contains("^")) {
                            List<String> regExpList = List.of(ruleSetVO.getStringPattern().split("\\^"));

                            Boolean isMatch = false;

                            Optional<String> first = regExpList.stream().filter(s -> s.contains("-")).findAny();
                            if (first.isPresent()) {
                                for (String expStr : regExpList) {
                                    String[] patterns = expStr.split("-");

                                    StringBuffer regExp = new StringBuffer("^(\\d{1,");
                                    for (int k = 0; k < patterns.length; k++) {
                                        regExp.append(patterns[k].length());
                                        if (patterns.length > 0 && k < patterns.length-1) {
                                            regExp.append("})[\\-](\\d{1,");
                                        }
                                    }
                                    regExp.append("})$");

                                    if (diagData.matches(regExp.toString())) {
                                        isMatch = true;
                                        break;
                                    }
                                }
                                if (!isMatch) {
                                    String result = "";
                                    for (String expStr : regExpList) {
                                        String[] patterns = expStr.split("-");
                                        if (patterns[0].length() == 2)
                                            if (!diagData.substring(0,2).equals("02"))
                                                continue;

                                        result = fixedData(expStr, diagData, "-", "-");

                                        if (!result.isEmpty())
                                            break;
                                    }

                                    DiagnosisFixedDataVO fixedDataVO = new DiagnosisFixedDataVO();
                                    fixedDataVO.setId(resultVO.getId());
                                    fixedDataVO.setHistoryId(historyId);
                                    fixedDataVO.setRuleSetId(i+1);
                                    fixedDataVO.setRowNum(rowNum);
                                    fixedDataVO.setFileId(fileId);
                                    fixedDataVO.setOrigin(diagData);
                                    if (!result.isEmpty()) {
                                        fixedDataVO.setFixed(result);
                                        fixedDataVO.setFixedMsg("번호 형식 정정 예)" + ruleSetVO.getExample());
                                        fixedDataVO.setFixedYn("Y");
                                        f[i].set(resultVO, result);
                                    }
                                    else {
                                        fixedDataVO.setFixedYn("N");
                                        fixedDataVO.setErrorMsg("번호 형식 불일치 오류 예)" + ruleSetVO.getExample());
                                    }
                                    isError = true;

                                    diagnosisFixedDataVOList.add(fixedDataVO);
                                    continue;
                                }
                            }

                            Optional<String> first2 = regExpList.stream().filter(s -> s.contains(":")).findAny();
                            if (first2.isPresent()) {
                                for (String expStr : regExpList) {
                                    String[] patterns = expStr.split(":");

                                    StringBuffer regExp = new StringBuffer("^(\\d{");
                                    for (int k = 0; k < patterns.length; k++) {
                                        regExp.append(patterns[k].length());
                                        if (patterns.length > 0 && k < patterns.length-1) {
                                            regExp.append("})[\\:](\\d{");
                                        }
                                    }
                                    regExp.append("})$");

                                    if (diagData.matches(regExp.toString())) {
                                        isMatch = true;
                                        break;
                                    }
                                }

                                if (!isMatch) {
                                    String result = "";
                                    for (String expStr : regExpList) {
                                        result = fixedData(expStr, diagData, ":", ":");

                                        if (!result.isEmpty())
                                            break;
                                    }

                                    DiagnosisFixedDataVO fixedDataVO = new DiagnosisFixedDataVO();
                                    fixedDataVO.setId(resultVO.getId());
                                    fixedDataVO.setHistoryId(historyId);
                                    fixedDataVO.setRuleSetId(i+1);
                                    fixedDataVO.setRowNum(rowNum);
                                    fixedDataVO.setFileId(fileId);
                                    fixedDataVO.setOrigin(diagData);
                                    if (!result.isEmpty()) {
                                        fixedDataVO.setFixed(result);
                                        fixedDataVO.setFixedMsg("시간 형식 정정 예)" + ruleSetVO.getExample());
                                        fixedDataVO.setFixedYn("Y");
                                        f[i].set(resultVO, result);
                                    }
                                    else {
                                        fixedDataVO.setFixedYn("N");
                                        fixedDataVO.setErrorMsg("시간 형식 오류 예)" + ruleSetVO.getExample());
                                    }
                                    isError = true;
                                    diagnosisFixedDataVOList.add(fixedDataVO);
                                    continue;
                                }
                            }

                            Optional<String> first3 = regExpList.stream().filter(s -> s.contains(".")).findAny();
                            if (first3.isPresent()) {
                                for (String expStr : regExpList) {
                                    String[] patterns = expStr.split("\\.");

                                    StringBuffer regExp = new StringBuffer("^(\\d{1,");
                                    for (int k = 0; k < patterns.length; k++) {
                                        regExp.append(patterns[k].length());
                                        if (patterns.length > 0 && k < patterns.length-1) {
                                            regExp.append("})[\\.](\\d{1,");
                                        }
                                    }
                                    regExp.append("})$");

                                    if (diagData.matches(regExp.toString())) {
                                        isMatch = true;
                                        break;
                                    }
                                }

                                if (!isMatch) {
                                    String result = "";
                                    for (String expStr : regExpList) {
                                        result = fixedData(expStr, diagData, "\\.", ".");

                                        if (!result.isEmpty())
                                            break;
                                    }

                                    DiagnosisFixedDataVO fixedDataVO = new DiagnosisFixedDataVO();
                                    fixedDataVO.setId(resultVO.getId());
                                    fixedDataVO.setHistoryId(historyId);
                                    fixedDataVO.setRuleSetId(i+1);
                                    fixedDataVO.setRowNum(rowNum);
                                    fixedDataVO.setFileId(fileId);
                                    fixedDataVO.setOrigin(diagData);
                                    if (!result.isEmpty()) {
                                        fixedDataVO.setFixed(result);
                                        fixedDataVO.setFixedMsg("숫자 형식 정정 예)" + ruleSetVO.getExample());
                                        fixedDataVO.setFixedYn("Y");
                                        f[i].set(resultVO, result);
                                    }
                                    else {
                                        fixedDataVO.setFixedYn("N");
                                        fixedDataVO.setErrorMsg("숫자 형식 오류 예)" + ruleSetVO.getExample());
                                    }
                                    isError = true;
                                    diagnosisFixedDataVOList.add(fixedDataVO);
                                    continue;
                                }
                            }
                        }
                        else {
                            Boolean isMatch = false;

                            if (ruleSetVO.getStringPattern().contains("-")) {
                                String[] patterns = ruleSetVO.getStringPattern().split("-");

                                StringBuffer regExp = new StringBuffer("^(\\d{1,");
                                for (int k = 0; k < patterns.length; k++) {
                                    regExp.append(patterns[k].length());
                                    if (patterns.length > 0 && k < patterns.length-1) {
                                        regExp.append("})[\\-](\\d{1,");
                                    }
                                }
                                regExp.append("})$");

                                if (diagData.matches(regExp.toString())) {
                                    isMatch = true;
                                }

                                if (!isMatch) {
                                    String result = fixedData(ruleSetVO.getStringPattern(), diagData, "-", "-");
                                    DiagnosisFixedDataVO fixedDataVO = new DiagnosisFixedDataVO();
                                    fixedDataVO.setId(resultVO.getId());
                                    fixedDataVO.setHistoryId(historyId);
                                    fixedDataVO.setRuleSetId(i+1);
                                    fixedDataVO.setRowNum(rowNum);
                                    fixedDataVO.setFileId(fileId);
                                    fixedDataVO.setOrigin(diagData);
                                    if (!result.isEmpty()) {
                                        fixedDataVO.setFixed(result);
                                        fixedDataVO.setFixedMsg("번호 헝식 정정 예)" + ruleSetVO.getExample());
                                        fixedDataVO.setFixedYn("Y");
                                        f[i].set(resultVO, result);
                                    }
                                    else {
                                        fixedDataVO.setFixedYn("N");
                                        fixedDataVO.setErrorMsg("번호 형식 오류 예)" + ruleSetVO.getExample());
                                    }
                                    isError = true;
                                    diagnosisFixedDataVOList.add(fixedDataVO);
                                }
                            }

                            if (ruleSetVO.getStringPattern().contains(":")) {
                                String[] patterns = ruleSetVO.getStringPattern().split(":");

                                StringBuffer regExp = new StringBuffer("^(\\d{");
                                for (int k = 0; k < patterns.length; k++) {
                                    regExp.append(patterns[k].length());
                                    if (patterns.length > 0 && k < patterns.length-1) {
                                        regExp.append("})[\\:](\\d{");
                                    }
                                }
                                regExp.append("})$");

                                if (diagData.matches(regExp.toString())) {
                                    isMatch = true;
                                }

                                if (!isMatch) {
                                    String[] token = diagData.split(":");
                                    String result = "";
                                    if (token[0].length() == 1) {
                                        token[0] = "0" + token[0];
                                        result = token[0] + ":" + token[1];
                                    }

                                    DiagnosisFixedDataVO fixedDataVO = new DiagnosisFixedDataVO();
                                    fixedDataVO.setId(resultVO.getId());
                                    fixedDataVO.setHistoryId(historyId);
                                    fixedDataVO.setRuleSetId(i+1);
                                    fixedDataVO.setRowNum(rowNum);
                                    fixedDataVO.setFileId(fileId);
                                    fixedDataVO.setOrigin(diagData);
                                    if (result.matches(regExp.toString())) {
                                        fixedDataVO.setFixed(result);
                                        fixedDataVO.setFixedMsg("시간 헝식 정정 예)" + ruleSetVO.getExample());
                                        fixedDataVO.setFixedYn("Y");
                                        f[i].set(resultVO, result);
                                    }
                                    else {
                                        fixedDataVO.setFixedYn("N");
                                        fixedDataVO.setErrorMsg("시간 형식 오류 예)" + ruleSetVO.getExample());
                                    }
                                    isError = true;
                                    diagnosisFixedDataVOList.add(fixedDataVO);
                                }
                            }

                            if (ruleSetVO.getStringPattern().contains(".")) {
                                String[] patterns = ruleSetVO.getStringPattern().split("\\.");

                                StringBuffer regExp = new StringBuffer("^(\\d{1,");
                                for (int k = 0; k < patterns.length; k++) {
                                    regExp.append(patterns[k].length());
                                    if (patterns.length > 0 && k < patterns.length-1) {
                                        regExp.append("})[\\.](\\d{1,");
                                    }
                                }
                                regExp.append("})$");

                                if (diagData.matches(regExp.toString())) {
                                    isMatch = true;
                                }

                                if (!isMatch) {
                                    String result = fixedData(ruleSetVO.getStringPattern(), diagData, "\\.", ".");

                                    DiagnosisFixedDataVO fixedDataVO = new DiagnosisFixedDataVO();
                                    fixedDataVO.setId(resultVO.getId());
                                    fixedDataVO.setHistoryId(historyId);
                                    fixedDataVO.setRuleSetId(i+1);
                                    fixedDataVO.setRowNum(rowNum);
                                    fixedDataVO.setFileId(fileId);
                                    fixedDataVO.setOrigin(diagData);
                                    if (!result.isEmpty()) {
                                        fixedDataVO.setFixed(result);
                                        fixedDataVO.setFixedMsg("숫자 헝식 정정 예)" + ruleSetVO.getExample());
                                        fixedDataVO.setFixedYn("Y");
                                        f[i].set(resultVO, result);
                                    }
                                    else {
                                        fixedDataVO.setFixedYn("N");
                                        fixedDataVO.setErrorMsg("숫자 형식 오류 예)" + ruleSetVO.getExample());
                                    }
                                    isError = true;
                                    diagnosisFixedDataVOList.add(fixedDataVO);
                                }
                            }

                            if (ruleSetVO.getStringPattern().contains("*")) {

                                String regExp = "^[0-9.]+$";
                                if (diagData.matches(regExp)) {
                                    isMatch = true;
                                }

                                if (!isMatch) {
                                    String delimiter = "[ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z*.,:-^-$()!@#/%&]";
                                    String fixData = diagData.trim().replaceAll(delimiter,"");

                                    DiagnosisFixedDataVO fixedDataVO = new DiagnosisFixedDataVO();
                                    fixedDataVO.setId(resultVO.getId());
                                    fixedDataVO.setHistoryId(historyId);
                                    fixedDataVO.setRuleSetId(i+1);
                                    fixedDataVO.setRowNum(rowNum);
                                    fixedDataVO.setFileId(fileId);
                                    fixedDataVO.setOrigin(diagData);
                                    if (StringUtils.isNumeric(fixData)) {
                                        fixedDataVO.setFixed(fixData);
                                        fixedDataVO.setFixedMsg("숫자 헝식 정정 예)" + ruleSetVO.getExample());
                                        fixedDataVO.setFixedYn("Y");
                                        f[i].set(resultVO, fixData);
                                    }
                                    else {
                                        fixedDataVO.setFixedYn("N");
                                        fixedDataVO.setErrorMsg("숫자 형식 오류 예)" + ruleSetVO.getExample());
                                    }
                                    isError = true;
                                    diagnosisFixedDataVOList.add(fixedDataVO);
                                }
                            }

                            if (ruleSetVO.getStringPattern().contains("+")) {

                                String regExp = "^[0-9+]+$";
                                if (diagData.matches(regExp)) {
                                    isMatch = true;
                                }

                                if (!isMatch) {
                                    DiagnosisFixedDataVO fixedDataVO = new DiagnosisFixedDataVO();
                                    fixedDataVO.setId(resultVO.getId());
                                    fixedDataVO.setHistoryId(historyId);
                                    fixedDataVO.setRuleSetId(i+1);
                                    fixedDataVO.setRowNum(rowNum);
                                    fixedDataVO.setFileId(fileId);
                                    fixedDataVO.setOrigin(diagData);
                                    fixedDataVO.setFixedMsg("복수 표기 오류)" + ruleSetVO.getExample());
                                    fixedDataVO.setFixedYn("Y");
                                    isError = true;
                                    diagnosisFixedDataVOList.add(fixedDataVO);
                                }
                            }
                        }

                    }
                }
            }

            diagnosisResultDataList.add(resultVO);
        }

        if (!diagnosisFixedDataVOList.isEmpty()) {
            for (DiagnosisFixedDataVO vo : diagnosisFixedDataVOList) {
                diagnosisHistoryDAO.insertDiagnosisFixedHistory(vo);
            }
        }

        List<DiagnosisDataVO> dupChkList = new ArrayList();

        if (!diagnosisResultDataList.isEmpty()) {

            if (paramVO.getDupChkYn()) {

                for (int i = 0; i < diagnosisResultDataList.size(); i++) {
                    DiagnosisDataVO loopVo1 = diagnosisResultDataList.get(i);
                    Boolean isDup = false;

                    for (int j = i + 1; j < diagnosisResultDataList.size(); j++) {
                        DiagnosisDataVO loopVo2 = diagnosisResultDataList.get(j);
                        if (loopVo1.equals(loopVo2)) {
                            isDup = true;
                            loopVo1.setDupDelYn("Y");
                        }
                    }

                    if (!isDup) {
                        dupChkList.add(loopVo1);
                    }
                }
            }
            else {
                dupChkList = diagnosisResultDataList;
            }

            for (DiagnosisDataVO vo : diagnosisResultDataList) {
                diagnosisHistoryDAO.insertDiagnosisResult(vo);
            }

        }

        Map resultMap = new HashMap();
        String fixedFileName = "";
        if (isError || dupChkList.size() > 0) {
            fixedFileName = fileService.writeData(paramVO, dupChkList, diagnosisRuleSetVOList);
            isError = true;
            resultMap.put("isError", "Y");
            resultMap.put("fixedFileName", fixedFileName);
        }

        DiagnosisFileHistoryVO historyVO = new DiagnosisFileHistoryVO();
        historyVO.setHistoryId(historyId);
        historyVO.setFileId(fileId);
        historyVO.setErrorYn(isError ? "Y" : "N");
        historyVO.setStatus("진단완료");
        historyVO.setFixedFile(fixedFileName);
        diagnosisHistoryDAO.updateFileHistory(historyVO);

        return resultMap;
    }

    private String fixedData(String pattern, String source, String delimiter, String joinChar) {

        String returnString = "";

        delimiter = "[^ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9]";

        if (pattern.replaceAll(delimiter,"").length() ==
                source.replaceAll(delimiter,"").length()) {

            List<String> fixItemList = new ArrayList<String>();
            String[] itemArray = pattern.split(delimiter);
            source = source.replaceAll(delimiter,"");
            int min = 0, max = 0;

            for (int i =0; i < itemArray.length; i++) {
                max += itemArray[i].length();
                fixItemList.add(source.substring(min, max));
                min = max;
            }
            returnString = fixItemList.stream().collect(Collectors.joining(joinChar));
        }
        else {
            returnString = "";
        }
        return returnString;
    }

    public void writeFixedData(ParamVO paramVO) {
        DataFileService dataFileService = fileServiceFactory.getFileService("csv");
        dataFileService.writeFixedData(paramVO);
    }
}