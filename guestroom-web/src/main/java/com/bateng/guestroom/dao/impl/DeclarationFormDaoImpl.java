package com.bateng.guestroom.dao.impl;

import com.bateng.guestroom.dao.repository.DeclarationFormRepository;
import com.bateng.guestroom.entity.DeclarationForm;
import com.bateng.guestroom.entity.PageVo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("declarationFormRepository")
public class DeclarationFormDaoImpl implements DeclarationFormRepository {

    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public PageVo<DeclarationForm> findDeclarationFormByPage(PageVo<DeclarationForm> pageVo, DeclarationForm declarationForm) {
        StringBuilder sb=new StringBuilder();
        Map<String,Object> params=new HashMap<String,Object>();
        sb.append("from DeclarationForm df where 1=1");

        if(declarationForm!=null && declarationForm.getUser()!=null && declarationForm.getUser().getId()!= null && declarationForm.getUser().getId()!= 0 ) {
            sb.append(" and df.user.id = :uid ");
            params.put("uid",declarationForm.getUser().getId());
        }

        if(declarationForm.getFlag() != null){
            sb.append(" and df.flag = :flag");
            params.put("flag",declarationForm.getFlag());
        }

        if(declarationForm.getFormName() !=null && !declarationForm.getFormName().equals("")){
            sb.append(" and df.formName like :formName");
            params.put("formName","%"+declarationForm.getFormName()+"%");
        }

        Query query=entityManager.createQuery(sb.toString());//生成查询对象

        //设置参数
         for(Map.Entry<String,Object> entry:params.entrySet()){
             query.setParameter(entry.getKey(),entry.getValue());
         }

        //计算总页数，总数量
        List<DeclarationForm> list= query.getResultList();//查询的是总数据
        pageVo.setTotalCount(list.size());//设置总数据
        pageVo.setTotalPages((int) (pageVo.getTotalCount()%pageVo.getNumPerPage()==0?pageVo.getTotalCount()/pageVo.getNumPerPage():(pageVo.getTotalCount()/pageVo.getNumPerPage()+1)));
        //设置总页数

        query.setMaxResults(pageVo.getNumPerPage());//设置查询数量
        query.setFirstResult((pageVo.getPageNum()-1)*pageVo.getNumPerPage());//设置哪里开始取数据

        //query.getResultList();//获取查询结果

        pageVo.setContents( query.getResultList());//数据结果


        return pageVo;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}