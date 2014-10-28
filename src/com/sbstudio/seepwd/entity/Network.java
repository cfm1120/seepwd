package com.sbstudio.seepwd.entity;

/**
 * @author water3
 * 网络连接实体
 */
public class Network {

    private String ssid;
    private String psk;
    private String security;
    public String getSsid() {
        return ssid;
    }
    public void setSsid(String ssid) {
        this.ssid = ssid;
    }
    public String getPsk() {
        return psk;
    }
    public void setPsk(String psk) {
        this.psk = psk;
    }
    public String getSecurity() {
        return security;
    }
    public void setSecurity(String security) {
        this.security = security;
    }
    
    
    
}
