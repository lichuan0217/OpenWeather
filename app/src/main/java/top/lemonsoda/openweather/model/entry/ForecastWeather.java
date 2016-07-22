package top.lemonsoda.openweather.model.entry;

import java.util.List;

/**
 * Created by chuanl on 7/19/16.
 */
public class ForecastWeather {

    /**
     * cnt : 3
     * list : [ {
     *     "dt":1468832400,
     *     "main": {
     *         "temp":293.52,
     *         "temp_min":293.519,
     *         "temp_max":293.52,
     *     },
     *     "weather": [ {
     *         "id":800,
     *         "main":"Clear",
     *         "description":"clear sky",
     *         "icon":"01d" } ],
     *     "dt_txt":"2016-07-18 09:00:00" }
     *     ]
     */

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


    public static class ListBean {
        private int dt;
        private MainBean main;
        private String dt_txt;
        private List<WeatherBean> weather;

        public int getDt() {
            return dt;
        }

        public void setDt(int dt) {
            this.dt = dt;
        }

        public MainBean getMain() {
            return main;
        }

        public void setMain(MainBean main) {
            this.main = main;
        }

        public String getDt_txt() {
            return dt_txt;
        }

        public void setDt_txt(String dt_txt) {
            this.dt_txt = dt_txt;
        }

        public List<WeatherBean> getWeather() {
            return weather;
        }

        public void setWeather(List<WeatherBean> weather) {
            this.weather = weather;
        }

        public static class MainBean {
            private double temp;
            private double temp_min;
            private double temp_max;

            public double getTemp() {
                return temp;
            }

            public void setTemp(double temp) {
                this.temp = temp;
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
