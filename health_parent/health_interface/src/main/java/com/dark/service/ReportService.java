package com.dark.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ReportService {
    Map<String, List<Object>> findMemberReport() throws Exception;

    Map<String, List> findSetmealReport();

    Map<String, Object> findBusinessReport() throws Exception;

    List<Map<String, Object>> findMemberReportBySex();

    Map<String, List> findMemberReportByAge();

    Map<String, List> findMemberReportByDate(Map<String, Date> map) throws Exception;
}
