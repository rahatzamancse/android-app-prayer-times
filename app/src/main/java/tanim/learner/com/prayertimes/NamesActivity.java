package tanim.learner.com.prayertimes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import tanim.learner.com.prayertimes.Util.JsonTask;
import tanim.learner.com.prayertimes.Util.JsonTask2;

public class NamesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_names);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.names_recyclerview);

        JsonTask2 jsonTask = new JsonTask2(this, recyclerView);
        jsonTask.execute("http://api.aladhan.com/asmaAlHusna");


    }
}
