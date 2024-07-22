package com.jzo2o.foundations.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.PageUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jzo2o.common.expcetions.ForbiddenOperationException;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.foundations.constants.FoundationConstants;
import com.jzo2o.foundations.enums.FoundationStatusEnum;
import com.jzo2o.foundations.model.domain.Region;
import com.jzo2o.foundations.model.domain.Serve;
import com.jzo2o.foundations.mapper.ServeMapper;
import com.jzo2o.foundations.model.domain.ServeItem;
import com.jzo2o.foundations.model.dto.request.ServePageQueryReqDTO;
import com.jzo2o.foundations.model.dto.request.ServeUpsertReqDTO;
import com.jzo2o.foundations.model.dto.response.ServeResDTO;
import com.jzo2o.foundations.service.IRegionService;
import com.jzo2o.foundations.service.IServeItemService;
import com.jzo2o.foundations.service.IServeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jzo2o.mysql.utils.PageHelperUtils;
import com.jzo2o.mysql.utils.PageUtils;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 服务表 服务实现类
 * </p>
 *
 * @author author
 * @since 2024-07-22
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ServeServiceImpl extends ServiceImpl<ServeMapper, Serve> implements IServeService {

    @Resource
    IServeItemService serveItemService;

    @Resource
    IRegionService regionService;

    @Override
    public PageResult<ServeResDTO> getPages(ServePageQueryReqDTO dto) {
        if(ObjectUtil.isEmpty(dto)){
            throw new ForbiddenOperationException("参数错误");
        }
        PageResult<ServeResDTO> res = PageHelperUtils.selectPage(dto, () -> {
            return baseMapper.queryByRegionId(dto.getRegionId());
        });

        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchAdd(List<ServeUpsertReqDTO> dto) {
        if (ObjectUtil.isEmpty(dto)){
            throw new ForbiddenOperationException("参数异常");
        }
        //服务项必须启用
        for (ServeUpsertReqDTO item : dto) {
            Long serveItemId = item.getServeItemId();
            LambdaQueryWrapper<ServeItem> wrapper = Wrappers.<ServeItem>lambdaQuery().eq(ServeItem::getId, serveItemId);
            ServeItem resItem = serveItemService.getOne(wrapper);
            if(resItem.getActiveStatus() != FoundationStatusEnum.ENABLE.getStatus()){
                throw new ForbiddenOperationException("服务未启用");
            }

            //服务项不能重复添加
            Integer count = lambdaQuery().eq(Serve::getRegionId, item.getRegionId())
                    .eq(Serve::getServeItemId, item.getServeItemId())
                    .count();
            if (count > 0){
                throw new ForbiddenOperationException("服务已存在");
            }

            Long regionId = item.getRegionId();
            Region regionRes = regionService.getById(regionId);

            //region判空省略

            boolean flag = this.save(Serve.builder()
                    .regionId(item.getRegionId())
                    .serveItemId(item.getServeItemId())
                    .cityCode(regionRes.getCityCode())
                    .price(item.getPrice())
                    .build()
            );

            if(!flag){
                throw new ForbiddenOperationException("插入失败");
            }
        }

    }

    @Override
    public void updatePrice(Long id, BigDecimal price) {
        //参数判空
        if(!ObjectUtil.isAllNotEmpty(id,price)){
            throw new ForbiddenOperationException("参数异常");
        }

        //更新
        LambdaUpdateWrapper<Serve> wrapper = Wrappers.<Serve>lambdaUpdate()
                .eq(Serve::getId, id)
                .set(Serve::getPrice, price);
        boolean flag = this.update(wrapper);
        if(!flag){
            throw new ForbiddenOperationException("更新失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(Long id,Short type) {
        //参数判空
        if(ObjectUtil.isEmpty(id)){
            throw new ForbiddenOperationException("参数异常");
        }

        //校验
        LambdaUpdateWrapper<Serve> wrapper = Wrappers.<Serve>lambdaUpdate()
                .eq(Serve::getId, id);
        Serve serveRes = this.getOne(wrapper);
        Integer saleStatus = serveRes.getSaleStatus();
        if (type == FoundationConstants.UP) {
            if(saleStatus == FoundationStatusEnum.ENABLE.getStatus()){
                throw new ForbiddenOperationException("已经为上架状态");
            }
        } else if((type == FoundationConstants.DOWN)) {
            if(saleStatus != FoundationStatusEnum.ENABLE.getStatus()){
                throw new ForbiddenOperationException("已经为下架状态");
            }
        }
        //服务项id
        Long serveItemId = serveRes.getServeItemId();
        ServeItem serveItemRes = serveItemService.getById(serveItemId);
        if(ObjectUtil.isEmpty(serveItemRes)){
            throw new ForbiddenOperationException("服务项不存在");
        }
        //服务项需要是启动状态才能上/下架
        Integer activeStatus = serveItemRes.getActiveStatus();
        //服务项为启用状态方可上/下架
        if (!(FoundationStatusEnum.ENABLE.getStatus()==activeStatus)) {
            throw new ForbiddenOperationException("服务项为启用状态方可上架");
        }

        //更新
        serveRes.setSaleStatus(type == FoundationConstants.UP ? FoundationStatusEnum.ENABLE.getStatus() : FoundationStatusEnum.DISABLE.getStatus());
        boolean flag = this.update(serveRes, Wrappers.<Serve>lambdaUpdate().eq(Serve::getId,id));
        if(!flag){
            throw new ForbiddenOperationException("更新失败");
        }
    }

    @Override
    public void deleteServe(Long id) {
        //参数判空
        if(ObjectUtil.isEmpty(id)){
            throw new ForbiddenOperationException("参数异常");
        }
        Serve serveRes = this.getById(id);
        //校验
        if(serveRes.getSaleStatus() == FoundationStatusEnum.ENABLE.getStatus()){
            throw new ForbiddenOperationException("上架状态下无法删除");
        }
        Long serveItemId = serveRes.getServeItemId();
        ServeItem serveItemRes = serveItemService.getById(serveItemId);
        if(ObjectUtil.isEmpty(serveItemRes)){
            throw new ForbiddenOperationException("服务项不存在");
        }
        boolean flag = this.removeById(id);
        if(!flag){
            throw new ForbiddenOperationException("删除失败");
        }
    }

    @Override
    public void setHot(Long id,Integer isHot) {
        //参数判空
        if(ObjectUtil.isEmpty(id)){
            throw new ForbiddenOperationException("参数异常");
        }
        //校验
        Serve serveRes = this.getById(id);
        if(ObjectUtil.isEmpty(serveRes)){
            throw new ForbiddenOperationException("服务项不存在");
        }
        //状态已经为热门/非热门
        if (serveRes.getIsHot().equals(isHot)) {
            throw new ForbiddenOperationException("状态无需更改");
        }
        serveRes.setIsHot(isHot);
        boolean flag = this.update(serveRes, Wrappers.<Serve>lambdaUpdate().eq(Serve::getId, id));
        if(!flag){
            throw new ForbiddenOperationException("更新失败");
        }
    }

    
}
