package pl.p32.shopclient.ui.categoryproduct;

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
import pl.p32.shopclient.model.Product;
import pl.p32.shopclient.utils.CurrencyFormatter;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ProductViewHolder> {

    class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView nameView;
        private final TextView priceView;
        private final ImageView imageView;

        ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            nameView = itemView.findViewById(R.id.product_name);
            priceView = itemView.findViewById(R.id.product_price);
            imageView = itemView.findViewById(R.id.product_image);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    private List<Product> products = Collections.emptyList();
    private Context context;
    private ItemClickListener mClickListener;

    public ProductListAdapter(Context context) {
        this.context = context;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView item = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_row, parent, false);

        return new ProductViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = products.get(position);

        CurrencyFormatter formatter = CurrencyFormatter.getInstance();
        holder.priceView.setText(formatter.getFormattedCurrency(product.getPrice()));

        holder.nameView.setText(product.getName());
        GlideApp
                .with(context)
                .load(product.getImageUrl())
                .placeholder(R.drawable.placeholder)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public Product getItem(int position) {
        return products.get(position);
    }

    public void setClickListener(ItemClickListener mClickListener) {
        this.mClickListener = mClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
