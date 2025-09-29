let page_value = 1;
let id_value =''

                                
function getMyReadingList() { 
    let url = `/codeenator/api/album/getListenAlbumList.do?user_agent=web&page=${page_value}`
    $.ajax({
        type: 'GET',
        url: url,
        dataType: 'json', 
        success: function (data) {
            createMyAlbumList(data.resultList)
            createPageBtn(data)
        },
        error: function (err) {
            console.error("요청 실패: ", err);
            if (err.responseText) {
                console.error("응답 텍스트: ", err.responseText);
            }
        }
    });
}
getMyReadingList();

function createMyAlbumList(list) {
    const readingList = document.getElementById('readingList');
    console.log(list)
    let textList = '';

    list.forEach(readingList => {
        textList += `
        <tr role="row">
            <td role="cell" class="pr-5 pl-5 pt-2 pb-2 border-b min-w-[150px]">${readingList.name}</td>
            <td role="cell" class="pr-5 pl-5 pt-2 pb-2 border-b min-w-[150px]">${readingList.first_datetime}</td>
            <td role="cell" class="pr-5 pl-5 pt-2 pb-2 border-b min-w-[150px]">${readingList.recent_datetime}</td>
            <td role="cell" class="pr-5 pl-5 pt-2 pb-2 border-b min-w-[150px]">${readingList.listen_count}</td>
            <td class="pt-2 pb-2 pl-5 pr-5 border-b">
                <button class="p-2 text-white rounded Button_normalSize__custom Button_subColor__custom" id=${readingList.album}>열람 이력 삭제</button>
            </td>
        </tr>
        `
    });
    readingList.innerHTML = textList;
}

function createPageBtn(totalPageNumber) { //페이지를 넘길 수 있는 버튼 생성
    const pagination = document.getElementById('pagination');
    let textpage = '';
    let pageEndNumber = totalPageNumber.resultMap.endPage;

    textpage =`
        <li class="hover:cursor-pointer" value="1" style="color:gray;" id="firstBtn">‹‹</li>
        <li class="hover:cursor-pointer" id="preBtn" style="color:gray;" value=-1 >‹</li>`
        
    for (let i = 1; i <= pageEndNumber; i++) {         // 현재 페이지를 bold로 표시
        if (i == page_value) {
            textpage += `<li class="hover:cursor-pointer" value="${i}" style="font-weight: bold;">${i}</li>`;
        } else {
            textpage += `<li class="hover:cursor-pointer" style="color:gray;" value="${i}">${i}</li>`;
        }
    }

                                    
        textpage +=`
        <li class="hover:cursor-pointer" id="nextBtn" style="color:gray;" value=+1>›</li>
        <li class="hover:cursor-pointer" value=${pageEndNumber} style="color:gray;" id="lastBtn">››</li>
        `

    pagination.innerHTML = textpage;
}


function clickPagination(e) { //생성된 페이지 버튼 클릭 이벤트
    console.log(e.target)
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
        getMyReadingList();
        
    }
}

function deleteReadingList(id_value) { 
    $.ajax({
        type: 'POST',
        url: `/codeenator/api/album/deleteListenAlbum.do?user_agent=web&id=${id_value}`,
        contentType : 'application/json',
        dataType: 'json', 
        success: function (data) {
            alert('내역이 삭제되었습니다.')
            location.reload(); //새로고침
        },
        error: function (err) {
            console.error("요청 실패: ", err);
            if (err.responseText) {
                console.error("응답 텍스트: ", err.responseText);
            }
        }
    });
}

function clickDeleteBtn(e) { //열람 이력 삭제 버튼 클릭
    if (e.target.tagName === 'BUTTON') { //클릭한 태그 이름이 button인지 확인
        id_value = e.target.id; //클릭한 타겟의 아이디를 id_value에 넣기
        deleteReadingList(id_value) //id값 전달
    }
}