// 모달 요소 선택
const modal = document.getElementById("myModal");
const openModal = document.getElementById("openModal");
const closeModal = document.getElementById("closeModal");

// 모달 열기
openModal.onclick = function() {
    modal.style.display = "block"; // 모달 표시
}

// 모달 닫기
closeModal.onclick = function() {
    modal.style.display = "none"; // 모달 숨김
}

// 모달 외부 클릭 시 닫기
window.onclick = function(event) {
    if (event.target === modal) {
        modal.style.display = "none"; // 모달 숨김
    }
}
