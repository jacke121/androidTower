package my.JSB.android;

/***********************************
 * 备忘录设置处理类
 * 
 **********************************/

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lbg.yan01.R;

import note.dao.Note;
import note.dao.NoteDao;
import ui.IndexActivity;


public class SetMemo extends Activity {
	// 备忘录名称
	private EditText etName;
	// 备忘录内容
	private EditText etMain;
	TextView memo_title;
	private Button btnCommit;
	private Button btnCancel;
	int editFlag = 0;// 0添加、1修改

	String noteTitle = "";
	String noteContext = "";

	NoteDao notesDao;
	Note notes;
	SparseArray<Note> noteSa;
	
	int Id;
	int level;
	String titleb;
	String contentb;
	
	String processURL = IndexActivity.serverIp +"/note_add.action";
	String url = IndexActivity.serverIp +"/note_update.action";
	IndexActivity indexActivity = new IndexActivity();
	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
		setContentView(R.layout.notecontext);
		notesDao = new NoteDao(IndexActivity.helper);
		memo_title = (TextView) findViewById(R.id.memo_title);
		btnCommit = (Button) findViewById(R.id.btnCommit);
		btnCancel = (Button) findViewById(R.id.btnCancel);
		etName = (EditText) findViewById(R.id.noteName);
		etMain = (EditText) findViewById(R.id.et1);
		TextView config_hidden1 = (TextView) findViewById(R.id.config_hidden1);
		config_hidden1.requestFocus();

		Intent intent = getIntent();
		editFlag = intent.getIntExtra("edit", 0);
		Id = intent.getIntExtra("Id", 0);
		level = intent.getIntExtra("level", 0);

		final EditText edittext_Msg = new EditText(this);
		edittext_Msg.setGravity(Gravity.TOP);
		edittext_Msg.setLines(8);

		etName = (EditText) findViewById(R.id.noteName);
		etMain = (EditText) findViewById(R.id.et1);

		noteSa = notesDao.queryToList("id = ?", new String[] { Id + "" });
		if (noteSa != null) {
			notes = noteSa.get(0);
		} else {
			notes = new Note();
		}
		titleb = notes.title;
		contentb = notes.content;
		if (editFlag == 0) {
			memo_title.setText("新增备忘录");
			etName.setText("");
			etMain.setText("");
		} else {
			memo_title.setText("修改备忘录");
			etName.setText(titleb);
			etMain.setText(contentb);
		}


		btnCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { // 按下的如果是BACK，同时没有重复
			Intent intent = new Intent(SetMemo.this, IndexActivity.class);
			startActivity(intent);
			SetMemo.this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onResume() {
		/**
		 * 设置横屏
		 */
		if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		super.onResume();
	}
}