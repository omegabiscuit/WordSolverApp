package com.brighambangerter.wordsolver;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.view.View;
import android.content.Intent;


import java.io.*;


import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {
    ScrabbleHelper myscrabblehelper = new ScrabbleHelper();

    public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    Oxford oxford;
    View root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ScrabbleHelper.init();
        root = findViewById(R.id.root);

        /*oxford.lookupword("fish").enqueue(new Callback<WordResponse>() {
            @Override
            public void onResponse(Call<WordResponse> call, retrofit2.Response<WordResponse> response) {
                if (response.isSuccessful()) {
                    Snackbar.make(root, "word exists", Snackbar.LENGTH_SHORT).show();
                } else {
                    Snackbar.make(root, "nope", Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WordResponse> call, Throwable t) {//if person has no internet connection
                Snackbar.make(root, "failure", Snackbar.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });*/

    }

    /**
     * Called when the user clicks the Send button
     */


    public void sendMessage(View view) {
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();
        ScrabbleHelper.solve(message)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new SingleObserver<String>() {
            @Override
            public void onSubscribe(Disposable d) {
            }
            @Override
            public void onSuccess(String bestword) {
                Intent intent = new Intent(MainActivity.this, DisplayMessageActivity.class);
                intent.putExtra(EXTRA_MESSAGE, bestword);
                startActivity(intent);
                //Snackbar.make(root, "Best scoring word is " + bestword, Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable e) {
                Snackbar.make(root, "failure", Snackbar.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });

        //use solver function
    }
}


