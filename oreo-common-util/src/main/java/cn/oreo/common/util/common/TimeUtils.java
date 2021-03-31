package cn.oreo.common.util.common;

/**
 * 时间和整数相互转换的工具类
 *
 * @author XuMin
 */
public class TimeUtils {

    private final static String[] times = new String[] {
            "00:00", "00:30", "01:00", "01:30", "02:00", "02:30", "03:00", "03:30", "04:00", "04:30", "05:00", "05:30",
            "06:00", "06:30", "07:00", "07:30", "08:00", "08:30", "09:00", "09:30", "10:00", "10:30", "11:00", "11:30",
            "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30",
            "18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30", "22:00", "22:30", "23:00", "23:30"
    };

    /**
     * 将时间字符串转化为代表时间的整数
     * e.g. 00:00 -> 1
     *
     * @param time 时间字符串
     * @return 代表时间的整数
     */
    public static Integer timeToi(String time) {
        if (Integer.parseInt(time.substring(3)) == 0) {
            return Integer.parseInt(time.substring(0, 2)) * 2 + 1;
        } else {
            return Integer.parseInt(time.substring(0, 2)) * 2 + 2;
        }
    }

    /**
     * 将代表时间的整数转化为时间字符串
     * e.g. 1 -> 00:00
     *
     * @param i 代表时间的整数
     * @return 时间字符串
     */
    public static String iToTime(Integer i) {
        return times[i - 1];
    }

    public static void main(String[] args) {
        // test
        System.out.println(timeToi("02:30"));
        System.out.println(iToTime(48));
    }
}
