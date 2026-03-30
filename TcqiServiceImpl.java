package kr.dku.univ.mcqi.tcqi.service.impl;

import java.util.ArrayList;
import java.util.List;
import kr.dku.base.service.BaseService;
import kr.dku.base.service.ServiceException;
import kr.dku.base.servlet.mvc.TransactionTypes;
import kr.dku.base.util.CamelCaseMap;
import kr.dku.univ.mcqi.tcqi.domain.McqiContDsc;
import kr.dku.univ.mcqi.tcqi.domain.OpCuriBas;
import kr.dku.univ.mcqi.tcqi.domain.McqiPfltAscDsc;
import kr.dku.univ.mcqi.tcqi.domain.TcqiOrgzDsc;
import kr.dku.univ.mcqi.tcqi.repository.TcqiMapper;
import kr.dku.univ.mcqi.tcqi.service.TcqiService;
import kr.dku.univ.mcqi.tcqi.domain.McqiMaster;
import kr.dku.univ.mcqi.tcqi.domain.McqiCompDsc;
import kr.dku.univ.mcqi.tcqi.domain.McqiSectDsc;

import kr.dku.univ.mcqi.tcqi.web.TcqiSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;


/**
 * @author 이종구
 * @version 1.0
 * <pre>
 * 수정일        수정자        수정내용
 * ---------------------------------------------------------------------
 * 2024-06-10  이종구        최초작성
 * </pre>
 */
@Service
public class TcqiServiceImpl extends BaseService implements TcqiService {
    private TcqiMapper tcqiMapper;

    @Autowired
    public void setTcqiMapper(TcqiMapper tcqiMapper) {
        this.tcqiMapper = tcqiMapper;
    }

    @Override
    public OpCuriBas findOpCuriBas(String opOrgid, String yy, String semCd, String deptLoctCd, String mjOrgid, String subjId, int bssTkcrsSchgr) {
        return tcqiMapper.findOpCuriBas(opOrgid, yy, semCd, deptLoctCd, mjOrgid, subjId, bssTkcrsSchgr);
    }


    @Override
    public List<CamelCaseMap> findCont(TcqiSearch tcqiSearch) {

        return tcqiMapper.findCont(tcqiSearch);

    }


    @Override
    public void insertOpCuriBas(OpCuriBas opCuriBas) {
        tcqiMapper.insertOpCuriBas(opCuriBas);
    }

    @Override
    public void updateOpCuriBas(OpCuriBas opCuriBas) {
        int affectedRowCount = tcqiMapper.updateOpCuriBas(opCuriBas);
        if (affectedRowCount != 1) {
            throw new ServiceException("교육과정기본 데이터 수정에 실패했습니다.");
        }
    }

    @Override
    public void deleteOpCuriBas(String opOrgid, String yy, String semCd, String deptLoctCd, String mjOrgid, String subjId, int bssTkcrsSchgr) {
        int affectedRowCount = tcqiMapper.deleteOpCuriBas(opOrgid, yy, semCd, deptLoctCd, mjOrgid, subjId, bssTkcrsSchgr);
        if (affectedRowCount != 1) {
            throw new ServiceException("교육과정기본 데이터 삭제에 실패했습니다.");
        }
    }



    @Override
    public List<CamelCaseMap> preCheck(TcqiSearch tcqiSearch) {
        return tcqiMapper.preCheck(tcqiSearch);
    }



    @Override
    public List<CamelCaseMap> findCqiOrzgDscList(TcqiSearch tcqiSearch) {
        return tcqiMapper.findCqiOrzgDscList(tcqiSearch);

    }



    // 수강학점 저장
    @Override
    public void saveCqiOrzgDsc(List<TcqiOrgzDsc> tcqiOrgzDsc) {
        // TODO Auto-generated method stub
        if (CollectionUtils.isEmpty(tcqiOrgzDsc)) {
            return;
        }

        for(TcqiOrgzDsc tcqiOrgzDscList : tcqiOrgzDsc) {
            switch (tcqiOrgzDscList.getStatus()) {
                case TransactionTypes.INSERTED:
                    /*tcqiMapper.insertTcqiOrgzDsc(tcqiOrgzDscList);*/
                    break;
                case TransactionTypes.UPDATED:
                    tcqiMapper.updateTcqiOrgzDsc(tcqiOrgzDscList);
                    break;
                case TransactionTypes.DELETED:

                    break;
            }
        }
    }

