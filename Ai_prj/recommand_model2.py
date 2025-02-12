#recommand_model.py
#recommender_model.pth 생성하는 코드
import torch
import torch.nn as nn
import torch.optim as optim
import pandas as pd
from sqlalchemy import create_engine
from sklearn.metrics import accuracy_score, precision_score

# MariaDB 연결
# pymysql을 사용하여 연결합니다.
engine = create_engine("mysql+pymysql://root:1234@localhost:3308/recommend_db")

# Pandas DataFrame 형식으로 reviews 변수에 저장하는 코드
reviews = pd.read_sql("SELECT * FROM reviews", engine)

# 데이터베이스에서 불러온 범주형 값을 머신러닝 모델에서 처리하기 쉽도록 정수 인덱스로 변환
# enumerate()의 결과를 {key: value} 형식으로 딕셔너리로 변환
# user_ids = {1: 0, 2: 1, 3: 2} -> userId:index번호
user_ids = {uid: idx for idx, uid in enumerate(reviews['user_id'].unique())}
product_ids = {pid: idx for idx, pid in enumerate(reviews['product_id'].unique())}

# reviews 데이터프레임에서 user_id 열(컬럼)을 의미
# 변환과정은 
# reviews['user_id'] → [101, 102, 103, 101, 104]
# 매핑 후 → [0, 1, 2, 0, 3]
# 위해서 작업한 userId값은 index값으로 맵핑
reviews['user_id'] = reviews['user_id'].map(user_ids)
reviews['product_id'] = reviews['product_id'].map(product_ids)

# 모델 정의
# 임베딩(Embedding) 개념
# 임베딩은 고차원 데이터를 저차원 벡터로 표현하는 방법입니다. 예를 들어, user_id와 product_id는 각각 고유의 ID로 주어지지만, 
# 이를 의미 있는 숫자 벡터로 변환하여 모델이 학습 가능하도록 만드는 과정이 필요합니다. 
# 이 변환 작업에서 embedding_size는 결과 벡터의 크기를 정의
# embedding_size=50이고 num_users=100(총개수), num_products=200(총개수)이라면
# 임베딩 행렬의 크기:
# 사용자 임베딩: (num_users, embedding_size) → (100, 50)
# 제품 임베딩: (num_products, embedding_size) → (200, 50)
# 사용자 ID와 제품 ID를 통해 임베딩 추출:
# 사용자 ID 10 → 벡터 user_embedding[10] (크기: (1, 50))
# 사용자의 구매 성향, 관심 제품군, 가격대 선호도등등
# user_embedding[5] = [ 0.23, -0.75, 1.12, ..., 0.06, -0.42, 0.89 ]  # 크기: (1, 50)
# 제품 ID 45 → 벡터 product_embedding[45] (크기: (1, 50))
# 벡터의 내적을 통해 사용자-제품의 선호도 계산:
# dot(user_embedding[10], product_embedding[45])
class RecommenderNet(nn.Module):
    def __init__(self, num_users, num_products, embedding_size=50):
        super(RecommenderNet, self).__init__()
        self.user_embedding = nn.Embedding(num_users, embedding_size)
        self.product_embedding = nn.Embedding(num_products, embedding_size)

    def forward(self, user, product):
        user_vec = self.user_embedding(user)
        product_vec = self.product_embedding(product)
        return (user_vec * product_vec).sum(1)

# forward함수 기능 . user, product 아래값이 전달된다는 가정하고,
# user = torch.tensor([5, 20, 15])  # 사용자 ID 3개
# product = torch.tensor([12, 8, 3])  # 제품 ID 3개
# 임베딩 변환
# user_vec.shape    # (3, 50)  -> 사용자 벡터 (3개, 50차원)
# product_vec.shape # (3, 50)  -> 제품 벡터 (3개, 50차원)
# 내적 수행 및 예측 평점 반환
# result = (user_vec * product_vec).sum(1)
# result.shape  # (3,) -> 각 사용자-제품 쌍에 대해 하나의 예측 평점 생성
# esult = torch.tensor([3.5, 4.2, 2.8])  # 예측 평점, 즉, user=5, product=12 조합의 예측 평점이 3.5로 계산됨.

# 모델 초기화
num_users = len(user_ids)
num_products = len(product_ids)
model = RecommenderNet(num_users, num_products)
criterion = nn.MSELoss()
optimizer = optim.Adam(model.parameters(), lr=0.001)

