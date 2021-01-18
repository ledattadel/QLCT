package com.example.qlchitieu.customs;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qlchitieu.DeleteEditLoanBorrow;
import com.example.qlchitieu.PayActivity;
import com.example.qlchitieu.R;
import com.example.qlchitieu.models.VayNo1;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ItemListBLAdapter extends RecyclerView.Adapter<ItemListBLAdapter.ViewHolder> {

    private Context context;
    private List<VayNo1> list;
    private String a;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private String date = dateFormat.format(new Date());
    public ItemListBLAdapter(Context context, List<VayNo1> list, String a) {
        this.context = context;
        this.list = list;
        this.a = a;
    }

    public void clearApplications() {
        int size = list.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                list.remove(0);
            }
            this.notifyItemRangeRemoved(0, size);
        }
    }
    @NonNull
    @Override
    public ItemListBLAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.recyclerview_borrow_loan_item, parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tv_name_l.setText(list.get(position).getHoten_vayno());
        holder.tv_money_l.setText(String.format("%,.2f", list.get(position).getSotien()));
        holder.tv_day_l.setText(getDate(list.get(position).getNgay()));
        if(date.compareTo(list.get(position).getNgaytra()) >= 0){
            holder.tv_pay_l.setTextColor(Color.parseColor("#F44336"));
            holder.tv_pay_l.setError("_");
        }
        else {
            holder.tv_pay_l.setError(null);
            holder.tv_pay_l.setTextColor(Color.parseColor("#43A047"));
        }
        holder.tv_pay_l.setText(list.get(position).getNgaytra() == null? "không có hạn":getDate(list.get(position).getNgaytra()));
        holder.tv_des_l.setText(list.get(position).getMota());
        switch (list.get(position).getLoaiphatsinh()) {
            case "v":
                holder.tv_money_l.setTextColor(Color.parseColor("#F44336"));
                holder.image1.setImageResource(R.drawable.loan1);
                break;
            case "n":
                holder.tv_money_l.setTextColor(Color.parseColor("#43A047"));
                holder.image1.setImageResource(R.drawable.borrow);
                break;
        }


    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
        TextView tv_name_l, tv_money_l, tv_day_l, tv_pay_l, tv_des_l;
        ImageView image1, image2;

        ViewHolder(View itemView) {
            super(itemView);
            tv_name_l = itemView.findViewById(R.id.tv_name_l);
            tv_money_l = itemView.findViewById(R.id.tv_money_l);
            tv_day_l = itemView.findViewById(R.id.tv_day_l);
            tv_pay_l = itemView.findViewById(R.id.tv_pay_l);
            tv_des_l = itemView.findViewById(R.id.tv_des_l);
            image1 = itemView.findViewById(R.id.im_l);
            image2 = itemView.findViewById(R.id.im_select_l);

            image2.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            showPopupMenu(v);
        }

        private void showPopupMenu(View view){
            PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
            if(a == "v"){
                popupMenu.inflate(R.menu.loan_menu);
                popupMenu.setOnMenuItemClickListener(this);
                popupMenu.show();
            }
            else
            {
                popupMenu.inflate(R.menu.borrow_menu);
                popupMenu.setOnMenuItemClickListener(this);
                popupMenu.show();
            }
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()){
                case R.id.loan_detail:
                case R.id.borrow_detail:
                    Intent intent = new Intent(context, DeleteEditLoanBorrow.class);
                    intent.putExtra("ma_phatsinh", list.get(getAdapterPosition()).getMa_phatsinh());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    return true;
                case R.id.loan_take:
                case R.id.borrow_take:
                //    CustomToast.makeText(context,"trả nợ", CustomToast.LENGTH_LONG,CustomToast.SUCCESS,true).show();
                    Intent intent1 = new Intent(context, PayActivity.class);
                    intent1.putExtra("ma_phatsinh", list.get(getAdapterPosition()).getMa_phatsinh());
                    intent1.putExtra("sotien", list.get(getAdapterPosition()).getSotien());
                    intent1.putExtra("loaiphatsinh", list.get(getAdapterPosition()).getLoaiphatsinh());
                    intent1.putExtra("ten",list.get(getAdapterPosition()).getHoten_vayno());
                    intent1.putExtra("ngay", getDate(list.get(getAdapterPosition()).getNgay()));
                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent1);
                    return true;
                default:
                    return false;
            }
        }
    }
    private String getDate(String datein){
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date dateObject;
        String date;

        try{
                dateObject = formatter.parse(datein);
                date = new SimpleDateFormat("dd/MM/yyyy").format(dateObject);
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
