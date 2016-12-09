package toromu.familymap;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import toromu.familymap.models.Model;

/**
 * Adapter for the Recycler View in the Search Activity
 * Created by Austin on 08-Dec-16.
 */
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {
    private List<SearchResult> searchList;
    private HashMap<String, String> subInfo;
    private HashMap<String, Integer> descriptionImages;
    private HashSet<String> personTerms;

    public SearchAdapter(List<SearchResult> searchList) {
        this.searchList = searchList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_list_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        SearchResult result = searchList.get(position);
        holder.imageView.setImageResource(result.getPictureLocation());
        holder.textView1.setText(result.getLine());
        holder.textView2.setText(result.getSubline());
    }

    @Override
    public int getItemCount() {
        return searchList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView1;
        public TextView textView2;

        public MyViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.searchPersonIcon);
            textView1 = (TextView) view.findViewById(R.id.searchLine1);
            textView2 = (TextView) view.findViewById(R.id.searchLine2);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RecyclerView recyclerView = (RecyclerView) v.getParent();
                    SearchResult result = searchList.get(recyclerView.getChildAdapterPosition(v));
                    if(result.getPerson() != null) {
                        Model.SINGLETON.setCurrentPerson(result.getPerson());
                        Intent intent = new Intent(v.getContext(), PersonActivity.class);
                        v.getContext().startActivity(intent);
                    } else if(result.getEvent() != null) {
                        Model.SINGLETON.setCurrentEvent(result.getEvent());
                        Intent intent = new Intent(v.getContext(), MapsActivity.class);
                        v.getContext().startActivity(intent);
                    }
                }
            });
        }
    }
}
