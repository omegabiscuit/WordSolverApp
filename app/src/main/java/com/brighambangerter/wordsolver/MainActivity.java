package com.brighambangerter.wordsolver;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.view.View;
import android.content.Intent;

import java.util.ArrayList;

import okhttp3.OkHttpClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "WordSolver";
    Anagramica anagramica;
    //View root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        OkHttpClient client = new OkHttpClient.Builder().build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.anagramica.com/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        anagramica = retrofit.create(Anagramica.class);


    }


    public void sendMessage(View view) {
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();
        anagramica.findBest(message).enqueue(new Callback<WordResponse>() {
            @Override
            public void onResponse(Call<WordResponse> call, retrofit2.Response<WordResponse> response) {
                ArrayList<String> mypossibilities = new ArrayList<>(response.body().getBest());
                String bestword = ScrabbleHelper.solve(mypossibilities);
                Intent intent = new Intent(MainActivity.this, DisplayMessageActivity.class);
                intent.putExtra(EXTRA_MESSAGE, bestword + " , " + ScrabbleHelper.Calculator(bestword));
                startActivity(intent);
            }
            @Override
            public void onFailure(Call<WordResponse> call, Throwable t) {
                Intent intent = new Intent(MainActivity.this, DisplayMessageActivity.class);
                intent.putExtra(EXTRA_MESSAGE, "No Words Found");
                startActivity(intent);
            }
        });
    }
}


