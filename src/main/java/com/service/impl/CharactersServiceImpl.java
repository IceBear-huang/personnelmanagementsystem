package com.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pojo.Characters;
import com.service.CharactersService;
import com.mapper.CharactersMapper;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class CharactersServiceImpl extends ServiceImpl<CharactersMapper, Characters>
implements CharactersService{

}




