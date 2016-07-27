package top.lemonsoda.openweather.model.entry;

import java.util.List;

/**
 * Created by chuanl on 7/19/16.
 */
public class ForecastWeather {
    /**
     * city : {"id":1816670,"name":"Beijing","coord":{"lon":116.397232,"lat":39.907501},"country":"CN","population":0}
     * cod : 200
     * message : 0.0113
     * cnt : 3
     * list : [{"dt":1469160000,"temp":{"day":30.9,"min":27.05,"max":32.46,"night":27.05,"eve":32.1,"morn":30.9},"pressure":990.26,"humidity":85,"weather":[{"id":800,"main":"Clear","description":"clear sky","icon":"02d"}],"speed":1.76,"deg":192,"clouds":8},{"dt":1469246400,"temp":{"day":31.11,"min":25.56,"max":33.98,"night":27.15,"eve":33.61,"morn":25.56},"pressure":991.84,"humidity":91,"weather":[{"id":500,"main":"Rain","description":"light rain","icon":"10d"}],"speed":1.6,"deg":97,"clouds":32},{"dt":1469332800,"temp":{"day":31.14,"min":24.65,"max":33.44,"night":24.65,"eve":30.82,"morn":25.31},"pressure":995.22,"humidity":89,"weather":[{"id":501,"main":"Rain","description":"moderate rain","icon":"10d"}],"speed":1.57,"deg":95,"clouds":44,"rain":11.15}]
     */

    private int cnt;
    /**
     * dt : 1469160000
     * temp : {"day":30.9,"min":27.05,"max":32.46,"night":27.05,"eve":32.1,"morn":30.9}
     * pressure : 990.26
     * humidity : 85
     * weather : [{"id":800,"main":"Clear","description":"clear sky","icon":"02d"}]
     * speed : 1.76
     * deg : 192
     * clouds : 8
     */

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

    public static class ListBean {
        private int dt;
        /**
         * day : 30.9
         * min : 27.05
         * max : 32.46
         * night : 27.05
         * eve : 32.1
         * morn : 30.9
         */

        private TempBean temp;
        private double pressure;
        private int humidity;
        private double speed;
        private int deg;
        private int clouds;
        /**
         * id : 800
         * main : Clear
         * description : clear sky
         * icon : 02d
         */

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

        public int getDeg() {
            return deg;
        }

        public void setDeg(int deg) {
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

        public static class TempBean {
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

}
