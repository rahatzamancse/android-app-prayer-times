package tanim.learner.com.prayertimes.Util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import tanim.learner.com.prayertimes.MainActivity;
import tanim.learner.com.prayertimes.NamesActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class JsonTask2 extends AsyncTask<String, String, String> {

    private RecyclerView recyclerView;
    private ProgressDialog pd;
    private Context context;

    public JsonTask2(NamesActivity mainActivity, RecyclerView recyclerView) {
        this.context = mainActivity;
        this.recyclerView = recyclerView;
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
        try {
            JSONObject jsonObject = new JSONObject(result);
//            JSONObject dataObject = jsonObject.getJSONObject("data").getJSONObject("timings");

            JSONArray array = jsonObject.getJSONArray("data");

            String[] names = new String[99];
            String[] meanings = new String[99];
            String[] trans = new String[99];

            for(int i = 0; i<array.length(); i++) {
                JSONObject ob = array.getJSONObject(i);
                names[i] = ob.getString("name");
                trans[i] = ob.getString("transliteration");
                JSONObject en = ob.getJSONObject("en");
                meanings[i] = en.getString("meaning");
            }


//            for(int i = 0; i<99; i++) {
//                Log.d("NAMES", names[i]);
//                Log.d("MEANINGS", meanings[i]);
//            }

            MyAdapter adapter = new MyAdapter(names, meanings, trans);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(
                    new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            );
            recyclerView.setAdapter(adapter);
            if (pd.isShowing()){
                pd.dismiss();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
