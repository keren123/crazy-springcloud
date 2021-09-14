package com.crazymaker.springcloud.kafka.repository;

import com.crazymaker.springcloud.kafka.entity.FoundationMsgSubscribe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
/**
    com.gsafety.dataplatform.srma.model.FoundationMsgSubscribe;


 * 消息订阅表
 *
 * @author jake
 * @date 2020-11-26 17:23:06
 */
public interface FoundationMsgSubscribeRepository extends JpaRepository<FoundationMsgSubscribe, Long>, JpaSpecificationExecutor<FoundationMsgSubscribe>{
}