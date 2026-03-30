package kr.dku.univ.mcqi.tcqi.service;

import java.util.List;

import kr.dku.base.util.CamelCaseMap;
import kr.dku.univ.mcqi.tcqi.domain.*;
import kr.dku.univ.mcqi.tcqi.web.TcqiSearch;

/**
 * @author 이종구
 * @version 1.0
 * <pre>
 * 수정일        수정자        수정내용
 * ---------------------------------------------------------------------
 * 2024-06-10  이종구        최초작성
 * </pre>
 */
public interface TcqiService {
    /**
     * 교육과정기본을(를) 조회한다.
     * @param opOrgid 개설조직ID
     * @param yy 년도
     * @param semCd 학기코드
     * @param deptLoctCd 공통코드
     * @param mjOrgid 전공조직ID
     * @param subjId 교과목ID
     * @param bssTkcrsSchgr 기준수강학년
     */
    OpCuriBas findOpCuriBas(String opOrgid, String yy, String semCd, String deptLoctCd, String mjOrgid, String subjId, int bssTkcrsSchgr);


    List<CamelCaseMap> findCont(TcqiSearch tcqiSearch);


    /**
     * 교육과정기본을(를) 저장한다.
     * @param opCuriBas 교육과정기본정보
     */
    void insertOpCuriBas(OpCuriBas opCuriBas);

    /**
     * 교육과정기본을(를) 수정한다.
     * @param opCuriBas 교육과정기본정보
     */
    void updateOpCuriBas(OpCuriBas opCuriBas);

    /**
     * 교육과정기본을(를) 삭제한다.
     * @param opOrgid 개설조직ID
     * @param yy 년도
     * @param semCd 학기코드
     * @param deptLoctCd 공통코드
     * @param mjOrgid 전공조직ID
     * @param subjId 교과목ID
     * @param bssTkcrsSchgr 기준수강학년
     */
    void deleteOpCuriBas(String opOrgid, String yy, String semCd, String deptLoctCd, String mjOrgid, String subjId, int bssTkcrsSchgr);

    /**
     * 학부인지 아닌지 체크한다.
     * @param ttmgSearch
     * @return
     */
    List<CamelCaseMap> preCheck(TcqiSearch tcqiSearch);




    List<CamelCaseMap> findCqiOrzgDscList(TcqiSearch tcqiSearch);

    /**
     *  저장한다.
     * @param yy 년도
     */
    void saveCqiOrzgDsc(List<TcqiOrgzDsc> tcqiOrgzDsc);



    void saveCallCqiProc(TcqiSearch tcqiSearch);


    /**
     * CQI 작성내역을 조회한다.
     * @param yy 년도
     * @param collCd 대학코드
     * @param dpmtCd 단과대학코드
     * @param mjCd 전공ID
     * @param orgid 조직ID
     * @param deptLoctCd 캠퍼스
     */
    List<CamelCaseMap> findMcqiContDscList(TcqiSearch tcqiSearch);


    void saveMcqiContDsc(McqiContDsc mcqiContDsc);


    void saveMcqiContSubmit(McqiContDsc mcqiContDsc);


    void saveMcqiContSubmitCnl(McqiContDsc mcqiContDsc);

    List<CamelCaseMap> findMcqiPfltAscDsc(TcqiSearch tcqiSearch);

    void saveMcqiPfltAscDsc(List<McqiPfltAscDsc>  mcqiPfltAscDsc);

    /**
     * CQI 마스터 저장 (신규/수정)
     */
    void saveMcqiMaster(McqiMaster mcqiMaster);

    /**
     * CQI 제출 처리
     */
    void submitMcqi(McqiMaster mcqiMaster);

    /**
     * CQI 제출 취소
     */
    void submitCancelMcqi(McqiMaster mcqiMaster);

    /**
     * CQI 마감 처리
     */
    void closeMcqi(McqiMaster mcqiMaster);
    /**
     * CQI 역량 목록 조회
     */
    List<McqiCompDsc> findMcqiCompList(McqiCompDsc mcqiCompDsc);

    /**
     * CQI 역량 저장 (DELETE + INSERT)
     */
    void saveMcqiCompList(List<McqiCompDsc> mcqiCompList);
    /**
     * CQI 영역 답변 조회
     */
    List<McqiSectDsc> findMcqiSectList(McqiSectDsc mcqiSectDsc);

    /**
     * CQI 영역 답변 저장 (TYPE 단위 DELETE + INSERT)
     */
    void saveMcqiSectList(List<McqiSectDsc> mcqiSectList);
    /**
     * CQI 관리자 집계 조회 (Pivot)
     */
//    List<CamelCaseMap> findMcqiSectPivot(TcqiSearch tcqiSearch);
    /* ============================================================
       교양 Macro-CQI 서비스 메서드
    ============================================================ */

    /** 교양 MCQI_MASTER 상태 조회 */
    CamelCaseMap findLcqiMaster(TcqiSearch tcqiSearch);

    /** 교양 영역답변 현재연도 조회 (TYPE=01,02,03) */
    List<CamelCaseMap> findLcqiSectList(TcqiSearch tcqiSearch);

    /** 전년도 교양 영역답변 이월 조회 (SQL 내부 yy-1) */
    List<CamelCaseMap> findLcqiPrevSectList(TcqiSearch tcqiSearch);

    /**
     * 교양 CQI 영역답변 저장
     * TYPE='01'(전년도 개선계획) 저장 금지 - 서버 레벨 필터링
     */
    void saveLcqiSectList(TcqiSearch tcqiSearch, List<McqiSectDsc> sectList);

    /** 전공(CQI_DIV_CD=0001) MCQI_MASTER 상태 조회 */
    CamelCaseMap findMcqiMasterMap(TcqiSearch tcqiSearch);
    /** 전공 영역답변 현재연도 전체 조회 */
    List<CamelCaseMap> findMcqiSectListAll(TcqiSearch tcqiSearch);
    /** 전공 전년도 영역답변 이월 조회 */
    List<CamelCaseMap> findMcqiPrevSectList(TcqiSearch tcqiSearch);
    /** 전공 CQI 영역답변 저장 (TYPE=0001 저장 금지) */
    void saveMcqiSectList(TcqiSearch tcqiSearch, List<McqiSectDsc> sectList);
}