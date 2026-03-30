package kr.dku.univ.lssn.ccmg.web;

import kr.dku.base.security.SecurityContextHelper;
import kr.dku.base.security.access.annotation.Secured;
import kr.dku.base.security.access.annotation.SecurityPolicy;
import kr.dku.base.security.authentication.UserDetails;
import kr.dku.base.servlet.BaseController;
import kr.dku.base.util.CamelCaseMap;
import kr.dku.comm.syst.cdmg.domain.Org;
import kr.dku.comm.syst.cdmg.service.CodeService;
import kr.dku.comm.syst.cdmg.web.CommCodeDataSearch;
import kr.dku.univ.lssn.ccmg.domain.*;
import kr.dku.univ.lssn.ccmg.service.CcmgService;
import kr.dku.univ.lssn.lsbm.service.LsbmService;
import kr.dku.univ.lssn.sbmg.service.SbmgService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @author 여창기
 * @version 1.0
 * <pre>
 * 수정일        수정자        수정내용
 * ---------------------------------------------------------------------
 * 2014-10-10  여창기        최초작성
 * 2015-03-21  김도연        전공교육과정신청, 승인 관련 수정
 * </pre>
 */
@Controller
@RequestMapping(value = "/univ/lssn/ccmg")
@SessionAttributes("ccmgSearch") // Web 전용
public class CcmgController extends BaseController {
    private CcmgService ccmgService;
    private SbmgService sbmgService;
    private CodeService codeService;
    private LsbmService lsbmService;

    @Autowired
    public void setCcmgService(CcmgService ccmgService) {
        this.ccmgService = ccmgService;
    }

    @Autowired
    public void setSbmgService(SbmgService sbmgService) {
        this.sbmgService = sbmgService;
    }

    @Autowired
   	public void setCodeService(CodeService codeService) {
   		this.codeService = codeService;
   	}

    /**
     * 교양개설신청내역을(를) 조회한다.
     */
    @RequestMapping(value = "/findCulOpAplDscList.do")
    public void findCulOpAplDscList(CcmgSearch culOpAplDscSearch, Model model) {
        model.addAttribute("culOpAplDscList", ccmgService.findCulOpAplDscList(culOpAplDscSearch));
    }


    /**
     * 교양개설신청내역을(를) 저장한다.
     * @param culOpAplDscListWrapper 교양개설신청내역 목록정보
     */
    @RequestMapping(value = "/saveCulOpAplDsc.do")
    public void saveCulOpAplDsc(CulOpAplDscListWrapper culOpAplDscListWrapper) {
        ccmgService.saveCulOpAplDsc(culOpAplDscListWrapper.getCulOpAplDscList());
    }

    /**
     * 교양개설신청내역을(를) 삭제한다.
     * @param orgid 조직ID
     * @param yy 년도
     * @param subjId 교과목ID
     */
    @RequestMapping(value = "/deleteCulOpAplDsc.do")
    public void deleteCulOpAplDsc(@RequestParam String yy, @RequestParam String orgid, @RequestParam String subjId){
    	ccmgService.deleteCulOpAplDsc(yy, orgid, subjId);
    }

    /**
     * 교양개설평가자(를) 조회한다.
     * @param yy 년도
     * @param curiSemCd 학기코드
     * @param orgid 교과목 조직ID
     */
    @RequestMapping(value = "/findCulEvalrMngtList.do")
    public void findCulEvalrMngtList(CusEvalrSearch cusEvalrSearch, Model model) {
        model.addAttribute("findCulEvalrMngtList", ccmgService.findCulEvalrMngtList(cusEvalrSearch));
    }

    /**
     * 교양개설강좌평가내역을(를) 저장한다.
     * @param CulEvalrMngtListWrapper 교양개설강좌평가내역 목록정보
     */
    @RequestMapping(value = "/saveCulEvalrMngt.do")
    public void saveCulEvalrMngt(CulEvalrMngtListWrapper CulEvalrMngtListWrapper) {
        ccmgService.saveCulEvalrMngt(CulEvalrMngtListWrapper.getCulEvalrMngtList());
    }


    /**
     * 교양개설평가자(를) 조회한다.
     * @param yy 년도
     * @param curiSemCd 학기코드
     * @param orgid 교과목 조직ID
     */
    @RequestMapping(value = "/findCulOpLtrEvalrList.do")
    public void findCulOpLtrEvarlList(CusEvalrSearch cusEvalrSearch, Model model) {
        model.addAttribute("culOpLtrEvalrList", ccmgService.findCulOpLtrEvalrList(cusEvalrSearch));
    }

    /**
     * 교양개설신청내역을(를) 승인한다.
     * @param culOpAplDscApprListWrapper 교양개설신청내역 목록정보
     */
    @RequestMapping(value = "/saveCulOpAplApprdDsc.do")
    public void saveCulOpAplApprdDsc(CulOpAplDscListWrapper culOpAplDscListWrapper) {
        ccmgService.saveCulOpAplApprdDsc(culOpAplDscListWrapper.getCulOpAplDscList());
    }



    /**
     * 교육과정기본을(를) 조회한다.
     * @param yy 년도
     * @param semCd 학기코드
     * @param orgid 조직ID
     * @param subjId 교과목ID
     */
    @RequestMapping(value = "/findCuriBasList.do")
    public void findCuriBasList(CcmgSearch ccmgSearch, Model model) {
        model.addAttribute("curiBasList", ccmgService.findCuriBasList(ccmgSearch));
    }



