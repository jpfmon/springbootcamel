package com.montojo;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class WeatherDataProvider {


    Map<String, WeatherDTO> map = new HashMap<>();

    {
        System.out.println("New WeatherDataProvider created");
        map.put("madrid", WeatherDTO.builder().id(1).city("Madrid").temp("20").build());
    }
    public WeatherDTO retrieveWeather(String city){

        if (map.containsKey(city.toLowerCase())){
            System.out.println("City is Madrid: " + map.get(city));
            return map.get(city);
        } else {
            return null;
        }

//        comment in feature branch
    }

    public void saveModifyCity(WeatherDTO dto){
        if (map.containsKey(dto.getCity().toLowerCase())){
            System.out.println("Contains " + dto.getCity());
            map.get(dto.getCity().toLowerCase()).setTemp(dto.getTemp());
            dto.setId(map.get(dto.getCity().toLowerCase()).getId());
            System.out.println("Temperature in map is: " + map.get(dto.getCity().toLowerCase()).getTemp());

            WeatherDTO.decreaseCounter();
        } else {
            map.put(dto.getCity().toLowerCase(), dto);
        }
    }
}
