import pandas as pd
from sqlalchemy import create_engine, text
from datetime import datetime

# ==========================================
# 1. 配置区域
# ==========================================
CSV_FILE_PATH = 'D:\\Develop\\Code\\data\\A21\\10-【A21】面向新闻场景的伪造检测平台【君同未来】公开数据集news.csv'

DB_USER = 'root'
DB_PASS = '123456'
DB_HOST = 'localhost'
DB_PORT = '3306'
DB_NAME = 'forgery_detection'

# ==========================================
# 2. 建立连接
# ==========================================
connection_str = f"mysql+pymysql://{DB_USER}:{DB_PASS}@{DB_HOST}:{DB_PORT}/{DB_NAME}?charset=utf8mb4"
engine = create_engine(connection_str)

print("🚀 开始读取 CSV 文件...")

try:
    # ==========================================
    # 运行前清空旧数据
    # ==========================================
    print("🧹 检测到可能有旧数据，正在清空 sys_news 表...")
    with engine.connect() as con:
        # TRUNCATE 会清空所有数据并重置自增 ID，速度快且干净
        con.execute(text("TRUNCATE TABLE sys_news;"))
        con.commit() # 提交清空操作
    print("✨ 旧数据已清空，准备写入新数据。")

    # ==========================================
    # 3. 读取并清洗数据
    # ==========================================
    df = pd.read_csv(CSV_FILE_PATH, encoding='utf-8')
    print(f"✅ 读取到 {len(df)} 条数据")

    # 3.1 补充固定字段
    df['data_source'] = 1 

    # 3.2 手动生成入库时间
    current_time = datetime.now()
    df['create_time'] = current_time

    # 3.3 时间格式转换
    if 'publish_time' in df.columns:
        df['publish_time'] = pd.to_datetime(df['publish_time'], errors='coerce')
    if 'check_time' in df.columns:
        df['check_time'] = pd.to_datetime(df['check_time'], errors='coerce')

    # ==========================================
    # 4. 数据清洗 (去除空格和换行符)
    # ==========================================
    print("🛁 正在清洗脏数据 (去除空格和换行符)...")
    
    # 清洗 URL
    if 'url' in df.columns:
        df['url'] = df['url'].astype(str).str.strip().replace('nan', None)
    
    # 清洗图片 URL
    if 'pic_url' in df.columns:
        df['pic_url'] = df['pic_url'].astype(str).str.strip().replace('nan', None)
        
    # 清洗标题
    if 'title' in df.columns:
         df['title'] = df['title'].astype(str).str.strip()

    # ==========================================
    # 5. 筛选列并写入
    # ==========================================
    existing_cols = list(df.columns)

    allowed_cols = [
        'news_id', 'title', 'content', 'publish_time', 'platform',
        'author', 'url', 'pic_url', 'check_time', 'label',
        'hashtag', 'data_source', 'create_time'
    ]

    final_cols = [col for col in existing_cols if col in allowed_cols]
    df_final = df[final_cols]

    print("⏳ 正在写入数据库 (sys_news)...")

    # 写入数据
    df_final.to_sql(name='sys_news', con=engine, if_exists='append', index=False, chunksize=1000)

    print(f"🎉 成功导入！共写入 {len(df_final)} 条数据。")

except Exception as e:
    print(f"❌ 发生错误: {e}")