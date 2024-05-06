package com.example.plantaaura;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Login extends AppCompatActivity {

    EditText mEmailL, mPassL;
    Button btnLog;
    TextView mNoTenerCuentaTv, mRecupPassTv;
    private FirebaseAuth mAuth;
    ProgressDialog pd;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mAuth = FirebaseAuth.getInstance();

        mEmailL = findViewById(R.id.emailEt);
        mPassL = findViewById(R.id.passEt);
        btnLog = findViewById(R.id.btnLogings);
        mNoTenerCuentaTv = findViewById(R.id.NoTenerCuentaTv);
        mRecupPassTv = findViewById(R.id.RecuPassTv);

        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = mEmailL.getText().toString().trim();
                String passw = mPassL.getText().toString().trim();

                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    mEmailL.setError("Email invalido");
                    mEmailL.setFocusable(true);
                }else{
                    loginUser(email,passw);
                }
            }
        });

        mNoTenerCuentaTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Login.this,Registro.class);
                startActivity(intent);
                finish();
            }
        });

        mRecupPassTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mostrarRecuPassDialog();
            }
        });
    }

    private void mostrarRecuPassDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Recuperar contraseña!..");

        LinearLayout linearLayout = new LinearLayout(this);
        final EditText emailEt = new EditText(this);
        emailEt.setHint("Email");
        emailEt.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        emailEt.setMinEms(16);


        linearLayout.addView(emailEt);
        linearLayout.setPadding(10,10,10,10);

        builder.setView(linearLayout);

        //recuperar
        builder.setPositiveButton("Recuperar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String email = emailEt.getText().toString().trim();
                procesoRecuperacion(email);

            }
        });

        //cancelar
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void procesoRecuperacion(String email) {
        pd = new ProgressDialog(this);
        pd.setMessage("Envio de correo electronico....");
        pd.show();
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                pd.dismiss();
                if(task.isSuccessful()){
                    Toast.makeText(Login.this,"Email enviado",Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(Login.this,"Fallo..",Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(Login.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });



    }

    private void loginUser(String email, String passw) {
        pd = new ProgressDialog(this);
        pd.setMessage("Logging In....");
        pd.show();
        mAuth.signInWithEmailAndPassword(email,passw)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            pd.dismiss();
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(Login.this,Perfil.class);
                            startActivity(intent);

                        }else{
                            pd.dismiss();
                            Toast.makeText(Login.this,"Autentificacion fallida.",Toast.LENGTH_SHORT).show();

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(Login.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return  super.onSupportNavigateUp();
    }
}