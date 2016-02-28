package com.tool;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;

public class NetworkInfoListener {

	private Context mContext = null;
	private ConnectivityManager mConnectivityManager = null;
	private boolean mNetworkAvaliable = false;
	private boolean mLastNetworkAvaliable = false;
	private boolean mFirstOpen = true;

	private State mWifiState;
	private State mMobileState;
	private State mToothState;

	public NetworkFlipCallback mFlipCallback = null;

	public interface NetworkFlipCallback {
		public void onNetworkFlipEvent(int type);
	}

	public NetworkInfoListener(Context context) {

		mContext = context;

		mConnectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		updateNetworkState();

		// reginster
		IntentFilter mFilter = new IntentFilter();
		mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		context.registerReceiver(mReceiver, mFilter);
	}

	public void updateNetworkState() {
		//如果仅仅是用来判断网络连接
//　　　　　则可以使用 cm.getActiveNetworkInfo().isAvailable();  
		
            NetworkInfo[] info = mConnectivityManager.getAllNetworkInfo();   
            if (info != null) {   
                for (int i = 0; i < info.length; i++) {   
                    if (info[i].getState() == State.CONNECTED) {
                    	mNetworkAvaliable= true;  
                    	break;
                    }  else if(info[i].getState() == State.DISCONNECTED){
                    	mNetworkAvaliable= false;  
                    	
                    } 
              
                }   
            }else{
            	mNetworkAvaliable = false;
            } 
           
          
            
//		NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo();
//
//		if (networkInfo != null) {
//			mNetworkAvaliable = networkInfo.isAvailable();
//
//			if (mConnectivityManager
//					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null) {
//				mMobileState = mConnectivityManager.getNetworkInfo(
//						ConnectivityManager.TYPE_MOBILE).getState();
//			}
//			mWifiState = mConnectivityManager.getNetworkInfo(
//					ConnectivityManager.TYPE_WIFI).getState();
//			
//			
//			/*
//			 * mWifiState = mConnectivityManager.getNetworkInfo(
//			 * ConnectivityManager.TYPE_BLUETOOTH).getState();
//			 */
//		} else{
//			mNetworkAvaliable = false;
//		}

		if (mFirstOpen == true || mLastNetworkAvaliable != mNetworkAvaliable) {
			if (mFlipCallback != null)
				mFlipCallback.onNetworkFlipEvent(getConnectedType());
		}
		mLastNetworkAvaliable = mNetworkAvaliable;
	}

	//
	public int getConnectedType() {
		if (mNetworkAvaliable == false)
			return -1;
		else {
			NetworkInfo tmpInfo = mConnectivityManager.getActiveNetworkInfo();
			return tmpInfo.getType();
		}
	}

	//
	public String getConnectedTypeName() {
		if (mNetworkAvaliable == false)
			return "NONE";
		else {
			NetworkInfo tmpInfo = mConnectivityManager.getActiveNetworkInfo();

			return tmpInfo.getTypeName();
		}
	}

	private BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {

				updateNetworkState();
			}
		}
	};

	public void registerNetworkCallback(NetworkFlipCallback networkFlipCallback) {
		// TODO Auto-generated method stub
		mFlipCallback = networkFlipCallback;
	}

	public void destroy() {
		mContext.unregisterReceiver(mReceiver);
	}

}
