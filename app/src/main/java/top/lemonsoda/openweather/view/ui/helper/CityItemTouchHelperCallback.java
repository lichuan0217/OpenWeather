package top.lemonsoda.openweather.view.ui.helper;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import java.util.List;

import top.lemonsoda.openweather.model.entry.City;

/**
 * Created by Chuan on 7/26/16.
 */
public class CityItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private final ItemTouchHelperAdapter adapter;
    private List<City> cityList;

    public CityItemTouchHelperCallback(ItemTouchHelperAdapter adapter, List<City> cityList) {
        this.adapter = adapter;
        this.cityList = cityList;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;

        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        adapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        adapter.onItemDismiss(viewHolder.getAdapterPosition());
    }

    @Override
    public boolean isLongPressDragEnabled() {
        if (cityList.size() > 1)
            return true;
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        if (cityList.size() > 1)
            return true;
        return false;
    }
}
