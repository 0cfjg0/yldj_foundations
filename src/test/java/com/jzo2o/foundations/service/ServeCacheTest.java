package com.jzo2o.foundations.service;

import com.jzo2o.foundations.model.domain.Serve;
import com.jzo2o.foundations.service.impl.ServeServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.yaml.snakeyaml.events.Event;

import javax.annotation.Resource;

@SpringBootTest
public class ServeCacheTest {

    @Resource
    ServeServiceImpl serveService;

    @Test
    public void selectTest(){
        Long id = 1687401441778552834L;
        Serve res = serveService.getServeById(id);
        System.out.println(res);
    }

}
