package kr.dku.univ.mcqi.tcqi.domain;

import kr.dku.base.domain.Domain;

public class McqiMaster extends Domain {

    private static final long serialVersionUID = 1L;

    private String yy;
    private String orgid;
    private String pfltId;
    private String cqiDivCd;

    private String submitYn;
    private String submitUid;
    private String submitDm;

    private String closeYn;
    private String closeUid;
    private String closeDm;

    private String inputUid;
    private String inputDm;
    private String inputIp;

    private String ltstUpdUid;
    private String ltstUpdDm;
    private String ltstUpdIp;

    public String getYy() { return yy; }
    public void setYy(String yy) { this.yy = yy; }

    public String getOrgid() { return orgid; }
    public void setOrgid(String orgid) { this.orgid = orgid; }

    public String getPfltId() { return pfltId; }
    public void setPfltId(String pfltId) { this.pfltId = pfltId; }

    public String getCqiDivCd() { return cqiDivCd; }
    public void setCqiDivCd(String cqiDivCd) { this.cqiDivCd = cqiDivCd; }

    public String getSubmitYn() { return submitYn; }
    public void setSubmitYn(String submitYn) { this.submitYn = submitYn; }

    public String getSubmitUid() { return submitUid; }
    public void setSubmitUid(String submitUid) { this.submitUid = submitUid; }

    public String getSubmitDm() { return submitDm; }
    public void setSubmitDm(String submitDm) { this.submitDm = submitDm; }

    public String getCloseYn() { return closeYn; }
    public void setCloseYn(String closeYn) { this.closeYn = closeYn; }

    public String getCloseUid() { return closeUid; }
    public void setCloseUid(String closeUid) { this.closeUid = closeUid; }

    public String getCloseDm() { return closeDm; }
    public void setCloseDm(String closeDm) { this.closeDm = closeDm; }

    public String getInputUid() { return inputUid; }
    public void setInputUid(String inputUid) { this.inputUid = inputUid; }

    public String getInputDm() { return inputDm; }
    public void setInputDm(String inputDm) { this.inputDm = inputDm; }

    public String getInputIp() { return inputIp; }
    public void setInputIp(String inputIp) { this.inputIp = inputIp; }

    public String getLtstUpdUid() { return ltstUpdUid; }
    public void setLtstUpdUid(String ltstUpdUid) { this.ltstUpdUid = ltstUpdUid; }

    public String getLtstUpdDm() { return ltstUpdDm; }
    public void setLtstUpdDm(String ltstUpdDm) { this.ltstUpdDm = ltstUpdDm; }

    public String getLtstUpdIp() { return ltstUpdIp; }
    public void setLtstUpdIp(String ltstUpdIp) { this.ltstUpdIp = ltstUpdIp; }
}