package dijairdev.com.br.ui.lista.compromisso;

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

import java.io.Serializable;
import java.util.List;

import dijairdev.com.br.R;
import dijairdev.com.br.adapter.ListaCompromissoAdapter;
import dijairdev.com.br.dao.AgendaDaoSQLite;
import dijairdev.com.br.modelo.Compromisso;

public class ListaCompromissoFragment extends Fragment {

    private ListaCompromissoViewModel mViewModel;
    private Compromisso compromisso;

    public static ListaCompromissoFragment newInstance() {
        return new ListaCompromissoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.lista_compromisso_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ListaCompromissoViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //atribuição click itens do ListView
        ListView listViewCompromissos = view.findViewById(R.id.listViewCompromissos);
        listViewCompromissos.setOnItemClickListener(onItemClick);

        //Atribuição click fabNovo
        FloatingActionButton fabNovo = view.findViewById(R.id.fabNovo);
        fabNovo.setOnClickListener(onNovoClick);

    }

    //classe assíncrona para listar os compromissos cadastrados
    private class ListaCompromissoTask extends AsyncTask<String, Integer, List<Compromisso>> {

        @Override
        protected List<Compromisso> doInBackground(String... strings) {
            AgendaDaoSQLite agendaDaoSQLite = new AgendaDaoSQLite(getActivity());
            return agendaDaoSQLite.listaCompromissos();
        }

        @Override
        protected void onPostExecute(List<Compromisso> compromissos) {
            ListView listViewCompromissos = getActivity().findViewById(R.id.listViewCompromissos);
            listViewCompromissos.setAdapter(new ListaCompromissoAdapter(getActivity(), compromissos));
        }
    }

    private AdapterView.OnItemClickListener onItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //envia para o fragment o contato escolhido na lista
            Compromisso compromisso = (Compromisso) parent.getItemAtPosition(position);
            Bundle dados = new Bundle();
            dados.putSerializable("compromisso", (Serializable) compromisso);

            NavHostFragment.findNavController(ListaCompromissoFragment.this)
                    .navigate(R.id.action_nav_lista_compromisso_to_nav_edita_compromisso, dados);
        }
    };

    private View.OnClickListener onNovoClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            NavHostFragment.findNavController(ListaCompromissoFragment.this)
                    .navigate(R.id.action_nav_lista_compromisso_to_nav_edita_compromisso);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        new ListaCompromissoTask().execute();
    }

}