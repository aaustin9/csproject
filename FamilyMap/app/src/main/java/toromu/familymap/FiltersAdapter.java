package toromu.familymap;

import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashSet;
import java.util.List;

import toromu.familymap.models.MapSettings;
import toromu.familymap.models.Model;

/**
 * Filters Adapter for Recycler View
 * Created by Austin on 08-Dec-16.
 */
public class FiltersAdapter extends RecyclerView.Adapter<FiltersAdapter.MyViewHolder> {

    private List<String> filtersList;
    private HashSet<String> eventTypes;

    public FiltersAdapter(List<String> filters) {
        this.filtersList = filters;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.filter_list_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String filter = "Filter by "+filtersList.get(position);
        holder.filterHeader.setText(filter);
        holder.filterSwitch.setChecked(!MapSettings.SINGLETON.getOffFilters().contains(position));
        holder.filterSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View parentRow = (View) v.getParent();
                RecyclerView recyclerView = (RecyclerView) parentRow.getParent();
                final int position = recyclerView.getChildAdapterPosition(parentRow);
                changeFilters(position);
            }
        });
    }

    private void changeFilters(int position) {
        MapSettings.SINGLETON.switchOffFilters(position);
        String filter = filtersList.get(position);
        if(filter.equals("gender: male")) MapSettings.SINGLETON.switchShowMales();
        else if(filter.equals("gender: female")) MapSettings.SINGLETON.switchShowFemales();
        else if(filter.equals("father's side")) MapSettings.SINGLETON.switchShowFatherSide();
        else if(filter.equals("mother's side")) MapSettings.SINGLETON.switchShowMotherSide();
        else MapSettings.SINGLETON.switchShowEventType(filter);
    }

    @Override
    public int getItemCount() {
        return filtersList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView filterHeader;
        public Switch filterSwitch;

        public MyViewHolder(View view) {
            super(view);
            filterHeader = (TextView) view.findViewById(R.id.filterHeader);
            filterSwitch = (Switch) view.findViewById(R.id.filterSwitch);
        }
    }

    public void addEventType(String type) { filtersList.add(type); }

    public void removeEventType(String type) { filtersList.remove(type); }

    public HashSet<String> getEventTypes() { return eventTypes; }
}
