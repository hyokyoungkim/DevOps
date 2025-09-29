const emailInput= document.getElementById('emailInput');
const numberInput= document.getElementById('numberInput');
const nicknameInput = document.getElementById('nicknameInput');
const numberSendBtn = document.getElementById('numberSendBtn');
const numberCheckBtn = document.getElementById('numberCheckBtn');
const nicknameCheckBtn = document.getElementById('nicknameCheckBtn');
const changeBtn = document.getElementById('changeBtn');
const checkPopup = document.getElementById('checkPopup');
const cancelBtn = document.getElementById('cancelBtn');
const okBtn = document.getElementById('okBtn');
const currentPwInput = document.getElementById('currentPwInput');

let userInfo = {
    "email": "",
    "nickname":""
}

function getUser() { //로그인 정보 가져오기
    $.ajax({
    type: 'GET',
    url: '/codeenator/api/user/getUser.do?user_agency=web',
    dataType: 'json', 
        success: function (data) {
            viewUserInfo(data.resultMap);
        },
    error: function (err) {
        console.error("요청 실패: ", err);
        if (err.responseText) {
            console.error("응답 텍스트: ", err.responseText);
        }
    }
    });
}

getUser();



function viewUserInfo(userData) { //기존 이메일과 닉네임을 불러와서 inputbox에 넣기
    emailInput.value = userData.email;
    nicknameInput.value = userData.nickname;
}

function isValidEmail(email) { //이메일 형식 확인
    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailPattern.test(email);
}

numberSendBtn.addEventListener('click', function () { //인증번호 전송버튼 클릭
    if (isValidEmail(emailInput.value)) {
        let userEmail = { "email": emailInput.value }
        sendEmail(userEmail);
    } else {
        alert('이메일 형식을 확인해주세요.')
    }
    console.log(userInfo)
})


function sendEmail(email_value) { //이메일 인증번호 전송
    $.ajax({
        type: 'POST',
        url: '/codeenator/api/user/email/getCipher.do?user_agency=web',
        data: JSON.stringify(email_value),
        contentType : 'application/json',
        dataType: 'json', 
        success: function (data) {
            alert(data.msg)
            if (data.msg === '인증번호가 발송되었습니다.') {
                numberSendBtn.innerText = "전송 완료";
                userInfo.email = emailInput.value;
            }
        },
        error: function (err) {
            console.error("실패: ", err);

        }
    });
}




numberCheckBtn.addEventListener('click', function () { //인증번호 확인버튼 클릭

    if (!(numberInput.value === '')) {
        let userEmail = {
            "email": emailInput.value,
            "cipher" : numberInput.value
        }
        console.log(userEmail)
        checkEmailNumber(userEmail)
    }
    else {
        alert('이메일 인증 번호를 입력해주세요.')
    }
})


function checkEmailNumber(check_value) {
        $.ajax({
        type: 'POST',
        url: '/codeenator/api/user/email/checkCipher.do?user_agency=web',
        data: JSON.stringify(check_value),
        contentType : 'application/json',
        dataType: 'json', 
        success: function (data) {
            if (data.result === "success") {
                alert('인증번호가 확인되었습니다.')
                numberCheckBtn.innerText = "인증 완료";
                userInfo.email = emailInput.value;
            } else if (data.result === 'failed') {
                alert('올바른 인증번호가 아닙니다.')
            }
        },
        error: function (err) {
            console.error("실패: ", err);

        }
    });
}



function isValidNickname(nickname) {
    // 정규 표현식: 1~12글자 길이의 문자, 숫자 또는 한글
    const regex = /^[a-zA-Z0-9가-힣]{1,12}$/;
    return regex.test(nickname);
}

nicknameCheckBtn.addEventListener('click', function () {
    if (isValidNickname(nicknameInput.value)) {
        let userNickname = { "nickname": nicknameInput.value };
        checkNickname(userNickname); // 닉네임 확인 함수 호출
    } else {
        alert('닉네임을 확인해주세요');
        userInfo.nickname = '';
    }

});

function checkNickname(nickname_value) { //닉네임 중복확인
    $.ajax({
        type: 'POST',
        url: '/codeenator/api/user/isUsedNickname.do?user_agency=web',
        data: JSON.stringify(nickname_value),
        contentType : 'application/json',
        dataType: 'json', 
        success: function (data) {
            if (data.result === "success") {
                alert('사용 가능한 닉네임입니다.');
                userInfo.nickname = nicknameInput.value; //성공하면 joinForm에 넣기~
                console.log(userInfo)
            } else if (data.result === "failed") {
                alert('이미 사용 중인 닉네임입니다.')
            }
        },
        error: function (err) {
            console.error("실패: ", err);
        }
    });
}


changeBtn.addEventListener('click', function () { //회원정보 변경 버튼

    if (userInfo.email === "" && userInfo.nickname === "") { //둘 다 빈 값 = 변동사항이 없음
        alert('변경사항이 없습니다.')
        console.log(userInfo)
    } else if (userInfo.email === "" || userInfo.nickname === "") {  //둘 중 값이 1개라도 있음 = 변동사항이 있는 부분
        if (userInfo.email === "") {
            userInfo.email = emailInput.value;
        } else if (userInfo.nickname === "") {
            userInfo.nickname = nicknameInput.value;
        }
        console.log(userInfo)
        checkPopup.classList = `rounded shadow p-3 m-auto border w-1/3 fixed bg-white top-[50%] left-[50%] translate-x-[-50%] translate-y-[-50%] z-1`
    }

    
})

function changeUserInfo(userInfo) { //회원정보 변경
    $.ajax({
        type: 'POST',
        url: '/codeenator/api/user/modifyUser.do?user_agency=web',
        data: JSON.stringify(userInfo),
        contentType : 'application/json',
        dataType: 'json', 
        success: function (data) {   
            if (data.result === 'success') {
                alert(data.msg)
                location.reload();
            }
        },
        error: function (err) {
            console.error("실패: ", err);

        }
    });
}

function clickBtn(e) {
    if (e.target.id === 'okBtn') { //확인 버튼
        let user_pw_input = { "password": currentPwInput.value };
        checkUserPw(user_pw_input)
    }

    if (e.target.id === 'cancelBtn') { //취소 버튼 ->팝업창 숨기기
        checkPopup.classList = `rounded shadow p-3 m-auto border w-1/3 fixed bg-white top-[50%] left-[50%] translate-x-[-50%] translate-y-[-50%] z-1 hidden`
    }
}





function checkUserPw(userPw) { //회원정보 변경
    $.ajax({
        type: 'POST',
        url: '/codeenator/api/user/isUsedPassword.do?user_agency=web',
        data: JSON.stringify(userPw),
        contentType : 'application/json',
        dataType: 'json', 
        success: function (data) {
            if(data.result === "success"){
                changeUserInfo(userInfo)

            } else {
                alert(data.msg);
            }
        },
        error: function (err) {
            console.error("실패: ", err);

        }
    });
}