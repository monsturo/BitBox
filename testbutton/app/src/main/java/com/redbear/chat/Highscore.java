package com.example.frederik.bitbox3;

import java.util.ArrayList;

/**
 * Created by Frederik on 21-05-2015.
 */
public class Highscore {
    public String username;
    public int highscore;
    public int id;

    public Highscore(String username, int highscore, int id) {
        this.username = username;
        this.highscore = highscore;
        this.id = id;
    }
    
}
