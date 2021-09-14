package kafka;

import com.crazymaker.springcloud.kafka.entity.FoundationMsgSubscribe;
import com.crazymaker.springcloud.kafka.service.FoundationMsgSubscribeService;
import com.crazymaker.springcloud.kafka.start.UnusedApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * GenUtils测试用例
 *
 * @author zlt
 * @date 2019/5/10
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = UnusedApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EntityTest
{

    @Autowired
    private FoundationMsgSubscribeService foundationMsgSubscribeService;

    @Test
    public void testaddOne()
    {
        FoundationMsgSubscribe a=new FoundationMsgSubscribe();
        a.setPrjName("project");
        a.setMsgGroup("group-a");
        a.setMsgTopic("topic-a");
        a.setSpringId("beanName");
        a.setMethodName("methodName");
        //设置表信息
         foundationMsgSubscribeService.save(a);
    }
    @Test
    public void testaddTwo()
    {
        FoundationMsgSubscribe a=new FoundationMsgSubscribe();
        a.setPrjName("project-b");
        a.setMsgGroup("group-b");
        a.setMsgTopic("topic-b");
        a.setSpringId("beanName");
        a.setMethodName("methodName");
        //设置表信息
        foundationMsgSubscribeService.save(a);
    }
    @Test
    public void testsearch()
    {
        FoundationMsgSubscribe sample=new FoundationMsgSubscribe();
        sample.setPrjName("project");

        //设置表信息
        List<FoundationMsgSubscribe> list = foundationMsgSubscribeService.search(sample);
        System.out.println("list = " + list);
    }
}
