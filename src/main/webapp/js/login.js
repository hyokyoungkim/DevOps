let userInfo = {
    "id": "",
    "password": ""
};

function userLogin(params) { // 로그인 확인하기
    $.ajax({
        type: 'POST',
        url: '/codeenator/api/user/login.do?user_agency=web',
        data: JSON.stringify(params),
        contentType: 'application/json',
        dataType: 'json',
        success: function (data) {
            if (data.result === "success") {
                console.log('성공');
                location.href="/codeenator/main.do";
            } else if (data.result === "failed") {
                alert('아이디 또는 비밀번호가 틀립니다.');
            }
        },
        error: function (err) {
            console.error("실패: ", err);
        }
    });
}

const ID = document.getElementById('id');
const PW = document.getElementById('password');
const submitBtn = document.getElementById('submitBtn');

function handleLogin() { // 빈 값 확인 후 로그인 시도
    if (ID.value === '' && PW.value === '') {
        alert('아이디와 비밀번호를 입력하세요.');
    } else if (ID.value === '') {
        alert('아이디를 입력하세요.');
    } else if (PW.value === '') {
        alert('비밀번호를 입력하세요.');
    } else {
        userInfo.id = ID.value;
        userInfo.password = PW.value;
        console.log(userInfo);
        userLogin(userInfo);
    }
}

// 클릭 이벤트
submitBtn.addEventListener('click', handleLogin);

// 엔터키 입력 처리
document.addEventListener('keydown', function (event) {
    if (event.key === 'Enter') {
        handleLogin();
    }
});
