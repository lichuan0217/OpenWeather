package top.lemonsoda.openweather.model.entry;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by chuanl on 7/19/16.
 */
public class ForecastWeather implements Parcelable {

    private int cnt;

    private List<ListBean> list;

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean implements Parcelable {
        private int dt;

        private TempBean temp;
        private double pressure;
        private int humidity;
        private double speed;
        private double deg;
        private int clouds;
        private List<WeatherBean> weather;



        public int getDt() {
            return dt;
        }

        public void setDt(int dt) {
            this.dt = dt;
        }

        public TempBean getTemp() {
            return temp;
        }

        public void setTemp(TempBean temp) {
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

        public int getClouds() {
            return clouds;
        }

        public void setClouds(int clouds) {
            this.clouds = clouds;
        }

        public List<WeatherBean> getWeather() {
            return weather;
        }

        public void setWeather(List<WeatherBean> weather) {
            this.weather = weather;
        }



        public static class TempBean implements Parcelable {
            private double day;
            private double min;
            private double max;
            private double night;
            private double eve;
            private double morn;

            public double getDay() {
                return day;
            }

            public void setDay(double day) {
                this.day = day;
            }

            public double getMin() {
                return min;
            }

            public void setMin(double min) {
                this.min = min;
            }

            public double getMax() {
                return max;
            }

            public void setMax(double max) {
                this.max = max;
            }

            public double getNight() {
                return night;
            }

            public void setNight(double night) {
                this.night = night;
            }

            public double getEve() {
                return eve;
            }

            public void setEve(double eve) {
                this.eve = eve;
            }

            public double getMorn() {
                return morn;
            }

            public void setMorn(double morn) {
                this.morn = morn;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeDouble(this.day);
                dest.writeDouble(this.min);
                dest.writeDouble(this.max);
                dest.writeDouble(this.night);
                dest.writeDouble(this.eve);
                dest.writeDouble(this.morn);
            }

            public TempBean() {
            }

            protected TempBean(Parcel in) {
                this.day = in.readDouble();
                this.min = in.readDouble();
                this.max = in.readDouble();
                this.night = in.readDouble();
                this.eve = in.readDouble();
                this.morn = in.readDouble();
            }

            public static final Parcelable.Creator<TempBean> CREATOR = new Parcelable.Creator<TempBean>() {
                @Override
                public TempBean createFromParcel(Parcel source) {
                    return new TempBean(source);
                }

                @Override
                public TempBean[] newArray(int size) {
                    return new TempBean[size];
                }
            };
        }

        public static class WeatherBean implements Parcelable {
            private int id;
            private String main;
            private String description;
            private String icon;

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

            // getter ans setter
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
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.dt);
            dest.writeParcelable(this.temp, flags);
            dest.writeDouble(this.pressure);
            dest.writeInt(this.humidity);
            dest.writeDouble(this.speed);
            dest.writeDouble(this.deg);
            dest.writeInt(this.clouds);
            dest.writeTypedList(this.weather);
        }

        public ListBean() {
        }

        protected ListBean(Parcel in) {
            this.dt = in.readInt();
            this.temp = in.readParcelable(TempBean.class.getClassLoader());
            this.pressure = in.readDouble();
            this.humidity = in.readInt();
            this.speed = in.readDouble();
            this.deg = in.readDouble();
            this.clouds = in.readInt();
            this.weather = in.createTypedArrayList(WeatherBean.CREATOR);
        }

        public static final Parcelable.Creator<ListBean> CREATOR = new Parcelable.Creator<ListBean>() {
            @Override
            public ListBean createFromParcel(Parcel source) {
                return new ListBean(source);
            }

            @Override
            public ListBean[] newArray(int size) {
                return new ListBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.cnt);
        dest.writeTypedList(this.list);
    }

    public ForecastWeather() {
    }

    protected ForecastWeather(Parcel in) {
        this.cnt = in.readInt();
        this.list = in.createTypedArrayList(ListBean.CREATOR);
    }

    public static final Parcelable.Creator<ForecastWeather> CREATOR = new Parcelable.Creator<ForecastWeather>() {
        @Override
        public ForecastWeather createFromParcel(Parcel source) {
            return new ForecastWeather(source);
        }

        @Override
        public ForecastWeather[] newArray(int size) {
            return new ForecastWeather[size];
        }
    };
}
