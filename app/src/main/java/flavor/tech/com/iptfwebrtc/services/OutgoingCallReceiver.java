package flavor.tech.com.iptfwebrtc.services;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import flavor.tech.com.iptfwebrtc.R;
import flavor.tech.com.iptfwebrtc.activity.MainActivity;
import flavor.tech.com.iptfwebrtc.fragment.LiveCameraFragment;
import flavor.tech.com.iptfwebrtc.util.Constant;
import flavor.tech.com.iptfwebrtc.util.SharedObject;

public class OutgoingCallReceiver extends BroadcastReceiver {
    Context context = null;
    SharedObject sharedObject = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        this.context = context;

        sharedObject = (SharedObject) context.getApplicationContext();

        String number=intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
        Log.i(Constant.TAG,"Outgoing number : "+number);

       if( number.startsWith("800")){
           Toast.makeText(context, "It's 800 number, Let open the browser" + number, Toast.LENGTH_LONG).show();
           sharedObject.setCallingNumber(number);
           sharedObject.setCallStarted(true);
           new GetServerData().execute(sharedObject);

           if (sharedObject.getBundle() == null && sharedObject.getFragmentManager() != null) {
               Log.i(Constant.TAG, "Activity open found!...." + sharedObject.getBundle() +"& getFragmentManager: "+ sharedObject.getFragmentManager());

               if (sharedObject.getFragmentManager().isStateSaved() ){
                   Log.i(Constant.TAG, "FragmentManger State already saved! \"Can not perform this action after onSaveInstanceState\"");
               }else{
                   sharedObject.getFragmentManager().beginTransaction().replace(R.id.fragment_container, new LiveCameraFragment()).commit();
               }
           } else{
               Log.i(Constant.TAG, "Looks Activity not open found!....");
              // Toast.makeText(context, "Looks, app is closed, open app." + number, Toast.LENGTH_LONG).show();
             //  sharedObject.getFragmentManager().beginTransaction().replace(R.id.fragment_container, new LiveCameraFragment()).commit();
           }
           new GetServerData().execute(sharedObject);
           bringApplicationToFront();
       }
    }

    private void bringApplicationToFront()
    {
        Log.d(Constant.TAG, "====Bringging Application to Front====");

        Intent notificationIntent = new Intent(sharedObject.getApplicationContext(), MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(sharedObject.getApplicationContext(), 0, notificationIntent, 0);
        try
        {
            pendingIntent.send();
        }
        catch (PendingIntent.CanceledException e)
        {
            e.printStackTrace();
        }
    }
}
