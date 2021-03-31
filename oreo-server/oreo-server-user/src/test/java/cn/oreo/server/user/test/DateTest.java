package cn.oreo.server.user.test;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.oreo.common.model.entity.po.User;
import cn.oreo.server.user.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author GuanMingJian
 * @since 2020/10/26
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class DateTest {

    @Resource
    private UserMapper userMapper;

    @Test
    public void testDate(){
        Date date = new Date();
        System.out.println(date);
    }

    @Test
    public void test(){

        User user = new User();
        user.setPhoneNumber("135888888");
        user.setRegisterTime(DateUtil.date());
        user.setAvatarUrl("https://oreox.oss-cn-guangzhou.aliyuncs.com/42491778-cd6a-4388-819e-cccbc1c178f4.jpg");
        user.setUserName(RandomUtil.randomString(15));
        // 0：未完善身份信息，1：完善身份信息
        user.setInfoCode("0");

        userMapper.insert(user);
    }

}
