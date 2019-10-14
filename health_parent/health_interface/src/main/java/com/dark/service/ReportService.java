package com.dark.service;

import java.util.List;
import java.util.Map;

public interface ReportService {
    Map<String, List<Object>> findMemberReport() throws Exception;

    Map<String, List> findSetmealReport();

    Map<String, Object> findBusinessReport() throws Exception;
}
