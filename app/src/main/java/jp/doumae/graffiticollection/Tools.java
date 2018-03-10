package jp.doumae.graffiticollection;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by doumae.kazuki on 2018/03/06.
 */

public class Tools {
    // 日付取得
    public static String getNowData() {
        final DateFormat df = new SimpleDateFormat("yyy_mm_dd_HH_mm:ss");
        final Date date = new Date(System.currentTimeMillis());
        return df.format(date);
    }
}
