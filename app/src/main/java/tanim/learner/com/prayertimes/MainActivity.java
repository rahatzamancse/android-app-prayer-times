package tanim.learner.com.prayertimes;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.Toolbar;

import tanim.learner.com.prayertimes.Util.Constants;
import tanim.learner.com.prayertimes.Util.JsonTask;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends Activity {

    TextView date;
    TextView[] timeTextView;
    String city, country;
    ToggleButton[] toggles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        date = (TextView) findViewById(R.id.date);
        timeTextView = new TextView[5];
        timeTextView[0] = (TextView) findViewById(R.id.fazr_time);
        timeTextView[1] = (TextView) findViewById(R.id.dhuhr_time);
        timeTextView[2] = (TextView) findViewById(R.id.asr_time);
        timeTextView[3] = (TextView) findViewById(R.id.maghrib_time);
        timeTextView[4] = (TextView) findViewById(R.id.isha_time);

        // To change the location . May be used later
        final SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
        city = preferences.getString("CITY", "Dhaka");
        country = preferences.getString("COUNTRY", "BN");

        // Initialize views
        toggles = new ToggleButton[5];
        toggles[0] = findViewById(R.id.fazr_alarm);
        toggles[1] = findViewById(R.id.dhuhr_alarm);
        toggles[2] = findViewById(R.id.asr_alarm);
        toggles[3] = findViewById(R.id.magrib_alarm);
        toggles[4] = findViewById(R.id.isha_alarm);
        for(int i = 0 ;i<5; i++ ) {
                toggles[i].setChecked(preferences.getBoolean("FOR"+String.valueOf(i), false));
        }

        // Set the toolbar as actionbar
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setActionBar(toolbar);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "dd-MM-yyyy ", Locale.US);
        String d =  simpleDateFormat.format( new Date() );
        date.setText(d);

        JsonTask jsonTask = new JsonTask(timeTextView, this);
        jsonTask.execute("http://api.aladhan.com/timingsByCity?city=" + city + "&country=" + country);

        // Set the toggle buttons for notification
        for(int i = 0; i < 5; i++) {
            final  Integer pos = i;
            toggles[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences.Editor editor = preferences.edit();
                    AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
                    Intent intent = new Intent(MainActivity.this, NotificationReceiver.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(
                            MainActivity.this,
                            Constants.NOTIFICATION_ID,
                            intent,
                            PendingIntent.FLAG_UPDATE_CURRENT);

                    if(toggles[pos].isChecked()) {

                        editor.putBoolean("FOR"+String.valueOf(pos), true);
                        Calendar cal = Calendar.getInstance();
                        String time = timeTextView[pos].getText().toString();
                        Integer h = Integer.parseInt(time.substring(0, 2));
                        Integer m = Integer.parseInt(time.substring(3, 5));
                        cal.set(Calendar.HOUR_OF_DAY, h);
                        cal.set(Calendar.MINUTE, m);
//                        cal.set(Calendar.SECOND, 0);

                        if (am != null) {
                            am.set(
                                    AlarmManager.RTC_WAKEUP,
                                    cal.getTimeInMillis(),
                                    pendingIntent);
                        }
                    } else {
                        editor.putBoolean("FOR"+String.valueOf(pos), true);
                        if (am != null) {
                            am.cancel(pendingIntent);
                        }
                    }
                    editor.apply();
                }
            });
        }
    }

   @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent;
        switch (item.getItemId()) {
            case R.id.about:
                intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.names:
                intent = new Intent(this, NamesActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}