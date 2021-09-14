package com.crazymaker.springcloud.kafka.mq.consumer;

import com.crazymaker.springcloud.kafka.entity.FoundationMsgSubscribe;
import com.crazymaker.springcloud.standard.context.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Slf4j
public class BeanMassageListener implements MessageListener
{

    FoundationMsgSubscribe subscribe;

    public BeanMassageListener(FoundationMsgSubscribe subscribe)
    {
        this.subscribe = subscribe;
    }

    /**
     * 消息主题，表示只接受给定主题下的消息
     *
     * @return 消息主题
     */
    @Override
    public String getTopic()
    {
        return subscribe.getMsgTopic();
    }

    /**
     * 处理接收到的消息
     *
     * @param message 收到的消息
     */
    @Override
    public void process(ConsumeMessage message)
    {
        Object target = null;
        Class clazz = null;
        if (StringUtils.isNotBlank(subscribe.getSpringId()))
        {
            target = SpringContextUtil.getBean(subscribe.getSpringId());
            clazz = target.getClass();
        } else if (StringUtils.isNotBlank(subscribe.getBeanClass()))
        {
            try
            {
                clazz = Class.forName(subscribe.getBeanClass());
                target = clazz.newInstance();
            } catch (Exception e)
            {
                log.error("", e);
            }

        } else
        {

            target = new EmptyComsumer();
            clazz = EmptyComsumer.class;
        }
        if (target == null)
        {
            log.error("消息订阅配置有误");
            return;
        }
        String methodName = subscribe.getMethodName();
        if (StringUtils.isEmpty(methodName))
        {
            methodName = "process";
        }
        final Method method = getMethod(clazz, methodName);

        if (method == null)
        {
            log.error("消息订阅配置有误，方法配置错误");
            return;
        }
        try
        {
            method.invoke(target, message);
        } catch (IllegalAccessException e)
        {
            e.printStackTrace();
        } catch (InvocationTargetException e)
        {
            e.printStackTrace();
        }

    }
    public static Method getMethod(Class clazz,String methodName){
        try {
            Method[] methods=clazz.getMethods();
            for(Method method : methods){
                if(method.getName().equals(methodName)){
                    return method;
                }
            }

            return null;
        } catch (Exception e) {
        }
        return null;
    }

    static class EmptyComsumer
    {
        public void process(ConsumeMessage message)
        {
            System.out.println("message = " + message.getValueAsString());
        }
    }
}