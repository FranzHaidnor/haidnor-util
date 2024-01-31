package haidnor.util.core.web;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author wang xiang
 */
public class Downloader {

    /**
     * 代理 Hostname
     */
    private String proxyHostname;

    /**
     * 代理端口号
     */
    private Integer proxyPort;

    /**
     * 下载速度限制器
     */
    private IOSpeedLimiter speedLimiter;

    /**
     * 下载速度监视器
     */
    private IOSpeedMonitor speedMonitor;

    public Downloader() {
    }

    /**
     * 使用代理方式下载
     *
     * @param proxyHostname 代理 Hostname
     * @param proxyPort     代理端口号
     */
    public Downloader(String proxyHostname, Integer proxyPort) {
        this.proxyHostname = proxyHostname;
        this.proxyPort = proxyPort;
    }

    /**
     * 绑定下载速度限制器
     */
    public Downloader bindSpeedLimiter(IOSpeedLimiter speedLimiter) {
        this.speedLimiter = speedLimiter;
        return this;
    }

    /**
     * 绑定下载速度监视器
     */
    public Downloader bindSpeedMonitor(IOSpeedMonitor speedMonitor) {
        this.speedMonitor = speedMonitor;
        return this;
    }

    /**
     * 下载并写入文件
     *
     * @param httpURL 网络资源 URL
     * @param path    文件保存地址
     *                下载成功返回 true, 下载失败返回 false
     */
    public void save(String httpURL, String path) {
        try {
            URLConnection connection;
            if (proxyHostname != null & proxyPort != null) {
                Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHostname, proxyPort));
                connection = new URL(httpURL).openConnection(proxy);
            } else {
                connection = new URL(httpURL).openConnection();
            }
            InputStream inputStream = null;
            FileOutputStream fileOutputStream = null;
            try {
                inputStream = connection.getInputStream();

                fileOutputStream = new FileOutputStream(path);

                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    fileOutputStream.write(buffer, 0, length);
                    // 下载速度限制器
                    if (speedLimiter != null) {
                        speedLimiter.braking(length);
                    }
                    // 下载速度监视器
                    if (speedMonitor != null) {
                        speedMonitor.addByte(length);
                    }
                }
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            }
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

}
