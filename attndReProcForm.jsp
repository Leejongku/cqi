<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ include file="/WEB-INF/includes/taglibs.jsp" %>

<script type="text/javascript">
var currSubjId="";          //과목
var currAttndDt="";         //일자
var currFrLesnTmId="";      //시작시간
var sbmUsPrtYn= "";         //최종성적완료여부

$(document).ready(function() {
    $("#yy").inputmask("9999");
    $("#semCd").selected("${bztmSearch.semCd}");

    //버튼명 - for 다국어
    $("#saveBtn").find(".btn_txt").text("<s:message code='lssn.univ.save'/>");

  //검색 유효성체크
    $("#bztmSearch").validate({
        rules: {
            yy: {
                required: true
            },
            semCd: {
                required: true
            }
        },
        messages: {
            yy: {
                required: "<s:message code='lssn.univ.req.yy'/>"/* 년도는 필수입력값입니다. */
            },
            semCd: {
                required: "<s:message code='lssn.univ.req.sem'/>"/* 학기는 필수입력값입니다. */
            }
        }
    });

    // 강의내역 table내에 있는 a click이벤트
    $(document).on("click", "#lecrLecCfmDscTbl a", function(e) {
        e.preventDefault();
        e.stopPropagation();
        var params = $(this).data("params");
        sbmUsPrtYn = $(this).parents("tr").find("[name$='sbmUsPrtYn']").val();

        $("#dateDtlDiv").hide();
        if( $(this).attr("href") !="#") {
	        $.getJSON("univ/lssn/bztm/findLecrLecReplList.do", params, function (data) {
	        	var $lecrLecReplTbl = $("#lecrLecReplTbl");
	            $lecrLecReplTbl.find("tbody").empty();
	            //검색 카운트,,
	            $("#lecrLecReplTotCnt").html(data.lecrLecCfmDscList.length);

	            if (data.lecrLecCfmDscList.length) {
	                $.each(data.lecrLecCfmDscList, function (index, entry) {
	                	var params =[];
	                	params.push(entry.attndDt2);
	                	params.push(entry.yy);
	                	params.push(entry.semCd);
	                	params.push(entry.subjId);
	                	params.push(entry.dvclsNb);
	                	params.push(entry.attndDt);
	                	params.push(entry.frLesnTmId);
	                	params.push(entry.repl);             //보강일/대강자
	                    params.push(entry.mklsYn);           //보강여부
	                    params.push(entry.lecNm);            //출강여부
	                    $lecrLecReplTbl.addRow("#lecrLecReplTpl",params);

	                    var $row = $lecrLecReplTbl.find("tbody tr:last");
	                    /*$row.find("td").eq(1).text(entry.attndDt);          //일자
	                    $row.find("td").eq(1).mask("date"); */
	                    $row.find("td").eq(2).text(entry.frLesnTmId);       //시작시간
	                    $row.find("td").eq(2).mask("hm");
	                    $row.find("td").eq(3).text(entry.toLesnTmId);       //종료시간
	                    $row.find("td").eq(3).mask("hm");
	                    $row.find("td").eq(4).text(entry.roomAll);          //강의실
	                    $.each(entry, function (name, value) {
	                        var $element = $row.find("[name$=" + name + "]");
	                   		if ($element.attr("type") == "checkbox") {
	                           	$element.prop("checked", value == 1? true : false);
	                        } else {
	                           	$element.val(value);
	                        }
	                        $row.find("[name$=status]").val("4");
	                    });

	                });

	              } else {
	                  $("<tr><td class='ta_c empty' colspan='8'><s:message code='lssn.univ.no.result'/></td></tr>").appendTo($lecrLecReplTbl).find("tbody");
	              }

	            $("#stdtAttndDiv").hide();
	            $("#lecrLecReplDiv").show();
	        });
	        $(this).closest("tr").addClass("tr_on");
	        $(this).closest("tr").siblings().removeClass();
        }
    });

    // 일자별목록 table내에 있는 a click이벤트
    $(document).on("click", "#lecrLecReplTbl a", function(e){
    	e.preventDefault();
        e.stopPropagation();
        if($(this).parents("tr").find("[name$='lectureYn']").val() == "0") {
        	alert("<s:message code='bztm.univ.no.compl'/>");/* 출강처리가 되지 않았습니다. */
        } else {
        	var params = $(this).data("params");

            var $dateDtlTbl = $("#dateDtlTbl");
            var $row = $dateDtlTbl.find("tbody tr:last");
            $row.find("td").eq(0).text($(this).parents("tr").find("[name$='atDate']").val());          //일자
            $row.find("td").eq(0).mask("date");
            $row.find("td").eq(1).text($(this).parents("tr").find("[name$='atFrTm']").val());       //시작시간
            $row.find("td").eq(1).mask("hm");
            $row.find("td").eq(2).text($(this).parents("tr").find("[name$='atToTm']").val());       //종료시간
            $row.find("td").eq(2).mask("hm");
            $row.find("td").eq(3).text($(this).parents("tr").find("[name$='roomAll']").val());           //강의실

            $("#dateDtlDiv").show();

            currSubjId      = $(this).parents("tr").find("[name$='subjId']").val();                           //과목
            currAttndDt     = $(this).parents("tr").find("[name$='attndDt']").val();         //일자
            currFrLesnTmId  = $(this).parents("tr").find("[name$='frLesnTmId']").val();                       //시작시간

            $.getJSON("univ/lssn/bztm/findTkcrsAplDscList.do", params, function (data) {
            	var $stdtAttndCfmDscTbl = $("#stdtAttndCfmDscTbl");
                $stdtAttndCfmDscTbl.find("tbody").empty();

                //검색 카운트,,
                $("#stdtAttndCfmDscTotCnt").html(data.tkcrsAplDscList.length);
                if (data.tkcrsAplDscList.length) {
                    $.each(data.tkcrsAplDscList, function (index, entry) {
                    	var params =[];
                    	params.push(entry.rmk);
                    	params.push(entry.coronaChk);
                        $stdtAttndCfmDscTbl.addRow("#stdtAttndCfmDscTpl",params);
                        var $row = $stdtAttndCfmDscTbl.find("tbody tr:last");
                         $.each(entry, function (name, value) {
                            var $element = $row.find("[name$=" + name + "]");
    						if(name=="attndTyCd") {
    							if(entry.rmk != null){
    		                		//$row.find("[name$=attndTyCd]").attr('disabled',true);

    		                	//	$row.find("[name$=attndTyCd]").parent("td").append("<input type='hidden' name='stdtAttndCfmDscList["+index+"].attndTyCd' value='0001'>");
    		                	}else{
    		                		$row.find("[name$=attndRecogCfmYn]").attr('disabled',true);
    		                	}
                            	if(value=="" || value==null)
                            		$row.find("[name$=attndTyCd]:radio[value='0001']").attr('checked',true);
                            	else{
                            		$row.find("[name$=attndTyCd]:radio[value='"+ value+"']").attr('checked',true);
                            	}
                            }else if ($element.attr("type") == "checkbox") {
	                           	$element.prop("checked", value == 1? true : false);
	                        }else {
                            	$element.val(value);
                            }
                            //$row.find("[name$=status]").val("4");

                        });
                    });
                    $("#stdtAttndCfmDscTbl").trigger("update")
                                          //  .tirigger("appendCache")
                                            .trigger("sorton",[ [[2,1],[0,0]] ]);
                } else {
                    $stdtAttndCfmDscTbl.addEmptyRow();
                }

            });

            if(sbmUsPrtYn == '1')
	            $("#stdtAttndDiv button").enable(false);
            else
            	$("#stdtAttndDiv button").enable(true);

            $("#stdtAttndDiv").show();
            $("#lecrLecReplDiv").hide();

        }

        $(this).closest("tr").addClass("tr_on");
        $(this).closest("tr").siblings().removeClass();
    });



    $(document).on("change", "[name$=attndTyCd]", function (e) {
    	var beforeAttndTyCd = $(this).parents("tr").find("[name$='beforeAttndTyCd']").val();
    	var attndTyCd       = $(this).val();
	 	if(beforeAttndTyCd != attndTyCd){
    		$(this).parents("tr").find("[name$='attndChgRsn']").addClass("form_text form_required").attr("readonly", false);
    		$(this).parents("tr").find("[name$='status']").val("4");
    		$(this).parents("tr").find("[name$='attndChgRsn']").focus();
    	}else{
    		$(this).parents("tr").find("[name$='attndChgRsn']").removeClass("form_text form_required").attr("readonly", true);
    		$(this).parents("tr").find("[name$='status']").val("");
    		$(this).parents("tr").find("[name$='attndChgRsn']").val("");
    	}
    });




    if ("${fn:length(lctDscList)}">0) {
        //저장 시 foreign key currCtteId 값을 assign한다.
        if (("${param.currSubjId}") && ("${param.currDvclsNb}")) {
            $("#lecrLecCfmDscTbl tr").each(function(index) {
            	var subjId  = $.trim($(this).find("[name$=subjId]").val());
                var dvclsNb = $.trim($(this).find("[name$=dvclsNb]").val());
                if (subjId=="${param.currSubjId}" && dvclsNb=="${param.currDvclsNb}") {
                    $("#lecrLecCfmDscTbl tr:eq("+(index)+") a").trigger('click');
                }
            });
        } else {
            $("#lecrLecCfmDscTbl tr:eq(1) a").trigger('click');
        }
    }





    $("#lecrLecCfmDscForm").validate({
        submitHandler: function(form) {
        	var checked = true;
        	$("#lecrLecCfmDscForm .form_required").each(function(index){
        		var beforeAttndChgRsn = $(this).parents("tr").find("[name$='beforeAttndChgRsn']").val();
            	var attndChgRsn       = $(this).val();

	       		 if(attndChgRsn== null || attndChgRsn == ""){
	    			 alert("출석이 변경된 학생은 반드시 출석변경사유를 입력하셔야 합니다.");
	    			 $(this).focus();
	    			 checked= false;
	    		 	return checked;
	    		 }
	       	 	if(beforeAttndChgRsn == attndChgRsn){
	       	 		alert("출석변경사유가 이전과 동일 합니다.");
	       			 $(this).focus();
	       			 checked= false;
	       		 	return checked;
	           	}



        	})

		if(checked)

          if (!confirm("<s:message code='lssn.univ.conf.save'/>")) {/* 저장 하시겠습니까? */
                return false;
            } else {
            	// submit 전 미리 저장해놓은 currSubjId, currDvclsNb 값을 assign한다.
                $('<input>').attr({type: 'hidden', name: 'currSubjId',  value: $(".tr_on").find("[name$=subjId]").val()}).appendTo(form);
                $('<input>').attr({type: 'hidden', name: 'currDvclsNb', value: $(".tr_on").find("[name$=dvclsNb]").val()}).appendTo(form);
                $(form).unmask();
                form.submit();
            }
        }
    });

    // 출석부
    $(".print").click(function(e){
    	e.stopPropagation();
		e.preventDefault();
		var fileNm ="lssn/";
		var txt = $(this).parents("tr").find("[name$='attandYn']").val();
		if(txt == "불가"){
			fileNm += "attndCoverBodyEmpty";
		} else {
			fileNm += "attndCoverBody";
		}
		var params = {
                 "yy"        : "${bztmSearch.yy}"
                ,"semCd"     : "${bztmSearch.semCd}"
				,"opOrgid"   : $(this).parents("tr").find("[name$='opOrgid']").val()
				,"subjInfo"  : "${userDetails.intgUid}" +  $(this).parents("tr").find("[name$='subjId']").val() + $(this).parents("tr").find("[name$='dvclsNb']").val()
				,"prnCondi"  : "1"    // 표지 1: 포함 0:비포함
				,"cretYn"    : "0"    // 성적 1: 포함 0:비포함
				,"titleNm"   : "출 석 부"
		};

		var objParam = {params : params};
		$.openReport(fileNm, objParam);
	});

    // 출석부 자료 변환용
    $(".print_cover").click(function(e){
    	e.stopPropagation();
		e.preventDefault();
		var fileNm ="lssn/";
		var txt = $(this).parents("tr").find("[name$='attandYn']").val();
		var cybLtrTyCd = $(this).parents("tr").find("[name$='cybLtrTyCd']").val();
		if(txt == "불가"  && (cybLtrTyCd != 0001 && cybLtrTyCd != 0006) ){
			fileNm += "attndBodyExcelEmpty";
		} else {
			fileNm += "attndBodyExcel";
		}
		var params = {
				 "yy"        : "${bztmSearch.yy}"
	            ,"semCd"     : "${bztmSearch.semCd}"
				,"opOrgid"   : $(this).parents("tr").find("[name$='opOrgid']").val()
				,"subjInfo"  : "${userDetails.intgUid}" + $(this).parents("tr").find("[name$='subjId']").val() + $(this).parents("tr").find("[name$='dvclsNb']").val()
				,"cretYn"    : "1"     // 성적 1: 포함 0:비포함
				,"titleNm"   : "출 석 부"
		};

		var objParam = {params : params};
        objParam.exportType = 3; // exportType : 1(PDF),2(한글),3(엑셀)
        //objParam.excelExportSheetOption = 0;
        $.exportReport(true,fileNm, objParam);

	});

    // 빈양식 출석부
    $(".print_empty").click(function(e){
        e.stopPropagation();
        e.preventDefault();
        var fileNm ="lssn/";
        fileNm += "attndCoverBodyEmpty";
        var params = {
                 "yy"        : "${bztmSearch.yy}"
                ,"semCd"     : "${bztmSearch.semCd}"
                ,"opOrgid"   : $(this).parents("tr").find("[name$='opOrgid']").val()
                ,"subjInfo"  : "${userDetails.intgUid}" +  $(this).parents("tr").find("[name$='subjId']").val() + $(this).parents("tr").find("[name$='dvclsNb']").val()
                ,"prnCondi"  : "1"      // 표지 1: 포함 0:비포함
                ,"cretYn"    : "0"      // 성적 1: 포함 0:비포함
                ,"titleNm"   : "출 석 부"
        };

        var objParam = {params : params};
        $.openReport(fileNm, objParam);
    });

    // 출석부
    $(".print_report").click(function(e){
    	e.stopPropagation();
		e.preventDefault();
		var fileNm ="lssn/";
		var txt = $(this).parents("tr").find("[name$='attandYn']").val();
		if(txt == "불가"){
			fileNm += "attndCoverBodyEmpty";
		} else {
			if (confirm("<s:message code='bztm.univ.cont.attd'/>"))/* 출결사항을 포함해서 출력하시겠습니까? */
    		{
				fileNm += "attndCoverBody";
            }
    		else
    		{
    			fileNm += "attndCoverBodyEmpty";
    		}

		}
		var params = {
                 "yy"        : "${bztmSearch.yy}"
                ,"semCd"     : "${bztmSearch.semCd}"
				,"opOrgid"   : $(this).parents("tr").find("[name$='opOrgid']").val()
				,"subjInfo"  : "${userDetails.intgUid}" +  $(this).parents("tr").find("[name$='subjId']").val() + $(this).parents("tr").find("[name$='dvclsNb']").val()
				,"prnCondi"  : "1"    // 표지 1: 포함 0:비포함
				,"cretYn"    : "1"    // 성적 1: 포함 0:비포함
				,"titleNm"   : "출 석 부"
		};

		var objParam = {params : params};
		$.openReport(fileNm, objParam);
	});

});
</script>


