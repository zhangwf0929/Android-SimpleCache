package cn.zwf.simplecache.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

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

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
