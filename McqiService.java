package kr.dku.univ.mcqi.mcqi.service;

import kr.dku.base.util.CamelCaseMap;
import kr.dku.univ.srec.bmgt.web.BmgtSearch;


/**
 * @author 정보기획팀
 * @version 1.0
 * <pre>
 * 수정일                  수정자        수정내용
 * ---------------------------------------------------------------------
 * 2025-01-15  엄현수        최초작성
 * </pre>
 */
public interface McqiService {


    /**
     * web 전공MACRO-CQI데이터확인을(를) 조회한다.
     * @param yy 년도
     * @param pcondSeq 조회조건
     * @param deptLoctCd 조직위치
     * @param collCd 조직 대분류
     * @param dpmtCd 조직 중분류
     * @param mjCd 조직 소분류
     */
    CamelCaseMap findMcqiContDscList(BmgtSearch bmgtSearch);


    CamelCaseMap findLcqiContDscList(BmgtSearch bmgtSearch);
}
