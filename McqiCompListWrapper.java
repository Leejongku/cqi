package kr.dku.univ.mcqi.tcqi.domain;

import java.util.List;

/**
 * saveMcqiComp.do 요청 바디용 DTO.
 * - @RequestBody List<McqiCompDsc> 직접 바인딩 시 LinkedHashMap 역직렬화 문제가 발생하는 환경 대응용 래퍼
 */
public class McqiCompListWrapper {

    private List<McqiCompDsc> compList;

    public List<McqiCompDsc> getCompList() {
        return compList;
    }

    public void setCompList(List<McqiCompDsc> compList) {
        this.compList = compList;
    }
}

