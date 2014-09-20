package com.test.weijuhui.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import com.easemob.chat.ConnectionListener;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactListener;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMNotifier;
import com.easemob.chat.GroupChangeListener;
import com.easemob.chat.TextMessageBody;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.EMMessage.Type;
import com.test.weijuhui.Constant;
import com.test.weijuhui.DemoApplication;
import com.test.weijuhui.R;
import com.test.weijuhui.activity.MainActivity;
import com.test.weijuhui.data.ActivityData;
import com.test.weijuhui.data.InviteMessgeDao;
import com.test.weijuhui.data.UserDao;
import com.test.weijuhui.data.DianpingDao.ComplexBusiness;
import com.test.weijuhui.domain.ActivityManager;
import com.test.weijuhui.domain.InviteMessage;
import com.test.weijuhui.domain.User;
import com.test.weijuhui.domain.InviteMessage.InviteMesageStatus;
import com.test.weijuhui.service.ActivityService;
import com.test.weijuhui.utils.CommonUtils;
import com.easemob.util.HanziToPinyin;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class EntryActivity extends FragmentActivity {
	protected static final String TAG = "MainActivity";
	// 未读消息textview
	private TextView unreadLabel;
	// 未读通讯录textview
	private TextView unreadAddressLable;

	private Button[] mTabs;
	private ActivityListFragment mActivityListFragment;
	//private SettingsFragment settingFragment;
	private ContactlistFragment mContactListFragment;
	private Fragment[] fragments;
	private int index;
	private RelativeLayout[] tab_containers;
	// 当前fragment的index
	private int currentTabIndex;
	private NewMessageBroadcastReceiver msgReceiver;
	// 账号在别处登录
	private boolean isConflict = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.entry_activity);
		initView();
		inviteMessgeDao = new InviteMessgeDao(this);
		userDao = new UserDao(this);
		mActivityListFragment = new ActivityListFragment();
		mContactListFragment = new ContactlistFragment();
//		settingFragment = new SettingsFragment();
		fragments = new Fragment[] { mActivityListFragment, mContactListFragment };
