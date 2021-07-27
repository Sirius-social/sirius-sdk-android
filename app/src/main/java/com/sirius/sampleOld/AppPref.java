package com.sirius.sampleOld;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;


/**
 * Created by User on 29.08.2016.
 */
public class AppPref {


    private static final String PREF_APP = "app_pref";
    private static final String PREFS_MAIN = "PREFS_MAIN";
    private static final String PREFS_SERVER_INFO = "PREFS_SERVER_INFO";
    private static final String PREFS_ROSTER_HASH = "PREFS_ROSTER_HASH";
    private static final String PREFS_ROOMS_HASH = "PREFS_ROOMS_HASH";
    private static final String PREFS_FAVORITES_HASH = "PREFS_FAVORITES_HASH";
    private static final String PREFS_PROJECTS_HASH = "PREFS_PROJECTS_HASH";
    private static final String PREFS_TREE_HASH = "PREFS_TREE_HASH";
    public static final String PREF_IS_SOUND_NEW_MESSAGE = "sound_new_message";
    public static final String PREF_IS_REMINDER_TASK = "sound_reminder_task";
    public static final String PREF_IS_ENDED_TASK = "ended_task";
    public static final String PREF_IS_SOUND_NEW_MESSAGE_VIBRO = "sound_new_message_vibro";
    public static final String PREF_IS_REMINDER_TASK_VIBRO = "sound_reminder_task_vibro";
    public static final String PREF_IS_ENDED_TASK_VIBRO = "ended_task_vibro";
    public static final String PREF_CHECK_PUSH = "check_push";
    public static final String PREF_IN_MESSAGE = "in_message";
    public static final String PREF_IN_COMMENTS = "in_comments";
    public static final String PREF_TIME_DIALOG = "time_dialog";
    public static final String PREF_MAIN_DOMAIN = "main_domain";
    public static final String PREF_RECOGNITION_AVAILABLE = "is_recognition_available";
    private int blockType;


    private static volatile AppPref instance;

    private AppPref() {
    }

    public static AppPref getInstance() {
        if (instance == null) {
            synchronized (AppPref.class) {
                if (instance == null) {
                    instance = new AppPref();
                }
            }
        }
        return instance;
    }


    private SharedPreferences getPrefApp() {
        return App.getContext().getSharedPreferences(PREF_APP, Context.MODE_PRIVATE);
    }

    private SharedPreferences getPrefMain() {
        return App.getContext().getSharedPreferences(PREFS_MAIN, Context.MODE_PRIVATE);
    }

    //TODO REFACTOR
/*    public static ServerInfo serverInfo;
    public static ServerInfo tempServerInfo;
    public static RosterUser myselfUser;*/

  /*  public static void setTempServerInfo(ServerInfo tempServerInfo) {
        AppPref.tempServerInfo = tempServerInfo;
    }

    public String getServerInfoString() {
        return DaoUtilsSettings.getDbSettings().getServerInfoString();
    }


    public String getServerInfoSession() {
        return DaoUtilsSettings.getDbSettings().getServerInfoToken();
    }
*/

    public void setServerInfoString(String serverInfo) {
        getPrefMain().edit().putString(PREFS_SERVER_INFO, serverInfo).apply();
    }


/*    public static ServerInfo getServerInfo() {
        if (AppPref.serverInfo == null) {
            String serverInfoString = AppPref.getInstance().getServerInfoString();
            ServerInfo serverInfo = Utils.GSON.fromJson(serverInfoString, ServerInfo.class);
            AppPref.serverInfo = serverInfo;
            return AppPref.serverInfo;
        } else {
            return AppPref.serverInfo;
        }

    }*/

/*    public String getDomain() {
        String roomDomainName = "";
        ServerInfo serverInfo = AppPref.getServerInfo();
        if (serverInfo != null) {
            if (serverInfo.getRoute() != null) {
                if (serverInfo.getRoute().getAddress() != null) {
                    roomDomainName = serverInfo.getRoute().getAddress();
                }

            }
        }
        return roomDomainName;
    }*/

