package cn.exrick.xboot.modules.base.utils;

public class DayUtil {
    public static String nextDay(String nowDay){
        if(nowDay.length()>8){
            String[] split = nowDay.split("-");
            int day = Integer.parseInt(split[2]) + 1;
            int mouth = Integer.parseInt(split[1]);
            int year = Integer.parseInt(split[0]);
            return year + "-" + mouth + "-" + day;
        }
        return nowDay;
    }
}
