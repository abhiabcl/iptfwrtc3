package flavor.tech.com.iptfwebrtc.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import flavor.tech.com.iptfwebrtc.R;
import flavor.tech.com.iptfwebrtc.services.GetServerData;
import flavor.tech.com.iptfwebrtc.util.Constant;
import flavor.tech.com.iptfwebrtc.util.SharedObject;
import flavor.tech.com.iptfwebrtc.util.Utility;

public class ShareSessionFragment extends Fragment {

    View inflatedView = null;
    CheckBox cb_SMS = null;
    CheckBox cb_Mail = null;

    EditText ed_Mobile_no = null;
    EditText  ed_mail_id = null;

    Button btn_send = null;
    SharedObject sharedObject = null;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        this.inflatedView = inflater.inflate(R.layout.fragment_sharesession, container, false);

        sharedObject = (SharedObject) getActivity().getApplicationContext();

        FloatingActionButton fab = (FloatingActionButton) inflatedView.findViewById(R.id.share_fab);
        fab.setImageResource(R.drawable.ic_call_black_24dp);
        btn_send = (Button) inflatedView.findViewById(R.id.bt_send);

        if(! sharedObject.getCallStarted() ) {
            Toast.makeText(getContext(), "Support call is not started, Please call for support.", Toast.LENGTH_LONG).show();
            fab.setVisibility(View.VISIBLE);
            btn_send.setVisibility(View.VISIBLE);
          //  return this.inflatedView;
        }

        String data = sharedObject.getData();

        Log.i(Constant.TAG, "shared data: "+ data);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Calling to support", Snackbar.LENGTH_LONG)
                        .setAction("Call:", null).show();

                Utility.callSupport(getActivity(), Constant.SUPPORT_NUMBER);
                new GetServerData().execute(sharedObject);
            }
        });


        cb_SMS = (CheckBox) inflatedView.findViewById(R.id.cb_sms);
        cb_Mail = (CheckBox) inflatedView.findViewById(R.id.cb_email);

        ed_mail_id = (EditText) inflatedView.findViewById(R.id.et_email);
        ed_Mobile_no = (EditText) inflatedView.findViewById(R.id.et_sms);



        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb_SMS.isChecked()){
                    if (Utility.isValidPhoneNumber(ed_Mobile_no.getText())) {
                        Utility.sendSMS(getActivity(), ed_Mobile_no.getText().toString(), "Click to join: \\n" + sharedObject.getLivecamURL());
                    }else{
                        ed_Mobile_no.setError("Phone number is not valid");
                        return;
                    }
                }
                if(cb_Mail.isChecked()){
                    if (Utility.isValidEmail(ed_mail_id.getText())) {
                        Utility.sendEmail(getActivity(), ed_mail_id.getText().toString(), "WebRTC URL to join", "Click to join: \\n" + sharedObject.getLivecamURL(), false , null);
                    }else{
                        ed_mail_id.setError("Email is not valid");
                        return;
                    }
                }
            }
        });

        return this.inflatedView;
    }
}
