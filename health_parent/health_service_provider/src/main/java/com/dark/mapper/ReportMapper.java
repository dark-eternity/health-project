package com.dark.mapper;

import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ReportMapper {
    @Select("select count(id) from t_member where regTime <= #{value}")
    Integer findMemberCountByMonth(Date parse);

    HashMap<String, Object> findMemberCountByOrders(List<Date> orders);

    List<Map<String, Object>> findSetmealOrder();

    Map<String, Object> findBusinessReport(Map<String, Date> orders);
}
