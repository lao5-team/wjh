package com.test.weijuhui.receiver;

import org.json.JSONException;
import org.json.JSONObject;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.easemob.chat.EMMessage.ChatType;
import com.test.weijuhui.data.ActivityData;
import com.test.weijuhui.data.DianpingDao.ComplexBusiness;
import com.test.weijuhui.domain.ActivityManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NewActivityBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String username = intent.getStringExtra("from");
		String msgid = intent.getStringExtra("msgid");
		// 收到这个广播的时候，message已经在db和内存里了，可以通过id获取mesage对象
		EMMessage message = EMChatManager.getInstance().getMessage(msgid);
		// 如果是群聊消息，获取到group id
		if (message.getChatType() == ChatType.GroupChat) {
			username = message.getTo();
		}
		ActivityData data;
        if(message.getType() == EMMessage.Type.TXT)
        {
        	try {
				JSONObject obj = new JSONObject(((TextMessageBody)message.getBody()).getMessage());
            	data = ActivityData.fromJSON(obj);
    			ActivityManager.getInstance().addActivity(data);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			abortBroadcast();
        }

	}

}
