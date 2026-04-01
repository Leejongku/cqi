<%--
  Class Name : lcqiContDscForm.jsp
  Description : 교양 Macro-CQI 작성 화면 (진입: /univ/mcqi/mcqi/views/findLcqiContDscForm.do)
  Modification Information
  -------
  수정일       수정자     수정내용
  -------
  2026-02-26  정보기획팀  최초작성
  -------
  author : 정보기획팀
--%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp" %>
<script type="text/javascript">var _ctx = '${pageContext.request.contextPath}';</script>

<c:if test="${not empty accessDeniedMsg}">
  <script type="text/javascript">
    alert('${fn:escapeXml(accessDeniedMsg)}');
  </script>
</c:if>

<script type="text/javascript">
  var relatWokCd;
  var acsAlRngCd;
  var pgmId;

  $(document).ready(function() {
    var accessDeniedMsg = '${fn:escapeXml(accessDeniedMsg)}';
    window.LF_Dirty = window.LF_Dirty || {};
    window.LF_TAB_LOCKED = false;
    var pgmInfo = $.getPgmInfo(location);
    if (pgmInfo) {
      relatWokCd = pgmInfo.relatWokCd;
      acsAlRngCd = pgmInfo.acsAlRngCd;
      pgmId = pgmInfo.pgmId;
      $("#tcqiSearch #relatWokCd").val(relatWokCd);
      $("#tcqiSearch #acsAlRngCd").val(acsAlRngCd);
    }

    $("#yy").parent("div").addClass("form_required");
    $("#detMngtDscCd").parent("div").addClass("form_required");

    $("#tcqiSearch").validate({
      rules: {
        yy: { required: true },
        detMngtDscCd: { required: true }
      },
      messages: {
        yy: { required: "년도는 필수입력값입니다." },
        detMngtDscCd: { required: "교양구분을 선택해 주세요." }
      }
    });

    $("#btn_search").on("click", function(e) {
      e.preventDefault();
      if (accessDeniedMsg) {
        alert(accessDeniedMsg);
        return;
      }
      if ($("#tcqiSearch").valid()) {
        LF.search();
      }
    });

    $(".tab_header a[data-tab-index]").on("click", function(e) {
      e.preventDefault();
      if (window.LF_TAB_LOCKED) {
        alert("교양 Macro-CQI 작성 기간이 아닙니다.");
        e.stopPropagation();
        e.stopImmediatePropagation();
        return false;
      }
      var idx = parseInt($(this).data("tabIndex"), 10);
      var curIdx = LF.currentTabIndex ? LF.currentTabIndex() : 0;
      if (window.LF_Dirty && window.LF_Dirty[String(curIdx)]) {
        if (confirm("저장하시겠습니까?")) {
          LF.save({ skipConfirm: true, afterTab: idx });
          return;
        }
      }
      LF.tab(idx, this);
    });

    $("#submitBtn").on("click", function(e) {
      e.preventDefault();
      LF.submit();
    });

    $("#cancelBtn").on("click", function(e) {
      e.preventDefault();
      LF.submitCancel();
    });

    $("#saveBtn").on("click", function(e) {
      e.preventDefault();
      LF.save({ afterTab: (LF.currentTabIndex ? LF.currentTabIndex() : 0) });
    });

    // detMngtDscCd(교양구분) - 1건이면 변경 불가, 다건이면 선택 가능
    var detMngtDscCdLocked = <c:choose><c:when test="${detMngtDscCdLocked != null && !detMngtDscCdLocked}">false</c:when><c:otherwise>true</c:otherwise></c:choose>;
    if (detMngtDscCdLocked) {
      $("#lfPfltId").prop("disabled", true);
      if ($("#lfPfltId").hasClass("chosen-select")) $("#lfPfltId").trigger("chosen:updated");
    } else {
      $("#lfPfltId").prop("disabled", false);
      if ($("#lfPfltId").hasClass("chosen-select")) $("#lfPfltId").trigger("chosen:updated");
      $("#lfPfltId").on("change", function() {
        var selectedCd = $(this).val();
        $("#detMngtDscCd").val(selectedCd);
        if (S && S.loaded) {
          if (confirm("교양구분을 변경하면 재조회됩니다. 계속하시겠습니까?")) {
            LF.search();
          } else {
            $(this).val(S.detMngtDscCd);
            if ($(this).hasClass("chosen-select")) $(this).trigger("chosen:updated");
            $("#detMngtDscCd").val(S.detMngtDscCd);
          }
        }
      });
    }

    var periodCheck = "${cnt}";
    if (periodCheck === "0") {
      window.LF_TAB_LOCKED = true;
      alert("교양 Macro-CQI 작성 기간이 아닙니다.");
      $("#saveBtn, #submitBtn, #cancelBtn").prop("disabled", true);
      if (typeof $("#saveBtn").enable === "function") {
        $("#saveBtn").enable(false);
        $("#submitBtn").enable(false);
        $("#cancelBtn").enable(false);
      }
    }

    if ($("#lfYy").val() && $("#lfPfltId").val() && !accessDeniedMsg && periodCheck !== "0") {
      LF.search();
    }
  });
