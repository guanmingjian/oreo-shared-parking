package cn.oreo.server.system.controller;

import cn.oreo.common.core.entity.LogSo;
import cn.oreo.common.core.entity.OreoResponse;
import cn.oreo.common.core.entity.QueryRequest;
import cn.oreo.common.core.entity.constant.StringConstant;
import cn.oreo.common.core.entity.system.Log;
import cn.oreo.common.core.utils.OreoUtil;
import cn.oreo.server.system.annotation.ControllerEndpoint;
import cn.oreo.server.system.service.ILogService;
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
@RequestMapping("log")
public class LogController {

    private final ILogService logService;

    /**
     * 供远程服务调用保存接口
     * @param logSo
     * @return
     */
    @PostMapping("save")
    public String saveLog(
            @RequestBody(required = false) LogSo logSo
    ){
        System.out.println("logSo:"+logSo);
        logService.saveLog(logSo.getPoint(), logSo.getMethod(), logSo.getIp(), logSo.getOperation(), logSo.getUsername(), logSo.getStart());
        return "保存日志成功";
    }

    @GetMapping
    public OreoResponse logList(Log log, QueryRequest request) {
        Map<String, Object> dataTable = OreoUtil.getDataTable(this.logService.findLogs(log, request));
        return new OreoResponse().data(dataTable);
    }

    @DeleteMapping("{ids}")
    @PreAuthorize("hasAuthority('log:delete')")
    @ControllerEndpoint(exceptionMessage = "删除日志失败")
    public void deleteLogss(@NotBlank(message = "{required}") @PathVariable String ids) {
        String[] logIds = ids.split(StringConstant.COMMA);
        this.logService.deleteLogs(logIds);
    }


    @PostMapping("excel")
    @PreAuthorize("hasAuthority('log:export')")
    @ControllerEndpoint(exceptionMessage = "导出Excel失败")
    public void export(QueryRequest request, Log lg, HttpServletResponse response) {
        List<Log> logs = this.logService.findLogs(lg, request).getRecords();
        ExcelKit.$Export(Log.class, response).downXlsx(logs, false);
    }
}
