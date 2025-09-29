let user; //0=로그아웃 1=로그인
let admin; //0로그아웃, 1로그인
let user_name;


function getUser() {
    return $.ajax({ 
        type: 'GET',
        url: '/codeenator/api/user/getUser.do?user_agency=web',
        dataType: 'json',
        success: function (data) {
            if (data.result === 'success') {
                if (data.resultMap.type === 2) { // user 로그인일 때
                    user = 1;
                    admin = 0;
                    user_name = data.resultMap.nickname;
                } else if (data.resultMap.type === 1) { // admin 로그인일 때
                    user = 1;
                    admin = 1;
                }
            } else if (data.result === 'error') {
                user = 0;
                admin = 0;
            }
            // 변수 설정 후 createUserLogin 호출
            createUserLogin(user, admin);
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

function logout() { //로그아웃
    $.ajax({
        type: 'POST',
        url: '/codeenator/api/user/logout.do?user_agency=web',
        success: function (data) {
            alert('로그아웃 되었습니다.');
            location.reload(); //새로고침
        },
        error: function (err) {
            console.error("실패: ", err);

        }
    });
}

function createUserLogin(userNumber, adminNumber) { //로그인 또는 로그아웃 상태에 따라 상단에 유저UI 변경

    const userLogin = document.getElementById('userLogin');
    const loginMenu = document.getElementById('loginMenu');
    let textLogin = '';
    let textMenu = ''
    
    if (userNumber === 0) { //로그인을 하지 않았을 때는 로그인/회원가입을 보여줌
        textLogin = `
        <ul class="flex float-right laptop:hidden">
                            <li class="m-2 text-sm hover:cursor-pointer text-slate-600">
                                <div class="cursor-pointer" onclick="location.href='/codeenator/login.do';">로그인</div>
                            </li>
                            <li class="m-2 text-sm hover:cursor-pointer text-slate-600">
                                <div class="cursor-pointer" onclick="location.href='/codeenator/join.do';">회원가입</div>
                            </li>
                        </ul>
        `
        textMenu = 
        `<div class="cursor-pointer" onclick="location.href='/codeenator/login.do';">로그인</div>`
    }


    if (userNumber === 1) { //로그인 했을 때는 아이콘을 보여주고 마우스를 가져가면 유저 메뉴를 보여줌
        textLogin = `
        <ul class="flex float-right laptop:hidden">
                            <li class="relative">
                                <img src="/codeenator/image/person.png" alt="사람 아이콘"
                                    class="float-right w-8 mt-4 text-sm hover:cursor-pointer">
                                <ul class="z-50 hidden clear-both bg-white border dropdown-menu">
                                    <li class="border-b menu-item hover:bg-slate-100">
                                        <div class="cursor-pointer" onclick="location.href='/codeenator/page/myReading.do';">내 열람</div>
                                    </li>
                                    <li class="border-b menu-item hover:bg-slate-100" onclick="location.href='/codeenator/page/myAlbumList.do';">
                                        <div class="cursor-pointer" >내 앨범</div>
                                    </li>
                                    <li class="border-b menu-item hover:bg-slate-100"  onclick="location.href='/codeenator/page/myPage_userInfo.do';">
                                        <div class="cursor-pointer">마이페이지</div>
                                    </li>`
        if (adminNumber === 1) { //관리자 모드로 로그인할 때만 생성됨
            textLogin += `<li class="border-b menu-item hover:bg-slate-100" onclick="location.href='/codeenator/page/admin_user.do';">
                            <div class="cursor-pointer">관리자모드</div>
                        </li>`
        }    
        textLogin +=`<li class="border-b menu-item hover:bg-slate-100">
                    <div class="cursor-pointer"  id="logoutBtn">로그아웃</div>
                    </li>
                    </ul>
                    </li>
                    </ul>
        `
        textMenu = 
        `<div class="cursor-pointer" id="logoutBtn">로그아웃</div>`
    }
    userLogin.innerHTML = textLogin;
    loginMenu.innerHTML = textMenu;

}


document.addEventListener('DOMContentLoaded', () => { 
    document.addEventListener('click', clickLogout);
});

function clickLogout(e) { //로그아웃 클릭했을 때
    if (e.target.id === 'logoutBtn') {
        logout();
    }

}

const openBtn = document.getElementById('openBtn');
const closeBtn = document.getElementById('closeBtn');
const menuSpace = document.getElementById('menuSpace');

openBtn.addEventListener('click', function () { //메뉴 열기 버튼 클릭 시
menuSpace.classList.remove('hidden');
});

closeBtn.addEventListener('click', function () { //메뉴 닫기 버튼 클릭 시
menuSpace.classList.add('hidden');
});