</script>

<!-- 교양 전용: 동적 영역(체크리스트·textarea) 스타일. 나머지는 표준 section_tbl/tabs 적용 -->
<style>
  #lfPfltId_chosen .chosen-single,
  #lfPfltId_chosen .chosen-single span,
  #lfPfltId_chosen .chosen-default {
    font-weight:700 !important;
    color:#4a4a4a !important;
  }
  .lcqi-tbl { width:100%; border-collapse:collapse; font-size:13px; }
  .lcqi-tbl th, .lcqi-tbl td { border:1px solid #c8d4e4; padding:7px 10px; vertical-align:middle; }
  .lcqi-tbl thead th { background:#d4dff0; color:#1a4f91; font-weight:700; text-align:center; font-size:12px; }
  .lcqi-rh { background:#eef2f8; font-weight:700; color:#333; text-align:center; width:150px; white-space:normal; word-break:keep-all; font-size:12px; }
  tr.lcqi-prev td { background:#f4f6f9 !important; }
  .lcqi-cg { display:grid; grid-template-columns:repeat(3,1fr); gap:4px 2px; padding:3px 0; }
  .lcqi-ci { display:flex; align-items:center; gap:5px; padding:3px 4px; border-radius:3px; }
  .lcqi-ci:hover:not(.lcqi-d) { background:#f0f4f8; }
  .lcqi-ci input[type=checkbox] { width:14px; height:14px; accent-color:#1a4f91; cursor:pointer; flex-shrink:0; }
  .lcqi-ci label { font-size:12px; color:#333; cursor:pointer; user-select:none; white-space:nowrap; }
  .lcqi-ci.lcqi-d label, .lcqi-ci.lcqi-d input { cursor:not-allowed; color:#999; }
  .lcqi-etc { height:24px; border:1px solid #c0c8d8; border-radius:3px; padding:0 7px; font-size:12px; min-width:120px; max-width:220px; }
  .lcqi-subj { box-sizing:border-box; max-width:100%; width:100%; min-height:66px; border:1px solid #c0c8d8; border-radius:4px; padding:6px 9px; font-size:13px; resize:vertical; overflow-y:auto; }
  .lcqi-tbl td .lcqi-subj { display:block; }
  .lcqi-carry { background:#f0f5ff; border:1px dashed #90aad4; border-radius:4px; padding:5px 11px; font-size:11px; color:#3a5a90; margin-bottom:8px; }
  .lcqi-badge { font-size:11px; font-weight:700; padding:2px 8px; border-radius:10px; color:#fff; background:#f57c00; }
  .lcqi-badge.lcqi-sub { background:#2e7d32; }
  .lcqi-badge.lcqi-cls { background:#616161; }
  #lcqi_toast { position:fixed; bottom:20px; right:20px; z-index:9999; display:flex; flex-direction:column; gap:6px; }
  .lcqi-toast-msg { padding:9px 16px; border-radius:5px; color:#fff; font-size:13px; box-shadow:0 4px 14px rgba(0,0,0,.2); }
  .lcqi-toast-msg.ok { background:#2e7d32; }
  .lcqi-toast-msg.err { background:#c62828; }
  .lcqi-toast-msg.info { background:#1565c0; }
  #lcqi_modal { display:none; position:fixed; inset:0; background:rgba(0,0,0,.42); z-index:8000; align-items:center; justify-content:center; }
  #lcqi_modal.on { display:flex; }
  .lcqi-modal-box { background:#fff; border-radius:6px; padding:24px 28px; box-shadow:0 8px 30px rgba(0,0,0,.22); min-width:300px; max-width:400px; text-align:center; }
</style>

<div class="section_tbl">
  <div class="section_mid_tit">
    <h4 class="tit_mid f_l">교양 Macro-CQI 작성</h4>
    <span id="lcqiStatusBadge" class="lcqi-badge">작성중</span>
  </div>
</div>

<form:form modelAttribute="tcqiSearch" id="tcqiSearch" method="post" class="form-inline">
  <input type="hidden" id="relatWokCd" name="relatWokCd" value="" />
  <input type="hidden" id="acsAlRngCd" name="acsAlRngCd" value="" />

  <div class="tbl_search">
    <table>
      <caption>검색테이블</caption>
      <colgroup>
        <col style="width: 10%;" />
        <col style="width: 20%;" />
        <col style="width: 40%;" />
        <col style="width: 20%;" />
        <col style="width: 120px;" />
      </colgroup>
      <tbody>
        <tr>
          <td>
            <div class="form_text form_required">
              <form:input path="yy" id="lfYy" placeholder="년도" data-inputmask-clearmaskonlostfocus="false" style="width:70px;" />
            </div>
          </td>
          <td>
            <div class="form_select form_required" style="width:185px;">
              <select id="lfPfltId" title="교양구분" class="chosen-select" disabled="disabled">
                <c:choose>
                  <c:when test="${!empty detMngtDscOptions}">
                    <c:forEach var="opt" items="${detMngtDscOptions}">
                      <option value="${fn:escapeXml(opt.cd)}"
                              ${opt.cd eq tcqiSearch.detMngtDscCd ? 'selected="selected"' : ''}>
                        ${fn:escapeXml(opt.nm)}
                      </option>
                    </c:forEach>
                  </c:when>
                  <c:otherwise>
                    <option value="${fn:escapeXml(tcqiSearch.detMngtDscCd)}" selected="selected">
                      ${fn:escapeXml(detMngtDscNm)}
                    </option>
                  </c:otherwise>
                </c:choose>
              </select>
              <input type="hidden" id="detMngtDscCd" name="detMngtDscCd" value="${fn:escapeXml(tcqiSearch.detMngtDscCd)}" />
            </div>
          </td>
          <td colspan="2">
              <span id="lcqiNotice" style="font-size:12px; color:#d32f2f; font-weight:700;">※ 각 탭의 내용을 작성 후 반드시 [저장] 버튼을 누르고 [제출] 해 주세요.</span>
          </td>
          <td class="search_td">
            <button type="button" class="btn_search" id="btn_search">
              <strong><span><img src="images/btn/btn_serach.png" alt="SEARCH" style="cursor:pointer;" /></span></strong>
            </button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</form:form>

<div id="lcqiContDscList" class="form-inline">
  <div id="tabs" class="tabs">
    <div class="tab_header_box">
      <ul class="tab_header f_l">
        <c:forEach var="sect" items="${cqiSectCdList}" varStatus="st">
          <li>
            <a href="#tab-${st.index + 1}" class="lf-tbtn ${st.first ? 'on' : ''}" data-tab-index="${st.index}">
              ${st.index + 1}. ${fn:escapeXml(sect.cmnCdvalNm)}
            </a>
          </li>
        </c:forEach>
      </ul>
    </div>

    <c:forEach var="sect" items="${cqiSectCdList}" varStatus="st">
      <div class="tab_cont lf-pane ${st.first ? 'on' : ''}" id="tab-${st.index + 1}">
        <div class="lcqi-carry">ℹ 전년도 개선계획이 자동 이월됩니다. <b>전년도 개선계획</b> 행은 수정할 수 없습니다.</div>
        <div class="section_tbl">
          <div class="tbl_row">
            <table class="lcqi-tbl lf-tbl">
              <thead><tr><th style="width:100px;">구분</th><th>체크 항목 및 개선 내용</th></tr></thead>
              <tbody>
                <c:forEach var="ty" items="${cqiTypeCdList}">
                  <c:set var="isPrevType" value="${ty.cmnCdval eq '0001' or ty.cmnCdval eq '1001' or fn:contains(ty.cmnCdvalNm, '전년도') or fn:contains(ty.cmnCdvalNm, '이월')}"/>
                  <tr class="${isPrevType ? 'lcqi-prev lf-prev' : ''}">
                    <td class="lcqi-rh lf-rh">
                      ${fn:escapeXml(ty.cmnCdvalNm)}
                      <c:if test="${isPrevType}"><span class="s">(이월)</span></c:if>
                    </td>
                    <td><div id="lc_${fn:escapeXml(sect.cmnCdval)}_${fn:escapeXml(ty.cmnCdval)}"></div></td>
                  </tr>
                </c:forEach>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </c:forEach>
  </div>

  <div class="submit-notice" style="color: red; font-size: 20px; font-weight: bold; text-align: center; margin-top: 30px;">
    각 탭의 필수 내용을 모두 작성 및 저장 후 반드시 [제출]버튼을 클릭해 주십시오.
  </div>

  <div class="section_btn">
    <div class="f_r">
      <span id="cancelBtnWrap" style="display:none;">
        <ui:button type="button" id="cancelBtn" name="cancelBtn" text="제출취소" className="btn btn_default" role="C" enabled="false" />
      </span>
      <span id="submitBtnWrap">
        <ui:button type="button" id="submitBtn" name="submitBtn" text="제출" className="btn btn_default" role="C" enabled="false" />
      </span>
      <span id="saveBtnWrap">
        <ui:button type="button" id="saveBtn" name="saveBtn" text="저장" className="btn_success" role="C" enabled="false" />
      </span>
    </div>
  </div>

  <div id="lcqiNoticeBottom" style="color: red; font-weight: bold; margin-right: 16px; float: left; line-height: 32px;"></div>
</div>

<div id="lcqi_toast"></div>
<div id="lcqi_modal">
  <div class="lcqi-modal-box">
    <div id="lcqi_modal_icon" class="lcqi-mi" style="font-size:32px; margin-bottom:9px;">❓</div>
    <div id="lcqi_modal_title" style="font-size:15px; font-weight:700; margin-bottom:7px;"></div>
    <div id="lcqi_modal_body" style="font-size:13px; color:#555; margin-bottom:18px; line-height:1.65;"></div>
    <div style="display:flex; justify-content:center; gap:7px;">
      <button type="button" class="btn btn_success" id="lcqi_modal_ok">확인</button>
      <button type="button" class="btn btn_default" id="lcqi_modal_cancel" style="display:none;">취소</button>
    </div>
  </div>
</div>

<!-- 공통코드(섹션/타입) JSON: JS에서 파싱하여 동적 처리 -->
<script type="application/json" id="lcqi_sects_json">[
  <c:forEach var="sect" items="${cqiSectCdList}" varStatus="st">
    <c:if test="${!st.first}">,</c:if>{"cd":"${fn:escapeXml(sect.cmnCdval)}","nm":"${fn:escapeXml(sect.cmnCdvalNm)}"}
  </c:forEach>
]</script>
<script type="application/json" id="lcqi_types_json">[
  <c:forEach var="ty" items="${cqiTypeCdList}" varStatus="st">
    <c:if test="${!st.first}">,</c:if>{"cd":"${fn:escapeXml(ty.cmnCdval)}","nm":"${fn:escapeXml(ty.cmnCdvalNm)}"}
  </c:forEach>
]</script>
<script type="application/json" id="lcqi_choi_json">[
  <c:forEach var="choi" items="${choiCdList}" varStatus="st">
    <c:if test="${!st.first}">,</c:if>{"cd":"${fn:escapeXml(choi.cmnCdval)}","nm":"${fn:escapeXml(choi.cmnCdvalNm)}"}
  </c:forEach>
]</script>

<script type="text/javascript">
(function() {
  "use strict";
  var CH = JSON.parse((document.getElementById("lcqi_choi_json") || {}).textContent || "[]")
              .filter(function(c) { return !(c.cd === "C107" || (c.nm && c.nm.indexOf("비고") >= 0)); });
  var ETC_CD = (function() { var x = CH.filter(function(c) { return c.cd === "C106" || (c.nm && c.nm.indexOf("기타") >= 0); }); return x.length ? x[0].cd : null; })();
  // 공통코드 기반 동적 탭/행
  var SECTS = JSON.parse((document.getElementById("lcqi_sects_json") || {}).textContent || "[]");
  var TYPES = JSON.parse((document.getElementById("lcqi_types_json") || {}).textContent || "[]");
  var SC = SECTS.map(function(s) { return s.cd; });
  var TY = TYPES.map(function(t) { return t.cd; });
  var PREV_TY_CD = TYPES.filter(function(t) {
    return t.cd === "0001" || t.cd === "1001" || (t.nm && (t.nm.indexOf("전년도") >= 0 || t.nm.indexOf("이월") >= 0));
  }).map(function(t) { return t.cd; });
  function isPrevTy(typeCd) { return PREV_TY_CD.indexOf(typeCd) >= 0; }
  var EDIT_TY = TY.filter(function(cd) { return !isPrevTy(cd); });
  var MAX_OBJ = 3;

  var S = {
    yy: '${fn:escapeXml(tcqiSearch.yy)}',
    pfltId: '${fn:escapeXml(userEmpId != null ? userEmpId : "")}',
    detMngtDscCd: '${fn:escapeXml(tcqiSearch.detMngtDscCd)}',
    orgid: '${fn:escapeXml(userOrgId != null ? userOrgId : tcqiSearch.collCd)}',
    empid: '${fn:escapeXml(userEmpId != null ? userEmpId : "")}',
    submitYn: 'N',
    closeYn: 'N',
    loaded: false
  };
  var _mdCb = null;

  function tab(idx, btn) {
    $(".tab_header .lf-tbtn").removeClass("on");
    $(btn).addClass("on");
    $(".tab_cont.lf-pane").removeClass("on").hide();
    $("#tab-" + (idx + 1)).addClass("on").show();
  }
  function currentTabIndex() {
    var $p = $(".tab_cont.lf-pane.on:visible").first();
    if (!$p.length) $p = $(".tab_cont.lf-pane.on").first();
    if ($p.length) {
      var id = String($p.attr("id") || "");
      var n = parseInt(id.replace("tab-", ""), 10);
      if (!isNaN(n) && n > 0) return n - 1;
    }
    var a = parseInt($(".tab_header .lf-tbtn.on").data("tabIndex"), 10);
    return isNaN(a) ? 0 : a;
  }

  function search(done) {
    S.yy = $("#lfYy").val().trim();
    S.detMngtDscCd = ($("#lfPfltId").val() || "").trim() || ($("#detMngtDscCd").val() || "").trim();
    if (!S.yy) { _t("err", "⚠ 연도를 입력해 주세요."); return; }
    if (!S.detMngtDscCd) { _t("err", "⚠ 교양구분을 선택해 주세요."); return; }
    _reset();

    _get("/univ/mcqi/mcqi/findLcqiMaster.do", { yy: S.yy, detMngtDscCd: S.detMngtDscCd, collCd: S.orgid, pfltId: S.pfltId }, function(d) {
      var m = d.lcqiMaster || {};
      S.submitYn = m.submitYn || "N";
      S.closeYn = m.closeYn || "N";
      _get("/univ/mcqi/mcqi/findLcqiSectList.do", { yy: S.yy, detMngtDscCd: S.detMngtDscCd, collCd: S.orgid, pfltId: S.pfltId }, function(d2) {
        var cur = d2.lcqiSectList || [];
        _post("/univ/mcqi/mcqi/findLcqiPrevSectList.do", { yy: S.yy, detMngtDscCd: S.detMngtDscCd, collCd: S.orgid, pfltId: S.pfltId }, function(d3) {
          var prv = d3.lcqiPrevSectList || [];
          for (var i = 0; i < SC.length; i++) {
            var sc = SC[i];
            for (var j = 0; j < TY.length; j++) {
              var ty = TY[j];
              if (isPrevTy(ty)) {
                _rdr(sc, ty, prv.filter(function(r) { return r.cqiSectCd === sc; }), true);
              } else {
                _rdr(sc, ty, cur.filter(function(r) { return r.cqiSectCd === sc && r.cqiTypeCd === ty; }), false);
              }
            }
          }
          S.loaded = true;
          _ui();
          _t("info", "✔ 데이터를 조회하였습니다.");
          if (typeof done === "function") done();
        });
      });
    });
  }

  function save(opts) {
    opts = opts || {};
    if (!S.loaded) { _t("err", "⚠ 먼저 [SEARCH]로 데이터를 조회해 주세요."); return; }
    if (S.closeYn === "Y") { _t("err", "⚠ 마감된 CQI는 수정할 수 없습니다."); return; }
    if (S.submitYn === "Y") { _t("err", "⚠ 제출된 CQI는 수정할 수 없습니다. 제출취소 후 수정해 주세요."); return; }
    if (!opts.skipConfirm && !confirm("저장하시겠습니까?")) return;
    if (!_validateBeforeSend()) return;
    var keepIdx = (opts.afterTab != null) ? opts.afterTab : currentTabIndex();
    var list = _collectList();
    _postJson("/univ/mcqi/mcqi/saveLcqiSect.do", { yy: S.yy, orgid: S.orgid, pfltId: S.pfltId, detMngtDscCd: S.detMngtDscCd, empid: S.empid, sectList: list }, function(r) {
      if (r.msg && r.msg !== "") { _t("err", "⚠ " + r.msg); return; }
      _t("ok", "✔ 저장되었습니다.");
      if (window.LF_Dirty) window.LF_Dirty = {};
      search(function() {
        var $a = $(".tab_header a[data-tab-index='" + keepIdx + "']");
        if ($a.length) tab(keepIdx, $a[0]);
      });
    });
  }

  function submit() {
    if (!S.loaded) { _t("err", "⚠ 먼저 [SEARCH]로 데이터를 조회해 주세요."); return; }
    if (S.closeYn === "Y") { _t("err", "⚠ 이미 마감된 CQI입니다."); return; }
    if (S.submitYn === "Y") { _t("err", "⚠ 이미 제출되었습니다."); return; }
    if (!_validateBeforeSend()) return;
    var em = _val();
    if (em.length > 0) {
      _m("⚠️", "제출 불가", "다음 탭에 작성하지 않은 항목이 있습니다.\n" + em.join(", ") + "\n\n모든 탭을 작성·저장 후 제출해 주세요.", null);
      return;
    }
    _m("📤", "제출 확인", "작성한 교양 Macro-CQI를 제출하시겠습니까?\n제출 후에는 수정이 제한됩니다.", function() {
      _post("/univ/mcqi/mcqi/saveMcqiMasterSubmit.do", { yy: S.yy, cqiDivCd: "0002", orgid: S.orgid, pfltId: S.pfltId, detMngtDscCd: S.detMngtDscCd }, function(r) {
        if (r.msg && r.msg !== "") { _t("err", "⚠ " + r.msg); return; }
        S.submitYn = "Y";
        _ui();
        _t("ok", "✔ 제출되었습니다.");
      });
    });
  }

  function submitCancel() {
    if (S.closeYn === "Y") { _t("err", "⚠ 마감된 CQI는 제출취소가 불가합니다."); return; }
    _m("↩️", "제출취소 확인", "제출을 취소하시겠습니까?\n취소 후 내용을 수정하고 다시 제출할 수 있습니다.", function() {
      _post("/univ/mcqi/mcqi/saveMcqiMasterSubmitCnl.do", { yy: S.yy, cqiDivCd: "0002", orgid: S.orgid, pfltId: S.pfltId, detMngtDscCd: S.detMngtDscCd }, function() {
        S.submitYn = "N";
        _ui();
        _t("info", "제출이 취소되었습니다.");
      });
    });
  }

  function _rdr(sc, ty, rows, disabled) {
    var el = document.getElementById("lc_" + sc + "_" + ty);
    if (!el) return;
    var chkd = rows
      .filter(function(r) { return r.ansTypeCd === "OBJ" || (ETC_CD && r.choiCd === ETC_CD); })
      .map(function(r) { return r.choiCd; });
    var sj = rows.find(function(r) { return r.ansTypeCd === "SUBJ" && r.choiCd === "C107"; });
    var et = ETC_CD ? rows.find(function(r) { return r.choiCd === ETC_CD; }) : null;
    var ds = disabled ? " disabled" : "";
    var dc = disabled ? " lcqi-d lf-d" : "";
    var ph = {};
    for (var i = 0; i < TYPES.length; i++) {
      var t = TYPES[i];
      if (isPrevTy(t.cd)) ph[t.cd] = "전년도 개선계획 내용 (이월 - 수정 불가)";
      else if (t.cd === "0002" || t.cd === "1002") ph[t.cd] = "반영된 개선사항 내용을 입력해 주세요.";
      else if (t.cd === "0003" || t.cd === "1003") ph[t.cd] = "이번년도 개선계획 내용을 입력해 주세요.";
      else ph[t.cd] = (t.nm || "내용을 입력해 주세요.");
    }

    var h = '<textarea class="lcqi-subj lf-subj" id="sj_' + sc + '_' + ty + '" data-sect="' + sc + '" data-type="' + ty + '" rows="3" placeholder="' + ph[ty] + '"' + ds + '>' + _esc(sj && sj.cont ? sj.cont : "") + '</textarea>';
    h += '<div class="lcqi-cg lf-cg">';
    for (var i = 0; i < CH.length; i++) {
      var c = CH[i];
      var cid = "cb_" + sc + "_" + ty + "_" + c.cd;
      var chk = chkd.indexOf(c.cd) >= 0 ? " checked" : "";
      if (ETC_CD && c.cd === ETC_CD) {
        var eid = "et_" + sc + "_" + ty;
        var ev = (et && et.cont) ? _esc(et.cont) : "";
        var eds = (!chk || disabled) ? " disabled" : "";
        var och = disabled ? "" : ' onchange="LF._tE(this,\'' + eid + '\')"';
        h += '<div class="lcqi-ci ' + dc + '" style="gap:4px"><input type="checkbox" id="' + cid + '" data-sect="' + sc + '" data-type="' + ty + '" data-choi="' + _esc(c.cd) + '" data-is-etc="1"' + chk + ds + och + '><label for="' + cid + '">' + _esc(c.nm) + '</label><input type="text" class="lcqi-etc lf-etc" id="' + eid + '" data-sect="' + sc + '" data-type="' + ty + '" placeholder="기타 설명(필수)" value="' + ev + '"' + ds + eds + '></div>';
      } else {
        h += '<div class="lcqi-ci ' + dc + '"><input type="checkbox" id="' + cid + '" data-sect="' + sc + '" data-type="' + ty + '" data-choi="' + _esc(c.cd) + '"' + chk + ds + '><label for="' + cid + '">' + _esc(c.nm) + '</label></div>';
      }
    }
    h += "</div>";
    el.innerHTML = h;
    if (!disabled) _bindAreaHandlers(sc, ty);
  }

  window.LF = {
    _tE: function(cb, id) {
      var i = document.getElementById(id);
      if (!i) return;
      if (cb.checked) {
        i.disabled = false;
        i.classList.remove("on");
        i.focus();
      } else {
        i.value = "";
        i.disabled = true;
        i.classList.add("on");
      }
      _limitObjSelection(cb.dataset.sect, cb.dataset.type, cb);
      if (window.LF_Dirty) window.LF_Dirty[String(currentTabIndex())] = true;
    },
    tab: tab,
    currentTabIndex: currentTabIndex,
    search: search,
    save: save,
    submit: submit,
    submitCancel: submitCancel,
    _mOk: function() {
      $("#lcqi_modal").removeClass("on");
      if (_mdCb) { _mdCb(); _mdCb = null; }
    },
    _mCl: function() {
      $("#lcqi_modal").removeClass("on");
    }
  };

  function _collectList() {
    var list = [];
    $(".lf-tbl input[type=checkbox]:checked").each(function() {
      var cb = this;
      if (!cb.dataset.sect || !cb.dataset.type || isPrevTy(cb.dataset.type)) return;
      var row = { yy: S.yy, orgid: S.orgid, pfltId: S.pfltId, cqiDivCd: "0002", cqiSectCd: cb.dataset.sect, cqiTypeCd: cb.dataset.type, choiCd: cb.dataset.choi, ansTypeCd: ((ETC_CD && cb.dataset.choi === ETC_CD) ? "SUBJ" : "OBJ"), cont: "", sortOrd: "0" };
      if (cb.dataset.isEtc === "1") {
        var e = document.getElementById("et_" + cb.dataset.sect + "_" + cb.dataset.type);
        if (e) row.cont = e.value.trim();
      }
      list.push(row);
    });
    $(".lf-tbl textarea.lf-subj").each(function() {
      var ta = this;
      if (!ta.dataset.sect || !ta.dataset.type || isPrevTy(ta.dataset.type)) return;
      if (!ta.value.trim()) return;
      list.push({ yy: S.yy, orgid: S.orgid, pfltId: S.pfltId, cqiDivCd: "0002", cqiSectCd: ta.dataset.sect, cqiTypeCd: ta.dataset.type, choiCd: "C107", ansTypeCd: "SUBJ", cont: ta.value.trim(), sortOrd: "0" });
    });
    return list;
  }

  function _limitObjSelection(sect, type, changedCb) {
    if (!sect || !type || isPrevTy(type)) return true;
    var selector = 'input[type=checkbox][data-sect="' + sect + '"][data-type="' + type + '"]:checked';
    var checked = document.querySelectorAll(selector);
    if (checked.length <= MAX_OBJ) return true;
    if (changedCb) changedCb.checked = false;
    _t("err", "⚠ 체크 항목은 최대 " + MAX_OBJ + "개까지 선택할 수 있습니다.");
    return false;
  }

  function _validateEtcRequired(sect, type) {
    var cb = document.querySelector('input[type=checkbox][data-sect="' + sect + '"][data-type="' + type + '"][data-is-etc="1"]');
    if (!cb || !cb.checked) return true;
    var et = document.getElementById("et_" + sect + "_" + type);
    var v = et ? et.value.trim() : "";
    if (v) return true;
    _t("err", "⚠ [기타]를 선택한 경우 기타 설명 입력이 필요합니다.");
    if (et) { et.classList.add("on"); et.focus(); }
    return false;
  }

  function _validateBeforeSend() {
    for (var i = 0; i < SC.length; i++) {
      for (var j = 0; j < EDIT_TY.length; j++) {
        var type = EDIT_TY[j];
        if (!_limitObjSelection(SC[i], type, null)) return false;
        if (!_validateEtcRequired(SC[i], type)) return false;
      }
    }
    return true;
  }

  function _bindAreaHandlers(sect, type) {
    $('input[type=checkbox][data-sect="' + sect + '"][data-type="' + type + '"]').each(function() {
      var cb = this;
      if (cb.dataset.isEtc === "1") return;
      $(cb).off("change").on("change", function() {
        _limitObjSelection(sect, type, cb);
        if (window.LF_Dirty) window.LF_Dirty[String(currentTabIndex())] = true;
      });
    });
    var et = document.getElementById("et_" + sect + "_" + type);
    if (et) $(et).off("blur").on("blur", function() {
      _validateEtcRequired(sect, type);
      if (window.LF_Dirty) window.LF_Dirty[String(currentTabIndex())] = true;
    });
  }
  $(document).off("input.lfDirty change.lfDirty").on("input.lfDirty change.lfDirty", ".tab_cont.lf-pane textarea, .tab_cont.lf-pane input[type=text]", function() {
    if (window.LF_Dirty) window.LF_Dirty[String(currentTabIndex())] = true;
  });

  function _val() {
    var nm = SECTS.map(function(s, idx) { return (idx + 1) + "." + (s.nm || s.cd); });
    var em = [];
    for (var i = 0; i < SC.length; i++) {
      var sc = SC[i];
      var sel = EDIT_TY.map(function(t) { return 'input[type=checkbox][data-sect="' + sc + '"][data-type="' + t + '"]:checked'; }).join(", ");
      var hc = document.querySelector(sel);
      var ht = false;
      document.querySelectorAll('textarea[data-sect="' + sc + '"]').forEach(function(t) {
        if (!isPrevTy(t.dataset.type) && t.value.trim() !== "") ht = true;
      });
      if (!hc && !ht) em.push(nm[i]);
    }
    return em;
  }

  function _ui() {
    var $badge = $("#lcqiStatusBadge");
    var $saveBtn = $("#saveBtn");
    var $submitBtn = $("#submitBtn");
    var $cancelBtn = $("#cancelBtn");
    var $saveBtnWrap = $("#saveBtnWrap");
    var $submitBtnWrap = $("#submitBtnWrap");
    var $cancelBtnWrap = $("#cancelBtnWrap");
    var $notice = $("#lcqiNotice");

    if (window.LF_TAB_LOCKED) {
      $badge.text("작성중").removeClass("lcqi-sub lcqi-cls");
      $notice.text("교양 Macro-CQI 작성 기간이 아닙니다.");
      $saveBtn.prop("disabled", true);
      $submitBtn.prop("disabled", true);
      $cancelBtn.prop("disabled", true);
      $submitBtnWrap.show();
      $cancelBtnWrap.hide();
      _sd(true);
      if (typeof $saveBtn.enable === "function") {
        $saveBtn.enable(false);
        $submitBtn.enable(false);
        $cancelBtn.enable(false);
      }
      return;
    }

    if (S.closeYn === "Y") {
      $badge.text("마감").removeClass("lcqi-sub").addClass("lcqi-cls");
      $notice.text("마감된 CQI입니다. 내용을 수정할 수 없습니다.");
      $saveBtn.prop("disabled", true);
      $submitBtn.prop("disabled", true);
      $submitBtnWrap.hide();
      $cancelBtnWrap.hide();
      _sd(true);
    } else if (S.submitYn === "Y") {
      $badge.text("제출완료").removeClass("lcqi-cls").addClass("lcqi-sub");
      $notice.text("제출이 완료되었습니다. 수정이 필요하면 [제출취소] 후 수정해 주세요.");
      $saveBtn.prop("disabled", true);
      $submitBtnWrap.hide();
      $cancelBtnWrap.show();
      _sd(true);
    } else {
      $badge.text("작성중").removeClass("lcqi-sub lcqi-cls");
      $notice.text("※ 각 탭의 내용을 작성 후 반드시 [저장] 버튼을 누르고 [제출] 해 주세요.");
      $saveBtn.prop("disabled", false);
      $submitBtnWrap.show();
      $submitBtn.prop("disabled", false);
      $cancelBtnWrap.hide();
      _sd(false);
      _lk();
    }
    if (typeof $saveBtn.enable === "function") {
      $saveBtn.enable(!$saveBtn.prop("disabled"));
      $submitBtn.enable(!$submitBtn.prop("disabled"));
      $cancelBtn.enable(true);
    }
  }

  function _lk() {
    PREV_TY_CD.forEach(function(ty) {
      document.querySelectorAll('.lf-tbl [data-type="' + ty + '"]').forEach(function(e) { e.disabled = true; });
    });
  }

  function _sd(d) {
    document.querySelectorAll('.lf-tbl input[type=checkbox], .lf-tbl textarea.lf-subj, .lf-tbl input.lf-etc').forEach(function(e) {
      if (!e.dataset.type || isPrevTy(e.dataset.type)) return;
      e.disabled = d;
    });
  }

  function _reset() {
    for (var i = 0; i < SC.length; i++) {
      for (var j = 0; j < TY.length; j++) {
        _rdr(SC[i], TY[j], [], isPrevTy(TY[j]));
      }
    }
    S.submitYn = "N";
    S.closeYn = "N";
    S.loaded = false;
  }

  function _errMsg(xhr, label) {
    var st = (xhr && xhr.status) ? xhr.status : "";
    var rt = (xhr && (xhr.responseText || xhr.responseJSON)) ? (xhr.responseText || "") : "";
    if (rt && typeof rt === "string") {
      rt = rt.replace(/\s+/g, " ").trim();
      if (rt.length > 80) rt = rt.substring(0, 80) + "...";
    } else {
      rt = "";
    }
    return "⚠ " + (label || "서버 통신 오류") + (st ? " (HTTP " + st + ")" : "") + (rt ? " - " + rt : "");
  }

  function _get(url, params, cb) {
    var qs = $.param(params || {});
    var fullUrl = _ctx + url + (url.indexOf("?") >= 0 ? "&" : "?") + qs;
    $.ajax({
      url: fullUrl,
      type: "GET",
      dataType: "json",
      success: function(d) { if (cb) cb(d || {}); },
      error: function(xhr) { _t("err", _errMsg(xhr, "서버 통신 오류")); }
    });
  }

  function _post(url, data, cb) {
    $.ajax({
      url: _ctx + url,
      type: "POST",
      data: data,
      dataType: "json",
      traditional: true,
      success: function(d) { if (cb) cb(d || {}); },
      error: function(xhr) { _t("err", _errMsg(xhr, "서버 통신 오류")); }
    });
  }

  function _postJson(url, payload, cb) {
    $.ajax({
      url: _ctx + url,
      type: "POST",
      contentType: "application/json; charset=UTF-8",
      data: JSON.stringify(payload || {}),
      dataType: "json",
      success: function(d) { if (cb) cb(d || {}); },
      error: function(xhr) { _t("err", _errMsg(xhr, "서버 통신 오류")); }
    });
  }

  function _esc(v) {
    return String(v || "").replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;").replace(/"/g, "&quot;");
  }

  function _t(ty, msg) {
    var $toast = $("#lcqi_toast");
    var $e = $("<div class='lcqi-toast-msg " + ty + "'>" + msg + "</div>");
    $toast.append($e);
    setTimeout(function() {
      $e.css({ transition: "opacity .3s", opacity: 0 });
      setTimeout(function() { $e.remove(); }, 320);
    }, 3000);
  }

  function _m(ic, ti, ms, cb) {
    $("#lcqi_modal_icon").text(ic);
    $("#lcqi_modal_title").text(ti);
    $("#lcqi_modal_body").html(ms.replace(/\n/g, "<br>"));
    $("#lcqi_modal_cancel").toggle(!!cb);
    $("#lcqi_modal").addClass("on");
    _mdCb = cb;
  }

  $("#lcqi_modal_ok").on("click", function() { LF._mOk(); });
  $("#lcqi_modal_cancel").on("click", function() { LF._mCl(); });
  $("#lcqi_modal").on("click", function(e) { if (e.target === this) LF._mCl(); });

  $(".tab_cont.lf-pane").hide();
  // 첫 탭 노출
  $("#tab-1").show();
})();
</script>
