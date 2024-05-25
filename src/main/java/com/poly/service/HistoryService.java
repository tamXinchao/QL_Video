package com.poly.service;

import java.util.List;

import com.poly.entity.History;
import com.poly.entity.User;
import com.poly.entity.Video;

public interface HistoryService {
	List<History> findByUser(String username);
	List<History> findByUserAndIsLiked(String username);
	History findByUserAndVideoId(Integer userId, Integer videoId);
	History create(User user, Video video);
	boolean updateLikeOrUnLike(User user, String videoHref);
}
