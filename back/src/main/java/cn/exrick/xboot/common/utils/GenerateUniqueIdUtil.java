package cn.exrick.xboot.common.utils;

import java.text.SimpleDateFormat;

public class GenerateUniqueIdUtil
{
    /**
     * 20位末尾的数字id
     */
    private static volatile int Guid = 100;

    /**
     * <获取唯一id>
     *
     * @return 结果
     * @throws
     */
    public static String getGuid()
    {
        GenerateUniqueIdUtil.Guid += 1;

        long now = System.currentTimeMillis();
        //获取4位年份数字
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
        //获取时间戳
        String time = dateFormat.format(now);
        String info = now + "";
        //获取三位随机数
        //int ran=(int) ((Math.random()*9+1)*100);
        //要是一段时间内的数据量过大会有重复的情况，所以做以下修改
        int ran = 0;
        if (GenerateUniqueIdUtil.Guid > 999)
        {
            GenerateUniqueIdUtil.Guid = 100;
        }
        ran = GenerateUniqueIdUtil.Guid;

        return time + info.substring(2, info.length()) + ran;
    }
}
