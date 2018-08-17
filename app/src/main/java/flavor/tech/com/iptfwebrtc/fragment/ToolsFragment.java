package flavor.tech.com.iptfwebrtc.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import flavor.tech.com.iptfwebrtc.R;
import flavor.tech.com.iptfwebrtc.util.SharedObject;

public class ToolsFragment extends Fragment {

    View inflatedView = null;
    SharedObject sharedObject = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        this.inflatedView = inflater.inflate(R.layout.fragment_tools, container, false);
        sharedObject = (SharedObject) getActivity().getApplicationContext();
        if(! sharedObject.getCallStarted() ) {
            Toast.makeText(getContext(), "Support call is not started, Please call for support.", Toast.LENGTH_LONG).show();
            return this.inflatedView;
        }
        return this.inflatedView;
    }
}
