package com.szgvtv.ead.app.taijietemplates.service.upgrade;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import javax.net.ssl.HttpsURLConnection;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Dialog;
import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import com.szgvtv.ead.app.taijietemplates_betvytv.R;
import com.szgvtv.ead.app.taijietemplates.dataprovider.http.NetUtil;
import com.szgvtv.ead.app.taijietemplates.util.Constant;
import com.szgvtv.ead.app.taijietemplates.util.FlagConstant;
import com.szgvtv.ead.app.taijietemplates.util.GeneralParam;
import com.szgvtv.ead.app.taijietemplates.util.Logcat;
import com.szgvtv.ead.app.taijietemplates.util.MD5;

public class UpgradeModelService extends IntentService
{
	private static final int BUFFER_SIZE = 100 * 1024;
	private static final int REQUEST_TIMEOUT=30000;
	private static final int READ_TIMEOUT=60000; 
	private static String       mUpgradeName = "/upgradename.apk";
	private UpServiceReceive    mBroadcastReceiver = new UpServiceReceive();
	private Context             mContext;
	private ByteBuffer          mBuffer;
	private UpgradeInfo         mUpgradeInfo;
	private Dialog              mDialog;
	private HttpURLConnection   mConnection = null;
	private BufferedInputStream mBis = null;
	private RandomAccessFile    mFos = null;	
	private boolean             mQuitflag = true;
	private boolean				mPopupgradeflag = false;
	private boolean             mRequestPost = true;
	private byte[]              mBuf = new byte[BUFFER_SIZE];	
	private int                 mReadBufferTotalLen;
	private int                 mFileLength = 0;
	private int                 mFileProgress = 0;
	private int                 mResponseCode = 0;
	
	public enum STATUS 
	{
		SERVICE_NODATA,
		MD5_SAME,
		SERVICE_MD5_NOEXIST,
		LOCAL_MD5_NOEXIST,
		MD5_DIFFENCE,
	}

	public UpgradeModelService() 
	{
		super("UpgradeModelService");
	}

