package com.inhataxi.response;

import com.google.gson.annotations.SerializedName;
import com.inhataxi.model.TestData;

import java.util.ArrayList;


public class TestListResponse extends SuperResponse {
    @SerializedName("result")
    private ArrayList<TestData> result;
}
