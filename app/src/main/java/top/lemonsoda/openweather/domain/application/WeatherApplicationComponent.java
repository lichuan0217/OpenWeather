package top.lemonsoda.openweather.domain.application;

import dagger.Component;
import top.lemonsoda.openweather.domain.net.WeatherService;
import top.lemonsoda.openweather.domain.net.WeatherServiceModule;
import top.lemonsoda.openweather.domain.utils.di.WeatherApplicationScope;

/**
 * Created by Chuan on 31/05/2017.
 */

@WeatherApplicationScope
@Component(modules = {WeatherServiceModule.class})
public interface WeatherApplicationComponent {

    WeatherService getWeatherService();
}