//		// 添加显示第一个fragment
		getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, mActivityListFragment)
				.show(mActivityListFragment).commit();

		// 注册一个接收消息的BroadcastReceiver
		msgReceiver = new NewMessageBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter(EMChatManager.getInstance().getNewMessageBroadcastAction());
		intentFilter.setPriority(3);
		registerReceiver(msgReceiver, intentFilter);

		// 注册一个ack回执消息的BroadcastReceiver
		IntentFilter ackMessageIntentFilter = new IntentFilter(EMChatManager.getInstance()
				.getAckMessageBroadcastAction());
		ackMessageIntentFilter.setPriority(3);
		registerReceiver(ackMessageReceiver, ackMessageIntentFilter);

		// setContactListener监听联系人的变化等
		EMContactManager.getInstance().setContactListener(new MyContactListener());
		// 注册一个监听连接状态的listener
		EMChatManager.getInstance().addConnectionListener(new MyConnectionListener());
		// 注册群聊相关的listener
		EMGroupManager.getInstance().addGroupChangeListener(new MyGroupChangeListener());
		// 通知sdk，UI 已经初始化完毕，注册了相应的receiver和listener, 可以接受broadcast了
		EMChat.getInstance().setAppInited();
		
		//doBindService();
	}

	/**
	 * 初始化组件
	 */
	private void initView() {
		unreadLabel = (TextView) findViewById(R.id.unread_msg_number);
		unreadAddressLable = (TextView) findViewById(R.id.unread_address_number);
		mTabs = new Button[3];
		mTabs[0] = (Button) findViewById(R.id.btn_conversation);
		mTabs[1] = (Button) findViewById(R.id.btn_address_list);
		mTabs[2] = (Button) findViewById(R.id.btn_setting);
		// 把第一个tab设为选中状态
		mTabs[0].setSelected(true);

	}

	/**
	 * button点击事件
	 * 
	 * @param view
	 */
	public void onTabClicked(View view) {
		switch (view.getId()) {
		case R.id.btn_conversation:
			index = 0;
			break;
		case R.id.btn_address_list:
			index = 1;
			break;
		case R.id.btn_setting:
			index = 2;
			break;
		}
		if (currentTabIndex != index) {
			FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
			//getSupportFragmentManager().getFragments().clear();
			trx.hide(fragments[currentTabIndex]);
			if (!fragments[index].isAdded()) {
				trx.add(R.id.fragment_container, fragments[index]);
			}
			trx.show(fragments[index]).commit();
		}
		mTabs[currentTabIndex].setSelected(false);
		// 把当前tab设为选中状态
		mTabs[index].setSelected(true);
		currentTabIndex = index;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 注销广播接收者
		try {
			unregisterReceiver(msgReceiver);
		} catch (Exception e) {
		}
		try {
			unregisterReceiver(ackMessageReceiver);
		} catch (Exception e) {
		}

		if (conflictBuilder != null) {
			conflictBuilder.create().dismiss();
			conflictBuilder = null;
		}
		doUnbindService();

	}

	/**
	 * 刷新未读消息数
	 */
	public void updateUnreadLabel() {
		int count = getUnreadMsgCountTotal();
		if (count > 0) {
			unreadLabel.setText(String.valueOf(count));
			unreadLabel.setVisibility(View.VISIBLE);
		} else {
			unreadLabel.setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * 刷新申请与通知消息数
	 */
	public void updateUnreadAddressLable() {
		runOnUiThread(new Runnable() {
			public void run() {
				int count = getUnreadAddressCountTotal();
				if (count > 0) {
					unreadAddressLable.setText(String.valueOf(count));
					unreadAddressLable.setVisibility(View.VISIBLE);
				} else {
					unreadAddressLable.setVisibility(View.INVISIBLE);
				}
			}
		});

	}

	/**
	 * 获取未读申请与通知消息
	 * 
	 * @return
	 */
	public int getUnreadAddressCountTotal() {
		int unreadAddressCountTotal = 0;
		if (DemoApplication.getInstance().getContactList().get(Constant.NEW_FRIENDS_USERNAME) != null)
			unreadAddressCountTotal = DemoApplication.getInstance().getContactList().get(Constant.NEW_FRIENDS_USERNAME)
					.getUnreadMsgCount();
		return unreadAddressCountTotal;
	}

	/**
	 * 获取未读消息数
	 * 
	 * @return
	 */
	public int getUnreadMsgCountTotal() {
		int unreadMsgCountTotal = 0;
		unreadMsgCountTotal = EMChatManager.getInstance().getUnreadMsgsCount();
		return unreadMsgCountTotal;
	}

	/**
	 * 新消息广播接收者
	 * 
	 * 
	 */
	private class NewMessageBroadcastReceiver extends BroadcastReceiver {
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
			// conversation =
			// EMChatManager.getInstance().getConversation(toChatUsername);
			// 通知adapter有新消息，更新ui
			// 记得把广播给终结掉
			ActivityData data;
            if(message.getType() == EMMessage.Type.TXT)
            {
            	//data.mCB.mName = ((TextMessageBody)message.getBody()).getMessage();
            	ComplexBusiness cb = new ComplexBusiness();
            	try {
					JSONObject obj = new JSONObject(((TextMessageBody)message.getBody()).getMessage());
	            	data = ActivityData.fromJSON(obj);
	    			ActivityManager.getInstance().addActivity(data);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	//cb.mName = ;

    			//showNotification(cb.mName);
    			abortBroadcast();
            }
		}
	}

	/**
	 * 消息回执BroadcastReceiver
	 */
	private BroadcastReceiver ackMessageReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String msgid = intent.getStringExtra("msgid");
			String from = intent.getStringExtra("from");
			EMConversation conversation = EMChatManager.getInstance().getConversation(from);
			if (conversation != null) {
				// 把message设为已读
				EMMessage msg = conversation.getMessage(msgid);
				if (msg != null) {
					msg.isAcked = true;
				}
			}
			abortBroadcast();
		}
	};

	private InviteMessgeDao inviteMessgeDao;
	private UserDao userDao;

	/***
	 * 联系人变化listener
	 * 
	 */
	private class MyContactListener implements EMContactListener {

		@Override
		public void onContactAdded(List<String> usernameList) {
			// 保存增加的联系人
			Map<String, User> localUsers = DemoApplication.getInstance().getContactList();
			Map<String, User> toAddUsers = new HashMap<String, User>();
			for (String username : usernameList) {
				User user = new User();
				user.setUsername(username);
				String headerName = null;
				if (!TextUtils.isEmpty(user.getNick())) {
					headerName = user.getNick();
				} else {
					headerName = user.getUsername();
				}
				if (username.equals(Constant.NEW_FRIENDS_USERNAME)) {
					user.setHeader("");
				} else if (Character.isDigit(headerName.charAt(0))) {
					user.setHeader("#");
				} else {
					user.setHeader(HanziToPinyin.getInstance().get(headerName.substring(0, 1)).get(0).target.substring(
							0, 1).toUpperCase());
					char header = user.getHeader().toLowerCase().charAt(0);
					if (header < 'a' || header > 'z') {
						user.setHeader("#");
					}
				}
				// 暂时有个bug，添加好友时可能会回调added方法两次
				if (!localUsers.containsKey(username)) {
					userDao.saveContact(user);
				}
				toAddUsers.put(username, user);
			}
			localUsers.putAll(toAddUsers);
			// 刷新ui
			//if (currentTabIndex == 1)
				//contactListFragment.refresh();

		}

		@Override
		public void onContactDeleted(List<String> usernameList) {
			// 被删除
			Map<String, User> localUsers = DemoApplication.getInstance().getContactList();
			for (String username : usernameList) {
				localUsers.remove(username);
				userDao.deleteContact(username);
				inviteMessgeDao.deleteMessage(username);
			}
			// 刷新ui
			if (currentTabIndex == 1)
				//contactListFragment.refresh();
			updateUnreadLabel();

		}

		@Override
		public void onContactInvited(String username, String reason) {
			// 接到邀请的消息，如果不处理(同意或拒绝)，掉线后，服务器会自动再发过来，所以客户端不要重复提醒
			List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();
			for (InviteMessage inviteMessage : msgs) {
				if (inviteMessage.getGroupId() == null && inviteMessage.getFrom().equals(username)) {
					return;
				}
			}
			// 自己封装的javabean
			InviteMessage msg = new InviteMessage();
			msg.setFrom(username);
			msg.setTime(System.currentTimeMillis());
			msg.setReason(reason);
			Log.d(TAG, username + "请求加你为好友,reason: " + reason);
			// 设置相应status
			msg.setStatus(InviteMesageStatus.BEINVITEED);
			notifyNewIviteMessage(msg);

		}

		@Override
		public void onContactAgreed(String username) {
			List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();
			for (InviteMessage inviteMessage : msgs) {
				if (inviteMessage.getFrom().equals(username)) {
					return;
				}
			}
			// 自己封装的javabean
			InviteMessage msg = new InviteMessage();
			msg.setFrom(username);
			msg.setTime(System.currentTimeMillis());
			Log.d(TAG, username + "同意了你的好友请求");
			msg.setStatus(InviteMesageStatus.BEAGREED);
			notifyNewIviteMessage(msg);

		}

		@Override
		public void onContactRefused(String username) {
			// 参考同意，被邀请实现此功能,demo未实现

		}

	}

	/**
	 * 保存提示新消息
	 * 
	 * @param msg
	 */
	private void notifyNewIviteMessage(InviteMessage msg) {
		saveInviteMsg(msg);
		// 提示有新消息
		EMNotifier.getInstance(getApplicationContext()).notifyOnNewMsg();

		// 刷新bottom bar消息未读数
		updateUnreadAddressLable();
		// 刷新好友页面ui
//		if (currentTabIndex == 1)
//			contactListFragment.refresh();
	}
	/**
	 * 保存邀请等msg
	 * @param msg
	 */
	private void saveInviteMsg(InviteMessage msg) {
		// 保存msg
		inviteMessgeDao.saveMessage(msg);
		// 未读数加1
		User user = DemoApplication.getInstance().getContactList().get(Constant.NEW_FRIENDS_USERNAME);
		user.setUnreadMsgCount(user.getUnreadMsgCount() + 1);
	}

	/**
	 * 连接监听listener
	 * 
	 */
	private class MyConnectionListener implements ConnectionListener {

		@Override
		public void onConnected() {
			//chatHistoryFragment.errorItem.setVisibility(View.GONE);
		}

		@Override
		public void onDisConnected(String errorString) {
			if (errorString != null && errorString.contains("conflict")) {
				// 显示帐号在其他设备登陆dialog
				showConflictDialog();
			} else {
//				chatHistoryFragment.errorItem.setVisibility(View.VISIBLE);
//				chatHistoryFragment.errorText.setText("连接不到聊天服务器");
			}
		}

		@Override
		public void onReConnected() {
			//chatHistoryFragment.errorItem.setVisibility(View.GONE);
		}

		@Override
		public void onReConnecting() {
		}

		@Override
		public void onConnecting(String progress) {
		}

	}

	/**
	 * MyGroupChangeListener
	 */
	private class MyGroupChangeListener implements GroupChangeListener {

		@Override
		public void onInvitationReceived(String groupId, String groupName, String inviter, String reason) {
			// 被邀请
			EMMessage msg = EMMessage.createReceiveMessage(Type.TXT);
			msg.setChatType(ChatType.GroupChat);
			msg.setFrom(inviter);
			msg.setTo(groupId);
			msg.setMsgId(UUID.randomUUID().toString());
			msg.addBody(new TextMessageBody(inviter + "邀请你加入了群聊"));
			// 保存邀请消息
			EMChatManager.getInstance().saveMessage(msg);
			// 提醒新消息
			EMNotifier.getInstance(getApplicationContext()).notifyOnNewMsg();

			runOnUiThread(new Runnable() {
				public void run() {
					updateUnreadLabel();
					// 刷新ui
//					if (currentTabIndex == 0)
//						chatHistoryFragment.refresh();
//					if (CommonUtils.getTopActivity(EntryActivity.this).equals(GroupsActivity.class.getName())) {
//						GroupsActivity.instance.onResume();
//					}
				}
			});

		}

		@Override
		public void onInvitationAccpted(String groupId, String inviter, String reason) {

		}

		@Override
		public void onInvitationDeclined(String groupId, String invitee, String reason) {

		}

		@Override
		public void onUserRemoved(String groupId, String groupName) {
			// 提示用户被T了，demo省略此步骤
			// 刷新ui
			runOnUiThread(new Runnable() {
				public void run() {
					try {
						updateUnreadLabel();
//						if (currentTabIndex == 0)
//							chatHistoryFragment.refresh();
//						if (CommonUtils.getTopActivity(EntryActivity.this).equals(GroupsActivity.class.getName())) {
//							GroupsActivity.instance.onResume();
//						}
					} catch (Exception e) {
						Log.e("###", "refresh exception " + e.getMessage());
					}

				}
			});
		}

		@Override
		public void onGroupDestroy(String groupId, String groupName) {
			// 群被解散
			// 提示用户群被解散,demo省略
			// 刷新ui
			runOnUiThread(new Runnable() {
				public void run() {
					updateUnreadLabel();
//					if (currentTabIndex == 0)
//						chatHistoryFragment.refresh();
//					if (CommonUtils.getTopActivity(MainActivity.this).equals(GroupsActivity.class.getName())) {
//						GroupsActivity.instance.onResume();
//					}
				}
			});

		}

		@Override
		public void onApplicationReceived(String groupId, String groupName, String applyer, String reason) {
			// 用户申请加入群聊
			InviteMessage msg = new InviteMessage();
			msg.setFrom(applyer);
			msg.setTime(System.currentTimeMillis());
			msg.setGroupId(groupId);
			msg.setGroupName(groupName);
			msg.setReason(reason);
			Log.d(TAG, applyer + " 申请加入群聊：" + groupName);
			msg.setStatus(InviteMesageStatus.BEAPPLYED);
			notifyNewIviteMessage(msg);
		}

		@Override
		public void onApplicationAccept(String groupId, String groupName, String accepter) {
			//加群申请被同意
			EMMessage msg = EMMessage.createReceiveMessage(Type.TXT);
			msg.setChatType(ChatType.GroupChat);
			msg.setFrom(accepter);
			msg.setTo(groupId);
			msg.setMsgId(UUID.randomUUID().toString());
			msg.addBody(new TextMessageBody(accepter + "同意了你的群聊申请"));
			// 保存同意消息
			EMChatManager.getInstance().saveMessage(msg);
			// 提醒新消息
			EMNotifier.getInstance(getApplicationContext()).notifyOnNewMsg();
			
			runOnUiThread(new Runnable() {
				public void run() {
					updateUnreadLabel();
					// 刷新ui
//					if (currentTabIndex == 0)
//						chatHistoryFragment.refresh();
//					if (CommonUtils.getTopActivity(EntryActivity.this).equals(GroupsActivity.class.getName())) {
//						GroupsActivity.instance.onResume();
//					}
				}
			});
		}

		@Override
		public void onApplicationDeclined(String groupId, String groupName, String decliner, String reason) {
			//加群申请被拒绝，demo未实现
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (!isConflict) {
			updateUnreadLabel();
			updateUnreadAddressLable();
			EMChatManager.getInstance().activityResumed();
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			moveTaskToBack(false);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private android.app.AlertDialog.Builder conflictBuilder;
	private boolean isConflictDialogShow;

	/**
	 * 显示帐号在别处登录dialog
	 */
	private void showConflictDialog() {
		isConflictDialogShow = true;
		DemoApplication.getInstance().logout();

		if (!EntryActivity.this.isFinishing()) {
			// clear up global variables
			try {
				if (conflictBuilder == null)
					conflictBuilder = new android.app.AlertDialog.Builder(EntryActivity.this);
				conflictBuilder.setTitle("下线通知");
				conflictBuilder.setMessage(R.string.connect_conflict);
				conflictBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						conflictBuilder = null;
						finish();
						startActivity(new Intent(EntryActivity.this, LoginActivity.class));
					}
				});
				conflictBuilder.setCancelable(false);
				conflictBuilder.create().show();
				isConflict = true;
			} catch (Exception e) {
				Log.e("###", "---------color conflictBuilder error" + e.getMessage());
			}

		}

	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if(getIntent().getBooleanExtra("conflict", false) && !isConflictDialogShow)
			showConflictDialog();
	}
	
	/** Messenger for communicating with service. */
	Messenger mService = null;
	/** Flag indicating whether we have called bind on the service. */
	boolean mIsBound;
	/** Some text view we are using to show state information. */
	TextView mCallbackText;

	/**
	 * Handler of incoming messages from service.
	 */
	class IncomingHandler extends Handler {
	    @Override
	    public void handleMessage(Message msg) {
	        switch (msg.what) {
	            case ActivityService.MSG_SET_VALUE:
	                mCallbackText.setText("Received from service: " + msg.arg1);
	                break;
	            default:
	                super.handleMessage(msg);
	        }
	    }
	}

	/**
	 * Target we publish for clients to send messages to IncomingHandler.
	 */
	final Messenger mMessenger = new Messenger(new IncomingHandler());

	/**
	 * Class for interacting with the main interface of the service.
	 */
	private ServiceConnection mConnection = new ServiceConnection() {
	    public void onServiceConnected(ComponentName className,
	            IBinder service) {
	        // This is called when the connection with the service has been
	        // established, giving us the service object we can use to
	        // interact with the service.  We are communicating with our
	        // service through an IDL interface, so get a client-side
	        // representation of that from the raw service object.
	        mService = new Messenger(service);
	        mCallbackText.setText("Attached.");

	        // We want to monitor the service for as long as we are
	        // connected to it.
	        try {
	            Message msg = Message.obtain(null,
	                    ActivityService.MSG_REGISTER_CLIENT);
	            msg.replyTo = mMessenger;
	            mService.send(msg);

	            // Give it some value as an example.
	            msg = Message.obtain(null,
	            		ActivityService.MSG_SET_VALUE, this.hashCode(), 0);
	            mService.send(msg);
	        } catch (RemoteException e) {
	            // In this case the service has crashed before we could even
	            // do anything with it; we can count on soon being
	            // disconnected (and then reconnected if it can be restarted)
	            // so there is no need to do anything here.
	        }

	        // As part of the sample, tell the user what happened.
	       // Toast.makeText(this, R.string.remote_service_connected,
	       //         Toast.LENGTH_SHORT).show();
	    }

	    public void onServiceDisconnected(ComponentName className) {
	        // This is called when the connection with the service has been
	        // unexpectedly disconnected -- that is, its process crashed.
	        mService = null;
	        mCallbackText.setText("Disconnected.");

	        // As part of the sample, tell the user what happened.
	        //Toast.makeText(this, R.string.remote_service_disconnected,
	        //        Toast.LENGTH_SHORT).show();
	    }
	};

	void doBindService() {
	    // Establish a connection with the service.  We use an explicit
	    // class name because there is no reason to be able to let other
	    // applications replace our component.
	    bindService(new Intent(this, 
	    		ActivityService.class), mConnection, Context.BIND_AUTO_CREATE);
	    mIsBound = true;
	    mCallbackText.setText("Binding.");
	}

	void doUnbindService() {
	    if (mIsBound) {
	        // If we have received the service, and hence registered with
	        // it, then now is the time to unregister.
	        if (mService != null) {
	            try {
	                Message msg = Message.obtain(null,
	                		ActivityService.MSG_UNREGISTER_CLIENT);
	                msg.replyTo = mMessenger;
	                mService.send(msg);
	            } catch (RemoteException e) {
	                // There is nothing special we need to do if the service
	                // has crashed.
	            }
	        }

	        // Detach our existing connection.
	        unbindService(mConnection);
	        mIsBound = false;
	        mCallbackText.setText("Unbinding.");
	    }
	}
}
