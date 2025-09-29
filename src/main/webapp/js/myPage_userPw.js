const currentPwInput = document.getElementById('currentPwInput');
const changePwInput = document.getElementById('changePwInput');
const checkPwInput = document.getElementById('checkPwInput');
const changePwBtn = document.getElementById('changePwBtn');

let userPw_value = {
    "current_password": "",
    "new_password": ""
};


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



function changePw(pw_value) { // 비밀번호 변경 함수
    $.ajax({
        type: 'POST',
        url: '/codeenator/api/user/modifyPassword.do?user_agency=web',
        data: JSON.stringify(pw_value),
        contentType: 'application/json',
        dataType: 'json',
        success: function (data) {
            if (data.result === "success") {
                alert(data.msg);
                location.reload();
            } else {
                alert(data.msg);
            }
        },
        error: function (err) {
            console.error("실패: ", err);
        }
    });
}


changePwBtn.addEventListener('click', function () { // 비밀번호 변경 
    if (currentPwInput.value !== '' && changePwInput.value !== '' && checkPwInput.value !== '') { //input이 모두 빈 값이 아니어야함
        userPw_value.current_password = currentPwInput.value;

        // 새 비밀번호와 확인 비밀번호가 동일한지 확인
        if (changePwInput.value === checkPwInput.value) {
            if (isValidPassword(changePwInput.value)) { //true인지 확인
                userPw_value.new_password = changePwInput.value;
                changePw(userPw_value);
            } else {
                alert('새 비밀번호가 유효하지 않습니다.');
            }
        } else {
            alert('새 비밀번호와 확인 비밀번호가 일치하지 않습니다.');
        }
    } else {
        alert('모든 입력 필드를 확인해주세요');
    }
});


changePwInput.addEventListener('input', function () { // 비밀번호 입력 필드에서 입력이 변경될 때
    const input = this.value;
    if (isValidPassword(input)) {
        this.classList.add('valid'); //초록색 테두리 추가
        this.classList.remove('invalid'); //빨간색 테두리 제거
    } else {
        this.classList.add('invalid'); //빨간색 테두리 추가
        this.classList.remove('valid'); //초록색 테두리 제거
    }
});


checkPwInput.addEventListener('input', function () { // 비밀번호 확인 필드에서 입력이 변경될 때
    const input = this.value;
    const isMatching = input === changePwInput.value;

    if (isMatching) {
        this.classList.add('valid');
        this.classList.remove('invalid');
    } else {
        this.classList.add('invalid');
        this.classList.remove('valid');
    }
});


changePwInput.addEventListener('blur', function () { // 비밀번호 입력 필드에서 포커스를 잃을 때 유효성 검사
    const input = this.value;
    if (isValidPassword(input)) {
        userPw_value.new_password = input;
    } else {
        userPw_value.new_password = '';
    }
});


checkPwInput.addEventListener('blur', function () { // 비밀번호 확인 필드에서 포커스를 잃을 때 비밀번호 확인
    const input = this.value;
    const isMatching = input === changePwInput.value;

    if (!isMatching) {
        userPw_value.new_password = '';
    }
});
