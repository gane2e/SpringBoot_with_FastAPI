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

})
