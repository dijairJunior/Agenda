package dijairdev.com.br.ui.lista.contato;

import android.arch.lifecycle.ViewModelProviders;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.navigation.fragment.NavHostFragment;

import java.util.List;

import dijairdev.com.br.R;
import dijairdev.com.br.adapter.ListaContatoAdapter;
import dijairdev.com.br.dao.AgendaDaoSQLite;
import dijairdev.com.br.modelo.Contato;

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

    private AdapterView.OnItemClickListener onItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //envia para o fragment o contato escolhido na lista
            Contato contato = (Contato) parent.getItemAtPosition(position);
            Bundle dados = new Bundle();
            dados.putSerializable("contato", contato);

            NavHostFragment.findNavController(ListaContatoFragment.this)
                    .navigate(R.id.action_nav_lista_contato_to_nav_edita_contato, dados);
        }
    };

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

        //atribuição click itens do ListView
        ListView listViewContatos = view.findViewById(R.id.listViewContatos);

        //Atribuição click fabNovo
        FloatingActionButton fabNovo = view.findViewById(R.id.fabNovo);
        fabNovo.setOnClickListener(onNovoClick);
    }

    //classe assíncrona para listar os contatos cadastrados
    private class ListaContatoTask extends AsyncTask<String, Integer, List<Contato>> {

        @Override
        protected List<Contato> doInBackground(String... strings) {
            AgendaDaoSQLite agendaDaoSQLite = new AgendaDaoSQLite(getActivity());
            return agendaDaoSQLite.listaContatos();
        }

        @Override
        protected void onPostExecute(List<Contato> contatos) {
            ListView listViewContatos = getActivity().findViewById(R.id.listViewContatos);
            listViewContatos.setAdapter(new ListaContatoAdapter(getActivity(), contatos));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        new ListaContatoTask().execute();
    }
}