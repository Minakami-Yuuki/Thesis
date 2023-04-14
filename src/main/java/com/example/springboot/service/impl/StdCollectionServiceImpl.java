package com.example.springboot.service.impl;

import com.example.springboot.entity.StdCollection;
import com.example.springboot.mapper.StdCollectionMapper;
import com.example.springboot.service.IStdCollectionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Misaki
 * @since 2023-04-14
 */
@Service
public class StdCollectionServiceImpl extends ServiceImpl<StdCollectionMapper, StdCollection> implements IStdCollectionService {

}
