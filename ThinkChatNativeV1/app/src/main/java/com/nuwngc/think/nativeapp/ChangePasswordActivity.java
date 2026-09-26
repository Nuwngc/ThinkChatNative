package com.nuwngc.think.nativeapp;
import android.app.*;import android.os.*;import android.content.*;import android.widget.*;import org.json.*;
public class ChangePasswordActivity extends Activity{
 EditText cur,nw;TextView err;
 public void onCreate(Bundle b){super.onCreate(b);Api.init(this);setContentView(R.layout.activity_change_password);cur=findViewById(R.id.current);nw=findViewById(R.id.newpass);err=findViewById(R.id.error);findViewById(R.id.save).setOnClickListener(v->save());}
 void save(){String n=nw.getText().toString();if(n.length()<6){err.setText("Mật khẩu mới cần từ 6 ký tự.");return;}new Thread(()->{try{JSONObject out=new JSONObject();out.put("currentPassword",cur.getText().toString());out.put("newPassword",n);JSONObject r=Api.post("/api/me/password",out);if(r.has("user"))Api.cache("user_json",r.getJSONObject("user").toString());runOnUiThread(()->{startActivity(new Intent(this,MainActivity.class));finish();});}catch(Exception e){runOnUiThread(()->err.setText(e.getMessage()));}}).start();}
}
