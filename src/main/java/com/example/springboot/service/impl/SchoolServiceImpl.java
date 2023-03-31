package com.example.springboot.service.impl;

import com.example.springboot.entity.School;
import com.example.springboot.mapper.SchoolMapper;
import com.example.springboot.service.ISchoolService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Misaki
 * @since 2023-03-10
 */
@Service
public class SchoolServiceImpl extends ServiceImpl<SchoolMapper, School> implements ISchoolService {

}
