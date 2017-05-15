package com.bidanet.bdcms.common;

/**
 * Created by xuejike on 2017/3/21.
 */
public class ConfigInfo
{
    protected String fileDomain;

    protected String fileDomainWei;

    protected String qrCodeAddress;

    public String getQrCodeAddress() {
        return qrCodeAddress;
    }

    public void setQrCodeAddress(String qrCodeAddress) {
        this.qrCodeAddress = qrCodeAddress;
    }

    public String getFileDomain() {
        return fileDomain;
    }

    public void setFileDomain(String fileDomain) {
        this.fileDomain = fileDomain;
    }

    public String getFileDomainWei() {
        return fileDomainWei;
    }

    public void setFileDomainWei(String fileDomainWei) {
        this.fileDomainWei = fileDomainWei;
    }
}
