ARG RUNTIME_IMAGE=amazoncorretto:17.0.7-alpine
FROM ${RUNTIME_IMAGE} AS runtime

# 拷贝打包后的可执行 JAR
COPY ./springboot4-samples-0.0.1.jar /app/app.jar
WORKDIR /app

# Spring Boot 默认端口
EXPOSE 8080

# 可通过环境变量调整运行参数/激活配置文件
ENV SPRING_PROFILES_ACTIVE=default \
    JAVA_TOOL_OPTIONS=""

# 直接运行可执行 JAR
ENTRYPOINT ["java","-jar","/app/app.jar"]