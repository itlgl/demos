package com.itlgl.demo.hencoder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.hencoder.hencoderpracticedraw1.Practice11;
import com.hencoder.hencoderpracticedraw2.Practice12;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listview);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_expandable_list_item_1,
                getData());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new ListViewItemClickListener());
    }

    private List<String> getData() {
        List<String> list = new ArrayList<>();
        list.add("view1-1");
        list.add("view1-2");
        list.add("view1-3");
        return list;
    }

    private class ListViewItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch(position) {
                case 0:
                    startActivity(new Intent(context, Practice11.class));
                    break;
                    case 1:
                    startActivity(new Intent(context, Practice12.class));
                    break;
            }
        }
    }
}
