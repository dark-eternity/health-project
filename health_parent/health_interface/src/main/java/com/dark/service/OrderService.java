package com.dark.service;

import java.util.Map;

public interface OrderService {
    Integer setmealOrder(Map map) throws Exception;

    Map findMsgById(Integer id);
}
