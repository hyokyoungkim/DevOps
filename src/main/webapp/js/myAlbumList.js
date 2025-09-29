let page_value = 1;
let endPage;
let sort_value = '';  //sort_option : 1-최신, 2-인기, 3-이름
let status_value = ''
let delete_value = '';

function getMyAlbumList() { 
    let urlData = `/codeenator/api/album/getMyAlbumList.do?user_agent=web&page=${page_value}`

    if (!(sort_value === '')) {
        urlData  += `&sort_option=${sort_value}`;
    }

    if (!(status_value === '')) {
        urlData  += `&filter=${status_value}`;
    }

    $.ajax({
        type: 'GET',
        url: urlData,
        dataType: 'json', 
        success: function (data) {
            console.log(data);
            createMyAlbumList(data.resultList)
        },
        error: function (err) {
            console.error("요청 실패: ", err);
            if (err.responseText) {
                console.error("응답 텍스트: ", err.responseText);
            }
        }
    });
}


function createMyAlbumList(list) {
    const albumList = document.getElementById('myContents');
    let textAlbum = '';

    list.forEach(albumList => {

{   textAlbum +=`<div class="p-2 rounded shadow-lg AlbumCard_card__group" data-value="${albumList.id}${albumList.pk}">
                                <div class="h-full cursor-pointer">
                                    <img src="/codeenator${albumList.thumbnail}" alt="thumbnail" class="h-[200px] w-[100%]">
            
                                    <div class="AlbumCard_greenTag__custom">${albumList.status}</div>
                                    <p class="AlbumCard_title__custom">${albumList.name}</p>
                                    <p class="AlbumCard_writer__custom"><span> </span></p>
                                    <div class="AlbumCard_mycardTag__custom">
                                        <div class="AlbumCard_mainTag__custom">`
                                            
    if (!(albumList.user_tag === "")) { //user_tag의 값이 빈 값이 아닐 때만 실행
        let userTagsArray = (albumList.user_tag).split(',');
        for (let i = 0; i < userTagsArray.length; i++){
            textAlbum +=
            `<div class="inline-block pl-2 pr-2 mr-1 text-xs text-blue-900 border border-blue-900 rounded-xl m2">
                <p class="inline-block break-all "># ${userTagsArray[i]}</p>
            </div>`
        }
    }                                        
            textAlbum +=`
            </div><br></div></div>`
            if(!(albumList.pk === "")){    
            textAlbum += `<img src="../image/lock.png" alt="lock"  class="absolute bottom-[10px] left-[10px] w-[30px]">`
            }
            textAlbum +=`
            <div class="AlbumCard_buttonConatiner__group">
                                    <button>
                                        <img src="../image/AddLinkFilled.png" class=" hover:scale-105" alt="urlBtn"
                                            title="url복사"  data-value=${albumList.id} id="copyBtn_${albumList.id}"></button>
                                            <button><img src="../image/InfoFilled.png"
                                            class=" hover:scale-105" alt="infoBtn"
                                            title="JSON다운로드" data-value=${albumList.id} id="jsonBtn_${albumList.id}"></button>
                                            <button>
                                                <img src="../image/EditFilled.png"class=" hover:scale-105" alt="infoBtn" title="앨범수정" data-value=${albumList.id}  id="modifyBtn_${albumList.id}">
                                                </button><button><img src="../image/DeleteFilled.png" class=" hover:scale-105" alt="deleteBtn" title="앨범 삭제" data-value=${albumList.id} id="deleteBtn_${albumList.id}"></button></div></div> `}
    })
    albumList.innerHTML = textAlbum;
}


const newBtn = document.getElementById('newList')
const popularBtn = document.getElementById('popularList')
const nameBtn = document.getElementById('nameList')

newBtn.addEventListener('click', function () {
    sort_value = 1;
    getMyAlbumList();
})
popularBtn.addEventListener('click', function () {
    sort_value = 2;
    getMyAlbumList();
})
nameBtn.addEventListener('click', function () {
    sort_value = 3;
    getMyAlbumList();
})

const allBtn = document.getElementById('allVeiw');
const cancelBtn = document.getElementById('cancelView');
const underBtn = document.getElementById('underReview');
const registerBtn = document.getElementById('registerView');

allBtn.addEventListener('click', function () {
    status_value = '';
    getMyAlbumList();
})
cancelBtn.addEventListener('click', function () {
    status_value = '등록취소';
    getMyAlbumList();
})
underBtn.addEventListener('click', function () {
    status_value = '검토중';
    getMyAlbumList();
})
registerBtn.addEventListener('click', function () {
    status_value = '등록';
    getMyAlbumList();
})


function deleteAlbum(id_value) {
    let album_id = id_value;
    $.ajax({
        type: 'POST',
        url: `/codeenator/api/album/deleteAlbum.do?user_agency=web&id=${album_id}`,
        success: function (data) {
            alert('삭제되었습니다.');
            location.reload();
        },
        error: function (err) {
            console.error("실패: ", err);
        }
    });
}

function clickIcon(e) {    

    if (e.target.id.startsWith('copyBtn')) {
        let albumID = (e.target.id).split('_');

            const tempInput = document.createElement('input');
            tempInput.value = `/codeenator/page/albumViewDetail.do?id=${albumID[1]}`;
            document.body.appendChild(tempInput);

            // 텍스트 영역의 내용 선택 및 복사
            tempInput.select();
            document.execCommand('copy');

            // 임시 텍스트 영역 제거
            document.body.removeChild(tempInput);

            // 복사 완료 알림
            alert('URL이 복사되었습니다.');
    }

    if (e.target.id.startsWith('deleteBtn')) {
        deleteAlert.className = 'rounded shadow-md p-3 m-auto border w-[500px] fixed top-[40%] left-[50%] translate-x-[-50%] bg-white';
    }

    if (e.target.id.startsWith('modifyBtn')) {
        let albumId = e.target.getAttribute('data-value');
        console.log(albumId);
        location.href=`/codeenator/page/modifyAlbum.do?id=${albumId}`;
    }

    deleteOkBtn.addEventListener('click', function () {
    let albumId = e.target.getAttribute('data-value');
    deleteAlbum(albumId);
    })
    
    deleteCancelBtn.addEventListener('click', function () {
        deleteAlert.className = 'rounded shadow-md p-3 m-auto border w-[500px] fixed top-[40%] left-[50%] translate-x-[-50%] bg-white hidden';
    })
}
const deleteAlert = document.getElementById('deleteAlert');
const deleteOkBtn = document.getElementById('deleteOkBtn');
const deleteCancelBtn = document.getElementById('deleteCancelBtn');
const myAlbumListBtn = document.getElementById('myAlbumListBtn');
const myAlbumCommentBtn = document.getElementById('myAlbumCommentBtn');
const createAlbumBtn = document.getElementById('createAlbumBtn');
const albumGroup = document.querySelectorAll('.AlbumCard_card__group');

myAlbumListBtn.addEventListener('click', function () { //왼쪽 내 앨범
    location.href=`/codeenator/page/myAlbumList.do`;
})

myAlbumCommentBtn.addEventListener('click', function () { //왼쪽 댓글 관리
    location.href=`/codeenator/page/myAlbumComment.do`;
})

createAlbumBtn.addEventListener('click', function () { //오른쪽 앨범 만들기 버튼
    location.href=`/codeenator/page/createAlbum.do`;
})

