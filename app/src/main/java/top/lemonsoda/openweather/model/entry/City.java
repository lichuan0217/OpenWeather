package top.lemonsoda.openweather.model.entry;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Chuan on 04/11/2016.
 */

public class City implements Parcelable {


    /**
     * _id : 2038349
     * country : CN
     * lat : 39.916908
     * lon : 116.397057
     * name : Beijing Shi
     */

    private int _id;
    private String country;
    private double lat;
    private double lon;
    private String name;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "City: " + getName() + ", " + getCountry();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this._id);
        dest.writeString(this.country);
        dest.writeDouble(this.lat);
        dest.writeDouble(this.lon);
        dest.writeString(this.name);
    }

    public City() {
    }

    protected City(Parcel in) {
        this._id = in.readInt();
        this.country = in.readString();
        this.lat = in.readDouble();
        this.lon = in.readDouble();
        this.name = in.readString();
    }

    public static final Parcelable.Creator<City> CREATOR = new Parcelable.Creator<City>() {
        @Override
        public City createFromParcel(Parcel source) {
            return new City(source);
        }

        @Override
        public City[] newArray(int size) {
            return new City[size];
        }
    };
}
