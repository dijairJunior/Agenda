package dijairdev.com.br.ui.edita.contato;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dijairdev.com.br.R;

public class EditaContatoFragment extends Fragment {

    private EditaContatoViewModel mViewModel;

    public static EditaContatoFragment newInstance() {
        return new EditaContatoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.edita_contato_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(EditaContatoViewModel.class);
        // TODO: Use the ViewModel
    }

}