package kr.dku.univ.mcqi.mcqi.web;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import kr.dku.base.security.SecurityContextHelper;
import kr.dku.base.security.access.annotation.Secured;
import kr.dku.base.security.access.annotation.SecurityPolicy;
import kr.dku.base.security.authentication.UserDetails;
import kr.dku.base.service.ServiceException;
import kr.dku.base.servlet.BaseController;
import kr.dku.base.util.CamelCaseMap;
import kr.dku.comm.syst.cdmg.service.CodeService;
import kr.dku.comm.syst.cdmg.web.CommCodeDataSearch;
import kr.dku.univ.lssn.ccmg.domain.D3CoreAblyBss;
import kr.dku.univ.lssn.ccmg.service.CcmgService;
import kr.dku.univ.mcqi.mcqi.domain.McqiListWrapper;
import kr.dku.univ.mcqi.mcqi.service.McqiService;
import kr.dku.univ.mcqi.tcqi.domain.McqiCompDsc;
import kr.dku.univ.mcqi.tcqi.domain.McqiCompListWrapper;
import kr.dku.univ.mcqi.tcqi.domain.McqiContDsc;
import kr.dku.univ.mcqi.tcqi.domain.McqiMaster;
import kr.dku.univ.mcqi.tcqi.domain.McqiSectDsc;
import kr.dku.univ.mcqi.tcqi.domain.McqiSectListWrapper;
import kr.dku.univ.mcqi.tcqi.service.TcqiService;
import kr.dku.univ.mcqi.tcqi.web.TcqiSearch;
import kr.dku.univ.srec.bmgt.service.BmgtService;
import kr.dku.univ.srec.bmgt.web.BmgtSearch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author 정보기획팀
 * @version 1.0
 * <pre>
 * 수정일                      수정자        수정내용
 * ---------------------------------------------------------------------
 * 2025-01-15   엄현수        최초작성
 * 2026-02-11   엄현수        교양 CQI 작성 화면 - findLcqiContDscForm 보강
 *                            saveLcqiSect.do  신규 추가
 * </pre>
 */