    /**
     * 교육과정기본을(를) 저장한다.
     * @param curiBasListWrapper 교육과정기본 목록정보
     */
    @RequestMapping(value = "/saveCuriBas.do")
    public void saveCuriBas(CuriBasListWrapper curiBasListWrapper) {
        ccmgService.saveCuriBas(curiBasListWrapper.getCuriBasList());
    }


    /**
     * 교과목기본을(를) 조회한다.
     * @param subjId 교과목ID
     */
    @RequestMapping(value = "/findSubjBasList.do")
    public void findSubjBasList(CcmgSearch ccmgSearch, Model model) {
        model.addAttribute("subjBasList", ccmgService.findSubjBasList(ccmgSearch));
    }

    /**
     * 교육과정수강기본을(를) 조회한다.
     * @param orgid 조직ID
     * @param yy 년도
     * @param semCd 학기코드
     * @param subjId 교과목ID
     * @param bssSchgr 기준학년
     */
    @RequestMapping(value = "/findCuriTkcrsBasList.do")
    public void findCuriTkcrsBasList(CcmgSearch ccmgSearch, Model model) {
        model.addAttribute("curiTkcrsBasList", ccmgService.findCuriTkcrsBasList(ccmgSearch));
    }


    /**
     * 교육과정수강기본을(를) 저장한다.
     * @param curiTkcrsBasListWrapper 교육과정수강기본 목록정보
     */
    @RequestMapping(value = "/saveCuriTkcrsBas.do")
    public void saveCuriTkcrsBas(CuriTkcrsBasListWrapper curiTkcrsBasListWrapper) {
    	ccmgService.saveCuriTkcrsBas(curiTkcrsBasListWrapper.getCuriTkcrsBasList());
    }
    /**
     * 교육과정수강기본을(를) 조회한다.
     * @param orgid 조직ID
     * @param yy 년도
     * @param semCd 학기코드
     * @param subjId 교과목ID
     * @param bssSchgr 기준학년
     */
    @RequestMapping(value = "/findCuriCoreAblySetup.do")
    public void findCuriCoreAblySetup(CcmgSearch ccmgSearch, Model model) {
        model.addAttribute("curiCoreAblySetup", ccmgService.findCuriCoreAblySetup(ccmgSearch));
    }

    /**
     * 교육과정핵심역량 (웅비, 자유교과 ) 학사팀만 사용 2022.01.07
     * @param orgid 조직ID
     * @param yy 년도
     * @param semCd 학기코드
     * @param subjId 교과목ID
     * @param bssSchgr 기준학년
     */
    @RequestMapping(value = "/findCuriCoreAblySetupStu.do")
    public void findCuriCoreAblySetupStu(CcmgSearch ccmgSearch, Model model) {
        model.addAttribute("curiCoreAblySetupStu", ccmgService.findCuriCoreAblySetupStu(ccmgSearch));
    }

    /**
     * 교육과정핵심역량 설정을  조회한다.
     * @param orgid 조직ID
     * @param yy 년도
     * @param semCd 학기코드
     * @param subjId 교과목ID
     * @param bssSchgr 기준학년
     */
    @RequestMapping(value = "/findCuriCoreAblySetupList.do")
    public void findCuriCoreAblySetupList(CcmgSearch ccmgSearch, Model model) {
        model.addAttribute("curiCoreAblySetupList", ccmgService.findCuriCoreAblySetupList(ccmgSearch));
    }



    /**
     * 교육과정수강기본을(를) 저장한다.
     * @param curiTkcrsBasListWrapper 교육과정수강기본 목록정보
     */
    @RequestMapping(value = "/saveCuriCoreAblySetup.do")
    public void saveCuriCoreAblySetup(CuriTkcrsBasListWrapper curiTkcrsBasListWrapper) {
    	ccmgService.saveCuriCoreAblySetup(curiTkcrsBasListWrapper.getCuriTkcrsBasList());
    }


    /**
     * 교육과정수강기본을(를) 저장한다.
     * @param curiTkcrsBasListWrapper 교육과정수강기본 목록정보
     */
    @RequestMapping(value = "/copyCuriCoreAblySetup.do")
    public void copyCuriCoreAblySetup(CcmgSearch ccmgSearch, Model model) {

    	ccmgService.copyCuriCoreAblySetup(ccmgSearch);
    }




    /**
     * 동일과목기본을(를) 조회한다.
     * @param subjId 교과목ID
     * @param sameSubjId 동일교과목ID
     */
    @RequestMapping(value = "/findSameSbjBasList.do")
    public void findSameSbjBasList(CcmgSearch ccmgSearch, Model model) {
        model.addAttribute("sameSbjBasList", ccmgService.findSameSbjBasList(ccmgSearch));
    }


    /**
     * 동일과목기본을(를) 저장한다.
     * @param sameSbjBasListWrapper 동일과목기본 목록정보
     */
    @RequestMapping(value = "/saveSameSbjBas.do")
    public void saveSameSbjBas(SameSbjBasListWrapper sameSbjBasListWrapper) {
        ccmgService.saveSameSbjBas(sameSbjBasListWrapper.getSameSbjBasList());
    }



    /**
     * 교육과정을(를) 복사한다.
     * @param ccmgSearch 조건
     */
    @RequestMapping(value = "/saveCuriBasCopy.do")
    public void saveCuriBasCopy(CcmgCopy ccmgCopy, Model model) {
    	ccmgService.saveCuriBasCopy(ccmgCopy);
    }

