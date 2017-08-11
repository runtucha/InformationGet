package com.example.informationget;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.informationget.db.Information;
import com.example.informationget.util.HttpUtil;
import com.example.informationget.util.Util;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by glenn_cui on 17-8-11.
 */
public class ChooseAreaFragment extends Fragment{
    private TextView titleText;
    private ListView listView;
    private Button backButton;

    private ArrayAdapter<String> adapter;
    private List<String> dataList = new ArrayList<>();
    private Information selectedInformation;
    private List<Information> informationList;

    private String TAG = "gtest";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_area,container,false);
        titleText = (TextView)view.findViewById(R.id.title_text);
        backButton = (Button)view.findViewById(R.id.back_button);
        listView = (ListView)view.findViewById(R.id.list_view);

        adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,dataList);
        listView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.d(TAG, "onActivityCreated: ");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                selectedInformation = informationList.get(position);
                queryInformation();

            }
        });

        queryInformation();
    }

    public void queryInformation(){
        //titleText.setText("Information");
        backButton.setVisibility(View.GONE);
        informationList = DataSupport.findAll(Information.class);
        if (informationList.size()>0){
            dataList.clear();

            for (Information information:informationList){
                dataList.add(information.getDesc());
            }

            //adapter.notifyDataSetChanged();
            listView.setSelection(0);

        }else {
            String address = "http://gank.io/api/data/Android/20/1";
            Log.d(TAG, "queryInformation: 1");
            queryFromServer(address,"information");
        }
    }

    public void queryFromServer(String address,String information){
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(getContext(),"加载失败",Toast.LENGTH_SHORT).show();
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
                    queryInformation();
                }


            }



        });

    }
}
