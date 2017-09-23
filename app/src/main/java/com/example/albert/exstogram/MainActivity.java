package com.example.albert.exstogram;

import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editTextUsername,editTextPassword;
    TextView textView;
    Button button;
    boolean loginMode=true;
    long timeClicked;
    int duration=2500;
    boolean clicked=false;
    RelativeLayout relativeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        MobileAds.initialize(this, "ca-app-pub-7627846972046326~3791030574");
        editTextUsername=(EditText)findViewById(R.id.edittext_username);
        editTextPassword=(EditText)findViewById(R.id.edittext_password);
        editTextPassword.setOnKeyListener(new View.OnKeyListener(){

            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i==keyEvent.KEYCODE_ENTER&&keyEvent.getAction()==KeyEvent.ACTION_DOWN){
                    BtnClicked(null);
                }
                return false;
            }
        });
        textView=(TextView)findViewById(R.id.textStatus);
        button=(Button)findViewById(R.id.button);
        relativeLayout=(RelativeLayout)findViewById(R.id.relLayout);
        relativeLayout.setOnClickListener(this);
        ParseInit();
        if(ParseUser.getCurrentUser()!=null){
            gotoHome();
        }
    }
        private void ParseInit(){
            Parse.initialize(new Parse.Configuration.Builder(this)
            .applicationId("9c344d161a974bb20f0cbb5e869a455e2bacb9ef")
            .server("http://ec2-52-14-11-143.us-east-2.compute.amazonaws.com/parse")
            .build());
        }
    public void BtnClicked(View view){
        TryLoginSignUp();
    }
    private void TryLoginSignUp(){
        if(loginMode){
            ParseUser.logInInBackground(editTextUsername.getText().toString(), editTextPassword.getText().toString(), new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if (e == null) {
                        Toast.makeText(MainActivity.this, "Berhasil Login bos "+user.getUsername(), Toast.LENGTH_SHORT).show();
                        gotoHome();
                    } else {
                        Toast.makeText(MainActivity.this, "Gagal Login coy " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else {
            ParseUser user = new ParseUser();
            user.setUsername(editTextUsername.getText().toString());
            user.setPassword(editTextPassword.getText().toString());
            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Toast.makeText(MainActivity.this, "Berhasil Daftar bos", Toast.LENGTH_SHORT).show();
                        gotoHome();
                    } else {
                        Toast.makeText(MainActivity.this, "Gagal Daftar coy " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void ubahText(View view){

        if(loginMode){
            textView.setText("Or, SIGN IN");
            button.setText("REGISTER");
            loginMode=false;
        }else {
            textView.setText("Or, SIGN UP");
            button.setText("LOGIN");
            loginMode=true;
        }

    }
    public void gotoHome(){
        Intent intent=new Intent(getApplicationContext(),home.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if(clicked){
            if((System.currentTimeMillis() - timeClicked) < duration){
                System.exit(1);
            } else {
                Toast.makeText(this, "Press Back Again To Exit", Toast.LENGTH_SHORT).show();
                timeClicked = System.currentTimeMillis();
            }
        } else {
            clicked = true;
            Toast.makeText(this, "Press Back Again To Exit", Toast.LENGTH_SHORT).show();
            timeClicked = System.currentTimeMillis();
        }
    }
    private void HideBar() {
        // Hide Action Bar
        getSupportActionBar().hide();

        // Hide Status Bar
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
    }

    @Override
    public void onClick(View view) {
        InputMethodManager inputMethodManager=(InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
    }
}
