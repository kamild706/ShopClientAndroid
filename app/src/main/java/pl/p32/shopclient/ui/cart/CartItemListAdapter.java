package pl.p32.shopclient.ui.cart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class CartItemListAdapter extends RecyclerView.Adapter<CartItemListAdapter.CartItemViewHolder> {

    class CartItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView nameView;
        private final TextView priceView;
        private final ImageView imageView;
        private final TextView quantityView;

        CartItemViewHolder(@NonNull View itemView) {
            super(itemView);

            nameView = itemView.findViewById(R.id.name_view);
            priceView = itemView.findViewById(R.id.price_view);
            imageView = itemView.findViewById(R.id.image_view);
            quantityView = itemView.findViewById(R.id.quantity_view);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    private List<CartItemDao.OrderedProduct> orderedProducts = Collections.emptyList();
    private ItemClickListener mClickListener;
    private Context context;

    public CartItemListAdapter(Context context) {
        this.context = context;
    }

    public void setOrderedProducts(List<CartItemDao.OrderedProduct> orderedProducts) {
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
        holder.priceView.setText(String.valueOf(product.price));
        holder.quantityView.setText(String.valueOf(product.quantity));
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
