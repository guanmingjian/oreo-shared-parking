package cn.oreo.server.system.controller;

import cn.oreo.common.core.entity.OreoResponse;
import cn.oreo.common.core.entity.QueryRequest;
import cn.oreo.common.core.entity.constant.StringConstant;
import cn.oreo.common.core.entity.system.LoginLog;
import cn.oreo.common.core.entity.system.SystemUser;
import cn.oreo.common.core.exception.OreoException;
import cn.oreo.common.core.utils.OreoUtil;
import cn.oreo.server.system.annotation.ControllerEndpoint;
import cn.oreo.server.system.service.ILoginLogService;
import cn.oreo.server.system.service.IUserDataPermissionService;
import cn.oreo.server.system.service.IUserService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wuwenze.poi.ExcelKit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @validated 用来校验数据，如果数据异常则会统一抛出异常，方便异常中心统一处理。
 * @RequiredArgsConstructor 可以生成带参或者不带参的构造方法。
 *
 *  * 在类中加入@RequestMapping("路径名")
 *  * 表示类下所有方法中的请求路径前面都带上此前缀
 *  * 目的是为了区分不同的微服务
 *
 *  * @RestController是@Controller和@ResponseBody的结合体
 *  * @Controller可以讲类注入IOC容器中
 *  * @ResponseBody表示以JSON字符串的形式返回给客户端
 *  * 如果你不想只固定JSON形式，可以只是用@Controller
 *
 * @author GuanMingJian
 * @since 2020/10/5
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("user")
public class UserController {

    private final IUserService userService;
    private final IUserDataPermissionService userDataPermissionService;
    private final ILoginLogService loginLogService;
    private final PasswordEncoder passwordEncoder;

    /**
     * 这个类中的增、改、查 的URL 都是 system/user
     * 主要是靠不同的注解区分
     * 分别是：@PostMapping、@PutMapping、@GetMapping
     */

    // 这里前端交互的URL是 system/user
    // @GetMapping是定义的查询规范请求格式
    // @PreAuthorize("hasAuthority('user:view')") 这就是权限控制了，user:view只是一个标识符，实际上会按这个格式定义区分
    @GetMapping
    @PreAuthorize("hasAuthority('user:view')")
    public OreoResponse userList(QueryRequest queryRequest, SystemUser user) {

        // 这是前端传过来的形式是 xxx?a=1&b=2
        // 看来在spring下能直接接到单个参数然后装入对象中

        // queryRequest:QueryRequest(pageSize=10, pageNum=1, field=null, order=null)
        System.out.println("queryRequest:"+queryRequest);
        // user:SystemUser(userId=null, username=null, password=null, deptId=null, email=null, mobile=null, status=null, createTime=null, modifyTime=null, lastLoginTime=null, sex=null, avatar=null, description=null, deptName=null, createTimeFrom=null, createTimeTo=null, roleId=null, roleName=null, deptIds=null)
        System.out.println("user:"+user);

        // 调业务层查询用户信息，IPage<Object>是mybatis plus的分页插件
        IPage<SystemUser> systemUserIPage = userService.findUserDetailList(user, queryRequest);

        // 看看Record下是不是查询的信息
        List<SystemUser> records = systemUserIPage.getRecords();
        // 我这里用stream流输出，结合lambda表达式
        records.stream().forEach(systemUser -> System.out.println(systemUser));

        // OreoUtil.getDataTable()封装前端分页表格所需数据
        // 实际上就是一个 含有rows和total两个key的Map集合
        Map<String, Object> dataTable = OreoUtil.getDataTable(systemUserIPage);
        dataTable.forEach((key,value)-> System.out.println("key:"+key+"---"+"value:"+value));

        // 封装到OreoResponse的data中返回
        // 我看了有一些项目实战，习惯以键值对传输是比较优秀的方式
        return new OreoResponse().data(dataTable);
    }

    @GetMapping("check/{username}")
    public boolean checkUserName(@NotBlank(message = "{required}") @PathVariable String username) {
        return this.userService.findByName(username) == null;
    }

    /**
     * @Valid 用于验证注解是否符合要求，当不符合要求时就会在方法中返回message 的错误提示信息。
     *
     * @PostMapping 新增时规范的请求形式
     *
     * @ControllerEndpoint 自定义注解，用来标志用途和定义失败后返回的错误信息
     */
    @PostMapping
    @PreAuthorize("hasAuthority('user:add')")
    @ControllerEndpoint(operation = "新增用户", exceptionMessage = "新增用户失败")
    public void addUser(@Valid SystemUser user) {

        // 这里演示一下日志打印，由于在UserController类上加了@Slf4j注解，就有了日志打印功能
        // @Slf4j是lombok插件下的，需要引入依赖和在idea安装插件
        log.info("user:"+user);

        // 由于前端在给后端传值时进行了格式化，所以在spring框架下，能直接封装成对象了

        // 这个类是void类型，所以不需要返回值
        // 但是，后面应该会有异常处理
        this.userService.createUser(user);
    }