 /*   public List<String> getAllBootstrapSIps() {
        List<String> ipList = new ArrayList<>();
        ServerInfo serverInfo = getServerInfo();
        if (serverInfo != null) {
            List<String> stringList = serverInfo.getBootstrap_servers();
            if (stringList != null) {
                if (stringList.size() > 0) {
                    ipList.addAll(stringList);
                }
            }
        }
        return ipList;
    }
*/
/*
    public static void setServerInfo(ServerInfo serverInfo) {
        AppPref.serverInfo = serverInfo;
    }

    public static String getTempUserJid() {
        String userJid = "";
        if (tempServerInfo != null) {
            if ((tempServerInfo.getUser_data() != null)) {
                userJid = tempServerInfo.getUser_data().getJid();
            }
        }
        return Jid.getUsername(userJid);
    }
*/

/*    //TODO refactor to isMy in DB?
    public static String getUserJid() {
        String userJid = getTempUserJid();
        if (!TextUtils.isEmpty(userJid)) {
            return userJid;
        }
        if (AppPref.serverInfo != null) {
            if ((AppPref.serverInfo.getUser_data() != null)) {
                userJid = AppPref.serverInfo.getUser_data().getJid();
            }
        } else {
            AppPref.serverInfo = getServerInfo();
            if (AppPref.serverInfo != null) {
                if ((AppPref.serverInfo.getUser_data() != null)) {
                    userJid = AppPref.serverInfo.getUser_data().getJid();
                }
            }
        }
        return Jid.getUsername(userJid);
    }*/
/*

    public static RosterUser getMyselfUser() {
        if (AppPref.myselfUser == null) {
            AppPref.myselfUser = RosterUser.findUserByJid(getUserJid());
        }

        if (AppPref.myselfUser == null) {
            AppPref.myselfUser = new RosterUser();
        }
        return AppPref.myselfUser;
    }

*/




/*
    public String getMainDomain(boolean useHttp, boolean withSlash) {
        ServerInfo serverInfo = getServerInfo();
        String serverIp = "";
        if (serverInfo != null) {
            serverIp = serverInfo.getRandomServerIp(withSlash);
        }
        if (TextUtils.isEmpty(serverIp) || useHttp) {
            if (withSlash) {
                return getPrefApp().getString(PREF_MAIN_DOMAIN, BuildConfig.domenUrl) + "/";
            } else {
                return getPrefApp().getString(PREF_MAIN_DOMAIN, BuildConfig.domenUrl);
            }

        } else {
            return serverIp;
        }
    }
*/

    public void setMainDomain(String domainString) {
        SharedPreferences.Editor ed = getPrefApp().edit();
        ed.putString(PREF_MAIN_DOMAIN, domainString);
        ed.apply();
    }


    public void setIsSoundNewMessage(boolean isSound) {
        SharedPreferences.Editor ed = getPrefApp().edit();
        ed.putBoolean(PREF_IS_SOUND_NEW_MESSAGE, isSound);
        ed.apply();
    }

    public boolean getIsSoundNewMessage() {
        return getPrefApp().getBoolean(PREF_IS_SOUND_NEW_MESSAGE, true);
    }

    public void setSoundNewMessageVibro(boolean isSound) {
        SharedPreferences.Editor ed = getPrefApp().edit();
        ed.putBoolean(PREF_IS_SOUND_NEW_MESSAGE_VIBRO, isSound);
        ed.apply();
    }

    public boolean isSoundNewMessageVibro() {
        return getPrefApp().getBoolean(PREF_IS_SOUND_NEW_MESSAGE_VIBRO, true);
    }

    public void setIsReminderTask(boolean bool) {
        SharedPreferences.Editor ed = getPrefApp().edit();
        ed.putBoolean(PREF_IS_REMINDER_TASK, bool);
        ed.apply();
    }

    public boolean getIsReminderTask() {
        return getPrefApp().getBoolean(PREF_IS_REMINDER_TASK, true);
    }

    public boolean isReminderTaskVibro() {
        return getPrefApp().getBoolean(PREF_IS_REMINDER_TASK_VIBRO, true);
    }

    public void setReminderTaskVibro(boolean bool) {
        SharedPreferences.Editor ed = getPrefApp().edit();
        ed.putBoolean(PREF_IS_REMINDER_TASK_VIBRO, bool);
        ed.apply();
    }

    public void setIsEndedTask(boolean bool) {
        SharedPreferences.Editor ed = getPrefApp().edit();
        ed.putBoolean(PREF_IS_ENDED_TASK, bool);
        ed.apply();
    }

    public boolean getIsEndedTask() {
        return getPrefApp().getBoolean(PREF_IS_ENDED_TASK, true);
    }

