package tk.meven.eisenhower;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class ItemArrayAdapter extends RecyclerView.Adapter<ItemArrayAdapter.ViewHolder> {
    //Adapter personalisé pour contenir et afficher les tâches

    //All methods in this adapter are required for a bare minimum recyclerview adapter
    private final int listItemLayout;
    private final ArrayList<Tache> itemList;

    // Constructor of the class
    public ItemArrayAdapter(int layoutId, ArrayList<Tache> itemList) {
        listItemLayout = layoutId;
        this.itemList = itemList;
    }

    // get the size of the list
    @Override
    public int getItemCount() {
        return itemList == null ? 0 : itemList.size();
    }


    // specify the row layout file and click for each row
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(listItemLayout, parent, false);
        return new ViewHolder(view);
    }

    // load data in each row element
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int listPosition) {
        holder.itemTitre.setText(itemList.get(listPosition).getTitre());
        holder.itemDesc.setText(itemList.get(listPosition).getDescription());
        holder.itemDate.setText(itemList.get(listPosition).getDateString());
    }

    // Static inner class to initialize the views of rows
    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView itemTitre;
        public TextView itemDesc;
        public TextView itemDate;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemTitre = itemView.findViewById(R.id.item_titre);
            itemDesc = itemView.findViewById(R.id.item_description);
            itemDate = itemView.findViewById(R.id.item_date);
        }

        @Override
        public void onClick(View view) {
            if(itemDesc.getMaxLines()==1)
                itemDesc.setMaxLines(Integer.MAX_VALUE);
            else
                itemDesc.setMaxLines(1);
        }
    }
}