    //  cqi 강의평가 집계
    @Override
    public void saveCallCqiProc(TcqiSearch tcqiSearch) {


        tcqiMapper.saveCallCqiProc(tcqiSearch);

        if(tcqiSearch.getResultCode()< 0) {
            throw new ServiceException(tcqiSearch.getResultMessage());
        }

    }

    @Override
    public List<CamelCaseMap> findMcqiContDscList(TcqiSearch tcqiSearch) {
        // TODO Auto-generated method stub
        return tcqiMapper.findMcqiContDscList(tcqiSearch);
    }

    @Override
    public void saveMcqiContDsc(McqiContDsc mcqiContDsc) {
        // TODO Auto-generated method stub
        if("1".equals(mcqiContDsc.getSubmitYn())) {
            throw new ServiceException("이미 제출되었습니다.");
        } else if("1".equals(mcqiContDsc.getCloseYn())) {
            throw new ServiceException("이미 마감되었습니다.");
        }

        tcqiMapper.mergeMcqiContDsc(mcqiContDsc);
    }

    @Override
    public void saveMcqiContSubmit(McqiContDsc mcqiContDsc) {
        // TODO Auto-generated method stub
        int cnt = tcqiMapper.findMcqiContInpCnt(mcqiContDsc);

        if(cnt > 0) {
            throw new ServiceException("작성하지 않은 항목이 존재합니다.");
        }

        tcqiMapper.updateMcqiContSubmit(mcqiContDsc);
    }

    @Override
    public void saveMcqiContSubmitCnl(McqiContDsc mcqiContDsc) {
        // TODO Auto-generated method stub
        tcqiMapper.updateMcqiContSubmitCnl(mcqiContDsc);
    }

    @Override
    public List<CamelCaseMap> findMcqiPfltAscDsc(TcqiSearch tcqiSearch) {
        // TODO Auto-generated method stub
        return tcqiMapper.findMcqiPfltAscDsc(tcqiSearch);
    }

    /*@Override
    public void saveMcqiPfltAscDsc(McqiContDsc mcqiContDsc) {
        // TODO Auto-generated method stub
        tcqiMapper.saveMcqiPfltAscDsc(mcqiContDsc);
    }*/

    //@Override
    public void saveMcqiPfltAscDsc(List<McqiPfltAscDsc> mcqiPfltAscDscList) {
        if (CollectionUtils.isEmpty(mcqiPfltAscDscList)) {
            return;
        }

        for (McqiPfltAscDsc mcqiPfltAscDsc : mcqiPfltAscDscList) {
            switch (mcqiPfltAscDsc.getStatus()) {
                case TransactionTypes.INSERTED:
                    tcqiMapper.insertMcqiPfltAscDsc(mcqiPfltAscDsc);
                    break;
                case TransactionTypes.UPDATED:
                    tcqiMapper.updateMcqiPfltAscDsc(mcqiPfltAscDsc);
                    break;
                case TransactionTypes.DELETED:
                    tcqiMapper.deleteMcqiPfltAscDsc(mcqiPfltAscDsc);
                    break;
            }
        }

    }
    @Override
    public void saveMcqiMaster(McqiMaster master) {

        McqiMaster exists = tcqiMapper.findMcqiMaster(master);

        if (exists == null) {
            tcqiMapper.insertMcqiMaster(master);
        } else {
            tcqiMapper.updateMcqiMaster(master);
        }
    }

    @Override
    public void submitMcqi(McqiMaster master) {

        if ("Y".equals(master.getSubmitYn())) {
            throw new ServiceException("이미 제출되었습니다.");
        }

        if ("Y".equals(master.getCloseYn())) {
            throw new ServiceException("이미 마감되었습니다.");
        }

        tcqiMapper.updateMcqiMasterSubmit(master);
    }

    @Override
    public void submitCancelMcqi(McqiMaster master) {
        tcqiMapper.updateMcqiMasterSubmitCnl(master);
    }

