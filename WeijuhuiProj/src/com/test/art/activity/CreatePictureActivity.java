package com.test.art.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.test.art.Constant;
import com.test.art.DemoApplication;
import com.test.art.R;
import com.test.art.data.CommentInfo;
import com.test.art.data.MyArtUser;
import com.test.art.data.PictureInfo;
import com.test.art.domain.MyServerManager;
import com.test.art.mediator.IPictureCreateMediator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class CreatePictureActivity extends Activity {

	private String[] items = new String[] { "选择本地图片", "拍照" };
	private String IMAGE_PIC_PATH = Constant.CACHE_DIR + File.separator;
	private String IMAGE_PIC_CACHE = Constant.CACHE_DIR + File.separator + "cache.jpg";
	private static final int IMAGE_REQUEST_CODE = 0;
	private static final int CAMERA_REQUEST_CODE = 1;
	private static final int RESULT_REQUEST_CODE = 2;
	private EditText mEtxTitle;
	private Button mBtnConfirm;
	private Button mBtnCancel;
	private ImageView mImgPic;
	private Bitmap mBitmap;
	IPictureCreateMediator mMediator = new IPictureCreateMediator() {
		
		@Override
		public void showPicture(Bitmap bmp) {
			Drawable drawable = new BitmapDrawable(mBitmap);
			mImgPic.setImageDrawable(drawable);
		}

		@Override
		public void uploadPictureInfo() {
			PictureInfo info = createPictureInfo();
			MyServerManager.getInstance().createPicture(info);
		}

		@Override
		public PictureInfo createPictureInfo() {
			// TODO Auto-generated method stub
			PictureInfo info = new PictureInfo();
			//info.mID = 
			info.mUserName = DemoApplication.getInstance().getUser().mName;
			info.mTitle = mEtxTitle.getEditableText().toString();
			if(null== (info.mUrl = uploadPicture(mBitmap)))
			{
				return null;
			}
			return info;
		}

		@Override
		public String uploadPicture(Bitmap bmp) {
			final Bitmap fBmp = bmp;
			Callable<String> callable = new Callable<String>() {
				
				@Override
				public String call() throws Exception {
					FileOutputStream fos;
					String imgUrl = null;
					try {
						String strDate = String.format("%d%d%d%d%d%d", 
								Calendar.getInstance().get(Calendar.YEAR),
								Calendar.getInstance().get(Calendar.MONTH),
								Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
								Calendar.getInstance().get(Calendar.HOUR),
								Calendar.getInstance().get(Calendar.MINUTE),
								Calendar.getInstance().get(Calendar.SECOND));
						Log.v(DemoApplication.TAG, "uploadPicture strDate is " + strDate);
						fos = new FileOutputStream(IMAGE_PIC_PATH + strDate + ".jpg");
						fBmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
						imgUrl = MyServerManager.getInstance()
							.uploadImage(new File(IMAGE_PIC_PATH + strDate + ".jpg"));
					if (null != imgUrl) {
						CreatePictureActivity.this
								.runOnUiThread(new Runnable() {

									@Override
									public void run() {
										Toast.makeText(
												CreatePictureActivity.this,
												"图片上传成功",
												Toast.LENGTH_SHORT).show();
									}
								});
					} else {
						CreatePictureActivity.this
								.runOnUiThread(new Runnable() {
									@Override
									public void run() {
										Toast.makeText(
												CreatePictureActivity.this,
												"图片上传失败",
												Toast.LENGTH_SHORT).show();
									}
								});
					}
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Toast.makeText(CreatePictureActivity.this, "图片上传失败",
								Toast.LENGTH_SHORT).show();
					} // TODO Auto-generated method stub
					
					return imgUrl;
				}
			};
			
			Future<String>future = Executors.newSingleThreadExecutor().submit(callable);
			try {
				return future.get();
			} catch (InterruptedException e) {
				e.printStackTrace();
				return null;
			} catch (ExecutionException e) {
				e.printStackTrace();
				return null;
			}
		}
		
		
		
		
	};
	@Override
	protected void onCreate(Bundle savedInstance)
	{
		super.onCreate(savedInstance);
		
		initUI();
		initData();
	}

	private void initData() {
		// TODO Auto-generated method stub
		
	}

	private void initUI() {
		setContentView(R.layout.activity_createpicture);
		mEtxTitle = (EditText)findViewById(R.id.editText_title);
		mBtnConfirm = (Button)findViewById(R.id.button_OK);
		mBtnConfirm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mMediator.uploadPictureInfo();
				CreatePictureActivity.this.finish();
			}
		});
		mBtnCancel = (Button)findViewById(R.id.button_Cancel);
		mBtnCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				CreatePictureActivity.this.finish();
			}
		});
		mImgPic = (ImageView)findViewById(R.id.imageView_picture);
		mImgPic.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showDialog();
			}
		});
	}
	
	
	private void showDialog() {

		new AlertDialog.Builder(this)
				.setTitle("设置头像")
				.setItems(items, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							Intent intentFromGallery = new Intent();
							intentFromGallery.setType("image/*"); // 设置文件类型
							intentFromGallery
									.setAction(Intent.ACTION_GET_CONTENT);
							startActivityForResult(intentFromGallery,
									IMAGE_REQUEST_CODE);
							break;
						case 1:

							Intent intentFromCapture = new Intent(
									MediaStore.ACTION_IMAGE_CAPTURE);
							// 判断存储卡是否可以用，可用进行存储
							if (hasSdcard()) {

								intentFromCapture.putExtra(
										MediaStore.EXTRA_OUTPUT,
										Uri.fromFile(new File(IMAGE_PIC_CACHE)));
							}

							startActivityForResult(intentFromCapture,
									CAMERA_REQUEST_CODE);
							break;
						}
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		//结果码不等于取消时候
		if (resultCode != Activity.RESULT_CANCELED) {

			switch (requestCode) {
			case IMAGE_REQUEST_CODE:
				startPhotoZoom(data.getData());
				break;
			case CAMERA_REQUEST_CODE:
				if (hasSdcard()) {
					File tempFile = new File(IMAGE_PIC_CACHE);
					startPhotoZoom(Uri.fromFile(tempFile));
				} else {
					Toast.makeText(this, "未找到存储卡，无法存储照片！",
							Toast.LENGTH_LONG).show();
				}

				break;
			case RESULT_REQUEST_CODE:
				if (data != null) {
					getImageToView(data);
				}
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {

		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 设置裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 320);
		intent.putExtra("outputY", 320);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, RESULT_REQUEST_CODE);
	}

	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param picdata
	 */
	private void getImageToView(Intent data) {
		Bundle extras = data.getExtras();
		if (extras != null) {
			mBitmap = extras.getParcelable("data");
			mMediator.showPicture(mBitmap);

		}
	}
	
	public  boolean hasSdcard(){
		String state = Environment.getExternalStorageState();
		if(state.equals(Environment.MEDIA_MOUNTED)){
			return true;
		}else{
			return false;
		}
	}
	
}
