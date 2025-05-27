# 贝尔实验室 Spring 官方推荐镜像 JDK下载地址 https://bell-sw.com/pages/downloads/
FROM bellsoft/liberica-openjdk-debian:17.0.11-cds
#FROM bellsoft/liberica-openjdk-debian:21.0.5-cds
#FROM findepi/graalvm:java17-native

# 维护者
LABEL maintainer="rstyro"

RUN mkdir -p /cloud/order/logs \
    /cloud/order/backup \

# 设置工作目录为/cloud/mall
WORKDIR /cloud/order

# 环境
ENV SERVER_PORT=8003 LANG=C.UTF-8 LC_ALL=C.UTF-8 JAVA_OPTS=""

EXPOSE ${SERVER_PORT}

ADD ./target/yuanqi-order.jar ./app.jar

SHELL ["/bin/bash", "-c"]

# -Djava.security.egd=file:/dev/./urandom：优化随机数生成，加快启动速度
# -XX:+HeapDumpOnOutOfMemoryError：内存溢出时生成堆转储文件（默认在当前目录）
# -XX:+UseZGC：启用 ZGC 垃圾回收器（低延迟，适合大堆内存场景）
ENTRYPOINT java -Djava.security.egd=file:/dev/./urandom -Dserver.port=${SERVER_PORT} \
           -XX:+HeapDumpOnOutOfMemoryError -XX:+UseZGC ${JAVA_OPTS} \
           -jar app.jar