    /**
     * 교육과정을(를) 복사한다.
     * @param ccmgSearch 조건
     */
    @RequestMapping(value = "/saveCuriAplyBasCopy.do")
    public void saveCuriAplyBasCopy(CcmgCopy ccmgCopy, Model model) {
    	ccmgService.saveCuriAplyBasCopy(ccmgCopy);
    }



    /**
     * 교육과정기본을(를) 조회한다.
     * @param yy 년도
     * @param semCd 학기코드
     * @param orgid 조직ID
     * @param subjId 교과목ID
     */
    @RequestMapping(value = "/findSameCuriBas.do")
    public void findSameCuriBas(CcmgSearch ccmgSearch, Model model) {
        model.addAttribute("sameCuriBasList", ccmgService.findSameCuriBas(ccmgSearch));
    }



    /**
     * 전공 교육과정 신청을(를) 조회한다.
     * @param yy 년도
     * @param semCd 학기코드
     * @param orgid 조직ID
     * @param subjId 교과목ID
     */
    @RequestMapping(value = "/findNewCuriBasList.do")
    public void findNewCuriBasList(CcmgSearch ccmgSearch, Model model) {
        model.addAttribute("newCuriBasList", ccmgService.findNewCuriBasList(ccmgSearch));
    }



    /**
     * 전공 교육과정 신청을(를) 저장한다.
     * @param curiBasListWrapper 교육과정기본 목록정보
     */
    @RequestMapping(value = "/saveNewCuriBas.do")
    public void saveNewCuriBas(CuriAplyBasListWrapper curiAplyBasListWrapper) {
        ccmgService.saveNewCuriBas(curiAplyBasListWrapper.getCuriAplyBasList());
    }


    /**
     * 전공 교육과정 승인처리를(을) 조회한다.
     * @param yy 년도
     * @param semCd 학기코드
     * @param orgid 조직ID
     * @param subjId 교과목ID
     */
    @RequestMapping(value = "/findApvNewCuriBasList.do")
    public void findApvNewCuriBasList(CcmgSearch ccmgSearch, Model model) {
        model.addAttribute("apvNewCuriBasList", ccmgService.findApvNewCuriBasList(ccmgSearch));
    }



    /**
     * 전공 교육과정 승인처리를(을) 저장한다.
     * @param curiBasListWrapper 교육과정기본 목록정보
     */
    @RequestMapping(value = "/saveApvNewCuriBas.do")
    public void saveApvNewCuriBas(CuriAplyBasListWrapper curiAplyBasListWrapper,@RequestParam String apvSxnCd) {
        ccmgService.saveApvNewCuriBas(curiAplyBasListWrapper.getCuriAplyBasList(),apvSxnCd);
    }


    /**
     * 교육과정수강기본을(를) 조회한다.
     * @param orgid 조직ID
     * @param yy 년도
     * @param semCd 학기코드
     * @param subjId 교과목ID
     * @param bssSchgr 기준학년
     */
    @RequestMapping(value = "/findCuriTkcrsBas.do")
    public void findCuriTkcrsBas(@RequestParam String yy, @RequestParam String semCd, @RequestParam String deptLoctCd, @RequestParam String orgid, @RequestParam String subjId, Model model) {
        model.addAttribute("curiTkcrsBas", ccmgService.findCuriTkcrsBas(yy, semCd, deptLoctCd, orgid, subjId));
    }

    /**
     * 교육과정 일괄처리 프로시저를(을) 저장한다.
     * @param curiBas
     */
    @RequestMapping(value = "/saveCuriBatApvProc.do")
    public void saveCuriBatApvProc(CuriBas curiBas) {
        ccmgService.saveCuriBatApvProc(curiBas);
    }

    /**
     * 교육과정제안기준정보을(를) 조회한다.
     * @param aplYy 신청년도
     * @param aplSemCd 신청학기
     * @param deptLoctCd 부서위치코드
     * @param curiCparCd 교육과정이수영역
     */
    @RequestMapping(value = "/findCuriAplyBssList.do")
    public void findCuriAplyBssList(CcmgSearch ccmgSearch, Model model) {
        model.addAttribute("curiAplyBssList", ccmgService.findCuriAplyBssList(ccmgSearch));
    }

    /**
     * 교육과정제안기준정보을(를) 저장한다.
     * @param curiAplyBssListWrapper 교육과정제안기준정보 목록정보
     */
    @RequestMapping(value = "/saveCuriAplyBss.do")
    public void saveCuriAplyBss(CuriAplyBssListWrapper curiAplyBssListWrapper) {
        ccmgService.saveCuriAplyBss(curiAplyBssListWrapper.getCuriAplyBssList());
    }

    /**
     * 직전학기 교육과정제안기준정보을(를) 복사한다.
     */
    @RequestMapping(value = "/saveCuriAplyCopy.do")
    public void saveCuriAplyCopy(CuriAplyBss curiAplyBss) {
        ccmgService.saveCuriAplyCopy(curiAplyBss);
    }

    /**
     * 교육과정신청기준 화면에서 이수영역팝업을 조회한다.
     */
    @RequestMapping(value = "/findCulCuriCparCdBssList.do")
    public void findCulCuriCparCdBssList(@RequestParam String curiCparNm, Model model) {
        model.addAttribute("curiCparCdList", ccmgService.findCulCuriCparCdBssList(curiCparNm));
    }

