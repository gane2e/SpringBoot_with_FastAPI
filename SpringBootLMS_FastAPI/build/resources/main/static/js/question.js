function confirmSubmit(event) {
    const isConfirmed = confirm("제출 하시겠습니까?");

    if (isConfirmed) {
        alert("제출되었습니다.");
    } else {
        event.preventDefault();
    }
}

function confirmAlert(){
    const isConfirmed = confirm("시험을 종료하시겠습니까?");

    if (isConfirmed) {
        window.location.href = "/members/dashBoard";
    }
}