<!-- 검색 -->
<form:form modelAttribute="bztmSearch" method="post" class="form-inline" data-role="search" data-message="alert">
  <div class="tbl_search">
    <table>
      <caption>검색테이블</caption>
      <colgroup>
        <col style="width: 33%;" />
        <col style="width: 33%;" />
        <col />
        <col style="width: 120px;" />
      </colgroup>
      <tbody>
        <tr>
          <td>
            <div class="form_text form_required form_readonly">
              <form:input path="yy"  placeholder="년도" data-inputmask-clearmaskonlostfocus="false" />
            </div>
          </td>
          <td>
            <div class="form_select form_required">
              <form:select path="semCd" class="chosen-select" data-placeholder="학기">
                <option value=""><s:message code='lssn.univ.sem'/></option><!-- 학기 -->
                <form:options items="${semCdList}" itemValue="cmnCdval" itemLabel="cmnCdvalNm" />
              </form:select>
            </div>
          </td>
          <td></td>
          <td class="search_td">
            <button class="btn_search">
              <strong>
                <span>
                  <img src="images/btn/btn_serach.png" alt="SEARCH" style="cursor:pointer;">
                </span>
              </strong>
            </button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</form:form>


<div class="section_tbl">
  <div class="section_mid_tit">
    <h4 class="tit_mid f_l"><s:message code='bztm.univ.lct.list'/></h4><!-- 강의내역 -->
    <p class="txt_number f_r"><s:message code='lssn.univ.search.rsl1'/> [ <em class="fc_orange"> ${fn:length(lctDscList)} </em> ] <s:message code='lssn.univ.search.rsl2'/></p><!-- 검색결과는 --><!-- 건 입니다. -->
  </div>
  <div class="tbl_column" style="overflow-x: hidden; overflow-y: auto; max-height: 218px;">
    <table id="lecrLecCfmDscTbl" class="tbl_mixed tbl_click">
      <caption>강의내역</caption>
      <colgroup>
        <col style="width: 25px;"  />
        <col style="width: 7%;" />
        <col style="width: 8%;" />
        <col style="width: 15%;"/>
        <col style="width: 4%;" />
        <col style="width: 4%;" />
        <col />
        <col style="width: 5%;" />
        <col style="width: 5%;" />
        <col style="width: 5%;" />
        <col style="width: 5%;" />
        <col style="width: 5%;" />
        <col style="width: 7%;" />
        <col style="width: 5%;" />
        <col style="width: 5%;" />
        <col style="width: 5%;" />
        <col style="width: 5%;" />
      </colgroup>
      <thead>
        <tr>
          <th scope="col">#</th>
          <th scope="col"><s:message code='lssn.univ.campus'/></th><!-- 캠퍼스 -->
          <th scope="col"><s:message code='lssn.univ.subj.id'/></th><!-- 교과목 -->
          <th scope="col"><s:message code='lssn.univ.subj.nm'/></th><!-- 과목명 -->
          <th scope="col"><s:message code='lssn.univ.dvcls'/></th><!-- 분반 -->
          <th scope="col"><s:message code='lssn.univ.crd'/></th><!-- 학점 -->
          <th scope="col"><s:message code='lssn.univ.dow.prod'/></th><!-- 요일/교시 -->
          <th scope="col" >1단계</th>
	      <th scope="col" >2단계</th>
	      <th scope="col" >3단계</th>
	      <th scope="col" >4단계</th>
          <th scope="col"><s:message code='bztm.univ.stu.cnt'/></th><!-- 학생수 -->
          <th scope="col"><s:message code='bztm.univ.lec.conf1'/><br><s:message code='bztm.univ.lec.conf2'/></th><!-- 출강확인 --><!-- 가능여부 -->
          <th scope="col"><s:message code='bztm.univ.attd.list'/></th><!-- 출석부 -->
          <th scope="col"><s:message code='bztm.univ.attd.list.excel1'/><br><s:message code='bztm.univ.attd.list.excel2'/><br><s:message code='bztm.univ.attd.list.excel3'/></th><!-- 출석부 --><!-- 자료 --><!-- 변환용 -->
          <th scope="col"><s:message code='bztm.univ.attd.list.blank1'/><br><s:message code='bztm.univ.attd.list.blank2'/></th><!-- 빈양식 --><!-- 출석부 -->
          <th scope="col"><s:message code='bztm.univ.attd.list.subm1'/><br><s:message code='bztm.univ.attd.list.subm2'/></th><!-- 제출용 --><!-- (성적포함) -->
        </tr>
      </thead>
      <tbody>
        <c:choose>
          <c:when test="${!empty lctDscList}">
            <c:forEach var="lctDscList" items="${lctDscList}" varStatus="loop">
              <tr>
                <td class="left_line ta_c numbering">
                  ${loop.count}
                  <input type="hidden" name="lctDscList[${loop.index}].subjId"  value="${lctDscList.subjId}">
                  <input type="hidden" name="lctDscList[${loop.index}].dvclsNb" value="${lctDscList.dvclsNb}">
                  <input type="hidden" name="lctDscList[${loop.index}].opOrgid" value="${lctDscList.opOrgid}">
                  <input type="hidden" name="lctDscList[${loop.index}].attndDt" value="${lctDscList.attndDt}">
                  <input type="hidden" name="lctDscList[${loop.index}].frLesnTmId" value="${lctDscList.frLesnTmId}">
                  <input type="hidden" name="lctDscList[${loop.index}].sbmUsPrtYn" value="${lctDscList.sbmUsPrtYn}">
                </td>
                <td class="ta_l">
                ${lctDscList.opOrgNm }
                </td>
                <td class="ta_c">
                  ${lctDscList.subjId }
                </td>
                <td class="ta_l">
                  <c:choose>
                  	<c:when test="${lctDscList.attandYn =='불가'}">
                  		${lctDscList.subjNm }
                  	</c:when>
                  	<c:otherwise>
                  		<a href="univ/lssn/bztm/findLecrLecReplList.do" style="cursor:pointer;" data-params='{ "opOrgid"  : "${lctDscList.opOrgid}"
                                                                   , "yy"       : "${lctDscList.yy}"
                                                                   , "semCd"    : "${lctDscList.semCd}"
                                                                   , "subjId"   : "${lctDscList.subjId}"
                                                                   , "dvclsNb"  : "${lctDscList.dvclsNb}"}'>

		                ${lctDscList.subjNm }
		                </a>
                  	</c:otherwise>
                  </c:choose>
                  <input type="hidden" name="lctDscList[${loop.index}].status"     value="4">
                  <input type="hidden" name="lctDscList[${loop.index}].cybLtrTyCd"     value="${lctDscList.cybLtrTyCd }">
                </td>
                <td class="ta_c">
                  ${lctDscList.dvclsNb }
                </td>
                <td class="ta_c">
                  ${lctDscList.crd }
                </td>
                <td class="ta_l">
                  ${lctDscList.buldAndRoomCont }
                </td>
                <td class="ta_l">
                  ${lctDscList.cybCoronaTyNm }
                </td>
                <td class="ta_l">
                  ${lctDscList.cybCoronaTyNm2 }
                </td>
                <td class="ta_l">
                  ${lctDscList.cybCoronaTyNm3 }
                </td>
                <td class="ta_l">
                  ${lctDscList.cybCoronaTyNm4 }
                </td>
                <td class="ta_r">
                  ${lctDscList.tkcrsCnt }
                </td>
                <td class="ta_c"><input type="hidden" value="${lctDscList.attandYn }" name="lctDscList[${loop.index}].attandYn">
                  ${lctDscList.attandYn }
                </td>
                <td class="ta_c">
					<a href="#" class="btn btn_inner print">
						<span class="btn_txt"><s:message code='lssn.univ.print'/></span><!-- 출력 -->
					</a>
				</td>
				<td class="ta_c">
					<a href="#" class="btn btn_inner print_cover">
						<span class="btn_txt"><s:message code='lssn.univ.print'/></span><!-- 출력 -->
					</a>
				</td>
                <td class="ta_c">
                    <a href="#" class="btn btn_inner print_empty">
                        <span class="btn_txt"><s:message code='lssn.univ.print'/></span><!-- 출력 -->
                    </a>
                </td>
                <td class="ta_c">
                    <a href="#" class="btn btn_inner print_report">
                        <span class="btn_txt"><s:message code='lssn.univ.print'/></span><!-- 출력 -->
                    </a>
                </td>
              </tr>
            </c:forEach>
          </c:when>
          <c:otherwise>
             <tr class="empty">
               <td class="ta_c" colspan="13"><s:message code='lssn.univ.no.result'/></td><!-- 조회된 데이터가 없습니다. -->
             </tr>
           </c:otherwise>
        </c:choose>
      </tbody>
    </table>
  </div>