    /**
     * 교육과정 신청리스트를 조회한다.  20210914 교양교육과정 제안서 및 개설신청서 등록
     */
    @RequestMapping(value = "/views/findCuriAplyBasList.do")
    public String findCuriAplyBasList(CcmgSearch ccmgSearch, Model model) {
    	UserDetails userDetails = SecurityContextHelper.getUserDetails();
    	/* 등록기간 체크 */
    	CamelCaseMap cm = sbmgService.findDtlWokDsc("0000000158");
		if(cm.get("yy") !=null && cm.get("yy") != "")  // 해당 등록기간인 경우 등록가능
			model.addAttribute("possibleTerm"	, "Y");
		else
			model.addAttribute("possibleTerm"	, "N");

		if(ccmgSearch.getAplYy() == null) {   // 화면 년도 default값 세팅
			ccmgSearch.setAplYy(cm.get("yy") != null ? cm.get("yy").toString() : "");
		    ccmgSearch.setAplSemCd(cm.get("semCd") != null ? cm.get("semCd").toString() : "");
		}



		//조회조건 콤보세팅
		CommCodeDataSearch codeDataSearch = new CommCodeDataSearch();
		codeDataSearch.setCmnCdId("SEM_CD");
		codeDataSearch.setAddtInfoValu1("1");
		model.addAttribute("semCdList"		, codeService.findCommCodeDataList(codeDataSearch));		// 학기구분

		ccmgSearch.setEmpid(userDetails.getIntgUid());
		ccmgSearch.setRmk("2");  /* 20210914 교양교육과정 화면 -교양팀 사용  값 2 */
        model.addAttribute("curiAplyBasList", ccmgService.findCuriAplyBasList(ccmgSearch));


        return "univ/lssn/ccmg/curiAplyBasList";
    }



    /**
     * 교육과정 신청리스트를 조회한다.   웅비 교육과정  20210914 교양교육과정 제안서 및 개설신청서 등록
     */
    @RequestMapping(value = "/views/findCuriAplyBasList1.do")
    public String findCuriAplyBasList1(CcmgSearch ccmgSearch, Model model) {
    	UserDetails userDetails = SecurityContextHelper.getUserDetails();
    	/* 등록기간 체크 */
    	CamelCaseMap cm = sbmgService.findDtlWokDsc("0000000224");
		if(cm.get("yy") !=null && cm.get("yy") != "")  // 해당 등록기간인 경우 등록가능
			model.addAttribute("possibleTerm"	, "Y");
		else
			model.addAttribute("possibleTerm"	, "N");


		if(ccmgSearch.getAplYy() == null) {   // 화면 년도 default값 세팅
			ccmgSearch.setAplYy(cm.get("yy") != null ? cm.get("yy").toString() : "");
		    ccmgSearch.setAplSemCd(cm.get("semCd") != null ? cm.get("semCd").toString() : "");
		}
		//조회조건 콤보세팅
		CommCodeDataSearch codeDataSearch = new CommCodeDataSearch();
		codeDataSearch.setCmnCdId("SEM_CD");
		codeDataSearch.setAddtInfoValu1("1");
		model.addAttribute("semCdList"		, codeService.findCommCodeDataList(codeDataSearch));		// 학기구분

		ccmgSearch.setEmpid(userDetails.getIntgUid());

		ccmgSearch.setRmk("1");  /* 20210914 웅비 화면 -학사팀 사용  값 1 */
        model.addAttribute("curiAplyBasList", ccmgService.findCuriAplyBasList(ccmgSearch));


        return "univ/lssn/ccmg/curiAplyBasList1";
    }


