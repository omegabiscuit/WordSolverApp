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
    static Oxford oxford;
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

    /*
        static void init() {
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request request = chain.request().newBuilder()
                                    .addHeader("app_id", "7ddb00d8")
                                    .addHeader("app_key", "9b38e8c1685a64d7b05dfb07c0666559")
                                    .build();
                            return chain.proceed(request);
                        }
                    })
                    .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .build();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://od-api.oxforddictionaries.com/api/v1/")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            oxford = retrofit.create(Oxford.class);
        }
    */
    static String solve(ArrayList<String> mypossibilities) {
        String solution = "";
        String possiblesolution = "";
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
        /*
        anagramica.findBest(string).enqueue(new Callback<WordResponse>() {
            @Override
            public void onResponse(Call<WordResponse> call, retrofit2.Response<WordResponse> response) {
                ArrayList<String> mypossibilities = new ArrayList<>(response.body().getBest());
                String solution = "";
                String possiblesolution = "";
                int solvalue = 0;
                int possiblesolvalue = 0;
                for (String word : mypossibilities) {
                    possiblesolvalue = Calculator(word);
                    if (possiblesolvalue > solvalue) {
                        solvalue = possiblesolvalue;
                        solution = word;
                    }
                }
            }

        @Override
        public void onFailure (Call < WordResponse > call, Throwable t){
            solution = "No Words Found";
        }
    }

    );
    return solution;
    */

        /*
        ArrayList<ArrayList<String>> myPossibilities = new ArrayList<>();//array of permutations seperated in characters
        ArrayList<String> myHand = LetterList(string);//get list of letters from user
        myPossibilities.add(myHand);
        Permutation(myHand, myPossibilities, 0);//find every permutation of input letters
        final ArrayList<String> myFinalHand = Merger(myPossibilities); //convert words that were broken down to characters to an array of strings

        return Single.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                int currentvalue = 0;
                String currentword = "";
                ArrayList<String> validwords = new ArrayList<String>();
                for (String word : myFinalHand) {
                    retrofit2.Response<WordResponse> response = oxford.lookupword(word).execute();
                    if (response.isSuccessful()) {
                        int tempvalue = Calculator(word);
                        if(tempvalue > currentvalue){
                            currentvalue = tempvalue;
                            currentword = word;
                        }
                    }
                }
                return currentword;
            }
        });
        // ArrayList myDictionary = Dictionary();//get words for text file
        //String Solution = DeScramble(myFinalHand, myDictionary);//find matches between user input and text file, and output highest value word

        //ArrayList Solution = DictionaryAPI(myPossibilities, 0);
        /*
        if(Solution.isEmpty()){
            return "No word can be created";
        }
        else{
            return Solution + " " + Calculator(Solution);
            //System.out.println("The highest value word you can make is:" + Solution + " " + "with a value of " + Calculator(Solution));
        }
        */
    //return Solution;


    static ArrayList<String> LetterList(String string) {
        ArrayList<String> Hand = new ArrayList<>();
        Scanner scan = new Scanner(System.in);

        //System.out.println("Enter all of the desired letter. Press ENTER when finished.");
        // String s = scan.nextLine();

        string = string.replaceAll("\\d+", ""); //remove numbers
        string = string.toLowerCase(); //convert all to lowercase
        String[] splitStrings = string.split("(?!^)");
        for (int i = 0; i < splitStrings.length; i++) {
            if (splitStrings[i].equals(",") || splitStrings[i].equals(" ")) { //remove objects other than letters
            } else {
                Hand.add(splitStrings[i]);
            }
        }
        return Hand;
    }

    static void Permutation(ArrayList hand, ArrayList possibilities, int start) {
        for (int i = 0; i < hand.size() - 1; i++) {
            ArrayList tempArray = new ArrayList(hand);
            Object tempValue = tempArray.get(start);
            tempArray.set(start, tempArray.get(i));
            tempArray.set(i, tempValue);
            if (possibilities.indexOf(tempArray) == -1) {
                possibilities.add(tempArray);
            }
            if (start < hand.size() - 1) {
                Permutation(tempArray, possibilities, start + 1);
            }
        }
    }

    static ArrayList<String> Merger(ArrayList<ArrayList<String>> list) {
        ArrayList<String> mergedHand = new ArrayList<>(); //array of every permutation in seperate strings
        for (int i = 0; i < list.size(); i++) {
            ArrayList tempArray = (ArrayList) list.get(i);
            String tempString = "";
            for (int j = 0; j < tempArray.size(); j++) {
                tempString += tempArray.get(j);
            }
            mergedHand.add(tempString);
        }
        return mergedHand;
    }

    public static Single<String> getValidWords(final ArrayList<String> list) {
        return Single.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                int currentvalue = 0;
                String currentword = "";
                ArrayList<String> validwords = new ArrayList<String>();
                for (String word : list) {
                    retrofit2.Response<WordResponse> response = oxford.lookupword(word).execute();
                    if (response.isSuccessful()) {
                        //check if word is better than current answer
                    }
                }
                return currentword;
            }
        });
    }

    /*
    ArrayList Dictionary(){ //function to test input with WordList.txt
        ScrabbleHelper context = ScrabbleHelper.this;
        InputStream is = context.getResources().openRawResource(R.raw.wordlist);

        ArrayList Dictionary = new ArrayList();
        //File WordList = new File("WordList.txt");
        String[] splitStrings;

        Scanner line = new Scanner(is);

        while (line.hasNextLine()) {
            String i = line.nextLine();
            splitStrings = i.split("");
            Dictionary.add(i);
        }
        line.close();
        return Dictionary;
    }
*/
    static int Calculator(String word) {
        int value = 0;
        String[] splitstring = word.split("");
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

/*
    static String DeScramble(ArrayList list, ArrayList list2) {
        String PossibleAnswer = "";
        String TrueAnswer = "";
        ArrayList CheckedLetters = new ArrayList();
        int TrueValue = 0;
        for (int i = 0; i < list.size(); i++) {
            String word = (String) list.get(i);
            if (list2.contains(word)) {
                if (Calculator(word) > TrueValue) {
                    TrueAnswer = word;
                    TrueValue = Calculator(word);
                }
            }

        }
        return TrueAnswer;
    }
}
*/