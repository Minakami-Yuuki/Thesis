package com.example.springboot.service.impl;

import com.example.springboot.entity.Specialty;
import com.example.springboot.mapper.SpecialtyMapper;
import com.example.springboot.service.ISpecialtyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Misaki
 * @since 2023-02-15
 */
@Service
public class SpecialtyServiceImpl extends ServiceImpl<SpecialtyMapper, Specialty> implements ISpecialtyService {

}
