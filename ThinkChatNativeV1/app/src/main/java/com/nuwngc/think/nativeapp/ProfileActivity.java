package com.nuwngc.think.nativeapp;
import android.app.*;import android.os.*;import android.content.*;import android.widget.*;import org.json.*;
public class ProfileActivity extends Activity{
 TextView info,err;EditText name;JSONObject user;
 public void onCreate(Bundle b){super.onCreate(b);Api.init(this);setContentView(R.layout.activity_profile);info=findViewById(R.id.info);err=findViewById(R.id.error);name=findViewById(R.id.name);findViewById(R.id.save).setOnClickListener(v->save());findViewById(R.id.password).setOnClickListener(v->startActivity(new Intent(this,ChangePasswordActivity.class)));findViewById(R.id.logout).setOnClickListener(v->logout());load();}
 void load(){new Thread(()->{try{user=Api.get("/api/me").getJSONObject("user");runOnUiThread(()->{info.setText("@"+user.optString("username")+"\nID: "+user.optInt("id")+"\nTrạng thái: "+(user.optBoolean("online")?"Đang hoạt động":"Ngoại tuyến"));name.setText(user.optString("displayName"));});}catch(Exception e){runOnUiThread(()->err.setText(e.getMessage()));}}).start();}
 void save(){new Thread(()->{try{JSONObject out=new JSONObject();out.put("displayName",name.getText().toString());JSONObject r=Api.patch("/api/me",out);if(r.has("user"))Api.cache("user_json",r.getJSONObject("user").toString());runOnUiThread(()->Toast.makeText(this,"Đã lưu",Toast.LENGTH_SHORT).show());}catch(Exception e){runOnUiThread(()->err.setText(e.getMessage()));}}).start();}
 void logout(){new Thread(()->{try{Api.post("/api/logout",new JSONObject());}catch(Exception ignored){}Api.clearSession();runOnUiThread(()->{Intent i=new Intent(this,LoginActivity.class);i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);startActivity(i);finish();});}).start();}
}
