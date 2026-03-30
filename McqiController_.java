package kr.dku.univ.mcqi.mcqi.web;

import java.util.List;

import kr.dku.base.security.SecurityContextHelper;
import kr.dku.base.security.authentication.UserDetails;
import kr.dku.base.service.ServiceException;
import kr.dku.base.servlet.BaseController;
import kr.dku.base.util.CamelCaseMap;
import kr.dku.univ.mcqi.mcqi.domain.McqiListWrapper;
import kr.dku.univ.mcqi.mcqi.service.McqiService;
import kr.dku.univ.mcqi.tcqi.domain.McqiContDsc;
import kr.dku.univ.mcqi.tcqi.service.TcqiService;
import kr.dku.univ.mcqi.tcqi.web.TcqiSearch;
import kr.dku.univ.srec.bmgt.service.BmgtService;
import kr.dku.univ.srec.bmgt.web.BmgtSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * @author 정보기획팀
 * @version 1.0
 * <pre>
 * 수정일                      수정자        수정내용
 * ---------------------------------------------------------------------
 * 2025-01-15   엄현수        최초작성
 * </pre>
 */
@Controller
@RequestMapping(value = "/univ/mcqi/mcqi")
public class McqiController extends BaseController {


    private McqiService mcqiService;
    private BmgtService bmgtService;
    private TcqiService tcqiService;


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




    /**
     * web 전공MACRO-CQI데이터확인을(를) 조회한다.
     * 2025.01.15
     */
    @RequestMapping(value = "/views/findMcqiContDscList.do")
    public String findMcqiContDscListView(BmgtSearch bmgtSearch, Model model) {

        if(bmgtSearch.getYy()==null){
            BmgtSearch bmgtSearch2 = new BmgtSearch();
            bmgtSearch2.setDtlWokId("0000000249");
            List<CamelCaseMap> bachSchedDscList = bmgtService.findBachDtlWokDscList(bmgtSearch2);
            if(bachSchedDscList.size() > 0) bmgtSearch.setYy(bachSchedDscList.get(0).getString("basYy"));
        }

        bmgtSearch.setPgmId("6830"); // Macro-CQI메뉴ID
        bmgtSearch.setUseYn("1"); //사용여부
        bmgtSearch.setPcondTyCd("0001"); //현황유형

        model.addAttribute("bachCmnPcondDscList", bmgtService.findBachCmnPcondDscList(bmgtSearch));
        model.addAttribute("bmgtSearch", bmgtSearch);

        return "univ/mcqi/mcqi/mcqiContDscList";
    }


    /**
     * web 교양-CQI데이터확인을(를) 조회한다.
     * 2026.02.11
     */
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
     * web 전공MACRO-CQI데이터확인을(를) 조회한다.
     * @param yy 년도
     * @param pcondSeq 조회조건
     * @param deptLoctCd 조직위치
     * @param model 조직 대분류
     * @param dpmtCd 조직 중분류
     * @param mjCd 조직 소분류
     */
    @RequestMapping(value = "/findMcqiContDscList.do")
    public void findMcqiContDscList(BmgtSearch bmgtSearch, Model model){
        int checknum = 1;

        try {
            model.addAttribute("returnMap", mcqiService.findMcqiContDscList(bmgtSearch));
        } catch(ServiceException se) {
            checknum = 2;
        }
        model.addAttribute("success", checknum);
    }

    /**
     * web 전공MACRO-CQI데이터 입력을(를) 조회한다.
     * 2025.01.17
     */
    @RequestMapping(value = "/views/findMcqiContDscForm.do")
    public String findMcqiContDscFormView(TcqiSearch tcqiSearch, Model model) {
        int cnt = 0;
        UserDetails userDetails = SecurityContextHelper.getUserDetails(); // 로그인사용자정보
        if(tcqiSearch.getYy()==null){
            BmgtSearch bmgtSearch = new BmgtSearch();
            bmgtSearch.setDtlWokId("0000000249");
            List<CamelCaseMap> bachSchedDscList = bmgtService.findBachDtlWokDscList(bmgtSearch);
            if(bachSchedDscList.size() > 0) tcqiSearch.setYy(bachSchedDscList.get(0).getString("basYy"));

            tcqiSearch.setCollCd("2000000989");
            tcqiSearch.setDeptLoctCd(userDetails.getDeptLoctCd());
        }

        if(tcqiSearch.getCollCd() != null && tcqiSearch.getDeptLoctCd() != null) {
            BmgtSearch bmgtSearch = new BmgtSearch();
            bmgtSearch.setYy(Integer.toString(Integer.parseInt(tcqiSearch.getYy())+1));
            bmgtSearch.setDtlWokId("0000000251");
            bmgtSearch.setOrgid(tcqiSearch.getCollCd());
            bmgtSearch.setLesnPlcCd("1");
            bmgtSearch.setDateCk("1");
//            bmgtSearch.setIsWeb("Y");
            bmgtSearch.setLoginUserIntgUid(userDetails.getIntgUid());
            bmgtSearch.setLoginUserOrgid(userDetails.getOrgId());
            bmgtSearch.setDateCk("1");
            List<CamelCaseMap> bachSchedDscList = bmgtService.findBachSchedDscList(bmgtSearch);


            cnt = bachSchedDscList.size();

        }

        model.addAttribute("mcqiContDscList", tcqiService.findMcqiContDscList(tcqiSearch));
        model.addAttribute("tcqiSearch", tcqiSearch);
        model.addAttribute("cnt", cnt);

        return "univ/mcqi/mcqi/mcqiContDscForm";
    }

