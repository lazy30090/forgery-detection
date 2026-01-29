# ==========================================
# 阶段 1: 编译构建 (Builder)
# 使用 Maven 镜像，负责把 Java 代码编译成 .jar 文件
# ==========================================
FROM maven:3.8.5-openjdk-17 AS builder
WORKDIR /app

# 1. 先只复制 pom.xml 下载依赖 (利用 Docker 缓存机制加速)
COPY pom.xml .
# 这一步预下载依赖，下次改代码不改依赖时，这步会秒过
RUN mvn dependency:go-offline -B

# 2. 复制源代码并打包
COPY src ./src
# -DskipTests 跳过单元测试，加快打包速度
RUN mvn clean package -DskipTests

# ==========================================
# 阶段 2: 运行环境 (Runtime)
# 使用轻量级 JDK 镜像，只负责运行，减小镜像体积
# ==========================================
FROM openjdk:17-jdk-slim
WORKDIR /app

# 3. 从阶段 1 把生成的 jar 包复制过来
# 注意：这里假设 target 下只有一个 jar 包，或者你可以写死名字
COPY --from=builder /app/target/*.jar app.jar

# 4. 暴露端口 (仅作为声明，实际映射看 docker-compose)
EXPOSE 8080

# 5. 启动命令
# 关键：强制指定激活 prod (生产环境) 配置
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]