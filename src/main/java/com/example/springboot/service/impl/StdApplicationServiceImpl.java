package com.example.springboot.service.impl;

import com.example.springboot.entity.StdApplication;
import com.example.springboot.mapper.StdApplicationMapper;
import com.example.springboot.service.IStdApplicationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Misaki
 * @since 2023-02-28
 */
@Service
public class StdApplicationServiceImpl extends ServiceImpl<StdApplicationMapper, StdApplication> implements IStdApplicationService {

}
