const albumCount = document.getElementById('albumCount');
const boardCount = document.getElementById('boardCount');
let page_value = 1;
let endPage;
let boardEnable_value =[];

function getBoardList() {
    
    let urlData = `/codeenator/api/admin/board/getContentList.do?user_agency=web&page=${page_value}`;

    $.ajax({
        type: 'GET',
        url: urlData,
        dataType: 'json', 
        success: function (data) {
            createPageBtn(data);
            boardCount.innerText = `총 게시글 ${data.resultMap.count}개`;
            createBoard(data.resultList);
            boardEnable_value = data.resultList;
        },
        error: function (err) {
            console.error("요청 실패: ", err);
            if (err.responseText) {
                console.error("응답 텍스트: ", err.responseText);
            }
        }
    });
}
getBoardList();

function createPageBtn(totalPageNumber) { //페이지를 넘길 수 있는 버튼 생성
    const pagination = document.getElementById('pagination');
    let textpage = '';
    let pageEndNumber = totalPageNumber.resultMap.endPage;

    textpage =`
        <li class="hover:cursor-pointer" value="1" id="firstBtn" style="color:gray;">‹‹</li>
        <li class="hover:cursor-pointer" id="preBtn" style="color:gray;" value=-1 >‹</li>`;
        
    for (let i = 1; i <= pageEndNumber; i++) {

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
        getBoardList();
    }
}

function createBoard(list) {
    const boardList = document.getElementById('boardList');
    let boardText = '';

    list.forEach(boardList => {
            console.log(boardList)
        boardText += `
        <tr role="row">
            <td role="cell" class="pr-5 pl-5 pt-2 pb-2 border-b min-w-[300px]">${boardList.title}</td>
            <td role="cell" class="pr-5 pl-5 pt-2 pb-2 border-b min-w-[100px]">${boardList.board_name}</td>
            <td role="cell" class="pr-5 pl-5 pt-2 pb-2 border-b min-w-[100px]">${boardList.datetime}</td>
            <td role="cell" class="pr-5 pl-5 pt-2 pb-2 border-b min-w-[100px]">${boardList.view_count}</td>
            <td class="pr-5 pl-5 pt-2 pb-2 border-b w-[150px]">
                <div class="flex justify-center gap-1">`
        if(boardList.is_secret === false){
        boardText += ` <button class="p-2 text-white rounded Button_normalSize__custom Button_subColor__custom" id="enableBtn-${boardList.seq}">비활성화</button>`
        } else if(boardList.is_secret === true) {
        boardText += ` <button class="p-2 text-white rounded Button_normalSize__custom Button_subColor__custom" id="enableBtn-${boardList.seq}">활성화</button>`
        }
            boardText +=`<button class="p-2 text-white rounded Button_normalSize__custom Button_subColor__custom" id=deleteBtn-${boardList.seq}>삭제</button>
                </div>
            </td>
        </tr>
        `
    });
    boardList.innerHTML = boardText;
}

function clickBtn(e) { //버튼 클릭
    const target = e.target;

    if (target.tagName.toLowerCase() === 'button') {
        const btnID = target.id.split('-'); //-를 기준으로 배열에 넣음 ex)pw-15
        const boardSeq = btnID[1]; //ex)pw-'15' 저장

        if (target.id.includes('enableBtn')) { //비활성화
            enableBoard(boardSeq)
        } else if (target.id.includes('deleteBtn')) { //삭제
            deleteBoard(boardSeq)
        }
    }
}

function enableBoard(board_number) { // 게시글

    const enablePopup = document.getElementById('enablePopup');
    const enableCancelBtn = document.getElementById('enableCancelBtn');
    const enableCheckBtn = document.getElementById('enableCheckBtn');

    // 팝업창 표시
    enablePopup.classList.remove('hidden'); 
    
    function clickEnableBtn(e) {
        // 취소 버튼 클릭 시 팝업창 숨김
        if (e.target.id === 'enableCancelBtn') {
            enablePopup.classList.add('hidden');
        } 
        // 확인 버튼 클릭 시 처리
        else if (e.target.id === 'enableCheckBtn') {
            const targetButton = document.getElementById(`enableBtn-${board_number}`);
            let enable_value = {"enable": ""};

            // 현재 버튼 텍스트 값에 따라 enable_value를 설정하고 버튼 텍스트 변경
            if (targetButton.textContent === '비활성화') {
                enable_value.enable = false;
                targetButton.textContent = '활성화';
            } else {
                enable_value.enable = true;
                targetButton.textContent = '비활성화';
            }

            // AJAX 요청 보내기
            $.ajax({
                type: 'POST',
                url: `/codeenator/api/admin/board/enableViewContent.do?user_agency=web&seq=${board_number}`,
                data: JSON.stringify(enable_value),
                contentType: 'application/json',
                dataType: 'json',
                success: function (data) {
                    console.log(data);
                    alert(data.msg);
                    location.reload();
                },
                error: function (err) {
                    console.error("실패: ", err);
                }
            });

            // 팝업창 숨김
            enablePopup.classList.add('hidden');
        }
    }

    // 이벤트 리스너 추가
    enableCancelBtn.addEventListener('click', clickEnableBtn);
    enableCheckBtn.addEventListener('click', clickEnableBtn);
}


function deleteBoard(board_number) {

    $.ajax({
    type: 'POST',
    url: `/codeenator/api/admin/board/deleteContent.do?user_agency=web&seq=${board_number}`,
    contentType: 'application/json',
    dataType: 'json',
    success: function (data) {
        console.log(data);
        alert(data.msg);
        location.reload();
    },
    error: function (err) {
        console.error("실패: ", err);
    }
    });
}