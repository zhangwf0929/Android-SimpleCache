package cn.zwf.simplecache.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.zwf.simplecache.CacheManager;


public class MainActivity extends AppCompatActivity {

    @Bind(R.id.et_set_key)
    EditText etSetKey;

    @Bind(R.id.et_set_value)
    EditText etSetValue;

    @Bind(R.id.et_get_key)
    EditText etGetKey;

    @Bind(R.id.et_get_value)
    EditText etGetValue;

    @Bind(R.id.et_del_key)
    EditText etDelKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        CacheManager.init(this);
        CacheManager.init(this, 4);
        CacheManager.setMaxRecordSize(100);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        CacheManager.getInstance().closeDB();
        super.onDestroy();
    }

    @OnClick(R.id.btn_set)
    void set() {
        String key = etSetKey.getText().toString().trim();
        String value = etSetValue.getText().toString().trim();
        if (TextUtils.isEmpty(key) || TextUtils.isEmpty(value)) {
            toast("please set key and value");
        } else {
            CacheManager.getInstance().setValue(key, value);
            toast("set successful");
        }
    }

    @OnClick(R.id.btn_get)
    void get() {
        String key = etGetKey.getText().toString().trim();
        if (TextUtils.isEmpty(key)) {
            toast("please set key");
        } else {
            String value = CacheManager.getInstance().getValue(key);
            if (TextUtils.isEmpty(value)) {
                toast("no value");
            } else {
                etGetValue.setText(value);
            }
        }
    }

    @OnClick(R.id.btn_del)
    void del() {
        String key = etDelKey.getText().toString().trim();
        if (TextUtils.isEmpty(key)) {
            toast("please set key");
        } else {
            int rows = CacheManager.getInstance().delValue(key);
            if (rows <= 0) {
                toast("no value");
            } else {
                toast("del successful");
            }
        }
    }

    @OnClick(R.id.btn_empty)
    void empty() {
        int rows = CacheManager.getInstance().empty();
        if (rows <= 0) {
            toast("no value");
        } else {
            toast(String.format("empty successful,del %d records", rows));
        }
    }

    @OnClick(R.id.btn_set_object)
    void setObject() {
        User user = new User(1, "user01");
        CacheManager.getInstance().setObject("object", user);
    }

    @OnClick(R.id.btn_get_object)
    void getObject() {
        User user = CacheManager.getInstance().getObject("object", User.class);
        if (user != null) {
            toast(user.name);
        } else {
            toast("nothing");
        }
    }

    @OnClick(R.id.btn_set_list)
    void setList() {
        List<User> users = new ArrayList<>();
        users.add(new User(1, "user01"));
        users.add(new User(2, "user02"));
        CacheManager.getInstance().setObject("list", users);
    }

    @OnClick(R.id.btn_get_list)
    void getList() {
        List<User> users = CacheManager.getInstance().getList("list", new TypeToken<List<User>>() {
        }.getType());
        if (users != null) {
            StringBuilder sb = new StringBuilder("users:");
            for (User user : users) {
                sb.append("\n").append(user.name);
            }
            toast(sb.toString());
        } else {
            toast("nothing");
        }
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
