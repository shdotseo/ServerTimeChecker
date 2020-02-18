package io.animal.meerkat.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeFormatHelper {

    private final static String HH_mm_ss = "HH:mm:ss";
    private final static String hh_mm_ss = "hh:mm:ss";

    private SimpleDateFormat _formatter;
    private SimpleDateFormat _formatter12;
    private boolean is24Hour = true;

    public String toDate(long mills) {
        SimpleDateFormat formatter = getSimpleDateFormat();
        String date = formatter.format(new Date(mills));
        return date;
    }

    public String toDate12(long mills) {
        SimpleDateFormat formatter = getSimpleDateFormat12();
        String date = formatter.format(new Date(mills));
        return date;
    }

    private SimpleDateFormat getSimpleDateFormat() {
        if (_formatter == null) {
            _formatter = new SimpleDateFormat(HH_mm_ss);
        }

        return _formatter;
    }

    private SimpleDateFormat getSimpleDateFormat12() {
        if (_formatter12 == null) {
            _formatter12 = new SimpleDateFormat(hh_mm_ss);
        }

        return _formatter12;
    }
}
