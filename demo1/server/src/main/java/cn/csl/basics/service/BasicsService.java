package cn.csl.basics.service;

import cn.csl.basics.dao.BasicsAdminMapper;
import cn.csl.basics.model.ResultResponse;
import cn.csl.basics.util.TableReturnUtil;
import cn.csl.basics.util.TableUploadUtil;
import cn.csl.basics.util.annotation.check.CheckAnnotationUtil;
import cn.csl.basics.util.annotation.check.CheckValueResult;
import cn.csl.basics.util.annotation.unique.LoadUniqueAnnotationAttribute;
import cn.csl.basics.util.annotation.unique.UniqueResult;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

public abstract class BasicsService<E> {

    public abstract BasicsAdminMapper<E> getBasicsAdminMapper();
    /**
     * 插入 默认设置setState 为1
     * @param pojo
     * @return
     */
    public ResultResponse addSave(E pojo){
        ResultResponse response = checkInfo(pojo);
        if (response.isError()) {
            return response;
        }
        try{
            Method entityMethod = pojo.getClass().getMethod("setState",Integer.class);
            entityMethod.setAccessible(true);
            entityMethod.invoke(pojo,1);

            entityMethod = pojo.getClass().getMethod("setCreateTime", Date.class);
            entityMethod.setAccessible(true);
            entityMethod.invoke(pojo,new Date());
            getBasicsAdminMapper().insert(pojo);
            response.setSuccessResult("添加成功");
        }catch (NoSuchMethodException e){//没有创建时间的方法也可以
            getBasicsAdminMapper().insert(pojo);
            response.setSuccessResult("添加成功");
        }catch (Exception e){
            e.printStackTrace();
            response.setFailureResult("添加失败");
        }
        return response;
    }

    public abstract ResultResponse editSave(E pojo);

    /**
     * 逻辑删除
     * @param id
     * @return
     */
    public ResultResponse delete(Long id){
        ResultResponse response = new ResultResponse();
        try {
            E obj = getBasicsAdminMapper().selectByPrimaryKey(id);
            Method entityMethod = obj.getClass().getMethod("setState",Integer.class);
            entityMethod.setAccessible(true);
            entityMethod.invoke(obj,0);
            this.getBasicsAdminMapper().updateByPrimaryKey(obj);
            response.setSuccessResult("删除成功");
        }catch (Exception e){
            e.printStackTrace();
            response.setSuccessResult("删除失败");
        }

        return response;
    }
    public ResultResponse delete(String id){
        ResultResponse response = new ResultResponse();
        try {
            response = delete(Long.valueOf(id));
        }catch (Exception e){
            e.printStackTrace();
            response.setFailureResult("删除失败");
        }
        return response;
    }

    /**
     * 物理删除
     * @param id
     * @return
     */
    public ResultResponse deleteByPrimaryKey(Long id){
        ResultResponse response = new ResultResponse();
        try {
            this.getBasicsAdminMapper().deleteByPrimaryKey(id);
            response.setSuccessResult("删除成功");
        }catch (Exception e){
            e.printStackTrace();
            response.setFailureResult("删除失败");
        }

        return response;
    }
    public ResultResponse deleteByPrimaryKey(String id){
        ResultResponse response = new ResultResponse();
        try {
            this.getBasicsAdminMapper().deleteByPrimaryKey(Long.valueOf(id));
            response.setSuccessResult("删除成功");
        }catch (Exception e){
            e.printStackTrace();
            response.setFailureResult("删除失败");
        }

        return response;
    }

    /**
     * 根据id查找
     * @return
     */
    public E selectByPrimaryKey(Long id){
        E obj= getBasicsAdminMapper().selectByPrimaryKey(id);
        return obj;
    }
    public E selectByPrimaryKey(String id){
        E obj= getBasicsAdminMapper().selectByPrimaryKey(Long.valueOf(id));
        return obj;
    }

    /**
     * 根据属性查找 只找 状态为启用的 state=1
     * @return
     */
    public List<E> listByAttribute(String attribute, String checkVal){
        return getBasicsAdminMapper().listByAttribute(attribute,checkVal,1);
    }

