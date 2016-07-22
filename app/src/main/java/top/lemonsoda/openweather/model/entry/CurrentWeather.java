package top.lemonsoda.openweather.model.entry;

import java.util.List;

/**
 * Created by chuanl on 7/19/16.
 */
public class CurrentWeather {

    /**
     * lon : -0.13
     * lat : 51.51
     */

    private CoordBean coord;
    /**
     * temp : 289.61
     * pressure : 1020
     * humidity : 82
     * temp_min : 287.04
     * temp_max : 292.15
     */

    private MainBean main;
    /**
     * speed : 2.6
     * deg : 100
     */

    private WindBean wind;
    /**
     * all : 0
     */

    private CloudsBean clouds;
    /**
     * coord : {"lon":-0.13,"lat":51.51}
     * weather : [{"id":800,"main":"Clear","description":"clear sky","icon":"01n"}]
     * base : cmc stations
     * main : {"temp":289.61,"pressure":1020,"humidity":82,"temp_min":287.04,"temp_max":292.15}
     * wind : {"speed":2.6,"deg":100}
     * clouds : {"all":0}
     * dt : 1468892582
     * sys : {"type":1,"id":5091,"message":0.0039,"country":"GB","sunrise":1468901186,"sunset":1468958788}
     * id : 2643743
     * name : London
     * cod : 200
     */

    private int dt;
    private String name;
    /**
     * id : 800
     * main : Clear
     * description : clear sky
     * icon : 01n
     */

    private List<WeatherBean> weather;

    public CoordBean getCoord() {
        return coord;
    }

    public void setCoord(CoordBean coord) {
        this.coord = coord;
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

    public static class CoordBean {
        private double lon;
        private double lat;

        public double getLon() {
            return lon;
        }

        public void setLon(double lon) {
            this.lon = lon;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }
    }

    public static class MainBean {
        private double temp;
        private int pressure;
        private int humidity;
        private double temp_min;
        private double temp_max;

        public double getTemp() {
            return temp;
        }

        public void setTemp(double temp) {
            this.temp = temp;
        }

        public int getPressure() {
            return pressure;
        }

        public void setPressure(int pressure) {
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
    }

    public static class WindBean {
        private double speed;
        private int deg;

        public double getSpeed() {
            return speed;
        }

        public void setSpeed(double speed) {
            this.speed = speed;
        }

        public int getDeg() {
            return deg;
        }

        public void setDeg(int deg) {
            this.deg = deg;
        }
    }

    public static class CloudsBean {
        private int all;

        public int getAll() {
            return all;
        }

        public void setAll(int all) {
            this.all = all;
        }
    }

    public static class WeatherBean {
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
    }
}
