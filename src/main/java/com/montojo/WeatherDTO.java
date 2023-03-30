package com.montojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeatherDTO implements Serializable{

    static int counter = 1;
    private int id = counter++;
    private String city;
    private String temp;

    public static void decreaseCounter(){
        counter--;
    }

}
