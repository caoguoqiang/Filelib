package com.bidostar.videoedit.model;

/**
 * 定位位置信息
 * Created by smallc on 2017/4/11 18:32.
 */
public class LocationInfo {

    public int alarmFlag; // 报警标识
    public int blind = 1;// 是否盲区 0.否  1:是
    public int isLocation = 0;//是否定位 0 未定位 1 定位
    public int mode = 1;//默认基站定位   0GPS  1  基站定位
    public int latNs ;// 南北纬 0北纬  1 南纬
    public int lngEw; // 东西经 0东经 1西经
    public int lat =  0; // 纬度
    public int lng = 0;// 经度
    public int altitude = 0;// 高度
    public float speed = 0;//速度
    public int direction = 0;//角度
    public String time = "";//时间
    public int gpsNum  = -1;//GPS卫星个数
    public int gmsQuality;//GMS信号质量
    public int mileage = 0;//累计里程
    public String city;//城市名称

    @Override
    public String toString() {
        return "LocationInfo{" +
                "alarmFlag=" + alarmFlag +
                ", blind=" + blind +
                ", isLocation=" + isLocation +
                ", mode=" + mode +
                ", latNs=" + latNs +
                ", lngEw=" + lngEw +
                ", lat=" + lat +
                ", lng=" + lng +
                ", altitude=" + altitude +
                ", speed=" + speed +
                ", direction=" + direction +
                ", time='" + time + '\'' +
                ", gpsNum=" + gpsNum +
                ", gmsQuality=" + gmsQuality +
                ", mileage=" + mileage +
                ", city='" + city + '\'' +
                '}';
    }
}