    /**
     * 교육과정 신청 폼을 조회한다.   교양교육과정 (영역 )   rmk 2  학사팀   (   20241023  기존의 교양교육과정- 자유교양팀이 관리하던걸  교양교육과정(영역)으로 명칭변경 및 학사팀이 받음  . 윤나영 담당 )
     */
    @RequestMapping(value = "/views/findCuriAplyBasForm.do")
    public String findCuriAplyBasForm(CcmgSearch ccmgSearch, Model model) {
    	UserDetails userDetails = SecurityContextHelper.getUserDetails();
    	ccmgSearch.setEmpid(userDetails.getIntgUid());
    	ccmgSearch.setOrgid(userDetails.getOrgId());
    	//ccmgSearch.setPpsKndCd("10");

    	/* 등록기간 체크 */
    	CamelCaseMap cm = sbmgService.findDtlWokDsc("0000000158");
		if(cm.get("yy") !=null && cm.get("yy") != "")  // 해당 등록기간인 경우 등록가능
			model.addAttribute("possibleTerm"	, "Y");
		else
			model.addAttribute("possibleTerm"	, "N");

		if(ccmgSearch.getAplYy() == null) {   // 화면 년도 default값 세팅
			ccmgSearch.setAplYy(cm.get("yy") != null ? cm.get("yy").toString() : "");
		    ccmgSearch.setAplSemCd(cm.get("semCd") != null ? cm.get("semCd").toString() : "");
		}

/*		ccmgSearch.setAplYy("2024");
		ccmgSearch.setAplSemCd("2");*/


    	// 장소구분코드
    	CommCodeDataSearch codeDataSearch = new CommCodeDataSearch();
    	codeDataSearch.setCmnCdId("EDU_LOCT_CD");
		//codeDataSearch.setAddtInfoValu1("1");
		model.addAttribute("deptLoctCdList", codeService.findCommCodeDataList(codeDataSearch));     // 캠퍼스 코드

		codeDataSearch.setCmnCdId("PURP_CLSF_CD");
		codeDataSearch.setAddtInfoValu1(null);
		model.addAttribute("purpClsfCdList", codeService.findCommCodeDataList(codeDataSearch));     // 특정목적분류코드


		codeDataSearch.setCmnCdId("CURI_SEM_CD");        // 20241022 윤나영 요청으로 수업 전용 코드 따서 넣음. 전체학기 없게.
		model.addAttribute("semSxnCdList", codeService.findCommCodeDataList(codeDataSearch));     // 학기구분코드


		codeDataSearch.setCmnCdId("TXTBK_TY_CD");
		model.addAttribute("txtbkTyCdList", codeService.findCommCodeDataList(codeDataSearch));     // 교재구분코드

		codeDataSearch.setCmnCdId("DOW_CD");
		model.addAttribute("aplyDowCdList", codeService.findCommCodeDataList(codeDataSearch));     // 요일구분코드

		codeDataSearch.setCmnCdId("LCT_PROD_CD");
		model.addAttribute("lctProdCdList", codeService.findCommCodeDataList(codeDataSearch));     // 교시구분코드


		codeDataSearch.setCmnCdId("ROOM_TY_CD");
		model.addAttribute("lecrmTypNmList", codeService.findCommCodeDataList(codeDataSearch));//  강의실 형태

		// 교양교육 (교양팀 박광현 관리 화면 ) 이라서  이수영역 리스트 중 자유교과 제외

		ccmgSearch.setRmk("2");
		model.addAttribute("curiAplyBssList", ccmgService.findCuriAplyBssList(ccmgSearch));    // 교육과정이수영역


        model.addAttribute("basInfo", ccmgService.findBasInfo(ccmgSearch));                    // 교수정보(제안자)


        model.addAttribute("aplyOrgid", ccmgService.findAplyOrgid(ccmgSearch));                    // 심의단과대학 20241022 윤나영 요청 추가


        CamelCaseMap ca = ccmgService.findCuriAplyDsc(ccmgSearch);

        codeDataSearch.setCmnCdId("D3_CORE_SXN_CD");
        model.addAttribute("d3CoreSxnCd", codeService.findCommCodeDataList(codeDataSearch));     // D3혁신역량구분

        model.addAttribute("curiAplyCoreList", ccmgService.findCuriAplyCoreList(ccmgSearch));

        model.addAttribute("basSubjInfo", ca);            // 교과목 세부사항 & 교과목 해설

        model.addAttribute("curiAplySchedDscList", ccmgService.findCuriAplySchedDscList(ccmgSearch));   // 강의일정
        model.addAttribute("curiAplyTxtbkDscList", ccmgService.findCuriAplyTxtbkDscList(ccmgSearch));   // 교재 참고 문헌

        model.addAttribute("status", ccmgSearch.getPpsKndCd() != null ? "4" : "2" );   // 신규


        if(ccmgSearch.getPpsKndCd() != null){
        	ccmgSearch.setCuriCparCd(ca.getString("curiCparCd"));
        	model.addAttribute("crdList", ccmgService.findCrdList(ccmgSearch));
        	model.addAttribute("tkcrsNoperList", ccmgService.findTkcrsNoperList(ccmgSearch));

        	model.addAttribute("dayPosbList", ccmgService.findDayPosbList(ccmgSearch));
        }


        return "univ/lssn/ccmg/curiAplyBasForm";
    }



