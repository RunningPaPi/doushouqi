package com.artqiyi.doushouqi.system.service;

import com.artqiyi.doushouqi.common.util.StringUtils;
import com.artqiyi.doushouqi.system.domain.SystemDistribution;
import com.artqiyi.doushouqi.system.domain.SystemDistributionExample;
import com.artqiyi.doushouqi.system.mapper.SystemDistributionMapper;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/** 
 * 类或接口作用描述 
 *
 * @author wushuang
 * @since 2018-06-19
 */
@Service("distributionNoticeService")
public class DistributionService {
	private static Logger logger = LoggerFactory.getLogger(DistributionService.class);
	
	@Autowired(required = true)
	private SystemDistributionMapper systemDistributionMapper;


	/**
	 * 找应用发布状态
	 * @param appType
	 * @return
	 */
	public SystemDistribution findSystemDistribution(Short appType, String verison) {

		SystemDistributionExample example = new SystemDistributionExample();

		SystemDistributionExample.Criteria criteria = example.createCriteria();

		criteria.andAppTypeEqualTo(appType);
		if (!StringUtils.isEmpty(verison)) {
			criteria.andVersionEqualTo(verison);
		}

		List<SystemDistribution> list = systemDistributionMapper.selectByExample(example);

		if(CollectionUtils.isEmpty(list)){
			return null;
		}
		
		return list.get(0);
	}


	/**
	 * 找应用发布状态 带appStoreId
	 * @param appType
	 * @return
	 */
	public List<SystemDistribution> findSystemDistribution(Short appType,String verison,Short appStoreId) {

		SystemDistributionExample example = new SystemDistributionExample();

		SystemDistributionExample.Criteria criteria = example.createCriteria();

		criteria.andAppTypeEqualTo(appType);
		if (!StringUtils.isEmpty(verison)) {
			criteria.andVersionEqualTo(verison);
		}
		if (appStoreId !=null && appStoreId> 0) {
			criteria.andAppStoreIdEqualTo(appStoreId);
		}

		List<SystemDistribution> list = systemDistributionMapper.selectByExample(example);

		return list;
	}

}
