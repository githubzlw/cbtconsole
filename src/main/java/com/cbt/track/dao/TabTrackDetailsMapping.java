package com.cbt.track.dao;

import com.cbt.bean.TabTrackDetails;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TabTrackDetailsMapping {

    List<TabTrackDetails> queryByTrackNo(@Param("trackNo") String trackNo);
    
}