    /**
     * 교육과정 신청 폼을 조회한다.   20241023 교양교육과정(선택)    --  2024 이전  웅비교육과정 rmk 1  학사팀 이   교양교육과정(선택)으로 명칭 변경
     */
    @RequestMapping(value = "/views/findCuriAplyBasForm1.do")
    public String findCuriAplyBasForm1(CcmgSearch ccmgSearch, Model model) {
    	UserDetails userDetails = SecurityContextHelper.getUserDetails();
    	ccmgSearch.setEmpid(userDetails.getIntgUid());
    	ccmgSearch.setOrgid(userDetails.getOrgId());
    	//ccmgSearch.setPpsKndCd("10");

    	/* 등록기간 체크 */
    	CamelCaseMap cm = sbmgService.findDtlWokDsc("0000000224");
		if(cm.get("yy") !=null && cm.get("yy") != "")  // 해당 등록기간인 경우 등록가능
			model.addAttribute("possibleTerm"	, "Y");
		else
			model.addAttribute("possibleTerm"	, "N");

		if(ccmgSearch.getAplYy() == null) {   // 화면 년도 default값 세팅
			ccmgSearch.setAplYy(cm.get("yy") != null ? cm.get("yy").toString() : "");
		    ccmgSearch.setAplSemCd(cm.get("semCd") != null ? cm.get("semCd").toString() : "");
		}

    	// 장소구분코드
    	CommCodeDataSearch codeDataSearch = new CommCodeDataSearch();
    	codeDataSearch.setCmnCdId("EDU_LOCT_CD");
		//codeDataSearch.setAddtInfoValu1("1");
		model.addAttribute("deptLoctCdList", codeService.findCommCodeDataList(codeDataSearch));     // 캠퍼스 코드

		codeDataSearch.setCmnCdId("PURP_CLSF_CD");
		codeDataSearch.setAddtInfoValu1(null);
		model.addAttribute("purpClsfCdList", codeService.findCommCodeDataList(codeDataSearch));     // 특정목적분류코드


		codeDataSearch.setCmnCdId("CURI_SEM_CD");        // 20241022 윤나영 요청으로 수업 전용 코드 따서 넣음. 전체학기 없게.
		model.addAttribute("semSxnCdList", codeService.findCommCodeDataList(codeDataSearch));     // 학기구분코드


		codeDataSearch.setCmnCdId("TXTBK_TY_CD");
		model.addAttribute("txtbkTyCdList", codeService.findCommCodeDataList(codeDataSearch));     // 교재구분코드

		codeDataSearch.setCmnCdId("DOW_CD");
		model.addAttribute("aplyDowCdList", codeService.findCommCodeDataList(codeDataSearch));     // 요일구분코드

		codeDataSearch.setCmnCdId("LCT_PROD_CD");
		model.addAttribute("lctProdCdList", codeService.findCommCodeDataList(codeDataSearch));     // 교시구분코드


		codeDataSearch.setCmnCdId("ROOM_TY_CD");
		model.addAttribute("lecrmTypNmList", codeService.findCommCodeDataList(codeDataSearch));//  강의실 형태


		 // 웅비 (학사팀 관리 화면 ) 이라서  이수영역 리스트 중 자유교과 제외   20210916
		ccmgSearch.setRmk("1");


		model.addAttribute("curiAplyBssList", ccmgService.findCuriAplyBssList(ccmgSearch));    // 교육과정이수영역









        model.addAttribute("basInfo", ccmgService.findBasInfo(ccmgSearch));                    // 교수정보(제안자)
        CamelCaseMap ca = ccmgService.findCuriAplyDsc(ccmgSearch);
        if (ca != null && ca.getString("aplSeq") != null) {
            ccmgSearch.setAplSeq(ca.getString("aplSeq"));
            ccmgSearch.setPpsKndCd(ca.getString("ppsKndCd"));
        }

        model.addAttribute("basSubjInfo", ca);            // 교과목 세부사항 & 교과목 해설

        model.addAttribute("curiAplyCoreList", ccmgService.findCuriAplyCoreList(ccmgSearch));

        model.addAttribute("curiAplySchedDscList", ccmgService.findCuriAplySchedDscList(ccmgSearch));   // 강의일정
        model.addAttribute("curiAplyTxtbkDscList", ccmgService.findCuriAplyTxtbkDscList(ccmgSearch));   // 교재 참고 문헌

        model.addAttribute("status", ccmgSearch.getPpsKndCd() != null ? "4" : "2" );   // 신규

        model.addAttribute("crdList", ccmgService.findCrdList(ccmgSearch));


        codeDataSearch.setCmnCdId("D3_CORE_SXN_CD");
        model.addAttribute("d3CoreSxnCd", codeService.findCommCodeDataList(codeDataSearch));     // D3혁신역량구분


        codeDataSearch.setCmnCdId("CURI_AFFLT_CD");
        model.addAttribute("curiAffltCdList", codeService.findCommCodeDataList(codeDataSearch));     // 심의계열코드

        if(ccmgSearch.getPpsKndCd() != null){
        	ccmgSearch.setCuriCparCd(ca.getString("curiCparCd"));

        	model.addAttribute("tkcrsNoperList", ccmgService.findTkcrsNoperList(ccmgSearch));

        	model.addAttribute("dayPosbList", ccmgService.findDayPosbList(ccmgSearch));
        }


        return "univ/lssn/ccmg/curiAplyBasForm1";
    }




    /**
     * 교양교육과정신청기본을(를) 조회한다.
     * @param aplYy 신청년도
     * @param aplSemCd 신청학기
     * @param deptLoctCd 부서위치코드
     * @param aplOrgid 신청조직ID
     * @param mngtNb 관리번호
     */
    @RequestMapping(value = "/findCulCuriAplyBasList.do")
    public void findCulCuriAplyBasList(CcmgSearch ccmgSearch, Model model) {
        model.addAttribute("culCuriAplyBasList", ccmgService.findCulCuriAplyBasList(ccmgSearch));
    }

    /**
     * 교양교육과정이수영역코드 팝업리스트 조회한다.(교양)
     * @param strType 선택구분
     */
    @RequestMapping(value = "/findCulCuriCparCdPopList.do")
    public void findCulCuriCparCdPopList(@RequestParam String strType, @RequestParam String curiCparNm, @RequestParam String aplYy, @RequestParam String aplSemCd, @RequestParam String deptLoctCd, Model model) {
        model.addAttribute("curiCparCdList", ccmgService.findCulCuriCparCdPopList(strType,curiCparNm,aplYy,aplSemCd,deptLoctCd));
    }

    /**
     * 교양교육과정이수영역코드 팝업리스트 조회한다.(전공)
     * @param strType 선택구분
     */
    @RequestMapping(value = "/findMjCuriCparCdPopList.do")
    public void findMjCuriCparCdPopList(@RequestParam String strType, @RequestParam String curiCparNm, Model model) {
        model.addAttribute("curiCparCdList", ccmgService.findMjCuriCparCdPopList(strType,curiCparNm));
    }

    /**
     * 교양교육과정신청기본을(를) 저장한다.
     * @param strType 선택구분
     */
    @RequestMapping(value = "/saveCulCuriAplyBas.do")
    public void saveCulCuriAplyBas(CuriAplyBasListWrapper curiAplyBasListWrapper) {
        ccmgService.saveCulCuriAplyBas(curiAplyBasListWrapper.getCuriAplyBasList());
    }

