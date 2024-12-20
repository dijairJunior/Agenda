package dijairdev.com.br.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import dijairdev.com.br.R;
import dijairdev.com.br.modelo.Contato;

public class ListaContatoAdapter extends BaseAdapter {
    private Context context;
    private List<Contato> contatos;

    public ListaContatoAdapter(Context context, List<Contato> contatos) {
        this.context = context;
        this.contatos = contatos;
    }

    @Override
    public int getCount() {
        return contatos.size();
    }

    @Override
    public Object getItem(int position) {
        return contatos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return contatos.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.lista_contato_adapter, parent, false);
        TextView txtNome = view.findViewById(R.id.txtNome);
        TextView txtEmail = view.findViewById(R.id.txtEmail);
        ImageView imageViewFoto = view.findViewById(R.id.imageViewFoto);

        Contato contato = (Contato) getItem(position);

        txtNome.setText(contato.getNome());
        txtEmail.setText(contato.getEmail());

        Bitmap img = BitmapFactory.decodeByteArray(contato.getFoto(), 0, contato.getFoto().length);
        imageViewFoto.setImageBitmap(img);

        return view;
    }
}
