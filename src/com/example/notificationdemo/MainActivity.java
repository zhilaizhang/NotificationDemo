package com.example.notificationdemo;


import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.Notification.Action;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RemoteViews;
import android.os.Build;

/**
 * PendingIntent 可以看作是对intent的包装，通常通过getActivity,getBroadcast ,getService来得到pendingintent的实例，当前activity并不能马上启动它所包含的intent,而是在外部执行 pendingintent时，调用intent的。
 * 正由于pendingintent中 保存有当前App的Context，使它赋予外部App一种能力，使得外部App可以如同当前App一样的执行pendingintent里的 Intent， 就算在执行时当前App已经不存在了，也能通过存在pendingintent里的Context照样执行Intent。
 * 另外还可以处理intent执行后的操作。常和alermanger 和notificationmanager一起使用。 
  Intent一般是用作Activity、Sercvice、BroadcastReceiver之间传递数据，而Pendingintent，一般用在 Notification上，可以理解为延迟执行的intent，PendingIntent是对Intent一个包装。
 * @author zhangzhilai
 *
 */
@SuppressLint("NewApi")
public class MainActivity extends ActionBarActivity {

	private NotificationManager mNotificationManager;
	private Button mSendNoificationButton;
	private Button mDeleteNoificationButton;
	private Button mUpdateNoificationButton;
	private Button mCustomNoificationButton;
	private ServiceReceiver mServiceReceiver;
	
	private final int NOTIFICATION_ID = 100;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initViews();
		initData();
		initListener();
	}

	private void initViews() {
		mSendNoificationButton = (Button) findViewById(R.id.send_notification_button);
		mDeleteNoificationButton = (Button) findViewById(R.id.delete_notification_button);
		mUpdateNoificationButton = (Button) findViewById(R.id.update_notification_button);
		mCustomNoificationButton = (Button) findViewById(R.id.custom_notification_button);
	}

	private void initData() {
		mNotificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		mServiceReceiver = new ServiceReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ServiceReceiver.NOTIFICATION_ITEM_BUTTON_LAST);
		intentFilter.addAction(ServiceReceiver.NOTIFICATION_ITEM_BUTTON_PLAY);
		intentFilter.addAction(ServiceReceiver.NOTIFICATION_ITEM_BUTTON_NEXT);
		registerReceiver(mServiceReceiver, intentFilter);
	}

	private void initListener() {
		mSendNoificationButton.setOnClickListener(NotifactionListener);
		mDeleteNoificationButton.setOnClickListener(NotifactionListener);
		mUpdateNoificationButton.setOnClickListener(NotifactionListener);
		mCustomNoificationButton.setOnClickListener(NotifactionListener);
	}

	OnClickListener NotifactionListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.send_notification_button:
				sendNotification();
				break;
			case R.id.delete_notification_button:
				deletNotification();
				break;
			case R.id.update_notification_button:
				updateNotification();
				break;
			case R.id.custom_notification_button:
				customNotification();
				break;
			}
		}

	};
	private void sendNotification() {
		Intent intentNotification = new Intent();
		intentNotification.setClass(MainActivity.this, JumpActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, intentNotification, 0);
		//intent英文意思是意图，pending表示即将发生或来临的事情。PendingIntent这个类用于处理即将发生的事情 
		Notification notify = new Notification.Builder(this) //新建通知对象
		.setAutoCancel(true)    //设置点击状态栏中的通知，该通知自动消失
		.setTicker("测试消息")   //这个是当通知到来时，显示在屏幕头上状态栏的消息提醒，几秒后会消失
		.setContentTitle("标题") //下拉状态栏，通知的详情的标题
		.setContentInfo("内容")  //下拉状态栏，通知的详情的内容
		.setSmallIcon(R.drawable.ic_launcher)   //通知的icon
		.setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_LIGHTS) //设置默认的呼吸灯和声音及震动
		.setContentIntent(pendingIntent)           //点击通知会启动的activity
		.build();
		mNotificationManager.notify(NOTIFICATION_ID,notify);  //执行通知,定义此通知的ID，方便取消这条通知
	}
	
	private void deletNotification() {
		mNotificationManager.cancel(NOTIFICATION_ID); //取消这条通知
	}
	private void updateNotification() {
		Intent intentNotification = new Intent();
		intentNotification.setClass(MainActivity.this, JumpActivity.class); //点击通知会跳转到JumpActivity页面
		PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, intentNotification, 0);
		//intent英文意思是意图，pending表示即将发生或来临的事情。PendingIntent这个类用于处理即将发生的事情 
		Notification notify = new Notification.Builder(this) //新建通知对象
		.setAutoCancel(true)    //设置点击状态栏中的通知，该通知自动消失
		.setTicker("更新测试消息")   //这个是当通知到来时，显示在屏幕头上状态栏的消息提醒，几秒后会消失
		.setContentTitle("更新标题") //下拉状态栏，通知的详情的标题
		.setContentInfo("更新内容")  //下拉状态栏，通知的详情的内容
		.setSmallIcon(R.drawable.ic_launcher)   //通知的icon
		.setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_LIGHTS) //设置默认的呼吸灯和声音及震动
		.setContentIntent(pendingIntent)           //点击通知会启动的activity
		.build();
		notify.setLatestEventInfo(this, "更新测试消息1", "更新内容1", pendingIntent);
		mNotificationManager.notify(NOTIFICATION_ID,notify);
	}
	/**
	 * 自定义通知
	 * 功能：1.能实现通知栏中点击事件的监听，及进行操作（比如上一首或者下一首）
	 *      2.点击整个通知会进行跳转到指定的activity
	 *      3.用到了广播
	 * 注意：需要判断系统版本，网友说3.0以下是 不能触发事件的     
	 */
	private void customNotification() {
		Notification notification = new Notification(R.drawable.item, "通知",  System.currentTimeMillis()); //新建通知对象
		Intent intentNotification = new Intent();
		intentNotification.setClass(MainActivity.this, JumpActivity.class); //点击通知会跳转到JumpActivity页面
		PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, intentNotification, 0);
		
		//自定义布局  
	    RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notification);   
	    Intent buttonPlayIntent = new Intent(ServiceReceiver.NOTIFICATION_ITEM_BUTTON_LAST); //新建上一曲按钮意图
		PendingIntent pendButtonPlayIntent = PendingIntent.getBroadcast(this, 0, buttonPlayIntent, 0);
		contentView.setOnClickPendingIntent(R.id.sound_last_button, pendButtonPlayIntent);//
		
		Intent buttonPlayIntent1 = new Intent(ServiceReceiver.NOTIFICATION_ITEM_BUTTON_PLAY); //
		PendingIntent pendButtonPlayIntent1 = PendingIntent.getBroadcast(this, 0, buttonPlayIntent1, 0);
		contentView.setOnClickPendingIntent(R.id.sound_play_button, pendButtonPlayIntent1);//
		
		Intent buttonPlayIntent2 = new Intent(ServiceReceiver.NOTIFICATION_ITEM_BUTTON_NEXT); //
		PendingIntent pendButtonPlayIntent2 = PendingIntent.getBroadcast(this, 0, buttonPlayIntent2, 0);
		contentView.setOnClickPendingIntent(R.id.sound_next_button, pendButtonPlayIntent2);//
	    contentView.setImageViewResource(R.id.image, R.drawable.ico_qq);  
	    
	    notification.contentView = contentView; 
	    notification.contentIntent = pendingIntent;
	    mNotificationManager.notify(NOTIFICATION_ID,notification);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


}
