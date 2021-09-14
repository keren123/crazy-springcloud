package com.crazymaker.springcloud.kafka.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@DynamicInsert
@DynamicUpdate
@Data
@Entity
@Table(name = "foundation_msg_subscribe")
public class FoundationMsgSubscribe implements Serializable
{
    private static final long serialVersionUID = 1L;

    /**
     *Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", insertable = false, nullable = false)
    private Integer id;
    /**
     * 订阅状态 0-停止，1-运行，2-删除
     */
    @Column(name = "status")
    private String status;
    /**
     * 消息组@@true@@false
     */
    @Column(name = "msg_group")
    private String msgGroup;
    /**
     * 主题@@true@@false
     */
    @Column(name = "msg_topic")
    private String msgTopic;
    /**
     * 类路径@@true@@false
     */
    @Column(name = "bean_class")
    private String beanClass;
    /**
     * springId@@true@@false
     */
    @Column(name = "spring_id")
    private String springId;
    /**
     * 方法名@@true@@false
     */
    @Column(name = "method_name")
    private String methodName;
    /**
     * 描述@@true@@false
     */
    @Column(name = "description")
    private String description;
    /**
     * 创建时间@@false@@false
     */
    @Column(name = "create_time")
    private Date createTime;
    /**
     * 更新时间@@false@@false
     */
    @Column(name = "update_time")
    private Date updateTime;
    /**
     * 应用@@false@@false
     */
    @Column(name = "prj_name")
    private String prjName;
    /**
     * 订阅名称@@true@@true
     */
    @Column(name = "subscribe_name")
    private String subscribeName;
}