    public void setEndedTaskVibro(boolean bool) {
        SharedPreferences.Editor ed = getPrefApp().edit();
        ed.putBoolean(PREF_IS_ENDED_TASK_VIBRO, bool);
        ed.apply();
    }

    public boolean isEndedTaskVibro() {
        return getPrefApp().getBoolean(PREF_IS_ENDED_TASK_VIBRO, true);
    }

    public void setIsCheckPush(boolean bool) {
        SharedPreferences.Editor ed = getPrefApp().edit();
        ed.putBoolean(PREF_CHECK_PUSH, bool);
        ed.apply();
    }

    public boolean getIsCheckPush() {
        return getPrefApp().getBoolean(PREF_CHECK_PUSH, true);
    }

    public void setIsRecognitionMessage(boolean bool) {
        SharedPreferences.Editor ed = getPrefApp().edit();
        ed.putBoolean(PREF_IN_MESSAGE, bool);
        ed.apply();
    }

    public boolean getIsRecognitionMessage() {
        return getPrefApp().getBoolean(PREF_IN_MESSAGE, true);
    }

    public void setIsRecognitionComment(boolean bool) {
        SharedPreferences.Editor ed = getPrefApp().edit();
        ed.putBoolean(PREF_IN_COMMENTS, bool);
        ed.apply();
    }

    public boolean getIsRecognitionComment() {
        return getPrefApp().getBoolean(PREF_IN_COMMENTS, true);
    }

    public void setTimeDialog(int timePosition) {
        SharedPreferences.Editor ed = getPrefApp().edit();
        ed.putInt(PREF_TIME_DIALOG, timePosition);
        ed.apply();
    }

    public int getTimeDialog() {
        return getPrefApp().getInt(PREF_TIME_DIALOG, 2);
    }


    public void setRecognitionAvailable(boolean available) {
        SharedPreferences.Editor ed = getPrefApp().edit();
        ed.putBoolean(PREF_RECOGNITION_AVAILABLE, available);
        ed.apply();
    }

    public boolean isRecognitionAvailable() {
        return getPrefApp().getBoolean(PREF_RECOGNITION_AVAILABLE, false);
    }


    public String getDefaultCountry() {
        return getPrefMain().getString("default_country", "RU");
    }

    public void setDefaultCountry(String country) {
        getPrefMain().edit().putString("default_country", country).apply();
    }

    public void setSyncContacts(boolean isSync) {
        getPrefMain().edit().putBoolean("sync_contacts", isSync).apply();
    }

    public boolean isSyncContacts() {
        return getPrefMain().getBoolean("sync_contacts", true);
    }

    public String getIndy(String key) {
        String strings = getPrefMain().getString("indy_" + key, null);
        return strings;
    }


    public String getIndyEnpoint() {
        String endpoint = "";
        //debug
        //  endpoint = FirebaseInstanceId.getInstance().getToken();
        //release
        endpoint = getIndy("endpoint");
        return endpoint;
    }


    public void setIndyEndpoint(String endpoint) {
        setIndy("endpoint", endpoint);
    }

    public boolean isIndyExist(String key) {
        String strings = getPrefMain().getString("indy_" + key, null);
        return strings != null;
    }


    public void setIndy(String key, String value) {
        SharedPreferences.Editor ed = getPrefMain().edit();
        ed.putString("indy_" + key, value);
        ed.apply();
    }

    public void setProjectsHash(String projectsHash) {
        getPrefMain().edit().putString(PREFS_PROJECTS_HASH, projectsHash).apply();
    }

    public void setTreeHash(String treeHash) {
        getPrefMain().edit().putString(PREFS_TREE_HASH, treeHash).apply();
    }


    public String getProjectsHash() {
        return getPrefMain().getString(PREFS_PROJECTS_HASH, "");
    }

    public String getTreeHash() {
        return getPrefMain().getString(PREFS_TREE_HASH, "");
    }

    public String getFavoriteHash() {
        return getPrefMain().getString(PREFS_FAVORITES_HASH, "");
    }

    public void setFavoriteHash(String favoriteHash) {
        getPrefMain().edit().putString(PREFS_FAVORITES_HASH, favoriteHash).apply();
    }


    public void setRosterHash(String rosterHash) {
        getPrefMain().edit().putString(PREFS_ROSTER_HASH, rosterHash).apply();
    }


