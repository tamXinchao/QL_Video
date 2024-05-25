package com.poly.dao.impl;

import java.util.ArrayList;
import java.util.List;

import com.poly.dao.AbstractDao;
import com.poly.dao.StatsDao;
import com.poly.dto.VideoLikedInfo;

public class StatsDaoImpl extends AbstractDao<Object[]> implements StatsDao {

	@Override
	public List<VideoLikedInfo> findVideoLikedInfo() {
		String sql = "select \r\n"
				+ "	v.id , v.title, v.href, sum(cast(h.isLiked as int)) as totalLike\r\n"
				+ "	from \r\n"
				+ "	video v left  join history h on v.id = h.videoId\r\n"
				+ "	where v.isActive = 1\r\n"
				+ "	group by \r\n"
				+ "	v.id , v.title, v.href\r\n"
				+ "	order by \r\n"
				+ "	sum(cast(h.isLiked as int)) desc";
		
		List<Object[]> objects = super.findManyByNativeQuery(Object[].class, sql);
		List<VideoLikedInfo> result = new ArrayList<>();
		objects.forEach(object ->{
			VideoLikedInfo videoLikedInfo = setDataVideoLikedInfo(object);
			result.add(videoLikedInfo);
		});
		return result;
	}
	private VideoLikedInfo setDataVideoLikedInfo(Object[] object) {
		VideoLikedInfo videoLikedInfo = new VideoLikedInfo();
		videoLikedInfo.setVideoId((Integer) object[0]);
		videoLikedInfo.setTitle((String) object[1]);
		videoLikedInfo.setHref((String)object[2]);
		videoLikedInfo.setTotalLike(object[3] == null ? 0 : (Integer)object[3]);
		return videoLikedInfo;
	}
}
