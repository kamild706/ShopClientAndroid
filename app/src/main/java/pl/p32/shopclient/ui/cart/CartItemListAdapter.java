package pl.p32.shopclient.ui.cart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import pl.p32.shopclient.GlideApp;
import pl.p32.shopclient.R;
import pl.p32.shopclient.db.dao.CartItemDao;
import pl.p32.shopclient.utils.CurrencyFormatter;

public class CartItemListAdapter extends RecyclerView.Adapter<CartItemListAdapter.CartItemViewHolder> {

    class CartItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView nameView;
        private final TextView priceView;
        private final ImageView imageView;
        private final TextView quantityView;

        CartItemViewHolder(@NonNull View itemView) {
            super(itemView);

            nameView = itemView.findViewById(R.id.product_name);
            priceView = itemView.findViewById(R.id.product_total_price);
            imageView = itemView.findViewById(R.id.product_image);
            quantityView = itemView.findViewById(R.id.order_quantity);

            Button delButton = itemView.findViewById(R.id.del_button);
            delButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    private List<CartItemDao.OrderedProduct> orderedProducts = Collections.emptyList();
    private ItemClickListener mClickListener;
    private Context context;

    CartItemListAdapter(Context context) {
        this.context = context;
    }

    void setOrderedProducts(List<CartItemDao.OrderedProduct> orderedProducts) {
        this.orderedProducts = orderedProducts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView item = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_item, parent, false);

        return new CartItemViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemViewHolder holder, int position) {
        CartItemDao.OrderedProduct product = orderedProducts.get(position);
        holder.nameView.setText(product.name);

        CurrencyFormatter formatter = CurrencyFormatter.getInstance();
        holder.priceView.setText(formatter.getFormattedCurrency(product.price));

//        holder.quantityView.setText(String.valueOf(product.quantity));
        holder.quantityView.setText(context.getResources().getQuantityString(
                R.plurals.chosen_items, product.quantity, product.quantity));

        GlideApp
                .with(context)
                .load(product.imageUrl)
                .placeholder(R.drawable.placeholder)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return orderedProducts.size();
    }

    CartItemDao.OrderedProduct getItem(int position) {
        return orderedProducts.get(position);
    }

    void setClickListener(ItemClickListener mClickListener) {
        this.mClickListener = mClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