    @Override
    public void closeMcqi(McqiMaster master) {
        tcqiMapper.updateMcqiMasterClose(master);
    }
    @Override
    public void saveMcqiCompList(List<McqiCompDsc> compList) {

        if (CollectionUtils.isEmpty(compList)) {
            return;
        }

        McqiCompDsc base = compList.get(0);

        // 전체 삭제
        tcqiMapper.deleteMcqiComp(base);

        // 재삽입
        for (McqiCompDsc comp : compList) {
            tcqiMapper.insertMcqiComp(comp);
        }
    }
    @Override
    public void saveMcqiSectList(List<McqiSectDsc> sectList) {

        if (CollectionUtils.isEmpty(sectList)) {
            return;
        }

        McqiSectDsc base = sectList.get(0);

        // TYPE 단위 삭제
        tcqiMapper.deleteMcqiSect(base);

        // 재삽입
        for (McqiSectDsc sect : sectList) {
            tcqiMapper.insertMcqiSect(sect);
        }
    }
    //    @Override
//    public List<CamelCaseMap> findMcqiSectPivot(TcqiSearch search) {
//        return tcqiMapper.findMcqiSectPivot(search);
//    }
    @Override
    public List<McqiCompDsc> findMcqiCompList(McqiCompDsc mcqiCompDsc) {
        return tcqiMapper.findMcqiCompList(mcqiCompDsc);
    }
    @Override
    public List<McqiSectDsc> findMcqiSectList(McqiSectDsc mcqiSectDsc) {
        return tcqiMapper.findMcqiSectList(mcqiSectDsc);
    }
    /* ============================================================
       교양 Macro-CQI (CQI_DIV_CD = '0002')
    ============================================================ */

    @Override
    public CamelCaseMap findLcqiMaster(TcqiSearch tcqiSearch) {
        return tcqiMapper.findLcqiMaster(tcqiSearch);
    }

    @Override
    public List<CamelCaseMap> findLcqiSectList(TcqiSearch tcqiSearch) {
        return tcqiMapper.findLcqiSectList(tcqiSearch);
    }

    @Override
    public List<CamelCaseMap> findLcqiPrevSectList(TcqiSearch tcqiSearch) {
        return tcqiMapper.findLcqiPrevSectList(tcqiSearch);
    }

    /**
     * 교양 CQI 영역답변 저장
     *
     * 처리 순서:
     *   ① MCQI_MASTER 상태 확인 (제출/마감 시 예외)
     *   ② mergeLcqiMaster - MASTER UPSERT (CQI_DIV_CD='0002' SQL 내부 고정)
     *   ③ TYPE='01' 필터링 ★ (전년도 개선계획 저장 금지)
     *   ④ (SECT_CD + TYPE_CD) 단위 DELETE → INSERT
     *
     * XML 파라미터:
     *   mergeLcqiMaster  → #{yy} #{orgid} #{pfltId} #{inputUserId} #{latestUserId} #{remoteAddr}
     *   deleteLcqiSect   → #{yy} #{orgid} #{pfltId} #{cqiSectCd} #{cqiTypeCd}
     *   insertLcqiSect   → #{yy} #{orgid} #{pfltId} #{cqiSectCd} #{cqiTypeCd}
     *                       #{choiCd} #{ansTypeCd} #{cont} #{sortOrd}
     *                       #{inputUserId} #{remoteAddr}
     *   ★ McqiSectDsc extends Domain → inputUserId/remoteAddr 는 Domain 상속 필드
     */
    @Override
    public void saveLcqiSectList(TcqiSearch tcqiSearch, List<McqiSectDsc> sectList) {

        /* ① 마스터 상태 확인 */
        CamelCaseMap master = tcqiMapper.findLcqiMaster(tcqiSearch);
        if (master != null) {
            if ("Y".equals(master.getString("submitYn"))) {
                throw new ServiceException("이미 제출된 CQI입니다. 제출취소 후 수정해 주세요.");
            }
            if ("Y".equals(master.getString("closeYn"))) {
                throw new ServiceException("마감된 CQI는 수정할 수 없습니다.");
            }
        }

        /* ② MCQI_MASTER UPSERT */
        tcqiMapper.mergeLcqiMaster(tcqiSearch);

        if (CollectionUtils.isEmpty(sectList)) return;

        /* ③ TYPE='01' 필터링 ★
           전년도 개선계획(TYPE='01' 또는 '0001')은 이월 전용 → 저장 금지 */
        List<McqiSectDsc> filtered = new ArrayList<McqiSectDsc>();
        for (McqiSectDsc sect : sectList) {
            if ("01".equals(sect.getCqiTypeCd()) || "0001".equals(sect.getCqiTypeCd())) continue;  /* ★ TYPE 전년도 저장 금지 */

            /* 위변조 방지: 서버 세션값으로 강제 덮어쓰기 */
            sect.setYy(tcqiSearch.getYy());
            sect.setOrgid(tcqiSearch.getOrgid());
            sect.setPfltId(tcqiSearch.getPfltId());
            /* Domain 상속 필드 → XML #{inputUserId} #{remoteAddr} */
            sect.setInputUserId(tcqiSearch.getInputUserId());
            sect.setRemoteAddr(tcqiSearch.getRemoteAddr());
            filtered.add(sect);
        }

        /* ④ (SECT_CD + TYPE_CD) 단위 DELETE → INSERT */
        List<String> deletedKeys = new ArrayList<String>();
        for (McqiSectDsc sect : filtered) {
            String key = sect.getCqiSectCd() + "_" + sect.getCqiTypeCd();
            if (!deletedKeys.contains(key)) {
                tcqiMapper.deleteLcqiSect(sect);
                deletedKeys.add(key);
            }
            tcqiMapper.insertLcqiSect(sect);
        }
    }

