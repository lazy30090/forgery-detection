from fastapi import FastAPI
from pydantic import BaseModel
import time
import uvicorn
import random

app = FastAPI()

# ==========================================
# 1. 严格对齐 Java 的 News 实体类
# ==========================================
class PredictRequest(BaseModel):
    newsId: int       # 对应 News.id (数据库主键)
    title: str = ""   # 对应 News.title
    content: str = "" # 对应 News.content
    picUrl: str = ""  # 对应 News.picUrl (本地路径或URL)

# ==========================================
# 2. 模拟检测接口
# ==========================================
@app.post("/predict")
def predict(request: PredictRequest):
    print(f"\n-------- 收到 Java 请求 (NewsID: {request.newsId}) --------")
    print(f"📄 标题: {request.title[:20]}...")
    print(f"🖼️ 图片: {request.picUrl}")

    # 1. 模拟运算耗时
    print("⏳ AI 模型运算中...", end="", flush=True)
    time.sleep(2)
    print(" 完成!")

    # 2. 生成符合 A21 赛题的假数据
    # A21 数据集里的 label 通常是 "谣言" 或 "事实"
    is_fake = random.choice([True, False])

    # 构造返回数据 (严格对齐 DetectionTask 实体类)
    result = {
        "code": 200,
        "msg": "success",
        "data": {
            # 对应 DetectionTask.resultLabel
            "resultLabel": "谣言" if is_fake else "事实",

            # 对应 DetectionTask.confidence (0~1)
            "confidence": round(random.uniform(0.75, 0.99), 4),

            # 对应 DetectionTask.explanation
            "explanation": f"检测到{'图片篡改痕迹' if is_fake else '内容可信'}，特征向量匹配度高。"
        }
    }

    print(f"🚀 返回给 Java: {result['data']['resultLabel']} (置信度: {result['data']['confidence']})")
    return result

if __name__ == "__main__":
    # 启动监听 5000 端口
    uvicorn.run(app, host="127.0.0.1", port=5000)