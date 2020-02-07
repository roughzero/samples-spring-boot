package rough.samples.spring.boot.test;

import lombok.extern.apachecommons.CommonsLog;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@CommonsLog
@Component
public class AspectSample {

    /**
     * 此方法仅指定切片规则，不实际运行
     */
    @Pointcut("execution(* *..*.service.impl.*..*(..))") // 切片位置为包名中含有 "service.impl" 的所有类所有方法
    // @Pointcut("@annotation(org.springframework.stereotype.Controller)") // 也可以使用注解切片
    private void pointcut() {
    }

    @Before("pointcut()") // 定义切片的方法名，注意有 ()
    public void doBefore() {

    }

    @After("pointcut()")
    public void doAfter() {

    }

    @AfterReturning(pointcut = "pointcut()", returning = "retVal")
    public void doAfterReturning(Object retVal) {

    }

    @AfterThrowing(pointcut = "pointcut()", throwing = "e")
    public void doAfterThrowing(Exception e) {

    }

    @Around("pointcut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        // 前置处理，这里示例为输入执行时间
        // 对切片语法苦手的或者需要精细定义的，可以在这边再过滤
        StringBuilder logMsg = new StringBuilder();
        logMsg.append(joinPoint.getSignature().getDeclaringType().getSimpleName()).append(".");
        logMsg.append(joinPoint.getSignature().getName());
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            return joinPoint.proceed();
        } finally {
            // 后置内容
            stopWatch.stop();
            log.info(logMsg.append(" cost: ").append(stopWatch.getTotalTimeMillis()));
        }
    }
}
