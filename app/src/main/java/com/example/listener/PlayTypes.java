package com.example.listener;

public interface PlayTypes {

    public static final int LOOP_ONE = 1;//单曲循环

    public static final int ORDER = 2;//顺序播放

    public static final int LOOP = 3;//列表循环

    public static final int RANDOM = 4;//随机播放

    public static final int[] TYPES = {LOOP_ONE,LOOP,ORDER,RANDOM};
}
