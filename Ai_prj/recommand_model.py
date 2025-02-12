import torch
import torch.nn as nn
import torch.optim as optim
import pandas as pd
from sqlalchemy import create_engine
from sklearn.metrics import accuracy_score, precision_score

# MariaDB 연결
# pymysql을 사용하여 연결합니다.
engine = create_engine("mysql+pymysql://root:1234@localhost:3306/lms")

# 데이터 로드
reviews = pd.read_sql("SELECT * FROM reviews", engine)

# 데이터 전처리
user_ids = {uid: idx for idx, uid in enumerate(reviews['member_id'].unique())}
product_ids = {pid: idx for idx, pid in enumerate(reviews['course_id'].unique())}

reviews['member_id'] = reviews['member_id'].map(user_ids)
reviews['course_id'] = reviews['course_id'].map(product_ids)

# 모델 정의
class RecommenderNet(nn.Module):
    def __init__(self, num_users, num_products, embedding_size=50):
        super(RecommenderNet, self).__init__()
        self.user_embedding = nn.Embedding(num_users, embedding_size)
        self.product_embedding = nn.Embedding(num_products, embedding_size)

    def forward(self, user, product):
        user_vec = self.user_embedding(user)
        product_vec = self.product_embedding(product)
        return (user_vec * product_vec).sum(1)

# 모델 초기화
num_users = len(user_ids)
num_products = len(product_ids)
print("num_products")
print(num_products)

model = RecommenderNet(num_users, num_products)
criterion = nn.MSELoss()
optimizer = optim.Adam(model.parameters(), lr=0.001)

# 데이터 로더 준비
class ReviewDataset(torch.utils.data.Dataset):
    def __init__(self, reviews):
        self.reviews = reviews

    def __len__(self):
        return len(self.reviews)

    def __getitem__(self, idx):
        return (
            torch.tensor(self.reviews.iloc[idx]['member_id'], dtype=torch.long),
            torch.tensor(self.reviews.iloc[idx]['course_id'], dtype=torch.long),
            torch.tensor(self.reviews.iloc[idx]['rating'], dtype=torch.float)
        )

dataset = ReviewDataset(reviews)
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
    model.eval()
    all_predictions = []
    all_labels = []

    with torch.no_grad():  # 평가 시 그래디언트 비활성화
        for user, product, rating in dataloader:
            predictions = model(user, product)
            # 예측 값을 이진화: threshold 이상이면 추천(1), 아니면 비추천(0)
            binary_predictions = (predictions >= threshold).long()
            binary_labels = (rating >= threshold).long()

            all_predictions.extend(binary_predictions.tolist())
            all_labels.extend(binary_labels.tolist())

    # 정확도 및 정밀도 계산
    accuracy = accuracy_score(all_labels, all_predictions)
    precision = precision_score(all_labels, all_predictions, zero_division=1)  # 정밀도 계산
    return accuracy, precision

# 모델 학습
for epoch in range(50): 
    for user, product, rating in dataloader:
        optimizer.zero_grad()
        predictions = model(user, product)
        loss = criterion(predictions, rating)
        loss.backward()
        optimizer.step()

    # 에포크별 성능 평가
    accuracy, precision = evaluate_classification_metrics(model, dataset)
    print(f"Epoch {epoch + 1}/150 - Accuracy: {accuracy:.4f}, Precision: {precision:.4f}")

# 필요한 데이터들
model_state_dict = model.state_dict()
checkpoint = {
    "model_state_dict": model_state_dict,  # 모델 가중치 저장
    "num_users": num_users,               # 사용자 수
    "num_products": num_products,         # 상품 수
    "user_ids": user_ids                  # 사용자 ID 매핑
}


# 모델 저장
torch.save(checkpoint, "recommender_model.pth")
print("모델 저장 완료")


# user_id=10번 사용자의 내부 인덱스 확인
user_id = 10
user_idx = user_ids.get(user_id, None)

if user_idx is None:
    print("user_id=10이 존재하지 않습니다.")
    exit()

# 모든 제품 리스트 가져오기
products_df = pd.read_sql("SELECT course_id FROM courses WHERE course_id <=50", engine)
all_products = set(products_df["course_id"])

print("-------모든 교육과정-------")
print(all_products)

# 필터링된 데이터 출력
query = "SELECT course_id FROM reviews WHERE member_id = %s"
products_df = pd.read_sql(query, engine, params=(user_id,))

# products_df에서 course_id 값을 리스트로 변환
purchased_products = set(products_df['course_id'].tolist())

# 차집합 연산 수행
unseen_products = list(all_products - purchased_products)



print("-------수강하지 않은 교육과정-------")
print(unseen_products)

# 추천할 제품 ID가 모델이 학습한 제품 개수를 초과하지 않도록 필터링
unseen_products = [p for p in unseen_products if p < num_products]


if not unseen_products:
    print(" 추천할 제품이 없습니다.")
    exit()

# 추천을 위해 모델에 입력할 데이터 생성
user_tensor = torch.tensor([user_idx] * len(unseen_products))  # 같은 사용자 ID 반복
product_tensor = torch.tensor(unseen_products)  # 미구매 제품 ID 리스트
print("---------product_tensor---------")
print(product_tensor)

print(f"num_products: {num_products}")
print(f"최대 product index: {product_tensor.max()}")

# 예측 수행 (미구매 제품에 대한 평점 예측)
with torch.no_grad():
    predicted_ratings = model(user_tensor, product_tensor)

# 예측 평점이 높은 상위 5개 제품 추천
top_k = min(5, len(unseen_products))
top_indices = torch.topk(predicted_ratings, k=top_k).indices.numpy()
recommended_product_ids = [unseen_products[i] for i in top_indices]

print("-----------------------------------")
print(recommended_product_ids)


# 추천 제품 ID에 해당하는 상품명 조회
recommended_products = products_df[products_df["course_id"].isin(recommended_product_ids)]

# 결과 출력
print("\n 사용자 10번에게 추천할 제품 목록:")
print(recommended_products)

