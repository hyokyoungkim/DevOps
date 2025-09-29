let page_value = 1;
let endPage;
let sort_value = 1;  //sort_option : 1-접속순, 2-이름순, 3-앨범수순
let searchText_value = ''; //검색어
const searchSelect = document.getElementById('searchSelect');
const userCount = document.getElementById('userCount');
let userSeq_value = '';
let emailText = { "title": "", "content": ""}
let userComment_value =[];

function getUserList() {
    
    let urlData = `/codeenator/api/admin/user/getUserList.do?user_agency=web&sort_option=${sort_value}`;

    if (!(searchText_value === '')) {
        urlData += `&search_text=${searchText_value}`
    }

    $.ajax({
        type: 'GET',
        url: urlData,
        dataType: 'json', 
        success: function (data) {
            createPageBtn(data);
            userCount.innerText = `총 회원 ${data.resultMap.count}명`;
            createUserList(data.resultList);
            endPage = data.resultMap.endPage;
            userComment_value = data.resultList;
        },
        error: function (err) {
            console.error("요청 실패: ", err);
            if (err.responseText) {
                console.error("응답 텍스트: ", err.responseText);
            }
        }
    });
}
getUserList();

function createUserList(user) {
    const userList = document.getElementById('userList');
    let userText = '';

    user.forEach(list => {
        userText +=`
        <tr role="row">
            <td role="cell" class="pr-5 pl-5 pt-2 pb-2 border-b min-w-[130px]">${list.nickname}</td>
            <td role="cell" class="pr-5 pl-5 pt-2 pb-2 border-b min-w-[130px]">${list.id}</td>
            <td role="cell" class="pr-5 pl-5 pt-2 pb-2 border-b min-w-[130px]">${list.datetime}</td>
            <td role="cell" class="pr-5 pl-5 pt-2 pb-2 border-b min-w-[80px]">${list.login_count}</td>
            <td role="cell" class="pr-5 pl-5 pt-2 pb-2 border-b min-w-[80px]">${list.listen_count}</td>
            <td role="cell" class="pr-5 pl-5 pt-2 pb-2 border-b min-w-[80px]">${list.album_count}</td>
            <td role="cell" class="pr-5 pl-5 pt-2 pb-2 border-b min-w-[130px]">`
        if(!(list.comment === '' || list.comment === null)){
            userText +=`<button class="rounded p-2 text-white  Button_normalSize__custom Button_subColor__custom" id=logBtn-${list.seq}>로그확인</button>`
        } else{
            userText +=`<button class="p-2 text-white rounded Button_normalSize__custom Button_grayColor" id=logBtn-${list.seq}>로그입력</button>`
            }
            userText +=`</td>
            <td class="pt-2 pb-2 pl-5 pr-5 border-b ">
            <div class="flex gap-1">
                <button class="p-2 text-white rounded Button_normalSize__custom Button_subColor__custom" id="pwBtn-${list.seq}">비번초기화</button>
                <button class="p-2 text-white rounded Button_normalSize__custom Button_subColor__custom" id="emailBtn-${list.seq}">이메일</button>
                <button class="p-2 text-white rounded Button_normalSize__custom Button_subColor__custom" id="deleteBtn-${list.seq}">삭제</button>
            </div>
            </td>
        </tr>
        `
    });
    userList.innerHTML = userText;
}

function createPageBtn(totalPageNumber) { //페이지를 넘길 수 있는 버튼 생성
    const pagination = document.getElementById('pagination');
    let textpage = '';
    let pageEndNumber = totalPageNumber.resultMap.endPage;

    textpage =`
        <li class="hover:cursor-pointer" value="1" id="firstBtn" style="color:gray;">‹‹</li>
        <li class="hover:cursor-pointer" id="preBtn" style="color:gray;" value=-1>‹</li>`;
        
    for (let i = 1; i <= pageEndNumber; i++) {         // 현재 페이지를 bold로 표시
        if (i == page_value) {
            textpage += `<li class="hover:cursor-pointer" value="${i}" style="font-weight: bold;">${i}</li>`;
        } else {
            textpage += `<li class="hover:cursor-pointer" style="color:gray;" value="${i}">${i}</li>`;
        }
    }
                                    
    textpage += `
        <li class="hover:cursor-pointer" id="nextBtn" style="color:gray;" value=+1>›</li>
        <li class="hover:cursor-pointer" value=${pageEndNumber} id="lastBtn" style="color:gray;">››</li>`;

    pagination.innerHTML = textpage;
}


function clickPagination(e) { //생성된 페이지 버튼 클릭 이벤트
    if (e.target.tagName === 'LI' && e.target.closest('ul').id === 'pagination') { //클릭된 요소가 id가 pagination인 ul 요소 내의 LI 태그인지 확인
        if (e.target.id === 'firstBtn') { //처음으로 페이지로 이동
            page_value = 1; // 첫 페이지 값으로 설정
        } else if (e.target.id === 'lastBtn') { //마지막 페이지로 이동
            page_value = e.target.getAttribute('value'); // 마지막 페이지 값으로 설정
        } else if (e.target.id === 'preBtn') { //이전 버튼
            if (page_value > 1) { 
                page_value--;
            }
        } else if (e.target.id === 'nextBtn') { //다음 버튼
            if (page_value < endPage) { 
                page_value++;
            }
        } else {
            page_value = e.target.getAttribute('value');
        }

        window.scrollTo({ //페이지 맨 위로 이동
                top: 0
            });
        getUserList();
    }
}


