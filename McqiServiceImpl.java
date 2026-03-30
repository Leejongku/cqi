package kr.dku.univ.mcqi.mcqi.service.impl;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import kr.dku.base.service.BaseService;
import kr.dku.base.service.ServiceException;
import kr.dku.base.util.CamelCaseMap;
import kr.dku.univ.mcqi.mcqi.service.McqiService;
import kr.dku.univ.mcqi.tcqi.domain.McqiSectDsc;
import kr.dku.univ.mcqi.tcqi.repository.TcqiMapper;
import kr.dku.univ.mcqi.tcqi.web.TcqiSearch;
import kr.dku.univ.srec.bmgt.repository.BmgtMapper;
import kr.dku.univ.srec.bmgt.web.BmgtSearch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


/**
 * @author 정보기획팀
 * @version 1.0
 * <pre>
 * 수정일                      수정자        수정내용
 * ---------------------------------------------------------------------
 * 2025-01-15   엄현수        최초작성 (전공 Macro-CQI 현황 그리드)
 * 2026-02-11   엄현수        교양 CQI 작성 관련 메서드 추가
 * </pre>
 */
@Service
public class McqiServiceImpl extends BaseService implements McqiService {

    private static final Logger log = LoggerFactory.getLogger(McqiServiceImpl.class);

    /** 교양 CQI 구분 코드 */
    private static final String LCQI_DIV_CD = "0002";

    /** 교양 영역 코드 목록 (SECT_CD) */
    private static final String[] LCQI_SECT_CODES = {"01", "02", "04"};

    /** 교양 답변 유형 코드 (입력 가능 TYPE - 전년도 TYPE='01' 은 읽기전용이므로 제외) */
    private static final String[] EDITABLE_TYPE_CODES = {"02", "03"};


    /* ================================================================
       Mapper 주입
    ================================================================ */
    private BmgtMapper bmgtMapper;
    private TcqiMapper tcqiMapper;

    @Autowired
    public void setBmgtMapper(BmgtMapper bmgtMapper) {
        this.bmgtMapper = bmgtMapper;
    }

    @Autowired
    public void setTcqiMapper(TcqiMapper tcqiMapper) {
        this.tcqiMapper = tcqiMapper;
    }


    /* ================================================================
       [기존] 전공 Macro-CQI 현황 그리드 HTML 생성
       수정이력:
        - findCommStatList 중복 호출 제거 (returnMap 재활용)
        - System.out.println → SLF4J 로거로 교체
        - </tr> 중복 추가 버그 수정
    ================================================================ */

