package com.example.abrahamarreola.facebooklogin;

import android.app.ProgressDialog;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class LoginScreen extends AppCompatActivity {

    LinearLayout mainLayout;
    TextInputLayout emailLayout, passLayout;
    TextInputEditText emailInput, passInput;
    Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        mainLayout = (LinearLayout)findViewById(R.id.main_layout);
        emailLayout = (TextInputLayout) findViewById(R.id.email_layout);
        emailInput = (TextInputEditText)findViewById(R.id.email_input);
        passLayout = (TextInputLayout) findViewById(R.id.password_layout);
        passInput = (TextInputEditText)findViewById(R.id.password_input);
        submitButton = (Button) findViewById(R.id.login_button);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailLayout.getEditText().getText().toString().trim();
                String password = passLayout.getEditText().getText().toString().trim();
                Boolean loginProcedure = true;

                if(email.isEmpty()){
                    emailLayout.setError("Username required");
                    loginProcedure = false;
                }

                if(password.isEmpty()){
                    passLayout.setError("Password required");
                    loginProcedure = false;
                }

                if(loginProcedure) login(email, password);

                InputMethodManager imm = (InputMethodManager)getSystemService(getBaseContext().INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mainLayout.getWindowToken(), 0);
            }
        });

        emailInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                emailLayout.setErrorEnabled(false);
            }
        });

        passInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) { passLayout.setErrorEnabled(false); }
        });
    }

    private void login(final String email, final String password){
        final ProgressDialog dialog = new ProgressDialog(this);
        final String url = "http://localhost:8000/login_facebook_app";

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getBaseContext(), response, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(), error.toString(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);

                return params;
            }
        };
        requestQueue.add(postRequest);
        dialog.setMessage("Logging in...");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
}
