package com.brighambangerter.wordsolver;

import android.content.Context;
import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.view.View;
import android.content.Intent;


import java.util.*;
import java.io.*;
import java.io.File;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity {

    public String Solver(String string) {



        ArrayList myHand = LetterList(string);//get list of letters from user
        ArrayList myDictionary = Dictionary();//get words for text file
        String Solution = DeScramble(myDictionary, myHand);//find matches between user input and text file, and output highest value word

        if(Solution.isEmpty()){
            return "No word can be created";
        }
        else{
            return Solution + " " + Calculator(Solution);
            //System.out.println("The highest value word you can make is:" + Solution + " " + "with a value of " + Calculator(Solution));
        }
    }


    public static ArrayList LetterList(String string){
        ArrayList Hand = new ArrayList();
        Scanner scan = new Scanner(System.in);

        //System.out.println("Enter all of the desired letter. Press ENTER when finished.");
       // String s = scan.nextLine();

        string = string.replaceAll("\\d+", ""); //remove numbers
        string = string.toLowerCase(); //convert all to lowercase
        String[] splitStrings = string.split("");
        for (int i = 0; i < splitStrings.length; i++) {
            if (splitStrings[i].equals(",") || splitStrings[i].equals(" ")) { //remove objects other than letters
            } else {
                Hand.add(splitStrings[i]);
            }
        }
        return Hand;
    }



    public ArrayList Dictionary(){
        Context context = this;
       InputStream is = context.getResources().openRawResource(R.raw.wordlist);

        ArrayList Dictionary = new ArrayList();
        //File WordList = new File("WordList.txt");
        String[] splitStrings;

        Scanner line = new Scanner(is);

        while (line.hasNextLine()) {
            String i = line.nextLine();
            splitStrings = i.split("");
            Dictionary.add(splitStrings);
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



    public static String DeScramble(ArrayList list, ArrayList list2){
        String PossibleAnswer="";
        String TrueAnswer = "";
        ArrayList CheckedLetters = new ArrayList();
        int TrueValue = 0;
        for (int i=0; i<list.size();i++){
            String[] word = (String []) list.get(i);
            for(int j=0; j< word.length;j++) {
                if (list2.contains(word[j])) {
                    list2.remove(word[j]);//remove so letter is not accounted for more than once
                    PossibleAnswer+=word[j];
                    CheckedLetters.add(word[j]);
                }
                else {
                    PossibleAnswer = "";
                    list2.addAll(CheckedLetters);
                    break;
                }
            }
            if (PossibleAnswer != ""){ //stop if an answer is found
                int PossibleValue = Calculator(PossibleAnswer);
                if(PossibleValue > TrueValue){
                    TrueValue = PossibleValue;
                    TrueAnswer = PossibleAnswer;
                }
                PossibleAnswer = "";
                list2.addAll(CheckedLetters);
                CheckedLetters.clear();
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


