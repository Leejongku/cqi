<%--
  Class Name : mcqiContDscForm.jsp
  Description : 전공 Macro-CQI 작성 화면 (진입: /univ/mcqi/mcqi/views/findMcqiContDscForm.do)
  조회조건: 년도, 캠퍼스, 단과대학, 학과명, 전공명
  0. 학과 역량 설정 탭 추가 (MCQI_COMP_DSC)
--%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp" %>
<script type="text/javascript">var _ctx = '${pageContext.request.contextPath}';</script>

<c:set var="submitYnForEdit" value="N"/>
<c:if test="${!empty mcqiContDscList}">
  <c:set var="submitYnForEdit" value="${mcqiContDscList[0].submitYn != null ? mcqiContDscList[0].submitYn : 'N'}"/>
  <c:set var="firstRow" value="${mcqiContDscList[0]}"/>
</c:if>
<c:set var="hasFirstRow" value="${!empty mcqiContDscList}"/>

<script type="text/javascript">
  /* 역량코드 목록 - d3CoreAblyBssList 순서와 동일 (동적으로 서버에서 생성) */
  var D3_ABLY_CODES = [<c:forEach var="ably" items="${d3CoreAblyBssList}" varStatus="st">"${fn:escapeXml(ably.d3CoreAblyCd)}"<c:if test="${!st.last}">,</c:if></c:forEach>];
  var relatWokCd, acsAlRngCd, pgmId;
  $(document).ready(function() {
    // 최초 진입 시 탭 클릭 잠금(조회 완료 후 해제)
    window.MCQI_TAB_LOCKED = true;
    window.MCQI_PERIOD_LOCKED = ("${cnt}" === "0");

    // 탭별 변경 여부(dirty) 관리: 동적 렌더링에도 안전하게 이벤트 위임으로 처리
    window.MCQI_Dirty = window.MCQI_Dirty || (function() {
      var _dirty = {};
      function _tabIdxFromEl(el) {
        var $p = $(el).closest(".tab_cont.mcqi-pane");
        if (!$p.length) return null;
        var id = $p.attr("id") || "";
        // tab-1 => 0, tab-2 => 1 ...
        var n = parseInt(String(id).replace("tab-", ""), 10);
        if (isNaN(n)) return null;
        return n - 1;
      }
      return {
        mark: function(idx) { if (idx != null) _dirty[String(idx)] = true; },
        markFromEl: function(el) { this.mark(_tabIdxFromEl(el)); },
        isDirty: function(idx) { return !!_dirty[String(idx)]; },
        clearAll: function() { _dirty = {}; }
      };
    })();

    var pgmInfo = $.getPgmInfo(location);
    if (pgmInfo) {
      relatWokCd = pgmInfo.relatWokCd;
      acsAlRngCd = pgmInfo.acsAlRngCd;
      pgmId = pgmInfo.pgmId;
      $("#tcqiSearch #relatWokCd").val(relatWokCd);
      $("#tcqiSearch #acsAlRngCd").val(acsAlRngCd);
    }
    $("#yy").parent("div").addClass("form_required");
    $("#deptLoctCd").parent("div").addClass("form_required");
    $("#collCd").parent("div").addClass("form_required");
    $("#dpmtCd").parent("div").addClass("form_required");
    $("#mjCd").parent("div").addClass("form_required");

    $("#tcqiSearch").validate({
      rules: {
        yy: { required: true },
        deptLoctCd: { required: true },
        collCd: { required: true },
        dpmtCd: { required: true },
        mjCd: { required: true }
      },
      messages: {
        yy: { required: "년도는 필수입력값입니다." },
        deptLoctCd: { required: "캠퍼스를 선택해 주세요." },
        collCd: { required: "단과대학을 선택해 주세요." },
        dpmtCd: { required: "학과명을 선택해 주세요." },
        mjCd: { required: "전공명을 선택해 주세요." }
      }
    });

    function _syncLockedCollValue() {
      var $hidden = $("#collCdHidden");
      if (!$hidden.length) {
        $hidden = $("<input>").attr({ type: "hidden", id: "collCdHidden", name: "collCd" });
        $("#tcqiSearch").append($hidden);
      }
      $hidden.val($("#collCd").val() || "");
    }
    function _lockCollegeSelect() {
      _syncLockedCollValue();
      $("#collCd").prop("disabled", true);
      if ($("#collCd").hasClass("chosen-select")) $("#collCd").trigger("chosen:updated");
    }

    $("#btn_search").on("click", function(e) {
      e.preventDefault();
      if ($("#tcqiSearch").valid()) {
        _syncLockedCollValue();
        $("#tcqiSearch").attr("action", _ctx + "/univ/mcqi/mcqi/views/findMcqiContDscForm.do").submit();
      }
    });

    function _switchTab(idx, btn) {
      // 헤더(탭 버튼) + 컨텐츠를 함께 동기화
      $(".tab_header li").removeClass("on");
      $(".tab_header .mcqi-tbtn").removeClass("on");
      var $b = btn ? $(btn) : $(".tab_header a[data-tab-index='" + idx + "']");
      $b.addClass("on");
      $b.closest("li").addClass("on");
      $(".tab_cont.mcqi-pane").removeClass("on").hide();
      var tabId = "tab-" + (idx + 1);
      $("#" + tabId).addClass("on").show();
    }
    function _currentTabIndex() {
      // 1순위: 실제 보이는/활성 패널 기준(가장 신뢰도 높음)
      try {
        var $p = $(".tab_cont.mcqi-pane.on:visible").first();
        if (!$p.length) $p = $(".tab_cont.mcqi-pane.on").first();
        if ($p.length) {
          var id = String($p.attr("id") || "");
          var n = parseInt(id.replace("tab-", ""), 10);
          if (!isNaN(n) && n > 0) return n - 1;
        }
      } catch (ignore) {}
      // 2순위: URL hash
      try {
        var h = (window.location.hash || "").replace("#", "");
        if (h.indexOf("tab-") === 0) {
          var m = parseInt(h.replace("tab-", ""), 10);
          if (!isNaN(m) && m > 0) return m - 1;
        }
      } catch (ignore2) {}
      // 3순위: 헤더 on 클래스
      try {
        var a = parseInt($(".tab_header .mcqi-tbtn.on").data("tabIndex"), 10);
        if (!isNaN(a)) return a;
      } catch (ignore3) {}
      return 0;
    }

    // "탭 전환하면서 저장" 케이스에만 1회성으로 다음 탭을 기억했다가,
    // 저장 후 재로딩되면 해당 탭으로 이동하고 즉시 삭제(충돌 방지)
    function _setNextTabOnce(idx) {
      try { sessionStorage.setItem("mcqi_next_tab", String(idx)); } catch (ignore) {}
    }
    function _peekNextTabOnce() {
      try {
        return sessionStorage.getItem("mcqi_next_tab");
      } catch (ignore) { return null; }
    }
    function _clearNextTabOnce() {
      try { sessionStorage.removeItem("mcqi_next_tab"); } catch (ignore) {}
    }

    function _isSubmittedFlag(v) {
      return String(v || "").toUpperCase() === "Y" || String(v || "") === "1";
    }
    function _applyLock(submitYn, closeYn) {
      var locked = _isSubmittedFlag(submitYn) || _isSubmittedFlag(closeYn);
      if (!locked) return;
      // 입력 잠금
      $("#mcqiContDscList input[type='checkbox']").prop("disabled", true);
      $("#mcqiContDscList textarea").prop("readonly", true);
      $("#mcqiContDscList input[type='text']").prop("disabled", true);
      // 버튼 상태
      $("#saveBtn, #submitBtn").prop("disabled", true);
      if (typeof $("#saveBtn").enable === "function") { $("#saveBtn").enable(false); $("#submitBtn").enable(false); }
      $("#cancelBtn").prop("disabled", false);
      if (typeof $("#cancelBtn").enable === "function") $("#cancelBtn").enable(true);
      // UI 영역 노출
      $("#cancelBtnWrap").show();
      $("#submitBtnWrap").hide();
    }

    // 탭 내부 입력 변경 감지(0탭 + 영역탭 모두 포함)
    $(document).off("change.mcqiDirty input.mcqiDirty").on("change.mcqiDirty input.mcqiDirty", ".tab_cont.mcqi-pane :input", function() {
      try { if (window.MCQI_Dirty) window.MCQI_Dirty.markFromEl(this); } catch (ignore) {}
    });

    $(".tab_header a[data-tab-index]").on("click", function(e) {
      e.preventDefault();
      var idx = parseInt($(this).data("tabIndex"), 10);
      if (window.MCQI_TAB_LOCKED) {
        alert(window.MCQI_PERIOD_LOCKED ? "전공 Macro-CQI 작성 기간이 아닙니다." : "먼저 조회 후 탭 이동이 가능합니다.");
        e.stopPropagation();
        e.stopImmediatePropagation();
        return false;
      }
      // 저장 직후 탭 복원 클릭인 경우 confirm 스킵
      if (window._mcqi_noConfirm) {
        _switchTab(idx, this);
        return;
      }
      // 현재 탭에 변경사항이 있을 때만 저장 확인
      try {
        var curIdx = _currentTabIndex();
        if (window.MCQI_Dirty && window.MCQI_Dirty.isDirty(curIdx)) {
          if (confirm("저장하시겠습니까?")) {
            MCQI.save({ skipConfirm: true, afterSaveTab: idx });
            return;
          }
        }
      } catch (ignore) {}
      _switchTab(idx, this);
    });

    $("#submitBtn").on("click", function(e) { e.preventDefault(); MCQI.submit(); });
    $("#cancelBtn").on("click", function(e) { e.preventDefault(); MCQI.submitCancel(); });
    $("#saveBtn").on("click", function(e) {
      e.preventDefault();
      MCQI.save({ afterSaveTab: _currentTabIndex() });
    });

    $("#deptLoctCd").on("change", function() { setOrgz(); });
    $("#collCd").on("change", function() { setDpmt(); });
    $("#dpmtCd").on("change", function() { setMajor(); });

    // 이전 조회조건 복원
    $("#deptLoctCd").val("${fn:escapeXml(tcqiSearch.deptLoctCd)}");
    setOrgz();
    $("#collCd").val("${fn:escapeXml(tcqiSearch.collCd)}");
    _syncLockedCollValue();
    setDpmt();
    $("#dpmtCd").val("${fn:escapeXml(tcqiSearch.dpmtCd)}");
    setMajor();
    $("#mjCd").val("${fn:escapeXml(tcqiSearch.mjCd)}");
    if ($("#deptLoctCd").hasClass("chosen-select")) $("#deptLoctCd").trigger("chosen:updated");
    if ($("#collCd").hasClass("chosen-select")) $("#collCd").trigger("chosen:updated");
    if ($("#dpmtCd").hasClass("chosen-select")) $("#dpmtCd").trigger("chosen:updated");
    if ($("#mjCd").hasClass("chosen-select")) $("#mjCd").trigger("chosen:updated");
    _lockCollegeSelect();

    var periodCheck = "${cnt}";
    if (periodCheck !== "0") {
      if ("${submitYnForEdit}" !== "Y" && "${submitYnForEdit}" !== "1") {
        $("#mcqiContDscList input[type='checkbox']").prop("disabled", false);
        $("#mcqiContDscList textarea:not([name*='bfrCont'])").prop("readonly", false);
        $("#submitBtn").prop("disabled", false);
        $("#saveBtn").prop("disabled", false);
        if (typeof $("#saveBtn").enable === "function") { $("#saveBtn").enable(true); $("#submitBtn").enable(true); }
      } else {
        $("#cancelBtn").prop("disabled", false);
        if (typeof $("#cancelBtn").enable === "function") $("#cancelBtn").enable(true);
      }
    } else {
      alert("전공 Macro-CQI 작성 기간이 아닙니다.");
      window.MCQI_TAB_LOCKED = true;
      $("#saveBtn, #submitBtn, #cancelBtn").prop("disabled", true);
      if (typeof $("#saveBtn").enable === "function") {
        $("#saveBtn").enable(false);
        $("#submitBtn").enable(false);
        $("#cancelBtn").enable(false);
      }
    }
    // 서버 렌더 기준으로도 제출완료 시 즉시 잠금
    _applyLock("${submitYnForEdit}", "");
    if (periodCheck !== "0" && $("#mcqiContDscList #yy").val() && $("#mcqiContDscList #orgid").val() && typeof MCQI !== "undefined" && MCQI.sectSearch) {
      MCQI.sectSearch();
    }

    // 이전 버전(sessionStorage 기반 탭 복원)은 사용하지 않음(탭 충돌 방지)

  });

  function setOrgz() {
    var param = "deptLoctCd=" + $("#deptLoctCd").val() + "&relatWokCd=" + relatWokCd + "&acsAlRngCd=" + acsAlRngCd + "&pgmId=" + pgmId;
    var $el = $("#collCd");
    $el.find("option:not(:first)").remove();
    $.ajax({ dataType: "json", type: "post", async: false, url: _ctx + "/comm/syst/cdmg/findUnivOrgzBasList.do", data: param,
      success: function(data) {
        if (data && data.univOrgzBasList) {
          $.each(data.univOrgzBasList, function(i, o) { $el.append($("<option></option>").val(o.orgid).text(o.orgzNm)); });
        }
        $("#collCdHidden").val($el.val() || "");
      }
    });
  }
  function setDpmt() {
    var param = "deptLoctCd=" + $("#deptLoctCd").val() + "&orgid=" + $("#collCd").val() + "&relatWokCd=" + relatWokCd + "&acsAlRngCd=" + acsAlRngCd + "&pgmId=" + pgmId;
    var $el = $("#dpmtCd");
    $el.find("option:not(:first)").remove();
    $.ajax({ dataType: "json", type: "post", async: false, url: _ctx + "/comm/syst/cdmg/findLinkageOrgList.do", data: param,
      success: function(data) {
        if (data && data.orgList) {
          $.each(data.orgList, function(i, o) { $el.append($("<option></option>").val(o.orgid).text(o.orgzNm)); });
        }
      }
    });
  }
  function setMajor() {
    var param = "deptLoctCd=" + $("#deptLoctCd").val() + "&orgid=" + $("#dpmtCd").val() + "&relatWokCd=" + relatWokCd + "&acsAlRngCd=" + acsAlRngCd + "&pgmId=" + pgmId;
    var $el = $("#mjCd");
    $el.find("option:not(:first)").remove();
    $.ajax({ dataType: "json", type: "post", async: false, url: _ctx + "/comm/syst/cdmg/findLinkageOrgList.do", data: param,
      success: function(data) {
        if (data && data.orgList) {
          $.each(data.orgList, function(i, o) {
            $el.append($("<option></option>").val(o.orgid).text(o.orgzNm));
          });
        }
        if ($el.hasClass("chosen-select")) {
          $el.trigger("chosen:updated");
        }
      }
    });
  }

  function _mcqiCurrentTabIndex() {
    var $p = $(".tab_cont.mcqi-pane.on:visible").first();
    if (!$p.length) $p = $(".tab_cont.mcqi-pane.on").first();
    if ($p.length) {
      var id = String($p.attr("id") || "");
      var n = parseInt(id.replace("tab-", ""), 10);
      if (!isNaN(n) && n > 0) return n - 1;
    }
    var a = parseInt($(".tab_header .mcqi-tbtn.on").data("tabIndex"), 10);
    return isNaN(a) ? 0 : a;
  }
  function _mcqiSwitchTab(idx) {
    idx = parseInt(idx, 10);
    if (isNaN(idx) || idx < 0) idx = 0;
    $(".tab_header li").removeClass("on");
    $(".tab_header .mcqi-tbtn").removeClass("on");
    var $b = $(".tab_header a[data-tab-index='" + idx + "']");
    $b.addClass("on");
    $b.closest("li").addClass("on");
    $(".tab_cont.mcqi-pane").removeClass("on").hide();
    $("#tab-" + (idx + 1)).addClass("on").show();
  }

  var MCQI = {
    save: function(opts) {
      opts = opts || {};
      if (!opts.skipConfirm) {
        if (!confirm("저장하시겠습니까?")) return;
      }
      var keepIdx = (opts.afterSaveTab != null) ? opts.afterSaveTab : _mcqiCurrentTabIndex();
      var yy = $("#mcqiContDscList #yy").val();
      var orgid = $("#mcqiContDscList #orgid").val();
      if (typeof MCQI.S === "function") { MCQI.S(); }
      var sectList = (typeof MCQI._collectList === "function") ? MCQI._collectList() : [];
      $.ajax({ url: _ctx + "/univ/mcqi/mcqi/saveMcqiSect.do", type: "POST", contentType: "application/json; charset=UTF-8", data: JSON.stringify({ yy: yy, orgid: orgid, sectList: sectList }), dataType: "json",
        success: function(data) {
          if (data && data.msg && data.msg !== "") { alert(data.msg); return; }
          $.ajax({ dataType: "json", type: "post", url: _ctx + "/univ/mcqi/mcqi/saveMcqiMaster.do",
            data: {
              yy: yy,
              orgid: orgid,
              cqiDivCd: "0001",
              rmk: $("#mcqiContDscList textarea[name='rmk']").first().val() || ""
            },
            success: function(dm) {
              if (dm && dm.msg && dm.msg !== "") { alert(dm.msg); return; }
          var compList = [];
          D3_ABLY_CODES.forEach(function(cd) {
            compList.push({
              yy: yy,
              orgid: orgid,
              cqiDivCd: "0001",
              cqiCompCd: cd,
              cqiCompYn: $("#mcqiContDscList input[name='compYn_" + cd + "']").is(":checked") ? "Y" : "N"
            });
          });
          $.ajax({ dataType: "json", type: "post", url: _ctx + "/univ/mcqi/mcqi/saveMcqiComp.do", contentType: "application/json; charset=UTF-8", data: JSON.stringify({ compList: compList }),
            success: function(d2) {
              if (d2 && d2.msg && d2.msg !== "") { alert(d2.msg); return; }
              alert("저장되었습니다.");
              try { if (window.MCQI_Dirty) window.MCQI_Dirty.clearAll(); } catch (ignore) {}
              // 공통 탭 플러그인이 활성 탭을 되돌리지 않도록
              // 직접 DOM을 건드리지 않고, 탭 <a>를 클릭해서 플러그인도 같이 동기화
              window._mcqi_noConfirm = true;
              setTimeout(function() {
                var $a = $(".tab_header a[data-tab-index='" + keepIdx + "']");
                if ($a.length) $a[0].click();
                window._mcqi_noConfirm = false;
              }, 50);
            },
            error: function() { alert("처리 중 문제가 발생했습니다."); }
          });
            },
            error: function() { alert("처리 중 문제가 발생했습니다."); }
          });
        },
        error: function() { alert("처리 중 문제가 발생했습니다."); }
      });
    },
    submit: function() {
      var d = [];
      D3_ABLY_CODES.forEach(function(cd) { if (d.length === 0 && $("#mcqiContDscList input[name='compYn_" + cd + "']").is(":checked")) { d.push(true); } });
      if (d.length === 0) { alert("0. 학과 역량 설정에서 항목을 선택해 주세요."); return; }
      if (typeof MCQI.S === "function") { MCQI.S(); }
      var sectList = (typeof MCQI._collectList === "function") ? MCQI._collectList() : [];
      if (!sectList || sectList.length === 0) {
        alert("탭 1~4 역량/개선 내용을 입력한 후 저장·제출해 주세요.");
        return;
      }
      if (!confirm("제출하시겠습니까?")) return;
      var yy = $("#mcqiContDscList #yy").val();
      var orgid = $("#mcqiContDscList #orgid").val();
      $.ajax({ url: _ctx + "/univ/mcqi/mcqi/saveMcqiSect.do", type: "POST", contentType: "application/json; charset=UTF-8", data: JSON.stringify({ yy: yy, orgid: orgid, sectList: sectList }), dataType: "json",
        success: function(data) {
          if (data && data.msg && data.msg !== "") { alert(data.msg); return; }
          $.ajax({ dataType: "json", type: "post", url: _ctx + "/univ/mcqi/mcqi/saveMcqiMaster.do",
            data: {
              yy: yy,
              orgid: orgid,
              cqiDivCd: "0001",
              rmk: $("#mcqiContDscList textarea[name='rmk']").first().val() || ""
            },
            success: function(dm) {
              if (dm && dm.msg && dm.msg !== "") { alert(dm.msg); return; }
          var compList = [];
          D3_ABLY_CODES.forEach(function(cd) {
            compList.push({
              yy: yy,
              orgid: orgid,
              cqiDivCd: "0001",
              cqiCompCd: cd,
              cqiCompYn: $("#mcqiContDscList input[name='compYn_" + cd + "']").is(":checked") ? "Y" : "N"
            });
          });
          $.ajax({ dataType: "json", type: "post", url: _ctx + "/univ/mcqi/mcqi/saveMcqiComp.do", contentType: "application/json; charset=UTF-8", data: JSON.stringify({ compList: compList }),
            success: function(d2) {
              if (d2 && d2.msg && d2.msg !== "") { alert(d2.msg); return; }
              $.ajax({ dataType: "json", type: "post", url: _ctx + "/univ/mcqi/mcqi/saveMcqiMasterSubmit.do",
                data: {
                  yy: yy,
                  orgid: orgid,
                  pfltId: (typeof MCQI.S === "function" ? MCQI.S().pfltId : ""),
                  cqiDivCd: "0001",
                  submitYn: (typeof MCQI.S === "function" ? MCQI.S().submitYn : "N"),
                  closeYn: (typeof MCQI.S === "function" ? MCQI.S().closeYn : "N")
                },
                success: function(d3) {
                  if (d3 && d3.msg && d3.msg !== "") { alert(d3.msg); return; }
                  alert("제출되었습니다.");
                  $("#tcqiSearch").attr("action", _ctx + "/univ/mcqi/mcqi/views/findMcqiContDscForm.do").submit();
                },
                error: function() { alert("제출 처리 중 문제가 발생했습니다."); }
              });
            },
            error: function() { alert("저장 중 오류가 발생했습니다."); }
          });
            },
            error: function() { alert("저장 중 오류가 발생했습니다."); }
          });
        },
        error: function() { alert("저장 중 오류가 발생했습니다."); }
      });
    },
    submitCancel: function() {
      if (!confirm("제출을 취소하시겠습니까?")) return;
      $.ajax({ dataType: "json", type: "post", url: _ctx + "/univ/mcqi/mcqi/saveMcqiMasterSubmitCnl.do",
        data: {
          yy: $("#mcqiContDscList #yy").val(),
          orgid: $("#mcqiContDscList #orgid").val(),
          pfltId: (typeof MCQI.S === "function" ? MCQI.S().pfltId : ""),
          cqiDivCd: "0001"
        },
        success: function(data) {
          if (data && data.msg && data.msg !== "") { alert(data.msg); return; }
          alert("제출취소되었습니다.");
          $("#tcqiSearch").attr("action", _ctx + "/univ/mcqi/mcqi/views/findMcqiContDscForm.do").submit();
        },
        error: function() { alert("처리 중 문제가 발생했습니다."); }
      });
    }
  };
