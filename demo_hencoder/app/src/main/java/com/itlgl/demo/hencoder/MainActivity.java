package com.itlgl.demo.hencoder;

import android.app.Activity;
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
import com.hencoder.hencoderpracticedraw7.Practice17Activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    Context context = this;

    List<Item> dataList = new ArrayList<Item>();

    {
        dataList.add(new Item("1-1 绘制基础", Practice11.class));
        dataList.add(new Item("1-2 paint详解", Practice12.class));
        dataList.add(new Item("1-3 文字的绘制", Practice13.class));
        dataList.add(new Item("1-4 Canvas 对绘制的辅助", Practice14Activity.class));
        dataList.add(new Item("1-5 自定义View绘制顺序", Practice15Activity.class));
        dataList.add(new Item("1-6 属性动画（上手篇）", Practice16Activity.class));
        dataList.add(new Item("1-7 属性动画（进阶篇）", Practice17Activity.class));
        dataList.add(new Item("1-8 仿即刻点赞效果", PracticeJikeActivity.class));

        Collections.reverse(dataList);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listview);
        ArrayAdapter<Item> adapter = new ArrayAdapter<Item>(
                this,
                android.R.layout.simple_expandable_list_item_1,
                dataList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new ListViewItemClickListener());
    }

    private class ListViewItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Item item = dataList.get(position);
            startActivity(new Intent(context, item.activityClass));
        }
    }

    private static class Item {
        String title;
        Class<? extends Activity> activityClass;

        public Item() {

        }

        public Item(String title, Class<? extends Activity> activityClass) {
            this.title = title;
            this.activityClass = activityClass;
        }

        // 重写toString方法以适配ArrayAdapter，显示名称
        @Override
        public String toString() {
            return title;
        }
    }
}