    /**
     * 교양교육과정신청화면에서 제안서, 개설서 버튼 컨트롤 조회한다.
     * @param strType 선택구분
     */
    @RequestMapping(value = "/findCulCuriAplyBtnCtrl.do")
    public void findCulCuriAplyBtnCtrl(@RequestParam String aplYy, @RequestParam String aplSemCd, @RequestParam int aplSeq, Model model) {
        model.addAttribute("culCuriAplyBtnCtrlList", ccmgService.findCulCuriAplyBtnCtrl(aplYy,aplSemCd,aplSeq));
    }

    /**
     * 교육과정제안내역을(를) 조회한다.(제안서팝업)
     * @param aplYy 신청년도
     * @param aplSemCd 신청학기
     * @param deptLoctCd 부서위치코드
     * @param aplOrgid 신청조직ID
     * @param mngtNb 관리번호
     * @param ppsKndCd 제안종류코드
     */
    @RequestMapping(value = "/findPrplOpplCuriAplyDscList.do")
    public void findPrplOpplCuriAplyDscList(CcmgSearch ccmgSearch, Model model) {
        model.addAttribute("prplOpplCuriAplyDscList", ccmgService.findPrplOpplCuriAplyDscList(ccmgSearch));
    }

    /**
     * 교육과정교재내역을(를) 조회한다.(제안서팝업)
     * @param aplYy 신청년도
     * @param aplSemCd 신청학기
     * @param deptLoctCd 부서위치코드
     * @param aplOrgid 신청조직ID
     * @param mngtNb 관리번호
     * @param ppsKndCd 제안종류코드
     * @param seq 순번
     */
    @RequestMapping(value = "/findPrplOpplCuriAplyTxtbkDscList.do")
    public void findPrplOpplCuriAplyTxtbkDscList(@RequestParam String aplYy, @RequestParam String aplSemCd, @RequestParam int aplSeq, @RequestParam String ppsKndCd, Model model) {
        model.addAttribute("curiAplyTxtbkDscList", ccmgService.findPrplOpplCuriAplyTxtbkDscList(aplYy, aplSemCd, aplSeq, ppsKndCd));
    }
    /**
     * 교육과정강의일정내역을(를) 조회한다.(제안서팝업)
     * @param aplYy 신청년도
     * @param aplSemCd 신청학기
     * @param deptLoctCd 부서위치코드
     * @param aplOrgid 신청조직ID
     * @param mngtNb 관리번호
     * @param ppsKndCd 제안종류코드
     * @param wkSeq 주차
     */
    @RequestMapping(value = "/findPrplOpplCuriAplySchedDscList.do")
    public void findPrplOpplCuriAplySchedDscList(@RequestParam String aplYy, @RequestParam String aplSemCd, @RequestParam int aplSeq, @RequestParam String ppsKndCd, Model model) {
        model.addAttribute("curiAplySchedDscList", ccmgService.findPrplOpplCuriAplySchedDscList(aplYy, aplSemCd, aplSeq, ppsKndCd));
    }

    /**
     * 교육과정제안내역을(를) 저장한다. (제안서팝업)
     * @param curiAplyDscListWrapper 교육과정제안내역 목록정보
     */
    @RequestMapping(value = "/savePrplOpplCuriAplyDsc.do")
    public void savePrplOpplCuriAplyDsc(CuriAplyListWrapper curiAplyListWrapper) {
        ccmgService.savePrplOpplCuriAplyDsc(curiAplyListWrapper.getCuriAplyBas(), curiAplyListWrapper.getCuriAplyDsc(), curiAplyListWrapper.getCuriAplySchedDscList(), curiAplyListWrapper.getCuriAplyTxtbkDscList() );
    }


    /**
     * 교양교육과정신청기본을(를) 조회한다.(교양승인)
     * @param aplYy 신청년도
     * @param aplSemCd 신청학기
     * @param deptLoctCd 부서위치코드
     * @param aplOrgid 신청조직ID
     * @param mngtNb 관리번호
     */
    @RequestMapping(value = "/findApvCulCuriAplyBasList.do")
    public void findApvCulCuriAplyBasList(CcmgSearch ccmgSearch, Model model) {
        model.addAttribute("culCuriAplyBasList", ccmgService.findApvCulCuriAplyBasList(ccmgSearch));
    }

    /**
     * 교양교육과정신청기본을(를) 저장한다..(교양승인)
     * @param curiAplyDscListWrapper 교육과정제안내역 목록정보
     */
    @RequestMapping(value = "/saveApvCulCuriAplyBasList.do")
    public void saveApvCulCuriAplyBasList(CuriAplyListWrapper curiAplyListWrapper, @RequestParam String apvSxnCd) {
        ccmgService.saveApvCulCuriAplyBasList(curiAplyListWrapper.getCuriAplyDscList(), apvSxnCd);
    }

    /**
     * 교육과정제안기준정보을(를) 조회한다.
     * @param aplYy 신청년도
     * @param aplSemCd 신청학기
     * @param deptLoctCd 부서위치코드
     * @param curiCparCd 교육과정이수영역
     */
    @Secured(policy = SecurityPolicy.SESSION)
    @RequestMapping(value = "/findCuriAplyBssListCombo.do")
    public void findCuriAplyBssListCombo(CcmgSearch ccmgSearch, Model model) {
       model.addAttribute("curiAplyBssList", ccmgService.findCuriAplyBssList(ccmgSearch));
    }