    /**
     * web 전공MACRO-CQI데이터을(를) 저장한다.
     * 2025.01.17
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
     * web 전공MACRO-CQI데이터을(를) 제출한다.
     * 2025.01.21
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
     * web 전공MACRO-CQI데이터을(를) 제출취소한다.
     * 2025.01.21
     */
    @RequestMapping(value = "/saveMcqiContSubmitCnl.do")
    public void saveMcqiContSubmitCnl(McqiContDsc mcqiContDsc, Model model) {
        tcqiService.saveMcqiContSubmitCnl(mcqiContDsc);
    }
    /**
     * 종합정보시스템 역량별 교수를 매핑한다.
     * 2025.01.17
     */
    @RequestMapping(value = "/findMcqiPfltAscDsc.do")
    public  void findMcqiPfltAscDsc(TcqiSearch tcqiSearch, Model model) {
        model.addAttribute("mcqiPfltAscDscList", tcqiService.findMcqiPfltAscDsc(tcqiSearch));
    }
    @RequestMapping(value = "/saveMcqiPfltAscDsc.do")
    public void saveMcqiPfltAscDsc(McqiListWrapper mcqiListWrapper) {
        tcqiService.saveMcqiPfltAscDsc(mcqiListWrapper.getMcqiPfltAscDsc());
    }

    /**
     * web 전공MACRO-CQI데이터 입력을(를) 조회한다.
     * 2025.01.17
     */
    @RequestMapping(value = "/views/findLcqiContDscForm.do")
    public String findLcqiContDscForm(TcqiSearch tcqiSearch, Model model) {
        int cnt = 0;
        UserDetails userDetails = SecurityContextHelper.getUserDetails(); // 로그인사용자정보
        if(tcqiSearch.getYy()==null){
            BmgtSearch bmgtSearch = new BmgtSearch();
            bmgtSearch.setDtlWokId("0000000249");
            List<CamelCaseMap> bachSchedDscList = bmgtService.findBachDtlWokDscList(bmgtSearch);
            if(bachSchedDscList.size() > 0) tcqiSearch.setYy(bachSchedDscList.get(0).getString("basYy"));

            tcqiSearch.setCollCd("2000000989");
            tcqiSearch.setDeptLoctCd(userDetails.getDeptLoctCd());
        }

        if(tcqiSearch.getCollCd() != null && tcqiSearch.getDeptLoctCd() != null) {
            BmgtSearch bmgtSearch = new BmgtSearch();
            bmgtSearch.setYy(Integer.toString(Integer.parseInt(tcqiSearch.getYy())+1));
            bmgtSearch.setDtlWokId("0000000251");
            bmgtSearch.setOrgid(tcqiSearch.getCollCd());
            bmgtSearch.setLesnPlcCd("1");
            bmgtSearch.setDateCk("1");
//            bmgtSearch.setIsWeb("Y");
            bmgtSearch.setLoginUserIntgUid(userDetails.getIntgUid());
            bmgtSearch.setLoginUserOrgid(userDetails.getOrgId());
            bmgtSearch.setDateCk("1");
            List<CamelCaseMap> bachSchedDscList = bmgtService.findBachSchedDscList(bmgtSearch);


            cnt = bachSchedDscList.size();

        }

        model.addAttribute("mcqiContDscList", tcqiService.findMcqiContDscList(tcqiSearch));
        model.addAttribute("tcqiSearch", tcqiSearch);
        model.addAttribute("cnt", cnt);

        return "univ/mcqi/mcqi/lcqiContDscForm";
    }



}
