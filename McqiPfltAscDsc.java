package kr.dku.univ.mcqi.tcqi.domain;

import kr.dku.base.domain.Domain;

public class McqiPfltAscDsc extends Domain {

    private static final long serialVersionUID = 1725562519277736989L;

    private String yy;
    private String semCd;
    private String cqiDivCd;
    private String detMngtDscCd;
    private String pfltId;

    public String getPfltId() {
        return pfltId;
    }

    public void setPfltId(String pfltId) {
        this.pfltId = pfltId;
    }

    public String getSemCd() {
        return semCd;
    }

    public void setSemCd(String semCd) {
        this.semCd = semCd;
    }

    public String getCqiDivCd() {
        return cqiDivCd;
    }

    public void setCqiDivCd(String cqiDivCd) {
        this.cqiDivCd = cqiDivCd;
    }

    public String getDetMngtDscCd() {
        return detMngtDscCd;
    }

    public void setDetMngtDscCd(String detMngtDscCd) {
        this.detMngtDscCd = detMngtDscCd;
    }



    public String getYy() {
        return yy;
    }

    public void setYy(String yy) {
        this.yy = yy;
    }
}
