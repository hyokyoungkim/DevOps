let joinForm = {
    "id": "",
    "password": "",
    "email": "",
    "nickname": ""
}
let currentTermCheck = 0; //0:체크X 1:체크

const idCheck = document.getElementById('idCheck');
const idCheckInput = document.getElementById('idCheckInput');
const numberSend = document.getElementById('numberSend');
const numberSendInput = document.getElementById('numberSendInput');
const numberCheck = document.getElementById('numberCheck');
const numberCheckInput = document.getElementById('numberCheckInput');
const nicknameCheck = document.getElementById('nicknameCheck');
const nicknameCheckInput = document.getElementById('nicknameCheckInput');
const termCheck = document.getElementById('termCheck');
const joinBtn = document.getElementById('joinBtn');
const password = document.getElementById('password');
const passwordCheck = document.getElementById('passwordCheck');


function isValidInput(value) { //아이디 조건 확인
    const regex = /^[a-zA-Z0-9]{6,12}$/;
    return regex.test(value);
}

idCheck.addEventListener('click', function () { //아이디 중복확인
    if (isValidInput(idCheckInput.value)) {
        let userId = { "id": idCheckInput.value }
        checkID(userId);
    } else {
        alert('아이디는 6~12자리 영문 또는 영문+숫자만 가능합니다.')
        joinForm.id = '';
    }
    console.log(joinForm)

})

function checkID(ID_value) { //아이디 중복확인
    $.ajax({
        type: 'POST',
        url: '/codeenator/api/user/isUsedId.do?user_agency=web',
        data: JSON.stringify(ID_value),
        contentType : 'application/json',
        dataType: 'json', 
        success: function (data) {
            if (data.result === "success") {
                alert('사용 가능한 아이디입니다.');
                joinForm.id = idCheckInput.value;
            } else if (data.result === "failed") {
                alert('이미 사용 중인 아이디입니다.')
            }
        },
        error: function (err) {
            console.error("실패: ", err);1
        }
    });
}

function isValidEmail(email) { //이메일 형식 확인
    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailPattern.test(email);
}


numberSend.addEventListener('click', function () { //인증번호 전송
    if (isValidEmail(numberSendInput.value)) {
        let userEmail = { "email": numberSendInput.value }
        sendEmail(userEmail);
    } else {
        alert('이메일 형식을 확인해주세요.')
    }
    console.log(joinForm)
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
                numberSend.innerText = "전송 완료";
                joinForm.email = numberSendInput.value;
            }
        },
        error: function (err) {
            console.error("실패: ", err);

        }
    });
}



numberCheck.addEventListener('click', function () { //인증번호 확인

    if (!(joinForm.email === '')) {
        let userEmail = joinForm.email
        let checkUserEmail = { "email": userEmail, 
            "cipher" : numberCheckInput.value
        }
        checkEmailNumber(checkUserEmail)
        console.log(joinForm)
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
                numberCheck.innerText = "인증 완료";
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

nicknameCheck.addEventListener('click', function () {
    if (isValidNickname(nicknameCheckInput.value)) {
        let userNickname = { "nickname": nicknameCheckInput.value };
        checkNickname(userNickname); // 닉네임 확인 함수 호출
    } else {
        alert('닉네임을 확인해주세요');
        joinForm.nickname = '';
    }
        console.log(joinForm)
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
                joinForm.nickname = nicknameCheckInput.value; //성공하면 joinForm에 넣기~
                console.log(joinForm)
            } else if (data.result === "failed") {
                alert('이미 사용 중인 닉네임입니다.')
            }
        },
        error: function (err) {
            console.error("실패: ", err);
        }
    });
}



function isValidPassword(input) {
    if (input.length < 8 || input.length > 12) {
        return false;
    }

    const hasLetter = /[a-zA-Z]/.test(input);
    const hasDigit = /\d/.test(input);
    const hasSpecialChar = /[!@#$%^&*(),.?":{}|<>]/.test(input) && !/[\'\"]/.test(input);

    const typeCount = [hasLetter, hasDigit, hasSpecialChar].filter(Boolean).length;

    return typeCount >= 2;
}



password.addEventListener('input', function () {
    const input = this.value;
    if (isValidPassword(input)) {
        this.classList.add('valid');
        this.classList.remove('invalid');
    } else {
        this.classList.add('invalid');
        this.classList.remove('valid');
    }
});

// 비밀번호 확인 필드의 값이 변경될 때마다 비밀번호와 비교하고 스타일 적용
passwordCheck.addEventListener('input', function () {
    const input = this.value;
    const isMatching = input === password.value;

    if (isMatching) {
        this.classList.add('valid');
        this.classList.remove('invalid');
    } else {
        this.classList.add('invalid');
        this.classList.remove('valid');
    }
});

// 비밀번호 입력 필드에서 포커스를 잃을 때 유효성 검사 후 joinForm.password에 값 저장
password.addEventListener('blur', function () {
    const input = this.value;
    if (isValidPassword(input)) {
        joinForm.password = input;
    } else {
        joinForm.password = '';
    }
});

// 비밀번호 확인 필드에서 포커스를 잃을 때 비밀번호와 비교
passwordCheck.addEventListener('blur', function () {
    const input = this.value;
    const isMatching = input === password.value;

    if (!isMatching) {
        // 비밀번호 확인이 일치하지 않으면 joinForm.password를 비워둡니다
        joinForm.password = '';
    }
});


termCheck.addEventListener('change', function () { //약관 동의
    if (this.checked) {
        console.log('termCheck');
        currentTermCheck = 1;
    } else {
        currentTermCheck =0
    }
})


joinBtn.addEventListener('click', function () { //회원가입
    if (joinForm.email != '' && joinForm.id != '' && joinForm.nickname != '' && joinForm.password != '' && currentTermCheck === 1) {
        userJoin(joinForm)
        console.log("굿")
    } else {
        alert('필수항목 및 약관에 동의해주세요.')
    }
    console.log(joinForm)

})


function userJoin(form) { //회원가입
    $.ajax({
        type: 'POST',
        url: '/codeenator/api/user/join.do?user_agency=web',
        data: JSON.stringify(form),
        contentType : 'application/json',
        dataType: 'json', 
        success: function (data) {
            if (data.result === "success") {
                alert('회원가입이 완료되었습니다.');
                location.href=`/codeenator/main.do`;
            } else{
                alert(data.msg)
            }
        },
        error: function (err) {
            console.error("실패: ", err);

        }
    });
}