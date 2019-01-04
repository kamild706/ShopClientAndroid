package pl.p32.shopclient.ui.categoryproduct;

import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import pl.p32.shopclient.R;
import pl.p32.shopclient.model.Category;
import pl.p32.shopclient.ui.homepage.ProductsGridAdapter;
import pl.p32.shopclient.viewmodel.CategoryProductViewModel;

public class CategoryListFragment extends Fragment implements CategoryListAdapter.ItemClickListener{

    private CategoryProductViewModel model;
    private RecyclerView mRecyclerView;
    private CategoryListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private OnCategoryChosenListener callback;

    public static CategoryListFragment newInstance() {
        return new CategoryListFragment();
    }

    public void setOnCategoryChosenListener(OnCategoryChosenListener callback) {
        this.callback = callback;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.category_list_fragment, container, false);

        mRecyclerView = view.findViewById(R.id.category_list);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new CategoryListAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setClickListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        model = ViewModelProviders.of(getActivity()).get(CategoryProductViewModel.class);
        model.getCategories().observe(this, categories -> mAdapter.setCategories(categories));
        Log.d("MYAPP_C", model.toString());
        model.getChosenCategory().observe(this, category -> callback.setCategoryTitle(category));
    }

    @Override
    public void onItemClick(View view, int position) {
        Category category = mAdapter.getItem(position);
        model.chooseCategory(category);
        callback.onCategoryChosen(category);
    }

    public interface OnCategoryChosenListener {
        void onCategoryChosen(Category category);
        void setCategoryTitle(Category category);
    }
}
