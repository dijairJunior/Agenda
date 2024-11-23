package dijairdev.com.br.ui.edita.contato;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import dijairdev.com.br.R;
import dijairdev.com.br.modelo.Contato;

public class EditaContatoFragment extends Fragment {

    private static final int RESULT_CAMERA = 1000;
    private static final int PERMISSIONS_REQUEST_SEND_SMS = 2000;
    private String mCurrentPhotoPath;
    private ImageView imageViewFoto;
    private EditText edtNome, edtEmail, edtTelefone, edtCep, edtEndereco;
    private Button buttonCep, buttonSMS;
    private FloatingActionButton fabSalvar, fabExcluir;
    private Contato contato;

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

    private File createImageFile() throws IOException {
        //cria o arquivo para receber a foto
        String imageFileName = "jpeg_temp_foto";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName, /* arquivo */
                ".jpg", /* extensão */
                storageDir /* diretório */
        );

        //armazena o caminho para o arquivo
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // procura um aplicativo para resolver a requisição da câmera
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // criação do arquivo
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.e(getClass().getSimpleName(), "Erro ao obter foto: " + ex.getMessage());
            }
            // Se o arquivo foi criado com sucesso, aciona a câmera
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity(),
                        "dijairdev.com.br.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, RESULT_CAMERA);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_CAMERA) {
            if (resultCode != AppCompatActivity.RESULT_OK) {
                return;
            }
            Bitmap img = BitmapFactory.decodeFile(mCurrentPhotoPath);
            imageViewFoto.setImageBitmap(img);
        }
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //associação de componentes com a view
        imageViewFoto = view.findViewById(R.id.imageViewFoto);
        fabSalvar = view.findViewById(R.id.fabSalvar);
        fabExcluir = view.findViewById(R.id.fabExcluir);
        buttonCep = view.findViewById(R.id.buttonCep);
        buttonSMS = view.findViewById(R.id.buttonSMS);
        edtNome = view.findViewById(R.id.edtNome);
        edtEndereco = view.findViewById(R.id.edtEndereco);
        edtEmail = view.findViewById(R.id.edtEmail);
        edtTelefone = view.findViewById(R.id.edtTelefone);
        edtCep = view.findViewById(R.id.edtCep);

        //configura o evento de click para a foto
        imageViewFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        /* no final do método onViewCreated .... */
        buttonSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtTelefone.getText().toString().equals("")) {
                    return;
                }
                verificaPermissao();
            }
        });
    }

    private void verificaPermissao() {
        //verifica se o aplicativo tem permissão
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            //verifica se a permissão já foi negada no passado
            if (shouldShowRequestPermissionRationale(Manifest.permission.SEND_SMS)) {
                Toast.makeText(getActivity(), R.string.erro_sms, Toast.LENGTH_LONG).show();
            } else {
                //abre a caixa de diálogo de solicitação da permissão
                requestPermissions(new String[]{Manifest.permission.SEND_SMS},
                        PERMISSIONS_REQUEST_SEND_SMS);
            }
        } else {
            enviarSMS(edtTelefone.getText().toString());
        }
    }

    protected void enviarSMS(String telefone) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(telefone,
                null, "Olá. Adicionei seu número.", null, null);
        Toast.makeText(getActivity(), R.string.mensagem_enviada, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_REQUEST_SEND_SMS: {
                if (permissions[0].equalsIgnoreCase  (Manifest.permission.SEND_SMS) && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
                    //tudo OK, pode enviar
                    enviarSMS(edtTelefone.getText().toString());
                } else {
                    Log.d(getClass().getSimpleName(), "Sem permissão para enviar SMS.");
                }
            }
        }
    }

}