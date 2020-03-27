import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;
import org.apache.commons.net.ntp.TimeStamp;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Main {

    private static List<Long> offsetList = new ArrayList<>();

    public static void main(String[] args) {
        String[] serverList = {"ntp.ntsc.ac.cn",
                "ntp1.aliyun.com",
                "ntp2.aliyun.com",
                "time3.aliyun.com",
                "time4.aliyun.com",
                "time5.aliyun.com",
                "time6.aliyun.com",
                "time7.aliyun.com",
                "time1.cloud.tencent.com",
                "time2.cloud.tencent.com",
                "time3.cloud.tencent.com",
                "time4.cloud.tencent.com"};
        System.out.println("时间差为本地时间-远程服务器时间");
        Arrays.stream(serverList)
                .forEach(Main::check);

        offsetList.stream().reduce(Long::sum).ifPresent(aLong -> {
            System.out.println("平均值為：" + aLong / offsetList.size());
        });
    }

    private static void check(String server) {
        try {
            NTPUDPClient timeClient = new NTPUDPClient();
            timeClient.setDefaultTimeout(5000);
            InetAddress timeServerAddress = InetAddress.getByName(server);
            TimeInfo timeInfo = timeClient.getTime(timeServerAddress);
            TimeStamp timeStamp = timeInfo.getMessage().getTransmitTimeStamp();
            long local = System.currentTimeMillis();
            long offset = local - timeStamp.getTime();
            offsetList.add(offset);
            System.out.println(server + "|时间差：" + offset);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
