package cn.csl.manage.shiro.filter;

import cn.csl.manage.dao.SysLinkRoleMapper;
import cn.csl.manage.dao.SysResourceRoleMapper;
import cn.csl.manage.dao.SysSubscriberRoleMapper;
import cn.csl.manage.dao.SysResourceRoleMapper;
import cn.csl.manage.entity.SysResourceRole;
import cn.csl.manage.entity.SysSubscriberRole;
import cn.csl.manage.model.SysSubRole;
import cn.csl.manage.model.SysUrlRole;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 创建日期:2017年12月17日
 * 创建时间:下午9:39:30
 * 创建用户:yellowcong
 * 机能概要:
 */
public class FilterChainDefinitionsMapBuilder {
	
	@Resource
	private SysResourceRoleMapper sysResourceRoleMapper;
	@Resource
	private SysLinkRoleMapper sysLinkRoleMapper;
	/**
	 * 获取权限
	 * @return
	 */
	public LinkedHashMap<String, String> loadFilterChainDefinitions(){
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		//后台
		List<SysUrlRole> sysUrlRoles = sysResourceRoleMapper.listAllUrlRole();
		Map<String,String> urlrolesMap = new HashMap<>();
		for (SysUrlRole sysUrlRole:sysUrlRoles) {
			if (urlrolesMap.containsKey(sysUrlRole.getResUrl())){
				urlrolesMap.replace(sysUrlRole.getResUrl(),urlrolesMap.get(sysUrlRole.getResUrl()) +","+sysUrlRole.getRoleName());
			}else{
				urlrolesMap.put(sysUrlRole.getResUrl(),sysUrlRole.getRoleName());
			}
		}
		for (String key : urlrolesMap.keySet()) {
			map.put(key, "admin,roleOrFilter[\""+urlrolesMap.get(key)+"\"]");
		}
		//前台台
		List<SysSubRole> sysSubRoles = sysLinkRoleMapper.listAllSubscriberRole();
		Map<String,String> subRoleMap = new HashMap<>();
		for (SysSubRole sysSubRole:sysSubRoles) {
			if (subRoleMap.containsKey(sysSubRole.getLinkUrl())){
				subRoleMap.replace(sysSubRole.getLinkUrl(),urlrolesMap.get(sysSubRole.getLinkUrl()) +","+sysSubRole.getRoleName());
			}else{
				subRoleMap.put(sysSubRole.getLinkUrl(),sysSubRole.getRoleName());
			}
		}
		for (String key : subRoleMap.keySet()) {
			map.put(key, "front,roleOrFilter[\""+subRoleMap.get(key)+"\"]");
		}



		map.put("/wenjuan/front/**/**", "anon");//////////////////////开发的的时候用的
		map.put("/wx/front/editInfo", "anon");//微信访问openid是否存在的不用验证///开发的的时候用的

		map.put("/manage/front/introduceVideo", "anon");//////////////////////系统介绍视频
//		map.put("/manage/front/index", "front,roleOrFilter[\"匿名\"]");
		map.put("/**/**/login**", "anon");
		map.put("/**/**/**/loadCheckAttributeIsExistence", "anon");//校验是否存在的不用验证
		map.put("/**/**/register", "anon");//注册是否存在的不用验证
		map.put("/wx/front/getOpenId", "anon");//微信访问openid是否存在的不用验证
		map.put("/basics/**", "anon");//基本的信息
		map.put("/static/**", "anon");
		map.put("/upload/**", "anon");
		map.put("/download/**", "anon");
		map.put("/**/**/logout", "logout");
		map.put("/**/admin/**", "admin");
		map.put("/**/front/**", "front");
		map.put("/**", "front");
		map.put("/**", "admin");
		return map;
	}
}
