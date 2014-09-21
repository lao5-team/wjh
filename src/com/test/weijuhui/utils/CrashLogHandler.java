package com.test.weijuhui.utils;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;

import com.test.weijuhui.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

public class CrashLogHandler implements UncaughtExceptionHandler {
	
	private Activity mActivity = null;
	
	public static CrashLogHandler createHandler(Activity activity)
	{
		
		CrashLogHandler handler = new CrashLogHandler();
		handler.mActivity = activity;
		Thread.setDefaultUncaughtExceptionHandler(handler); 
		return handler;
	}
	
	@Override
	public void uncaughtException(Thread arg0, Throwable arg1) {
		final Throwable throwable = arg1;
			Writer writer = new StringWriter();
			PrintWriter printWriter = new PrintWriter(
					writer);
			throwable.printStackTrace(printWriter);
//			Intent intent = new Intent(
//					android.content.Intent.ACTION_SEND);
//			intent.setType("plain/text");
//			String[] strEmailReciver = {
//					"yhchinabest@163.com"};
//			String strEmailBody = writer.toString();
//			intent.putExtra(
//					android.content.Intent.EXTRA_EMAIL,
//					strEmailReciver); // 设置收件人
//			intent.putExtra(Intent.EXTRA_SUBJECT,
//					mActivity.getString(R.string.application_crashed, (int)(Math.random()*100000)));
//			intent.putExtra(
//					android.content.Intent.EXTRA_TEXT,
//					strEmailBody); // 设置内容
//			mActivity.startActivity(Intent
//					.createChooser(intent,
//							mActivity.getString(R.string.send_crash_report)));
			String[] subs = {"yhchinabest@163.com"};
			Intent data=new Intent(Intent.ACTION_SEND); 
			data.setType("plain/text");
			data.putExtra(Intent.EXTRA_EMAIL, subs); 
			data.putExtra(Intent.EXTRA_SUBJECT, "应用崩溃"); 
			data.putExtra(Intent.EXTRA_TEXT, writer.toString()); 
			mActivity.startActivity(Intent.createChooser(data, mActivity.getString(R.string.application_crashed))); 
			mActivity.finish();
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(10);
//		}
//		else
//		{
//			Thread.getDefaultUncaughtExceptionHandler().uncaughtException(thread, throwable);    
//		}
	}

}
