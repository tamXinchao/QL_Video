package com.poly.service;

import java.util.List;

import com.poly.dao.StatsDao;
import com.poly.dao.impl.StatsDaoImpl;
import com.poly.dto.VideoLikedInfo;

public class StatsServiceImpl implements StatsService {
	private StatsDao statsDao;
	
	
	public StatsServiceImpl() {
		statsDao = new StatsDaoImpl();
	}
	@Override
	public List<VideoLikedInfo> findVideoLikedInfo() {
		
		return statsDao.findVideoLikedInfo();
	}
	
}
