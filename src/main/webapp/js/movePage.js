document.addEventListener('DOMContentLoaded', () => {  //페이지가 로드된 후 이벤트 리스너 추가
    document.addEventListener('click', moveAlbumViewDetail); //상세페이지 이동 이벤트
    document.addEventListener('click', moveCategory);//카테고리 이동 이벤트
    document.addEventListener('click', moveMyAlbumList);
});


function moveAlbumViewDetail(e) { //main,albumList,myAlbumList
    if (!(e.target.closest('.AlbumCard_buttonConatiner__group button'))) {
        if (e.target.closest('.AlbumCard_card__group')) {  // 클릭한 요소가 클래스를 포함하고 있는지 확인
        const id_value = e.target.closest('.AlbumCard_card__group').dataset.value; // 클릭한 요소의 data 값을 가져옴
        location.href=`/codeenator/page/albumViewDetail_scroll.do?id=${id_value}`;
    }
}
}

function moveCategory(e) { //main
    if (e.target.closest('.CategoryCard_flexItem__custom')) {  // 클릭한 요소가 클래스를 포함하고 있는지 확인
    const category_value = e.target.closest('.CategoryCard_flexItem__custom').dataset.value; // 클릭한 요소의 data 값을 가져옴
    location.href=`/codeenator/page/albumList.do?${category_value}`;
}
}


function moveMyAlbumList(e) { //main , myAlbumList,albumList,albumViewDetail, createAlbum, myAlbumComment,header
    if (e.target.id === 'myAlbumList') {
        if (user === 1) {
            location.href=`/codeenator/page/myAlbumList.do`;
        } else {
            location.href=`/codeenator/login.do`;
        }
    }
}

