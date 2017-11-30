package tanim.learner.com.prayertimes.Util;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.widget.TextView;

import tanim.learner.com.prayertimes.R;

public class MyViewHolder extends RecyclerView.ViewHolder {

    TextView name, index, meaning, trans;
    public MyViewHolder(View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.allah_name);
        index = itemView.findViewById(R.id.index);
        meaning = itemView.findViewById(R.id.allah_meaning);
        trans = itemView.findViewById(R.id.allah_transliteration);
    }
}
