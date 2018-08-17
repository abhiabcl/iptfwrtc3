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
import android.widget.Toast;

import flavor.tech.com.iptfwebrtc.R;
import flavor.tech.com.iptfwebrtc.services.GetServerData;
import flavor.tech.com.iptfwebrtc.util.Constant;
import flavor.tech.com.iptfwebrtc.util.SharedObject;
import flavor.tech.com.iptfwebrtc.util.Utility;

public class DashboardFragment extends Fragment {

    View inflatedView = null;
    SharedObject sharedObject = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        this.inflatedView = inflater.inflate(R.layout.fragment_dashboard, container, false);


        sharedObject = (SharedObject) getActivity().getApplicationContext();
        String data = sharedObject.getData();
        Log.i(Constant.TAG, "shared data: "+ data);

        if(sharedObject.getCallStarted() ) {
            Log.i(Constant.TAG, "Call started");
            sharedObject.getFragmentManager().beginTransaction().replace(R.id.fragment_container, new LiveCameraFragment()).addToBackStack(null)
                    .commit();
        }

        FloatingActionButton fab = (FloatingActionButton) inflatedView.findViewById(R.id.dash_fab);
        fab.setImageResource(R.drawable.ic_call_black_24dp);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Calling to support", Snackbar.LENGTH_LONG)
                        .setAction("Calling...", null).show();

                Utility.callSupport(getActivity(), Constant.SUPPORT_NUMBER);
                new GetServerData().execute(sharedObject);
            }
        });

        return this.inflatedView;
    }
}
