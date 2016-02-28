package com.takephoto;

import java.io.File;
import java.io.IOException;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.lbg.yan01.R;

public class MainActivity extends Activity implements OnClickListener {
	private ImageView mimageViewPhotoShow;
	private PopupWindow mPopupWindow;
	private View mpopview;
	private Bitmap photo;
	private File mPhotoFile;
	private int CAMERA_RESULT = 100;
	private int RESULT_LOAD_IMAGE = 200;
	private String saveDir = Environment.getExternalStorageDirectory()
			.getPath() + "/temp_image";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.advice_praise_blue);
		setContentView(R.layout.activity_main);
//		InitUI();
	}

	private void InitUI() {
		View buttonChoosePhoto = (Button) findViewById(R.id.buttonChoosePhoto);
		if (buttonChoosePhoto != null) {
			buttonChoosePhoto.setOnClickListener(this);
		}

		mimageViewPhotoShow = (ImageView) findViewById(R.id.imageViewPhotoShow);
	}

	@Override
	public void onClick(View arg0) {
		if (arg0.getId() == R.id.buttonChoosePhoto) {
			LayoutInflater inflater = LayoutInflater.from(this);
			mpopview = inflater.inflate(R.layout.layout_login_choose_photo,
					null);

			mPopupWindow = new PopupWindow(mpopview, 300, 400, true);


			mPopupWindow.showAtLocation(mimageViewPhotoShow, Gravity.CENTER, 0,
					0);
			Button mbuttonTakePhoto = (Button) mpopview
					.findViewById(R.id.button_take_photo);
			Button mbuttonChoicePhoto = (Button) mpopview
					.findViewById(R.id.button_choice_photo);
			Button mbuttonChoicecannce = (Button) mpopview
					.findViewById(R.id.button_choice_cancer);

			// 相册上传
			mbuttonChoicePhoto.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mPopupWindow.dismiss();
					Intent i = new Intent(
							Intent.ACTION_PICK,
							android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					startActivityForResult(i, RESULT_LOAD_IMAGE);
				}
			});

			File savePath = new File(saveDir);
			if (!savePath.exists()) {
				savePath.mkdirs();
			}

			// 拍照上传
			mbuttonTakePhoto.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mPopupWindow.dismiss();
					destoryImage();
					String state = Environment.getExternalStorageState();
					if (state.equals(Environment.MEDIA_MOUNTED)) {
						mPhotoFile = new File(saveDir, "temp.jpg");
						mPhotoFile.delete();
						if (!mPhotoFile.exists()) {
							try {
								mPhotoFile.createNewFile();
							} catch (IOException e) {
								e.printStackTrace();
								Toast.makeText(getApplication(), "照片创建失败!",
										Toast.LENGTH_LONG).show();
								return;
							}
						}
						Intent intent = new Intent(
								"android.media.action.IMAGE_CAPTURE");
						intent.putExtra(MediaStore.EXTRA_OUTPUT,
								Uri.fromFile(mPhotoFile));
						startActivityForResult(intent, CAMERA_RESULT);
					} else {
						Toast.makeText(getApplication(), "sdcard无效或没有插入!",
								Toast.LENGTH_SHORT).show();
					}
				}
			});

			mbuttonChoicecannce.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					mPopupWindow.dismiss();
				}
			});
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == CAMERA_RESULT && resultCode == RESULT_OK) {
			if (mPhotoFile != null && mPhotoFile.exists()) {
				BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
				bitmapOptions.inSampleSize = 8;
				int degree = readPictureDegree(mPhotoFile.getAbsolutePath());
				Bitmap bitmap = BitmapFactory.decodeFile(mPhotoFile.getPath(),
						bitmapOptions);
				bitmap = rotaingImageView(degree, bitmap);
				mimageViewPhotoShow.setImageBitmap(bitmap);
			}
		}
		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
				&& null != data) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };

			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String picturePath = cursor.getString(columnIndex);
			cursor.close();

			mimageViewPhotoShow.setImageBitmap(BitmapFactory
					.decodeFile(picturePath));
		}
	}

	private static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					degree = 90;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					degree = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					degree = 270;
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	private static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
		// 旋转图片 动作
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		System.out.println("angle2=" + angle);
		// 创建新的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
				bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return resizedBitmap;
	}

	@Override
	protected void onDestroy() {
		destoryImage();
		super.onDestroy();
	}

	private void destoryImage() {
		if (photo != null) {
			photo.recycle();
			photo = null;
		}
	}
}
