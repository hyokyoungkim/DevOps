let page_value = 1;
let endPage;
let albumStatus_value = [];
let albumInfoArr = {
    "name": '',
    "introduction": '',
    "category": '',
    "user_tag": '',
    "admin_tag": '',
    "thumbnail": ''
};
let album_id_value;


function getAlbumList() { //앨범리스트 가져오기
    let urlData = `/codeenator/api/admin/album/getAlbumList.do?user_agency=web&page=${page_value}`;

    $.ajax({
        type: 'GET',
        url: urlData,
        dataType: 'json', 
        success: function (data) {
            createPageBtn(data);
            endPage = data.resultMap.endPage;
            createAlbumList(data.resultList); //데이터로 리스트 생성
            albumStatus_value = data.resultList;
        },
        error: function (err) {
            console.error("요청 실패: ", err);
            if (err.responseText) {
                console.error("응답 텍스트: ", err.responseText);
            }
        }
    });
}
getAlbumList(); //초기 실행

function createPageBtn(totalPageNumber) {   //페이지를 넘길 수 있는 버튼 생성
    const pagination = document.getElementById('pagination');
    let textpage = '';
    let pageEndNumber = totalPageNumber.resultMap.endPage;

    textpage =`
        <li class="hover:cursor-pointer" value="1" id="firstBtn" style="color:gray;">‹‹</li>
        <li class="hover:cursor-pointer" id="preBtn" value=-1 style="color:gray;">‹</li>`;
        
    for (let i = 1; i <= pageEndNumber; i++) { //페이지수만큼 반복
        if (i == page_value) {
            textpage += `<li class="hover:cursor-pointer" value="${i}" style="font-weight: bold;">${i}</li>`;
        } else {
            textpage += `<li class="hover:cursor-pointer" value="${i}" style="color:gray;">${i}</li>`;
        }
    }
    textpage += `
        <li class="hover:cursor-pointer" id="nextBtn" value=+1 style="color:gray;">›</li>
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
        getAlbumList();
    }
}

function createAlbumList(list) { //앨범 리스트 생성
    const albumList = document.getElementById('albumList');
    let albumText = '';

    list.forEach(album => {
        albumText += `
        <tr role="row">
            <td role="cell" class="pr-5 pl-5 pt-2 pb-2 border-b min-w-[300px]">${album.name}</td>
            <td role="cell" class="pr-5 pl-5 pt-2 pb-2 border-b min-w-[80px]">${album.user_id}</td>
            <td role="cell" class="pr-5 pl-5 pt-2 pb-2 border-b min-w-[80px]">${album.listen_count}</td>
            <td role="cell" class="pr-5 pl-5 pt-2 pb-2 border-b min-w-[150px]">${album.datetime}</td>
            <td role="cell" class="pr-5 pl-5 pt-2 pb-2 border-b min-w-[80px]">${album.status}</td>
            <td class="pt-2 pb-2 pl-5 pr-5 border-b">
                <div class="flex gap-1">`
        if(album.status === "등록"){
        albumText += `<button class="p-2 text-white rounded Button_normalSize__custom Button_subColor__custom" id=cancelBtn-${album.id}>등록취소</button>`
        } else if (album.status === "등록취소") {
        albumText += `<button class="p-2 text-white rounded Button_normalSize__custom Button_subColor__custom" id=cancelBtn-${album.id}>등록</button>`
        }
        albumText +=            `<button class="p-2 text-white rounded Button_normalSize__custom Button_subColor__custom" id=modifyBtn-${album.id}>정보수정</button>
                    <button class="p-2 text-white rounded Button_normalSize__custom Button_subColor__custom" id=deleteBtn-${album.id}>삭제</button>
                </div>
            </td>
        </tr>
        `
    });
    albumList.innerHTML = albumText;
}

function clickBtn(e) { //버튼 클릭
    const target = e.target;

    if (target.tagName.toLowerCase() === 'button') {
        const btnID = target.id.split('-'); //-를 기준으로 배열에 넣음 ex)pw-15
        const albumSeq = btnID[1]; //ex)pw-'15' 저장

        if (target.id.includes('cancelBtn')) { //등록취소
            cancelAlbum(albumSeq)
        } else if (target.id.includes('modifyBtn')) { //정보수정
            modifyAlbumInfo(albumSeq)
        } else if (target.id.includes('deleteBtn')) { //삭제
            deleteAlbum(albumSeq)
        }
    }
}

function deleteAlbum(album_id) { //삭제버튼 클릭 - 앨범 삭제
    $.ajax({
        type: 'POST',
        url: `/codeenator/api/admin/album/deleteAlbum.do?user_agency=web&id=${album_id}`,
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


function cancelAlbum(clicked_album_id) { // 앨범 등록 취소

    const album_value = albumStatus_value.find(album => album.id === clicked_album_id);
    let albumStatus = {"status" : ""}

    if (album_value) {
        let album_text = album_value.status;
        if (album_text === "등록") {
            console.log('등록입니다.')
            albumStatus.status = '등록취소';
        } else if (album_text === "등록취소") {
            console.log('등록취소입ㄴ다.')
            albumStatus.status = '등록';
        }
    }

            $.ajax({
                type: 'POST',
                url: `/codeenator/api/admin/album/modifyStatus.do?user_agency=web&id=${clicked_album_id}`,
                data: JSON.stringify(albumStatus),
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

function modifyAlbumInfo(clicked_album_id) { 
    const albumPopup = document.getElementById('albumPopup');
    albumPopup.classList.remove('hidden'); //수정창 띄우기


    $.ajax({
        type: 'GET',
        url: `/codeenator/api/admin/album/getAlbum.do?user_agency=web&id=${clicked_album_id}`,
        dataType: 'json', 
        success: function (data) {
            if (data && data.resultMap) {
                setAlbumInfo(data.resultMap);
            }
        },
        error: function (err) {
            console.error("요청 실패: ", err);
            if (err.responseText) {
                console.error("응답 텍스트: ", err.responseText);
            }
        }
    });


    modifyBtn.addEventListener('click', function () { //수정버튼 클릭
        changeAlbumInfo(clicked_album_id)
    })

}

const albumTitleInput = document.getElementById('albumTitleInput');
const albumContentInput = document.getElementById('albumContentInput');
const categoryBtn = document.querySelectorAll('.albumCategoryBtn > div');
const albumTagInput = document.getElementById('albumTagInput');
const albumServiceTagInput = document.getElementById('albumServiceTagInput');
const modifyBtn = document.getElementById('modifyBtn');


function setAlbumInfo(albumInfo) { //기존 앨범의 데이터 불러오기
    albumInfoArr.name = albumInfo.name;
    albumInfoArr.introduction = albumInfo.introduction;
    albumInfoArr.category = albumInfo.category;
    albumInfoArr.user_tag = albumInfo.user_tag;
    albumInfoArr.admin_tag = albumInfo.admin_tag;
    albumInfoArr.thumbnail = albumInfo.thumbnail;

    albumTitleInput.value = albumInfo.name;
    albumContentInput.value = albumInfo.introduction;
    albumTagInput.value = albumInfo.user_tag;
    albumServiceTagInput.value = albumInfo.admin_tag;

    categoryBtn.forEach(category_value => {
        if (category_value.textContent.trim() === albumInfo.category) {
            category_value.classList.add('border-[#9333ea]');
        } else {
            category_value.classList.remove('border-[#9333ea]');
        }
    });

    categoryBtn.forEach((categoryDiv) => { //카테고리값 변경
    categoryDiv.addEventListener('click', function () {
        const selectedCategory = categoryDiv.textContent.trim();// 클릭된 카테고리의 텍스트 값 가져오기
        albumInfoArr.category = selectedCategory;// albumInfoArr.category 값 업데이트
        categoryBtn.forEach((div) => {
            div.classList.remove('border-[#9333ea]'); // 모든 카테고리 div에서 border-[#9333ea] 클래스를 제거
        });
        categoryDiv.classList.add('border-[#9333ea]');// 클릭된 카테고리에 border-[#9333ea] 클래스 추가
    });
    });
    
}






function changeAlbumInfo(album_id_value) { //수정된 값
    if (albumTitleInput.value !== albumInfoArr.name) {
        albumInfoArr.name = albumTitleInput.value;
    }

    if (albumContentInput.value !== albumInfoArr.introduction) {
        albumInfoArr.introduction = albumContentInput.value;
    }

    categoryBtn.forEach(category_value => {
        if (category_value.classList.contains('border-[#9333ea]')) {
            albumInfoArr.category = category_value.textContent.trim();
        }
    });

    if (albumTagInput.value !== albumInfoArr.user_tag) {
        albumInfoArr.user_tag = albumTagInput.value;
    }

    if (albumServiceTagInput.value !== albumInfoArr.admin_tag) {
        albumInfoArr.admin_tag = albumServiceTagInput.value;
    }

    console.log(albumInfoArr); 
    console.log(album_id_value); 

    
    $.ajax({
        type: 'POST',
        url: `/codeenator/api/admin/album/modifyAlbum.do?user_agency=web&id=${album_id_value}`,
        data: JSON.stringify(albumInfoArr),
        contentType: 'application/json',
        dataType: 'json',
        success: function (data) {
            console.log(data);
            if(data.result === 'success'){
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

