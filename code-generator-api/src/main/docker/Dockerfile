# 指定基础镜像 这里springboot项目运行只需要java jdk环境即可
FROM java:latest
# 维护者信息
MAINTAINER zhengqing
# 将本地的可执行文件拷贝到Docker容器中的根目录下
COPY app.sh /
#给app.sh赋予可执行权限
RUN chmod +x /app.sh
# 重命名
ADD *.jar app.jar
# 对外暴漏的端口号
EXPOSE 9100
# 运行
ENTRYPOINT ["/app.sh"]
#ENTRYPOINT ["java", "-jar", "app.jar"]
