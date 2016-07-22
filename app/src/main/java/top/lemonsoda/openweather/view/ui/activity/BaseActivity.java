package top.lemonsoda.openweather.view.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import top.lemonsoda.openweather.R;

/**
 * Created by chuanl on 7/19/16.
 */
public class BaseActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    protected int layoutResID = R.layout.activity_base;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutResID);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
    }
}
