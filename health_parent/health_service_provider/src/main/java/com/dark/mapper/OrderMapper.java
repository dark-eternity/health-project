package com.dark.mapper;

import com.dark.pojo.Order;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

public interface OrderMapper {
    Integer setmealOrder(Map map);

    @Select("select * from t_order where member_id = #{memberId} and orderDate = #{orderDate} and setmeal_id = #{setmealId}")
    Order selectIsOrder(Order order);

    Integer add(Order order);

    @Select("select id from t_order where member_id = #{memberId} and orderDate = #{orderDate} and setmeal_id = #{setmealId}")
    Integer findIdByOrder(Order order);

    @Select("select id from t_order where member_id = #{member_id} and orderDate = #{orderDate} and setmeal_id = #{setmealId}")
    Integer findIdByMap(Map map);

    Map findMsg(Integer id);
}
