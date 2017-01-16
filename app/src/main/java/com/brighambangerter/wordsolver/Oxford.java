package com.brighambangerter.wordsolver;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

import static android.R.attr.path;

/**
 * Created by Brigham on 1/15/2017.
 */

public interface Oxford {
    @GET("entries/en/{word}")
    Call<WordResponse> lookupword(@Path("word")String word);

}
