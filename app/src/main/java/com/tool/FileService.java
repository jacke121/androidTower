package com.tool;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.LinkedHashMap;
import java.util.Map;


import android.content.Context;
import android.os.Environment;
import android.util.Log;

/**
 * 文件保存与读取功能实现类
 * 
 * @author Administrator
 * 
 *         2010-6-28 下午08:15:18
 */
public class FileService {
	public static final String TAG = "FileService";
	private Context context;

	// 得到传入的上下文对象的引用
	public FileService(Context context) {
		this.context = context;
	}

	String filenameTemp = Environment.getExternalStorageDirectory().getAbsolutePath() + "/note";
	File file;

	// 创建文件夹及文件
	public void CreateText() throws IOException {
		file = new File(filenameTemp);
		file.canWrite();
		if (!file.exists()) {
			try {
				// 按照指定的路径创建文件夹
				file.mkdirs();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		File dir = new File(filenameTemp);
		if (!dir.exists()) {
			try {
				// 在指定的文件夹中创建文件
				dir.createNewFile();
			} catch (Exception e) {

			}
		}

	}

	// 　/**
	// 　　* 保存文件
	// 　　* @param toSaveString
	// 　　* @param filePath
	// 　　*/
	public void save(String fileName, String content) throws IOException {
		file = new File(filenameTemp);
		if (!file.exists()) {
			try {
				// 按照指定的路径创建文件夹
				file.mkdirs();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		File dir = new File(file.getParent());
		if (!dir.exists()) {
			try {
				// 在指定的文件夹中创建文件
				dir.createNewFile();
			} catch (Exception e) {

			}
		}
		// File saveFile = new File(file);
		// if (!saveFile.exists()) {
		// File dir = new File(saveFile.getParent());
		// dir.mkdirs();
		// saveFile.createNewFile();
		// }

		FileOutputStream outStream = new FileOutputStream(dir);
		outStream.write(content.getBytes());
		outStream.write(fileName.getBytes());
		outStream.close();

	}

	/**
	 * 保存文件
	 * 
	 * @param fileName
	 *           文件名
	 * @param content
	 *           文件内容
	 * @throws Exception
	 */
	public void save2(String fileName, String content) throws Exception {
		CreateText();
		// 由于页面输入的都是文本信息，所以当文件名不是以.txt后缀名结尾时，自动加上.txt后缀
		if (!fileName.endsWith(".txt")) {
			fileName = fileName + ".txt";
		}

		byte[] buf = fileName.getBytes("iso8859-1");

		Log.e(TAG, new String(buf, "utf-8"));

		fileName = new String(buf, "utf-8");

		Log.e(TAG, fileName);

		// Context.MODE_PRIVATE：为默认操作模式，代表该文件是私有数据，只能被应用本身访问，在该模式下，写入的内容会覆盖原文件的内容，如果想把新写入的内容追加到原文件中。可以使用Context.MODE_APPEND
		// Context.MODE_APPEND：模式会检查文件是否存在，存在就往文件追加内容，否则就创建新文件。
		// Context.MODE_WORLD_READABLE和Context.MODE_WORLD_WRITEABLE用来控制其他应用是否有权限读写该文件。
		// MODE_WORLD_READABLE：表示当前文件可以被其他应用读取；MODE_WORLD_WRITEABLE：表示当前文件可以被其他应用写入。
		// 如果希望文件被其他应用读和写，可以传入：
		// openFileOutput("output.txt", Context.MODE_WORLD_READABLE +
		// Context.MODE_WORLD_WRITEABLE);

		@SuppressWarnings("static-access")
		FileOutputStream fos = context.openFileOutput(fileName, context.MODE_PRIVATE);
		fos.write(content.getBytes());
		fos.toString();
		// file.
		fos.close();
	}

	/**
	 * 读取文件内容
	 * 
	 * @param fileName
	 *           文件名
	 * @return 文件内容
	 * @throws Exception
	 */
	public String read(String fileName) throws Exception {

		// 由于页面输入的都是文本信息，所以当文件名不是以.txt后缀名结尾时，自动加上.txt后缀
		if (!fileName.endsWith(".txt")) {
			fileName = fileName + ".txt";
		}

		FileInputStream fis = context.openFileInput(fileName);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		byte[] buf = new byte[1024];
		int len = 0;

		// 将读取后的数据放置在内存中---ByteArrayOutputStream
		while ((len = fis.read(buf)) != -1) {
			baos.write(buf, 0, len);
		}

		fis.close();
		baos.close();

		// 返回内存中存储的数据
		return baos.toString();

	}
	ObjectOutputStream oos ;
	public void fileSave(String fileName, String content) {
		// //保存在本地
		// try {
		// // 通过openFileOutput方法得到一个输出流，方法参数为创建的文件名（不能有斜杠），操作模式
		// FileOutputStream fos =
		// this.openFileOutput("oauth_1.out",Context.MODE_WORLD_READABLE);
		// ObjectOutputStream oos = new ObjectOutputStream(fos);
		// oos.writeObject(fileName);
		// oos.writeObject(content);// 写入
		// fos.close(); // 关闭输出流
		// //Toast.makeText(WebviewTencentActivity.this,
		// "保存oAuth_1成功",Toast.LENGTH_LONG).show();
		// } catch (FileNotFoundException e) {
		// e.printStackTrace();
		// //Toast.makeText(WebviewTencentActivity.this,
		// "出现异常1",Toast.LENGTH_LONG).show();
		// } catch (IOException e) {
		// e.printStackTrace();
		// //Toast.makeText(WebviewTencentActivity.this,
		// "出现异常2",Toast.LENGTH_LONG).show();
		// }

		// 保存在sd卡
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

			File sdCardDir = Environment.getExternalStorageDirectory();// 获取SDCard目录
			File sdFile = new File(sdCardDir, fileName+".txt");

			try {
				FileOutputStream fos = new FileOutputStream(sdFile);
				oos= new ObjectOutputStream(fos);
				oos.writeObject(fileName);
				oos.writeObject(content);// 写入
//				loginRemoteService(fileName,content);
				fos.close(); // 关闭输出流
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// Toast.makeText(WebviewTencentActivity.this, "成功保存到sd卡",
			// Toast.LENGTH_LONG).show();

		}
	}

}