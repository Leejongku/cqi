package kr.dku.univ.mcqi.tcqi.domain;

import java.util.List;

/**
 * saveLcqiSect.do 요청 바디용 DTO.
 * <ul>
 *   <li>TcqiSearch 구성용: yy, orgid, pfltId (UNIV.MCQI_MASTER 키에 대응). JSP에서 파라미터로 넘어오거나 이 wrapper로 전달.</li>
 *   <li>sectList: UNIV.MCQI_MASTER 기준 영역답변 목록. JSP(lcqiContDscForm.jsp)에서 _collectList()로 수집 후 전송.</li>
 * </ul>
 * @RequestBody List&lt;McqiSectDsc&gt;만 사용 시 제네릭 소거로 LinkedHashMap 역직렬화 문제가 있어 래퍼 사용.
 */
public class McqiSectListWrapper {

    /** 기준년도 (TcqiSearch.yy, MCQI_MASTER 키) */
    private String yy;

    /** 조직코드 (TcqiSearch.orgid/collCd, 없으면 서버에서 UserDetails/기본값으로 보정) */
    private String orgid;

    /** 사번 (클라이언트 전달용, 필요 시 서버에서 활용) */
    private String empid;

    /** 교번 = intgUid (UserDetails.getIntgUid(), 담당자 교번) */
    private String pfltId;

    /** 교양구분 코드 = 화면 드롭다운 값 (SELF, COMM, PROB 등, MCQI_MASTER 키) */
    private String detMngtDscCd;

    /** 영역답변 목록 (JSP _collectList() → saveLcqiSect.do sectList) */
    private List<McqiSectDsc> sectList;

    public String getYy() {
        return yy;
    }

    public void setYy(String yy) {
        this.yy = yy;
    }

    public String getOrgid() {
        return orgid;
    }

    public void setOrgid(String orgid) {
        this.orgid = orgid;
    }

    public String getEmpid() {
        return empid;
    }

    public void setEmpid(String empid) {
        this.empid = empid;
    }

    public String getPfltId() {
        return pfltId;
    }

    public void setPfltId(String pfltId) {
        this.pfltId = pfltId;
    }

    public String getDetMngtDscCd() {
        return detMngtDscCd;
    }

    public void setDetMngtDscCd(String detMngtDscCd) {
        this.detMngtDscCd = detMngtDscCd;
    }

    public List<McqiSectDsc> getSectList() {
        return sectList;
    }

    public void setSectList(List<McqiSectDsc> sectList) {
        this.sectList = sectList;
    }
}