    /* ========== 전공 Macro-CQI (CQI_DIV_CD='0001') - lcqi와 동일 로직 ========== */
    @Override
    public CamelCaseMap findMcqiMasterMap(TcqiSearch tcqiSearch) {
        return tcqiMapper.findMcqiMasterMap(tcqiSearch);
    }
    @Override
    public List<CamelCaseMap> findMcqiSectListAll(TcqiSearch tcqiSearch) {
        return tcqiMapper.findMcqiSectListAll(tcqiSearch);
    }
    @Override
    public List<CamelCaseMap> findMcqiPrevSectList(TcqiSearch tcqiSearch) {
        return tcqiMapper.findMcqiPrevSectList(tcqiSearch);
    }
    @Override
    public void saveMcqiSectList(TcqiSearch tcqiSearch, List<McqiSectDsc> sectList) {
        CamelCaseMap master = tcqiMapper.findMcqiMasterMap(tcqiSearch);
        if (master != null) {
            if ("Y".equals(master.getString("submitYn"))) throw new ServiceException("이미 제출된 CQI입니다. 제출취소 후 수정해 주세요.");
            if ("Y".equals(master.getString("closeYn"))) throw new ServiceException("마감된 CQI는 수정할 수 없습니다.");
        }
        tcqiMapper.mergeMcqiMaster(tcqiSearch);
        if (CollectionUtils.isEmpty(sectList)) return;
        List<McqiSectDsc> filtered = new ArrayList<McqiSectDsc>();
        for (McqiSectDsc sect : sectList) {
            if ("0001".equals(sect.getCqiTypeCd())) continue;
            sect.setYy(tcqiSearch.getYy());
            sect.setOrgid(tcqiSearch.getOrgid());
            sect.setPfltId(tcqiSearch.getPfltId());
            sect.setCqiDivCd("0001");
            sect.setInputUserId(tcqiSearch.getInputUserId());
            sect.setRemoteAddr(tcqiSearch.getRemoteAddr());
            filtered.add(sect);
        }
        List<String> deletedKeys = new ArrayList<String>();
        for (McqiSectDsc sect : filtered) {
            String key = sect.getCqiSectCd() + "_" + sect.getCqiTypeCd();
            if (!deletedKeys.contains(key)) {
                tcqiMapper.deleteMcqiSectForMajor(sect);
                deletedKeys.add(key);
            }
            tcqiMapper.insertMcqiSectForMajor(sect);
        }
    }
}