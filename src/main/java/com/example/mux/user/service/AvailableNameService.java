package com.example.mux.user.service;

import lombok.AllArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class AvailableNameService {
    public static ArrayList<Pair<String, String>> availableNames = new ArrayList<>();

    public static Pair<String, String> getAvailableName() {
        if (!availableNames.isEmpty()) {
            return availableNames.remove(availableNames.size()-1);
        } else {
            System.out.println("WARNING: Using default UserName");
            return Pair.of("schneller", "Löwe");
        }
    }

    public static void generateAvailableNames(List<String> adjectives, List<String> nouns) {
        for (String adjective : adjectives){
            for(String noun : nouns) {
                availableNames.add(Pair.of(adjective, noun));
            }
        }
        Collections.shuffle(availableNames);
    }

    public static int getNumberOfAvailableNames(){
        return availableNames.size();
    }
}
