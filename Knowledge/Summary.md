# Summary

## Tips

### How to make ScrollView fill the whole screen

```
    <ScrollView
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:fillViewport="true">
            
        <RelativeLayout
            android:layout_width="match_parent"
            android:layout_height="match_parent">
            <ImageView
                android:id="@+id/img_network_failed"
                android:layout_width="@dimen/overview_img_error_size"
                android:layout_height="@dimen/overview_img_error_size"
                android:layout_centerInParent="true"
                android:src="@drawable/ic_network_fail" />            
        </RelativeLayout>
    </ScrollView>
```

If you want the Image in the center of the screen, you need to set `android:fillViewport="true"` in your `ScrollView`.