package com.gsh.staggeredlinearlayoutdemo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private StaggeredLinearLayout staggeredLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        initLayout();
        initData();
    }

    private void initData() {
        float horizontalSpace = getResources().getDimension(R.dimen.pregnancy_wall_text_horizontal_space);
        staggeredLinearLayout.setTextHorizontalSpace((int) horizontalSpace);
        float verticalSpace = getResources().getDimension(R.dimen.pregnancy_wall_text_vertical_space);
        staggeredLinearLayout.setTextVerticalSpcae((int) verticalSpace);
        float textSize = getResources().getDimension(R.dimen.pregnancy_wall_text_size);
        staggeredLinearLayout.setTextSize((int) textSize);
        float textPaddingLeftAndRight = getResources().getDimension(R.dimen.pregnancy_wall_text_padding_left_or_right);
        staggeredLinearLayout.setTextPadding((int) textPaddingLeftAndRight, 0, (int) textPaddingLeftAndRight, 0);
        staggeredLinearLayout.setTextColor(getResources().getColor(R.color.color_f9));
        float textHeight = getResources().getDimension(R.dimen.pregnancy_wall_text_height);
        staggeredLinearLayout.setTextHeight((int) textHeight);
        staggeredLinearLayout.setChildList(getCacheLabels());
    }


    private List<StaggeredLinearLayout.ThinkLabelsEntity> getCacheLabels() {
        List<StaggeredLinearLayout.ThinkLabelsEntity> list = new ArrayList<>();
        String[] content = new String[]{"天生傲骨°怎能服输", "强求不来你不如放弃", "你的嘴欠缝", "笨小孩", "楼下贞子的妹妹", "蓦然回首你咋还没走", "今天你被整了吗", "宿命的偏执っ 抒写了谁的离歌", "不二小姐", "王妃范儿", "将心比心。OK？", "你要明白 自个心自个疼"};
        for (int i = 0; i < content.length; i++) {
            int id = (int) (Math.random() * Integer.MAX_VALUE);
            StaggeredLinearLayout.ThinkLabelsEntity thinklabel = new StaggeredLinearLayout.ThinkLabelsEntity(content[i], id);
            list.add(thinklabel);
        }
        return list;
    }

    private void initLayout() {
        staggeredLinearLayout = (StaggeredLinearLayout) findViewById(R.id.staggered_linear_layout);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
