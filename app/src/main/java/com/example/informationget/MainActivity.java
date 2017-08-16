package com.example.informationget;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.informationget.db.Information;
import com.example.informationget.util.HttpUtil;
import com.example.informationget.util.InfoItemAdapter;
import com.example.informationget.util.Util;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerlayout;
    private String TAG = "gtest";

    private List<String> dataList = new ArrayList<>();
    private List<Information> infoList;
    private InfoItemAdapter adapter;

    private SwipeRefreshLayout swipeRefresh;
    private String address;
    private RecyclerView recyclerView;
    private String currentType;
    private ProgressDialog progressDialog;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Android");
        setSupportActionBar(toolbar);

        mContext =this;

        swipeRefresh = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshInfo();
            }
        });
        mDrawerlayout = (DrawerLayout)findViewById(R.id.drawer_layout);
/*
        final TextView titleText = (TextView)findViewById(R.id.title_info);
        Button navButton = (Button) findViewById(R.id.nav_button);


        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerlayout.openDrawer(GravityCompat.START);
            }
        });

*/

        ActionBar actionBar = getSupportActionBar();
        if (actionBar!= null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        final NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_android);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.nav_android:
                        //showProgressDialog();
                        address = "http://gank.io/api/data/Android/20/1";
                        currentType = "Android";
                        break;

                    case R.id.nav_ios:
                        address = "http://gank.io/api/data/iOS/20/1";
                        currentType = "iOS";
                        break;

                    case R.id.nav_web:
                        address = "http://gank.io/api/data/%E5%89%8D%E7%AB%AF/20/1";
                        currentType = "前端";
                        break;

                    case R.id.nav_app:
                        address = "http://gank.io/api/data/App/20/1";
                        currentType = "Android";
                        break;

                    case R.id.nav_fun:
                        address = "http://gank.io/api/data/%E7%9E%8E%E6%8E%A8%E8%8D%90/20/1";
                        currentType = "瞎推荐";
                        break;

                    case R.id.nav_others:
                        address = "http://gank.io/api/data/%E6%8B%93%E5%B1%95%E8%B5%84%E6%BA%90/20/1";
                        currentType = "拓展资源";
                        break;

                    default:
                        break;

                }
                toolbar.setTitle(currentType);
                //titleText.setText(currentType);
                swipeRefresh.setRefreshing(true);
                refreshInfo();
                mDrawerlayout.closeDrawers();


                return true;
            }
        });

        initInfo();
        Log.d(TAG, "onCreate: currentType"+currentType);
        toolbar.setTitle(currentType);
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new InfoItemAdapter(infoList);
        recyclerView.setAdapter(adapter);

    }

    private void initInfo(){
        address = "http://gank.io/api/data/Android/20/1";
        currentType = "Android";

        infoList = DataSupport.where("type = ?", currentType).find(Information.class);

        if (infoList.size()>0){
            dataList.clear();

            for (Information information:infoList){
                Log.d(TAG, "initInfo: information.getDesc() is " + information.getDesc());
                dataList.add(information.getDesc());
            }

            //adapter.notifyDataSetChanged();


        }else {

            queryFromServer(address,currentType);
        }



    }

    public void queryInfo(String address,String type) {

        infoList = DataSupport.where("type = ?", type).find(Information.class);

        if (infoList.size() > 0) {
            dataList.clear();

            for (Information information : infoList) {

                dataList.add(information.getDesc());

            }

            adapter = new InfoItemAdapter(infoList);
            recyclerView.setAdapter(adapter);

        }else {
            queryFromServer(address,type);
        }

    }
    public void queryFromServer(final String address, final String type){

        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(MainActivity.this,"load fail",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String responseText = response.body().string();
                boolean result;

                result = Util.handleGankApiResponse(responseText);
                Log.d(TAG, "onResponse: result is " + result);
                if (result){
                    queryInfo(address,type);
                }

            }
        });

    }

    private void refreshInfo(){
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "run: address is " +address);
                        queryInfo(address,currentType);
                        //adapter.notifyDataSetChanged();
                        swipeRefresh.setRefreshing(false);

                    }
                });
            }
        }).start();
    }

    public boolean onCreateOptionsMenu(Menu menu){
        //getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }

    private void showProgressDialog(){
        if (progressDialog == null){
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("loading...");
            progressDialog.setCanceledOnTouchOutside(false);
        }

        progressDialog.show();

    }

    private void closeProgressDialog(){
        if (progressDialog != null){
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        closeProgressDialog();
        Log.d(TAG, "onResume: ");
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case android.R.id.home:
                mDrawerlayout.openDrawer(GravityCompat.START);
                break;


            default:
                break;

        }
        return true;
    }
}
