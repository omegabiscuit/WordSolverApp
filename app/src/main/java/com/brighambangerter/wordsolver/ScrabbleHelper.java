package com.brighambangerter.wordsolver;

import android.content.Context;
import android.support.design.widget.Snackbar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;

import io.reactivex.Single;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Brigham on 1/13/2017.
 */

public class ScrabbleHelper {
    static Anagramica anagramica;
    static void init() {
        OkHttpClient client = new OkHttpClient.Builder().build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.anagramica.com/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        anagramica = retrofit.create(Anagramica.class);
    }

    static String solve(ArrayList<String> mypossibilities) {
        String solution = "";
        int solvalue = 0;
        int possiblesolvalue = 0;
        for (String word : mypossibilities) {
            possiblesolvalue = Calculator(word);
            if (possiblesolvalue > solvalue) {
                solvalue = possiblesolvalue;
                solution = word;
            }
        }
        return solution;
    }

    static int Calculator(String word) {
        int value = 0;
        String[] splitstring = word.split("");
        if(splitstring.length <= 1) return 0;
        List<String> OnePoint = Arrays.asList("a", "e", "i", "o", "u", "l", "n", "s", "t", "r");
        List<String> TwoPoint = Arrays.asList("d", "g");
        List<String> ThreePoint = Arrays.asList("b", "c", "m", "p");
        List<String> FourPoint = Arrays.asList("f", "h", "v", "w", "y");
        List<String> FivePoint = Arrays.asList("k");
        List<String> EightPoint = Arrays.asList("j", "x");
        List<String> TenPoint = Arrays.asList("q", "z");
        for (int i = 0; i < splitstring.length; i++) {
            if (OnePoint.contains(splitstring[i])) {
                value += 1;
            } else if (TwoPoint.contains(splitstring[i])) {
                value += 2;
            } else if (ThreePoint.contains(splitstring[i])) {
                value += 3;
            } else if (FourPoint.contains(splitstring[i])) {
                value += 4;
            } else if (FivePoint.contains(splitstring[i])) {
                value += 5;
            } else if (EightPoint.contains(splitstring[i])) {
                value += 8;
            } else if (TenPoint.contains(splitstring[i])) {
                value += 10;
            }
        }
        return value;

    }
}