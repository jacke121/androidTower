package upload;//package com.upload;
//
//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//
//import com.note.dao.Notes;
//
//import android.content.Context;
//import android.os.Environment;
//
//public class CreateFiles {
//	
//	String filenameTemp = Environment.getExternalStorageDirectory().getAbsolutePath() + "/note";
//	//创建文件夹及文件
//	public void CreateText() throws IOException {
//		File file = new File(filenameTemp);
//		if (!file.exists()) {
//			try {
//				//按照指定的路径创建文件夹
//				file.mkdirs();
//			} catch (Exception e) {
//				// TODO: handle exception
//			}
//		}
//		File dir = new File(filenameTemp);
//		if (!dir.exists()) {
//			  try {
//				  //在指定的文件夹中创建文件
//				  dir.createNewFile();
//			} catch (Exception e) {
//			}
//		}
//
//	}
//	
//	//向已创建的文件中写入数据
//	public void print(com.basedao.Notes notes)  {
//		
//		String filename = "myfile";
//		String string = "Hello world!";
//		FileOutputStream outputStream;
//
//		try {
////		  outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
////		  outputStream.write(string.getBytes());
////		  outputStream.close();
//		} catch (Exception e) {
//		  e.printStackTrace();
//		}
//	}
//}