</script>

<style>
  .lcqi-tbl { width:100%; border-collapse:collapse; font-size:13px; }
  .lcqi-tbl th, .lcqi-tbl td { border:1px solid #c8d4e4; padding:7px 10px; vertical-align:middle; }
  .lcqi-tbl thead th { background:#d4dff0; color:#1a4f91; font-weight:700; text-align:center; font-size:12px; }
  .lcqi-rh { background:#eef2f8; font-weight:700; color:#333; text-align:center; width:150px; white-space:normal; word-break:keep-all; font-size:12px; }
  tr.lcqi-prev td { background:#f4f6f9 !important; }
  .lcqi-subj { box-sizing:border-box; max-width:100%; width:100%; min-height:66px; border:1px solid #c0c8d8; border-radius:4px; padding:6px 9px; font-size:13px; resize:vertical; overflow-y:auto; }
  .lcqi-tbl td .lcqi-subj { display:block; }
  .lcqi-badge { font-size:11px; font-weight:700; padding:2px 8px; border-radius:10px; color:#fff; background:#f57c00; }
  .lcqi-badge.lcqi-sub { background:#2e7d32; }
  .lcqi-badge.lcqi-cls { background:#616161; }
  .lcqi-cg { display:grid; grid-template-columns:repeat(3,1fr); gap:4px 2px; padding:3px 0; }
  .lcqi-ci { display:flex; align-items:center; gap:5px; padding:3px 4px; border-radius:3px; }
  .lcqi-ci:hover:not(.lcqi-d) { background:#f0f4f8; }
  .lcqi-ci input[type=checkbox] { width:14px; height:14px; accent-color:#1a4f91; cursor:pointer; flex-shrink:0; }
  .lcqi-ci label { font-size:12px; color:#333; cursor:pointer; user-select:none; }
  .lcqi-ci.lcqi-d label, .lcqi-ci.lcqi-d input { cursor:not-allowed; color:#999; }
  .lcqi-etc { height:24px; border:1px solid #c0c8d8; border-radius:3px; padding:0 7px; font-size:12px; min-width:120px; max-width:220px; }
  .lcqi-carry { background:#f0f5ff; border:1px dashed #90aad4; border-radius:4px; padding:5px 11px; font-size:11px; color:#3a5a90; margin-bottom:8px; }
  #mcqi_toast { position:fixed; bottom:20px; right:20px; z-index:9999; display:flex; flex-direction:column; gap:6px; }
  .lcqi-toast-msg { padding:9px 16px; border-radius:5px; color:#fff; font-size:13px; box-shadow:0 4px 14px rgba(0,0,0,.2); }
  .lcqi-toast-msg.ok { background:#2e7d32; }
  .lcqi-toast-msg.err { background:#c62828; }
  .lcqi-toast-msg.info { background:#1565c0; }
  /* chosen dropdown highlight (대학도 학과/전공처럼 진하게) */
  .chosen-container .chosen-results li.highlighted { background:#1a4f91; }
</style>

<div class="section_tbl">
  <div class="section_mid_tit">
    <h4 class="tit_mid f_l">전공 Macro-CQI 작성</h4>
    <span id="mcqiStatusBadge" class="lcqi-badge">작성중</span>
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
        <col style="width: 14%;" />
        <col style="width: 16%;" />
        <col style="width: 16%;" />
        <col style="width: 34%;" />
        <col style="width: 120px;" />
      </colgroup>
      <tbody>
        <tr>
          <td>
            <div class="form_text form_required">
              <form:input path="yy" id="yy" placeholder="년도" data-inputmask-clearmaskonlostfocus="false" style="width:70px;" />
            </div>
          </td>
          <td>
            <div class="form_select form_required" style="width:90px;">
              <form:select path="deptLoctCd" id="deptLoctCd" class="chosen-select" title="캠퍼스" data-placeholder="캠퍼스">
                <option value="">캠퍼스</option>
                <option value="1">죽전</option>
                <option value="2">천안</option>
              </form:select>
            </div>
          </td>
          <td>
            <div class="form_select form_required" style="width:105px;">
              <form:select path="collCd" id="collCd" class="chosen-select" title="단과대학" data-placeholder="대학">
                <option value="">대학</option>
              </form:select>
            </div>
          </td>
          <td>
            <div class="form_select form_required" style="width:135px;">
              <form:select path="dpmtCd" id="dpmtCd" class="chosen-select" title="학과명" data-placeholder="학과명">
                <option value="">학과명</option>
              </form:select>
            </div>
          </td>
          <td>
            <div class="form_select form_required" style="width:240px;">
              <form:select path="mjCd" id="mjCd" class="chosen-select" title="전공명" data-placeholder="전공명">
                <option value="">전공명</option>
              </form:select>
            </div>
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

<form:form modelAttribute="mcqiContDsc" id="mcqiContDscList" method="post" class="form-inline">
  <c:choose>
    <c:when test="${!empty mcqiContDscList}">
      <input type="hidden" id="yy" name="yy" value="${mcqiContDscList[0].yy}" />
      <input type="hidden" id="orgid" name="orgid" value="${mcqiContDscList[0].orgid}" />
    </c:when>
    <c:otherwise>
      <input type="hidden" id="yy" name="yy" value="${fn:escapeXml(tcqiSearch.yy)}" />
      <input type="hidden" id="orgid" name="orgid" value="${fn:escapeXml(tcqiSearch.orgid)}" />
    </c:otherwise>
  </c:choose>

  <div id="tabs" class="tabs">
    <div class="tab_header_box">
      <ul class="tab_header f_l">
        <li><a href="#tab-1" class="mcqi-tbtn on" data-tab-index="0">0. 학과 역량 설정</a></li>
        <c:forEach var="sect" items="${cqiSectCdList}" varStatus="st">
          <li><a href="#tab-${st.index + 2}" class="mcqi-tbtn" data-tab-index="${st.index + 1}">${st.index + 1}. ${fn:escapeXml(sect.cmnCdvalNm)}</a></li>
        </c:forEach>
      </ul>
    </div>

    <!-- 0. 학과 역량 설정 -->
    <div class="tab_cont mcqi-pane on" id="tab-1">
      <div class="section_tbl">
        <div class="tbl_row" style="overflow-x: auto;">
          <table class="lcqi-tbl" style="width: max-content; min-width: 100%;">
            <thead>
              <tr>
                <th colspan="3">학과/전공 정보</th>
                <th colspan="${fn:length(d3CoreAblyBssList)}">D3 핵심역량</th>
                <th>변경<br/>사유</th>
              </tr>
              <tr>
                <th>캠퍼스</th>
                <th>단과대학</th>
                <th>학과/전공</th>
                <c:forEach var="ably" items="${d3CoreAblyBssList}">
                  <th>${fn:escapeXml(ably.d3CoreAblyNm)}</th>
                </c:forEach>
                <th></th>
              </tr>
            </thead>
            <tbody>
              <c:choose>
                <c:when test="${!empty mcqiContDscList}">
                  <%-- mcqiContDscList: Controller에서 compYn{역량코드} 키로 동적 pivot 주입됨 --%>
                  <c:forEach var="row" items="${mcqiContDscList}">
                    <tr>
                      <td class="ta_c">${fn:escapeXml(row.deptLoctNm)}</td>
                      <td class="ta_c">${fn:escapeXml(row.dpmtNm)}</td>
                      <td class="ta_l">${fn:escapeXml(row.orgzNm)}</td>
                      <c:forEach var="ably" items="${d3CoreAblyBssList}">
                        <c:set var="compKey" value="compyn${ably.d3CoreAblyCd}"/>
                        <td class="ta_c"><input type="checkbox" name="compYn_${ably.d3CoreAblyCd}" <c:if test="${row[compKey] eq 'Y'}">checked</c:if> /></td>
                      </c:forEach>
                      <td class="ta_c"><textarea name="rmk" id="rmk" rows="2" class="lcqi-subj">${fn:escapeXml(row.rmk)}</textarea></td>
                    </tr>
                  </c:forEach>
                </c:when>
                <c:when test="${empty mcqiContDscList and !empty mcqiCompList}">
                  <%-- 신규 입력: mcqiCompList(저장된 역량) 기준으로 체크 상태 초기화 --%>
                  <tr>
                    <td class="ta_c">${fn:escapeXml(tcqiSearch.deptLoctCd)}</td>
                    <td class="ta_c">${fn:escapeXml(tcqiSearch.dpmtCd)}</td>
                    <td class="ta_l">${fn:escapeXml(tcqiSearch.orgid)}</td>
                    <c:forEach var="ably" items="${d3CoreAblyBssList}">
                      <c:set var="ablyYn" value="N"/>
                      <c:forEach var="comp" items="${mcqiCompList}">
                        <c:if test="${comp.cqiCompCd eq ably.d3CoreAblyCd and comp.cqiCompYn eq 'Y'}">
                          <c:set var="ablyYn" value="Y"/>
                        </c:if>
                      </c:forEach>
                      <td class="ta_c"><input type="checkbox" name="compYn_${ably.d3CoreAblyCd}" <c:if test="${ablyYn eq 'Y'}">checked</c:if> /></td>
                    </c:forEach>
                    <td class="ta_c"><textarea name="rmk" id="rmk" rows="2" class="lcqi-subj"></textarea></td>
                  </tr>
                </c:when>
                <c:otherwise>
                  <tr><td class="ta_c" colspan="${3 + fn:length(d3CoreAblyBssList) + 1}">조회된 데이터가 없습니다.</td></tr>
                </c:otherwise>
              </c:choose>
            </tbody>
          </table>
        </div>
      </div>
    </div>

    <!-- 탭 1~4: lcqiContDscForm.jsp와 동일 로직(공통코드 기반 구분+체크항목) -->
    <c:forEach var="sect" items="${cqiSectCdList}" varStatus="st">
      <div class="tab_cont mcqi-pane" id="tab-${st.index + 2}" style="display:none;">
        <div class="lcqi-carry">ℹ 전년도 개선계획이 자동 이월됩니다. <b>전년도 개선계획</b> 행은 수정할 수 없습니다.</div>
        <div class="section_tbl">
          <div class="tbl_row">
            <table class="lcqi-tbl mcqi-tbl">
              <thead><tr><th style="width:100px;">구분</th><th>체크 항목 및 개선 내용</th></tr></thead>
              <tbody>
                <c:forEach var="ty" items="${cqiTypeCdList}">
                  <tr class="${ty.cmnCdval eq '0001' ? 'lcqi-prev' : ''}">
                    <td class="lcqi-rh">${fn:escapeXml(ty.cmnCdvalNm)}<c:if test="${ty.cmnCdval eq '0001'}"><span class="s">(이월)</span></c:if></td>
                    <td><div id="mc_${fn:escapeXml(sect.cmnCdval)}_${fn:escapeXml(ty.cmnCdval)}"></div></td>
                  </tr>
                </c:forEach>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </c:forEach>
  </div>

  <script type="application/json" id="mcqi_sects_json">[
    <c:forEach var="sect" items="${cqiSectCdList}" varStatus="st">
      <c:if test="${!st.first}">,</c:if>{"cd":"${fn:escapeXml(sect.cmnCdval)}","nm":"${fn:escapeXml(sect.cmnCdvalNm)}"}
    </c:forEach>
  ]</script>
  <script type="application/json" id="mcqi_types_json">[
    <c:forEach var="ty" items="${cqiTypeCdList}" varStatus="st">
      <c:if test="${!st.first}">,</c:if>{"cd":"${fn:escapeXml(ty.cmnCdval)}","nm":"${fn:escapeXml(ty.cmnCdvalNm)}"}
    </c:forEach>
  ]</script>
  <script type="application/json" id="mcqi_choi_json">[
    <c:forEach var="choi" items="${choiCdList}" varStatus="st">
      <c:if test="${!st.first}">,</c:if>{"cd":"${fn:escapeXml(choi.cmnCdval)}","nm":"${fn:escapeXml(choi.cmnCdvalNm)}"}
    </c:forEach>
  ]</script>

  <script type="text/javascript">
  (function() {
    "use strict";
    // 체크항목(CHOI_CD) 중 '비고'는 사용하지 않음 (비고는 textarea(SUBJ)로 입력)
    var CH = JSON.parse((document.getElementById("mcqi_choi_json") || {}).textContent || "[]")
              .filter(function(c) { return !(c.cd === "C007" || (c.nm && c.nm.indexOf("비고") >= 0)); });
    var ETC_CD = (function() { var x = CH.filter(function(c) { return c.cd === "C006" || (c.nm && c.nm.indexOf("기타") >= 0); }); return x.length ? x[0].cd : null; })();
    var SECTS = JSON.parse((document.getElementById("mcqi_sects_json") || {}).textContent || "[]");
    var TYPES = JSON.parse((document.getElementById("mcqi_types_json") || {}).textContent || "[]");
    var SC = SECTS.map(function(s) { return s.cd; });
    var TY = TYPES.map(function(t) { return t.cd; });
    // 각 탭(sect) + 구분(type)별 체크항목 최대 선택 수 (lcqi와 동일)
    var MAX_OBJ = 3;
    var PREV_TY_CD = TYPES.filter(function(t) {
      return t.cd === "0001" || t.cd === "1001" || (t.nm && (t.nm.indexOf("전년도") >= 0 || t.nm.indexOf("이월") >= 0));
    }).map(function(t) { return t.cd; });
    function isPrevTy(typeCd) { return PREV_TY_CD.indexOf(typeCd) >= 0; }
    var S = { yy: "", orgid: "", pfltId: "${fn:escapeXml(userEmpId != null ? userEmpId : '')}", submitYn: "N", closeYn: "N", loaded: false };
    function _esc(v) {
      return String(v || "").replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;").replace(/"/g, "&quot;");
    }
    function _t(ty, msg) {
      var $toast = $("#mcqi_toast");
      if (!$toast.length) return;
      var $e = $("<div class='lcqi-toast-msg " + ty + "'>" + msg + "</div>");
      $toast.append($e);
      setTimeout(function() { $e.css({ opacity: 0 }); setTimeout(function() { $e.remove(); }, 320); }, 3000);
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
    function _bindAreaHandlers(sect, type) {
      $('input[type=checkbox][data-sect="' + sect + '"][data-type="' + type + '"]').each(function() {
        var cb = this;
        $(cb).off("change").on("change", function() {
          // 최대 3개 제한
          _limitObjSelection(sect, type, cb);
          // 기타 텍스트박스 활성/비활성
          if (cb.dataset.isEtc === "1") {
            var e = document.getElementById("et_" + sect + "_" + type);
            if (!e) return;
            if (cb.checked) { e.disabled = false; e.focus(); }
            else { e.value = ""; e.disabled = true; }
          }
        });
        // 초기 상태 반영(이월 조회로 체크되어 렌더링된 경우)
        if (cb.dataset.isEtc === "1") {
          var e0 = document.getElementById("et_" + sect + "_" + type);
          if (e0) e0.disabled = !(cb.checked);
        }
      });
    }
    function _rdr(sc, ty, rows, disabled) {
      var el = document.getElementById("mc_" + sc + "_" + ty);
      if (!el) return;
      var chkd = rows
        .filter(function(r) { return r.ansTypeCd === "OBJ" || (ETC_CD && r.choiCd === ETC_CD); })
        .map(function(r) { return r.choiCd; });
      var sj = rows.find(function(r) { return r.ansTypeCd === "SUBJ" && r.choiCd === "C007"; });
      var et = ETC_CD ? rows.find(function(r) { return r.choiCd === ETC_CD; }) : null;
      var ds = disabled ? " disabled" : "";
      var dc = disabled ? " lcqi-d" : "";
      var ph = {};
      for (var i = 0; i < TYPES.length; i++) {
        var t = TYPES[i];
        if (t.cd === "0001") ph[t.cd] = "전년도 개선계획 내용 (이월 - 수정 불가)";
        else if (t.cd === "0002") ph[t.cd] = "반영된 개선사항 내용을 입력해 주세요.";
        else if (t.cd === "0003") ph[t.cd] = "이번년도 개선계획 내용을 입력해 주세요.";
        else ph[t.cd] = (t.nm || "내용을 입력해 주세요.");
      }
      var h = '<textarea class="lcqi-subj mcqi-subj" id="sj_' + sc + '_' + ty + '" data-sect="' + sc + '" data-type="' + ty + '" rows="3" placeholder="' + ph[ty] + '"' + ds + '>' + _esc(sj && sj.cont ? sj.cont : "") + '</textarea>';
      h += '<div class="lcqi-cg">';
      for (var i = 0; i < CH.length; i++) {
        var c = CH[i];
        var cid = "cb_" + sc + "_" + ty + "_" + c.cd;
        var chk = chkd.indexOf(c.cd) >= 0 ? " checked" : "";
        if (ETC_CD && c.cd === ETC_CD) {
          var eid = "et_" + sc + "_" + ty;
          var ev = (et && et.cont) ? _esc(et.cont) : "";
          var eds = (!chk || disabled) ? " disabled" : "";
          h += '<div class="lcqi-ci ' + dc + '"><input type="checkbox" id="' + cid + '" data-sect="' + sc + '" data-type="' + ty + '" data-choi="' + _esc(c.cd) + '" data-is-etc="1"' + chk + ds + '><label for="' + cid + '">' + _esc(c.nm) + '</label><input type="text" class="lcqi-etc" id="' + eid + '" value="' + ev + '"' + ds + eds + '></div>';
        } else {
          h += '<div class="lcqi-ci ' + dc + '"><input type="checkbox" id="' + cid + '" data-sect="' + sc + '" data-type="' + ty + '" data-choi="' + _esc(c.cd) + '"' + chk + ds + '><label for="' + cid + '">' + _esc(c.nm) + '</label></div>';
        }
      }
      h += "</div>";
      el.innerHTML = h;
      if (!disabled) _bindAreaHandlers(sc, ty);
    }
    function _collectList() {
      var list = [];
      $(".mcqi-tbl input[type=checkbox]:checked").each(function() {
        var cb = this;
        if (!cb.dataset.sect || !cb.dataset.type || cb.dataset.type === "0001") return;
        var row = { yy: S.yy, orgid: S.orgid, pfltId: S.pfltId, cqiDivCd: "0001", cqiSectCd: cb.dataset.sect, cqiTypeCd: cb.dataset.type, choiCd: cb.dataset.choi || "", ansTypeCd: ((ETC_CD && cb.dataset.choi === ETC_CD) ? "SUBJ" : "OBJ"), cont: "", sortOrd: "0" };
        if (cb.dataset.isEtc === "1") {
          var e = document.getElementById("et_" + cb.dataset.sect + "_" + cb.dataset.type);
          if (e) row.cont = e.value.trim();
        }
        list.push(row);
      });
      $(".mcqi-tbl textarea.mcqi-subj").each(function() {
        var ta = this;
        if (!ta.dataset.sect || !ta.dataset.type || ta.dataset.type === "0001") return;
        if (!ta.value.trim()) return;
        list.push({ yy: S.yy, orgid: S.orgid, pfltId: S.pfltId, cqiDivCd: "0001", cqiSectCd: ta.dataset.sect, cqiTypeCd: ta.dataset.type, choiCd: "C007", ansTypeCd: "SUBJ", cont: ta.value.trim(), sortOrd: "0" });
      });
      return list;
    }
    function sectSearch(done) {
      S.yy = $("#mcqiContDscList #yy").val();
      S.orgid = $("#mcqiContDscList #orgid").val();
      if (!S.yy || !S.orgid) return;
      if (window.MCQI_PERIOD_LOCKED) {
        window.MCQI_TAB_LOCKED = true;
        return;
      }
      // 조회 수행 시작 시 잠금, 조회 완료 시 해제
      window.MCQI_TAB_LOCKED = true;
      $.ajax({ url: _ctx + "/univ/mcqi/mcqi/findMcqiMaster.do", type: "GET", data: { yy: S.yy, orgid: S.orgid }, dataType: "json",
        success: function(d) {
          var m = d.mcqiMaster || {};
          S.submitYn = m.submitYn || "N";
          S.closeYn = m.closeYn || "N";
          // 제출/마감 상태면 즉시 수정 잠금
          try {
            var locked = (String(S.submitYn || "").toUpperCase() === "Y" || String(S.submitYn || "") === "1" ||
                          String(S.closeYn || "").toUpperCase() === "Y" || String(S.closeYn || "") === "1");
            if (locked) {
              $("#mcqiContDscList input[type='checkbox']").prop("disabled", true);
              $("#mcqiContDscList textarea").prop("readonly", true);
              $("#mcqiContDscList input[type='text']").prop("disabled", true);
              $("#saveBtn, #submitBtn").prop("disabled", true);
              if (typeof $("#saveBtn").enable === "function") { $("#saveBtn").enable(false); $("#submitBtn").enable(false); }
              $("#cancelBtn").prop("disabled", false);
              if (typeof $("#cancelBtn").enable === "function") $("#cancelBtn").enable(true);
              $("#cancelBtnWrap").show();
              $("#submitBtnWrap").hide();
            }
          } catch (ignore) {}
          $.ajax({ url: _ctx + "/univ/mcqi/mcqi/findMcqiSectListAll.do", type: "GET", data: { yy: S.yy, orgid: S.orgid }, dataType: "json",
            success: function(d2) {
              var cur = d2.mcqiSectList || [];
              $.ajax({ url: _ctx + "/univ/mcqi/mcqi/findMcqiPrevSectList.do", type: "POST", data: { yy: S.yy, orgid: S.orgid }, dataType: "json",
                success: function(d3) {
                  var prv = d3.mcqiPrevSectList || [];
                  for (var i = 0; i < SC.length; i++) {
                    var sc = SC[i];
                    for (var j = 0; j < TY.length; j++) {
                      var ty = TY[j];
                      if (ty === "0001") _rdr(sc, ty, prv.filter(function(r) { return r.cqiSectCd === sc; }), true);
                      else _rdr(sc, ty, cur.filter(function(r) { return r.cqiSectCd === sc && r.cqiTypeCd === ty; }), false);
                    }
                  }
                  S.loaded = true;
                  window.MCQI_TAB_LOCKED = false;
                  _t("info", "✔ 데이터를 조회하였습니다.");
                  if (typeof done === "function") done();
                }
              });
            }
          });
        }
      });
    }
    window.MCQI = window.MCQI || {};
    window.MCQI.sectSearch = sectSearch;
    window.MCQI._collectList = _collectList;
    window.MCQI.S = function() { S.yy = $("#mcqiContDscList #yy").val(); S.orgid = $("#mcqiContDscList #orgid").val(); return S; };
  })();
  </script>

  <div id="mcqi_toast"></div>
  <div class="submit-notice" style="color: red; font-size: 20px; font-weight: bold; text-align: center; margin-top: 30px;">
    각 탭의 필수 내용을 모두 작성 및 저장 후 반드시 [제출] 버튼을 클릭해 주십시오.
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
  <c:if test="${!empty mcqiContDscList && submitYnForEdit eq 'Y'}">
    <script type="text/javascript">$(function(){ $("#cancelBtnWrap").show(); $("#submitBtnWrap").hide(); });</script>
  </c:if>
</form:form>