    /**
	 * 교육과정제안기준정보을(를) 저장한다.
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/saveCuriAplyListWrapper.do")
	public String saveCuriAplyListWrapper(CuriAplyListWrapper curiAplyListWrapper) {
        ccmgService.saveCuriAplyListWrapper(curiAplyListWrapper);

		return "redirect:views/findCuriAplyBasList.do";
	}
    /**
	 * 교육과정제안기준정보을(를) 저장한다.   웅비교육과정
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/saveCuriAplyListWrapper1.do")
	public String saveCuriAplyListWrapper1(CuriAplyListWrapper curiAplyListWrapper) {
        ccmgService.saveCuriAplyListWrapper(curiAplyListWrapper);
		return "redirect:views/findCuriAplyBasList1.do";
	}

	@RequestMapping(value = "/deleteCuriAplyTxtbkDscList.do")
	public void deleteCuriAplyTxtbkDscList(CuriAplyTxtbkDsc curiAplyTxtbkDsc , Model model) {
		ccmgService.deleteCuriAplyTxtbkDsc(curiAplyTxtbkDsc);

		model.addAttribute("curiAplyTxtbkDscList", ccmgService.findCuriAplyTxtbkDscList(curiAplyTxtbkDsc));
	}

	@RequestMapping(value = "/findDuplCuriAplyBas.do")
	public void findDuplCuriAplyBas(CcmgSearch ccmgSearch, Model model) {

		model.addAttribute("duplCnt", ccmgService.findDuplCuriAplyBas(ccmgSearch));
	}

	/**
     * 교과목기본을(를) 조회한다.
     * @param subjId 교과목ID
     */
    @RequestMapping(value = "/findSubjectList.do")
    public void findSubjectList(CcmgSearch ccmgSearch, Model model) {
        model.addAttribute("subjBasList", ccmgService.findSubjectList(ccmgSearch));
    }
    /**
     * 교육과정제안 이수영역에 따른 기준정보를 조회한다.
     * @param subjId 교과목ID
     */
    @RequestMapping(value = "/findCuriAplyBssInfo.do")
    public void findCuriAplyBssInfo(CcmgSearch ccmgSearch, Model model){
    	model.addAttribute("curiAplyBssInfo", ccmgService.findCuriAplyBssInfo(ccmgSearch));
    	model.addAttribute("crdList", ccmgService.findCrdList(ccmgSearch));
    	model.addAttribute("tkcrsNoperList", ccmgService.findTkcrsNoperList(ccmgSearch));
    	model.addAttribute("dayPosbList", ccmgService.findDayPosbList(ccmgSearch));

    }

	/**
     * 교과목의 개설이력을(를) 조회한다.
     * @param subjId 교과목ID
     */
    @RequestMapping(value = "/findOpSubjList.do")
    public void findOpSubjList(CcmgSearch ccmgSearch, Model model) {
        model.addAttribute("opSubjList", ccmgService.findOpSubjList(ccmgSearch));
    }


	/**
     * 모듈,마이크로전공,트랙에 해당하는 목록을 가져온다.
     * @param subjId 교과목ID
     */
    @RequestMapping(value = "/findMmtOrgid.do")
    public void findMmtOrgid(CcmgSearch ccmgSearch, Model model) {
        model.addAttribute("mmtOrgid", ccmgService.findMmtOrgid(ccmgSearch));
    }

    @RequestMapping("/findDupSubjByCampBothSem.do")
    public void findDupSubjByCampBothSem(CcmgSearch ccmgSearch, Model model) {
        model.addAttribute("dupSubjYn", ccmgService.findDupSubjByCampBothSem(ccmgSearch));
    }

    @RequestMapping("/findDupSubjCrossCampSem.do")
    public void findDupSubjCrossCampSem(CcmgSearch ccmgSearch, Model model) {
        model.addAttribute("dupSubjYn", ccmgService.findDupSubjCrossCampSem(ccmgSearch));
    }

    /**
     * 핵심역량 목록을 조회한다.
     */
    @RequestMapping(value = "/findCoreBssList.do")
    public void findCoreBssList(CcmgSearch ccmgSearch, Model model) {
        model.addAttribute("coreBssList", ccmgService.findCoreBssList(ccmgSearch));
    }

    @RequestMapping(value = "/getNewAblyCd.do")
    public void getNewAblyCd(@RequestParam String ablyCd, Model model) {
        model.addAttribute("newAblyCd", ccmgService.getNewAblyCd(ablyCd));
    }

    /**
     * 핵심역량(를) 저장한다.
     * @param d3CoreAblyCd 핵심역량코드
     */
    @RequestMapping(value = "/insertD3CoreAblyCd.do")
    public void insertD3CoreAblyCd(D3CoreAblyBss d3CoreAblyBss) {
        ccmgService.insertD3CoreAblyCd(d3CoreAblyBss);
    }


    /**
     * 핵심역량(를) 수정한다.
     * @param d3CoreAblyCd 핵심역량코드
     */
    @RequestMapping(value = "/updateD3CoreAblyCd.do")
    public void updateD3CoreAblyCd(D3CoreAblyBss d3CoreAblyBss) {
        ccmgService.updateD3CoreAblyCd(d3CoreAblyBss);
    }


    /**
     * 핵심역량(를) 삭제한다.
     * @param d3CoreAblyCd 핵심역량코드
     */
    @RequestMapping(value = "/deleteD3CoreAblyCd.do")
    public void deleteD3CoreAblyCd(@RequestParam String d3CoreAblyCd) {
        ccmgService.deleteD3CoreAblyCd(d3CoreAblyCd);
    }



}
