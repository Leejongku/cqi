package kr.dku.univ.mcqi.tcqi.domain;

import kr.dku.base.domain.Domain;

/**
 * MCQI_COMP_DSC 도메인
 * - 역량(D1~D10) 선택 여부를 저장하는 엔티티
 * - PK: YY, ORGID, PFLT_ID, CQI_DIV_CD, CQI_COMP_CD
 */
public class McqiCompDsc extends Domain {

    private static final long serialVersionUID = 1L;

    private String yy;
    private String orgid;
    private String pfltId;
    private String cqiDivCd;

    private String cqiCompCd;   // 역량 코드 (D3_CORE_ABLY_CD: 330,340,350,360,130,140,150,230,240,250)
    private String cqiCompYn;   // 선택 여부 (Y/N)

    public String getYy() { return yy; }
    public void setYy(String yy) { this.yy = yy; }

    public String getOrgid() { return orgid; }
    public void setOrgid(String orgid) { this.orgid = orgid; }

    public String getPfltId() { return pfltId; }
    public void setPfltId(String pfltId) { this.pfltId = pfltId; }

    public String getCqiDivCd() { return cqiDivCd; }
    public void setCqiDivCd(String cqiDivCd) { this.cqiDivCd = cqiDivCd; }

    public String getCqiCompCd() { return cqiCompCd; }
    public void setCqiCompCd(String cqiCompCd) { this.cqiCompCd = cqiCompCd; }

    public String getCqiCompYn() { return cqiCompYn; }
    public void setCqiCompYn(String cqiCompYn) { this.cqiCompYn = cqiCompYn; }
}