@Controller
@RequestMapping(value = "/univ/mcqi/mcqi")
public class McqiController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(McqiController.class);

    /** 교양 CQI 구분 코드 */
    private static final String LCQI_DIV_CD = "0002";

    /** 교양 CQI 프로그램 ID */
    private static final String LCQI_PGM_ID = "7057";

    /** 기준 업무일정 ID */
    private static final String BASE_WOK_ID_249 = "0000000249";
    private static final String BASE_WOK_ID_251 = "0000000251";
    private static final String BASE_WOK_ID_263 = "0000000263";

    /** 교양 기본 조직 코드 */
    private static final String LCQI_DEFAULT_COLL_CD = "2000000989";

    private McqiService mcqiService;
    private BmgtService bmgtService;
    private TcqiService tcqiService;
    private CodeService codeService;

    @Autowired
    private CcmgService ccmgService;

    @Autowired
    public void setMcqiService(McqiService mcqiService) {
        this.mcqiService = mcqiService;
    }

    @Autowired
    public void setBmgtService(BmgtService bmgtService) {
        this.bmgtService = bmgtService;
    }

    @Autowired
    public void setTcqiService(TcqiService tcqiService) {
        this.tcqiService = tcqiService;
    }

    @Autowired
    public void setCodeService(CodeService codeService) {
        this.codeService = codeService;
    }


    /**
     * 전공 Macro-CQI 현황 화면을(를) 조회한다.
     */
    @RequestMapping(value = "/views/findMcqiContDscList.do")
    public String findMcqiContDscListView(BmgtSearch bmgtSearch, Model model) {
        if (bmgtSearch.getYy() == null) {
            bmgtSearch.setYy(findBasYy(BASE_WOK_ID_249));
        }
        bmgtSearch.setPgmId("6830");
        bmgtSearch.setUseYn("1");
        bmgtSearch.setPcondTyCd("0001");
        model.addAttribute("bachCmnPcondDscList", bmgtService.findBachCmnPcondDscList(bmgtSearch));
        model.addAttribute("bmgtSearch", bmgtSearch);
        return "univ/mcqi/mcqi/mcqiContDscList";
    }
    /**
     * web 교양MACRO-CQI데이터확인을(를) 조회한다.
     * @param yy 년도
     * @param pcondSeq 조회조건
     * @param deptLoctCd 조직위치
     * @param model 조직 대분류
     * @param dpmtCd 조직 중분류
     * @param mjCd 조직 소분류
     */
    @RequestMapping(value = "/findLcqiContDscList.do")
    public void findLcqiContDscList(BmgtSearch bmgtSearch, Model model){
        int checknum = 1;

        try {
            model.addAttribute("returnMap", mcqiService.findLcqiContDscList(bmgtSearch));
        } catch(ServiceException se) {
            checknum = 2;
        }
        model.addAttribute("success", checknum);
    }

    /**
     * 전공 Macro-CQI 현황 목록을(를) 조회한다.
     */
    @RequestMapping(value = "/findMcqiContDscList.do")
    public void findMcqiContDscList(BmgtSearch bmgtSearch, Model model) {
        int checknum = 1;
        try {
            model.addAttribute("returnMap", mcqiService.findMcqiContDscList(bmgtSearch));
        } catch (ServiceException se) {
            log.warn("[McqiController] findMcqiContDscList ServiceException: {}", se.getMessage());
            checknum = 2;
        }
        model.addAttribute("success", checknum);
    }

    /**
     * 전공 Macro-CQI 작성 화면을(를) 조회한다.
     */
    @RequestMapping(value = "/views/findMcqiContDscForm.do")
    public String findMcqiContDscFormView(TcqiSearch tcqiSearch, Model model) {
        UserDetails userDetails = SecurityContextHelper.getUserDetails();
        if (tcqiSearch.getYy() == null) {
            tcqiSearch.setYy(findBasYy(BASE_WOK_ID_249));
            tcqiSearch.setCollCd(LCQI_DEFAULT_COLL_CD);
            tcqiSearch.setDeptLoctCd(userDetails.getDeptLoctCd());
        }



        // 전공 화면에서는 orgid(저장/조회 키)를 전공코드(mjCd)로 사용한다. (mcqiContDscForm.jsp hidden orgid)
        try {
            if ((tcqiSearch.getOrgid() == null || tcqiSearch.getOrgid().isEmpty())
                    && tcqiSearch.getMjCd() != null && !tcqiSearch.getMjCd().isEmpty()) {
                tcqiSearch.setOrgid(tcqiSearch.getMjCd());
            }
        } catch (Exception ignore) { }
        D3CoreAblyBss  d3CoreAblyBss   = new D3CoreAblyBss();
        d3CoreAblyBss.setFrYy(tcqiSearch.getYy());
        d3CoreAblyBss.setFrSemCd("1");
        d3CoreAblyBss.setCoreTypCd("2"); /*2가 전공*/
        d3CoreAblyBss.setPrtYn("1");  /*역량코드*/

        // 역량 기준 목록 조회 (화면 컬럼 헤더 + 동적 pivot 키 생성용)
        List<CamelCaseMap> d3CoreAblyBssList = ccmgService.findCoreBssAblyList(d3CoreAblyBss);
        model.addAttribute("d3CoreAblyBssList", d3CoreAblyBssList);

        // 조직+마스터 목록 조회
        List<CamelCaseMap> mcqiContDscList = tcqiService.findMcqiContDscList(tcqiSearch);

        // 역량 raw 데이터 조회 후 동적 pivot: compYn{역량코드} 키로 각 행에 주입
        // → 역량코드가 매년 바뀌어도 코드 수정 불필요
        try {
            List<String> ablyCodes = new java.util.ArrayList<String>();
            for (CamelCaseMap m : d3CoreAblyBssList) {
                if (m.get("d3CoreAblyCd") != null) ablyCodes.add(m.get("d3CoreAblyCd").toString());
            }
            if (!ablyCodes.isEmpty()) {
                List<CamelCaseMap> compRawList = tcqiService.findMcqiCompRawList(tcqiSearch);
                java.util.Map<String, java.util.Map<String, String>> compByOrg =
                    new java.util.HashMap<String, java.util.Map<String, String>>();
                for (CamelCaseMap raw : compRawList) {
                    String oid  = raw.get("orgid")      != null ? raw.get("orgid").toString()      : "";
                    String code = raw.get("cqiCompCd")  != null ? raw.get("cqiCompCd").toString()  : "";
                    String yn   = raw.get("cqiCompYn")  != null ? raw.get("cqiCompYn").toString()  : "N";
                    if (!compByOrg.containsKey(oid)) compByOrg.put(oid, new java.util.HashMap<String, String>());
                    compByOrg.get(oid).put(code, yn);
                }
                for (CamelCaseMap row : mcqiContDscList) {
                    String oid = row.get("orgid") != null ? row.get("orgid").toString() : "";
                    java.util.Map<String, String> compMap = compByOrg.containsKey(oid)
                        ? compByOrg.get(oid) : new java.util.HashMap<String, String>();
                    for (String code : ablyCodes) {
                        row.put("compYn" + code, compMap.containsKey(code) ? compMap.get(code) : "N");
                    }
                }
            }
        } catch (Exception e) {
            log.warn("[McqiController] 역량 동적 pivot 실패: {}", e.getMessage());
        }
        model.addAttribute("mcqiContDscList", mcqiContDscList);
        model.addAttribute("tcqiSearch", tcqiSearch);
        model.addAttribute("cnt", findSchedCnt(tcqiSearch, userDetails, BASE_WOK_ID_251));

        // 0. 학과 역량 설정: MCQI_COMP_DSC에서 전공 역량(D1~D10) 조회
        if (tcqiSearch.getYy() != null && tcqiSearch.getOrgid() != null && !tcqiSearch.getOrgid().isEmpty() && userDetails != null) {
            McqiCompDsc compSearch = new McqiCompDsc();
            compSearch.setYy(tcqiSearch.getYy());
            compSearch.setOrgid(tcqiSearch.getOrgid());
            compSearch.setPfltId(userDetails.getIntgUid());
            compSearch.setCqiDivCd("0001"); // 전공
            model.addAttribute("mcqiCompList", tcqiService.findMcqiCompList(compSearch));
        } else {
            model.addAttribute("mcqiCompList", java.util.Collections.emptyList());
        }
        // 공통코드(섹션/타입) - 탭 1~4 역량중심·학습자중심·혁신적·지원요청 등 lcqi와 동일 로직
        try {
            CommCodeDataSearch codeDataSearch = new CommCodeDataSearch();
            codeDataSearch.setAddtInfoValu1("0001");
            codeDataSearch.setCmnCdId("CQI_SECT_CD");
            model.addAttribute("cqiSectCdList", codeService.findCommCodeDataList(codeDataSearch));
            codeDataSearch.setCmnCdId("CQI_TYPE_CD");
            model.addAttribute("cqiTypeCdList", codeService.findCommCodeDataList(codeDataSearch));
            codeDataSearch.setCmnCdId("CHOI_CD");
            model.addAttribute("choiCdList", codeService.findCommCodeDataList(codeDataSearch));
        } catch (Exception e) {
            log.warn("[McqiController] 전공 공통코드(CQI_SECT_CD/CQI_TYPE_CD) 조회 실패: {}", e.getMessage());
            model.addAttribute("cqiSectCdList", java.util.Collections.emptyList());
            model.addAttribute("cqiTypeCdList", java.util.Collections.emptyList());
            model.addAttribute("choiCdList", java.util.Collections.emptyList());
        }
        if (userDetails != null) {
            model.addAttribute("userEmpId", userDetails.getIntgUid());
        }
        return "univ/mcqi/mcqi/mcqiContDscForm";
    }

    /**
     * 전공 Macro-CQI을(를) 저장한다.
     */
    @RequestMapping(value = "/saveMcqiContDsc.do")
    public void saveMcqiContDsc(McqiContDsc mcqiContDsc, Model model) {
        String msg = "";
        try {
            tcqiService.saveMcqiContDsc(mcqiContDsc);
        } catch (ServiceException e) {
            msg = e.getMessage();
        }
        model.addAttribute("msg", msg);
    }

    /**
     * 전공 Macro-CQI을(를) 제출한다.
     */
    @RequestMapping(value = "/saveMcqiContSubmit.do")
    public void saveMcqiContSubmit(McqiContDsc mcqiContDsc, Model model) {
        String msg = "";
        try {
            tcqiService.saveMcqiContSubmit(mcqiContDsc);
        } catch (ServiceException e) {
            msg = e.getMessage();
        }
        model.addAttribute("msg", msg);
    }

    /**
     * 전공 Macro-CQI 제출을(를) 취소한다.
     */
    @RequestMapping(value = "/saveMcqiContSubmitCnl.do")
    public void saveMcqiContSubmitCnl(McqiContDsc mcqiContDsc, Model model) {
        tcqiService.saveMcqiContSubmitCnl(mcqiContDsc);
    }

    /**
     * 역량-교수 매핑을(를) 조회한다.
     */
    @RequestMapping(value = "/findMcqiPfltAscDsc.do")
    public void findMcqiPfltAscDsc(TcqiSearch tcqiSearch, Model model) {
        model.addAttribute("mcqiPfltAscDscList", tcqiService.findMcqiPfltAscDsc(tcqiSearch));
    }

    /**
     * 역량-교수 매핑을(를) 저장한다.
     */
    @RequestMapping(value = "/saveMcqiPfltAscDsc.do")
    public void saveMcqiPfltAscDsc(McqiListWrapper mcqiListWrapper) {
        tcqiService.saveMcqiPfltAscDsc(mcqiListWrapper.getMcqiPfltAscDsc());
    }

    /**
     * MCQI 마스터를(를) 제출한다.
     */
    @Secured(policy = SecurityPolicy.SESSION)
    @RequestMapping(value = "/saveMcqiMaster.do")
    public void saveMcqiMaster(McqiMaster mcqiMaster, Model model, HttpServletRequest request) {
        String msg = "";
        try {
            UserDetails userDetails = SecurityContextHelper.getUserDetails();
            if (userDetails != null) {
                // 전공/교양 공통: PFLT_ID는 교번(사용자 ID) 기준
                mcqiMaster.setPfltId(userDetails.getIntgUid());
                mcqiMaster.setInputUserId(userDetails.getIntgUid());
                mcqiMaster.setLatestUserId(userDetails.getIntgUid());
            }
            if (mcqiMaster.getCqiDivCd() == null || "".equals(mcqiMaster.getCqiDivCd())) {
                mcqiMaster.setCqiDivCd("0001");
            }
            mcqiMaster.setRemoteAddr(request != null ? request.getRemoteAddr() : null);
            tcqiService.saveMcqiMaster(mcqiMaster);
        } catch (ServiceException e) {
            msg = e.getMessage();
        }
        model.addAttribute("msg", msg);
    }

    /**
     * MCQI 마스터를(를) 제출한다.
     */
    @RequestMapping(value = "/saveMcqiMasterSubmit.do")
    public void saveMcqiMasterSubmit(McqiMaster mcqiMaster, Model model) {
        String msg = "";
        try {
            tcqiService.submitMcqi(mcqiMaster);
        } catch (ServiceException e) {
            msg = e.getMessage();
        }
        model.addAttribute("msg", msg);
    }

    /**
     * MCQI 마스터 제출을(를) 취소한다.
     */
    @RequestMapping(value = "/saveMcqiMasterSubmitCnl.do")
    public void saveMcqiMasterSubmitCnl(McqiMaster mcqiMaster) {
        tcqiService.submitCancelMcqi(mcqiMaster);
    }

    /**
     * MCQI 마스터를(를) 마감한다.
     */
    @RequestMapping(value = "/saveMcqiMasterClose.do")
    public void saveMcqiMasterClose(McqiMaster mcqiMaster) {
        tcqiService.closeMcqi(mcqiMaster);
    }

    /**
     * 역량 목록을(를) 조회한다.
     */
    @RequestMapping(value = "/findMcqiCompList.do")
    public void findMcqiCompList(McqiCompDsc mcqiCompDsc, Model model) {
        model.addAttribute("mcqiCompList", tcqiService.findMcqiCompList(mcqiCompDsc));
    }

    /**
     * 역량을(를) 저장한다.
     */
    @Secured(policy = SecurityPolicy.SESSION)
    @RequestMapping(value = "/saveMcqiComp.do", method = RequestMethod.POST, consumes = "application/json; charset=UTF-8")
    public void saveMcqiComp(@RequestBody McqiCompListWrapper wrapper, Model model, HttpServletRequest request) {
        String msg = "";
        try {
            UserDetails ud = SecurityContextHelper.getUserDetails();
            String empId = (ud != null) ? ud.getIntgUid() : null;
            String remoteAddr = (request != null) ? request.getRemoteAddr() : null;
            java.util.List<McqiCompDsc> list = (wrapper != null && wrapper.getCompList() != null)
                    ? wrapper.getCompList() : java.util.Collections.<McqiCompDsc>emptyList();
            // 위변조 방지: 교번/전공구분/감사필드 서버값으로 강제 세팅
            for (McqiCompDsc d : list) {
                if (d == null) continue;
                d.setPfltId(empId);
                d.setCqiDivCd("0001");
                d.setInputUserId(empId);
                d.setLatestUserId(empId);
                d.setRemoteAddr(remoteAddr);
            }
            tcqiService.saveMcqiCompList(list);
        } catch (ServiceException e) {
            msg = e.getMessage();
        }
        model.addAttribute("msg", msg);
    }

    /**
     * 영역 답변 목록을(를) 조회한다.
     */
    @RequestMapping(value = "/findMcqiSectList.do")
    public void findMcqiSectList(McqiSectDsc mcqiSectDsc, Model model) {
        model.addAttribute("mcqiSectList", tcqiService.findMcqiSectList(mcqiSectDsc));
    }

    /* saveMcqiSect.do 는 하단 JSON API(Wrapper 기반) 단일 엔드포인트를 사용한다. */

    @RequestMapping(value = "/views/findLcqiContDscList.do")
    public String findLqiContDscListView(BmgtSearch bmgtSearch, Model model) {

        if(bmgtSearch.getYy()==null){
            BmgtSearch bmgtSearch2 = new BmgtSearch();
            bmgtSearch2.setDtlWokId("0000000249");
            List<CamelCaseMap> bachSchedDscList = bmgtService.findBachDtlWokDscList(bmgtSearch2);
            if(bachSchedDscList.size() > 0) {
                bmgtSearch.setYy(bachSchedDscList.get(0).getString("basYy"));
                bmgtSearch.setSemCd(bachSchedDscList.get(0).getString("basSemCd"));
            }
        }

        model.addAttribute("curiCparNmList", bmgtService.findLqiCuriCparNmList(bmgtSearch));

        bmgtSearch.setPgmId("7057"); // 교양-CQI메뉴ID
        bmgtSearch.setUseYn("1"); //사용여부
        bmgtSearch.setPcondTyCd("0001"); //현황유형

        model.addAttribute("bachCmnPcondDscList", bmgtService.findBachCmnPcondDscList(bmgtSearch));
        model.addAttribute("bmgtSearch", bmgtSearch);

        return "univ/mcqi/mcqi/lcqiContDscList";
    }

    /**
     * 교양 Macro-CQI 작성 화면을(를) 조회한다.
     */
    @RequestMapping(value = "/views/findLcqiContDscForm.do")
    public String findLcqiContDscForm(TcqiSearch tcqiSearch, Model model) {

        UserDetails userDetails = SecurityContextHelper.getUserDetails();

        // 기본 파라미터 세팅 (UserDetails에서 orgid 반영)
        if (tcqiSearch.getYy() == null) {
            tcqiSearch.setYy(findBasYy(BASE_WOK_ID_249));
        }
        String userOrgId = (userDetails != null) ? userDetails.getOrgId() : null;
        if (userOrgId == null || userOrgId.isEmpty()) {
            userOrgId = LCQI_DEFAULT_COLL_CD;
        }
        if (tcqiSearch.getCollCd() == null || tcqiSearch.getCollCd().isEmpty()) {
            tcqiSearch.setCollCd(userOrgId);
            tcqiSearch.setOrgid(userOrgId);
        }
        // pfltId / detMngtDscCd = 교양구분 코드(SELF, COMM, PROB 등). 교번(intgUid)과 혼동 금지.
        if (tcqiSearch.getPfltId() != null && !tcqiSearch.getPfltId().isEmpty()) {
            tcqiSearch.setBeforePfltId(tcqiSearch.getPfltId());
        }

        if (userDetails != null) {
            tcqiSearch.setDeptLoctCd(userDetails.getDeptLoctCd());
        }

        // ─────────────────────────────────────────────────────────
        // [요구사항] 교양CQI 사용자 권한 확인 및 detMngtDscCd 자동 매핑
        // - USERDETAILS.intgUid(교번) = TcqiSearch.pfltId 로 사용
        // - 현재년도(yy) + 교번(pfltId) 기준으로 UNIV.MCQI_PFLT_ASC_DSC 조회
        // - 결과 없으면 "교양CQI 사용자만 화면 접근 가능합니다." 메시지 노출
        // - 결과 있으면 DET_MNGT_DSC_CD 를 화면에 매핑하고 콤보 변경 불가
        // ─────────────────────────────────────────────────────────
        String loginUid = (userDetails != null) ? userDetails.getIntgUid() : null;
        if (loginUid != null && !loginUid.isEmpty()) {
            tcqiSearch.setPfltId(loginUid);
        }

        String mappedDetMngtDscCd = null;
        String mappedDetMngtDscNm = null;
        try {
            List<CamelCaseMap> ascList = tcqiService.findMcqiPfltAscDsc(tcqiSearch);
            if (ascList != null && !ascList.isEmpty()) {
                CamelCaseMap firstLcqiRow = null;
                for (CamelCaseMap row : ascList) {
                    if (row == null) continue;
                    String cqiDivCd = row.get("cqiDivCd") != null ? String.valueOf(row.get("cqiDivCd"))
                            : (row.get("cqi_div_cd") != null ? String.valueOf(row.get("cqi_div_cd")) : null);
                    String pfltId = row.get("pfltId") != null ? String.valueOf(row.get("pfltId"))
                            : (row.get("pflt_id") != null ? String.valueOf(row.get("pflt_id"))
                            : (row.get("empid") != null ? String.valueOf(row.get("empid"))
                            : (row.get("inputUid") != null ? String.valueOf(row.get("inputUid")) : null)));
                    String detCd = row.get("detMngtDscCd") != null ? String.valueOf(row.get("detMngtDscCd"))
                            : (row.get("det_mngt_dsc_cd") != null ? String.valueOf(row.get("det_mngt_dsc_cd")) : null);
                    String detNm = row.get("detMngtDscNm") != null ? String.valueOf(row.get("detMngtDscNm"))
                            : (row.get("det_mngt_dsc_nm") != null ? String.valueOf(row.get("det_mngt_dsc_nm")) : null);

                    // cqi_div_cd 컬럼이 누락된 조회도 있어 null은 통과시킨다.
                    boolean lcqiRow = (cqiDivCd == null || "".equals(cqiDivCd) || LCQI_DIV_CD.equals(cqiDivCd));
                    if (!lcqiRow) continue;
                    if (firstLcqiRow == null && detCd != null && !"".equals(detCd)) {
                        firstLcqiRow = row;
                    }
                    if (loginUid != null && loginUid.equals(pfltId) && detCd != null && !"".equals(detCd)) {
                        mappedDetMngtDscCd = detCd;
                        mappedDetMngtDscNm = detNm;
                        break;
                    }
                }
                // 사용자 키 매칭 실패 시, 교양 행 1건을 fallback 사용
                if ((mappedDetMngtDscCd == null || "".equals(mappedDetMngtDscCd)) && firstLcqiRow != null) {
                    mappedDetMngtDscCd = firstLcqiRow.get("detMngtDscCd") != null ? String.valueOf(firstLcqiRow.get("detMngtDscCd"))
                            : (firstLcqiRow.get("det_mngt_dsc_cd") != null ? String.valueOf(firstLcqiRow.get("det_mngt_dsc_cd")) : null);
                    mappedDetMngtDscNm = firstLcqiRow.get("detMngtDscNm") != null ? String.valueOf(firstLcqiRow.get("detMngtDscNm"))
                            : (firstLcqiRow.get("det_mngt_dsc_nm") != null ? String.valueOf(firstLcqiRow.get("det_mngt_dsc_nm")) : null);
                }
            }
        } catch (Exception e) {
            log.warn("[McqiController] findLcqiContDscForm findMcqiPfltAscDsc 오류: {}", e.getMessage());
        }

        if (mappedDetMngtDscCd == null || mappedDetMngtDscCd.isEmpty()) {
            model.addAttribute("accessDeniedMsg", "교양CQI 사용자만 화면 접근 가능합니다.");
        } else {
            tcqiSearch.setDetMngtDscCd(mappedDetMngtDscCd);
            model.addAttribute("detMngtDscCdLocked", true);
            model.addAttribute("detMngtDscNm", mappedDetMngtDscNm != null ? mappedDetMngtDscNm : "");
        }

        // 화면에서 저장/제출 시 사용할 사용자 정보
        model.addAttribute("userEmpId", userDetails != null ? userDetails.getIntgUid() : "");
        model.addAttribute("userOrgId", userOrgId);

        model.addAttribute("cnt", findSchedCnt(tcqiSearch, userDetails, BASE_WOK_ID_263));

        // 공통코드(섹션/타입) - 화면 탭/행 동적 생성용
        try {
            CommCodeDataSearch codeDataSearch = new CommCodeDataSearch();
            codeDataSearch.setAddtInfoValu1("0002");

            codeDataSearch.setCmnCdId("CQI_SECT_CD");
            model.addAttribute("cqiSectCdList", codeService.findCommCodeDataList(codeDataSearch));

            codeDataSearch.setCmnCdId("CQI_TYPE_CD");
            model.addAttribute("cqiTypeCdList", codeService.findCommCodeDataList(codeDataSearch));

            codeDataSearch.setCmnCdId("CHOI_CD");
            model.addAttribute("choiCdList", codeService.findCommCodeDataList(codeDataSearch));
        } catch (Exception e) {
            log.warn("[McqiController] 공통코드(CQI_SECT_CD/CQI_TYPE_CD) 조회 실패: {}", e.getMessage());
            model.addAttribute("cqiSectCdList", java.util.Collections.emptyList());
            model.addAttribute("cqiTypeCdList", java.util.Collections.emptyList());
            model.addAttribute("choiCdList", java.util.Collections.emptyList());
        }

        if (tcqiSearch.getDetMngtDscCd() != null
                && !tcqiSearch.getDetMngtDscCd().isEmpty()) {

            CamelCaseMap lcqiMaster = tcqiService.findLcqiMaster(tcqiSearch);
            model.addAttribute("lcqiMaster", lcqiMaster);

            boolean closed = lcqiMaster != null && "Y".equals(lcqiMaster.get("closeYn"));
            model.addAttribute("lcqiClosed", closed);

            List<CamelCaseMap> lcqiSectList = tcqiService.findLcqiSectList(tcqiSearch);
            model.addAttribute("lcqiSectList", lcqiSectList);

        } else {
            model.addAttribute("lcqiMaster",   null);
            model.addAttribute("lcqiClosed",   false);
            model.addAttribute("lcqiSectList", java.util.Collections.emptyList());
        }

        model.addAttribute("tcqiSearch", tcqiSearch);

        return "univ/mcqi/mcqi/lcqiContDscForm";
    }

    /**
     * 교양 Macro-CQI 영역답변을(를) 저장한다.
     * <ul>
     *   <li>TcqiSearch: JSP에서 파라미터로 넘어오는 것이 아니라 wrapper(yy, orgid, pfltId) + UserDetails로 구성.</li>
     *   <li>wrapper.sectList: UNIV.MCQI_MASTER 기준 영역답변 목록. JSP(lcqiContDscForm.jsp)에서 _collectList()로 수집 후 sectList로 전송.</li>
     * </ul>
     */
    @Secured(policy = SecurityPolicy.SESSION)
    @RequestMapping(value = "/saveLcqiSect.do",
            method = RequestMethod.POST,
            consumes = "application/json; charset=UTF-8")
    @ResponseBody
    public CamelCaseMap saveLcqiSect(@RequestBody McqiSectListWrapper wrapper, HttpServletRequest request) {
        CamelCaseMap result = new CamelCaseMap();
        result.put("msg", "");
        List<McqiSectDsc> mcqiSectDscList = (wrapper != null && wrapper.getSectList() != null)
                ? wrapper.getSectList() : Collections.<McqiSectDsc>emptyList();
        try {
            UserDetails userDetails = SecurityContextHelper.getUserDetails();

            // pfltId = 교번(intgUid), detMngtDscCd = 교양구분 선택(SELF, COMM, ...) → TcqiSearch/MASTER 키
            String yy = (wrapper != null) ? wrapper.getYy() : null;
            String detMngtDscCd = (wrapper != null) ? wrapper.getDetMngtDscCd() : null;
            if ((yy == null || yy.isEmpty()) && !mcqiSectDscList.isEmpty()) yy = mcqiSectDscList.get(0).getYy();

            // orgid는 요청값/첫row/세션값 중에서 결정, 최종적으로 기본값 보정
            String orgId = (wrapper != null) ? wrapper.getOrgid() : null;
            if ((orgId == null || orgId.isEmpty()) && !mcqiSectDscList.isEmpty()) orgId = mcqiSectDscList.get(0).getOrgid();
            if ((orgId == null || orgId.isEmpty()) && userDetails != null) orgId = userDetails.getOrgId();
            if (orgId == null || orgId.isEmpty()) orgId = LCQI_DEFAULT_COLL_CD;

            if (yy == null || yy.isEmpty() || detMngtDscCd == null || detMngtDscCd.isEmpty()) {
                result.put("msg", "저장 파라미터가 부족합니다. (yy/교양구분)");
                return result;
            }

            // 공통 search 구성 (list가 비어도 MASTER MERGE가 수행되므로 반드시 값 세팅)
            TcqiSearch tcqiSearch = new TcqiSearch();
            tcqiSearch.setYy(yy);
            tcqiSearch.setCollCd(orgId);
            tcqiSearch.setOrgid(orgId);                    // SQL #{orgid}
            tcqiSearch.setDetMngtDscCd(detMngtDscCd);      // 교양구분(화면 드롭다운: SELF, COMM, ...)
            // 교양 CQI에서도 MCQI_MASTER.PFLT_ID는 교번(사용자 ID)을 사용한다.
            tcqiSearch.setPfltId(userDetails != null ? userDetails.getIntgUid() : null);
            if (userDetails != null) {
                tcqiSearch.setInputUserId(userDetails.getIntgUid());
                tcqiSearch.setLatestUserId(userDetails.getIntgUid());
                tcqiSearch.setDeptLoctCd(userDetails.getDeptLoctCd());
            }
            if (request != null) {
                tcqiSearch.setRemoteAddr(request.getRemoteAddr());
            }

            // 마감 상태 체크 (서비스에서도 체크하지만 컨트롤러 레벨 조기 차단)
            CamelCaseMap master = tcqiService.findLcqiMaster(tcqiSearch);
            if (master != null && "Y".equals(master.get("closeYn"))) {
                result.put("msg", "마감된 데이터는 수정할 수 없습니다.");
                return result;
            }
            if (master != null && "Y".equals(master.get("submitYn"))) {
                result.put("msg", "제출된 데이터는 제출취소 후 수정하여 주십시오.");
                return result;
            }

            // 저장은 TcqiServiceImpl 로 위임 (OBJ/SUBJ 및 기타 CONT 포함 저장)
            tcqiService.saveLcqiSectList(tcqiSearch, mcqiSectDscList);

        } catch (ServiceException e) {
            log.warn("[McqiController] saveLcqiSect ServiceException: {}", e.getMessage());
            result.put("msg", e.getMessage());
        } catch (Exception e) {
            log.error("[McqiController] saveLcqiSect 오류", e);
            result.put("msg", "저장 중 오류가 발생하였습니다.");
        }
        return result;
    }


    /* ================================================================
       [신규] 교양 CQI 조회 API (AJAX)
       - mcqiContDscForm.jsp / lcqiContDscForm.jsp 에서 공통 사용
       ================================================================ */

    /**
     * 교양 MCQI_MASTER 상태 조회 (submitYn/closeYn).
     * 요청: GET /findLcqiMaster.do?yy=2026&detMngtDscCd=SELF
     * 응답: 뷰에서 model "lcqiMaster" 사용
     */
    @RequestMapping(value = "/findLcqiMaster.do")
    public String findLcqiMaster(TcqiSearch tcqiSearch, Model model) {
        prepareLcqiSearchDefaults(tcqiSearch);
        model.addAttribute("lcqiMaster", tcqiService.findLcqiMaster(tcqiSearch));
        return "forward:/WEB-INF/views/univ/mcqi/mcqi/lcqiMaster.jsp";
    }

    /**
     * 교양 영역답변 현재연도 조회 (TYPE=02/03 + 저장된 TYPE=01 포함 가능).
     * 요청: GET /findLcqiSectList.do?yy=2026&detMngtDscCd=SELF
     * 응답: 뷰에서 model "lcqiSectList" 사용
     */
    @RequestMapping(value = "/findLcqiSectList.do")
    public String findLcqiSectList(TcqiSearch tcqiSearch, Model model) {
        prepareLcqiSearchDefaults(tcqiSearch);
        model.addAttribute("lcqiSectList", tcqiService.findLcqiSectList(tcqiSearch));
        return "forward:/WEB-INF/views/univ/mcqi/mcqi/lcqiSectList.jsp";
    }

    /**
     * 전년도 교양 영역답변 이월 조회 (SQL 내부 yy-1, TYPE=03 소스만).
     * PFLT_ID = 교번(intgUid)으로 조회.
     * 요청: POST /findLcqiPrevSectList.do (yy, detMngtDscCd, collCd)
     * 응답: 뷰에서 model "lcqiPrevSectList" 사용
     */
    @RequestMapping(value = "/findLcqiPrevSectList.do", method = RequestMethod.POST)
    public String findLcqiPrevSectList(TcqiSearch tcqiSearch, Model model) {
        prepareLcqiSearchDefaults(tcqiSearch);
        UserDetails userDetails = SecurityContextHelper.getUserDetails();
        if (userDetails != null && userDetails.getIntgUid() != null) {
            tcqiSearch.setPfltId(userDetails.getIntgUid());  /* PFLT_ID = 교번 */
        }
        model.addAttribute("lcqiPrevSectList", tcqiService.findLcqiPrevSectList(tcqiSearch));
        return "forward:/WEB-INF/views/univ/mcqi/mcqi/lcqiPrevSectList.jsp";
    }

    /** 전공 CQI 조회 공통 기본값: orgid=mjCd, pfltId=교번 */
    private void prepareMcqiSearchDefaults(TcqiSearch tcqiSearch) {
        if (tcqiSearch.getOrgid() == null || tcqiSearch.getOrgid().isEmpty()) {
            if (tcqiSearch.getMjCd() != null && !tcqiSearch.getMjCd().isEmpty()) {
                tcqiSearch.setOrgid(tcqiSearch.getMjCd());
            } else if (tcqiSearch.getCollCd() != null) {
                tcqiSearch.setOrgid(tcqiSearch.getCollCd());
            }
        }
        UserDetails ud = SecurityContextHelper.getUserDetails();
        if (ud != null && ud.getIntgUid() != null && (tcqiSearch.getPfltId() == null || tcqiSearch.getPfltId().isEmpty())) {
            tcqiSearch.setPfltId(ud.getIntgUid());
        }
    }

    /**
     * 전공 MCQI_MASTER 상태 조회 (AJAX JSON).
     */
    @RequestMapping(value = "/findMcqiMaster.do")
    public String findMcqiMaster(TcqiSearch tcqiSearch, Model model) {
        prepareMcqiSearchDefaults(tcqiSearch);
        model.addAttribute("mcqiMaster", tcqiService.findMcqiMasterMap(tcqiSearch));
        return "forward:/WEB-INF/views/univ/mcqi/mcqi/mcqiMaster.jsp";
    }
    /**
     * 전공 영역답변 현재연도 전체 조회 (AJAX JSON, 전공 Macro-CQI 작성 폼용).
     */
    @RequestMapping(value = "/findMcqiSectListAll.do")
    public String findMcqiSectListAll(TcqiSearch tcqiSearch, Model model) {
        prepareMcqiSearchDefaults(tcqiSearch);
        model.addAttribute("mcqiSectList", tcqiService.findMcqiSectListAll(tcqiSearch));
        return "forward:/WEB-INF/views/univ/mcqi/mcqi/mcqiSectList.jsp";
    }
    /**
     * 전공 전년도 영역답변 이월 조회 (AJAX JSON, POST).
     */
    @RequestMapping(value = "/findMcqiPrevSectList.do", method = RequestMethod.POST)
    public String findMcqiPrevSectList(TcqiSearch tcqiSearch, Model model) {
        prepareMcqiSearchDefaults(tcqiSearch);
        model.addAttribute("mcqiPrevSectList", tcqiService.findMcqiPrevSectList(tcqiSearch));
        return "forward:/WEB-INF/views/univ/mcqi/mcqi/mcqiPrevSectList.jsp";
    }
    /**
     * 전공 Macro-CQI 영역답변 저장 (탭 1~4, lcqi와 동일 로직).
     */
    @Secured(policy = SecurityPolicy.SESSION)
    @RequestMapping(value = "/saveMcqiSect.do", method = RequestMethod.POST, consumes = "application/json; charset=UTF-8")
    @ResponseBody
    public CamelCaseMap saveMcqiSect(@RequestBody McqiSectListWrapper wrapper, HttpServletRequest request) {
        CamelCaseMap result = new CamelCaseMap();
        result.put("msg", "");
        List<McqiSectDsc> list = (wrapper != null && wrapper.getSectList() != null) ? wrapper.getSectList() : Collections.<McqiSectDsc>emptyList();
        try {
            UserDetails ud = SecurityContextHelper.getUserDetails();
            TcqiSearch tcqiSearch = new TcqiSearch();
            tcqiSearch.setYy(wrapper.getYy());
            tcqiSearch.setOrgid(wrapper.getOrgid());
            tcqiSearch.setPfltId(ud != null ? ud.getIntgUid() : null);
            tcqiSearch.setInputUserId(ud != null ? ud.getIntgUid() : null);
            tcqiSearch.setLatestUserId(ud != null ? ud.getIntgUid() : null);
            tcqiSearch.setRemoteAddr(request != null ? request.getRemoteAddr() : null);
            tcqiService.saveMcqiSectList(tcqiSearch, list);
        } catch (ServiceException e) {
            result.put("msg", e.getMessage());
        } catch (Exception e) {
            log.error("[McqiController] saveMcqiSect 오류", e);
            result.put("msg", "저장 중 오류가 발생했습니다.");
        }
        return result;
    }

    /**
     * 교양 CQI 조회 공통 기본값 처리.
     * - collCd 미전달 시 교양 기본 조직코드로 세팅
     * - JSP는 collCd, detMngtDscCd만 보내므로 Mapper용 orgid/pfltId에 매핑
     */
    private void prepareLcqiSearchDefaults(TcqiSearch tcqiSearch) {
        if (tcqiSearch.getCollCd() == null || tcqiSearch.getCollCd().isEmpty()) {
            tcqiSearch.setCollCd(LCQI_DEFAULT_COLL_CD);
        }
        if (tcqiSearch.getOrgid() == null || tcqiSearch.getOrgid().isEmpty()) {
            tcqiSearch.setOrgid(tcqiSearch.getCollCd());
        }
        // pfltId는 항상 교번(사용자 ID) 기준으로 세팅되며,
        // 조회 시에도 외부에서 명시적으로 전달받은 값을 그대로 사용한다.
    }


    /* ================================================================
       [공통] 교양 CQI 제출 / 제출취소 / 마감
       기존 saveMcqiMasterSubmit.do / saveMcqiMasterSubmitCnl.do 재사용
       McqiMaster.cqiDivCd = '0002' 를 JSP에서 hidden으로 전달
    ================================================================ */
    // → 기존 saveMcqiMasterSubmit.do, saveMcqiMasterSubmitCnl.do,
    //   saveMcqiMasterClose.do 를 그대로 사용 (cqiDivCd 파라미터만 다름)


    /* ================================================================
       Private 헬퍼 메서드
    ================================================================ */

    /**
     * 기준업무일정에서 기준년도를 가져온다.
     * 없으면 null 반환 (JSP 에서 직접 입력 유도).
     */
    private String findBasYy(String dtlWokId) {
        BmgtSearch s = new BmgtSearch();
        s.setDtlWokId(dtlWokId);
        List<CamelCaseMap> list = bmgtService.findBachDtlWokDscList(s);
        return (!list.isEmpty()) ? list.get(0).getString("basYy") : null;
    }

    /**
     * 업무 일정 카운트 조회 (0이면 작성 기간 외).
     * 기존 전공/교양 공통으로 사용.
     */
    private int findSchedCnt(TcqiSearch tcqiSearch, UserDetails userDetails, String dtlWokId) {
        if (tcqiSearch == null || userDetails == null
                || tcqiSearch.getCollCd() == null || tcqiSearch.getDeptLoctCd() == null
                || tcqiSearch.getYy() == null) {
            return 0;
        }
        String resolvedDtlWokId = dtlWokId;
        /*if (resolvedDtlWokId == null || "".equals(resolvedDtlWokId)) {
            // fallback: 교양 구분값이 있으면 263, 그 외 전공 251
            resolvedDtlWokId = (tcqiSearch.getDetMngtDscCd() != null && !"".equals(tcqiSearch.getDetMngtDscCd()))
                    ? BASE_WOK_ID_263 : BASE_WOK_ID_251;
            log.warn("[McqiController] findSchedCnt dtlWokId null/blank. fallback dtlWokId={}", resolvedDtlWokId);
        }*/
        BmgtSearch bmgtSearch = new BmgtSearch();
        bmgtSearch.setDtlWokId(resolvedDtlWokId);
        bmgtSearch.setDateCk("1");
        bmgtSearch.setIsWeb("Y");
        bmgtSearch.setOrgid("2000000989");
        bmgtSearch.setLesnPlcCd("1");
        return bmgtService.findBachSchedDscList(bmgtSearch).size();
    }
}
