package application.wisefile.util;

import application.wisefile.common.Const;
import application.wisefile.vo.ParamVO;
import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;
import org.apache.log4j.Logger;
import org.mozilla.universalchardet.UniversalDetector;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FileUtil {
	private Logger logger = Logger.getLogger(this.getClass());

	public String getFileCharset(String p) {
		String charset = "ISO-8859-1";
		FileInputStream fin = null;
		try {
			File file = new File(p);

			byte[] fileContent = null;
			fin = new FileInputStream(file.getPath());
			fileContent = new byte[(int) file.length()];
			fin.read(fileContent);

			byte[] data =  fileContent;

			CharsetDetector detector = new CharsetDetector();
			detector.setText(data);

			CharsetMatch cm = detector.detect();

			if (cm != null) {
			    int confidence = cm.getConfidence();
				logger.debug("Encoding: " + cm.getName() + " - Confidence: " + confidence + "%");

			    if (confidence > 50) {
			        charset = cm.getName();
			    }
			}		
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fin != null) { fin.close(); fin = null; }
			} catch (Exception se) {
				se.printStackTrace();
			}
		}

		logger.debug("File-Charset:"+charset);
		return charset;
	}

	public String findFileEncoding(String p) {
		
		File file = new File(p);
	    byte[] buf = new byte[4096];
	    String encoding = null;
	    java.io.FileInputStream fis = null;
	    
		try {
			
			fis = new java.io.FileInputStream(file);
		    UniversalDetector detector = new UniversalDetector(null);
		 
		    int nread;
		    while ((nread = fis.read(buf)) > 0 && !detector.isDone()) {
		      detector.handleData(buf, 0, nread);
		    }
		    detector.dataEnd();
		 
		    encoding = detector.getDetectedCharset();
		    if (encoding != null) {
				logger.debug("UniversalDetector Detected encoding = " + encoding);
		    } else {
				logger.debug("UniversalDetector No encoding detected.");
		    }
		 
		    detector.reset();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fis != null) { fis.close(); fis = null; }
			} catch (Exception se) {
				se.printStackTrace();
			}
		}
		logger.debug("File-Charset encoding:"+encoding);
	    return encoding;
	}


	public String findFileCRLF(String p) {

		int i,cnt; 
		int win_cnt = 0;
		int unix_cnt = 0;
		int mac_cnt = 0;
		String rtnOs ="";
		String str = null;
		InputStream is = null; 
		try {
			is = new FileInputStream(p); 	
			StringBuffer buffer = new StringBuffer(); 
			byte[] b = new byte[4096]; 
			cnt=0;
			while( (i = is.read(b)) != -1){ 
				buffer.append(new String(b, 0, i));
				cnt++;
				if(cnt>5) break;
			} 
			/****************************************
			 *  1. 윈도우 : CRLF(\r\n)
				2. 유닉스 : LF(\n)
				3. 맥 : CR(\r)
			 * *******************/
			 str = buffer.toString();
		        if(str.indexOf("\r\n") != -1) { /*\\\r\\\n*/
		        	 System.out.println("CRLF 존재........................WINDOWS");
		        	 rtnOs="WIN";
		        } else if (str.indexOf("\n") != -1) {
		        	 System.out.println("LF 존재 .......................UNIX");
		        	 rtnOs="UNIX";
		        } else if (str.indexOf("\r") != -1) {
		        	 System.out.println("CR 존재 .......................MAC");
		        	 rtnOs="MAC";
		        }
		        /*****************************/
		        String strFind = "\r\n";
		        String strF = "win";
		        
		        for (int j=0;j<3;j++) {
			        if (j==0) {
			        	 strFind = "\r\n";
			        	 strF = "win";
			        } else if 	(j==1) {
			        	 strFind = "\n";
			        	 strF = "unix";
			        } else if 	(j==2) {
			        	 strFind = "\r";
			        	 strF = "mac";
			        }
			        	
			        int count = 0, fromIndex = 0;
			        
			        while ((fromIndex = str.indexOf(strFind, fromIndex)) != -1 ){
			            count++;
			            fromIndex++;
			        }
		        
			        if (j==0) {
			        	 win_cnt = count;
			        	 strF = "win";
			        } else if 	(j==1) {
			        	unix_cnt = count;
			        	 strF = "unix";
			        } else if 	(j==2) {
			        	mac_cnt = count;
			        	 strF = "mac";
			        }
		        }
		        if (win_cnt == unix_cnt && unix_cnt == mac_cnt) {
		        	logger.debug(" CRLF"+win_cnt+"건 존재........................WINDOWS");
		        	rtnOs="WIN";
		        } else {
			        if(win_cnt > unix_cnt) {
			        	if(win_cnt > mac_cnt) {
			        		logger.debug(" CRLF"+win_cnt+"건 존재........................WINDOWS");
			        	 	rtnOs="WIN";
			        	} else {
			        		logger.debug(" CR"+mac_cnt+"건 존재........................MAC");
			        		rtnOs="MAC";
			        	}
			        } else  {
			        	if(unix_cnt > mac_cnt) {
			        		logger.debug(" LF"+unix_cnt+"건 존재........................UNIX");
			        	 	rtnOs="UNIX";
			        	} else {
			        		logger.debug(" CR"+mac_cnt+"건 존재........................MAC");
			        		rtnOs="MAC";
			        	}
			        	 
			        }
		        }
		        /*****************************/
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		} finally {
			try {
				if (is != null) { is.close(); is = null; }
			} catch (Exception se) {
				se.printStackTrace();
			}
		}
		logger.debug("File-Charset encoding rtnOsrtnOs:"+rtnOs);
	    return rtnOs;
	}

	/**
	 * 정정파일명 생성
	 * @param paramVO
	 * @param ext : 확장자
	 * @return
	 */
	public static String getFileName(ParamVO paramVO, String ext) {

		File file = new File(Const.FIXED_FILE_PATH);
		if (!file.isDirectory()) {
			file.mkdir();
		}

		LocalDate now = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		return paramVO.getOrganName() + "_" + paramVO.getFileDesc() + "_" + now.format(formatter) + "."+ ext;
	}

}
