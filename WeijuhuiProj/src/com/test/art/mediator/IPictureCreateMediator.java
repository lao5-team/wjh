package com.test.art.mediator;

import java.util.ArrayList;

import android.graphics.Bitmap;

import com.test.art.data.CommentInfo;
import com.test.art.data.MyArtUser;
import com.test.art.data.PictureInfo;

public interface IPictureCreateMediator {
	public void showPicture(Bitmap bmp);
	public void uploadPictureInfo();
	public PictureInfo createPictureInfo();
	public String uploadPicture(Bitmap bmp);
}
