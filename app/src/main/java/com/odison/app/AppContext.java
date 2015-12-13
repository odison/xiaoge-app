package com.odison.app;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

//import com.loopj.android.http.AsyncHttpClient;
//import com.loopj.android.http.PersistentCookieStore;

//import net.oschina.app.api.ApiHttpClient;
import com.odison.app.bean.Courier;
import com.odison.app.util.CyptoUtils;
import com.odison.app.util.StringUtils;
import com.odison.app.base.BaseApplication;
//import net.oschina.app.cache.DataCleanManager;
//import net.oschina.app.util.MethodsCompat;
//import net.oschina.app.util.TLog;
//import net.oschina.app.util.UIHelper;

//import org.kymjs.kjframe.KJBitmap;
//import org.kymjs.kjframe.bitmap.BitmapConfig;
//import org.kymjs.kjframe.utils.KJLoger;

import java.util.Properties;
import java.util.UUID;


/**
 * 全局应用程序类：用于保存和调用全局应用配置及访问网络数据
 * 
 * @author 火蚁 (http://my.oschina.net/LittleDY)
 * @version 1.0
 * @created 2014-04-22
 */
public class AppContext extends BaseApplication {

    public static final int PAGE_SIZE = 20;// 默认分页大小

    private static AppContext instance;

    private int loginUid;

