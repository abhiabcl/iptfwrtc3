package flavor.tech.com.iptfwebrtc.util;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

public class SharedObject extends Application {

    private static SharedObject instance;
    public static SharedObject getAppInstance() { return instance; }

    private Bundle bundle;
    private Activity activity;
    private String data;
    private String livecamURL;
    private String serverURL;
    private Boolean isCallStarted = false;
    private String CallingNumber;

    private FragmentManager fragmentManager;

    @Override
    public void onCreate()
    {
        super.onCreate();
        instance = this;
    }

    public String getData() {return data;}
    public void setData(String data) {this.data = data;}

    public String getLivecamURL() { return livecamURL;}
    public void setLivecamURL(String livecamURL) {this.livecamURL = livecamURL;}

    public Activity getActivity() {return activity;}
    public void setActivity(Activity activity) {this.activity = activity;}

    public Bundle getBundle() {return bundle;}
    public void setBundle(Bundle bundle) {this.bundle = bundle;}

    public FragmentManager getFragmentManager() {return fragmentManager;}
    public void setFragmentManager(FragmentManager fragmentManager) {this.fragmentManager = fragmentManager;}

    public String getServerURL() {return serverURL;}
    public void setServerURL(String serverURL) {this.serverURL = serverURL;}

    public Boolean getCallStarted() {return isCallStarted;}
    public void setCallStarted(Boolean callStarted) {isCallStarted = callStarted;}

    public String getCallingNumber() {return CallingNumber;}
    public void setCallingNumber(String callingNumber) {CallingNumber = callingNumber;}
}
