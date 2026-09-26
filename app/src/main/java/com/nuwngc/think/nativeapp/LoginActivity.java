package com.nuwngc.think.nativeapp;
import android.app.*;import android.os.*;import android.content.*;import android.view.*;import android.widget.*;import org.json.*;
public class LoginActivity extends Activity{
 EditText u,p; TextView err; ProgressBar prog;
 public void onCreate(Bundle b){super.onCreate(b);Api.init(this);setContentView(R.layout.activity_login);u=findViewById(R.id.username);p=findViewById(R.id.password);err=findViewById(R.id.error);prog=findViewById(R.id.progress);findViewById(R.id.login).setOnClickListener(v->login());if(Api.hasSession())checkSession();}
 void checkSession(){new Thread(()->{try{JSONObject j=Api.get("/api/me");if(!j.isNull("user")){JSONObject userJson=j.getJSONObject("user");Api.cache("user_json",userJson.toString());go(userJson.optBoolean("mustChangePassword"));}else Api.clearSession();}catch(Exception e){ } }).start();}
 void login(){String user=u.getText().toString().trim(),pass=p.getText().toString();if(user.isEmpty()||pass.isEmpty()){err.setText("Nhập đầy đủ thông tin.");return;}prog.setVisibility(View.VISIBLE);err.setText("");new Thread(()->{try{JSONObject j=Api.post("/api/login",new JSONObject().put("username",user).put("password",pass));JSONObject userJson=j.getJSONObject("user");Api.cache("user_json",userJson.toString());go(userJson.optBoolean("mustChangePassword"));}catch(Exception e){runOnUiThread(()->err.setText(e.getMessage()));}finally{runOnUiThread(()->prog.setVisibility(View.GONE));}}).start();}
 void go(boolean change){runOnUiThread(()->{Intent i=new Intent(this,change?ChangePasswordActivity.class:MainActivity.class);startActivity(i);finish();});}
}
