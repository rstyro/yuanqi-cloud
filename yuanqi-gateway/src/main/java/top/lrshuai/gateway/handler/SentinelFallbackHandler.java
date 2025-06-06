package top.lrshuai.gateway.handler;

import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayRuleManager;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.BlockRequestHandler;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParameterMetric;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParameterMetricStorage;
import com.alibaba.csp.sentinel.slots.statistic.cache.CacheMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import top.lrshuai.common.core.constant.SecurityConst;
import top.lrshuai.common.core.enums.ApiResultEnum;
import top.lrshuai.common.core.resp.R;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 自定义限流异常处理
 */
@Slf4j
public class SentinelFallbackHandler implements BlockRequestHandler {

    @Override
    public Mono<ServerResponse> handleRequest(ServerWebExchange exchange, Throwable e) {
        // 默认流控
        ApiResultEnum apiResultEnum = ApiResultEnum.BLOCKED_FLOW;
        if (e instanceof FlowException) {
            log.error("触发流控，url={}", exchange.getRequest().getPath());
            apiResultEnum = ApiResultEnum.BLOCKED_FLOW;
        } else if (e instanceof DegradeException) {
            log.error("触发熔断降级，url={}", exchange.getRequest().getPath());
            apiResultEnum = ApiResultEnum.BLOCKED_DEGRADE;
        } else if (e instanceof AuthorityException) {
            log.error("请求未授权，url={}", exchange.getRequest().getPath());
            apiResultEnum = ApiResultEnum.BLOCKED_AUTHORITY;
        } else if (e instanceof ParamFlowException) {
            ParamFlowException paramFlowException = (ParamFlowException) e;
            ParamFlowRule rule = paramFlowException.getRule();
            log.error("触发热点参数流控，url={},resourceName={},时间={}ms,阀值={}"
                    , exchange.getRequest().getPath()
                    , paramFlowException.getResourceName()
                    , rule.getDurationInSec(), rule.getCount());
            apiResultEnum = ApiResultEnum.BLOCKED_FLOW;
            // 重置
//            resetFlowLimit(exchange,"user-qps");
        }
        return ServerResponse.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(R.fail(apiResultEnum)));
    }

    /**
     * 重置限流规则,可能有需求，所以这里记录个入口
     * @param exchange     exchange
     * @param resourceName 资源名称
     */
    public void resetFlowLimit(ServerWebExchange exchange, String resourceName) {
//        String resourceName="user-qps";
        //获取已转换的参数限流规则
        List<ParamFlowRule> rules = GatewayRuleManager.getConvertedParamRules(resourceName);
        // 获取资源的参数度量指标，其中包含了该资源相关的令牌桶计数器。
        ParameterMetric metric = ParameterMetricStorage.getParamMetricForResource(resourceName);
        // 获取参数度量指标，则从中获取第一条规则对应的令牌桶计数器（CacheMap），键值对表示各个用户的请求计数。
        CacheMap<Object, AtomicLong> tokenCounters = metric == null ? null : metric.getRuleTimeCounter(rules.get(0));
        if (Objects.nonNull(tokenCounters)) {
            // 令牌桶余量
            AtomicLong oldQps = tokenCounters.get(exchange.getRequest().getHeaders().getFirst(SecurityConst.USER_ID));
            if (Objects.nonNull(oldQps) && oldQps.get() == 0) {
                long oldValue = oldQps.get();
                // 重置请求计数值
                oldQps.compareAndSet(oldValue, (long) rules.get(0).getCount());
                log.info("已重置resourceName={}的流控,count={}",resourceName,rules.get(0).getCount());
            }
        }
    }
}
