package cn.csl.basics.util;


import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.Region;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class ExcelHandle {

	private static ExcelHandle instance;

	/**
	 * excel流输出
	 * @param sheets
	 * @param response
	 * @param filename
	 */
	public static void ExcelIoDownload(HSSFWorkbook sheets,HttpServletResponse response,String filename){
		javax.servlet.ServletOutputStream sos = null;
		try {
			sos = response.getOutputStream();
			response.reset(); // 必要地清除response中的缓存信息
			response.setContentType("application/x-msdownload");
			response.setHeader("Content-Disposition", "attachment; filename=".concat(new String(filename.getBytes("gb2312"),"iso8859-1")));
            sheets.write(sos);//导出excel白件
            sos.flush();
            sos.close();//封闭输出
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (sos!=null) {
				try {
					sos.close();
				} catch (IOException e) {}
			}
		}
	}
	public static void writeStream(String filename, HSSFWorkbook wb, HttpServletResponse response, HttpServletRequest request) {

		try
		{
			String agent = request.getHeader("USER-AGENT");

			filename += ".xls";

			filename.replaceAll("/", "-");
			// filename = new String(filename.getBytes("gbk"),"ISO8859_1");
			

			if (agent.toLowerCase().indexOf("firefox")>0)
			{
				filename = new String(filename.getBytes("utf-8"), "iso-8859-1");
			}else{
				filename = URLEncoder.encode(filename, "UTF-8");
			}

			response.reset();
			response.setCharacterEncoding("UTF-8");
			response.setHeader("Content-Disposition", "attachment; filename=" + filename);
			response.setContentType("application/octet-stream;charset=UTF-8");
			OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
			wb.write(outputStream);
			outputStream.flush();
			outputStream.close();

		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}catch (Exception e)
		{
			e.printStackTrace();
		}

	}
	public static ExcelHandle getInstance() {
		synchronized (ExcelHandle.class) {
			if (instance == null) {
				instance = new ExcelHandle();
			}
		}
		return instance;
	}
	public static StyleUtil getStyleUtil() {
		return getInstance().new StyleUtil();
	}
	public class StyleUtil {
		
		private Map<Short, HSSFCellStyle> erpStyleMap;
		private Map<Short, HSSFCellStyle> styleMap;
		private Map<String, HSSFFont> fontMap;
		private Map<String, HSSFCellStyle> dataMap;
		private Map<String, HSSFCellStyle> erpDataMap;
		

		public StyleUtil() {
			erpStyleMap = new HashMap<Short, HSSFCellStyle>();
			styleMap = new HashMap<Short, HSSFCellStyle>();
			fontMap = new HashMap<String, HSSFFont>();
			dataMap = new HashMap<String, HSSFCellStyle>();
			erpDataMap = new HashMap<String, HSSFCellStyle>();
		}
		/**
		 * 获取一个workbook样式
		 * @param book
		 * @param color
		 * @return
		 */
		public HSSFCellStyle getStyle(HSSFWorkbook book, Short color) {
			HSSFCellStyle style;
			if (styleMap.containsKey(color)) {
				style = styleMap.get(color);
			} else {
				style = book.createCellStyle();
				styleMap.put(color, style);
			}
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER);//左右居中   
			style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//上下居中 
			style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			style.setBorderRight(HSSFCellStyle.BORDER_THIN);
			style.setBorderTop(HSSFCellStyle.BORDER_THIN);
			style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			if (null == color) {
				color = IndexedColors.WHITE.getIndex();
			}
			style.setWrapText(true);
			style.setFillForegroundColor(color);
			return style;
		}
		/**
		 * 从workbook获取一个字体样式
		 * @param book
		 * @param fontName
		 * @param fontSize
		 * @return
		 */
		public HSSFFont getFont(HSSFWorkbook book, String fontName,
				Short fontSize) {
			HSSFFont font;
			if (fontName==null||"".equals(fontName.trim())) {
				fontName = "宋体";
			}
			if (null==fontSize || fontSize <= 0) {
				fontSize = 10;
			}
			String key = fontName+fontSize;
			if (fontMap.containsKey(key)) {
				font = fontMap.get(key);
			} else {
				font = book.createFont();
				fontMap.put(key, font);
			}
			font.setFontName(fontName);
			font.setFontHeightInPoints(fontSize);
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			return font;
		}
		
		/**
		 * 设置一个单元格类型
		 * @param book
		 * @param format
		 * @return
		 */
		public HSSFCellStyle getDataFormat(HSSFWorkbook book,String format){
			return getDataFormat(book,null,format);
		}
		
		/**
		 * 设置一个单元格类型
		 * @param book
		 * @param color
		 * @param format
		 * @return
		 */
		public HSSFCellStyle getDataFormat(HSSFWorkbook book, Short color,String format){
			HSSFCellStyle style;
			if (dataMap.containsKey(color+format)) {
				style = dataMap.get(color+format);
			} else {
				style = book.createCellStyle();
				dataMap.put(color+format, style);
			}
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER);//左右居中   
			style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//上下居中 
			style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			style.setBorderRight(HSSFCellStyle.BORDER_THIN);
			style.setBorderTop(HSSFCellStyle.BORDER_THIN);
			style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			if (null == color) {
				color = IndexedColors.WHITE.getIndex();
			}
			style.setFillForegroundColor(color);
			HSSFDataFormat dataFormat= book.createDataFormat();;
			style.setDataFormat(dataFormat.getFormat(format));
			return style;
		}
		/**
		 * 设置一个单元格类型 
		 * 		百分数
		 * @param book
		 * @param color
		 * @param format
		 * @return
		 */
		public HSSFCellStyle getDataFormat1(HSSFWorkbook book, Short color,String format){
			HSSFCellStyle style;
			if (dataMap.containsKey(color+format)) {
				style = dataMap.get(color+format);
			} else {
				style = book.createCellStyle();
				dataMap.put(color+format, style);
			}
			style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			style.setBorderRight(HSSFCellStyle.BORDER_THIN);
			style.setBorderTop(HSSFCellStyle.BORDER_THIN);
			style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			if (null == color) {
				color = IndexedColors.WHITE.getIndex();
			}
			style.setFillForegroundColor(color);
			style.setDataFormat((short)BuiltinFormats.getBuiltinFormat("0.00%"));
			return style;
		}
		
		/**
		 * 设置一个单元格类型 
		 * 		保留两位小数
		 * @param book
		 * @param color
		 * @param format
		 * @return
		 */
		public HSSFCellStyle getDataDoubleFormat(HSSFWorkbook book, Short color,String format){
			HSSFCellStyle style;
			if (dataMap.containsKey(color+format)) {
				style = dataMap.get(color+format);
			} else {
				style = book.createCellStyle();
				dataMap.put(color+format, style);
			}
			style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			style.setBorderRight(HSSFCellStyle.BORDER_THIN);
			style.setBorderTop(HSSFCellStyle.BORDER_THIN);
			style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			if (null == color) {
				color = IndexedColors.WHITE.getIndex();
			}
			style.setFillForegroundColor(color);
			style.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
			return style;
		}
		
		/**
		 * 获取单元格对象
		 * @param sheet
		 * @param rowIndex
		 * @param cellIndex
		 * @return
		 */
		public HSSFCell getCell(HSSFSheet sheet, int rowIndex, int cellIndex) {
			HSSFRow row = sheet.getRow(rowIndex);
			if(row==null){
				row=sheet.createRow(rowIndex);
			}
			HSSFCell cell= row.getCell(cellIndex);
			if(cell==null){
				cell=row.createCell(cellIndex);
			}
			return cell;
		}
		/**
		 * 给一个单元格添加样式
		 * @param sheet
		 * @param rowIndex
		 * @param colIndex
		 * @param colorIndex
		 */
		public void setCellBackgroundColor(HSSFSheet sheet, int rowIndex,
				int colIndex, Short colorIndex) {
			HSSFCell cell = getCell(sheet, rowIndex, colIndex);
			cell.setCellStyle(getStyle(sheet.getWorkbook(), colorIndex));
		}
		/**
		 * 给多个单元格添加样式
		 * @param sheet
		 * @param rowIndex
		 * @param colIndexStart
		 * @param colIndexEnd
		 * @param colorIndex
		 */
		public void setCellsBackgroundColor(HSSFSheet sheet, int rowIndex,
				int colIndexStart, int colIndexEnd, Short colorIndex) {
			for (int i = colIndexStart; i < colIndexEnd; i++) {
				setCellBackgroundColor(sheet, rowIndex, i, colorIndex);
			}
		}
		public HSSFCell setCellValue(HSSFSheet sheet, int rowIndex, int colIndex,Object value,int typeValue){
			HSSFCell cell = getCell(sheet, rowIndex, colIndex);
			if (null!=value) {
				switch (typeValue) {
				case 0:
					cell.setCellValue(Double.parseDouble(value.toString()));
					break;
				case 1:
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue(value.toString());
					break;
				case 2:
					cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					cell.setCellValue(Long.valueOf(value.toString()));
					break;
				case 3:
					cell.setCellValue((Date)value);
					break;
				default:
					cell.setCellValue(value.toString());
					break;
				}
			}
			return cell;
		}
		/**
		 * 写入值、背景颜色、公式、字体、字体大小
		 * @param sheet    设定sheet
		 * @param rowIndex 设定sheet的行
		 * @param colIndex 设定sheet的列
		 * @param value    输出单元格中的值
		 * @param cellForumla
		 * @param type
		 * @param colorIndex
		 * @param fontName
		 * @param fontSize
		 */
		public void setCellValue(HSSFSheet sheet, int rowIndex, int colIndex,
				Object value, String cellForumla, int type, Short colorIndex,
				String fontName, Short fontSize) {
			HSSFCell cell = setCellValue(sheet, rowIndex, colIndex, value, type);
			if (null != cellForumla) {
				cell.setCellFormula(cellForumla);
			}
			HSSFFont font = getFont(sheet.getWorkbook(), fontName, fontSize);
			HSSFCellStyle cellStyle = getStyle(sheet.getWorkbook(), colorIndex);
			cellStyle.setFont(font);
			cell.setCellStyle(cellStyle);
		}
		

		public void setCellValue(HSSFCellStyle cellStyle, HSSFSheet sheet,
				int rowIndex, int colIndex, Object value,int typeValue) {
			HSSFCell cell = setCellValue(sheet, rowIndex, colIndex, value, typeValue);
			cell.setCellStyle(cellStyle);
		}
		
		public void setCellFormula(HSSFSheet sheet,	int rowIndex, int colIndex,String formula) {
			HSSFCell cell = getCell(sheet, rowIndex, colIndex);
			cell.setCellFormula(formula);
		}
		public void setCellBorder(HSSFSheet sheet, int rowIndex,
				int colIndex,Object value,int type, Short colorIndex) {
			HSSFCell cell = setCellValue(sheet, rowIndex, colIndex, value, type);
			cell.setCellStyle(getStyle(sheet.getWorkbook(), colorIndex));
		}
		/**
		 * 为ERP样式出库准备
		 * 设置单元格指定样式和颜色
		 * @param cellStyle
		 * @param sheet
		 * @param rowIndex
		 * @param colIndex
		 * @param value
		 * @param type
		 * @param colorIndex
		 */
		public void setERPCellStyle(HSSFCellStyle cellStyle,HSSFSheet sheet, int rowIndex,
				int colIndex,Object value,int type, Short colorIndex) {
			HSSFCell cell = setCellValue(sheet, rowIndex, colIndex, value, type);
			if(null==colorIndex){
				colorIndex = IndexedColors.WHITE.getIndex();
			}
			cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStyle.setFillForegroundColor(colorIndex);
			
			cell.setCellStyle(cellStyle);
			
		}
		
		public void setCellFormat(HSSFSheet sheet, int rowIndex,int cellIndex,String format){
			HSSFCell cell = getCell(sheet, rowIndex, cellIndex);
			HSSFCellStyle cellStyle = this.getDataFormat(sheet.getWorkbook(), format);
			cell.setCellStyle(cellStyle);
		}
		
		public void setCellDate(HSSFSheet sheet, int rowIndex,
				int colIndex,Object value,Short colorIndex,String format){
			HSSFCell cell = setCellValue(sheet, rowIndex, colIndex, value, 3);
			HSSFCellStyle cellStyle = this.getDataFormat(sheet.getWorkbook(), colorIndex, format);
			cell.setCellStyle(cellStyle);
		}
		
		public void setCellFormData(HSSFSheet sheet, int rowIndex,
				int colIndex,Object value,Short colorIndex,String format,Integer valueType){
			HSSFCell cell = setCellValue(sheet, rowIndex, colIndex, value, valueType);
			HSSFCellStyle cellStyle = this.getDataFormat1(sheet.getWorkbook(), colorIndex, format);
			cell.setCellStyle(cellStyle);
		}
		
		/**
		 * 设置单元格格式保留两位小数
		 * @param sheet
		 * @param rowIndex
		 * @param colIndex
		 * @param value
		 * @param colorIndex
		 * @param format
		 * @param valueType
		 */
		public void setCellDoubleData(HSSFSheet sheet, int rowIndex,
				int colIndex,Object value,Short colorIndex,String format,Integer valueType){
			HSSFCell cell = setCellValue(sheet, rowIndex, colIndex, value, valueType);
			HSSFCellStyle cellStyle = this.getDataDoubleFormat(sheet.getWorkbook(), colorIndex, format);
			cell.setCellStyle(cellStyle);
		}
		
		/**
		 * 单元格合并
		 * @param sheet	
		 * 			标签对象
		 * @param startRowNumb
		 * 			开始行	
		 * @param startColumnNumb
		 * 			开始列
		 * @param endRowNumb
		 * 			结束行
		 * @param endColumnNumb
		 * 			结束列
		 */
		public void addMergedRegion(HSSFSheet sheet,int startRowNumb,short startColumnNumb,int endRowNumb,short endColumnNumb){
			sheet.addMergedRegion(new Region(startRowNumb,startColumnNumb,endRowNumb,endColumnNumb));
		}
		/**
		 * 获取单元格值
		 * @param cell
		 * @return
		 */
		public String getCellValue(HSSFCell cell){
			String cellValue = "";
			if (cell == null) {
				return null;
			}
			/** 处理数字型的,自动去零 */
			if (Cell.CELL_TYPE_NUMERIC == cell.getCellType()) {
				/** */
				/** 在excel里,日期也是数字,在此要进行判断 */
				if (HSSFDateUtil.isCellDateFormatted(cell)) {
					java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(
							"yyyy-MM-dd HH:mm");
					Date Date = cell.getDateCellValue();
					cellValue = formatter.format(Date);
				} else {
					cellValue = NumbStr.getRightStr(cell.getNumericCellValue() + "");
				}
			}
			/** 处理字符串型 */
			else if (Cell.CELL_TYPE_STRING == cell.getCellType()) {
				cellValue = cell.getStringCellValue();
			}
			/** 处理布尔型 */
			else if (Cell.CELL_TYPE_BOOLEAN == cell.getCellType()) {
				cellValue = cell.getBooleanCellValue() + "";
			}
			/** 其它的,非以上几种数据类型 */
			else {
				cellValue = cell.toString() + "";
			}
			return cellValue;
		}
		public HSSFWorkbook getWorkbook(String filePath){
			File file = new File(filePath);
			if (!file.exists()) {
				return null;
			}
			return this.getExcelFile(file);
		}
		public HSSFWorkbook getExcelFile(File file){
			HSSFWorkbook wbs;
			try {
				FileInputStream is = new FileInputStream(file);
				wbs = new HSSFWorkbook(is);
			} catch (Exception e) {
				return null;
			}
			return wbs;
		}
		public HSSFWorkbook getExcelFile(InputStream is){
			HSSFWorkbook wbs;
			try {
				wbs = new HSSFWorkbook(is);
			} catch (Exception e) {
				return null;
			}
			return wbs;
		}
		public HSSFSheet getSheet(HSSFWorkbook workbook,int sheetIndex){
			return workbook.getSheetAt(sheetIndex);
		}
		
		public HSSFCell getCell(HSSFWorkbook workbook,int sheetIndex,int rowIndex, int cellIndex) {
			if(workbook.getNumberOfSheets()<=sheetIndex){
				return null;
			}
			HSSFSheet sheet = getSheet(workbook, sheetIndex);
			
			HSSFRow row = sheet.getRow(rowIndex);
			if(row==null){
				return null;
			}
			return row.getCell(cellIndex); 
		}
		public HSSFCell getCell(HSSFWorkbook workbook,String sheetName,int rowIndex, int cellIndex) {
			if(workbook.getNumberOfSheets()<=workbook.getSheetIndex(sheetName)){
				return null;
			}
			HSSFSheet sheet = workbook.getSheet(sheetName);
			
			HSSFRow row = sheet.getRow(rowIndex);
			if(row==null){
				return null;
			}
			return row.getCell(cellIndex); 
		}
		/**
		 * 实现读取excel字段内容的方法
		 * 
		 * @param rowIndex
		 * @param cellIndex
		 * @return
		 */
		public String getCellValue(HSSFWorkbook workbook,int rowIndex, int cellIndex) {
			return getCellValue(workbook,0,rowIndex,cellIndex);
		}
		public String getCellValue(HSSFWorkbook workbook,int sheetIndex,int rowIndex, int cellIndex) {
			HSSFCell cell=getCell(workbook,sheetIndex,rowIndex,cellIndex);
			if(cell==null){
				return "";
			}
			return getCellValue(cell);
		}
		public String getCellValue(HSSFWorkbook workbook,String sheetName,int rowIndex, int cellIndex) {
			HSSFCell cell=getCell(workbook,sheetName,rowIndex,cellIndex);
			if(cell==null){
				return "";
			}
			return getCellValue(cell);
		}
		
		public HSSFCellStyle getCellStyle(HSSFWorkbook workbook,int sheetIndex,int rowIndex,int colIndex){
			HSSFSheet sheet = getSheet(workbook, sheetIndex);
			HSSFCell cell = getCell(sheet, rowIndex, colIndex);
			return cell.getCellStyle();
		}
		public HSSFCellStyle getCellStyle(HSSFSheet sheet,int rowIndex,int colIndex){
			HSSFCell cell = getCell(sheet, rowIndex, colIndex);
			return cell.getCellStyle();
		}
		public void setCellStyle(HSSFSheet sheet,HSSFCellStyle cellStyle,int rowIndex,int colIndex){
			HSSFCell cell = getCell(sheet, rowIndex, colIndex);
			cell.setCellStyle(cellStyle);
		}
		
		
		
	}

	public static void readExcel(int first,int last,int CellNum,int page,String filename) {
		InputStream inputStream = null;
		Workbook workbook = null;
		Sheet sheet = null;
		Row row = null;
		Cell cell = null;
		StringBuffer stringBuffer = new StringBuffer();
		try {
			inputStream = new FileInputStream(filename);
			if (filename.endsWith(".xls")) {
				workbook = new HSSFWorkbook(inputStream);
			} else {
				workbook = new XSSFWorkbook(inputStream);
			}
			sheet = workbook.getSheetAt(page);
			if (null != sheet) {
				stringBuffer.append(sheet.getSheetName() + "\n");
				for (int rowIndex = first; rowIndex <= last; rowIndex++) {
					row = sheet.getRow(rowIndex);
					if (null != row) {
						for (int cellnum = 0; cellnum < CellNum; cellnum++) {
							cell = row.getCell(cellnum);
							if (!(cell.getCellType() == Cell.CELL_TYPE_NUMERIC)
									|| !(DateUtil.isCellDateFormatted(cell))) {
								cell.setCellType(Cell.CELL_TYPE_STRING);
								stringBuffer.append(cell
										.getStringCellValue() + "\t");
							} else {
								Date date = cell.getDateCellValue();
								stringBuffer.append(new SimpleDateFormat(
										"HH:mm:ss").format(date) + "\t");
							}
						}
						stringBuffer.append("\n");
					}
				}
				stringBuffer.append("\n\n");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println(stringBuffer);
	}


	public static void createExcel(String fileName,String sheetName, List<List<String>> data){
		//第一步创建workbook
		HSSFWorkbook wb = new HSSFWorkbook();
		//第二步创建sheet
		HSSFSheet sheet = wb.createSheet(sheetName);
		List<String> hang = data.get(0);
		//第三步创建行row:添加表头0行
		HSSFRow row = sheet.createRow(0);
		HSSFCellStyle  style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);  //居中
		HSSFFont font = wb.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示
		style.setFont(font);

		HSSFCell cell =null;
		for (int i = 0;i<hang.size();i++) {
			cell = row.createCell(i);         //表头单元格
			cell.setCellValue(hang.get(i));
			cell.setCellStyle(style);
		}


		//第五步插入数据
		for (int i = 1; i < data.size(); i++) {
			row = sheet.createRow(i);
			hang = data.get(i);
			for (int ii = 0; ii < hang.size(); ii++) {
				//创建行
				cell = row.createCell(ii);
				cell.setCellValue(hang.get(ii));
			}
		}

		//第六步将生成excel文件保存到指定路径下
		try {
			FileOutputStream fout = new FileOutputStream(fileName);
			wb.write(fout);
			fout.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 读取Excel表格
	 *
	 * @param filename
	 */
	public static List<List<String>> readExcel(String filename) {
		InputStream inputStream = null;
		Workbook workbook = null;
		Sheet sheet = null;
		Row row = null;
		Cell cell = null;
		List<List<String>> data = new ArrayList<>();
		try {
			inputStream = new FileInputStream(filename);
			if (filename.endsWith(".xls")) {
				workbook = new HSSFWorkbook(inputStream);
			} else {
				workbook = new XSSFWorkbook(inputStream);
			}
//			workbook.getNumberOfSheets()
			for (int sheetIndex = 0, sheets = 1; sheetIndex < sheets; sheetIndex++) {
				sheet = workbook.getSheetAt(sheetIndex);
				if (null != sheet) {
					for (int rowIndex = 0, rows = sheet.getLastRowNum(); rowIndex <= rows; rowIndex++) {
						List<String> rowData = new ArrayList<>();
						row = sheet.getRow(rowIndex);
						if (null != row) {
							for (int cellnum = 0, cells = row.getLastCellNum(); cellnum < cells; cellnum++) {
								cell = row.getCell(cellnum);
								if(cell==null){
									rowData.add("");
								}else{
									if (!(cell.getCellType() == Cell.CELL_TYPE_NUMERIC)
											|| !(DateUtil.isCellDateFormatted(cell))) {
										cell.setCellType(Cell.CELL_TYPE_STRING);
										rowData.add(cell
												.getStringCellValue());
									} else {
										Date date = cell.getDateCellValue();
										rowData.add(new SimpleDateFormat(
												"HH:mm:ss").format(date));
									}
								}
							}
							data.add(rowData);
						}
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return data;
	}

	/**
	 * @param first
	 * first从第几行开始读，
	 * @param last
	 * last到第几行结束
	 * @param CellNum
	 * CellNum读几列数,
	 * @param page
	 * page为第几页,
	 * @return
	 * 返回为空的时候说明页数不存在
	 * string二维数组
	 */
	public static String[][] readExcelByCondition(int first,int last,int CellNum,int page,InputStream inputStream){//first从第几行开始读，last到第几行结束，CellNum读几列数,x为第几页,fileName文件名
		Workbook wb = null;
		String msg=null;
		Cell cell=null;
		String[][] re= new String[CellNum][last-first+1];
		try {
			wb = WorkbookFactory.create(inputStream);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		for(int i=first-1;i<last;i++){
			for (int j = 0; j < CellNum; j++) {
				try {
					Sheet sheet;
					try {
						sheet = wb.getSheetAt(page);
					} catch (Exception e) {
						return null;
					}
					Row row = sheet.getRow(i);//行
					cell = row.getCell(j);//列
					System.out.println("行："+i+"列："+j+"\t\tcell:"+cell+"\t\trow:"+row.getLastCellNum());
					msg = cell.getStringCellValue();
				}catch (IllegalStateException e) {//去到数字会报错，就原转化成为string类型的
					cell.setCellType(Cell.CELL_TYPE_STRING);
					msg=cell.getStringCellValue();
				}catch (NullPointerException e) {
					// TODO: handle exception
//					System.out.println("cell	的时候	有一个	单元格为空");
					msg="";
				}
				if(msg.trim().equals("")){//空的用null来表示(内容空)
					msg=null;
				}
				re[j][i-first+1]=msg;
			}
		}
		return re;
	}
}