	@Override
	public void onCreate() 
	{
		super.onCreate();
		mContext = this;
		IntentFilter intentFilter = new IntentFilter();
	    intentFilter.addAction("android.intent.action.CLOSE_SYSTEM_DIALOGS");
	    registerReceiver(mBroadcastReceiver, intentFilter);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) 
	{
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onStart(Intent intent, int startId) 
	{
		super.onStart(intent, startId);
	}
	
	@Override
	public IBinder onBind(Intent intent) 
	{
		return super.onBind(intent);
	}

	@Override
	public void onDestroy() 
	{
		super.onDestroy();
		setQuitflag(true);
		if(mHandler != null)
		{
			mHandler.removeCallbacksAndMessages(null);
		}
		
		if(mBroadcastReceiver != null)
		{
			unregisterReceiver(mBroadcastReceiver);
		}
	}
	
	@SuppressLint("HandlerLeak")
	Handler mHandler = new Handler()
	{
		public void handleMessage(android.os.Message msg) 
		{
			switch (msg.what) 
			{
			case 1:
			{
				if (!isBackground(mContext)) 
				{
					if(mUpgradeInfo != null)
					{
						int count = mUpgradeInfo.getUpgradeList().size();
						if(count > 0)
						{
							UpgradePkgItem pkgItem = mUpgradeInfo.mUpgradeList.get(count-1);
							if(pkgItem.getIsforce().equals("1"))
							{
								//强制升级
								onCreatDialog(0, pkgItem);
							}
							else 
							{
								if(!mPopupgradeflag)
								{
									onCreatDialog(0, pkgItem);
								}
							}
							mPopupgradeflag = true;
						}
					}
				} 
				break;
			}
			default:
				break;
			}
		};
	};
	
	@Override
	protected void onHandleIntent(Intent intent) 
	{
		setQuitflag(false);
		while(!getQuitflag())
		{
			UpgradePkgItem pkgItem;
			RequestUpgrade();
			STATUS status = currentEventStatus(mUpgradeInfo);
			Logcat.i(FlagConstant.TAG," status = " + status);
			switch (status) 
			{
			case SERVICE_NODATA:
			case SERVICE_MD5_NOEXIST:
			{
				break;
			}
			case MD5_SAME:
			{
				if(!getQuitflag())
				{
					mHandler.sendEmptyMessage(1);
				}	
				break;
			}
			case LOCAL_MD5_NOEXIST:
			case MD5_DIFFENCE:
			{
				int count = mUpgradeInfo.getUpgradeList().size();
				boolean downSuc = false;
				if(count > 0)
				{
					pkgItem = mUpgradeInfo.mUpgradeList.get(count-1);
					downSuc = downloadApp(pkgItem.getUrl());
					if(downSuc)
					{
						STATUS downStatus = currentEventStatus(mUpgradeInfo);
						if(downStatus == STATUS.MD5_SAME&&!getQuitflag())
						{
							mHandler.sendEmptyMessage(1);
						}
					}
				}
				break;
			}
			default:
				break;
			}
			
			if(getQuitflag())
			{
				break;
			}
			
			try 
			{
				int totalTime = 5*60;//20150105改为定时取升级5分钟
				while (true) 
				{
					Thread.sleep(1000);	
					totalTime--;
					if(totalTime < 0||getQuitflag())
					{
						break;
					}
				}
			} catch (Exception e) {
			}	
		}
	}
	
	public boolean getQuitflag() 
	{
		return mQuitflag;
	}

	public void setQuitflag(boolean mQuitflag) 
	{
		this.mQuitflag = mQuitflag;
	}
	
	private void RequestUpgrade()
	{
		HttpURLConnection conn = null;
		int mResponseCode;
		try 
		{
			if(!NetUtil.isNetConnected(mContext))
			{
				throw new Exception("no net");
			}
			conn = iniConn();
			if (mRequestPost) 
			{
				OutputStream outputStream;
				outputStream = conn.getOutputStream();	
				if(outputStream != null) 
				{
					outputStream.flush();
					outputStream.close();
				}
			}
			mResponseCode = readData(conn);	
		} 
		catch (ProtocolException e) 
		{
			mResponseCode = HttpURLConnection.HTTP_BAD_REQUEST;
		} 
		catch (SocketTimeoutException e) 
		{	
			mResponseCode = HttpURLConnection.HTTP_CLIENT_TIMEOUT;
		} 
		catch (IOException e) 
		{
			//請求不存在 服務器異常
			mResponseCode = HttpURLConnection.HTTP_NOT_FOUND;
		} 
		catch (Exception e) 
		{
			mResponseCode = -999;
		} 
		finally 
		{
			if (conn != null) 
			{
				conn.disconnect();
			}
		}
		Logcat.i(FlagConstant.TAG, " response code: " + mResponseCode + ", mRequestPost: " + mRequestPost);
		
		if (mResponseCode == HttpURLConnection.HTTP_OK && mReadBufferTotalLen > 0)
		{
			mUpgradeInfo = null;
			mUpgradeInfo = parseUpgrade(mBuffer);
		}
	}
	
	public URL serviceURL(Context context) throws ProtocolException, IOException 
	{
		StringBuffer sb = new StringBuffer();
		sb.append(Constant.REQUEST_URL_HOST);
		
		sb.append("/");
		sb.append("upgrade.action?");
		sb.append(GeneralParam.generateGeneralParam(context));
		sb.append("&");
		sb.append("version=");
		sb.append(getAppVersion());
		
		Logcat.i(FlagConstant.TAG, " URL: " + sb.toString());
		return new URL(sb.toString());
	}
	
	private HttpURLConnection iniConn() throws ProtocolException, IOException 
	{
		HttpURLConnection httpConn = null;
		URL url = serviceURL(mContext);
		{
			httpConn = (HttpURLConnection) url.openConnection();
		}
		// 设置超时
		httpConn.setConnectTimeout(15000);
		httpConn.setReadTimeout(15000);
		
		if (mRequestPost) 
		{
			httpConn.setDoOutput(true);
			httpConn.setDoInput(true);
			httpConn.setUseCaches(false);
			httpConn.setRequestMethod("POST");
		} 
		else 
		{
			httpConn.setRequestMethod("GET");
		}
		httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");//输出键值对
		httpConn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
		httpConn.setRequestProperty("Charset", "utf-8");
		httpConn.setRequestProperty("User-Agent","Mozilla/5.0 (Windows; U; Windows NT 5.2) AppleWebKit/525.13 (KHTML, like Gecko) Chrome/0.2.149.27 Safari/525.13");
		httpConn.setRequestProperty("Accept","*/*");
		httpConn.setRequestProperty("Accept-Encoding","identity");
		return httpConn;
	}
	
	private int readData(HttpURLConnection conn) throws SocketTimeoutException, IOException 
	{
		int mResponseCode;
		InputStream inputStream = conn.getInputStream();
		byte[] buffer = new byte[1024 * 10];
		int responseContentLength = conn.getContentLength();
		
		if (responseContentLength == 0) 
		{
			mResponseCode = HttpURLConnection.HTTP_NO_CONTENT;
		} 
		else 
		{
			mResponseCode = conn.getResponseCode();
			if (mResponseCode == HttpURLConnection.HTTP_OK) 
			{
				int len = 0;
				mBuffer = null;
				mReadBufferTotalLen = 0;
				// 循环读取数据
				if (true) 
				{
					while ((len = inputStream.read(buffer)) != -1) 
					{
						putByteToBufer(buffer, 0, len);
						mReadBufferTotalLen += len;
					}
				}
			}
		}
		
		if(inputStream != null) 
		{
			inputStream.close();
		}	
		return mResponseCode;
	}
	
	private void putByteToBufer(byte[] data, int start, int len) 
	{
		if (mBuffer == null) 
		{
			mBuffer = ByteBuffer.allocate(len << 2);
		} 
		else if (mBuffer.remaining() < len) 
		{
			ByteBuffer tmpBufer = ByteBuffer.allocate(mBuffer.capacity() + (len << 2));
			tmpBufer.put(mBuffer.array(), 0, mBuffer.position());
			mBuffer = null;
			mBuffer = tmpBufer;
		}
		mBuffer.put(data, start, len);
	}

	public UpgradeInfo parseUpgrade(ByteBuffer responseBytes)
	{
		UpgradeInfo upgradeInfo = null;
		UpgradePkgItem pkgItem = null;
		ByteArrayInputStream bin = new ByteArrayInputStream(responseBytes.array());
        InputStreamReader in = new InputStreamReader(bin);
		try
		{
			XmlPullParserFactory pullFactory = XmlPullParserFactory.newInstance();     
            XmlPullParser parser = pullFactory.newPullParser();
            parser.setInput(in);
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) 
            {
            	switch (eventType) 
            	{
            	case XmlPullParser.START_DOCUMENT:
            	{
            		break;
            	}
            	case XmlPullParser.START_TAG:
            	{
            		if ("result".equalsIgnoreCase(parser.getName()))
            		{
            			upgradeInfo = new UpgradeInfo();
            			upgradeInfo.setIsupgrade(parser.getAttributeValue(null, "isupgrade"));
            		}
            		else if (upgradeInfo != null)
            		{
            			if ("package".equalsIgnoreCase(parser.getName()))
            			{
            				pkgItem = new UpgradePkgItem();
                		}
            			else if(pkgItem != null)
            			{
                			if ("version".equalsIgnoreCase(parser.getName()))
                			{
                				pkgItem.setVersion(parser.nextText());
                    		}
                			else if("md5".equalsIgnoreCase(parser.getName()))
                			{
                    			pkgItem.setMd5(parser.nextText());
                    		}
                			else if("url".equalsIgnoreCase(parser.getName()))
                			{
                    			pkgItem.setUrl(parser.nextText());
                    		}
                			else if("message".equalsIgnoreCase(parser.getName()))
                			{
                    			pkgItem.setMessage(parser.nextText());
                    		}
                			else if("isforce".equalsIgnoreCase(parser.getName()))
                			{
                    			pkgItem.setIsforce(parser.nextText());
                    		}
                			else if("upgradeway".equalsIgnoreCase(parser.getName()))
                			{
                    			pkgItem.setUpgradeway(parser.nextText());
                    		}
                		}
            		}
            		break;
            	}
            	case XmlPullParser.END_TAG:
            	{
            		if ("package".equalsIgnoreCase(parser.getName())&&pkgItem!=null) 
            		{
            			upgradeInfo.getUpgradeList().add(pkgItem);
            			pkgItem = null;
					}
					break;
            	}
            	default:
            		break;
            	}
            	eventType = parser.next();  
            }
		}
		catch (Exception e) 
		{
			Logcat.i(FlagConstant.TAG, " parseUpgrade " + e.toString());
		}
		return upgradeInfo;
	}
	
