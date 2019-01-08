package pl.p32.shopclient.ui.currencypicker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;
import pl.p32.shopclient.R;
import pl.p32.shopclient.viewmodel.CurrencyDialogViewModel;

public class CurrencyDialogFragment extends DialogFragment {

    private RadioGroup radioGroup;
    private CurrencyDialogViewModel mViewModel;

    public static CurrencyDialogFragment newInstance() {
        return new CurrencyDialogFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_currencypicker, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel = ViewModelProviders.of(this).get(CurrencyDialogViewModel.class);
        radioGroup = view.findViewById(R.id.picker);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            int id = radioGroup.getCheckedRadioButtonId();
            mViewModel.setCurrency(id);
        });

        showCurrency();
    }

    private void showCurrency() {
        String currency = mViewModel.getCurrency();

        switch (currency) {
            case "PLN":
                radioGroup.check(R.id.pln);
                break;
            case "USD":
                radioGroup.check(R.id.usd);
                break;
            case "EUR":
                radioGroup.check(R.id.eur);
                break;
            case "GBP":
                radioGroup.check(R.id.gbp);
                break;
        }
    }
}
