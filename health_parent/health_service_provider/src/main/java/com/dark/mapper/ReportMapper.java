package com.dark.mapper;

import org.apache.ibatis.annotations.Select;

import java.util.*;

public interface ReportMapper {
    @Select("select count(id) from t_member where regTime <= #{value}")
    Integer findMemberCountByMonth(Date parse);

    LinkedHashMap<String, Integer> findMemberCountByOrders(List<Date> orders);

    List<Map<String, Object>> findSetmealOrder();

    Map<String, Object> findBusinessReport(Map<String, Date> orders);

    Map<String, Integer> findMemberCountBySex();

    Map<String, Integer> findMemberCountByAge(Map<String, Date> orders);
}
