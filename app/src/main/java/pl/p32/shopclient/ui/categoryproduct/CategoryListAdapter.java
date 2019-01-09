package pl.p32.shopclient.ui.categoryproduct;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import pl.p32.shopclient.R;
import pl.p32.shopclient.model.Category;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.CategoryViewHolder> {

    class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView nameView;

        CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            nameView = itemView.findViewById(R.id.category_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    private List<Category> categories = Collections.emptyList();
    private ItemClickListener mClickListener;


    public void setCategories(List<Category> categories) {
        this.categories = categories;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ConstraintLayout item = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_row, parent, false);

        return new CategoryViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.nameView.setText(category.getName());
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    Category getItem(int position) {
        return categories.get(position);
    }

    void setClickListener(ItemClickListener mClickListener) {
        this.mClickListener = mClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
