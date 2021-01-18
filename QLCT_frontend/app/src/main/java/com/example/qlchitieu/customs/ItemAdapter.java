package com.example.qlchitieu.customs;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qlchitieu.R;
import com.example.qlchitieu.models.DanhSach;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    private Context context;
    private List<DanhSach> itemList;


        public ItemAdapter(Context context, List<DanhSach> itemList) {
        this.context = context;
        this.itemList = itemList;
    }
    public void clearApplications() {
        int size = itemList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                itemList.remove(0);
            }
            this.notifyItemRangeRemoved(0, size);
        }
    }
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_card, viewGroup, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder itemViewHolder, int i) {
        DanhSach item = itemList.get(i);
        itemViewHolder.tvItemTitle.setText(getDate(item.getNgayphatsinh()));
        // Create layout manager with initial prefetch item count
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                itemViewHolder.rvSubItem.getContext(),
                LinearLayoutManager.VERTICAL,
                false
        );
        layoutManager.setInitialPrefetchItemCount(item.getPhatsinh().size());

        // Create sub item view adapter
        SubItemAdapter subItemAdapter = new SubItemAdapter( context,item.getPhatsinh());

        itemViewHolder.rvSubItem.setLayoutManager(layoutManager);
        itemViewHolder.rvSubItem.setAdapter(subItemAdapter);
        itemViewHolder.rvSubItem.setRecycledViewPool(viewPool);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView tvItemTitle;
        private RecyclerView rvSubItem;

        ItemViewHolder(View itemView) {
            super(itemView);
            tvItemTitle = itemView.findViewById(R.id.tv_title_card);
            rvSubItem = itemView.findViewById(R.id.rw_item);
        }
    }
    private String getDate(String datein){
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat formatter1 = new SimpleDateFormat("yyyy-MM");
        Date dateObject;
        String date;

        try{
            if(datein.length() == 10 ) {
                dateObject = formatter.parse(datein);
                date = new SimpleDateFormat("dd/MM/yyyy").format(dateObject);
            }
            else {
                dateObject = formatter1.parse(datein);
                date = new SimpleDateFormat("MM/yyyy").format(dateObject);
            }
            return date;
        }
        catch (java.text.ParseException e)
        {
            e.printStackTrace();
            Log.i("E11111111111", e.toString());
            return "";
        }

    }
}