    private boolean login;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        init();
        initLogin();

//        Thread.setDefaultUncaughtExceptionHandler(AppException
//                .getAppExceptionHandler(this));
//        UIHelper.sendBroadcastForNotice(this);
    }

    private void init() {
        //TODO 自动登录在此处初始化
//        // 初始化网络请求
//        AsyncHttpClient client = new AsyncHttpClient();
//        PersistentCookieStore myCookieStore = new PersistentCookieStore(this);
//        client.setCookieStore(myCookieStore);
//        ApiHttpClient.setHttpClient(client);
//        ApiHttpClient.setCookie(ApiHttpClient.getCookie(this));
//
//        // Log控制器
//        KJLoger.openDebutLog(true);
//        TLog.DEBUG = BuildConfig.DEBUG;
//
//        // Bitmap缓存地址
//        BitmapConfig.CACHEPATH = "OSChina/imagecache";
    }

    private void initLogin() {
//        User user = getLoginUser();
//        if (null != user && user.getId() > 0) {
//            login = true;
//            loginUid = user.getId();
//        } else {
//            this.cleanLoginInfo();
//        }
    }

    /**
     * 获得当前app运行的AppContext
     * 
     * @return
     */
    public static AppContext getInstance() {
        return instance;
    }

    public boolean containsProperty(String key) {
        Properties props = getProperties();
        return props.containsKey(key);
    }

    public void setProperties(Properties ps) {
        AppConfig.getAppConfig(this).set(ps);
    }

    public Properties getProperties() {
        return AppConfig.getAppConfig(this).get();
    }

    public void setProperty(String key, String value) {
        AppConfig.getAppConfig(this).set(key, value);
    }

    /**
     * 获取cookie时传AppConfig.CONF_COOKIE
     * 
     * @param key
     * @return
     */
    public String getProperty(String key) {
        String res = AppConfig.getAppConfig(this).get(key);
        return res;
    }

    public void removeProperty(String... key) {
        AppConfig.getAppConfig(this).remove(key);
    }

    /**
     * 获取App唯一标识
     * 
     * @return
     */
    public String getAppId() {
        String uniqueID = getProperty(AppConfig.CONF_APP_UNIQUEID);
        if (StringUtils.isEmpty(uniqueID)) {
            uniqueID = UUID.randomUUID().toString();
            setProperty(AppConfig.CONF_APP_UNIQUEID, uniqueID);
        }
        return uniqueID;
    }

    /**
     * 获取App安装包信息
     * 
     * @return
     */
    public PackageInfo getPackageInfo() {
        PackageInfo info = null;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        if (info == null)
            info = new PackageInfo();
        return info;
    }

    /**
     * 保存登录信息
     * 
     * @param user 用户信息
     */
    @SuppressWarnings("serial")
    public void saveUserInfo(final Courier user) {
        this.loginUid = user.getId();
        this.login = true;
        setProperties(new Properties() {
            {
                setProperty("user.uid", String.valueOf(user.getId()));
                setProperty("user.name", user.getName());
                //setProperty("user.face", user.getPortrait());// 用户头像-文件名
                setProperty("user.phone", user.getPhone());
                setProperty("user.pwd",
                        CyptoUtils.encode("fuyangApp", user.getPassword()));
                setProperty("user.address", user.getAddress());
                setProperty("user.signTime",user.getSignTime().toString());
                setProperty("user.score", String.valueOf(user.getScore()));
                setProperty("user.state",String.valueOf(user.getState()));
                setProperty("user.updTime",user.getUpdateTime().toString());
                setProperty("user.isRememberMe",
                        String.valueOf(user.isRememberMe()));// 是否记住我的信息
            }
        });
    }

    /**
     * 更新用户信息
     * 
     * @param user
     */
    @SuppressWarnings("serial")
    public void updateUserInfo(final Courier user) {
//        setProperties(new Properties() {
//            {
//                setProperty("user.name", user.getName());
//            }
//        });
    }

    /**
     * 获得登录用户的信息
     * 
     * @return
     */
    public Courier getLoginUser() {
        Courier user = new Courier();
        user.setId(StringUtils.toInt(getProperty("user.uid"), 0));
        user.setName(getProperty("user.name"));
        user.setScore(StringUtils.toInt(getProperty("user.score"), 0));
        user.setIsRememberMe(StringUtils.toBool(getProperty("user.isRememberMe")));
        return user;
    }

    /**
     * 清除登录信息
     */
    public void cleanLoginInfo() {
        this.loginUid = 0;
        this.login = false;
        removeProperty("user.uid", "user.name", "user.face", "user.location",
                "user.followers", "user.fans", "user.score",
                "user.isRememberMe", "user.gender", "user.favoritecount");
    }

    public int getLoginUid() {
        return loginUid;
    }

    public boolean isLogin() {
        return login;
    }

    /**
     * 用户注销
     */
    public void Logout() {
        cleanLoginInfo();
       // ApiHttpClient.cleanCookie();
        this.cleanCookie();
        this.login = false;
        this.loginUid = 0;
//TODO 发送广播退出
//        Intent intent = new Intent(Constants.INTENT_ACTION_LOGOUT);
//        sendBroadcast(intent);
    }

    /**
     * 清除保存的缓存
     */
    public void cleanCookie() {
        removeProperty(AppConfig.CONF_COOKIE);
    }

    /**
     * 清除app缓存
     */
    public void clearAppCache() {
//        DataCleanManager.cleanDatabases(this);
//        // 清除数据缓存
//        DataCleanManager.cleanInternalCache(this);
//        // 2.2版本才有将应用缓存转移到sd卡的功能
//        if (isMethodsCompat(android.os.Build.VERSION_CODES.FROYO)) {
//            DataCleanManager.cleanCustomCache(MethodsCompat
//                    .getExternalCacheDir(this));
//        }
//        // 清除编辑器保存的临时内容
//        Properties props = getProperties();
//        for (Object key : props.keySet()) {
//            String _key = key.toString();
//            if (_key.startsWith("temp"))
//                removeProperty(_key);
//        }
//        new KJBitmap().cleanCache();
    }

    public static void setLoadImage(boolean flag) {
       // set(KEY_LOAD_IMAGE, flag);
    }

    /**
     * 判断当前版本是否兼容目标版本的方法
     * 
     * @param VersionCode
     * @return
     */
    public static boolean isMethodsCompat(int VersionCode) {
        int currentVersion = android.os.Build.VERSION.SDK_INT;
        return currentVersion >= VersionCode;
    }

    public static String getTweetDraft() {
        return getPreferences().getString(
                AppConfig.KEY_TWEET_DRAFT + getInstance().getLoginUid(), "");
    }

    public static void setTweetDraft(String draft) {
        set(AppConfig.KEY_TWEET_DRAFT + getInstance().getLoginUid(), draft);
    }

    public static String getNoteDraft() {
        return getPreferences().getString(
                AppConfig.KEY_NOTE_DRAFT + getInstance().getLoginUid(), "");
    }

    public static void setNoteDraft(String draft) {
        set(AppConfig.KEY_NOTE_DRAFT + getInstance().getLoginUid(), draft);
    }

    public static boolean isFristStart() {
        return getPreferences().getBoolean(AppConfig.KEY_FRITST_START, true);
    }

    public static void setFristStart(boolean frist) {
        set(AppConfig.KEY_FRITST_START, frist);
    }

    //夜间模式
    public static boolean getNightModeSwitch() {
        return getPreferences().getBoolean(AppConfig.KEY_NIGHT_MODE_SWITCH, false);
    }

    // 设置夜间模式
    public static void setNightModeSwitch(boolean on) {
        set(AppConfig.KEY_NIGHT_MODE_SWITCH, on);
    }
}