package com.dragon4.owo.resight_android.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Muscledata {

    @SerializedName("1")
    @Expose
    private Integer _1;
    @SerializedName("2")
    @Expose
    private Integer _2;
    @SerializedName("3")
    @Expose
    private Integer _3;
    @SerializedName("4")
    @Expose
    private Integer _4;
    @SerializedName("5")
    @Expose
    private Integer _5;
    @SerializedName("6")
    @Expose
    private Integer _6;

    /**
     * No args constructor for use in serialization
     *
     */
    public Muscledata() {
    }

    /**
     *
     * @param _3
     * @param _4
     * @param _5
     * @param _6
     * @param _1
     * @param _2
     */
    public Muscledata(Integer _1, Integer _2, Integer _3, Integer _4, Integer _5, Integer _6) {
        super();
        this._1 = _1;
        this._2 = _2;
        this._3 = _3;
        this._4 = _4;
        this._5 = _5;
        this._6 = _6;
    }



    public Integer get1() {
        return _1;
    }

    public void set1(Integer _1) {
        this._1 = _1;
    }

    public Integer get2() {
        return _2;
    }

    public void set2(Integer _2) {
        this._2 = _2;
    }

    public Integer get3() {
        return _3;
    }

    public void set3(Integer _3) {
        this._3 = _3;
    }

    public Integer get4() {
        return _4;
    }

    public void set4(Integer _4) {
        this._4 = _4;
    }

    public Integer get5() {
        return _5;
    }

    public void set5(Integer _5) {
        this._5 = _5;
    }

    public Integer get6() {
        return _6;
    }

    public void set6(Integer _6) {
        this._6 = _6;
    }

}