package com.dark.mapper;

import com.dark.pojo.OrderSetting;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderSettingMapper {
    @Select("select * from t_ordersetting where orderDate = #{value}")
    OrderSetting findByDate(Date orderDate);

    @Insert("insert into t_ordersetting(orderDate,number) values(#{orderDate},#{number})")
    void add(OrderSetting orderSetting);

    @Update("update t_ordersetting set number = #{number} where orderDate = #{orderDate}")
    void update(OrderSetting orderSetting);

    @Select("select * from t_ordersetting where orderDate between #{begin} and #{end}")
    List<OrderSetting> findByMonth(Map<String, String> map);
}
