document.addEventListener('DOMContentLoaded', function() {
    const openPreviews = document.querySelectorAll('.openPreview'); // 모든 'openPreview' 링크 선택

    openPreviews.forEach(function(preview) {
        preview.onclick = function() {
            const modalId = preview.getAttribute('data-modal-id'); // 클릭한 링크에 해당하는 모달 id 가져오기
            const modal = document.getElementById(modalId); // 해당 모달 요소 선택
            modal.style.display = "block"; // 모달 열기
        };
    });

    // 각 모달의 닫기 버튼 설정
    const closeButtons = document.querySelectorAll('.close');
    closeButtons.forEach(function(closeBtn) {
        closeBtn.onclick = function() {
            const modal = closeBtn.closest('.modal'); // 닫기 버튼이 속한 모달을 찾기
            modal.style.display = "none"; // 모달 닫기
        };
    });
    // 각 모달의 닫기 버튼 설정
    const closeButtons2 = document.querySelectorAll('.modal_close_btn');
    closeButtons2.forEach(function(closeBtn) {
        closeBtn.onclick = function() {
            const modal = closeBtn.closest('.modal'); // 닫기 버튼이 속한 모달을 찾기
            modal.style.display = "none"; // 모달 닫기
        };
    });

    // 모달 외부 클릭 시 닫기
    window.onclick = function(event) {
        const modals = document.querySelectorAll('.modal');
        modals.forEach(function(modal) {
            if (event.target === modal) {
                modal.style.display = "none"; // 모달 외부 클릭 시 닫기
            }
        });
    };
});
