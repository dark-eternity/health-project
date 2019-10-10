package com.dark.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.dark.mapper.MemberMapper;
import com.dark.mapper.OrderMapper;
import com.dark.mapper.OrderSettingMapper;
import com.dark.pojo.Member;
import com.dark.pojo.Order;
import com.dark.pojo.OrderSetting;
import com.dark.service.OrderService;
import com.dark.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;

@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderSettingMapper orderSettingMapper;
    @Autowired
    private MemberMapper memberMapper;

    @Override
    public Integer setmealOrder(Map map) throws Exception {
        /* 根据用户选择的预约日期来判断在预约设置(t_ordersetting)表中是否进行过设置
     》不存在 终止
     》存在，判断可预约人数是否已满 (t_ordersetting number reservations)
     	》已满 终止
     	》未满，判断当前用户的手机号是否已经是会员(t_member)
     		》是
     			判断该会员在当前选中预约日期中是否预约过该套餐
     					（t_order member_id orderDate setmealId）
     				》有 终止
     				》没有
     					完成预约模块（t_order  insert)
     					更新预约人数(t_ordersetting)

     		》不是
     			自动注册（t_member  insert)
     			完成预约模块（t_order  insert)
     			更新预约人数(t_ordersetting) */

        //根据用户选择的预约日期来判断在预约设置(t_ordersetting)表中是否进行过设置
        String orderDate = (String) map.get("orderDate");
        OrderSetting byDate = orderSettingMapper.findByDate(DateUtils.parseString2Date(orderDate));
        if (byDate == null) {
            //不存在 终止
            return 0;
        }
        //存在，判断可预约人数是否已满 (t_ordersetting number reservations)
        int number = byDate.getNumber();
        int reservations = byDate.getReservations();
        if (number <= reservations) {
            //已满 终止
            return 0;
        }
        //未满，判断当前用户的手机号是否已经是会员(t_member)
        String telephone = (String) map.get("telephone");
        Member member = memberMapper.findByTelephone(telephone);
        if (member == null) {
            //不是
            //自动注册（t_member  insert)
            map.put("regTime", new Date());
            Integer member_id = memberMapper.add(map);
            //完成预约模块（t_order  insert)
            map.put("member_id", member_id);
            map.put("orderStatus", Order.ORDERSTATUS_NO);
            orderMapper.setmealOrder(map);
            //更新预约人数(t_ordersetting)
            byDate.setReservations(byDate.getReservations() + 1);
            orderSettingMapper.updateReservations(byDate);
            //返回orderId
            Integer orderId = orderMapper.findIdByMap(map);
            return orderId;
        } else {
            //是
            //判断该会员在当前选中预约日期中是否预约过该套餐
            Order order = new Order();
            order.setMemberId(member.getId());
            order.setOrderDate(DateUtils.parseString2Date(orderDate));
            String setmealId = (String) map.get("setmealId");
            order.setSetmealId(Integer.parseInt(setmealId));
            Order _order = orderMapper.selectIsOrder(order);
            if (_order != null) {
                //有 终止
                return 0;
            } else {
                //没有
                //完成预约模块（t_order  insert)
                order.setOrderType((String) map.get("orderType"));
                order.setOrderStatus(Order.ORDERSTATUS_NO);
                orderMapper.add(order);
                //更新预约人数(t_ordersetting)
                byDate.setReservations(byDate.getReservations() + 1);
                orderSettingMapper.updateReservations(byDate);
                //返回orderId
                Integer orderId = orderMapper.findIdByOrder(order);
                return orderId;
            }
        }
    }

    @Override
    public Map findMsgById(Integer id) {
        //调用mapper
        Map map = orderMapper.findMsg(id);
        //日期格式化
        java.sql.Date orderDate = (java.sql.Date) map.get("orderDate");
        try {
            Date date = DateUtils.parseString2Date(orderDate.toString());
            map.replace("orderDate", date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}