function clickAlign(e) { //정렬

    if (e.target.id === 'accessSeq') {
        console.log('접속순')
        sort_value = 1;
    }
    if (e.target.id === 'nameSeq') {
        console.log('이름순')
        sort_value = 2;
    }
    if (e.target.id === 'albumCountSeq') {
        console.log('앨범수 순')
        sort_value = 3;
    }
    getUserList();
}


searchSelect.addEventListener('change', function () { //제목 유형 변경
    searchText_value = searchSelect.value;
    getUserList();
})

const searchText = document.getElementById('searchText'); //text박스 검색(엔터 등 input박스에서 포커스가 사라지면 검색함)
searchText.addEventListener('change', function () {
    searchText_value = searchText.value;
    getUserList();
})

const searchTextImg = document.getElementById('searchTextImg'); //돋보기 이미지를 클릭 시 text박스의 값 검색

searchTextImg.addEventListener('click', function () {
    searchText_value = searchText.value;
    getUserList();
})

function clickBtn(e) { //버튼 클릭
    const target = e.target;

    if (target.tagName.toLowerCase() === 'button') {
        const btnID = target.id.split('-'); //-를 기준으로 배열에 넣음 ex)pw-15
        const userSeq = btnID[1]; //ex)pw-'15' 저장

        if (target.id.includes('pwBtn')) { // 비밀번호 초기화
            resetPw(userSeq);
        } else if (target.id.includes('emailBtn')) { // 이메일
            sendEmail(userSeq);
        } else if (target.id.includes('deleteBtn')) { // 삭제
            deleteUser(userSeq);
        } else if (target.id.includes('logBtn')) { // 로그
            setComment(userSeq);
        }
    }
}

function resetPw(user_seq) { //비밀번호 초기화
    $.ajax({
        type: 'POST',
        url: `/codeenator/api/admin/user/initPassword.do?user_agency=web&seq=${user_seq}&enable_email=true`,
        contentType : 'application/json',
        dataType: 'json', 
        success: function (data) {
            console.log(data)
            alert(data.msg);
        },
        error: function (err) {
            console.error("실패: ", err);

        }
    });
}

function sendEmail(user_seq) { //이메일
    const emailPopup = document.getElementById('emailPopup');
    const emailCancleBtn = document.getElementById('emailCancleBtn');
    const emailCheckBtn = document.getElementById('emailCheckBtn');
    const emailTitle = document.getElementById('emailTitle');
    const emailContent = document.getElementById('emailContent');
    
    emailPopup.classList.remove('hidden');
    
    function clickEmailBtn(e) {
        if (e.target.id === 'emailCancleBtn') {
            emailPopup.classList.add('hidden');
        } else if (e.target.id === 'emailCheckBtn') {
            let emailText = {
                "title": emailTitle.value,
                "content": emailContent.value
            };
            $.ajax({
                type: 'POST',
                url: `/codeenator/api/admin/user/sendEmail.do?user_agency=web&seq=${user_seq}`,
                data: JSON.stringify(emailText),
                contentType: 'application/json',
                dataType: 'json',
                success: function (data) {
                    alert(data.msg);
                    location.reload(); // 페이지 새로고침
                },
                error: function (err) {
                    console.error("실패: ", err);
                }
            });
        }
    }

    emailCancleBtn.addEventListener('click', clickEmailBtn);
    emailCheckBtn.addEventListener('click', clickEmailBtn);
}

function deleteUser(user_seq) { //삭제

    $.ajax({
        type: 'POST',
        url: `/codeenator/api/admin/user/deleteUser.do?user_agency=web&seq=${user_seq}`,
        contentType : 'application/json',
        dataType: 'json', 
        success: function (data) {
            if (data.result === 'success') {
                alert(data.msg); //성공 메세지 출력
                location.reload(); //페이지 새로고침
            } else {
                alert(data.msg);
            }
        },
        error: function (err) {
            console.error("실패: ", err);

        }
    });
}

function setComment(user_seq) { //로그

    const logPopup = document.getElementById('logPopup');
    const logContent = document.getElementById('logContent');
    const logCancelBtn = document.getElementById('logCancelBtn');
    const logCheckBtn = document.getElementById('logCheckBtn');

    logPopup.classList.remove('hidden'); //팝업창 표시

let userObj = userComment_value.find(item => item.seq === Number(user_seq));
    
    if (userObj) {
        logContent.value = userObj.comment || ''; // comment가 null 또는 undefined일 경우 빈 문자열로 설정
    } else {
        logContent.value = ''; // 객체를 찾지 못한 경우 빈 문자열로 설정
    }


    function clickLogBtn(e) {
        if (e.target.id === 'logCancelBtn') { //취소버튼
            logPopup.classList.add('hidden'); //팝업창 숨김
        } else if (e.target.id === 'logCheckBtn') { 
            let logText = {
                "comment" : logContent.value
            };
            $.ajax({
                type: 'POST',
                url: `/codeenator/api/admin/user/setComment.do?user_agency=web&seq=${user_seq}`,
                data: JSON.stringify(logText),
                contentType : 'application/json',
                dataType: 'json', 
                success: function (data) {
                    if (data.result === 'success') {
                        alert(data.msg);
                        location.reload(); // 페이지 새로고침
                    } else {
                        alert(data.msg);
                    }
                },
                error: function (err) {
                    console.error("실패: ", err);
                }
            });
        }
    }

    logCancelBtn.addEventListener('click', clickLogBtn);
    logCheckBtn.addEventListener('click', clickLogBtn);
}

