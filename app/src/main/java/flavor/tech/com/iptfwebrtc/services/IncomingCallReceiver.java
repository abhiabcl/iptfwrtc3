package flavor.tech.com.iptfwebrtc.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import flavor.tech.com.iptfwebrtc.util.Constant;
import flavor.tech.com.iptfwebrtc.util.SharedObject;


public class IncomingCallReceiver extends BroadcastReceiver {
    Context context = null;
    SharedObject sharedObject = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        this.context = context;
        sharedObject = (SharedObject) context.getApplicationContext();

        String number=sharedObject.getCallingNumber();
        Log.i(Constant.TAG,"incomingCall number : "+number);

        try {
            System.out.println("Receiver start");
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

            if(state.equals(TelephonyManager.EXTRA_STATE_RINGING)){
                Toast.makeText(context,"Incoming Call State",Toast.LENGTH_LONG).show();
                Toast.makeText(context,"Ringing State Number is -"+incomingNumber,Toast.LENGTH_LONG).show();
            }
            if ((state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK))){
                Toast.makeText(context,"Call Received State",Toast.LENGTH_LONG).show();
            }
            if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)){
                Toast.makeText(context,"Call Idle - DC State",Toast.LENGTH_LONG).show();
                sharedObject.setCallStarted(false);
            }

            if (state.equals(TelephonyManager.CALL_STATE_OFFHOOK)){
                Toast.makeText(context,"Outgoing Call Starting",Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


}
