package com.example.doreopartners.bg_tgl;

import java.util.ArrayList;
import java.util.List;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<Questions> values;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        public TextView ikName;
        public TextView ikNumber;
        public TextView score;
        public TextView village;
        public TextView grade;
        public TextView tfm_date;
        public TextView tfm_time;
        public TextView amik;
        public TextView sno;

        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            ikNumber = (TextView) v.findViewById(R.id.r_ikNumber);
            ikName = (TextView) v.findViewById(R.id.r_ikName);
            score = (TextView) v.findViewById(R.id.r_score);
            grade = (TextView) v.findViewById(R.id.r_grade);

            amik = (TextView) v.findViewById(R.id.r_amik);
            village = (TextView) v.findViewById(R.id.r_village);
            tfm_date = (TextView) v.findViewById(R.id.r_tfm_date);
            tfm_time = (TextView) v.findViewById(R.id.r_tfm_time);

            sno = (TextView) v.findViewById(R.id.r_sno);
        }
    }

    public void add(int position, Questions item) {
        values.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        values.remove(position);
        notifyItemRemoved(position);
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(List<Questions> myDataset) {
        values = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v =
                inflater.inflate(R.layout.row_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final Questions q = values.get(position);

        holder.ikNumber.setText(q.getIkNumber());
        holder.ikName.setText(q.getIkName());

        holder.village.setText(q.getVillage());
        holder.grade.setText(q.getGrade());
        holder.amik.setText(q.getAmik());
        holder.tfm_time.setText(q.getTfm_time());
        holder.tfm_date.setText(q.getTfm_date());

        holder.score.setText(q.getScore());
        holder.sno.setText(q.getQuestionNo());
        holder.ikName.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // delete from db then remove
                remove(position);
            }
        });

        //holder.txtFooter.setText("Footer: " + name);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return values.size();
    }

}