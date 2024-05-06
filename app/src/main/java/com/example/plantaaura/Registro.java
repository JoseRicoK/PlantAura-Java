package com.example.plantaaura;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;



public class Registro extends AppCompatActivity {


    EditText mEmail, mPass;
    Button btnRegis;
    ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    TextView mTenerCuentaTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        mEmail = findViewById(R.id.emailEt);
        mPass = findViewById(R.id.passEt);
        btnRegis = findViewById(R.id.btnRegistro);
        mTenerCuentaTv = findViewById(R.id.tenerCuentaTv);

        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registrando usuario....");




        btnRegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = mEmail.getText().toString().trim();
                String contra = mPass.getText().toString().trim();

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    mEmail.setError("Email invalido");
                    mEmail.setFocusable(true);

                }else if(contra.length()<6){
                    mPass.setError("Contraseña superior a 6 digitos ");
                    mPass.setFocusable(true);

                }else{
                    registerUser(email,contra);
                }
            }
        });

        mTenerCuentaTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Registro.this,Login.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void registerUser(String email, String contra) {

        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email,contra)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(Registro.this,"Registrado...\n"+user.getEmail(),Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(Registro.this,Perfil.class);
                            startActivity(intent);
                            finish();

                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(Registro.this, "Autentificacion fallida.....", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                    }
                });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}