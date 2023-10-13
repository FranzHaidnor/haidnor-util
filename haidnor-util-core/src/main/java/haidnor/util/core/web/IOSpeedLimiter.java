package haidnor.util.core.web;

import java.util.concurrent.TimeUnit;

/**
 * 多线程限速器
 */
public class IOSpeedLimiter {

    /**
     * 单位时间 毫秒
     */
    private final long speedLimitTime = 200L;
    /**
     * 传输速度 1024 kb/s, 该参数需要设置
     */
    private long transferSpeed;
    /**
     * 总下载量
     */
    private long downLoadSize = 0L;

    /**
     * 限速结束后文件已下载大小,用于判断下一个单位时间内已下载文件大小
     */
    private long speedLastDownloadSize = 0L;

    /**
     * 下载开始时间，每次限速等待结束后重置为当前时间,用于判断开始时间和单位时间下载大小超出时间点之间的差值,进而判断等待时间
     */
    private long speedLimitLastTime = System.currentTimeMillis();

    /**
     * @param speed 下载速度 KB/S
     */
    public IOSpeedLimiter(long speed) {
        this.transferSpeed = speed * 1024 / 5;
    }

    /**
     * 设置下载速度
     *
     * @param speed 下载速度 KB/S
     */
    public void setTransferSpeed(long speed) {
        if (speed <= 0) {
            throw new IllegalArgumentException("下载速度不能小于等于 0 KB/S");
        }
        this.transferSpeed = speed * 1024 / 5;
    }

    /**
     * 限制速度
     *
     * @param length IO 流写入的字节数
     */
    public synchronized void braking(int length) {
        downLoadSize += length;
        if (downLoadSize - speedLastDownloadSize >= transferSpeed) {
            long interval = System.currentTimeMillis() - speedLimitLastTime;
            if (interval < speedLimitTime) {
                try {
                    TimeUnit.MILLISECONDS.sleep(speedLimitTime - interval);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            speedLastDownloadSize = downLoadSize;
            speedLimitLastTime = System.currentTimeMillis();
        }
    }

}
