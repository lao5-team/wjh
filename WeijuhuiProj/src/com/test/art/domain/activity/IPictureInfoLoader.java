package com.test.art.domain.activity;

import java.util.ArrayList;

import com.test.art.data.MyArtUser;
import com.test.art.data.PictureInfo;

public interface IPictureInfoLoader {
	ArrayList<PictureInfo> getPictureList();
	ArrayList<MyArtUser> getUserList(ArrayList<PictureInfo> pictureList);
}