    // 和新增原理一样，这里不作多解释，但是注意的是 修改用的是 @PutMapping 和 $put
    @PutMapping
    @PreAuthorize("hasAuthority('user:update')")
    @ControllerEndpoint(operation = "修改用户", exceptionMessage = "修改用户失败")
    public void updateUser(@Valid SystemUser user) {
        this.userService.updateUser(user);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('user:update')")
    public OreoResponse findUserDataPermissions(@NotBlank(message = "{required}") @PathVariable String userId) {
        String dataPermissions = this.userDataPermissionService.findByUserId(userId);
        return new OreoResponse().data(dataPermissions);
    }

    // 因为在UserController中写了@RequestMapping("user")
    // 往后拼接/{userIds}，所以要带 /
    // 这里使用的是restful风格接口，格式是xxxx/{xxxx}/{xxxx}
    // 但是注意，不可以为空，因为为空的url是没有意义的，原则是get请求
    // 在类中用@PathVariable来接，要不然找不到
    @DeleteMapping("/{userIds}")
    @PreAuthorize("hasAuthority('user:delete')")
    @ControllerEndpoint(operation = "删除用户", exceptionMessage = "删除用户失败")
    // @NotBlank(message = "{required}") 实际上在src/main/resources/ValidationMessages.properties
    // 就是获取了提示语，意思是如果为空的时候，就会报错 required=”不能为空“
    public void deleteUsers(@NotBlank(message = "{required}") @PathVariable String userIds) {
        // userIds是一个或多个用户id的字符串，用，号隔开
        // 用，切分格式化为字符串数组
        String[] ids = userIds.split(StringConstant.COMMA);
        // 进行批量删除业务处理
        this.userService.deleteUsers(ids);
    }

    @PutMapping("profile")
    @ControllerEndpoint(exceptionMessage = "修改个人信息失败")
    public void updateProfile(@Valid SystemUser user) throws OreoException {
        this.userService.updateProfile(user);
    }

    @PutMapping("avatar")
    @ControllerEndpoint(exceptionMessage = "修改头像失败")
    public void updateAvatar(@NotBlank(message = "{required}") String avatar) {
        this.userService.updateAvatar(avatar);
    }

    @GetMapping("password/check")
    public boolean checkPassword(@NotBlank(message = "{required}") String password) {
        String currentUsername = OreoUtil.getCurrentUsername();
        SystemUser user = userService.findByName(currentUsername);
        return user != null && passwordEncoder.matches(password, user.getPassword());
    }

    @PutMapping("password")
    @ControllerEndpoint(exceptionMessage = "修改密码失败")
    public void updatePassword(@NotBlank(message = "{required}") String password) {
        userService.updatePassword(password);
    }

    @PutMapping("password/reset")
    @PreAuthorize("hasAuthority('user:reset')")
    @ControllerEndpoint(exceptionMessage = "重置用户密码失败")
    public void resetPassword(@NotBlank(message = "{required}") String usernames) {
        String[] usernameArr = usernames.split(StringConstant.COMMA);
        this.userService.resetPassword(usernameArr);
    }

    @GetMapping("success")
    public void loginSuccess(HttpServletRequest request) {
        String currentUsername = OreoUtil.getCurrentUsername();
        // update last login time
        this.userService.updateLoginTime(currentUsername);
        // save login log
        LoginLog loginLog = new LoginLog();
        loginLog.setUsername(currentUsername);
        loginLog.setSystemBrowserInfo(request.getHeader("user-agent"));
        this.loginLogService.saveLoginLog(loginLog);
    }

    @GetMapping("index")
    public OreoResponse index() {
        Map<String, Object> data = new HashMap<>(5);
        // 获取系统访问记录
        Long totalVisitCount = loginLogService.findTotalVisitCount();
        data.put("totalVisitCount", totalVisitCount);
        Long todayVisitCount = loginLogService.findTodayVisitCount();
        data.put("todayVisitCount", todayVisitCount);
        Long todayIp = loginLogService.findTodayIp();
        data.put("todayIp", todayIp);
        // 获取近期系统访问记录
        List<Map<String, Object>> lastTenVisitCount = loginLogService.findLastTenDaysVisitCount(null);
        data.put("lastTenVisitCount", lastTenVisitCount);
        SystemUser param = new SystemUser();
        param.setUsername(OreoUtil.getCurrentUsername());
        List<Map<String, Object>> lastTenUserVisitCount = loginLogService.findLastTenDaysVisitCount(param);
        data.put("lastTenUserVisitCount", lastTenUserVisitCount);
        return new OreoResponse().data(data);
    }

    @PostMapping("excel")
    @PreAuthorize("hasAuthority('user:export')")
    @ControllerEndpoint(operation = "导出用户数据", exceptionMessage = "导出Excel失败")
    public void export(QueryRequest queryRequest, SystemUser user, HttpServletResponse response) {
        List<SystemUser> users = this.userService.findUserDetailList(user, queryRequest).getRecords();
        ExcelKit.$Export(SystemUser.class, response).downXlsx(users, false);
    }
}
