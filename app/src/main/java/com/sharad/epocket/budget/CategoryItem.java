package com.sharad.epocket.budget;

/**
 * Created by Sharad on 12-Sep-15.
 */
public class CategoryItem {
    String _text;
    String _info;
    int _imgId;
    int _clrId;
    boolean _checked;

    public CategoryItem(String text, String info, int imgId, int clrId) {
        _text = text;
        _info = info;
        _imgId = imgId;
        _clrId = clrId;
        _checked = false;
    }

    public int get_imgId() {        return _imgId;    }
    public String get_info() {        return _info;    }
    public String get_text() {        return _text;    }
    public int get_clrId() {        return _clrId;    }
    public boolean is_checked() { return _checked; }
    public void set_checked(boolean _checked) {        this._checked = _checked;    }
}