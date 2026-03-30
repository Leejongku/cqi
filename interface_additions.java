/* ================================================================
   TcqiMapper.java 인터페이스에 추가할 메서드 시그니처
   (기존 메서드는 그대로 유지, 아래 내용만 추가)
================================================================ */

// ── 교양 CQI 조회 ──────────────────────────────────────────────

/** 교양 MCQI_MASTER 상태 조회 */
CamelCaseMap findLcqiMaster(Map<String, Object> param);

/** 교양 MCQI_SECT_DSC 현재연도 전체 조회 */
List<CamelCaseMap> findLcqiSectList(Map<String, Object> param);

/** 교양 MCQI_SECT_DSC 전년도 TYPE='03' 조회 (이월용) */
List<CamelCaseMap> findLcqiPrevSectList(Map<String, Object> param);

// ── 교양 CQI 저장 ──────────────────────────────────────────────

/** 교양 MCQI_MASTER upsert (저장 시 자동 생성) */
void mergeLcqiMaster(Map<String, Object> param);

/** 교양 MCQI_SECT_DSC 삭제 (SECT_CD + TYPE_CD 단위) */
void deleteLcqiSect(Map<String, Object> param);

/** 교양 MCQI_SECT_DSC 단건 insert */
void insertLcqiSect(Map<String, Object> param);


/* ================================================================
   McqiService.java 인터페이스에 추가할 메서드 시그니처
   (기존 메서드는 그대로 유지, 아래 내용만 추가)
================================================================ */

/**
 * 교양 CQI 마스터 상태 조회
 * @param tcqiSearch yy, collCd(=orgid), detMngtDscCd(=pfltId)
 * @return CamelCaseMap or null
 */
CamelCaseMap findLcqiMaster(TcqiSearch tcqiSearch);

/**
 * 교양 CQI 체크박스 데이터 조회 (현재연도 + 전년도 이월 합산)
 * @param tcqiSearch yy, collCd(=orgid), detMngtDscCd(=pfltId)
 * @return List<CamelCaseMap> (cqiSectCd, cqiTypeCd, choiCd 포함)
 */
List<CamelCaseMap> findLcqiSectList(TcqiSearch tcqiSearch);

/**
 * 교양 CQI 체크박스 저장 (MASTER upsert + SECT delete-insert 트랜잭션)
 * @param sectDscList 저장할 SECT_DSC 목록
 */
void saveLcqiSectList(List<McqiSectDsc> sectDscList);
