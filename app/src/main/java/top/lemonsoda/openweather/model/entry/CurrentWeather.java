package top.lemonsoda.openweather.model.entry;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by chuanl on 7/19/16.
 */
public class CurrentWeather implements Parcelable {

    private MainBean main;
    private WindBean wind;
    private CloudsBean clouds;
    private int dt;
    private String name;
    private List<WeatherBean> weather;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public MainBean getMain() {
        return main;
    }

    public void setMain(MainBean main) {
        this.main = main;
    }

    public WindBean getWind() {
        return wind;
    }

    public void setWind(WindBean wind) {
        this.wind = wind;
    }

    public CloudsBean getClouds() {
        return clouds;
    }

    public void setClouds(CloudsBean clouds) {
        this.clouds = clouds;
    }

    public int getDt() {
        return dt;
    }

    public void setDt(int dt) {
        this.dt = dt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<WeatherBean> getWeather() {
        return weather;
    }

    public void setWeather(List<WeatherBean> weather) {
        this.weather = weather;
    }


    public static class MainBean implements Parcelable {
        private double temp;
        private double pressure;
        private int humidity;
        private double temp_min;
        private double temp_max;

        public double getTemp() {
            return temp;
        }

        public void setTemp(double temp) {
            this.temp = temp;
        }

        public double getPressure() {
            return pressure;
        }

        public void setPressure(double pressure) {
            this.pressure = pressure;
        }

        public int getHumidity() {
            return humidity;
        }

        public void setHumidity(int humidity) {
            this.humidity = humidity;
        }

        public double getTemp_min() {
            return temp_min;
        }

        public void setTemp_min(double temp_min) {
            this.temp_min = temp_min;
        }

        public double getTemp_max() {
            return temp_max;
        }

        public void setTemp_max(double temp_max) {
            this.temp_max = temp_max;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeDouble(this.temp);
            dest.writeDouble(this.pressure);
            dest.writeInt(this.humidity);
            dest.writeDouble(this.temp_min);
            dest.writeDouble(this.temp_max);
        }

        public MainBean() {
        }

        protected MainBean(Parcel in) {
            this.temp = in.readDouble();
            this.pressure = in.readDouble();
            this.humidity = in.readInt();
            this.temp_min = in.readDouble();
            this.temp_max = in.readDouble();
        }

        public static final Parcelable.Creator<MainBean> CREATOR = new Parcelable.Creator<MainBean>() {
            @Override
            public MainBean createFromParcel(Parcel source) {
                return new MainBean(source);
            }

            @Override
            public MainBean[] newArray(int size) {
                return new MainBean[size];
            }
        };
    }

    public static class WindBean implements Parcelable {
        private double speed;
        private double deg;

        public double getSpeed() {
            return speed;
        }

        public void setSpeed(double speed) {
            this.speed = speed;
        }

        public double getDeg() {
            return deg;
        }

        public void setDeg(double deg) {
            this.deg = deg;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeDouble(this.speed);
            dest.writeDouble(this.deg);
        }

        public WindBean() {
        }

        protected WindBean(Parcel in) {
            this.speed = in.readDouble();
            this.deg = in.readDouble();
        }

        public static final Parcelable.Creator<WindBean> CREATOR = new Parcelable.Creator<WindBean>() {
            @Override
            public WindBean createFromParcel(Parcel source) {
                return new WindBean(source);
            }

            @Override
            public WindBean[] newArray(int size) {
                return new WindBean[size];
            }
        };
    }

    public static class CloudsBean implements Parcelable {
        private int all;

        public int getAll() {
            return all;
        }

        public void setAll(int all) {
            this.all = all;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.all);
        }

        public CloudsBean() {
        }

        protected CloudsBean(Parcel in) {
            this.all = in.readInt();
        }

        public static final Parcelable.Creator<CloudsBean> CREATOR = new Parcelable.Creator<CloudsBean>() {
            @Override
            public CloudsBean createFromParcel(Parcel source) {
                return new CloudsBean(source);
            }

            @Override
            public CloudsBean[] newArray(int size) {
                return new CloudsBean[size];
            }
        };
    }

    public static class WeatherBean implements Parcelable {
        private int id;
        private String main;
        private String description;
        private String icon;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getMain() {
            return main;
        }

        public void setMain(String main) {
            this.main = main;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeString(this.main);
            dest.writeString(this.description);
            dest.writeString(this.icon);
        }

        public WeatherBean() {
        }

        protected WeatherBean(Parcel in) {
            this.id = in.readInt();
            this.main = in.readString();
            this.description = in.readString();
            this.icon = in.readString();
        }

        public static final Parcelable.Creator<WeatherBean> CREATOR = new Parcelable.Creator<WeatherBean>() {
            @Override
            public WeatherBean createFromParcel(Parcel source) {
                return new WeatherBean(source);
            }

            @Override
            public WeatherBean[] newArray(int size) {
                return new WeatherBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.main, flags);
        dest.writeParcelable(this.wind, flags);
        dest.writeParcelable(this.clouds, flags);
        dest.writeInt(this.dt);
        dest.writeString(this.name);
        dest.writeTypedList(this.weather);
        dest.writeInt(this.id);
    }

    public CurrentWeather() {
    }

    protected CurrentWeather(Parcel in) {
        this.main = in.readParcelable(MainBean.class.getClassLoader());
        this.wind = in.readParcelable(WindBean.class.getClassLoader());
        this.clouds = in.readParcelable(CloudsBean.class.getClassLoader());
        this.dt = in.readInt();
        this.name = in.readString();
        this.weather = in.createTypedArrayList(WeatherBean.CREATOR);
        this.id = in.readInt();
    }

    public static final Parcelable.Creator<CurrentWeather> CREATOR = new Parcelable.Creator<CurrentWeather>() {
        @Override
        public CurrentWeather createFromParcel(Parcel source) {
            return new CurrentWeather(source);
        }

        @Override
        public CurrentWeather[] newArray(int size) {
            return new CurrentWeather[size];
        }
    };
}
