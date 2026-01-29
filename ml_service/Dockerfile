# 使用官方轻量级 Python 3.9 镜像
FROM python:3.9-slim

# 设置工作目录
WORKDIR /app

# 设置 pip 镜像源 (阿里云源)，加速国内下载
RUN pip config set global.index-url https://mirrors.aliyun.com/pypi/simple/

# 1. 安装依赖
# 如果有 requirements.txt，可以用 COPY requirements.txt . && pip install -r requirements.txt
# 这里为了简便，直接装
RUN pip install fastapi uvicorn pydantic

# 2. 复制脚本代码
COPY mock_ml_server.py .

# 3. 暴露端口
EXPOSE 5000

# 4. 启动命令
# 这里的 host 必须是 0.0.0.0，不能是 127.0.0.1，否则外部访问不到
CMD ["python", "mock_ml_server.py"]