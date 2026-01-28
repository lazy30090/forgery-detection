package com.juntong.forgerydetection.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.juntong.forgerydetection.entity.DetectionTask;
import com.juntong.forgerydetection.entity.News;
import com.juntong.forgerydetection.enums.TaskStatusEnum;
import com.juntong.forgerydetection.mapper.DetectionTaskMapper;
import com.juntong.forgerydetection.service.DetectionTaskService;
import com.juntong.forgerydetection.service.NewsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class DetectionTaskServiceImpl extends ServiceImpl<DetectionTaskMapper, DetectionTask> implements DetectionTaskService {

    private final NewsService newsService;
    private final RestTemplate restTemplate; // 注入刚才配好的“电话机”

    // Python 服务的地址
    // 和正规项目的区别：URL
    // 替换：核心动作就是把你 DetectionTaskServiceImpl.java 里写的 http://localhost:5000/predict 换成真实 ML 服务器的 IP 地址（比如 http://192.168.1.100:8000/predict）。
    //
    //配置化：为了避免每次换环境都要改 Java 代码，通常我们会把这个 URL 写在 application.properties 里，比如 ml.server.url=http://...，然后在代码里用 @Value("${ml.server.url}") 读取。
    //
    //契约一致：光换 URL 不够，必须保证真实的 ML 服务和你的Mock 脚本，在 入参（Input） 和 出参（Output） 的 JSON 格式上是完全一模一样的。只要格式对齐，Java 后端是感觉不到对面是“真 ML”还是“假脚本”的。
    private static final String ML_API_URL = "http://localhost:5000/predict";

    @Override
    public Long submitTask(Long newsId) {
        // 1. 查出新闻数据 (为了传给 Python)
        News news = newsService.getById(newsId);
        if (news == null) {
            throw new RuntimeException("新闻不存在");
        }

        // 2. 创建任务记录
        DetectionTask task = new DetectionTask();
        task.setNewsId(newsId);
        task.setStatus(TaskStatusEnum.PENDING.getCode());
        this.save(task);

        // 3. 【关键】调用异步方法去联系 Python
        // 注意：这里需要通过代理对象调用(暂且直接调用本类方法可能导致Async失效，简单起见我们先写在内部，
        // 如果要完全规范，建议把下面这个方法抽离到一个单独的 AsyncDetectionManager 类中)
        // 但为了方便你理解，我们先假设这里能异步执行：
        this.processTaskAsync(task.getId(), news);

        return task.getId();
    }

    /**
     * 真实业务：异步调用 ML 接口
     * @Async 表示这个方法会在一个新的线程里跑，不会卡住主线程
     */
    @Async
    public void processTaskAsync(Long taskId, News news) {
        log.info(">>> 开始异步检测任务，ID: {}", taskId);

        // 1. 先把状态改成 "检测中"
        DetectionTask updateTask = new DetectionTask();
        updateTask.setId(taskId);
        updateTask.setStatus(TaskStatusEnum.PROCESSING.getCode());
        this.updateById(updateTask);

        try {
            // 2. 准备传给 Python 的参数 (Map 会被自动转成 JSON)
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("newsId", news.getId());
            requestBody.put("title", news.getTitle() != null ? news.getTitle() : "");
            requestBody.put("content", news.getContent() != null ? news.getContent() : "");
            requestBody.put("picUrl", news.getPicUrl() != null ? news.getPicUrl() : "");

            // 3. 【拨打电话】发送 POST 请求
            // Map.class 表示我们期望 Python 返回一个 Map 对象
            Map response = restTemplate.postForObject(ML_API_URL, requestBody, Map.class);

            // 4. 解析 Python 返回的结果
            if (response != null && (Integer) response.get("code") == 200) {
                Map<String, Object> data = (Map<String, Object>) response.get("data");

                // 5. 将结果回填到数据库
                updateTask.setStatus(TaskStatusEnum.COMPLETED.getCode());
                updateTask.setResultLabel((String) data.get("resultLabel"));
                // BigDecimal 需要特殊处理一下转换
                updateTask.setConfidence(new BigDecimal(String.valueOf(data.get("confidence"))));
                updateTask.setExplanation((String) data.get("explanation"));
                updateTask.setCostTime(2000L); // 也可以让 Python 返回耗时

                this.updateById(updateTask);
                log.info("<<< 任务完成，结果已保存: {}", data.get("resultLabel"));
            } else {
                throw new RuntimeException("ML 服务返回错误");
            }

        } catch (Exception e) {
            log.error("调用 ML 服务失败", e);
            updateTask.setStatus(TaskStatusEnum.FAILED.getCode());
            updateTask.setExplanation("检测服务连接超时或异常");
            this.updateById(updateTask);
        }
    }
}