    /**
     * web 전공MACRO-CQI데이터확인을(를) 조회한다.
     */
    @Override
    public CamelCaseMap findMcqiContDscList(BmgtSearch bmgtSearch) {
        StringBuilder htmlBuilder = new StringBuilder();
        CamelCaseMap returnMap = new CamelCaseMap();

        bmgtSearch.setPgmId("6830");
        bmgtSearch.setUseYn("1");
        bmgtSearch.setPcondTyCd("0001");

        List<CamelCaseMap> gridDscList = bmgtMapper.findBachCmnPcondDscList(bmgtSearch);

        if (gridDscList == null || gridDscList.isEmpty()) {
            throw new ServiceException("그리드 설정 정보를 찾을 수 없습니다.");
        }

        String gridFormatXml = (String) gridDscList.get(0).get("ttlCont");
        if (gridFormatXml == null || gridFormatXml.isEmpty()) {
            throw new ServiceException("그리드 XML 포맷이 비어 있습니다.");
        }

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(gridFormatXml)));

            Element root = document.getDocumentElement();

            // ── THEAD ──────────────────────────────────────
            NodeList headBands = root.getElementsByTagName("Band");
            NodeList headCells = null;
            for (int i = 0; i < headBands.getLength(); i++) {
                Element band = (Element) headBands.item(i);
                if ("head".equals(band.getAttribute("id"))) {
                    headCells = band.getElementsByTagName("Cell");
                    break;
                }
            }

            if (headCells != null) {
                htmlBuilder.append("<thead>");
                htmlBuilder.append("<tr>");
                int currentRow = 0;

                for (int i = 0; i < headCells.getLength(); i++) {
                    Element cell = (Element) headCells.item(i);
                    int cellRow = parseInt(cell.getAttribute("row"), 0);

                    if (cellRow > currentRow) {
                        htmlBuilder.append("</tr><tr>");
                        currentRow = cellRow;
                    }

                    htmlBuilder.append("<th");
                    appendSpan(htmlBuilder, cell);
                    appendThStyle(htmlBuilder, cell);
                    htmlBuilder.append(">")
                               .append(cellText(cell))
                               .append("</th>");
                }
                htmlBuilder.append("</tr></thead>");
            }

            // ── TBODY ──────────────────────────────────────
            NodeList bodyBands = root.getElementsByTagName("Band");
            NodeList bodyCells = null;
            for (int i = 0; i < bodyBands.getLength(); i++) {
                Element band = (Element) bodyBands.item(i);
                if ("body".equals(band.getAttribute("id"))) {
                    bodyCells = band.getElementsByTagName("Cell");
                    break;
                }
            }

            htmlBuilder.append("<tbody>");

            if (bodyCells != null) {
                // 현황 SQL 조회
                List<CamelCaseMap> dscList = bmgtMapper.findBachCmnPcondDsc(bmgtSearch);
                if (!dscList.isEmpty()) {
                    if (dscList.get(0).get("sqlCont") == null) {
                        throw new ServiceException("현황관리에서 Query정보를 찾을 수 없습니다.");
                    }
                    bmgtSearch.setSqlCont(dscList.get(0).get("sqlCont").toString());
                }

                // ★ 핵심 수정: commStatList 를 한 번만 조회 후 재사용
                List<CamelCaseMap> commStatList = bmgtMapper.findCommStatList(bmgtSearch);
                returnMap.put("tableCnt", commStatList.size());

                if (!commStatList.isEmpty()) {
                    for (int row = 0; row < commStatList.size(); row++) {
                        htmlBuilder.append("<tr>");

                        for (int i = 0; i < bodyCells.getLength(); i++) {
                            Element cell = (Element) bodyCells.item(i);
                            String bindText = cell.getAttribute("text").replace("bind:", "");
                            String style    = cell.getAttribute("style");

                            // 정렬 클래스
                            String alignClass = "";
                            if (style != null && !style.isEmpty()) {
                                if      (style.contains("align:center")) alignClass = " class='ta_c'";
                                else if (style.contains("align:left"))   alignClass = " class='ta_l'";
                                else if (style.contains("align:right"))  alignClass = " class='ta_r'";
                            }

                            // 텍스트 값 추출
                            String tdText;
                            if (bindText.contains("expr:currow")) {
                                tdText = String.valueOf(row + 1);
                            } else if (commStatList.get(row).get(bindText) != null) {
                                tdText = commStatList.get(row).get(bindText).toString();
                            } else {
                                tdText = "";
                            }

                            // checkbox 처리
                            if ("checkbox".equals(cell.getAttribute("displaytype"))) {
                                String checked = "1".equals(tdText) ? "checked" : "";
                                tdText = "<input type='checkbox' disabled " + checked + "/>";
                            }

                            htmlBuilder.append("<td");
                            appendSpan(htmlBuilder, cell);
                            htmlBuilder.append(alignClass);
                            if (style != null && !style.isEmpty()) {
                                htmlBuilder.append(" style='")
                                           .append(style.replace("align:", "text-align:"))
                                           .append("'");
                            }
                            htmlBuilder.append(">").append(tdText).append("</td>");
                        }
                        htmlBuilder.append("</tr>");   // ★ 루프 안에서만 닫음 (중복 제거)
                    }
                } else {
                    htmlBuilder.append("<tr>")
                               .append("<td colspan='").append(bodyCells.getLength()).append("'>")
                               .append("조회된 데이터가 없습니다.")
                               .append("</td></tr>");
                }

                htmlBuilder.append("</tbody>");

                // ── TFOOT (SUMMARY) ─ commStatList 재사용 ──
                NodeList summaryBands = root.getElementsByTagName("Band");
                NodeList summaryCells = null;
                for (int i = 0; i < summaryBands.getLength(); i++) {
                    Element band = (Element) summaryBands.item(i);
                    if ("summary".equals(band.getAttribute("id"))) {
                        summaryCells = band.getElementsByTagName("Cell");
                        break;
                    }
                }

                if (summaryCells != null && !commStatList.isEmpty()) {
                    htmlBuilder.append("<tfoot><tr>");
                    for (int i = 0; i < summaryCells.getLength(); i++) {
                        Element cell   = (Element) summaryCells.item(i);
                        String  expr   = cell.getAttribute("expr");
                        String  text   = cell.getAttribute("text");

                        htmlBuilder.append("<td");
                        appendSpan(htmlBuilder, cell);
                        htmlBuilder.append(" style='background-color:#f0f0f0; font-weight:bold; text-align:center;'>");

                        String cellValue = "";
                        if (text != null && !text.isEmpty()) {
                            cellValue = text;
                        } else if (expr != null && expr.startsWith("getSum(")) {
                            int start = expr.indexOf("'") + 1;
                            int end   = expr.lastIndexOf("'");
                            String fieldName = expr.substring(start, end);
                            int sum = 0;
                            for (CamelCaseMap rowMap : commStatList) {
                                Object val = rowMap.get(fieldName);
                                if (val != null) {
                                    try { sum += (int) Double.parseDouble(val.toString()); }
                                    catch (NumberFormatException ignored) { /* 무시 */ }
                                }
                            }
                            cellValue = String.valueOf(sum);
                        }
                        htmlBuilder.append(cellValue).append("</td>");
                    }
                    htmlBuilder.append("</tr></tfoot>");
                }
            }

            log.debug("[McqiServiceImpl] 전공 CQI 그리드 HTML 생성 완료");

        } catch (ParserConfigurationException | SAXException | IOException e) {
            log.error("[McqiServiceImpl] 그리드 XML 파싱 오류", e);
            throw new ServiceException("그리드 생성 중 오류가 발생하였습니다.");
        }

        returnMap.put("mcqiContDscTable", htmlBuilder.toString());
        return returnMap;
    }


    /* ================================================================
       [신규] 교양 Macro-CQI 작성 화면 관련 메서드
    ================================================================ */

    /**
     * 교양 CQI 마스터 상태 조회 (submitYn, closeYn 등)
     *
     * @param tcqiSearch yy, collCd(=orgid), detMngtDscCd(=pfltId)
     * @return CamelCaseMap or null (레코드 없으면 null)
     */
    @Override
    public CamelCaseMap findLcqiMaster(TcqiSearch tcqiSearch) {
        Map<String, Object> param = buildLcqiParam(tcqiSearch);
        return tcqiMapper.findLcqiMaster(param);
    }


    /**
     * 교양 CQI 체크박스 데이터 조회.
     *
     * <p>반환 데이터 구성:
     * <ul>
     *   <li>현재연도 TYPE='02','03' → DB 저장값 그대로 반환</li>
     *   <li>현재연도 TYPE='01'(전년도개선계획) → 전년도 TYPE='03' 을 이월하여 반환</li>
     * </ul>
     * JSP 의 savedSectList 에 바인딩되어 체크박스 초기화에 사용된다.
     *
     * @param tcqiSearch yy, collCd(=orgid), detMngtDscCd(=pfltId)
     * @return List&lt;CamelCaseMap&gt; (cqiSectCd, cqiTypeCd, choiCd 포함)
     */
    @Override
    public List<CamelCaseMap> findLcqiSectList(TcqiSearch tcqiSearch) {
        Map<String, Object> param = buildLcqiParam(tcqiSearch);

        // ① 현재연도 저장 데이터 (TYPE='02','03')
        List<CamelCaseMap> currentList = tcqiMapper.findLcqiSectList(param);

        // ② 전년도 TYPE='03' → 현재연도 TYPE='01'(전년도개선계획) 로 이월
        List<CamelCaseMap> prevList    = tcqiMapper.findLcqiPrevSectList(param);
        List<CamelCaseMap> carryList   = buildCarryOverList(prevList);

        // ③ 현재연도에 이미 TYPE='01' 이 저장된 경우 carryList 제외
        //    (현재연도 저장 데이터가 있으면 이월 데이터를 덮지 않음)
        Set<String> existingType01Keys = new HashSet<>();
        for (CamelCaseMap row : currentList) {
            if ("01".equals(row.get("cqiTypeCd"))) {
                existingType01Keys.add(row.get("cqiSectCd") + "_" + row.get("choiCd"));
            }
        }

        List<CamelCaseMap> result = new ArrayList<>();

        // 이월 데이터 중 현재연도에 없는 것만 추가
        for (CamelCaseMap carry : carryList) {
            String key = carry.get("cqiSectCd") + "_" + carry.get("choiCd");
            if (!existingType01Keys.contains(key)) {
                result.add(carry);
            }
        }
        result.addAll(currentList);

        return result;
    }


    /**
     * 교양 CQI 체크박스 저장 (MASTER upsert + SECT delete-insert).
     *
     * <p>트랜잭션 처리:
     * <ol>
     *   <li>MCQI_MASTER upsert (mergeLcqiMaster)</li>
     *   <li>저장 대상 (SECT_CD + TYPE_CD) 조합별 기존 데이터 삭제</li>
     *   <li>체크된 항목 일괄 insert</li>
     * </ol>
     * TYPE='01'(전년도개선계획) 은 읽기 전용이므로 저장에서 제외됨.
     *
     * @param sectDscList 저장할 SECT_DSC 목록 (JSP AJAX POST 데이터)
     */
    @Override
    @Transactional
    public void saveLcqiSectList(List<McqiSectDsc> sectDscList) {
        if (sectDscList == null || sectDscList.isEmpty()) {
            return;
        }

        McqiSectDsc first = sectDscList.get(0);
        validateLcqiSectParam(first);

        // ① MCQI_MASTER upsert
        Map<String, Object> masterParam = new HashMap<>();
        masterParam.put("yy",           first.getYy());
        masterParam.put("orgid",        first.getOrgid());
        masterParam.put("pfltId",       first.getPfltId());
        masterParam.put("inputUserId",  getLoginUserId());
        masterParam.put("latestUserId", getLoginUserId());
        masterParam.put("remoteAddr",   getRemoteAddr());
        tcqiMapper.mergeLcqiMaster(masterParam);

        // ② 저장 대상 (SECT_CD + TYPE_CD) 조합 추출
        Set<String> sectTypePairs = new HashSet<>();
        for (McqiSectDsc dsc : sectDscList) {
            sectTypePairs.add(dsc.getCqiSectCd() + "_" + dsc.getCqiTypeCd());
        }

        // ③ 조합별 기존 데이터 삭제 (TYPE='01' 은 절대 삭제하지 않음)
        for (String pair : sectTypePairs) {
            String[] parts    = pair.split("_");
            String   sectCd   = parts[0];
            String   typeCd   = parts[1];

            if ("01".equals(typeCd)) {
                log.warn("[McqiServiceImpl] TYPE='01'(전년도개선계획) 저장 시도 차단: sectCd={}", sectCd);
                continue;
            }

            Map<String, Object> delParam = new HashMap<>();
            delParam.put("yy",         first.getYy());
            delParam.put("orgid",      first.getOrgid());
            delParam.put("pfltId",     first.getPfltId());
            delParam.put("cqiSectCd",  sectCd);
            delParam.put("cqiTypeCd",  typeCd);
            tcqiMapper.deleteLcqiSect(delParam);
        }

        // ④ 체크된 항목 insert
        for (McqiSectDsc dsc : sectDscList) {
            if ("01".equals(dsc.getCqiTypeCd())) continue; // 전년도개선계획 차단

            Map<String, Object> insParam = new HashMap<>();
            insParam.put("yy",          dsc.getYy());
            insParam.put("orgid",       dsc.getOrgid());
            insParam.put("pfltId",      dsc.getPfltId());
            insParam.put("cqiSectCd",   dsc.getCqiSectCd());
            insParam.put("cqiTypeCd",   dsc.getCqiTypeCd());
            insParam.put("choiCd",      dsc.getChoiCd());
            insParam.put("ansTypeCd",   "OBJ");
            insParam.put("cont",        null);
            insParam.put("sortOrd",     dsc.getSortOrd());
            insParam.put("inputUserId", getLoginUserId());
            insParam.put("remoteAddr",  getRemoteAddr());
            tcqiMapper.insertLcqiSect(insParam);
        }

        log.info("[McqiServiceImpl] 교양 CQI 저장 완료 yy={}, orgid={}, pfltId={}",
                first.getYy(), first.getOrgid(), first.getPfltId());
    }


    /* ================================================================
       Private 헬퍼 메서드
    ================================================================ */

    /**
     * TcqiSearch → Mapper 파라미터 Map 변환.
     * orgid = collCd, pfltId = detMngtDscCd (교양 영역코드)
     */
    private Map<String, Object> buildLcqiParam(TcqiSearch tcqiSearch) {
        Map<String, Object> param = new HashMap<>();
        param.put("yy",     tcqiSearch.getYy());
        param.put("orgid",  tcqiSearch.getCollCd());
        param.put("pfltId", tcqiSearch.getDetMngtDscCd());
        return param;
    }

    /**
     * 전년도 TYPE='03' 목록을 TYPE='01'(전년도개선계획 이월)로 변환.
     */
    private List<CamelCaseMap> buildCarryOverList(List<CamelCaseMap> prevList) {
        List<CamelCaseMap> result = new ArrayList<>();
        for (CamelCaseMap prev : prevList) {
            CamelCaseMap carry = new CamelCaseMap();
            carry.put("cqiSectCd",  prev.get("cqiSectCd"));
            carry.put("cqiTypeCd",  "01");             // 전년도개선계획
            carry.put("choiCd",     prev.get("choiCd"));
            carry.put("ansTypeCd",  "OBJ");
            carry.put("cont",       prev.get("cont"));
            carry.put("sortOrd",    prev.get("sortOrd"));
            result.add(carry);
        }
        return result;
    }

    /**
     * 저장 파라미터 유효성 검증.
     */
    private void validateLcqiSectParam(McqiSectDsc dsc) {
        if (dsc.getYy() == null || dsc.getYy().isEmpty()) {
            throw new ServiceException("년도 정보가 없습니다.");
        }
        if (dsc.getOrgid() == null || dsc.getOrgid().isEmpty()) {
            throw new ServiceException("조직 정보가 없습니다.");
        }
        if (dsc.getPfltId() == null || dsc.getPfltId().isEmpty()) {
            throw new ServiceException("교양 영역 정보가 없습니다.");
        }
    }

    /**
     * HTML 빌더 공통 - colspan / rowspan 추가.
     */
    private void appendSpan(StringBuilder sb, Element cell) {
        String colspan = cell.getAttribute("colspan");
        String rowspan = cell.getAttribute("rowspan");
        if (colspan != null && !colspan.isEmpty()) sb.append(" colspan='").append(colspan).append("'");
        if (rowspan != null && !rowspan.isEmpty()) sb.append(" rowspan='").append(rowspan).append("'");
    }

    /**
     * HTML 빌더 공통 - TH 스타일 추가.
     */
    private void appendThStyle(StringBuilder sb, Element cell) {
        StringBuilder style = new StringBuilder();
        String size  = cell.getAttribute("size");
        String sAttr = cell.getAttribute("style");
        if (size  != null && !size.isEmpty())  style.append("width:").append(size).append("px;");
        if (sAttr != null && !sAttr.isEmpty()) style.append(sAttr.replace("align:", "text-align:")).append(";");
        style.append("white-space:normal;");
        sb.append(" style='").append(style).append("'");
    }

    /**
     * HTML 빌더 공통 - cell text (줄바꿈 치환).
     */
    private String cellText(Element cell) {
        return cell.getAttribute("text")
                   .replace("\\n", "<br/>")
                   .replace("\n",  "<br/>")
                   .replace("&#10;", "<br/>");
    }

    /**
     * int 파싱 (파싱 실패 시 defaultValue 반환).
     */
    private int parseInt(String val, int defaultValue) {
        if (val == null || val.isEmpty()) return defaultValue;
        try { return Integer.parseInt(val); }
        catch (NumberFormatException e) { return defaultValue; }
    }
}
