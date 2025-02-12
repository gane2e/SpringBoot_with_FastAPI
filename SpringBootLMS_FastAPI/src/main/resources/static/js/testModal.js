// 모달 열기 버튼과 모달 요소를 선택

const openModalBtn = document.getElementById('openModalBtn');
const modal = document.getElementById('modal');
const closeBtn = document.getElementById('closeBtn');


// 모달 열기
openModalBtn.onclick = function() {
    modal.style.display = "block";
}

// 모달 닫기
closeBtn.onclick = function() {
    modal.style.display = "none";
}

// 모달 외부 클릭 시 닫기
window.onclick = function(event) {
    if (event.target === modal) {
        modal.style.display = "none";
    }
}

