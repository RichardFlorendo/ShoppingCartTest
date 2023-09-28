package com.example.shoppingcarttest.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppingcarttest.MainActivity;
import com.example.shoppingcarttest.R;
import com.example.shoppingcarttest.datamodel.ItemModel;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder>{
    private static final int TYPE = 1;
    private Context context;
    private final List<Object> listItem;

    public ItemAdapter(Context context, List<Object> listRecyclerItem) {
        this.context = context;
        this.listItem = listRecyclerItem;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE:
            default:
                View layoutView = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.singleitem, parent, false);
                View mainView = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.activity_main, parent, false);

                ViewHolder holder = new ViewHolder(layoutView, mainView, p -> {

                });
                return holder;
                //return new ViewHolder(layoutView);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int viewType = getItemViewType(position);

        SharedPreferences pref = this.context.getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = pref.edit();

        switch (viewType) {
            case TYPE:
            default:

                ViewHolder ViewHolder = holder;
                ItemModel itemModel = (ItemModel) listItem.get(position);

                String imageName = itemModel.getProdId();
                int resID = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());

                String itemID = String.valueOf((position + 1));

                holder.idIV.setImageResource(resID);
                holder.nameTV.setText(itemModel.getName());
                holder.categoryTV.setText(itemModel.getCategory());
                holder.costTV.setText("$" + itemModel.getCost().substring(0, itemModel.getCost().indexOf(".")));
                holder.bgIV.setBackgroundColor((Color.parseColor(itemModel.getColor())));
                holder.addBTN.setOnClickListener(v -> {

                    editor.putInt("p_" + itemID + "ID", position + 1);
                    editor.putBoolean("isCartEmpty", false);

                    editor.commit();

                    ((MainActivity)context).refreshActivtiy();
                });

        }
    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        MyClickListener listener;
        public ImageView idIV;
        public TextView nameTV;
        public TextView categoryTV;
        public TextView costTV;
        public ImageView bgIV;
        public TextView notifNameTV;
        public Button addBTN;
        public CardView notifCV;
        public TextView cartTV;
        public ImageButton notifBTN;

        public ViewHolder(View itemView, View mainView, MyClickListener listener) {
            super(itemView);

            idIV = itemView.findViewById(R.id.productimage);
            nameTV = itemView.findViewById(R.id.productname);
            categoryTV = itemView.findViewById(R.id.category);
            costTV = itemView.findViewById(R.id.productcost);
            bgIV = itemView.findViewById(R.id.productimage);
            addBTN = itemView.findViewById(R.id.addBTN);

//            notifCV = mainView.findViewById(R.id.addNotif);
//            notifNameTV = mainView.findViewById(R.id.itemNotifName);
//            notifBTN = mainView.findViewById(R.id.notifClose);

            cartTV = itemView.findViewById(R.id.cartnum);

            this.listener = listener;

            addBTN.setOnClickListener(this);
//            notifBTN.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onAdd(this.getLayoutPosition());
        }
    }

    public interface MyClickListener {
        void onAdd(int p);
    }

}
