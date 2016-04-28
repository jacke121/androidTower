package exceltest;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.List;


import android.content.Context;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;


import com.baseDao.Biao;
import com.baseDao.Ganta;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class JXLUtil {
	public static WritableFont arial14font = null;

	public static WritableCellFormat arial14format = null;
	public static WritableFont arial10font = null;
	public static WritableCellFormat arial10format = null;
	public static WritableFont arial12font = null;
	public static WritableCellFormat arial12format = null;

	public final static String UTF8_ENCODING = "UTF-8";
	public final static String GBK_ENCODING = "GBK";

	public static int row = 2;// 具体字段写入从第二行开始

	//格式定义
	public void format() {
		try {
			arial14font = new WritableFont(WritableFont.ARIAL, 14,
					WritableFont.BOLD);
			arial14font.setColour(jxl.format.Colour.LIGHT_BLUE);
			arial14format = new WritableCellFormat(arial14font);
			arial14format.setAlignment(jxl.format.Alignment.CENTRE);
			arial14format.setBorder(jxl.format.Border.ALL,
					jxl.format.BorderLineStyle.THIN);
			arial14format.setBackground(jxl.format.Colour.VERY_LIGHT_YELLOW);
			arial10font = new WritableFont(WritableFont.ARIAL, 10,
					WritableFont.BOLD);
			arial10format = new WritableCellFormat(arial10font);
			arial10format.setAlignment(jxl.format.Alignment.CENTRE);
			arial10format.setBorder(jxl.format.Border.ALL,
					jxl.format.BorderLineStyle.THIN);
			arial10format.setBackground(jxl.format.Colour.LIGHT_BLUE);
			arial12font = new WritableFont(WritableFont.ARIAL, 12);
			arial12format = new WritableCellFormat(arial12font);
			arial12format.setBorder(jxl.format.Border.ALL,
					jxl.format.BorderLineStyle.THIN);
		} catch (WriteException e) {
			
			e.printStackTrace();
		}
	}
	
	public  void initExcel(String fileName, String[] colName,
			int[] widthArr) {
		JXLUtil.row = 2;
		format();// 先设置格式
		WritableWorkbook workbook = null;
		try {
			// WorkbookSettings setEncode = new WorkbookSettings(); // 设置读文件编码
			// setEncode.setEncoding(UTF8_ENCODING);
			File file = new File(fileName);
			if(file.exists()){
				file.delete();
				file.createNewFile();
			}else{
				file.createNewFile();		
			}

			workbook = Workbook.createWorkbook(file);

			WritableSheet sheet = workbook.createSheet("Sheet 1", 0);// 建立sheet
			sheet.mergeCells(0, 0, colName.length - 1, 0);
			sheet.addCell((WritableCell) new Label(0, 0, fileName,
					arial14format));// 表头设置完成
			for (int i = 0; i < widthArr.length; i++) {
				sheet.setColumnView(i, widthArr[i]);// 设置col 宽度
			}

			int row = 1;

			for (int col = 0; col < colName.length; col++) {
				sheet.addCell(new Label(col, row, colName[col], arial10format));// 写入
			}
			workbook.write();// 写入数据
		} catch (RowsExceededException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (workbook != null) {
				try {
					workbook.close();
				} catch (WriteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	public void writeObjInToExcel( SparseArray<Ganta> objList,SparseArray<Biao> biaoList,
										String fileName, String[] fieldArr, Context c) {
		if (objList != null && objList.size() > 0) {
			format();
			row=2;
			WritableWorkbook writebook = null;
			try {
				//设置读文件编码
				// setEncode.setEncoding(UTF8_ENCODING);
				Workbook workbook = Workbook.getWorkbook(new File(fileName));
				writebook = Workbook.createWorkbook(new File(fileName),
						workbook);
				WritableSheet sheet = writebook.getSheet(0);

				for(int j = 0; j < objList.size(); j++) {
					Object tmp = objList.get(j);
					Class classType = tmp.getClass();
					sheet.addCell(new Label(0, row, (j+1)+"",arial12format));// 第一列用来写序号
					sheet.addCell(new Label(1, row, objList.get(j).areaname, arial12format));
					sheet.addCell(new Label(2, row, objList.get(j).name, arial12format));
					sheet.addCell(new Label(3, row, objList.get(j).caizhi, arial12format));
					sheet.addCell(new Label(4, row, objList.get(j).danwei, arial12format));
					sheet.addCell(new Label(5, row, objList.get(j).dianya, arial12format));
					sheet.addCell(new Label(6, row, objList.get(j).yunxing, arial12format));
					row++;
				}
				//----------------
				 sheet = writebook.getSheet(2);
				row=2;
				for(int j = 0; j < biaoList.size(); j++) {
					Biao tmp = biaoList.get(j);
					int col =0;
					sheet.addCell(new Label(0, row, (j+1)+"",	arial12format));// 第一列用来写序号
					sheet.addCell(new Label(1, row, tmp.areaname, arial12format));
					sheet.addCell(new Label(2, row, tmp.code, arial12format));
					sheet.addCell(new Label(3, row, tmp.name, arial12format));
					sheet.addCell(new Label(4, row, tmp.zuobiao, arial12format));
					}
					row++;

				writebook.write();
				Toast.makeText(c, "导出成功", Toast.LENGTH_SHORT).show();
			} catch (BiffException e) {
				e.printStackTrace();
			} catch (WriteException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}  finally {
				if (writebook != null) {
					try {
						writebook.close();
					} catch (WriteException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		}
	}


	public <T> void writeObjListToExcel(SparseArray<T> objList,
			String fileName, String[] fieldArr, Context c) {
		if (objList != null && objList.size() > 0) {
			WritableWorkbook writebook = null;
			InputStream in = null;
			try {
				//设置读文件编码
				// setEncode.setEncoding(UTF8_ENCODING);
				in = new FileInputStream(new File(fileName));
				Workbook workbook = Workbook.getWorkbook(in);
				writebook = Workbook.createWorkbook(new File(fileName),
						workbook);
				WritableSheet sheet = writebook.getSheet(0);
				for(int j = 0; j < objList.size(); j++) {
					Object tmp = objList.get(j);
					Class classType = tmp.getClass();
					int col = 1;
					sheet.addCell(new Label(col, row, (j+1)+"",
							arial12format));// 第一列用来写序号
					// 通过反射取值，并且写入到excel中
					for (int i = 0; i < fieldArr.length; i++) {
//						Class<?> cls=Class.forName(className);////////////////////////通过类的名称反射类
						String fieldName = fieldArr[i];
						Object value = GetValueByRef.getFiledValue(tmp,
								fieldName);
						String str = null;
						if (value == null) {
							str = "";
						} else {
							str = String.valueOf(value);
						}
						sheet.addCell(new Label(col, row, str, arial12format));
						col++;
					}
					row++;
				}
				writebook.write();
				Toast.makeText(c, "导出成功", Toast.LENGTH_SHORT).show();
			} catch (BiffException e) {
				e.printStackTrace();
			} catch (WriteException e) {
				e.printStackTrace();
			} catch (IOException e) {
			} finally {
				if (writebook != null) {
					try {
						writebook.close();
					} catch (WriteException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}if(in != null){
					try {
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		}
	}
	/**
	 * 将 数据写入到excel中
	 * @param os  创建Excel的输出流
	 * @param objList     要插入的数据
	 * @param fieldArr   字段名称
	 * @param fileName   excel表头名称
	 * @param colName     excel列明
	 * @param widthArr    excel单元格宽度
	 */
	public <T> void dataToExcel(ByteArrayOutputStream os,
								List<T> objList, String[] fieldArr, String fileName,
								String[] colName, int[] widthArr) {
		format();// 先设置格式
		WritableWorkbook workbook = null;
		try {
			// WorkbookSettings setEncode = new WorkbookSettings(); // 设置读文件编码
			// setEncode.setEncoding(UTF8_ENCODING);
			// File file = new File(fileName);
			workbook = Workbook.createWorkbook(os);
			WritableSheet sheet = workbook.createSheet("Sheet 1", 0);// 建立sheet
			sheet.mergeCells(0, 0, colName.length - 1, 0);
			sheet.addCell((WritableCell) new Label(0, 0, fileName,
					arial14format));// 表头设置完成
			for (int i = 0; i < widthArr.length; i++) {
				sheet.setColumnView(i, widthArr[i]);// 设置col 宽度
			}

			int row = 1;
			int col = 0;
			for (col = 0; col < colName.length; col++) {// 写入列明
				sheet.addCell(new Label(col, row, colName[col], arial10format));// 写入
				// col名称
			}
int index=0;
			for (Object tmp : objList) {// 写入数据
				row++;
				col = 1;
				index++;
				String serialNumberStr = String.valueOf(index);
				sheet.addCell(new Label(col, row, serialNumberStr,
						arial12format));// 第一列用来写序号
				col++;
				/**
				 * 通过反射取值，并且写入到excel中
				 */
				for (int i = 0; i < fieldArr.length; i++) {
					String fieldName = fieldArr[i];
					Object value = GetValueByRef.getFiledValue(tmp, fieldName);
					String str = null;
					if (value == null) {
						str = "";
					} else {
						str = String.valueOf(value);
					}
					sheet.addCell(new Label(col, row, str, arial12format));
					col++;
				}
			}
			workbook.write();// 写入数据
		} catch (RowsExceededException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (workbook != null) {
				try {
					workbook.close();
				} catch (WriteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
