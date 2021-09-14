package com.crazymaker.springcloud.kafka.service;


import com.crazymaker.springcloud.kafka.entity.FoundationMsgSubscribe;
import com.crazymaker.springcloud.kafka.repository.FoundationMsgSubscribeRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * 消息订阅配置表
 *
 * @date 2020-11-26 17:23:06
 */
@Service
public class FoundationMsgSubscribeService
{

    @Autowired
    private FoundationMsgSubscribeRepository foundationMsgSubscribeRepository;

    /**
     * 根据ID查询数据
     *
     * @param id 主键ID
     */
    public FoundationMsgSubscribe getById(Long id)
    {
        if (id == null) return null;
        FoundationMsgSubscribe foundationMsgSubscribe = foundationMsgSubscribeRepository.findById(id).orElse(null);
        return foundationMsgSubscribe;
    }

    /**
     * 获取分页列表数据
     *
     * @param foundationMsgSubscribe 查询实例
     * @return 返回分页数据
     */
    public Page<FoundationMsgSubscribe> getPageList(FoundationMsgSubscribe foundationMsgSubscribe, PageRequest pageRequest)
    {
        Specification<FoundationMsgSubscribe> poSpecification = new Specification<FoundationMsgSubscribe>()
        {
            @Override
            public Predicate toPredicate(Root<FoundationMsgSubscribe> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder)
            {
                List<Predicate> list = new ArrayList<>();
                if (null != foundationMsgSubscribe)
                {
                    String subscribeName = foundationMsgSubscribe.getSubscribeName();
                    if (!StringUtils.isEmpty(subscribeName))
                    {
                        list.add(criteriaBuilder.equal(root.get("subscribeName"), subscribeName));
                    }

                    String prjName = foundationMsgSubscribe.getPrjName();
                    if (!StringUtils.isEmpty(prjName))
                    {
                        list.add(criteriaBuilder.equal(root.get("prjName"), prjName));
                    }
                }
                Predicate[] p = new Predicate[list.size()];
                query.where(criteriaBuilder.and(list.toArray(p)));
                return query.getRestriction();
            }
        };
        Page<FoundationMsgSubscribe> pages = foundationMsgSubscribeRepository.findAll(poSpecification, pageRequest);
        return pages;
    }

    /**
     * 获取分页列表数据
     *
     * @param foundationMsgSubscribe 查询实例
     * @return 返回分页数据
     */
    public List<FoundationMsgSubscribe> search(FoundationMsgSubscribe foundationMsgSubscribe)
    {
        Specification<FoundationMsgSubscribe> poSpecification = new Specification<FoundationMsgSubscribe>()
        {
            @Override
            public Predicate toPredicate(Root<FoundationMsgSubscribe> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder)
            {
                List<Predicate> list = new ArrayList<>();
                if (null != foundationMsgSubscribe)
                {
                    String subscribeName = foundationMsgSubscribe.getSubscribeName();
                    if (!StringUtils.isEmpty(subscribeName))
                    {
                        list.add(criteriaBuilder.equal(root.get("subscribeName"), subscribeName));
                    }

                    String prjName = foundationMsgSubscribe.getPrjName();
                    if (!StringUtils.isEmpty(prjName))
                    {
                        list.add(criteriaBuilder.equal(root.get("prjName"), prjName));
                    }
                }
                Predicate[] p = new Predicate[list.size()];
                query.where(criteriaBuilder.and(list.toArray(p)));
                return query.getRestriction();
            }
        };
        List<FoundationMsgSubscribe> list = foundationMsgSubscribeRepository.findAll(poSpecification);
        return list;
    }
    /**
     * 保存数据
     *
     * @param foundationMsgSubscribe 实体对象
     */
    @Transactional
    public FoundationMsgSubscribe save(FoundationMsgSubscribe foundationMsgSubscribe)
    {
        return foundationMsgSubscribeRepository.save(foundationMsgSubscribe);
    }

    /**
     * 删除数据
     *
     * @param id
     */
    @Transactional
    public void delete(Long id)
    {
        foundationMsgSubscribeRepository.deleteById(id);
    }
}