	//0服务器没数据,1md5一样,2md5不存在,3md5不一样
	private STATUS currentEventStatus(UpgradeInfo info)
	{
		STATUS nRet = STATUS.SERVICE_NODATA;
		UpgradePkgItem pkgItem;
		try 
		{
			if(info != null)
			{
				int count = mUpgradeInfo.getUpgradeList().size();
				if(count>0)
				{
					while (true) 
					{
						pkgItem = mUpgradeInfo.mUpgradeList.get(count-1);
						String serviceMd5 = null, localMd5 = null;
						serviceMd5 = pkgItem.getMd5();
						localMd5 = MD5.md5sum(getUpgradeDir(mContext)+mUpgradeName);
						Logcat.i(FlagConstant.TAG, " serviceMd5: " + serviceMd5);
						Logcat.i(FlagConstant.TAG, " localMd5: " + localMd5);
					
						if(TextUtils.isEmpty(serviceMd5))
						{
							nRet = STATUS.SERVICE_MD5_NOEXIST;
							break;
						}
						if(TextUtils.isEmpty(localMd5))
						{
							nRet = STATUS.LOCAL_MD5_NOEXIST;
							break;
						}
						if(serviceMd5.equalsIgnoreCase(localMd5))
						{
							nRet = STATUS.MD5_SAME;
							break;
						}
						else 
						{
							nRet = STATUS.MD5_DIFFENCE;
							break;
						}	
					}	
				}
			}
		} catch (Exception e) {
		}
		return nRet;
	}
	
