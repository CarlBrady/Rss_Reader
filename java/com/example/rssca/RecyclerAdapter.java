package com.example.rssca;


import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter {

    private List<String> titles = new ArrayList<>();
    private List<String> descriptions = new ArrayList<>();
    private List<String> content = new ArrayList<>();
    private Context context;

    // setTitles takes in the string titles obtained from parsing the JSON created from the rss feed data as an argument and stores them in an array,
    // as this is called from inside a fragment, a context must also be passsed.
    public void setTitles(Context context, List<String> title) {
        titles = title;
        this.context = context;
    }
    public void setContext( List<String> context) {
        content = context;
    }

    public void setDesc(Context context, List<String> desc) {
        descriptions = desc;
        this.context = context;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return RecyclerViewHolder.inflate(parent);
    }


    // This method uses the recycler view holder in order to create an on click listener on each 'item'
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

        ((RecyclerViewHolder) holder).titleAdapter(titles.get(position));
        ((RecyclerViewHolder) holder).descAdapter(descriptions.get(position));
        TextView titleT = ((NewsActivity)context).findViewById(R.id.titleText);
        TextView contentT = ((NewsActivity)context).findViewById(R.id.contentText);
        if (contentT.getText().equals("")) {
            titleT.setText(titles.get(0));
            contentT.setText(content.get(0));
        }


        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {  // set on click listener to the itemView
                TextView contentText = ((NewsActivity)context).findViewById(R.id.contentText);
                TextView titleText = ((NewsActivity)context).findViewById(R.id.titleText);
               int orientation = Resources.getSystem().getConfiguration().orientation; // get orientation of device
                if (orientation == Configuration.ORIENTATION_PORTRAIT) { // if in potrait pass the article details to the Article Activity Class
                    Intent intent = new Intent(context, ArticleActivity.class);
                    intent.putExtra("title", titles.get(position));
                    intent.putExtra("desc", descriptions.get(position));
                    intent.putExtra("content", content.get(position));
                    context.startActivity(intent);
                }

                else { // if in landscape, update the infoFragment text views.

                    titleText.setText(titles.get(position));
                    contentText.setText(content.get(position));
                }



            }
        });

    }

    @Override
    public int getItemCount() {

        return titles.size();
    }



    static class RecyclerViewHolder extends RecyclerView.ViewHolder { //this class as can be seen in the android docs is used to describe an item and its meta data

        private TextView titleView;
        private TextView descView;
        private static RecyclerViewHolder inflate(ViewGroup parent){ // create method to retrieve inflate
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
            return new RecyclerViewHolder(view);
        }

        private RecyclerViewHolder(View view) {
            super(view);

            titleView = view.findViewById(R.id.title);
            descView = view.findViewById(R.id.desc);

        }


        private void titleAdapter(String title)
        {

            titleView.setText(title);
        }

        private void descAdapter(String desc)
        {
            descView.setText(desc);
        }
    }
}