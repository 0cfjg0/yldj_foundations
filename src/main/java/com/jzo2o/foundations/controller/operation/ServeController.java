package com.jzo2o.foundations.controller.operation;


import com.jzo2o.common.model.PageResult;
import com.jzo2o.common.model.Result;
import com.jzo2o.foundations.constants.FoundationConstants;
import com.jzo2o.foundations.model.dto.request.ServePageQueryReqDTO;
import com.jzo2o.foundations.model.dto.request.ServeUpsertReqDTO;
import com.jzo2o.foundations.model.dto.response.ServeResDTO;
import com.jzo2o.foundations.service.IServeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.Response;
import org.checkerframework.checker.units.qual.A;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 服务表 前端控制器
 * </p>
 *
 * @author author
 * @since 2024-07-22
 */
@RestController("operationServeController")
@RequestMapping("/operation/serve")
@Api(tags = "运营端 - 区域服务相关接口")
public class ServeController {

    @Resource
    IServeService serveService;

    @GetMapping("/page")
    @ApiOperation("服务项分页")
    public PageResult<ServeResDTO> getPages(ServePageQueryReqDTO dto){
        return serveService.getPages(dto);
    }


    @PostMapping("/batch")
    @ApiOperation("服务项新增")
    public void batchAdd(@RequestBody List<ServeUpsertReqDTO> dto){
        serveService.batchAdd(dto);
    }

    @PutMapping("/{id}")
    @ApiOperation("服务项价格修改")
    public void updatePrice(@PathVariable("id") Long id,
                            @RequestParam("price") BigDecimal price){
        serveService.updatePrice(id,price);
    }

    @PutMapping("/onSale/{id}")
    @ApiOperation("区域服务上架")
    public void updateOnSale(@PathVariable Long id){
        serveService.updateStatus(id, FoundationConstants.UP);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("服务删除")
    public void deleteServe(@PathVariable Long id){
        serveService.deleteServe(id);
    }

    @PutMapping("/offSale/{id}")
    @ApiOperation("区域服务下架")
    public void updateOffSale(@PathVariable Long id){
        serveService.updateStatus(id,FoundationConstants.DOWN);
    }

    @PutMapping("/onHot/{id}")
    @ApiOperation("设置服务热门")
    public void onHot(@PathVariable Long id){
        serveService.setHot(id,FoundationConstants.HOT);
    }

    @PutMapping("/offHot/{id}")
    @ApiOperation("取消服务热门")
    public void offHot(@PathVariable Long id){
        serveService.setHot(id,FoundationConstants.NOT_HOT);
    }
}
