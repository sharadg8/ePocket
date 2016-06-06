package com.sharad.budget;

import android.animation.ArgbEvaluator;
import android.graphics.Color;

import java.util.Random;

/**
 * Created by Sharad on 12-Sep-15.
 */
public class DetailItem {
    String _text;
    String _info;
    int _imgId;
    int _clrId;

    DetailItem(String text, String info, int imgId, int clrId) {
        _text = text;
        _info = info;
        _imgId = imgId;
        _clrId = clrId;
    }

    public int get_imgId() {        return _imgId;    }
    public String get_info() {        return _info;    }
    public String get_text() {        return _text;    }
    public int get_clrId() {        return _clrId;    }
    public int get_progress() {
        Random r = new Random();
        return r.nextInt(100);
    }
}