package cn.oreo.server.system.controller;

import cn.oreo.common.core.entity.OreoResponse;
import cn.oreo.common.core.entity.QueryRequest;
import cn.oreo.common.core.entity.constant.StringConstant;
import cn.oreo.common.core.entity.system.LoginLog;
import cn.oreo.common.core.utils.OreoUtil;
import cn.oreo.server.system.annotation.ControllerEndpoint;
import cn.oreo.server.system.service.ILoginLogService;
import com.wuwenze.poi.ExcelKit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

/**
 * @author GuanMingJian
 * @since 2020/10/5
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("loginLog")
public class LoginLogController {

    private final ILoginLogService loginLogService;

    @GetMapping
    public OreoResponse loginLogList(LoginLog loginLog, QueryRequest request) {
        Map<String, Object> dataTable = OreoUtil.getDataTable(this.loginLogService.findLoginLogs(loginLog, request));
        return new OreoResponse().data(dataTable);
    }

    @GetMapping("currentUser")
    public OreoResponse getUserLastSevenLoginLogs() {
        String currentUsername = OreoUtil.getCurrentUsername();
        List<LoginLog> userLastSevenLoginLogs = this.loginLogService.findUserLastSevenLoginLogs(currentUsername);
        return new OreoResponse().data(userLastSevenLoginLogs);
    }

    @DeleteMapping("{ids}")
    @PreAuthorize("hasAuthority('loginlog:delete')")
    @ControllerEndpoint(operation = "删除登录日志", exceptionMessage = "删除登录日志失败")
    public void deleteLogs(@NotBlank(message = "{required}") @PathVariable String ids) {
        String[] loginLogIds = ids.split(StringConstant.COMMA);
        this.loginLogService.deleteLoginLogs(loginLogIds);
    }

    @PostMapping("excel")
    @PreAuthorize("hasAuthority('loginlog:export')")
    @ControllerEndpoint(operation = "导出登录日志数据", exceptionMessage = "导出Excel失败")
    public void export(QueryRequest request, LoginLog loginLog, HttpServletResponse response) {
        List<LoginLog> loginLogs = this.loginLogService.findLoginLogs(loginLog, request).getRecords();
        ExcelKit.$Export(LoginLog.class, response).downXlsx(loginLogs, false);
    }
}
