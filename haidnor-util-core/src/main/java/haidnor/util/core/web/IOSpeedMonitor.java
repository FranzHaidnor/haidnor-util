package haidnor.util.core.web;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.concurrent.TimeUnit;

/**
 * 下载速度监视器
 */
public class IOSpeedMonitor {

    /**
     * 下载总量, 需要在下载任务中增加该值
     */
    private BigDecimal totalSize = new BigDecimal(BigInteger.ZERO);

    /**
     * 下载速度 KB/s
     */
    private BigDecimal speed = new BigDecimal(BigInteger.ZERO);

    /**
     * 是否打印日志
     */
    private boolean log = false;

    public IOSpeedMonitor(boolean log) {
        this.log = log;
        start();
    }

    public IOSpeedMonitor() {
        start();
    }

    /**
     * 获取下载速度
     *
     * @return KB/S
     */
    public long getSpeed() {
        return speed.longValue();
    }

    /**
     * 获取下载总量
     *
     * @return M
     */
    public long getTotalSize() {
        return totalSize.longValue();
    }

    private void start() {
        Thread thread = new Thread(() -> {
            // 上一次的下载总量
            BigDecimal lastSize = new BigDecimal(BigInteger.ZERO);
            while (true) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 下载速度 = 当前总量 - 上一次的下载总量
                speed = totalSize.subtract(lastSize).divide(new BigDecimal(1024), 2, RoundingMode.DOWN);
                lastSize = totalSize;
                if (log) {
                    System.out.printf("下载速度: %s KB/s 总下载量: %s MB%n", speed, totalSize.divide(new BigDecimal(1024 * 1024), 2, RoundingMode.DOWN));
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * 增加字节数
     *
     * @param byteLength 字节数
     */
    public synchronized void addByte(Integer byteLength) {
        this.totalSize = totalSize.add(new BigDecimal(byteLength));
    }

}