</div>
<div class="section_note">
  <h3 class="tit_check"><s:message code='bztm.univ.note'/></h3><!-- 주의사항 -->
  <ul class="lst_hyphen">
    <li><span class="lst_head">-</span> <font color="red">유고결석은 학생이 별도의 서류제출 없이 [유고결석승인]으로 출석 인정 (수업결손에 대한 과제, 논문, 시험 등을 제시·관리하여 성적 부여)</font></li>
  </ul>
</div>
<!-- 목록리스트 -->
  <div class="section_tbl" id="lecrLecReplDiv">
    <div class="section_mid_tit">
      <h4 class="tit_mid f_l"><s:message code='bztm.univ.date.cl.list'/></h4><!-- 일자별목록 -->
      <p class="txt_number f_r"><s:message code='lssn.univ.search.rsl1'/> [ <em class="fc_orange"><span id="lecrLecReplTotCnt">0</span></em> ] <s:message code='lssn.univ.search.rsl2'/></p><!-- 검색결과는 --><!-- 건 입니다. -->
    </div>
    <div class="tbl_column">
      <table id="lecrLecReplTbl" class="tbl_mixed">
        <caption>목록</caption>
        <colgroup>
          <col style="width: 2%;"  />
          <col style="width: 10%;"  />
          <col style="width: 10%;"  />
          <col style="width: 10%;"  />
          <col  />
          <col style="width: 10%;"  />
          <col style="width: 10%;"  />
          <col style="width: 10%;"  />
        </colgroup>
        <thead>
          <tr>
            <th scope="col" data-sorter="false">#</th>
            <th scope="col" data-sorter="false"><s:message code='lssn.univ.date'/></th><!-- 일자 -->
            <th scope="col" data-sorter="false"><s:message code='lssn.univ.start.time'/></th><!-- 시작시간 -->
            <th scope="col" data-sorter="false"><s:message code='lssn.univ.end.time'/></th><!-- 종료시간 -->
            <th scope="col" data-sorter="false"><s:message code='lssn.univ.lecrm'/></th><!-- 강의실 -->
            <th scope="col" data-sorter="false"><s:message code='bztm.univ.lec.yn'/></th><!-- 출강여부 -->
            <th scope="col" data-sorter="false"><s:message code='bztm.univ.mklsd.replp'/></th><!-- 보강일/대강자 -->
            <th scope="col" data-sorter="false"><s:message code='bztm.univ.mkls.yn'/></th><!-- 보강여부 -->
          </tr>
        </thead>
        <tbody>
          <tr class="empty">
            <td class="ta_c" colspan="8"><s:message code='lssn.univ.no.result'/></td><!-- 조회된 데이터가 없습니다. -->
          </tr>
        </tbody>
      </table>
    </div>
  </div>