	private boolean downloadApp(String downloadUrl)
	{
		boolean nRet = false;
		try 
		{
			startRequest(downloadUrl);
			if(getQuitflag())
			{
				return false;
			}
			if (HttpURLConnection.HTTP_OK == mResponseCode ||HttpsURLConnection.HTTP_PARTIAL == mResponseCode) 
			{
				AckEvent();
			}
			else 
			{
				throw new Exception("======getResponseCode other code======");
			}
			
			if(getQuitflag())
			{
				closeStream();
				return false;
			}
			while (true) 
			{	
				if (getQuitflag()) 
				{
					closeStream();
					return false;
				}
				getDataFromStream();
			}
		} 
		catch (Exception e) 
		{
			if(getQuitflag())
			{
				return false;
			}
			nRet = downloadExceptionEvent(e);
		}
		finally
		{
			closeStream();
		}		
		return nRet;
	}
	
	public void startRequest(String downloadUrl) throws Exception
	{
		mFileLength = 0;
		mFileProgress = 0;	
		URL url = new URL(downloadUrl);
		mConnection = (HttpURLConnection) url.openConnection();
		mConnection.setRequestMethod("GET");
		mConnection.setUseCaches(false);
		mConnection.setReadTimeout(READ_TIMEOUT);
		mConnection.setConnectTimeout(REQUEST_TIMEOUT);
		mConnection.setRequestProperty("Connection", "Keep-Alive");
		mConnection.setRequestProperty("User-Agent","gvhttp");
		mResponseCode = mConnection.getResponseCode();
		Logcat.i(FlagConstant.TAG, " Response code: " + mResponseCode);
	}
	
	public void AckEvent() throws Exception
	{
		File file = new File(getUpgradeDir(mContext)+mUpgradeName);
		if(file.exists())
		{
			file.delete();
		}
		mFos = new RandomAccessFile(getUpgradeDir(mContext)+mUpgradeName, "rw");
		mFos.seek(0);
		mFileLength=mConnection.getContentLength();;
		// 没有取到文件大小 
		if (mFileLength <= 0) 
		{
			throw new Exception("======fileSize<=0======");
		}
		else
		{	
		}
		mBis = new BufferedInputStream(mConnection.getInputStream());
	}
	