# 데이터 로더 준비
class ReviewDataset(torch.utils.data.Dataset):
    def __init__(self, reviews):
        self.reviews = reviews

    def __len__(self):
        return len(self.reviews)

    # idx번째 user_id, product_id, rating을 Tensor로 변환하여 반환 -> (tensor(7), tensor(12), tensor(4.0))  # (user_id, product_id, rating)
    def __getitem__(self, idx):
        return (
            torch.tensor(self.reviews.iloc[idx]['user_id'], dtype=torch.long),
            torch.tensor(self.reviews.iloc[idx]['product_id'], dtype=torch.long),
            torch.tensor(self.reviews.iloc[idx]['rating'], dtype=torch.float)
        )

dataset = ReviewDataset(reviews) # 데이터셋 객체 생성

# batch_size=32: 한 번에 32개의 데이터 샘플을 가져옴
# 32개의 사용자, 제품, 평점 데이터가 배치 단위로 모델에 전달
dataloader = torch.utils.data.DataLoader(dataset, batch_size=32, shuffle=True)

# 성능 평가 함수
def evaluate_classification_metrics(model, dataset, threshold=3.5):
    """
    모델의 정확도와 정밀도를 평가하는 함수.
    Args:
        model: PyTorch 모델 객체.
        dataset: 평가할 데이터셋 (ReviewDataset).
        threshold: 추천 여부를 결정하는 기준 평점 (기본값: 3.5).
    Returns:
        accuracy: 정확도.
        precision: 정밀도.
    """
    model.eval() # 모델을 평가 모드(evaluation mode)로 설정, 평가할 때는 모델의 가중치를 변경하지 않아야 하므로 필요함.
    all_predictions = [] #예측값과 실제값 저장을 위한 리스트 초기화
    all_labels = []

    with torch.no_grad():  # 평가 시 그래디언트 비활성화, 역전파를 막아 불필요한 메모리 사용을 방지,모델 평가할 때는 가중치를 업데이트하지 않기 때문
        for user, product, rating in dataloader: # dataloader에서 user_id, product_id, rating을 배치 단위(batch_size=32)로 가져옴
            predictions = model(user, product) # 모델을 이용하여 predictions 예측값 계산
            
            # 예측 값을 이진화: threshold 이상이면 추천(1), 아니면 비추천(0)
            # predictions => {4.2, 2.8, 3.6, 1.9}
            # binary_predictions => {1, 0, 1, 0}으로 변환
            binary_predictions = (predictions >= threshold).long()
            binary_labels = (rating >= threshold).long()

            all_predictions.extend(binary_predictions.tolist())
            all_labels.extend(binary_labels.tolist())

    # 정확도 및 정밀도 계산
    accuracy = accuracy_score(all_labels, all_predictions) # 추천(1) 또는 비추천(0)을 맞춘 경우의 비율
    precision = precision_score(all_labels, all_predictions, zero_division=1)  # 추천(1)이라고 예측한 것 중에서 실제 추천(1)인 비율
    return accuracy, precision

# 모델 학습
for epoch in range(150):
    for user, product, rating in dataloader:
        optimizer.zero_grad() # 기울기(Gradient)를 0으로 초기화
        predictions = model(user, product) # RecommenderNet 모델에 user_id와 product_id를 입력하여 예측 평점(predictions)을 생성
        loss = criterion(predictions, rating) # 예측 평점(predictions)과 실제 평점(rating) 간의 차이를 계산한 손실값
        loss.backward() # 역전파(Backpropagation) 및 가중치 업데이트
        optimizer.step() # 계산된 기울기를 바탕으로 모델의 가중치(weight) 업데이트

    # 에포크별 성능 평가
    accuracy, precision = evaluate_classification_metrics(model, dataset)
    print(f"Epoch {epoch + 1}/150 - Accuracy: {accuracy:.4f}, Precision: {precision:.4f}")

# 체크포인트 -> 체크포인트(checkpoint)는 학습이 완료된 모델을 다시 사용할 수 있도록 저장하는 구조
model_state_dict = model.state_dict() # 모델의 모든 가중치(파라미터) 정보를 딕셔너리 형태로 저장
checkpoint = {
    "model_state_dict": model_state_dict,  # 모델 가중치 저장
    "num_users": num_users,               # 사용자 수
    "num_products": num_products,         # 상품 수
    "user_ids": user_ids                  # 사용자 ID 매핑
}

# 모델 저장
torch.save(checkpoint, "recommender_model.pth")
print("모델 저장 완료")

"""
recommender_model.pth 내부 데이타는 이런식으로 저장된다
{
    "model_state_dict": {
        "user_embedding.weight": tensor(100, 50),
        "product_embedding.weight": tensor(200, 50)
    },
    "num_users": 100,
    "num_products": 200,
    "user_ids": {
        10: 0,
        25: 1,
        32: 2,
        ...
    }
}
"""
