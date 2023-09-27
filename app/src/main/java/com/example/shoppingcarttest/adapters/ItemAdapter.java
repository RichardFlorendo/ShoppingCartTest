package com.example.shoppingcarttest.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

                ViewHolder holder = new ViewHolder(layoutView, p -> {

                });
                return holder;
                //return new ViewHolder(layoutView);
        }
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int viewType = getItemViewType(position);

        MainActivity activity = new MainActivity();

        SharedPreferences pref = this.context.getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

        switch (viewType) {
            case TYPE:
            default:

                ViewHolder ViewHolder = holder;
                ItemModel itemModel = (ItemModel) listItem.get(position);

                String imageName = itemModel.getProdId();
                int resID = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());

                String itemID = itemModel.getProdId();

                ViewHolder.idIV.setImageResource(resID);
                ViewHolder.nameTV.setText(itemModel.getName());
                ViewHolder.categoryTV.setText(itemModel.getCategory());
                ViewHolder.costTV.setText("$" + itemModel.getCost().substring(0, itemModel.getCost().indexOf(".")));
                ViewHolder.bgIV.setBackgroundColor((Color.parseColor(itemModel.getColor())));

                ViewHolder.notifCV.setBackgroundColor((Color.parseColor(itemModel.getColor())));
                ViewHolder.notifNameTV.setText(itemModel.getName());

                holder.addBTN.setOnClickListener(v -> {
                    editor.putString(itemID + "ID", itemID); // Storing string
                    editor.putString(itemID + "ItemName", itemModel.getName()); // Storing string
                    editor.putString(itemID + "ItemCost", itemModel.getCost()); // Storing string
                    editor.putString(itemID + "BgColor", itemModel.getColor()); // Storing string
                    editor.putBoolean("isCartEmpty", false);

                    editor.commit();

                    ((MainActivity)context).refreshActivtiy();
                });
        }
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return listItem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        MyClickListener listener;
        public ImageView idIV;
        public TextView nameTV;
        public TextView categoryTV;
        public TextView costTV;
        public ImageView bgIV;
        public TextView notifNameTV;
        public Button addBTN;
        public CardView notifCV;
        public ImageView cartIV;
        public TextView cartTV;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView, MyClickListener listener) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            idIV = itemView.findViewById(R.id.productimage);
            nameTV = itemView.findViewById(R.id.productname);
            categoryTV = itemView.findViewById(R.id.category);
            costTV = itemView.findViewById(R.id.productcost);
            bgIV = itemView.findViewById(R.id.productimage);
            addBTN = itemView.findViewById(R.id.addBTN);

            notifCV = itemView.findViewById(R.id.addNotif);
            notifNameTV = itemView.findViewById(R.id.itemNotifName);

            cartIV = itemView.findViewById(R.id.cartnotif);
            cartTV = itemView.findViewById(R.id.cartnum);

            this.listener = listener;

            addBTN.setOnClickListener(this);
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
