package kr.dku.univ.mcqi.tcqi.domain;

import kr.dku.base.domain.Domain;

public class McqiSectDsc extends Domain {

    private static final long serialVersionUID = 1L;

    private String yy;
    private String orgid;
    private String pfltId;
    private String cqiDivCd;

    private String cqiSectCd;
    private String cqiTypeCd;
    private String choiCd;

    private String ansTypeCd;
    private String cont;
    private String sortOrd;

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

    public String getCqiSectCd() { return cqiSectCd; }
    public void setCqiSectCd(String cqiSectCd) { this.cqiSectCd = cqiSectCd; }

    public String getCqiTypeCd() { return cqiTypeCd; }
    public void setCqiTypeCd(String cqiTypeCd) { this.cqiTypeCd = cqiTypeCd; }

    public String getChoiCd() { return choiCd; }
    public void setChoiCd(String choiCd) { this.choiCd = choiCd; }

    public String getAnsTypeCd() { return ansTypeCd; }
    public void setAnsTypeCd(String ansTypeCd) { this.ansTypeCd = ansTypeCd; }

    public String getCont() { return cont; }
    public void setCont(String cont) { this.cont = cont; }

    public String getSortOrd() { return sortOrd; }
    public void setSortOrd(String sortOrd) { this.sortOrd = sortOrd; }

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