	public boolean downloadExceptionEvent(Exception e)
	{
		//下载完成都在此处处理
		boolean nRet = false;
		Logcat.i(FlagConstant.TAG,"===httprequest====" + "Exception"+e.toString());
		if(mFileProgress>0 && mFileLength>0 && mFileProgress>=mFileLength)
		{
			nRet = true;
		}
		return nRet;
	}

	public void getDataFromStream() throws Exception
	{
		int len = mBis.read(mBuf, 0, BUFFER_SIZE);
		if (len == -1) 
		{
			throw new Exception("========= data complete========");
		}
		// 下载正常 
		mFos.write(mBuf, 0, len);
		mFileProgress += len;
		Thread.sleep(50);
	}

	void closeStream()
	{
		try 
		{
			if(mBis != null)
			{
				mBis.close();
				mBis=null;
			}
			if(mFos != null)
			{
				mFos.close();
				mFos=null;
			}
		} 
		catch (Exception e) 
		{
		}
	}	
	
	private void onCreatDialog(int id, UpgradePkgItem pkgItem)
	{
		if(mDialog != null && mDialog.isShowing())
		{
			((UpgradeDialog)mDialog).setUpgradeInfo(pkgItem.getMessage());
			return;
		}
		mDialog = new UpgradeDialog(mContext, new UpgradeListener() 
		{
			public void OnClick(View view) 
			{
				if(view.getId() == R.id.id_upgrade_sure)
				{
					startInstallApk(getUpgradeDir(mContext)+mUpgradeName);
				}
				else if(view.getId() == R.id.id_upgrade_cancel)
				{
				}
			}
			public void OnBack() 
			{
				//setQuitflag(true);
			}
		});
		mDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		mDialog.show();
		((UpgradeDialog)mDialog).setUpgradeInfo(pkgItem.getMessage());
	}
	
	private void startInstallApk(String updateFile)
	{
		Uri uri = Uri.fromFile(new File(updateFile));
		Intent installIntent = new Intent(Intent.ACTION_VIEW);
		installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		installIntent.setDataAndType(uri,"application/vnd.android.package-archive");
		mContext.startActivity(installIntent);
	}
	
	public String getAppVersion() 
	{
		try 
		{
			PackageManager manager = mContext.getPackageManager();
			PackageInfo info = manager.getPackageInfo(mContext.getPackageName(), 0);
			String version = info.versionName;
			return version;
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return null;
		}
	}
	
	private String getUpgradeDir(Context context) 
	{
		File dataDir = new File(new File(Environment.getExternalStorageDirectory(), "Android"), "data");
		File appCacheDir = new File(dataDir, "upgrade");
		if (!appCacheDir.exists()) 
		{
			if (!appCacheDir.mkdirs()) 
			{
				Logcat.i(FlagConstant.TAG," Unable to create external cache directory");
				return null;	
			}	
		}
		return appCacheDir.getAbsolutePath();
	}
	
