package top.lemonsoda.openweather.view.ui.helper;

/**
 * Created by Chuan on 7/26/16.
 */
public interface ItemTouchHelperAdapter {

    void onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);
}
