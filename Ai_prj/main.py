import os
import torch
from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from sqlalchemy import create_engine, text, bindparam

# FastAPI 초기화
app = FastAPI()

# CORS 설정
app.add_middleware(
    CORSMiddleware,
    allow_origins=["http://localhost:8080"],  
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)


# DATABASE_URL = "mysql+pymysql://root:1234@localhost:3308/recommend_db"
engine = create_engine("mysql+pymysql://root:1234@localhost:3306/lms")
# engine = create_engine(DATABASE_URL)

# RecommenderNet 모델 정의
class RecommenderNet(torch.nn.Module):
    def __init__(self, num_users, num_products, embedding_size=50):
        super(RecommenderNet, self).__init__()
        self.user_embedding = torch.nn.Embedding(num_users, embedding_size)
        self.product_embedding = torch.nn.Embedding(num_products, embedding_size)

    def forward(self, user, product):
        user_vec = self.user_embedding(user)
        product_vec = self.product_embedding(product)
        return (user_vec * product_vec).sum(1)

# 모델 및 체크포인트 로드
"""
저장된 recommender_model.pth 파일을 불러와서 추천 모델을 복원하는 코드
사용자 수, 상품 수, ID 매핑 정보를 다시 불러와 기존 환경을 유지
저장된 가중치를 모델에 로드하여 학습된 상태를 그대로 재사용 가능
평가 모드(eval())로 설정하여 예측을 수행할 준비를 마침
"""
try:                  # 현재 실행 중인 파일(__file__)의 디렉토리 경로를 기준으로 recommender_model.pth 파일을 찾음
    checkpoint_path = os.path.join(os.path.dirname(__file__), "recommender_model.pth")
    checkpoint = torch.load(checkpoint_path, weights_only=False)
    num_users = checkpoint["num_users"]
    num_products = checkpoint["num_products"]
    user_ids = checkpoint["user_ids"]  # user_id 매핑 정보

    # 저장된 num_users, num_products를 이용하여 추천 모델을 다시 생성
    # num_users=30, num_products=40이면 (30, 50), (40, 50) 크기의 임베딩 레이어가 생성됨
    model = RecommenderNet(num_users, num_products)\
    
    # model_state_dict에 저장된 모델 가중치를 현재 모델에 적용
    # 즉, 학습했던 사용자/상품 임베딩 벡터(저장된 값)를 다시 불러와 모델에 적용
    model.load_state_dict(checkpoint["model_state_dict"]) 
    model.eval()
except Exception as e:
    raise RuntimeError(f"Error loading the model checkpoint: {str(e)}")

# 추천 API
@app.get("/recommend/{user_id}")
async def recommend(user_id: int):
    if user_id not in user_ids:
        raise HTTPException(status_code=404, detail="Invalid user_id")

    # 모델을 사용해 상품 추천 점수 계산
    # user_id는 user_ids 딕셔너리를 사용하여 내부 인덱스로 변환
    user_tensor = torch.tensor([user_ids[user_id]], dtype=torch.long)
   
    # 모델을 이용해 사용자가 모든 상품에 대한 추천 점수를 예측
    """  예측 점수
        product_scores = [
            (1, 4.2),   # 상품 ID 1, 추천 점수 4.2
            (2, 3.8),   # 상품 ID 2, 추천 점수 3.8
            (3, 4.9),   # 상품 ID 3, 추천 점수 4.9
            (4, 2.1)    # 상품 ID 4, 추천 점수 2.1
        ]
    """
    product_scores = []
    for product_id in range(num_products):
        product_tensor = torch.tensor([product_id], dtype=torch.long)
        with torch.no_grad():
            score = model(user_tensor, product_tensor).item()            
        product_scores.append((product_id + 1, score))  # product_id는 0부터 시작하므로, +1을 해주어 실제 DB의 상품 ID와 맞춤

    # 점수 기준으로 상위 5개 상품 선택
    product_scores.sort(key=lambda x: x[1], reverse=True)
    top_product_ids = [pid for pid, _ in product_scores[:5]]

    # 데이터베이스에서 상품 이름 조회
    """ 결과를 리스트 형태로 변환하여 반환
        products = [
            {"id": 3, "name": "스마트폰"},
            {"id": 1, "name": "노트북"},
            {"id": 2, "name": "태블릿"}
        ]
    """
    try:
        with engine.connect() as conn:
            query = text("""
                  SELECT course_id
                    FROM courses
                    WHERE course_id IN :ids
            """).bindparams(bindparam("ids", expanding=True))
            # top_product_ids 리스트에 있는 상품의 id, name을 DB에서 가져옴
            result = conn.execute(query, {"ids": tuple(top_product_ids)})
            # 결과를 리스트로 변환
            products = [{"course_id": row[0]} for row in result]

            # 만약 추천된 상품이 DB에 존재하지 않으면 404 에러 반환
            if not products: 
                raise HTTPException(status_code=404, detail="No products found for the recommendations.")
    except Exception as e:
        print(f"Database error: {str(e)}")  # 로그 출력
        raise HTTPException(status_code=500, detail=f"Database error: {str(e)}")

    return {"recommendations": products}
