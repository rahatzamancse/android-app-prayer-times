package tanim.learner.com.prayertimes.Util;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import tanim.learner.com.prayertimes.R;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    String[] names, meanings, trans;

    public MyAdapter(String[] names, String[] meanings, String[] trans) {
        this.names = names;
        this.meanings = meanings;
        this.trans = trans;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.name, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.name.setText(names[position]);
        holder.index.setText(String.valueOf(position+1));
        holder.meaning.setText(meanings[position]);
        holder.trans.setText(trans[position]);
    }

    @Override
    public int getItemCount() {
        return 99;
    }
}
