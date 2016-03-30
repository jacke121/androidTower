package ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.lbg.yan01.R;

import android.provider.Settings.Secure;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import webClient.TimeClientHandler;

public class IndexActivity extends Activity implements OnClickListener {
    public static String serverIp = "192.168.1.1";
    public static String eTxtUser, eTxtPwd, message;
    EditText ext_ip;
    //	ShowDialog mShowDialog;
    public static String error_message = "", remMsg;
    String msg = null;
    public SharedPreferences sharedPreferences_userInfo;
    Editor editor;
    private int isFirst;//0第一次
    Socket client;
    String android_id;
    final int serverport = 2001;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
        setContentView(R.layout.index);
        // /在Android2.2以后必须添加以下代码
        // 本应用采用的Android4.0
        // 设置线程的策略
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork() // or
                .penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath()
                .build());
        sharedPreferences_userInfo = getSharedPreferences("userInfo", MODE_PRIVATE);
        ext_ip = (EditText) findViewById(R.id.ext_ip);
        ext_ip.setText(serverIp);
        Button main_raoguo = (Button) findViewById(R.id.main_raoguo);
        main_raoguo.setOnClickListener(this);
        // 转到注册页面

        Button btnAuthorize = (Button) findViewById(R.id.btnAuthorize);
        btnAuthorize.setOnClickListener(this);

        //非首次登陆直接进入笔记页面
        remMsg = sharedPreferences_userInfo.getString("userName", "");
        if (remMsg != null && !remMsg.equals("")) {
            Intent intent = new Intent(IndexActivity.this, Activity_AreaList.class);
            startActivity(intent);
            IndexActivity.this.finish();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_raoguo: {
                Intent intent = new Intent(IndexActivity.this,
                        Activity_AreaList.class);
                    /* 把bundle对象assign给Intent */
                startActivityForResult(intent, 1);
            }
            break;
            case R.id.btnAuthorize:
                android_id  = Secure.getString(getApplication().getContentResolver(), Secure.ANDROID_ID);
                serverIp=ext_ip.getText().toString();

                EventLoopGroup workerGroup = new NioEventLoopGroup();
                try {
                    Bootstrap bootstrap = new Bootstrap();
                    bootstrap.group(workerGroup);
                    bootstrap.channel(NioSocketChannel.class);
                    bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
                    bootstrap.handler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new TimeClientHandler());

                        }
                    });
                    // 发起异步连接操作
                    ChannelFuture future = bootstrap.connect(serverIp, serverport).sync();

                    future.channel().closeFuture().sync();
                } catch (Exception e) {
                    workerGroup.shutdownGracefully();
                }
                Thread desktopServerThread = new Thread(new TCPDesktopServer());
//                desktopServerThread.start();
//                showDialog(android_id);
                break;
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            if (msg.what == 1) {
                showDialog("连接成功!");
                try {
                    client.getOutputStream().write(android_id.getBytes());
                    client.getOutputStream().flush();
//                    client.getChannel().write(ByteBuffer.wrap(android_id.getBytes()) );
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else if (msg.what == 2) {
                showDialog("授权成功!");
            }

        }
    };

    // 弹出警告对话框
    public void setAlertDialog(String title, String message) {
        Builder builder = new Builder(IndexActivity.this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public void showDialog(String msg) {
        // 创建提示框提醒是否登录成功
        Builder builder = new Builder(IndexActivity.this);
        builder.setTitle("提示").setMessage(msg).setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();
    }

    public class TCPDesktopServer implements Runnable {

        public void run() {
            try {
                InetAddress serverAddr = InetAddress.getByName(serverIp);//TCPServer.SERVERIP

                Log.d("TCP", "C: Connecting...");

                client = new Socket(serverAddr, serverport);
//                client.connect(socketAddress,10);
                if(client.isConnected()){
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
                }
                while (true) {
                    try {
                        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                        String str = in.readLine();
                        Message msg = new Message();
                        msg.obj = str;
                        msg.what = 2;
                        handler.sendMessage(msg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        client.close();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}