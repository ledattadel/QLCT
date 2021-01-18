package com.example.qlchitieu.customs;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qlchitieu.DeleteEditLoanBorrow;
import com.example.qlchitieu.DeleteEditRevenueExpenditure;
import com.example.qlchitieu.DeletePayActivity;
import com.example.qlchitieu.R;
import com.example.qlchitieu.api.Api;
import com.example.qlchitieu.api.ApiUtils;
import com.example.qlchitieu.interfaces.IItemClickListener;
import com.example.qlchitieu.models.PhatSinh1;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubItemAdapter extends RecyclerView.Adapter<SubItemAdapter.SubItemViewHolder> {

    private Context context;
    private List<PhatSinh1> subItemList;
    Api api = ApiUtils.getAPIService();
    public SubItemAdapter(Context context, List<PhatSinh1> subItemList) {
        this.context = context;
        this.subItemList = subItemList;
    }

    @NonNull
    @Override
    public SubItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_item, viewGroup, false);
        return new SubItemViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final SubItemViewHolder subItemViewHolder, final int i) {
        final PhatSinh1 phatSinh1 = subItemList.get(i);
        subItemViewHolder.tvSubItemTitle.setText(phatSinh1.getNoidung());
        subItemViewHolder.tvMoney.setText(String.format("%,.2f", Double.parseDouble(phatSinh1.getSotien())));
        switch (phatSinh1.getLoaiphatsinh()) {
            case "t":
                subItemViewHolder.image.setImageResource(R.drawable.ic_themtien96);
                subItemViewHolder.tvMoney.setTextColor(Color.parseColor("#43A047"));
                break;
            case "c":
                subItemViewHolder.image.setImageResource(R.drawable.ic_trutien96);
                subItemViewHolder.tvMoney.setTextColor(Color.parseColor("#F44336"));
                break;
            case "v":
                subItemViewHolder.image.setImageResource(R.drawable.ic_chovay96);
                subItemViewHolder.tvMoney.setTextColor(Color.parseColor("#F44336"));
                break;
            case "n":
                subItemViewHolder.image.setImageResource(R.drawable.ic_khoanno96);
                subItemViewHolder.tvMoney.setTextColor(Color.parseColor("#43A047"));
                break;
            case "l":
                subItemViewHolder.image.setImageResource(R.drawable.take);
                subItemViewHolder.tvMoney.setTextColor(Color.parseColor("#43A047"));
                break;
            case "d":
                subItemViewHolder.image.setImageResource(R.drawable.give);
                subItemViewHolder.tvMoney.setTextColor(Color.parseColor("#F44336"));
                break;
        }
        subItemViewHolder.tvDes.setText(phatSinh1.getMota());
        subItemViewHolder.setiItemClickListener(new IItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                if(subItemList.get(i).getLoaiphatsinh().compareTo("t") == 0 || subItemList.get(i).getLoaiphatsinh().compareTo("c") == 0){
                    Intent intent1 = new Intent(context, DeleteEditRevenueExpenditure.class);
                    intent1.putExtra("ma_phatsinh", subItemList.get(i).getMa_phatsinh());
                    context.startActivity(intent1);
                    return;
                }
                if(subItemList.get(i).getLoaiphatsinh().compareTo("v") == 0 || subItemList.get(i).getLoaiphatsinh().compareTo("n") == 0){
                    Intent intent = new Intent(context, DeleteEditLoanBorrow.class);
                    intent.putExtra("ma_phatsinh", subItemList.get(i).getMa_phatsinh());
                    context.startActivity(intent);
                    return;
                }
                if(subItemList.get(i).getLoaiphatsinh().compareTo("l") == 0 || subItemList.get(i).getLoaiphatsinh().compareTo("d") == 0){
                    Intent intent = new Intent(context, DeletePayActivity.class);
                    intent.putExtra("ma_phatsinh", subItemList.get(i).getMa_phatsinh());
                    context.startActivity(intent);
                    return;
                }

            }
        });
        api.getCheck(subItemList.get(i).getMa_phatsinh()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() == 200)
                {
                    subItemViewHolder.tvSubItemTitle.setPaintFlags(subItemViewHolder.tvSubItemTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    subItemViewHolder.tvMoney.setPaintFlags(subItemViewHolder.tvSubItemTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return subItemList.size();
    }

    class SubItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvSubItemTitle, tvDes, tvMoney;
        ImageView image;
        IItemClickListener iItemClickListener;

        public void setiItemClickListener(IItemClickListener iItemClickListener){
            this.iItemClickListener = iItemClickListener;
        }
        SubItemViewHolder(View itemView) {
            super(itemView);
            tvSubItemTitle = itemView.findViewById(R.id.tv_title);
            tvDes = itemView.findViewById(R.id.tv_description);
            image = itemView.findViewById(R.id.im_item_rw);
            tvMoney = itemView.findViewById(R.id.tv_money);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            iItemClickListener.onItemClickListener(v, getAdapterPosition());
        }
    }
}