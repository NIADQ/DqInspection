package application.wisefile.service.impl;

import application.wisefile.Main;
import application.wisefile.common.Const;
import application.wisefile.dao.DiagnosisHistoryDAO;
import application.wisefile.service.DataFileService;
import application.wisefile.util.FileUtil;
import application.wisefile.vo.*;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;


public class ExcelFileService implements DataFileService {
	protected Logger logger = Logger.getLogger(this.getClass());

    private final DiagnosisHistoryDAO diagnosisHistoryDAO;

	public ExcelFileService() {
		diagnosisHistoryDAO = Main.context.getBean(DiagnosisHistoryDAO.class);
	}

	@Override
	public Integer syncRuleSet() {
		List<DiagnosisRuleSetVO> contentList = new ArrayList<>();
		List<DiagnosisFileVO> fileList = new ArrayList<>();

		try (InputStream  is = new FileInputStream(Const.RULE_SET_FILE)) {

			Workbook workbook = WorkbookFactory.create(is);

			Sheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowItr = sheet.iterator();
			while(rowItr.hasNext()) {

				ArrayList<String> list = new ArrayList<>();
				Row row = rowItr.next();
				if(row.getRowNum() == 0) {
					continue;
				}

				for (int i = 0; i < row.getLastCellNum(); i++) {
					Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
					DataFormatter formatter = new DataFormatter();
					list.add(formatter.formatCellValue(cell));
				}

				Field[] f = DiagnosisRuleSetVO.class.getDeclaredFields();
				DiagnosisRuleSetVO vo = new DiagnosisRuleSetVO();

				for (int i = 0; i < list.size(); i++) {
					f[i].setAccessible(true);
					if (f[i].getType().getName().equals("java.lang.Integer"))
						f[i].set(vo, Integer.parseInt(list.get(i)));
					else
						f[i].set(vo, list.get(i));
				}

				contentList.add(vo);
			}

			Integer deleteCount = diagnosisHistoryDAO.deleteRuleSet();

			if (deleteCount >= 0 && !contentList.isEmpty()) {
				for (DiagnosisRuleSetVO vo : contentList) {
					diagnosisHistoryDAO.insertRuleSet(vo);
				}
			}

			Sheet fileSheet = workbook.getSheetAt(1);
			Iterator<Row> fileRowItr = fileSheet.iterator();

			while(fileRowItr.hasNext()) {

				ArrayList<String> list = new ArrayList<>();
				Row row = fileRowItr.next();
				if(row.getRowNum() == 0) {
					continue;
				}

				for (int i = 0; i < row.getLastCellNum(); i++) {
					Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
					DataFormatter formatter = new DataFormatter();
					list.add(formatter.formatCellValue(cell));
				}

				Field[] f = DiagnosisFileVO.class.getDeclaredFields();
				DiagnosisFileVO vo = new DiagnosisFileVO();

				for (int i = 0; i < list.size(); i++) {
					f[i].setAccessible(true);
					if (f[i].getType().getName().equals("java.lang.Integer"))
						f[i].set(vo, Integer.parseInt(list.get(i)));
					else
						f[i].set(vo, list.get(i));
				}

				fileList.add(vo);
			}

			Integer fileDelCount = diagnosisHistoryDAO.deleteFile();

			if (fileDelCount >= 0 && !fileList.isEmpty()) {
				for (DiagnosisFileVO vo : fileList) {
					diagnosisHistoryDAO.insertFile(vo);
				}
			}

		}
		catch (Exception e) {
			logger.error(e.getMessage());
			throw new RuntimeException("진단 룰셋 동기화 중오류가 발생했습니다.");
		}

		return 0;
	}

