package com.example.timeaboutproject.utils;

import com.example.timeaboutproject.R;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * �Զ����һ��Toast�ؼ�
 * 
 * @author ����Ȩ
 * 
 */
public class ToastUtil {
	public Toast getToast(Context context, String msg) {
		Toast toast = new Toast(context);
		View toastView = LayoutInflater.from(context).inflate(
				R.layout.view_tips, null);
		((TextView) toastView.findViewById(R.id.tips_msg)).setText(msg);
		toast.setView(toastView);
		toast.setGravity(Gravity.NO_GRAVITY, 0, 300);
		toast.show();
		return toast;
	}

	/**
	 * ����״̬����ɫ
	 * 
	 * @param activity
	 *            ��Ҫ���õ�activity
	 * @param color
	 *            ״̬����ɫֵ
	 */
	public static void setColor(Activity activity, int color) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			// ����״̬��͸��
			activity.getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			// ����һ��״̬����С�ľ���
			View statusView = createStatusView(activity, color);
			// ��� statusView ��������
			ViewGroup decorView = (ViewGroup) activity.getWindow()
					.getDecorView();
			decorView.addView(statusView);
			// ���ø����ֵĲ���
			ViewGroup rootView = (ViewGroup) ((ViewGroup) activity
					.findViewById(android.R.id.content)).getChildAt(0);
			rootView.setFitsSystemWindows(true);
			rootView.setClipToPadding(true);
		}
	}

	/**
	 * ����һ����״̬����С��ͬ�ľ�����
	 * 
	 * @param activity
	 *            ��Ҫ���õ�activity
	 * @param color
	 *            ״̬����ɫֵ
	 * @return ״̬��������
	 */
	private static View createStatusView(Activity activity, int color) {
		// ���״̬���߶�
		int resourceId = activity.getResources().getIdentifier(
				"status_bar_height", "dimen", "android");
		int statusBarHeight = activity.getResources().getDimensionPixelSize(
				resourceId);

		// ����һ����״̬��һ���ߵľ���
		View statusView = new View(activity);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight);
		statusView.setLayoutParams(params);
		statusView.setBackgroundColor(color);
		return statusView;
	}

	/**
	 * * ʹ״̬��͸�� *
	 * <p>
	 * * ������ͼƬ��Ϊ�����Ľ���,��ʱ��ҪͼƬ��䵽״̬�� * * @param activity ��Ҫ���õ�activity
	 */
	public static void setTranslucent(Activity activity) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			// ����״̬��͸��
			activity.getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			// ���ø����ֵĲ���
			ViewGroup rootView = (ViewGroup) ((ViewGroup) activity
					.findViewById(android.R.id.content)).getChildAt(0);
			rootView.setFitsSystemWindows(true);
			rootView.setClipToPadding(true);
		}
	}
}
