const userIdInput = document.getElementById('userIdInput')
const userEmailInput = document.getElementById('userEmailInput')
const findPwBtn = document.getElementById('findPwBtn')

findPwBtn.addEventListener('click', function () {
    if (!(userIdInput.value === '' && userEmailInput.value === '')) {
        let userInfo = { "id": userIdInput.value, "email": userEmailInput.value }
        findPW(userInfo)
    } else {
        alert('아이디와 이메일을 모두 입력해주세요')
    }
})



function findPW(userInfo_value) { //회원가입
    $.ajax({
        type: 'POST',
        url: '/codeenator/api/user/findPassword.do?user_agency=web',
        data: JSON.stringify(userInfo_value),
        contentType : 'application/json',
        dataType: 'json', 
        success: function (data) {
            if (data.result === "success") { alert('입력하신 이메일 주소로 임시 비밀번호가 전송되었습니다.') }
            else{alert(data.msg)}
        },
        error: function (err) {
            console.error("실패: ", err);

        }
    });
}