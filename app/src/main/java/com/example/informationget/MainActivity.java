package com.example.informationget;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        swipeRefresh = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshInfo();
            }
        });
        /*
        FloatingActionButton fab =(FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this, "FAB clicked", Toast.LENGTH_SHORT).show();
                Snackbar.make(v,"Data deleted",Snackbar.LENGTH_SHORT).setAction(
                        "Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(MainActivity.this, "Data restored", Toast.LENGTH_SHORT).show();
                            }
                        }
                ).show();
            }
        });
        */

        mDrawerlayout = (DrawerLayout)findViewById(R.id.drawer_layout);

        /*
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!= null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }
*/
        NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_android);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.nav_android:
                        Log.d(TAG, "onNavigationItemSelected: nav_android");

                        break;

                    case R.id.nav_ios:

                        break;

                    case R.id.nav_web:

                        break;

                    case R.id.nav_app:

                        break;

                    case R.id.nav_fun:

                        break;

                    case R.id.nav_others:

                        break;

                    default:
                        break;

                }

                mDrawerlayout.closeDrawers();
                return true;
            }
        });

        initInfo();
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new InfoItemAdapter(infoList);
        recyclerView.setAdapter(adapter);

    }

    private void initInfo(){

        infoList = DataSupport.findAll(Information.class);
        if (infoList.size()>0){
            dataList.clear();

            for (Information information:infoList){
                dataList.add(information.getDesc());
            }

            //adapter.notifyDataSetChanged();


        }else {
            String address = "http://gank.io/api/data/Android/20/1";

            queryFromServer(address,"information");
        }

    }

    public void queryFromServer(String address,String information){

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
                    initInfo();
                }

            }
        });

    }

    private void refreshInfo(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initInfo();
                        adapter.notifyDataSetChanged();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*
        switch(item.getItemId()){
            case android.R.id.home:
                mDrawerlayout.openDrawer(GravityCompat.START);
                break;

            case R.id.backup:
                Toast.makeText(this,"you clicked backup",Toast.LENGTH_SHORT
                ).show();
                break;

            case R.id.delete:
                Toast.makeText(this,"you clicked delete",Toast.LENGTH_SHORT
                ).show();
                break;

            case R.id.settings:
                Toast.makeText(this,"you clicked settings",Toast.LENGTH_SHORT
                ).show();
                break;

            default:
                break;

        }*/
        return true;
    }
}