	public static boolean isBackground(Context context) 
	{
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
		for (RunningAppProcessInfo appProcess : appProcesses) 
		{
			if (appProcess.processName.equals(context.getPackageName())) 
			{
				if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_BACKGROUND) 
				{
					return true;
				}
				else 
				{
					return false;
				}
			}
		}
		return false;
	}
	     
	public  String getTopActivityName(Context context)
    {
        String topActivityClassName=null;
         ActivityManager activityManager =
        (ActivityManager)(context.getSystemService(android.content.Context.ACTIVITY_SERVICE )) ;
         List<RunningTaskInfo> runningTaskInfos = activityManager.getRunningTasks(1) ;
         if(runningTaskInfos != null)
         {
             ComponentName f=runningTaskInfos.get(0).topActivity;
             topActivityClassName=f.getClassName();
         }
         return topActivityClassName;
    }
	     
	public class UpServiceReceive extends BroadcastReceiver
	{
		public void onReceive(Context context, Intent intent) 
		{
			if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(intent.getAction())) 
			{
	            String reason = intent.getStringExtra("reason");
	            if (reason != null) 
	            {
	                if (reason.equals("homekey")) 
	                {
	                	if(mDialog != null && mDialog.isShowing())
	                	{
	                		mDialog.dismiss();
	                	}
	                } 
	            }
	        }
		}
	}
	
	/**
	 *  自定义监听接口
	 */
	public interface UpgradeListener
	{
		public void OnClick(View view);
		public void OnBack();
	}
	
	public class UpgradeDialog extends Dialog implements OnClickListener
	{
		private Button mSure,mCancel;
		private UpgradeListener mListener;
		private TextView mUpgradeInfo = null;
		private TextView mUpgradedefalut = null;
		
				
		public UpgradeDialog(Context context, UpgradeListener listener) 
		{
			super(context, R.style.upgrade_dialog);
			mListener = listener;
			setContentView(R.layout.dialog_upgrade);
			mSure = (Button)findViewById(R.id.id_upgrade_sure);
			mCancel = (Button)findViewById(R.id.id_upgrade_cancel);
			mSure.setOnClickListener(this);
			mCancel.setOnClickListener(this);
			mUpgradedefalut = (TextView)findViewById(R.id.dialog_prompt_info);
			mUpgradeInfo = (TextView)findViewById(R.id.dialog_upgrade_info);
		}	
		
		@Override
		public void onClick(View v) 
		{
			dismiss();
			mListener.OnClick(v);
		}
		
		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) 
		{
			if(keyCode == KeyEvent.KEYCODE_BACK)
			{
				mListener.OnBack();
			}
			return super.onKeyDown(keyCode, event);
		}
		
		public void setUpgradeInfo(String info) 
		{
			if(!TextUtils.isEmpty(info))
			{
				mUpgradeInfo.setText(info);
				mUpgradeInfo.setVisibility(View.VISIBLE);
				mUpgradedefalut.setVisibility(View.INVISIBLE);
			}
			else 
			{
				mUpgradedefalut.setVisibility(View.VISIBLE);
				mUpgradeInfo.setVisibility(View.INVISIBLE);
			}
		}
	}
	
	public class UpgradeInfo 
	{
		private String mIsupgrade;
		private ArrayList<UpgradePkgItem> mUpgradeList = new ArrayList<UpgradePkgItem>();
		
		public String getIsupgrade() 
		{
			return mIsupgrade;
		}
		
		public void setIsupgrade(String mIsupgrade) 
		{
			this.mIsupgrade = mIsupgrade;
		}
		
		public ArrayList<UpgradePkgItem> getUpgradeList() 
		{
			return mUpgradeList;
		}
		
		public void setUpgradeList(ArrayList<UpgradePkgItem> mUpgradeList) 
		{
			this.mUpgradeList = mUpgradeList;
		}
	}

	public class UpgradePkgItem 
	{
		private String mVersion;
		private String mMd5;
		private String mUrl;
		private String mMessage;
		private String mIsforce;
		private String mUpgradeway;
		
		public String getVersion() 
		{
			return mVersion;
		}
		
		public void setVersion(String mVersion) 
		{
			this.mVersion = mVersion;
		}
		
		public String getMd5() 
		{
			return mMd5;
		}
		
		public void setMd5(String mMd5) 
		{
			this.mMd5 = mMd5;
		}
		
		public String getUrl() 
		{
			return mUrl;
		}
		
		public void setUrl(String mUrl) 
		{
			this.mUrl = mUrl;
		}
		
		public String getMessage() 
		{
			return mMessage;
		}
		
		public void setMessage(String mMessage) 
		{
			this.mMessage = mMessage;
		}
		
		public String getIsforce() 
		{
			return mIsforce;
		}
		
		public void setIsforce(String mIsforce) 
		{
			this.mIsforce = mIsforce;
		}
		
		public String getUpgradeway() 
		{
			return mUpgradeway;
		}
		
		public void setUpgradeway(String mUpgradeway) 
		{
			this.mUpgradeway = mUpgradeway;
		}
	}
}
