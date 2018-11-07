package com.itlgl.demo.hencoder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.itlgl.demo.jike.LikeCountView;

public class PracticeJikeActivity extends AppCompatActivity {

    LikeCountView likeCountView;
    EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_jike);

        likeCountView = (LikeCountView) findViewById(R.id.like_count_view);
        et = (EditText) findViewById(R.id.et);

        likeCountView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(likeCountView.getLikeCount() > 99) {
                    likeCountView.reduceLikeCountOne();
                } else {
                    likeCountView.addLikeCountOne();
                }
            }
        });

        findViewById(R.id.btn_set_count).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeCountView.setLikeCountWithAnim(Integer.parseInt(et.getText().toString()));
            }
        });
        findViewById(R.id.btn_set_count2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeCountView.setLikeCount(Integer.parseInt(et.getText().toString()));
            }
        });
        findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeCountView.addLikeCountOne();
            }
        });
        findViewById(R.id.btn_reduce).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeCountView.reduceLikeCountOne();
            }
        });
    }
}