<textarea id="lecrLecReplTpl" style="display: none">
  <tr>
    <td class="left_line ta_c numbering">
      {0}
      <input type="hidden" name="lecrLecCfmDscList[{1}].no" value="{0}">
      <input type="hidden" name="lecrLecCfmDscList[{1}].status" value="4">
      <input type="hidden" name="lecrLecCfmDscList[{1}].yy">
      <input type="hidden" name="lecrLecCfmDscList[{1}].semCd">
      <input type="hidden" name="lecrLecCfmDscList[{1}].subjId">
      <input type="hidden" name="lecrLecCfmDscList[{1}].dvclsNb">
      <input type="hidden" name="lecrLecCfmDscList[{1}].frLesnTmId">
      <input type="hidden" name="lecrLecCfmDscList[{1}].toLesnTmId">
      <input type="hidden" name="lecrLecCfmDscList[{1}].roomAll">
      <input type="hidden" name="lecrLecCfmDscList[{1}].opOrgid">
      <input type="hidden" name="lecrLecCfmDscList[{1}].pfltId">
      <input type="hidden" name="lecrLecCfmDscList[{1}].attndDt">
      <input type="hidden" name="lecrLecCfmDscList[{1}].atDate">
      <input type="hidden" name="lecrLecCfmDscList[{1}].atFrTm">
      <input type="hidden" name="lecrLecCfmDscList[{1}].atToTm">
    </td>
    <td class="ta_c">
      <a href="#" data-params='{  "yy"       : "{3}"
                                , "semCd"    : "{4}"
                                , "subjId"   : "{5}"
                                , "dvclsNb"  : "{6}"
                                , "attndDt"  : "{7}"
                                , "frLesnTmId"  : "{8}"}'>
        {2}
      </a>
    </td>
    <td>
    </td>
    <td>
    </td>
    <td class="ta_l">
    </td>
    <td>{11}
      	<!-- <input type="checkbox" name="lecrLecCfmDscList[{1}].lecYn" disabled>-->
      	<input type="hidden"  name="lecrLecCfmDscList[{1}].lectureYn">
    </td>
    <td>{9}
    </td>
    <td>{10}
    </td>
  </tr>