    public List<E> listByAttrValueMap(Map<String,String> attrValue, int state){
        StringBuffer attrValueMap = new StringBuffer();
        for(String key:attrValue.keySet()){//keySet获取map集合key的集合  然后在遍历key即可
            String value = attrValue.get(key).toString();//
            attrValueMap.append(" and " + key +"="+ value);
        }
        return getBasicsAdminMapper().listByAttrValueMap(attrValueMap.toString(),state);
    }
    public List<E> listByAttrValueMap(Map<String,String> attrValue){
        return this.listByAttrValueMap(attrValue,1);
    }



    /**
     * 唯一值
     * @param attribute
     * @param checkVal
     * @return
     */
    public E loadByAttribute(String attribute, String checkVal){
        return getBasicsAdminMapper().listByAttribute(attribute,checkVal,1).get(0);
    }
    /**
     * 根据属性查找 只找 状态为启用的 state=1
     * @return
     */
    public ResultResponse listByAttributeResponse(String attribute, String checkVal){
        ResultResponse response = new ResultResponse();
        response.setSuccessResult(getBasicsAdminMapper().listByAttribute(attribute,checkVal,1));
        return response;
    }

    public TableReturnUtil listJson(TableUploadUtil tableUploadUtil){
        TableReturnUtil tableReturnUtil = new TableReturnUtil();
        long totalCount = this.getBasicsAdminMapper().getCount(tableUploadUtil).longValue();
        tableReturnUtil.setRecordsTotal(totalCount);
        tableReturnUtil.setRecordsFiltered(totalCount);
        List<E> objs = this.getBasicsAdminMapper().listPageObjs(tableUploadUtil);
        tableReturnUtil.setData(objs);
        return tableReturnUtil;
    }

    /**
     * 验证属性值是否存在 只验证 state=1的
     * @param attribute
     * @param checkVal
     * @param checkId
     * @return
     */
    public ResultResponse loadCheckAttributeIsExistence(String attribute, String checkVal, Long checkId){
        ResultResponse response = new ResultResponse();
        try{
            if(StringUtils.isBlank(checkVal)){
                response.setFailureResult("属性值不能为空");
                return response;
            }
            if(StringUtils.isBlank(attribute)){
                response.setFailureResult("属性不能为空");
                return response;
            }
            List<E> objs = getBasicsAdminMapper().listByAttribute(attribute,checkVal,1);
            if(objs.size()>0){
                if(checkId==0){//为0全部校验
                    response.setFailureResult("已被注册");
                    return response;
                }else{//为否则校验是不是自己的
                    Method entityMethod = objs.get(0).getClass().getMethod("getId");
                    entityMethod.setAccessible(true);
                    Long objId = (Long) entityMethod.invoke(objs.get(0));
                    if(objId!=checkId){
                        response.setFailureResult("已被注册");
                        return response;
                    }
                }
            }
            response.setSuccessResult("可以使用");
        }catch (Exception e){
            e.printStackTrace();
            response.setResultCode("400");
        }
        return response;
    }



    /**
     * 验证数据信息和唯一性信息
     * @param pojo
     * @return
     */
    public ResultResponse checkInfo(E pojo){
        ResultResponse response = new ResultResponse();
        try{
            //数据验证
            CheckAnnotationUtil<E> checkAnnotationUtil=new CheckAnnotationUtil<E>(pojo);
            CheckValueResult resultMsg=checkAnnotationUtil.CheckValue();
            if(resultMsg!=null){
                response.setFailureResult(resultMsg.getMessage());
                return response;
            }
            //唯一性验证 只验证 状态为1的
            LoadUniqueAnnotationAttribute<E> loadUniqueAnnotationAttribute=new LoadUniqueAnnotationAttribute<E>(pojo);
            List<UniqueResult> resultList=loadUniqueAnnotationAttribute.getUniqueAttribute();
            for (UniqueResult uniqueResult: resultList) {
                Method entityMethod = pojo.getClass().getMethod("getId");
                entityMethod.setAccessible(true);
                Long id = (Long) entityMethod.invoke(pojo);
                response = loadCheckAttributeIsExistence(uniqueResult.getAttribute(),uniqueResult.getValue(),id);
                if(response.isError()){
                    return response;
                }
            }
            response.setResultCode("200");
        }catch (Exception e){
            e.printStackTrace();
            response.setResultCode("400");
        }
        return response;
    }

}
