package com.inhataxi.response;

import com.google.gson.annotations.SerializedName;
import com.inhataxi.model.TestData;

public class TestDetailResponse extends SuperResponse {
    @SerializedName("result")
    private TestData result;
}
