package com.example.timeaboutproject.loading;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;

import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.timeaboutproject.MainActivity;
import com.example.timeaboutproject.R;
import com.example.timeaboutproject.configuration.TimeConfiguration;
import com.example.timeaboutproject.httpaccess.HttpAccessForVolley;
import com.example.timeaboutproject.httpaccess.HttpAccessForVolley.getJsonStr;
import com.example.timeaboutproject.utils.ToastUtil;

public class LoginActivity extends Activity implements OnClickListener,
		OnItemClickListener, OnDismissListener {
	protected static final String TAG = "LoginActivity";
	private LinearLayout mLoginLinearLayout;
	private LinearLayout mUserIdLinearLayout;
	private Animation mTranslate;
	private Dialog mLoginingDlg;
	private ImageView mMoreUser;
	private Button mLoginButton;
	private ImageView mLoginMoreUserView;
	private String nameValue;
	private String passValue;
	private ArrayList<User> mUsers;
	private ListView mUserIdListView;
	private MyAapter mAdapter;
	private PopupWindow mPop;
	private String loadService = "L001";
	private String[] params=new String[3];
	private String param;
	private int IMEI=18;
	private SharedPreferences mySharedPreferences;
	private ToastUtil toastUtil=new ToastUtil();
	private EditText userName, passWord;
	//static的作用
	private String passwordValue,string,requestPacket,loginOne,progress;
	public static String responsePacket;
	public static final int requestUsernameOrPasswordLength=10;
	public static String userId,userNameValue;
	private ToastUtil toast;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		ToastUtil.setTranslucent(this);
		initView();
		setListener();
	}

	class MyAapter extends ArrayAdapter<User> {

		public MyAapter(ArrayList<User> users) {
			super(LoginActivity.this, 0, users);
		}

		public View getView(final int position, View convertView,
				ViewGroup parent) {
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(
						R.layout.listview_item, null);
			}

			TextView userIdText = (TextView) convertView
					.findViewById(R.id.listview_userid);
			userIdText.setText(getItem(position).getId());

			ImageView deleteUser = (ImageView) convertView
					.findViewById(R.id.login_delete_user);
			deleteUser.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					if (getItem(position).getId().equals(nameValue)) {
						nameValue = "";
						passValue = "";
						userName.setText(nameValue);
						passWord.setText(passValue);
					}
					mUsers.remove(getItem(position));
					mAdapter.notifyDataSetChanged();
				}
			});
			return convertView;
		}

	}

	private void setListener() {
		userName.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				nameValue = s.toString();
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void afterTextChanged(Editable s) {
			}
		});
		passWord.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				passValue = s.toString();
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			public void afterTextChanged(Editable s) {
			}
		});
		mLoginButton.setOnClickListener(this);
		mLoginMoreUserView.setOnClickListener(this);
		mLoginLinearLayout.startAnimation(mTranslate);
		mUsers = Utils.getUserList(LoginActivity.this);
		if (mUsers.size() > 0) {
			userName.setText(mUsers.get(0).getId());
			passWord.setText(mUsers.get(0).getPwd());
		}
		LinearLayout parent = (LinearLayout) getLayoutInflater().inflate(
				R.layout.userifo_listview, null);
		mUserIdListView = (ListView) parent.findViewById(android.R.id.list);
		parent.removeView(mUserIdListView);
		mUserIdListView.setOnItemClickListener(this);
		mAdapter = new MyAapter(mUsers);
		mUserIdListView.setAdapter(mAdapter);
	}

	private void initView() {
		mySharedPreferences = this.getSharedPreferences("userInfo", Context.MODE_WORLD_READABLE);
		toast=new ToastUtil();
		userName = (EditText) findViewById(R.id.login_edtId);
		passWord = (EditText) findViewById(R.id.login_edtPwd);
		mMoreUser = (ImageView) findViewById(R.id.login_more_user);
		mLoginButton = (Button) findViewById(R.id.login_btnLogin);
		mLoginMoreUserView = (ImageView) findViewById(R.id.login_more_user);
		mLoginLinearLayout = (LinearLayout) findViewById(R.id.login_linearLayout);
		mUserIdLinearLayout = (LinearLayout) findViewById(R.id.userId_LinearLayout);
		mTranslate = AnimationUtils.loadAnimation(this, R.anim.my_translate);
		initLoginingDlg();
	}

	public void initPop() {
		int width = mUserIdLinearLayout.getWidth() - 4;
		int height = LayoutParams.WRAP_CONTENT;
		mPop = new PopupWindow(mUserIdListView, width, height, true);
		mPop.setOnDismissListener(this);
		mPop.setBackgroundDrawable(new ColorDrawable(0xffffffff));

	}

	private void initLoginingDlg() {
		mLoginingDlg = new Dialog(this, R.style.loginingDlg);
		mLoginingDlg.setContentView(R.layout.logining_dlg);
		Window window = mLoginingDlg.getWindow();
		WindowManager.LayoutParams params = window.getAttributes();
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int cxScreen = dm.widthPixels;
		int cyScreen = dm.heightPixels;

		int height = (int) getResources().getDimension(
				R.dimen.loginingdlg_height);
		int lrMargin = (int) getResources().getDimension(
				R.dimen.loginingdlg_lr_margin);
		int topMargin = (int) getResources().getDimension(
				R.dimen.loginingdlg_top_margin);
		params.y = (-(cyScreen - height) / 2) + topMargin; // -199
		params.width = cxScreen;
		params.height = height;
		mLoginingDlg.setCanceledOnTouchOutside(false);
		mLoginingDlg.setOnKeyListener(new DialogInterface.OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				return false;
			}
		});
	}



	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_btnLogin:
			RequestQueue request=Volley.newRequestQueue(this);
			HttpAccessForVolley accessForVolley=new HttpAccessForVolley(request,TimeConfiguration.loadingService,null);
			accessForVolley.setJsonStr(new getJsonStr() {
				@Override
				public void volleyAccessForJson(String strJson) {
					Log.e("TAG",strJson);
				}
			});
			
			
			
//			boolean mIsSave = true;
//			try {
//				for (User user : mUsers) {
//					if (user.getId().equals(nameValue)) {
//						mIsSave = false;
//						break;
//					}
//				}
//				if (mIsSave) {
//					Log.e("TAG","运行2");
//					User user = new User(nameValue, passValue);
//					mUsers.add(user);
//				}
//
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			startActivity(new Intent(this,MainActivity.class));
//			mLoginingDlg.show();
			break;
		case R.id.login_more_user:
			if (mPop == null) {
				initPop();
			}
			if (!mPop.isShowing() && mUsers.size() > 0) {
				mMoreUser.setImageResource(R.drawable.login_more_down); 
				mPop.showAsDropDown(mUserIdLinearLayout, 2, 1); 
			}
			break;
		default:
			break;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		userName.setText(mUsers.get(position).getId());
		passWord.setText(mUsers.get(position).getPwd());
		mPop.dismiss();
	}

	@Override
	public void onDismiss() {
		mMoreUser.setImageResource(R.drawable.login_more_up);
	}

	@Override
	public void onPause() {
		super.onPause();
		try {
			Utils.saveUserList(LoginActivity.this, mUsers);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}


