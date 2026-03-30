package kr.dku.base.security.authentication;

import java.io.Serializable;
import java.util.List;

/**
 * 로그인 사용자에 대한 세션정보를 표현한다.
 *
 * @author 조용상
 * @version 1.0
 *
 * <pre>
 * 수정일                수정자         수정내용
 * ---------------------------------------------------------------------
 * </pre>
 */
public class UserDetails implements Serializable {
    private static final long serialVersionUID = 1L;

    private String intgUid;
    private String userNm;
    private String userEgNm;
    private String hrInstId;
    private String orgId;
    private String orgzNm;
    private String orgzAnm;
    private String stuSchgr;
    private String stuClCd;
    private String hofcStaCd;
    private String userTyCd;
    private String userTyNm;
    private int userTyCls;
    private int userPrivPoli;
    private String deptLoctCd;
    private String budgDeptLoctCd;
    private String acctInfoId;
    private String intlUserYn;
    private String sysUseYn;
    private String eml;
    private List<String> posnCdList;

    private String currYy;
    private String currSemCd;

    private String sexCd;
    private String bday;
    private String mpTelno;

    /**
     * @return 로그인 사용자 아이디
     */
    public String getIntgUid() {
        return intgUid;
    }

    /**
     * @param intgUid 로그인 사용자 아이디
     */
    public void setIntgUid(String intgUid) {
        this.intgUid = intgUid;
    }

    /**
     * @return 사용자 이름
     */
    public String getUserNm() {
        return userNm;
    }

    /**
     * @param userNm 사용자 이름
     */
    public void setUserNm(String userNm) {
        this.userNm = userNm;
    }

    /**
     * @param userEgNm 사용자 영문이름
     */
    public String getUserEgNm() {
        return userEgNm;
    }

    /**
     * @param userEgNm 사용자 영문이름
     */
    public void setUserEgNm(String userEgNm) {
        this.userEgNm = userEgNm;
    }

    /**
     * @return 인사조직아이디
     */
    public String getHrInstId() {
        return hrInstId;
    }

    /**
     * @param hrInstId 인사조직아이디
     */
    public void setHrInstId(String hrInstId) {
        this.hrInstId = hrInstId;
    }

    /**
     * @return 조직아이디
     */
    public String getOrgId() {
        return orgId;
    }

    /**
     * @param orgId 조직아이디
     */
    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getStuSchgr() {
        return stuSchgr;
    }

    public void setStuSchgr(String stuSchgr) {
        this.stuSchgr = stuSchgr;
    }

    public String getStuClCd() {
        return stuClCd;
    }

    public void setStuClCd(String stuClCd) {
        this.stuClCd = stuClCd;
    }

    public String getHofcStaCd() {
        return hofcStaCd;
    }

    public void setHofcStaCd(String hofcStaCd) {
        this.hofcStaCd = hofcStaCd;
    }

    public String getUserTyCd() {
        return userTyCd;
    }

    public void setUserTyCd(String userTyCd) {
        this.userTyCd = userTyCd;
    }

    public String getUserTyNm() {
        return userTyNm;
    }

    public void setUserTyNm(String userTyNm) {
        this.userTyNm = userTyNm;
    }

    public List<String> getPosnCdList() {
        return posnCdList;
    }

    public void setPosnCdList(List<String> posnCdList) {
        this.posnCdList = posnCdList;
    }

    /**
     * @return the orgzNm
     */
    public String getOrgzNm() {
        return orgzNm;
    }

    /**
     * @param orgzNm the orgzNm to set
     */
    public void setOrgzNm(String orgzNm) {
        this.orgzNm = orgzNm;
    }
    /**
     * @return the orgzNm
     */
    public String getOrgzAnm() {
        return orgzAnm;
    }

    /**
     * @param orgzNm the orgzNm to set
     */
    public void setOrgzAnm(String orgzAnm) {
        this.orgzAnm = orgzAnm;
    }
    /**
     * @return the orgzNm
     */
    public String getDeptLoctCd() {
        return deptLoctCd;
    }

    /**
     * @param orgzNm the orgzNm to set
     */
    public void setDeptLoctCd(String deptLoctCd) {
        this.deptLoctCd = deptLoctCd;
    }

    /**
     * @param 예산구매 부서위치
     */
    public String getBudgDeptLoctCd() {
        return budgDeptLoctCd;
    }

    /**
     * @param 예산구매 부서위치
     */
    public void setBudgDeptLoctCd(String budgDeptLoctCd) {
        this.budgDeptLoctCd = budgDeptLoctCd;
    }

    /**
     * @return the acctInfoId
     */
    public String getAcctInfoId() {
        return acctInfoId;
    }

    /**
     * @param acctInfoId the acctInfoId to set
     */
    public void setAcctInfoId(String acctInfoId) {
        this.acctInfoId = acctInfoId;
    }

    /**
     * @return the intlUserYn
     */
    public String getIntlUserYn() {
        return intlUserYn;
    }

    /**
     * @param intlUserYn the intlUserYn to set
     */
    public void setIntlUserYn(String intlUserYn) {
        this.intlUserYn = intlUserYn;
    }

    /**
     * @return the loginYn
     */
    public String getSysUseYn() {
        return sysUseYn;
    }

    /**
     * @param loginYn the loginYn to set
     */
    public void setSysUseYn(String sysUseYn) {
        this.sysUseYn = sysUseYn;
    }

    /**
     * 사용자가 학생그룹인지, 교직원그룹인지 판단하여 웹정보 로그인시 메인화면에 강의시간표 혹은 수업시간표 표기
     * @return userTyCls
     */
    public int getUserTyCls() {
        return userTyCls;
    }

    /**
     * 사용자가 학생그룹인지, 교직원그룹인지 판단하여 웹정보 로그인시 메인화면에 강의시간표 혹은 수업시간표 표기
     * @param userTyCls 사용자유형그룹
     */
    public void setUserTyCls(int userTyCls) {
        this.userTyCls = userTyCls;
    }

    /**
     * 사용자가 개인정보동의대상자(1)인지 혹은 소속/학년 무시 개인정보 동의대상자(2)
     * @return userPrivPoli
     */
    public int getUserPrivPoli() {
        return userPrivPoli;
    }

    /**
     * 사용자가 개인정보동의대상자(1)인지 혹은 소속/학년 무시 개인정보 동의대상자(2)
     * @param userPrivPoli
     */
    public void setUserPrivPoli(int userPrivPoli) {
        this.userPrivPoli = userPrivPoli;
    }

    /**
     * 이메일
     * @return eml
     */
    public String getEml() {
        return eml;
    }

    /**
     * 이메일
     * @param eml
     */
    public void setEml(String eml) {
        this.eml = eml;
    }

    public String getCurrYy() {
        return currYy;
    }
    public void setCurrYy(String currYy) {
        this.currYy = currYy;
    }
    public String getCurrSemCd() {
        return currSemCd;
    }
    public void setCurrSemCd(String currSemCd) {
        this.currSemCd = currSemCd;
    }

    public String getSexCd() {
        return sexCd;
    }

    public void setSexCd(String sexCd) {
        this.sexCd = sexCd;
    }

    public String getBday() {
        return bday;
    }

    public void setBday(String bday) {
        this.bday = bday;
    }

    public String getMpTelno() {
        return mpTelno;
    }

    public void setMpTelno(String mpTelno) {
        this.mpTelno = mpTelno;
    }

}