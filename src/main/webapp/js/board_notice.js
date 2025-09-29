let page_value = 1;
let search_value = 1; //search_option 1-제목+태그(기본) 2-제목 3-태그
let searchText_value = '';
const addNoticeBtn = document.getElementById('addNoticeBtn')
function getNoticeList() { //공지사항 리스트 가져오기

    let url = `/codeenator/api/board/getNoticeList.do?user_agent=web&page=${page_value}&search_option=${search_value}`
    
    if (searchText_value !== '') { //검색창 값이 있을 경우 urlData에 검색창 값을 추가함
    url += `&search_text=${searchText_value}`;
    }

    $.ajax({
        type: 'GET',
        url: url,
        dataType: 'json', 
        success: function (data) {
            creteNoticeList(data)
            createPageBtn(data);
        },
        error: function (err) {
            console.error("요청 실패: ", err);
            if (err.responseText) {
                console.error("응답 텍스트: ", err.responseText);
            }
        }
    });
}
getNoticeList();

function creteNoticeList(list) {
    let noticeList = list.resultList;
    let textList = '';
    let listNum = list.resultMap;
    const listGroup = document.getElementById('listGroup');
    const totalNotice = document.getElementById('totalNotice');
    totalNotice.innerHTML = `총 게시물 ${list.resultMap.count}개`

    for (let i = 0; i < noticeList.length; i++){
        textList +=
        `
    <tr role="row">
        <td role="cell" class="pt-2 pb-2 border-b desktop:min-w-[50px] desktop:pr-5 desktop:pl-5 tablet:desktop:min-w-[50px] tablet:pl-5 tablet:pr-5">${listNum.currentPage ===1?listNum.count-i:listNum.count-((listNum.currentPage-1)*10)-i}</td>
        <td role="cell" class="pt-2 pb-2 border-b desktop:min-w-[300px] desktop:pr-5 desktop:pl-5 tablet:max-w-[220px] tablet:pl-5 tablet:pr-5 cursor-pointer" data-id="${noticeList[i].seq}">${noticeList[i].title}</td>
        <td role="cell" class="pt-2 pb-2 border-b desktop:min-w-[50px] desktop:pr-5 desktop:pl-5 tablet:hidden">${noticeList[i].datetime}</td>
        <td role="cell" class="pt-2 pb-2 border-b desktop:min-w-[50px] desktop:pr-5 desktop:pl-5 tablet:desktop:min-w-[50px] tablet:pl-5 tablet:pr-5">${noticeList[i].nickname}</td>
        <td role="cell" class="pt-2 pb-2 border-b desktop:min-w-[50px] desktop:pr-5 desktop:pl-5 tablet:hidden">${noticeList[i].view_count}</td>
    </tr>
        `
    }
    listGroup.innerHTML = textList;
}





function createPageBtn(totalPageNumber) { //페이지를 넘길 수 있는 버튼 생성
    const pagination = document.getElementById('pagination');
    let textpage = '';
    let pageEndNumber = totalPageNumber.resultMap.endPage;

    textpage =`
        <li class="hover:cursor-pointer" value="1" id="firstBtn" style="color: gray;">‹‹</li>
        <li class="hover:cursor-pointer" id="preBtn" value=-1 style="color: gray;">‹</li>`;
        
    for (let i = 1; i <= pageEndNumber; i++) {         // 현재 페이지를 bold로 표시
        if (i == page_value) {
            textpage += `<li class="hover:cursor-pointer" value="${i}" style="font-weight: bold;">${i}</li>`;
        } else {
            textpage += `<li class="hover:cursor-pointer" value="${i}" style="color: gray;">${i}</li>`;
        }
    }
                                    
    textpage += `
        <li class="hover:cursor-pointer" id="nextBtn" value=+1 style="color: gray;">›</li>
        <li class="hover:cursor-pointer" value=${pageEndNumber} id="lastBtn" style="color: gray;">››</li>`;

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
        console.log(page_value)
        getNoticeList();
        
    }
}

const searchSelect = document.getElementById('searchSelect');
searchSelect.addEventListener('change', function () { //제목 유형 변경
    search_value = searchSelect.value;
    getNoticeList();
})

const searchText = document.getElementById('searchText'); //text박스 검색(엔터 등 input박스에서 포커스가 사라지면 검색함)
searchText.addEventListener('change', function () {
    searchText_value = searchText.value;
    getNoticeList();
})

const searchTextImg = document.getElementById('searchTextImg'); //돋보기 이미지를 클릭 시 text박스의 값 검색
searchTextImg.addEventListener('click', function () {
    searchText_value = searchText.value;
    getNoticeList();
})



addNoticeBtn.addEventListener('click', function () {
    console.log(user);
    if (admin === 1) {
        location.href = '/codeenator/page/board_notice_create.do';
    } else {
        alert('로그인 후 이용할 수 있습니다.')
    }
})


getUser().done(function () {     // Ajax 요청이 완료된 후 바로 실행
    if (admin === 1  && user ==! 0) {
        addNoticeBtn.classList.remove('hidden');
    } 
});

document.addEventListener('DOMContentLoaded', () => {

            document.addEventListener('click', clickPagination);

            const listGroup = document.getElementById('listGroup');
            listGroup.addEventListener('click', function (e) {
                const target = e.target;
                if (target.tagName === 'TD' && target.hasAttribute('data-id')) { // 클릭된 요소가 td이고 data-id 속성이 존재하는 경우
                    let id_value = target.getAttribute('data-id');
                    location.href = `/codeenator/page/board_notice_view.do?seq=${id_value}`;
                }
            });
    
    const tabletBtn = document.getElementById('tabletBtn');
    

tabletBtn.addEventListener('change', function () {
            handleSelectChange(tabletBtn.value);
        });
        
function handleSelectChange(selectedValue) {
            if (selectedValue === 'noticeBtn') {
                if (window.location.href.indexOf('board_notice.do') === -1) {
                    location.href = '/codeenator/page/board_notice.do';
                }
            } else if (selectedValue === 'qnaBtn') {
                if (window.location.href.indexOf('board_qna.do') === -1) {
                    location.href = '/codeenator/page/board_qna.do';
                }
            }
        }
})

