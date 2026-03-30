package kr.dku.univ.mcqi.tcqi.repository;

import java.util.List;
import kr.dku.base.util.CamelCaseMap;
import kr.dku.univ.mcqi.tcqi.domain.McqiContDsc;
import kr.dku.univ.mcqi.tcqi.domain.OpCuriBas;
import kr.dku.univ.mcqi.tcqi.domain.TcqiOrgzDsc;
import kr.dku.univ.mcqi.tcqi.domain.McqiPfltAscDsc;
import kr.dku.univ.mcqi.tcqi.web.TcqiSearch;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import kr.dku.univ.mcqi.tcqi.domain.McqiMaster;
import kr.dku.univ.mcqi.tcqi.domain.McqiCompDsc;
import kr.dku.univ.mcqi.tcqi.domain.McqiSectDsc;
/**
 * @author 이종구
 * @version 1.0
 * <pre>
 * 수정일        수정자        수정내용
 * ---------------------------------------------------------------------
 * 2024-06-10  이종구        최초작성
 * </pre>
 */
@Repository
public interface TcqiMapper {
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
    OpCuriBas findOpCuriBas(@Param("opOrgid") String opOrgid, @Param("yy") String yy, @Param("semCd") String semCd, @Param("deptLoctCd") String deptLoctCd, @Param("mjOrgid") String mjOrgid, @Param("subjId") String subjId, @Param("bssTkcrsSchgr") int bssTkcrsSchgr);


    List<CamelCaseMap> findCont(TcqiSearch tcqiSearch);
    List<CamelCaseMap> preCheck(TcqiSearch tcqiSearch);



    /**
     * 교육과정기본을(를) 저장한다.
     * @param opCuriBas 교육과정기본정보
     */
    int insertOpCuriBas(OpCuriBas opCuriBas);

    /**
     * 교육과정기본을(를) 수정한다.
     * @param opCuriBas 교육과정기본정보
     */
    int updateOpCuriBas(OpCuriBas opCuriBas);

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
    int deleteOpCuriBas(@Param("opOrgid") String opOrgid, @Param("yy") String yy, @Param("semCd") String semCd, @Param("deptLoctCd") String deptLoctCd, @Param("mjOrgid") String mjOrgid, @Param("subjId") String subjId, @Param("bssTkcrsSchgr") int bssTkcrsSchgr);




    List<CamelCaseMap> findCqiOrzgDscList(TcqiSearch tcqiSearch);

    /* void insertTcqiOrgzDsc(TcqiOrgzDsc tcqiOrgzDsc);*/
    void updateTcqiOrgzDsc(TcqiOrgzDsc tcqiOrgzDsc);


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


    void mergeMcqiContDsc(McqiContDsc mcqiContDsc);


    void updateMcqiContSubmit(McqiContDsc mcqiContDsc);


    void updateMcqiContSubmitCnl(McqiContDsc mcqiContDsc);


    int findMcqiContInpCnt(McqiContDsc mcqiContDsc);

    List<CamelCaseMap> findMcqiPfltAscDsc(TcqiSearch tcqiSearch);

    void deleteMcqiPfltAscDsc(McqiPfltAscDsc mcqiPfltAscDsc);
    void insertMcqiPfltAscDsc(McqiPfltAscDsc mcqiPfltAscDsc);
    void updateMcqiPfltAscDsc(McqiPfltAscDsc mcqiPfltAscDsc);

/* ===============================
   MCQI_MASTER
   =============================== */

    /** CQI 마스터 조회 */
    McqiMaster findMcqiMaster(McqiMaster mcqiMaster);

    /** CQI 마스터 등록 */
    int insertMcqiMaster(McqiMaster mcqiMaster);

    /** CQI 마스터 수정 */
    int updateMcqiMaster(McqiMaster mcqiMaster);

    /** CQI 마스터 삭제 */
    int deleteMcqiMaster(McqiMaster mcqiMaster);

    /** 제출 처리 */
    int updateMcqiMasterSubmit(McqiMaster mcqiMaster);

    /** 제출 취소 */
    int updateMcqiMasterSubmitCnl(McqiMaster mcqiMaster);

    /** 마감 처리 */
    int updateMcqiMasterClose(McqiMaster mcqiMaster);
/* ===============================
   MCQI_COMP_DSC
   =============================== */

    /** 역량 목록 조회 */
    List<McqiCompDsc> findMcqiCompList(McqiCompDsc mcqiCompDsc);

    /** 역량 삭제 (저장 전 전체 삭제 패턴) */
    int deleteMcqiComp(McqiCompDsc mcqiCompDsc);

    /** 역량 등록 */
    int insertMcqiComp(McqiCompDsc mcqiCompDsc);

    /** 역량 수정 */
    int updateMcqiComp(McqiCompDsc mcqiCompDsc);
    /* ===============================
   MCQI_SECT_DSC
   =============================== */

    /** 영역 답변 조회 */
    List<McqiSectDsc> findMcqiSectList(McqiSectDsc mcqiSectDsc);

    /** 영역 답변 삭제 (TYPE 단위 삭제 후 재입력 패턴) */
    int deleteMcqiSect(McqiSectDsc mcqiSectDsc);

    /** 영역 답변 등록 */
    int insertMcqiSect(McqiSectDsc mcqiSectDsc);

    /** 영역 답변 수정 */
    int updateMcqiSect(McqiSectDsc mcqiSectDsc);
    /* ============================================================
       교양 Macro-CQI (CQI_DIV_CD = '0002')
       TcqiMapper.xml 의 SQL ID 와 1:1 대응
    ============================================================ */

    /** ① 교양 MCQI_MASTER 상태 조회 [SQL: findLcqiMaster] */
    CamelCaseMap findLcqiMaster(TcqiSearch tcqiSearch);

    /** ② 교양 영역답변 현재연도 전체 조회 [SQL: findLcqiSectList] */
    List<CamelCaseMap> findLcqiSectList(TcqiSearch tcqiSearch);

    /** ③ 전년도 교양 영역답변 이월 조회 [SQL: findLcqiPrevSectList] */
    List<CamelCaseMap> findLcqiPrevSectList(TcqiSearch tcqiSearch);

    /** ④ 교양 MCQI_MASTER UPSERT [SQL: mergeLcqiMaster] */
    void mergeLcqiMaster(TcqiSearch tcqiSearch);

    /** ⑤ 교양 영역답변 삭제 SECT+TYPE 단위 [SQL: deleteLcqiSect] */
    void deleteLcqiSect(McqiSectDsc mcqiSectDsc);

    /** ⑥ 교양 영역답변 단건 등록 [SQL: insertLcqiSect] */
    void insertLcqiSect(McqiSectDsc mcqiSectDsc);

    /** 전공(CQI_DIV_CD=0001) MCQI_MASTER 상태 조회 */
    CamelCaseMap findMcqiMasterMap(TcqiSearch tcqiSearch);
    /** 전공 영역답변 현재연도 전체 조회 */
    List<CamelCaseMap> findMcqiSectListAll(TcqiSearch tcqiSearch);
    /** 전공 전년도 영역답변 이월 조회 */
    List<CamelCaseMap> findMcqiPrevSectList(TcqiSearch tcqiSearch);
    /** 전공 MCQI_MASTER UPSERT */
    void mergeMcqiMaster(TcqiSearch tcqiSearch);
    /** 전공 영역답변 삭제 SECT+TYPE 단위 */
    void deleteMcqiSectForMajor(McqiSectDsc mcqiSectDsc);
    /** 전공 영역답변 단건 등록 */
    void insertMcqiSectForMajor(McqiSectDsc mcqiSectDsc);
}