	@Override
	public List<DiagnosisDataVO> readData(ParamVO paramVO, int colCount) {
	        List<DiagnosisDataVO> contentList = new ArrayList<>();

	        try (InputStream  is = new FileInputStream(paramVO.getFileName());)
	        {
	    	    Workbook workbook = WorkbookFactory.create(is);
	    	    
	    	    Sheet sheet = workbook.getSheetAt(0);
	    	    Iterator<Row> rowItr = sheet.iterator();
	    	    while(rowItr.hasNext()) {
	    	    	ArrayList<String> list = new ArrayList<>();
	    	        Row row = rowItr.next();
	    	        if(row.getRowNum() == 0) {
	    	            continue;
	    	        }
	    	        Iterator<Cell> cellItr = row.cellIterator();
	    	        while(cellItr.hasNext()) {
	    	            Cell cell = cellItr.next();
	    	            DataFormatter formatter = new DataFormatter();
	    	            	 list.add(formatter.formatCellValue(cell));
	    	        }
	    	        if (list.size() != colCount)
	                    throw new RuntimeException("제공표준과 파일 형식이 일치하지 않습니다.");
	    	        
	    	        Field[] f = DiagnosisDataVO.class.getDeclaredFields();
	                DiagnosisDataVO vo = new DiagnosisDataVO();

	                for (int i = 0; i < list.size(); i++) {
	                    f[i].setAccessible(true);
	                    f[i].set(vo, list.get(i));
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

	        } catch (Exception e) {
	            logger.error(e.getMessage());
	            throw new RuntimeException("파일을 읽는 중에 오류가 발생했습니다.");
	        }
	        return contentList;
	}
	

	@Override
	public String writeData(ParamVO paramVO, List<DiagnosisDataVO> result, List<DiagnosisRuleSetVO> ruleSetVOList) {

		String fileName = FileUtil.getFileName(paramVO, "xlsx");
        String[] header = ruleSetVOList.stream().map(DiagnosisRuleSetVO::getColumnName).toArray(String[]::new);
        
        SXSSFWorkbook wb = new SXSSFWorkbook();
        Sheet sh = wb.createSheet();
	    
        Row row = sh.createRow(0);
        int cellnum = 0;

        Cell cell = row.createCell(cellnum);
        
        List<String> headers = Arrays.asList(header);
        
        for (String head : headers) {
            cell.setCellValue(head);
            cell = row.createCell(++cellnum);
            
        }
        Field[] f = DiagnosisDataVO.class.getDeclaredFields();
        int rownum = 1;
		DataFormat fmt = wb.createDataFormat();
		CellStyle cellStyle = wb.createCellStyle();
		cellStyle.setDataFormat(fmt.getFormat("@"));
        try{
            for (DiagnosisDataVO p : result) {
                String[] arr = new String[ruleSetVOList.size()];
                row = sh.createRow(rownum);
                for (int i = 0; i < arr.length; i++) {
                	f[i].setAccessible(true);
                	cell = row.createCell(i);
					cell.setCellStyle(cellStyle);
                	cell.setCellValue(f[i].get(p).toString());
                }
                rownum++;
            }
            
            FileOutputStream fileoutputstream=null;
			try {
				fileoutputstream = new FileOutputStream(Const.FIXED_FILE_PATH + fileName);
				wb.write(fileoutputstream);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					fileoutputstream.close();
					wb.close();
					
				} catch (IOException e) {
					 logger.error(e.getMessage());
			         throw new RuntimeException("정정파일 생성 중 오류가 발생했습니다.");
				}
				
			}
        } catch (IllegalAccessException e) {
            throw new RuntimeException("정정파일 생성 중 오류가 발생했습니다.");
        }

        return fileName;
	}


	@Override
	public void writeFixedData(ParamVO paramVO) {
		
		logger.info(":::::::::::::::::::::::::: EXCEL DOWNLOAD START ::::::::::::::::::::::::::");

        DiagnosisFileHistoryVO fileVO = diagnosisHistoryDAO.selectFileHistory(paramVO);

        List<DiagnosisRuleSetVO> ruleSetVOList = diagnosisHistoryDAO.selectFileRuleSet(paramVO.getFileId());
        List<Map> result = diagnosisHistoryDAO.selectDiagnosisResultData(paramVO);

        String[] header = ruleSetVOList.stream().map(DiagnosisRuleSetVO::getColumnName).toArray(String[]::new);

        SXSSFWorkbook wb = new SXSSFWorkbook();
        Sheet sh = wb.createSheet();
	    
        Row row = sh.createRow(0);
        int cellnum = 0;

        Cell cell = row.createCell(cellnum);
        
        List<String> headers = Arrays.asList(header);
        
        for (String head : headers) {
            cell.setCellValue(head);
            cell = row.createCell(++cellnum);
        }
        
        int rownum = 1;
        for (Map p : result) {
			String[] arr = new String[ruleSetVOList.size()];
		    row = sh.createRow(rownum);
		    for (int i = 0; i < arr.length; i++) {
		    	cell = row.createCell(i);
		    	cell.setCellValue(p.get("col"+(i+1)).toString());
		    }
		    rownum++;
		}
		FileOutputStream fileoutputstream=null;
		try {
			fileoutputstream = new FileOutputStream(FileUtil.getFileName(paramVO, "xlsx"));
			wb.write(fileoutputstream);
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fileoutputstream.close();
				wb.close();
				
			} catch (IOException e) {
				 logger.error(e.getMessage());
		          throw new RuntimeException("정정파일 생성 중 오류가 발생했습니다.");
			}
			
		}
	}

}
