package flavor.tech.com.iptfwebrtc.services;

import android.os.AsyncTask;
import android.util.Log;

import java.net.URL;

import flavor.tech.com.iptfwebrtc.util.Constant;
import flavor.tech.com.iptfwebrtc.util.ExternalHttpClient;
import flavor.tech.com.iptfwebrtc.util.SharedObject;

public class GetServerData extends AsyncTask<SharedObject, Integer, SharedObject>{

    String WebRTCRoomURL = null;
    SharedObject sharedObject1 = null;
    @Override
    protected SharedObject doInBackground(SharedObject... sharedObjects) {
        Log.i(Constant.TAG,"doInBackground: fetching room url ");
        sharedObject1 = sharedObjects[0];
        WebRTCRoomURL  = new ExternalHttpClient().sendRequest(sharedObject1.getServerURL(),null,"GET", 50000);
        return null;
    }

    protected void onProgressUpdate(Integer... progress) {
       // setProgressPercent(progress[0]);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        Log.i(Constant.TAG,"onPreExecute: nothing, if you want show progress url ");
    }

    @Override
    protected void onPostExecute(SharedObject sharedObject) {
        super.onPostExecute(sharedObject);
        Log.i(Constant.TAG,"onPostExecute: on room url found opening browser with url:" + WebRTCRoomURL );
        if (WebRTCRoomURL != null){
            sharedObject1.setLivecamURL(WebRTCRoomURL);
        }
    }
}
