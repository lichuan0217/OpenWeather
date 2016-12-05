package top.lemonsoda.openweather.view.ui.helper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import top.lemonsoda.openweather.R;
import top.lemonsoda.openweather.domain.utils.CitySharedPreference;
import top.lemonsoda.openweather.domain.utils.NotificationUtils;
import top.lemonsoda.openweather.domain.utils.Utils;
import top.lemonsoda.openweather.model.entry.City;

/**
 * Created by Chuan on 01/12/2016.
 */

public class CityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements ItemTouchHelperAdapter {

    private LayoutInflater inflater;
    private Context context;
    private List<City> cityList;
    private boolean dataChanged = false;

    public CityAdapter(Context cxt, List<City> cities) {
        this.cityList = cities;
        this.context = cxt;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_city_manage_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder viewHolder = (ItemViewHolder) holder;
        viewHolder.tvCityName.setText(cityList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(this.cityList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(this.cityList, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        CitySharedPreference.saveManagedCityList(context, cityList);
        dataChanged = true;
    }

    @Override
    public void onItemDismiss(int position) {
        City city = cityList.get(position);
        if (NotificationUtils.getInstance().getCityId() == city.get_id()) {
            NotificationUtils.getInstance().updateNotification(
                    context, cityList.get(0).get_id());
            Utils.updateNotificationLocation(context, cityList.get(0).get_id());
        }
        cityList.remove(position);
        notifyItemRemoved(position);
        CitySharedPreference.saveManagedCityList(context, cityList);
        dataChanged = true;
    }

    public void setDataChanged(boolean changed) {
        dataChanged = changed;
    }

    public boolean isDataChanged() {
        return dataChanged;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_layout_city_name)
        TextView tvCityName;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
