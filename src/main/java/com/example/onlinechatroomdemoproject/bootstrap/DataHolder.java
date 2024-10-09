package com.example.onlinechatroomdemoproject.bootstrap;

import com.example.onlinechatroomdemoproject.model.User;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DataHolder {

    public static List<User> users;
    public static Map<String, Double> conversionMap;


    @PostConstruct
    public void init(){
        users = new ArrayList<>();
        conversionMap = new HashMap<>();

        users.add(new User("vikikapceva@gmail.com", "vk", "12", "077832502"));
        users.add(new User("ana.gjurchinova@students.finki.ukim.mk", "ag", "12", "077832502"));

        conversionMap.put("USD", 1.00);
        conversionMap.put("EUR", 0.91);
        conversionMap.put("CAD", 1.33);
        conversionMap.put("JPY", 141.00);
        conversionMap.put("GBP", 0.77);
    }

}
