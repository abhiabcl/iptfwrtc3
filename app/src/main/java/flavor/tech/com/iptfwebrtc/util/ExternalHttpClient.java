package flavor.tech.com.iptfwebrtc.util;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class ExternalHttpClient {

	private int responseCode;

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public String sendRequest(String pUrl, String jsonData, String pHttpMethod, int hardTimeout) {

		String mResult = null;
		Log.i(Constant.TAG,"going to send request to url: "+ pUrl);
		try {
			URL object = new URL(pUrl);
			HttpURLConnection con = (HttpURLConnection) object.openConnection();
			if ( pHttpMethod.equalsIgnoreCase("POST")) {
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept", "application/json");
            }
			con.setRequestMethod(pHttpMethod);
			con.setConnectTimeout(hardTimeout);

			if (jsonData != null) {
				System.out.println("json request:" + jsonData);
				OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
				wr.write(jsonData);
				wr.flush();
			}

			// display what returns the POST request
			StringBuilder sb = new StringBuilder();

			setResponseCode(con.getResponseCode());

			Log.i(Constant.TAG,"Http Result:" + getResponseCode());

			if (getResponseCode() == HttpURLConnection.HTTP_OK) {
				BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
				String line = null;

				while ((line = br.readLine()) != null) {
					sb.append(line + "\n");
				}
				br.close();
				mResult = sb.toString();
				Log.i(Constant.TAG,"" + mResult);
			} else {
				mResult = con.getResponseMessage();
				Log.i(Constant.TAG,mResult);
			}
		} catch (MalformedURLException e) {
			Log.i(Constant.TAG,"MalformedURLException: " + e.getLocalizedMessage());
			e.printStackTrace();
		} catch (ProtocolException e) {
			Log.i(Constant.TAG,"ProtocolException: " + e.getLocalizedMessage());
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			Log.i(Constant.TAG,"UnsupportedEncodingException: " + e.getLocalizedMessage());
			e.printStackTrace();
		} catch (IOException e) {
			Log.i(Constant.TAG,"IOException: " + e.getLocalizedMessage());
			e.printStackTrace();
		}

        String room_url = null;
        try {
            JSONObject jsonObject = new JSONObject(mResult);
            room_url = jsonObject.getString("room_url");
            Log.i(Constant.TAG,"room_url: ----- " + room_url);
            System.out.println("room_url: ----- " + room_url);
        }catch (Exception ex){
            ex.printStackTrace();
        }
		return room_url;
	}
}