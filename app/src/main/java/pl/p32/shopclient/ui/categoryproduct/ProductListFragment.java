package pl.p32.shopclient.ui.categoryproduct;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import pl.p32.shopclient.R;
import pl.p32.shopclient.ui.productdetails.ProductDetailsActivity;
import pl.p32.shopclient.viewmodel.CategoryProductViewModel;

public class ProductListFragment extends Fragment implements ProductListAdapter.ItemClickListener {

    private CategoryProductViewModel model;
    private ProductListAdapter mAdapter;

    static ProductListFragment newInstance() {
        return new ProductListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.product_list_fragment, container, false);

        RecyclerView mRecyclerView = view.findViewById(R.id.product_list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new ProductListAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setClickListener(this);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        model = ViewModelProviders.of(getActivity()).get(CategoryProductViewModel.class);
        Log.d("MYAPP_P", model.toString());
        model.getProducts().observe(this, products -> mAdapter.setProducts(products));
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(getActivity(), ProductDetailsActivity.class);
        intent.putExtra(ProductDetailsActivity.PRODUCT_ID, mAdapter.getItem(position).getId());
        startActivity(intent);
    }
}

