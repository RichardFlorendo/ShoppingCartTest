package com.example.shoppingcarttest.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppingcarttest.MainActivity;
import com.example.shoppingcarttest.R;
import com.example.shoppingcarttest.datamodel.CartModel;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder>{
    private static final int TYPE = 1;
    private Context context;
    private final List<Object> listItem;
    private MainActivity mainActivity;

    public CartAdapter(Context context, List<Object> listRecyclerItem) {
        this.context = context;
        this.listItem = listRecyclerItem;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE:
            default:
                View layoutView = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.cartitem, parent, false);

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

        mainActivity = new MainActivity();
        List<String> cartList = mainActivity.getList();

        SharedPreferences pref = this.context.getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

        switch (viewType) {
            case TYPE:
            default:

                ViewHolder ViewHolder = holder;
                CartModel cartModel = (CartModel) listItem.get(position);

                ViewHolder.cartnameTV.setText(cartModel.getItem());
                ViewHolder.cartcostTV.setText("$" + cartModel.getCost().substring(0, cartModel.getCost().indexOf(".")));
                ViewHolder.cartCV.setBackgroundColor((Color.parseColor(cartModel.getColor())));

                holder.cartdelBTN.setOnClickListener(v -> {
                    editor.remove(cartModel.getId()); // Storing string
                    editor.commit();

                    removeItem(holder.getAdapterPosition());

                    ((MainActivity)context).refreshActivtiy();
                });
        }
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return listItem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        MyClickListener listener;
        public ImageButton cartdelBTN;
        public TextView cartnameTV;
        public TextView cartcostTV;
        public CardView cartCV;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView, MyClickListener listener) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            cartnameTV = itemView.findViewById(R.id.cartname);
            cartcostTV = itemView.findViewById(R.id.cartcost);
            cartdelBTN = itemView.findViewById(R.id.cartdelBTN);

            cartCV = itemView.findViewById(R.id.cartList);

            this.listener = listener;

            cartdelBTN.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onAdd(this.getLayoutPosition());
        }
    }

    private void removeItem(int position) {
        listItem.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, listItem.size());
    }

    public interface MyClickListener {
        void onAdd(int p);
    }

}
