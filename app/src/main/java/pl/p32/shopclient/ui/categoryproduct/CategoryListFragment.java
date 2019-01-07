package pl.p32.shopclient.ui.categoryproduct;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import pl.p32.shopclient.R;
import pl.p32.shopclient.model.Category;
import pl.p32.shopclient.viewmodel.CategoryProductViewModel;

public class CategoryListFragment extends Fragment implements CategoryListAdapter.ItemClickListener{

    private CategoryProductViewModel model;
    private CategoryListAdapter mAdapter;

    static CategoryListFragment newInstance() {
        return new CategoryListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.category_list_fragment, container, false);

        RecyclerView mRecyclerView = view.findViewById(R.id.category_list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
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
    }

    @Override
    public void onItemClick(View view, int position) {
        Category category = mAdapter.getItem(position);
        model.chooseCategory(category);
    }
}
