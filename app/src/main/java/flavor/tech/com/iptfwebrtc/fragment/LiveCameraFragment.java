package flavor.tech.com.iptfwebrtc.fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import flavor.tech.com.iptfwebrtc.R;
import flavor.tech.com.iptfwebrtc.services.GetServerData;
import flavor.tech.com.iptfwebrtc.util.Constant;
import flavor.tech.com.iptfwebrtc.util.SharedObject;
import flavor.tech.com.iptfwebrtc.util.Utility;

import static flavor.tech.com.iptfwebrtc.util.Constant.CAMERA_REQUEST;
import static flavor.tech.com.iptfwebrtc.util.Constant.TAG;

public class LiveCameraFragment extends Fragment {

    private View inflatedView = null;
    private Button mBtnOpenBrowser = null;
    private Button mBtnrefresh = null;
    private WebView mWebView;
    private String CustomURL = null;
    SharedObject sharedObject = null;

    private ProgressDialog progressDialog;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        this.inflatedView = inflater.inflate(R.layout.fragment_live, container, false);

        mWebView = (WebView) inflatedView.findViewById(R.id.webViewer);
        mWebView .loadUrl("file:///android_asset/placeholder.html");

        final FloatingActionButton fab = (FloatingActionButton) inflatedView.findViewById(R.id.live_fab);
        fab.setImageResource(R.drawable.ic_call_black_24dp);

        // Get the url
        sharedObject = (SharedObject) getActivity().getApplicationContext();

        // new GetServerData().execute(sharedObject); // Only for myTab-No calling - Uncomment this and comment next if block

        // checking - no session without support call.
        if(! sharedObject.getCallStarted() ) {
            Toast.makeText(getContext(), "Support call is not started, Please call for support.", Toast.LENGTH_LONG).show();
            fab.setVisibility(View.VISIBLE);
           // return this.inflatedView;
        }
        // Check permission and Open web view with the url
        checkForAndAskForPermissions();

        // refresh if url not found
        mBtnrefresh = (Button) inflatedView.findViewById(R.id.btn_refresh);
        mBtnrefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( mWebView != null )
                    mWebView.loadUrl(sharedObject.getLivecamURL());
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(Constant.TAG, "Click to call support");
                Snackbar.make(view, "Calling to support", Snackbar.LENGTH_LONG)
                        .setAction("Calling...", null).show();

                Utility.callSupport(getActivity(), Constant.SUPPORT_NUMBER);
                new GetServerData().execute(sharedObject);
                fab.setVisibility(View.GONE);
            }
        });

        // Open native browser with url
        mBtnOpenBrowser = (Button) inflatedView.findViewById(R.id.open_in_browser);
        mBtnOpenBrowser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBrowserWithURL(CustomURL);
            }
        });
        return this.inflatedView;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    createWebView();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void createWebView() {
        try {
            wait(100L);  // to finish async task.. in case of server delay.
        }catch (Exception e){
            e.printStackTrace();
        }
        CustomURL = sharedObject.getLivecamURL();
        Log.i(Constant.TAG, " Checking URL.. : " + CustomURL);
        if(CustomURL == null ){
            CustomURL = "file:///android_asset/errorlocal.html";
        }
        setUpWebViewDefaults(mWebView);
        mWebView.loadUrl(CustomURL);
        mWebView.setWebChromeClient(new WebChromeClient() {
            public boolean onConsoleMessage(ConsoleMessage m) {
                Log.d(TAG, m.message() + " -- From line "
                        + m.lineNumber() + " of "
                        + m.sourceId());
                return true;
            }

            @Override
            public void onPermissionRequest(final PermissionRequest request) {
                // getActivity().
                getActivity().runOnUiThread(new Runnable() {
                    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void run() {
                        // Below isn't necessary, however you might want to:
                        // 1) Check what the site is and perhaps have a blacklist
                        // 2) Have a pop up for the user to explicitly give permission
                        if(request.getOrigin().toString().contains("ericsson")) {
                            request.grant(request.getResources());
                        } else {
                            request.deny();
                        }
                    }
                });
            }
        });
    }

    private void checkForAndAskForPermissions() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // No explanation needed; request the permission
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.CAMERA)) {
                //
            } else ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST);
        } else {
            // Permission has already been granted
            createWebView();
        }
    }

    private void setUpWebViewDefaults(WebView webView) {

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }

        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setAllowContentAccess(true);
        //  settings.setAllowFileAccessFromFileURLs(true);
        // settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setMediaPlaybackRequiresUserGesture(false);

        // Enable remote debugging via chrome://inspect
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            WebView.setWebContentsDebuggingEnabled(true);
//        }

        webView.clearCache(true);
        webView.clearHistory();
        webView.setWebViewClient(new WebViewClient());

        // AppRTC requires third party cookies to work
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptThirdPartyCookies(webView, true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // chromium, enable hardware acceleration
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            // older android version, disable hardware acceleration
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }

    private void openBrowserWithURL(String url) {

//        Uri uri = Uri.parse("googlechrome://navigate?url=" + url);
//        Intent i = new Intent(Intent.ACTION_VIEW, uri);
//        if (i.resolveActivity(getActivity().getPackageManager()) == null) {
//            i.setData(Uri.parse(url));
//        }
//
//        getActivity().startActivity(i);
//

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        browserIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        getActivity().startActivity(Intent.createChooser(browserIntent, "Choose browser"));// Choose browser is arbitrary :)
    }
}
