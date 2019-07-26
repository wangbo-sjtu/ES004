package com.example.weatherpaper;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.joaquimley.faboptions.FabOptions;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import interfaces.heweather.com.interfacesmodule.bean.Code;
import interfaces.heweather.com.interfacesmodule.bean.basic.Basic;
import interfaces.heweather.com.interfacesmodule.bean.weather.forecast.Forecast;
import interfaces.heweather.com.interfacesmodule.bean.weather.forecast.ForecastBase;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.Now;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.NowBase;
import interfaces.heweather.com.interfacesmodule.view.HeWeather;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Weather";
    private static final int BING_WALLPAPER = 1;
    private TextView tv_tmp;
    private ImageView iv_main;
    private OkHttpClient client = new OkHttpClient.Builder().build();
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == BING_WALLPAPER) {
                Glide.with(getApplicationContext()).load((String) msg.obj).into(iv_main);
            }

        }
    };
    private TextView tv_location;
    private TextView tv_condition;
    private RecyclerView recyclerView;
    private ForecastAdapter adapter;
    private List<ForecastBase> forecasts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); //隐藏状态栏

        setContentView(R.layout.activity_main);

        AndPermission.with(this)
                .runtime()
                .permission(Permission.Group.LOCATION)
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        Toast.makeText(MainActivity.this, "没有GPS权限，无法获取天气信息！", Toast.LENGTH_SHORT).show();
                    }
                })
                .start();

        tv_tmp = findViewById(R.id.tv_tmp);
        tv_location = findViewById(R.id.tv_location);
        tv_condition = findViewById(R.id.tv_cond);
        iv_main = findViewById(R.id.iv_main);
        FabOptions fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);
        recyclerView = findViewById(R.id.recylerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(MainActivity.this, DividerItemDecoration.VERTICAL));
        adapter = new ForecastAdapter();
        recyclerView.setAdapter(adapter);
        getBingWallpaper();

    }

    @Override
    protected void onResume() {
        super.onResume();
        getWeatherForecast();
        getWeatherNow();
    }

    private void getWeatherNow() {
        HeWeather.getWeatherNow(MainActivity.this, new HeWeather.OnResultWeatherNowBeanListener() {
            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "Weather Now onError: ", e);
            }

            @Override
            public void onSuccess(Now dataObject) {
                //先判断返回的status是否正确，当status正确时获取数据，若status不正确，可查看status对应的Code值找到原因
                if (Code.OK.getCode().equalsIgnoreCase(dataObject.getStatus())) {
                    //此时返回数据
                    NowBase now = dataObject.getNow();
                    Basic basic = dataObject.getBasic();
                    tv_tmp.setText(now.getTmp());
                    tv_location.setText(String.format("%s-%s", basic.getParent_city(), basic.getLocation()));
                    tv_condition.setText(now.getCond_txt());
                } else {
                    //在此查看返回数据失败的原因
                    String status = dataObject.getStatus();
                    Code code = Code.toEnum(status);
                    Log.e(TAG, "failed code: " + code);
                }
            }
        });
    }

    private void getWeatherForecast() {
        HeWeather.getWeatherForecast(MainActivity.this, new HeWeather.OnResultWeatherForecastBeanListener() {
            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "Weather Now onError: ", e);
            }

            @Override
            public void onSuccess(Forecast forecast) {
                if (Code.OK.getCode().equalsIgnoreCase(forecast.getStatus())) {
                    forecasts = forecast.getDaily_forecast();
                    adapter.notifyDataSetChanged();
                } else {
                    //在此查看返回数据失败的原因
                    String status = forecast.getStatus();
                    Code code = Code.toEnum(status);
                    Log.e(TAG, "failed code: " + code);
                }
            }
        });
    }

    private void getBingWallpaper() {

        Request request = new Request.Builder()
                .url("https://www.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1")
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    JSONObject jobj = new JSONObject(response.body().string());
                    JSONObject target = jobj.getJSONArray("images").getJSONObject(0);
                    String url = target.getString("url");
                    url = url.replace("1920x1080", "1080x1920");
                    url = "https://www.bing.com" + url;
                    Message msg = new Message();
                    msg.what = BING_WALLPAPER;
                    msg.obj = url;
                    handler.sendMessage(msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.menu_refresh:
            {
                getWeatherNow();
                getWeatherForecast();
                adapter.notifyDataSetChanged();
            }
            break;
            case R.id.menu_insert:
            {

            }
            break;
        }
    }

    class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder> {

        @NonNull
        @Override
        public ForecastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_forecast,parent,false);
            return new ForecastViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ForecastViewHolder holder, int position) {
            holder.tv_tmp.setText(String.format(Locale.CHINA, "%s/%s℃", forecasts.get(position).getTmp_max(), forecasts.get(position).getTmp_min()));
            holder.tv_date_cond.setText(String.format(Locale.CHINA, "%s·%s", forecasts.get(position).getDate(), forecasts.get(position).getCond_txt_d()));
        }

        @Override
        public int getItemCount() {
            return forecasts.size();
        }

        class ForecastViewHolder extends RecyclerView.ViewHolder {
            TextView tv_date_cond;
            TextView tv_tmp;

            public ForecastViewHolder(@NonNull View itemView) {
                super(itemView);
                tv_date_cond = itemView.findViewById(R.id.tv_date_cond);
                tv_tmp = itemView.findViewById(R.id.tv_tmp);
            }
        }
    }
}
