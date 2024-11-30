package dijairdev.com.br.ui.edita.compromisso;

import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.navigation.fragment.NavHostFragment;

import dijairdev.com.br.R;
import dijairdev.com.br.dao.AgendaDaoSQLite;
import dijairdev.com.br.modelo.Compromisso;
import dijairdev.com.br.util.MaskEditUtil;

public class EditaCompromissoFragment extends Fragment {

    private EditaCompromissoViewModel mViewModel;

    public static EditaCompromissoFragment newInstance() {
        return new EditaCompromissoFragment();
    }

    private EditText editTextCurto, editTextLongo, editTextData, editTextHora;
    private FloatingActionButton fabSalvar, fabExcluir;
    private Compromisso compromisso;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextCurto = view.findViewById(R.id.editTextCurto);
        editTextLongo = view.findViewById(R.id.editTextLongo);
        editTextData = view.findViewById(R.id.editTextData);
        editTextData.addTextChangedListener(MaskEditUtil.mask(editTextData, "##/##/####"));
        editTextHora = view.findViewById(R.id.editTextHora);
        editTextHora.addTextChangedListener(MaskEditUtil.mask(editTextHora, "##:##"));
        fabSalvar = view.findViewById(R.id.fabSalvar);
        fabExcluir = view.findViewById(R.id.fabExcluir);

        //configura o evento de click para o botão salvar
        fabSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextCurto.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), R.string.erro_cadastro_compromisso, Toast.LENGTH_LONG).show();
                    return;
                }
                //setar campos
                if (compromisso == null) compromisso = new Compromisso();
                compromisso.setTextoCurto(editTextCurto.getText().toString());
                compromisso.setTextoLongo(editTextLongo.getText().toString());
                compromisso.setData(editTextData.getText().toString());
                compromisso.setHora(editTextHora.getText().toString());
                //execussão assincrona
                new SalvaCompromissoTask().execute(compromisso);
            }
        });

        //configura o evento de click para o botão excluir
        fabExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder
                        .setTitle(R.string.titulo_dialolgo_excluir)
                        .setMessage(R.string.confirma_excluir)
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new ExcluirCompromissoTask().execute(compromisso);
                            }
                        })
                        .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //não faz nada
                            }
                        }).setCancelable(false).show();
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.edita_compromisso_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(EditaCompromissoViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getArguments() != null) {
            compromisso = (Compromisso) getArguments().getSerializable("compromisso");
            if (compromisso != null) {
                preencheCampos(compromisso);
            }
        }
    }

    public void preencheCampos(Compromisso compromisso) {
        editTextCurto.setText(compromisso.getTextoCurto());
        editTextLongo.setText(compromisso.getTextoLongo());
        editTextData.setText(compromisso.getData());
        editTextHora.setText(compromisso.getHora());
    }

    //classe assíncrona para salvar um compromisso
    private class SalvaCompromissoTask extends AsyncTask<Compromisso, Integer, Long> {

        AgendaDaoSQLite agendaDaoSQLite = new AgendaDaoSQLite(getActivity());

        @Override
        protected Long doInBackground(Compromisso... compromissos) {
            if (compromissos[0].getId() == null) {
                return agendaDaoSQLite.grava(compromissos[0]);
            } else {
                agendaDaoSQLite.atualiza(compromissos[0]);
                return compromissos[0].getId();
            }
        }

        @Override
        protected void onPostExecute(Long id) {
            Toast.makeText(getActivity(), R.string.sucesso_cadastro, Toast.LENGTH_LONG).show();
            compromisso.setId(id);
            agendaDaoSQLite.close();
            //volta para lista após o salvamento
            NavHostFragment.findNavController(EditaCompromissoFragment.this)
                    .navigate(R.id.action_nav_edita_compromisso_to_nav_lista_compromisso);
        }
    }

    //classe assíncrona para excluir um compromisso
    private class ExcluirCompromissoTask extends AsyncTask<Compromisso, Integer, Integer> {

        AgendaDaoSQLite agendaDaoSQLite = new AgendaDaoSQLite(getActivity());

        @Override
        protected Integer doInBackground(Compromisso...compromissos) {
            return agendaDaoSQLite.exclui(compromissos[0]);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            //volta para lista após o exclusão
            NavHostFragment.findNavController(EditaCompromissoFragment.this)
                    .navigate(R.id.action_nav_edita_compromisso_to_nav_lista_compromisso);
        }
    }
}