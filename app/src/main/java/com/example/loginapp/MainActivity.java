package com.example.loginapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText TextEmail;
    private EditText TextPassword;
    private Button BotonLogin,BotonRegister;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //inicializamos el objeto firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();

        //Referenciamos los views
        TextEmail = (findViewById(R.id.txtEmail));
        TextPassword = (findViewById(R.id.txtPassword));

        BotonLogin = (findViewById(R.id.btnLogin));
        BotonRegister = (findViewById(R.id.btnRegister));

        progressDialog = new ProgressDialog(this);

        //asociamos un oyente al evento clic del botón
        BotonLogin.setOnClickListener(this);
        BotonRegister.setOnClickListener(this);
    }
    private void loguearUsuario() {
        //Obtenemos el email y la contraseña desde las cajas de texto
        final String email = TextEmail.getText().toString().trim();
        String password = TextPassword.getText().toString().trim();

        //Verificamos que las cajas de texto no esten vacías
        if (TextUtils.isEmpty(email)) {//(precio.equals(""))
            Toast.makeText(this, "Se debe ingresar un email", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Falta ingresar la contraseña", Toast.LENGTH_LONG).show();
            return;
        }


        progressDialog.setMessage("Realizando consulta en linea...");
        progressDialog.show();

        //loguear usuario
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //checking if success
                if (task.isSuccessful()) {
                    int pos = email.indexOf("@");
                    String user = email.substring(0, pos);
                    Toast.makeText(MainActivity.this, "Bienvenido: " + TextEmail.getText(), Toast.LENGTH_LONG).show();
                    Intent intencion = new Intent(getApplication(), WelcomeActivity.class);
                    intencion.putExtra(WelcomeActivity.user, user);
                    startActivity(intencion);


                }else{
                    Toast.makeText(MainActivity.this, "Email o Contraseña erroneas intenta otra vez." , Toast.LENGTH_LONG).show();
                }
                progressDialog.dismiss();
            }
            });
        }
        private void registerUsuario(){
            Intent i = new Intent(this, RegisterActivity.class);
            startActivity(i);
        }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnLogin:
                loguearUsuario();
                break;
            case R.id.btnRegister:
                registerUsuario();
                break;
        }


    }

}
