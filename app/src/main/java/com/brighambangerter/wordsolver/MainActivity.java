package com.brighambangerter.wordsolver;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.view.View;
import android.content.Intent;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import java.util.*;
import java.io.*;


import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity {

    public String Solver(String string) {
        ArrayList myPossibilities = new ArrayList<>();//array of permutations seperated in characters
        ArrayList myFinalHand = new ArrayList();//array of permutations in seperated in strings
        ArrayList myHand = LetterList(string);//get list of letters from user
        myPossibilities.add(myHand);
        Permutation(myHand, myPossibilities, 0);//find every permutation of input letters
        myFinalHand = Merger(myPossibilities); //convert words that were broken down to characters to an array of strings
        ArrayList myDictionary = Dictionary();//get words for text file
        String Solution = DeScramble(myFinalHand, myDictionary);//find matches between user input and text file, and output highest value word

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
        return Solution;

    }


    public static ArrayList LetterList(String string){
        ArrayList Hand = new ArrayList();
        Scanner scan = new Scanner(System.in);

        //System.out.println("Enter all of the desired letter. Press ENTER when finished.");
       // String s = scan.nextLine();

        string = string.replaceAll("\\d+", ""); //remove numbers
        string = string.toLowerCase(); //convert all to lowercase
        String[] splitStrings = string.split("(?!^)");
        for (int i=0 ; i < splitStrings.length ; i++) {
            if (splitStrings[i].equals(",")|| splitStrings[i].equals(' ')) { //remove objects other than letters
            }
            else {
                Hand.add(splitStrings[i]);
            }
        }
        return Hand;
    }

    public static void Permutation(ArrayList hand, ArrayList possibilities, int start) {
        for (int i = 0; i < hand.size()-1; i++) {
            ArrayList tempArray = new ArrayList(hand);
            Object tempValue = tempArray.get(start);
            tempArray.set(start, tempArray.get(i));
            tempArray.set(i,tempValue);
            if(possibilities.indexOf(tempArray) == -1){
                possibilities.add(tempArray);
            }
            if(start <hand.size()-1){
                Permutation(tempArray, possibilities, start + 1);
            }
        }
    }

    public static ArrayList Merger(ArrayList list){
        ArrayList mergedHand = new ArrayList(); //array of every permutation in seperate strings
        for(int i=0; i < list.size(); i++){
            ArrayList tempArray = (ArrayList) list.get(i);
            String tempString = new String();
            for(int j=0; j < tempArray.size(); j++){
                tempString += tempArray.get(j);
            }
            mergedHand.add(tempString);
        }
        return mergedHand;
    }

/*
    public ArrayList DictionaryAPI(ArrayList hand, ArrayList possibilities, int iter) {
        for (int i = iter; i < hand.size(); i++) {
            ArrayList tempArray = hand;
            Object tempValue = tempArray.get(iter);
            tempArray.set(iter, tempArray.get(i + iter));
            tempArray.set(i + iter, tempValue);
            possibilities.add(tempArray);
            if (iter < hand.size() - 1) {
                DictionaryAPI(hand, possibilities, iter + 1);
            }
        }
        return possibilities;
    }
*/

/*
        Gson gson = new GsonBuilder().create();
        String id = null;
        id = gson.fromJson()


    }
*/

    public ArrayList Dictionary(){ //function to test input with WordList.txt
        Context context = this;
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

    public static int Calculator(String word){
        int value=0;
        String[] splitstring = word.split("");
        List<String> OnePoint = Arrays.asList("a","e","i","o","u","l","n","s","t","r");
        List<String> TwoPoint = Arrays.asList("d","g");
        List<String> ThreePoint = Arrays.asList("b","c","m","p");
        List<String> FourPoint = Arrays.asList("f","h","v","w","y");
        List<String> FivePoint = Arrays.asList("k");
        List<String> EightPoint = Arrays.asList("j","x");
        List<String> TenPoint = Arrays.asList("q","z");
        for (int i=0; i<splitstring.length; i++){
            if (OnePoint.contains(splitstring[i])){
                value+= 1;
            }
            else if (TwoPoint.contains(splitstring[i])){
                value+= 2;
            }
            else if (ThreePoint.contains(splitstring[i])){
                value+= 3;
            }
            else if (FourPoint.contains(splitstring[i])){
                value+= 4;
            }
            else if (FivePoint.contains(splitstring[i])){
                value+= 5;
            }
            else if (EightPoint.contains(splitstring[i])){
                value+= 8;
            }
            else if (TenPoint.contains(splitstring[i])){
                value+=10;
            }
        }
        return value;

    }

/*
    public static String DeScramble(ArrayList list, ArrayList list2){


        String PossibleAnswer="";
        String TrueAnswer = "";
        ArrayList CheckedLetters = new ArrayList();
        int TrueValue = 0;
        for(int i=0; i < list2.size();i++){
            //String[] word = (String []) list.get(i);
            PossibleAnswer="";
            if(list.indexOf(list2.get(i)) != -1){
                for(int j=0; j<list2.get(i).size();j++){
                    PossibleAnswer+=word[j];
                }
                if(Calculator(PossibleAnswer) > TrueValue){
                    TrueAnswer = PossibleAnswer;
                    TrueValue = Calculator(PossibleAnswer);
                }

            }
        }
        return TrueAnswer;
    }
*/

    public static String DeScramble(ArrayList list, ArrayList list2){

        Gson gson = new GsonBuilder().create();
        String id = null;


        String PossibleAnswer="";
        String TrueAnswer = "";
        ArrayList CheckedLetters = new ArrayList();
        int TrueValue = 0;
        for (int i=0; i<list.size();i++){
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

    public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**Called when the user clicks the Send button */
    public void sendMessage(View view){

        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();
        String mySolution = Solver(message);


        Intent intent = new Intent(this, DisplayMessageActivity.class);
        intent.putExtra(EXTRA_MESSAGE, mySolution);
        startActivity(intent);
        //use solver function
    }
}


