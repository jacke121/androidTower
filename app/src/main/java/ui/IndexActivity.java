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
import android.support.v4.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
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
    public static String userName = "";
    public static String eTxtUser, eTxtPwd, message;
    EditText ext_user;
    EditText ext_Pwd;
    CheckBox check_rember;
    //	ShowDialog mShowDialog;
    String msg = null;
    public SharedPreferences sharedPreferences_userInfo;
    Editor editor;
    private int isFirst;//0第一次
    Socket client;
    String android_id;
    final int serverport = 2001;
    ArrayMap<String, String> userMap = new ArrayMap<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
        setContentView(R.layout.index);
        userMap.put("SJZ_FCSCZY", "1");
        userMap.put("SJZ_FCSCZY", "1");
        userMap.put("SJZ_FCWHK", "1");
        userMap.put("SJZ_FENGJW1", "1");
        userMap.put("SJZ_GAOHL", "1");
        userMap.put("SJZ_GAOLH1", "1");
        userMap.put("SJZ_GAOYB", "1");
        userMap.put("SJZ_GAOYL", "1");
        userMap.put("SJZ_GAOZY", "1");
        userMap.put("SJZ_GONGJK", "1");
        userMap.put("SJZ_GUOJXXJ", "1");
        userMap.put("SJZ_GUOX", "1");
        userMap.put("SJZ_CJSCZY", "1");
        userMap.put("SJZ_GUOYM", "1");
        userMap.put("SJZ_GUOYQ", "1");
        userMap.put("SJZ_GUZM", "1");
        userMap.put("SJZ_HANLN", "1");
        userMap.put("SJZ_HAORT", "1");
        userMap.put("SJZ_HAOSL", "1");
        userMap.put("SJZ_HAOYALI", "1");
        userMap.put("SJZ_HEB1", "1");
        userMap.put("SJZ_HOUSC", "1");
        userMap.put("SJZ_HUANGJD", "1");
        userMap.put("SJZ_JIAMC", "1");
        userMap.put("SJZ_JINZW", "1");
        userMap.put("SJZ_JXNYFH", "1");
        userMap.put("SJZ_ANYH", "1");
        userMap.put("SJZ_ANXF", "1");
        userMap.put("SJZ_BOYH", "1");
        userMap.put("SJZ_CAIH", "1");
        userMap.put("SJZ_CAOHJ", "1");
        userMap.put("SJZ_CAOLJ", "1");
        userMap.put("SJZ_CGWSL", "1");
        userMap.put("SJZ_CGXUCX", "1");
        userMap.put("SJZ_CHANGHF", "1");
        userMap.put("SJZ_CHENGGCZY", "1");
        userMap.put("SJZ_CSSCZY", "1");
        userMap.put("SJZ_CSWANGWS", "1");
        userMap.put("SJZ_CZWSL", "1");
        userMap.put("SJZ_DINGLC0", "1");

        // /在Android2.2以后必须添加以下代码
        // 本应用采用的Android4.0
        // 设置线程的策略
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork() // or
                .penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath()
                .build());
        sharedPreferences_userInfo = getSharedPreferences("userInfo", MODE_PRIVATE);
        userName = sharedPreferences_userInfo.getString("userName", "");

        ext_user = (EditText) findViewById(R.id.ext_ip);
        ext_Pwd = (EditText) findViewById(R.id.ext_Pwd);
        check_rember = (CheckBox) findViewById(R.id.check_rember);
        ext_user.setText(userName);
        if (sharedPreferences_userInfo.getBoolean("check_rember", false)) {
            check_rember.setChecked(true);
            ext_Pwd.setText(sharedPreferences_userInfo.getString("userPwd", ""));
        }
        Button main_raoguo = (Button) findViewById(R.id.main_raoguo);
        main_raoguo.setOnClickListener(this);
        // 转到注册页面

        Button btnAuthorize = (Button) findViewById(R.id.btn_login);
        btnAuthorize.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_raoguo: {
                finish();
            }
            break;
            case R.id.btn_login:

                String username = ext_user.getText().toString().trim();
                String userPwd = ext_Pwd.getText().toString();

                if (username.length() == 0) {
                    showDialog("用户名不能为空");
                    return;
                }
               else if (userPwd.length() == 0) {
                    showDialog("密码不能为空");
                    return;
                }
                else if (!userMap.containsKey(username.toUpperCase())) {
                    showDialog("用户名不存在");
                    return;
                }
                else if (!userPwd.equals(userMap.get(username.toUpperCase()))) {
                    showDialog("密码错误");
                    ext_Pwd.setText("");
                    return;
                }
                editor = sharedPreferences_userInfo.edit();
                if (check_rember.isChecked()) {
                    editor.putBoolean("check_rember", true);
                } else {
                    editor.putBoolean("check_rember", false);
                }
                editor.putString("userName", username);
                editor.putString("userPwd", userPwd);
                editor.commit();
                Intent intent = new Intent(IndexActivity.this,
                        Activity_AreaList.class);
                    /* 把bundle对象assign给Intent */
                startActivityForResult(intent, 1);
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
            } else if (msg.what == 2) {
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
                InetAddress serverAddr = InetAddress.getByName(userName);//TCPServer.SERVERIP

                Log.d("TCP", "C: Connecting...");

                client = new Socket(serverAddr, serverport);
//                client.connect(socketAddress,10);
                if (client.isConnected()) {
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

    public void tcp() {
        android_id = Secure.getString(getApplication().getContentResolver(), Secure.ANDROID_ID);
        userName = ext_user.getText().toString();

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
            ChannelFuture future = bootstrap.connect(userName, serverport).sync();

            future.channel().closeFuture().sync();
        } catch (Exception e) {
            workerGroup.shutdownGracefully();
        }
        Thread desktopServerThread = new Thread(new TCPDesktopServer());
    }
}