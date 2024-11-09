package dijairdev.com.br.ui.lista.contato;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.navigation.fragment.NavHostFragment;

import dijairdev.com.br.R;

public class ListaContatoFragment extends Fragment {

    private ListaContatoViewModel mViewModel;

    private View.OnClickListener onNovoClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            NavHostFragment.findNavController(ListaContatoFragment.this)
                    .navigate(R.id.action_nav_lista_contato_to_nav_edita_contato);
        }
    };

    public static ListaContatoFragment newInstance() {
        return new ListaContatoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.lista_contato_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ListaContatoViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Atribuição click fabNovo
        FloatingActionButton fabNovo = view.findViewById(R.id.fabNovo);
        fabNovo.setOnClickListener(onNovoClick);
    }
}