    public String getRosterHash() {
        return getPrefMain().getString(PREFS_ROSTER_HASH, "");
    }

    public String getRoomsHash() {
        return getPrefMain().getString(PREFS_ROOMS_HASH, "");
    }

    public void setRoomsHash(String roomsHash) {
        getPrefMain().edit().putString(PREFS_ROOMS_HASH, roomsHash).apply();
    }


    public void clearAfterDropTables() {
        setProjectsHash("");
        setTreeHash("");
        setFavoriteHash("");
        setRosterHash("");
    }


    public String getUserResourses() {
      /*  if (getPrefMain().getString("user_resourses", "").isEmpty()) {
            setUserResourses(UUID.randomUUID().toString());
        }*/
        return getPrefMain().getString("user_resourses", "");
    }

    public void setUserResourses(String userResousrces) {
        getPrefMain().edit().putString("user_resourses", userResousrces).apply();
    }

    public void setUserHash(String userHash) {
        getPrefMain().edit().putString("hash", userHash).apply();
    }

    public String getUserHash() {
        return getPrefMain().getString("hash", "");
    }


    public void setUserPin(String userHash) {
        getPrefMain().edit().putString("hash_code", userHash).apply();
    }

    public String getUserPin() {
        return getPrefMain().getString("hash_code", "");
    }


/*    public int getUserCodeTryCount() {
        try {
            String defValue = HashUtils.generateStorngPasswordHash(3 + "");
            String value = getPrefMain().getString("hash_code_try", defValue);

            if (TextUtils.isEmpty(value)) {
                return 3;
            }

            boolean isZero = HashUtils.validatePassword(0 + "", value);
            boolean isOne = HashUtils.validatePassword(1 + "", value);
            boolean isTwo = HashUtils.validatePassword(2 + "", value);
            boolean isThree = HashUtils.validatePassword(3 + "", value);

            if (isZero) {
                return 0;
            }

            if (isOne) {
                return 1;
            }
            if (isTwo) {
                return 2;
            }
            if (isThree) {
                return 3;
            }


        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

        return 3;

    }*/

 /*   public void setUserCodeTryCount(int count) {
        try {
            String defValue = HashUtils.generateStorngPasswordHash(count + "");
            getPrefMain().edit().putString("hash_code_try", defValue).apply();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }
*/

    public void clearAfterLogout() {
        getPrefMain().edit().clear().apply();
    }

    public static long getCurrentGmtTime() {
        return System.currentTimeMillis();
    }


    public static boolean isManyPane() {
        return App.getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && isTablet();
    }

    public static boolean isTablet() {
        return false;
        //App.getInstance().getResources().getBoolean(R.bool.is_tablet);
    }


    public int getBlockType() {
        return getPrefMain().getInt("block_type", 0);
    }

    public long getTimeByBlockType() {
        int type = getBlockType();
        switch (type) {
            case 0:
                return 30 * 1000;
            case 1:
                return 90 * 1000;
            case 2:
                return 60 * 3 * 1000;
            case 3:
                return 60 * 5 * 1000;
            case 4:
                return 1000;
        }
        return 1000;
    }

    public void setBlockType(int blockType) {
        getPrefMain().edit().putInt("block_type", blockType).apply();
    }

    public void setBackupPeriod(int blockType) {
        getPrefMain().edit().putInt("backup_period", blockType).apply();
    }

    public int getBackupPeriod() {
        return getPrefMain().getInt("backup_period", 1);
    }

    public void setBackupLastTime(long lastTime) {
        getPrefMain().edit().putLong("backup_last_time", lastTime).apply();
    }

    public long getBackupLastTime() {
        return getPrefMain().getLong("backup_last_time", 0);
    }


    public void setLastConnectionTimestamp(long currentTimeMillis) {
        getPrefMain().edit().putLong("currentTimeMillis", currentTimeMillis).apply();
    }

    public void setLastWalletConnectionTimestamp(long currentTimeMillis) {
        getPrefMain().edit().putLong("indy_currentTimeMillis", currentTimeMillis).apply();
    }

    public long getLastWalletConnectionTimestamp() {
        return getPrefMain().getLong("indy_currentTimeMillis", 0);
    }

    public long getLastConnectionTimestamp() {
        return getPrefMain().getLong("currentTimeMillis", 0);
    }


  /*  public boolean isLoggedIn() {
        return getServerInfo() != null;
    }
*/
}
