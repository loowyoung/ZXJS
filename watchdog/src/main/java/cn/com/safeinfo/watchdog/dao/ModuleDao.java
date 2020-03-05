package cn.com.safeinfo.watchdog.dao;

import cn.com.safeinfo.watchdog.model.entity.ModuleModel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 服务模块
 *
 * @author: ly
 * @date: 2020/3/5 16:05
 */
@Mapper
public interface ModuleDao extends BaseMapper<ModuleModel> {
}
