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
import com.hencoder.hencoderpracticedraw3.Practice13;
import com.hencoder.hencoderpracticedraw4.Practice14Activity;
import com.hencoder.hencoderpracticedraw5.Practice15Activity;
import com.hencoder.hencoderpracticedraw6.Practice16Activity;

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
        list.add("1-1 绘制基础");
        list.add("1-2 paint详解");
        list.add("1-3 文字的绘制");
        list.add("1-4 Canvas 对绘制的辅助");
        list.add("1-5 自定义View绘制顺序");
        list.add("1-6 属性动画（上手篇）");
        return list;
    }

    private class ListViewItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (position) {
                case 0:
                    startActivity(new Intent(context, Practice11.class));
                    break;
                case 1:
                    startActivity(new Intent(context, Practice12.class));
                    break;
                case 2:
                    startActivity(new Intent(context, Practice13.class));
                    break;
                case 3:
                    startActivity(new Intent(context, Practice14Activity.class));
                    break;
                case 4:
                    startActivity(new Intent(context, Practice15Activity.class));
                    break;
                case 5:
                    startActivity(new Intent(context, Practice16Activity.class));
                    break;
            }
        }
    }
}
