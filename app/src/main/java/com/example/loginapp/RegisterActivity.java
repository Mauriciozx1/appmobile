package com.example.loginapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RegisterActivity extends AppCompatActivity  implements View.OnClickListener{

    private EditText TextEmail;
    private EditText TextPassword;
    private EditText TextName, TextLastName;
    private DatabaseReference Users;
    private Button btnRegistrar,btnTerminos;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private CheckBox seleccionTerminos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        //inicializamos el objeto firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();
        Users = FirebaseDatabase.getInstance().getReference("Users");

        //Referenciamos los views
        TextEmail = (EditText)findViewById(R.id.txtEmailRegister);
        TextPassword = (EditText)findViewById(R.id.txtPassRegister);
        TextName =(EditText)findViewById(R.id.txtName);
        TextLastName =(EditText)findViewById(R.id.txtLastName);

        btnRegistrar = (Button) findViewById(R.id.btnRgt);
        btnTerminos = (Button) findViewById(R.id.btnLeerTerminos);
        seleccionTerminos = (CheckBox)findViewById(R.id.checkTerminos);

        progressDialog = new ProgressDialog(this);

        //asociamos un oyente al evento clic del botón
        btnRegistrar.setOnClickListener(this);
        btnTerminos.setOnClickListener(this);
    }

    private void registrarUsuario(){
        final String email = TextEmail.getText().toString().trim();
        String password = TextPassword.getText().toString().trim();
        final String name = TextName.getText().toString().trim().toLowerCase();
        final String lastname = TextLastName.getText().toString().trim().toLowerCase();


        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Se debe ingresar un nombre", Toast.LENGTH_LONG).show();
            return;
        }
        else if(TextUtils.isEmpty(lastname)){
            Toast.makeText(this, "Se debe ingresar un apellido", Toast.LENGTH_LONG).show();
            return;
        }
        else if (TextUtils.isEmpty(email)) {//(precio.equals(""))
            Toast.makeText(this, "Se debe ingresar un email", Toast.LENGTH_LONG).show();
            return;
        }

        else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Falta ingresar la contraseña", Toast.LENGTH_LONG).show();
            return;
        }
        else if(seleccionTerminos.isChecked() == false){
            Toast.makeText(this, "Lee y acepta los Terminos para registrarte", Toast.LENGTH_LONG).show();
            return;
        }
        progressDialog.setMessage("Realizando registro en linea...");
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success

                        if (task.isSuccessful()) {
                            String id = Users.push().getKey();
                            Users usuario = new Users(id, name,lastname, email);
                            Users.child(id).setValue(usuario);
                            Toast.makeText(RegisterActivity.this, "Se ha registrado el usuario con el email: " + TextEmail.getText(), Toast.LENGTH_LONG).show();
                            int pos = email.indexOf("@");
                            String correo = email.substring(0, pos);
                            Toast.makeText(RegisterActivity.this, "Bienvenido: " + TextName.getText() +" "+ TextLastName.getText(), Toast.LENGTH_LONG).show();
                            Intent intencion = new Intent(getApplication(), WelcomeActivity.class);
                            intencion.putExtra(WelcomeActivity.user, name);
                            startActivity(intencion);
                        } else {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {//si se presenta una colisión
                                Toast.makeText(RegisterActivity.this, "Ese usuario ya existe ", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(RegisterActivity.this, "No se pudo registrar el usuario ", Toast.LENGTH_LONG).show();
                            }
                        }
                        progressDialog.dismiss();
                    }
                });
    }
    private void leerTerminos(){
        Intent intent = new Intent(this, WebviewActivity.class);
        startActivity(intent);
    }
    private void verificarUsuario(){
    }
    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btnRgt:
                //Invocamos al método:
                registrarUsuario();
                break;
            case R.id.btnLeerTerminos:
                leerTerminos();
                break;
        }


    }
}
