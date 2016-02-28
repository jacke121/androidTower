package ui;

import webClient.DialogFactory;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.WindowManager;

public class ShowDialog {
	Activity mactivity;
	public Dialog mdlg;
	public ShowDialog(Activity mactivity){
		this.mactivity = mactivity;
	}

	
	
	public void showDialog(String msg){
		mdlg = DialogFactory.createLoadingDialog(
				mactivity, msg);
		mdlg.getWindow().setType(
				(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
		mdlg.show();
	}
	public void close(boolean whichThreadflag){//whichThreadflag为超时标识；true表示超时
		if(mdlg!=null){
			mdlg.dismiss();
			mdlg=null;
			if(whichThreadflag){
				new AlertDialog.Builder(mactivity)
				.setTitle("温馨提示")
				.setMessage("网络超时了!")
				.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							public void onClick(
									DialogInterface dialog,
									int which) {
								return;
							}
						}).create().show();
			}
		}
		
	}
	
//	public interface dialogThread {
//		
//	}
}
