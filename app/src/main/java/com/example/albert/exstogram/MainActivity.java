package com.example.albert.exstogram;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class MainActivity extends AppCompatActivity {

    EditText editTextUsername,editTextPassword;
    TextView textView;
    Button button;
    boolean loginMode=true;
    long timeClicked;
    int duration=2500;
    boolean clicked=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        MobileAds.initialize(this, "ca-app-pub-7627846972046326~3791030574");
        editTextUsername=(EditText)findViewById(R.id.edittext_username);
        editTextPassword=(EditText)findViewById(R.id.edittext_password);
        textView=(TextView)findViewById(R.id.textStatus);
        button=(Button)findViewById(R.id.button);

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




//        ParseObject obj=new ParseObject("Bukantabelmu");
//        obj.put("name","Albert");
//        obj.put("Nationality","INA");
//        obj.saveInBackground(new SaveCallback() {
//            @Override
//            public void done(ParseException e) {
//                if(e==null){
//                    Toast.makeText(MainActivity.this, "Berhasil bos", Toast.LENGTH_SHORT).show();
//                }else{
//                    Toast.makeText(MainActivity.this, "Gagal coy"+e.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
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
}
