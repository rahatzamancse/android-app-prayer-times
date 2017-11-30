package tanim.learner.com.prayertimes.Util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import tanim.learner.com.prayertimes.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

public class JsonTask extends AsyncTask<String, String, String> {

    private ProgressDialog pd;
    private Context context;
    private TextView[] textViews;

    public JsonTask(TextView[] textView, MainActivity mainActivity) {
        this.textViews = textView;
        this.context = mainActivity;
    }

    protected void onPreExecute() {
        super.onPreExecute();

        pd = new ProgressDialog(context);
        pd.setMessage("Please wait");
        pd.setCancelable(false);
        pd.show();
    }

    protected String doInBackground(String... params) {


        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            StringBuilder buffer = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
                Log.d("Response: ", "> " + line);
            }
            return buffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        String[] times = new String[5];
        if (pd.isShowing()){
            pd.dismiss();
        }
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONObject dataObject = jsonObject.getJSONObject("data").getJSONObject("timings");

            times[0] = dataObject.getString(Constants.FAJR);
            times[1] = dataObject.getString(Constants.DHUHR);
            times[2] = dataObject.getString(Constants.ASR);
            times[3] = dataObject.getString(Constants.MAGHRIB);
            times[4] = dataObject.getString(Constants.ISHA);

//
//            Calendar cal = Calendar.getInstance();
//            cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE)+1);
//            int h = cal.get(Calendar.HOUR_OF_DAY);
//            int m = cal.get(Calendar.MINUTE)+1;
//            String hs = String.valueOf(h);
//            String ms = String.valueOf(m);
//            if(h < 10) hs = "0" + String.valueOf(h);
//            if(m < 10) ms = "0" + String.valueOf(m);
//            times[0] = hs + ":" + ms;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        for(int i = 0; i<5 ; i++) {
            if(times[i] != null) {
                textViews[i].setText(times[i]);
            }
        }

    }
}
