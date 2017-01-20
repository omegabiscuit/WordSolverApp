package com.brighambangerter.wordsolver;

/**
 * Created by Brigham on 1/20/2017.
 */

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface Anagramica {
    @GET("best/:{word}")
    Call<WordResponse> findBest(@Path("word")String word);
}
