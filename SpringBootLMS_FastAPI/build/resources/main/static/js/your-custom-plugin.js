// var video = videojs('#videoPlayer');
// console.log(video)
// video.play(); //재생
// video.pause(); //정지
// video.playbackRate = 5.0; //재생속도
// video.currentTime(2); //재생초(2초로이동)
// player.duration(); == 비디오의 길이
// player.currentTime() == 비디오 재생시점
/* videoJS 플러그인 https://videojs.com/plugins/ */

document.addEventListener('DOMContentLoaded', function () {

    let totalDuration = 0; // 모든 영상의 총합길이
    let videoCount = 0; // 비디오의 개수를 셀 변수
    const studentCourseId = document.getElementById('studentCourseId').value;
    let last_watched = document.getElementById('last_watched').value;
    let remainingTime; //last_watched - (영상길이) 뺀 나머지 시청초
    let ProgressRate; // 영상 총 진도율

    document.querySelectorAll('.lecture_btn').forEach(function(button) {

        const videoUrl = button.getAttribute('data-video-url'); // 각 버튼에서 비디오 URL 가져오기
        const videoIndex = button.getAttribute('data-index');
        let videoElement = document.createElement('video');   // 비디오 요소 생성
        videoElement.src = videoUrl; // 비디오 URL을 설정

        // 비디오가 로드되었을 때 duration 값을 얻기 위해 이벤트 리스너 추가
        videoElement.onloadedmetadata = function() {

            const videoDuration = videoElement.duration;  //각 영상의 영상길이
            console.log("각 영상의 비디오 길이 : " + videoDuration);
            totalDuration += videoDuration; // 교육과정의 모든 영상길이 총합
            videoCount++;

            //각 버튼에 영상길이값 할당
            const button = document.querySelector(`[data-index="${videoIndex}"]`);
            if (button) {
                button.setAttribute('data-duration', videoDuration);
            }
            // 모든 비디오 길이의 총합 계산이 끝난 후 콘솔에 출력(테스트완료)
            if (videoCount === document.querySelectorAll('.lecture_btn').length) {
                console.log("모든 비디오의 총 길이 (초):", totalDuration);
            }
        };
        // 영상 목차 클릭 시 영상 변경 이벤트
        button.addEventListener('click', function() {
            var videoUrl = this.getAttribute('data-video-url');
            var localUrl = 'http://localhost:8080';
            var playUrl = localUrl + videoUrl;
            const index = button.getAttribute('data-index');  // 각 비디오의 고유 인덱스
            playVideo(index); // 해당 index의 영상 플레이

            // 영상 소스 설정
            player.src({ src: playUrl, type: "video/mp4" });
        }); //button click event END
    }); //BUTTON for-each END


    //플레이어 객체
    var player = videojs('videoPlayer', {
        autoplay: 'muted', // 자동재생 여부
        controls: true,
        poster: 'https://blog.kakaocdn.net/dn/buBbXt/btspOAijvT5/kK8QRvBlK8LyxId3G69ZA1/img.jpg',
        loop: false, //반복재생
        /* fluid: true, //크기제어?
         aspectRatio: '4:3', //비율*/
        playbackRates: [0.25, 0.5, 1, 1.5, 2, 2.5, 3, 3.5, 4], //재생속도
        controlBar: {                      // 컨트롤 바 설정
            playToggle: true,                // 재생/일시정지 토글 버튼 활성화
            remainingTimeDisplay: true,      // 남은 시간 표시 활성화
            progressControl: true,           // 진행률 컨트롤 활성화
            pictureInPictureToggle: true,    // 화면 내 화면(PiP) 토글 버튼 활성화
            currentTimeDisplay: true,        // 현재 재생 시간 표시 활성화
            qualitySelector: true,           // 화질 선택 컨트롤 활성화
        },
        plugins: {
            hotkeys: {
                enableModifiersForNumber: false, /* 특정행위시 초이동하는것 방지 */
                seekStep: 1 /* 방향키로 1초씩 증가/감소 */
            }
        }
    });

    // 비디오객체 로드후(0.02초)  //250106
    setTimeout(function() {
        const buttons = document.querySelectorAll('.lecture_btn');
        const durations = {}; //영상1: 10초,,영상2:15초.. 저장할 배열
        buttons.forEach(button => {
            const duration = button.dataset.duration;
            const index = button.dataset.index;
            durations[`duration${index}`] = parseInt(duration);
        });
            // last_watched 값에 따라서 어느 구간에 해당하는지 체크
            let testTotalDuration = 0;  // 누적된 총 시간
            remainingTime = last_watched; // 남은 시간

            for (let i = 1; i <= Object.keys(durations).length; i++) {
                const progressBar = document.getElementById('progress-' + i);  // 해당 프로그레스바
                const percentText = document.getElementById('percent-' + i);  //
                const status = document.getElementById('status-' + i);  //

                testTotalDuration += durations[`duration${i}`]; //각 영상 시간 누적

                // last_watched가 이 구간 내에 있는지 체크
                if (remainingTime < testTotalDuration) {
                    targetIndex = i;  // 해당 영상 인덱스를 찾음
                    remainingTime = remainingTime - (testTotalDuration - durations[`duration${i}`]); // 해당 영상에서 남은 시간
                    break;
                } else {
                    progressBar.style.width = '100%';
                    percentText.textContent = '100%';
                    status.textContent = '학습완료';
                    status.style.color = '#0076c0';
                }
            }
        // 결과 확인
        console.log('재생해야 할 영상 인덱스:', targetIndex);
        console.log('이 영상에서 재생해야  할 시간:', remainingTime);

        // targetIndex 영상 재생
        const targetButton = document.querySelector(`button[data-index="${targetIndex}"]`)
        if (targetButton) {
            targetButton.click();
        } else
            console.log("버튼이 없습니다.") //영상 재생위치 찾아 클릭


    }, 100); // 0.1초 후에 실행 //250106 END

    let totalWatchedTime = 0;  // 서버로 전송할 토탈 시청시간
    let lastWatchedTime = 0;   // 영상별 전체 시청시간
    let targetIndex = 0;    // 마지막 시청한 영상 인덱스

    function playVideo(index){

        /* 플레이어 로드이벤트 START */
        player.on('loadeddata', function() {

            //250106
            player.currentTime(remainingTime);
            //250106 END

            var progressBar = null;
            const courseStatus = document.getElementById('status-' + index);  //
            const statusText = courseStatus.innerText;  // 또는 courseStatus.innerText

            //로드한 플레이어의 학습상태가 '학습완료'면 타임업데이트 하지않음
            if(statusText !== '학습완료'){
                /* 타임업데이트 시작 */
                    player.on('timeupdate', function () {

                        var currentTime = player.currentTime();  // 현재 영상의 시청 시간 (초)
                        var duration = player.duration();

                        totalWatchedTime = Number(currentTime) + Number(lastWatchedTime);

                        const progressPercentage = (currentTime / duration) * 100;
                        const progressBar = document.getElementById('progress-' + index);  // 해당 프로그레스바
                        const percentText = document.getElementById('percent-' + index);  //
                        const status = document.getElementById('status-' + index);  // 해당 상태 텍스트

                        progressBar.style.width = progressPercentage.toFixed(0) + '%';
                        percentText.textContent = progressPercentage.toFixed(0) + '%';
                        status.textContent = '학습중';

                            if (progressPercentage === 100) {
                                status.textContent = '학습완료';
                            }
                        /*교육 (총)이수율 계산*/
                        ProgressRate = calculateProgress(totalDuration, totalWatchedTime);
                        document.getElementById("gEduVideoProgress").textContent = ProgressRate;
                    });
                /*타임업데이트 END*/
            } else
                console.log("이 영상은 이미 학습완료 상태입니다.")
        }); /* 플레이어 로드이벤트 END */
    }

    player.on('pause', function (){
        console.log("비디오 재생이 정지되었습니다. \n" +
            "시청자의 마지막 시청길이 : " + totalWatchedTime)
    })

    /* 비디오 종료이벤트 시작 */
    player.on('ended', function (){

        alert("학습이 완료되었습니다.")

        var enrollmentStatus = '';
        if(totalDuration === totalWatchedTime) {
            enrollmentStatus = '학습완료';
        } else
            enrollmentStatus = '학습중';
        lastWatchedTime = totalWatchedTime //이전시청시간할당

        //prograserate있던자리

        console.log("시청자의 마지막 시청길이 : " + totalWatchedTime)
        console.log("비디오 총길이 : " + totalDuration)
        console.log("진도율 : " + ProgressRate)
        console.log("학습상태 : " + enrollmentStatus);
        console.log("비디오 학습이 완료되었습니다.")

        fetch('/student/lastWatchedSave', {
            method: 'POST',  // POST 요청
            headers: {
                'Content-Type': 'application/json',  // JSON 형식으로 데이터 전송
            },
            body: JSON.stringify({
                last_watched: totalWatchedTime, // 마지막 시청 시간
                studentCourseId: studentCourseId, //학생 강좌 ID
                enrollmentStatus : enrollmentStatus, //학습상태
                ProgressRate : ProgressRate //진도율
            })
        })
            .then(response => response.text())
            .then(data => {
                console.log('Server data: ' + data);
            })
            .catch(error => {
                console.error("AJAX 오류", error)
            });
    })
    /* 비디오 종료이벤트 END */

    // 진도율 계산
    function calculateProgress(totalDuration, totalWatchedTime) {
        const progress = (totalWatchedTime / totalDuration) * 100;
        return progress.toFixed(0); // 소수점 둘째 자리까지 반올림
    }

    // 학습완료 시 다음영상 재생
    function nextVideo(index){
        let nextIndex = Number(index) + 1;
        console.log("nextIndex => " + nextIndex);
        const targetButton = document.querySelector(`button[data-index="${nextIndex}"]`)
        if (!targetButton) {
            console.log("다음영상이 없습니다.");
        } else {
            for(let i =1; i<=index; i++) {
                console.log(i + "번 영상 학습완료")
                const successProgressBar = document.getElementById('progress-' + i);
                const successPercentText = document.getElementById('percent-' + i);  //
                const successStatus = document.getElementById('status-' + i);  //

                successProgressBar.style.width ='100%';
                successPercentText.textContent = '100%';
                successStatus.textContent = '학습완료';
                successStatus.style.color = '#0076c0';
            }
            targetButton.click();
        }

    }


})
