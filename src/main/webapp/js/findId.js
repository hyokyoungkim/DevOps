const userEmailInput = document.getElementById('userEmailInput');
const findIdBtn = document.getElementById('findIdBtn');

function findID(email_value) { //아이디 찾기
    $.ajax({
        type: 'POST',
        url: '/codeenator/api/user/findId.do?user_agency=web',
        data: JSON.stringify(email_value),
        contentType : 'application/json',
        dataType: 'json', 
        success: function (data) {
            if (data.result === "success") { alert('가입하신 아이디를 이메일로 전송하였습니다.') }
            else{alert(data.msg)}
        },
        error: function (err) {
            console.error("실패: ", err);

        }
    });
}

findIdBtn.addEventListener('click', function () {
    if (!(userEmailInput.value === '')) {
        let userEmail = { "email": userEmailInput.value }
        findID(userEmail);
    } else {
        alert('이메일을 입력해주세요')
    }
})