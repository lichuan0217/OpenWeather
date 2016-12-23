package top.lemonsoda.openweather.view.ui.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.OnClick;
import top.lemonsoda.openweather.R;

public class PlaceActivity extends BaseActivity {

    @BindView(R.id.rv_place)
    RecyclerView rvPlaceList;

    @OnClick(R.id.fab_add_place)
    void onAddPlaceClick(View view) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        layoutResID = R.layout.activity_place;
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);

        rvPlaceList.setLayoutManager(new LinearLayoutManager(this));
    }


}