</textarea>
<form name="lecrLecCfmDscForm" id="lecrLecCfmDscForm" method="post" action="univ/lssn/bztm/saveStdtAttndCfmDsc.do">
<!-- 일자상세정보 -->
  <div class="section_tbl" id="dateDtlDiv" style="display: none">
    <div class="section_mid_tit">
      <h4 class="tit_mid f_l"><s:message code='bztm.univ.date.dsc'/></h4><!-- 일자상세정보 -->
    </div>
    <div class="tbl_column">
      <table id="dateDtlTbl" class="tbl_active">
        <caption>일자상세정보</caption>
        <colgroup>
          <col style="width: 20%;" />
          <col style="width: 10%;" />
          <col style="width: 10%;" />
          <col  />
        </colgroup>
        <thead>
          <tr>
            <th scope="col" ><s:message code='bztm.univ.lesn.date'/></th><!-- 수업일자 -->
            <th scope="col" ><s:message code='lssn.univ.start.time'/></th><!-- 시작시간 -->
            <th scope="col" ><s:message code='lssn.univ.end.time'/></th><!-- 종료시간 -->
            <th scope="col" ><s:message code='lssn.univ.lecrm'/></th><!-- 강의실 -->
          </tr>
        </thead>
        <tbody>
          <tr>
          	<td class="ta_c"></td>
          	<td class="ta_c"></td>
          	<td class="ta_c"></td>
          	<td class="ta_c"></td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>

  <!-- 출석자목록 -->
  <div class="section_tbl" id="stdtAttndDiv" style="display: none">
    <div class="f_r">
	  <ui:button name="saveBtn" id="saveBtn" text="저장" className="btn btn_success" role="C" /><!-- 저장 -->
	</div>
    <div class="section_mid_tit">
      <h4 class="tit_mid f_l"><s:message code='bztm.univ.attd.stu'/></h4><!-- 출석자목록 -->
      <p class="txt_number f_r"><s:message code='lssn.univ.search.rsl1'/> [ <em class="fc_orange"><span id="stdtAttndCfmDscTotCnt">0</span></em> ] <s:message code='lssn.univ.search.rsl2'/></p><!-- 검색결과는 --><!-- 건 입니다. -->
    </div>
    <div class="tbl_column">
      <table id="stdtAttndCfmDscTbl" class="tbl_active">
        <caption>출석자리스트</caption>
        <colgroup>
          <col style="width: 5%;" />
          <col style="width: 20%;" />
          <col style="width: 8%;" />
          <col style="width: 5%;" />
          <col style="width: 8%;" />
          <col style="width: 8%;" />
          <col style="width: 8%;" />
          <col style="width: 10%;" />
          <col style="width: 8%;" />
          <col style="width: 8%;" />
          <col />
        </colgroup>
        <thead>
          <tr>
            <th scope="col" ><s:message code='lssn.univ.no'/></th><!-- 순번 -->
            <th scope="col" ><s:message code='bztm.univ.dpmt'/></th><!-- 학과(부)/전공 -->
            <th scope="col" ><s:message code='lssn.univ.stuid'/></th><!-- 학번 -->
            <th scope="col" ><s:message code='bztm.univ.sex'/></th><!-- 성별 -->
            <th scope="col" ><s:message code='lssn.univ.name'/></th><!-- 성명 -->
            <th scope="col" colspan=3><s:message code='bztm.univ.attd.yn'/></th><!-- 출석여부 -->
            <th scope="col" ><s:message code='bztm.univ.recogYn'/></th><!--출석인정확인 -->
            <th scope="col" >출석변경사유</th><!-- 비고 -->
            <th scope="col" ><s:message code='lssn.univ.rmk'/></th><!-- 비고 -->
          </tr>
        </thead>
        <tbody>
          <tr class="empty">
            <td class="ta_c" colspan="11"><s:message code='lssn.univ.no.result'/></td><!-- 조회된 데이터가 없습니다. -->
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</form>
<textarea id="stdtAttndCfmDscTpl" style="display: none">
  <tr>
    <td class="left_line ta_c numbering">
      {0}
      <input type="hidden" name="stdtAttndCfmDscList[{1}].status">
      <input type="hidden" name="stdtAttndCfmDscList[{1}].yy">
      <input type="hidden" name="stdtAttndCfmDscList[{1}].semCd">
      <input type="hidden" name="stdtAttndCfmDscList[{1}].subjId">
      <input type="hidden" name="stdtAttndCfmDscList[{1}].dvclsNb">
      <input type="hidden" name="stdtAttndCfmDscList[{1}].attndDt">
      <input type="hidden" name="stdtAttndCfmDscList[{1}].frLesnTmId">
      <input type="hidden" name="stdtAttndCfmDscList[{1}].realAttndDt">
      <input type="hidden" name="stdtAttndCfmDscList[{1}].realFrLesnTmId">
      <input type="hidden" name="stdtAttndCfmDscList[{1}].opOrgid">
      <input type="hidden" name="stdtAttndCfmDscList[{1}].recogSeq">
      <input type="hidden" name="stdtAttndCfmDscList[{1}].coronaChk">
      <input type="hidden" name="stdtAttndCfmDscList[{1}].beforeAttndTyCd">
      <input type="hidden" name="stdtAttndCfmDscList[{1}].beforeAttndChgRsn">
    </td>
    <td>
      <input type="text"     name="stdtAttndCfmDscList[{1}].pstnOrgzNm" style="width:100%" class="ta_l" readonly>
    </td>
    <td>
      <input type="text"     name="stdtAttndCfmDscList[{1}].stuid"      style="width:100%" class="ta_c" readonly>
    </td>
    <td>
      <input type="text"     name="stdtAttndCfmDscList[{1}].sexNm"      style="width:100%" class="ta_c" readonly>
    </td>
    <td>
      <input type="text"     name="stdtAttndCfmDscList[{1}].nm"         style="width:100%" class="ta_c" readonly>
    </td>
    <td>
      <input type="radio"   name="stdtAttndCfmDscList[{1}].attndTyCd" value="0001"/><label><s:message code='lssn.univ.attd'/></label><!-- 출석 -->
     </td>
    <td>
      <input type="radio"   name="stdtAttndCfmDscList[{1}].attndTyCd" value="0002"/><label><font color="red"><s:message code='lssn.univ.abse'/></font></label><!-- 결석 -->
       </td>
    <td>
      <input type="radio"   name="stdtAttndCfmDscList[{1}].attndTyCd" value="0003"/><label><font color="blue"><s:message code='lssn.univ.latns'/></font></label><!-- 지각 -->
     </td>
    <td><input type="checkbox"     name="stdtAttndCfmDscList[{1}].attndRecogCfmYn" style="width:100%" class="ta_c"  value="1" ></td>
    <td>
      <input type="text"     name="stdtAttndCfmDscList[{1}].attndChgRsn" style="width:90%" class="ta_l" readonly>
    </td>
    <td class="ta_l"><label><font color="blue" style="font-weight:bold">{2}{3}</font></label>
      <!-- <input type="text"     name="stdtAttndCfmDscList[{1}].rmk"        style="width:100%" class="ta_l" readonly> -->
    </td>
  </tr>
</textarea>