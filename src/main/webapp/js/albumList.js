const url = new URL(window.location.href); //url값 저장
const category_url = url.search.substring(1); //?제거
let page_value = 1;
let endPage;
let search_value = 1; //search_option 1-제목+태그(기본) 2-제목 3-태그
let sort_value = 3;  //sort_option : 1-추천순, 2-이름순, 3-최신등록순(기본)
let searchText_value = '';
let category_value = ''; //IT,자기계발,여행,경제,취미,외국어,맛집,기타




function sortCategory() {
    if (category_url !== '') { //url에 카테고리 값이 있을 경우 category_value값에 넣음
        category_value = category_url;
    }
}



function getAlbumList() { //앨범 리스트 최신순으로(기본)

    let urlData = '';
    
    urlData = `/codeenator/api/album/getAlbumList.do?user_agent=web&page=${page_value}&search_option=${search_value}&sort_option=${sort_value}`

    if (category_value !== '') { //카테고리 값이 있을 경우 urlData에 카테고리 값을 추가함
    urlData += `&category=${category_value}`;
    }

        if (searchText_value !== '') { //검색창 값이 있을 경우 urlData에 검색창 값을 추가함
    urlData += `&search_text=${searchText_value}`;
    }

    $.ajax({
        type: 'GET',
        url: urlData,
        dataType: 'json', 
        success: function (data) {
            createAlbum(data);
            createPageBtn(data);
            endPage = data.resultMap.endPage;
        },
        error: function (err) {
            console.error("요청 실패: ", err);
            if (err.responseText) {
                console.error("응답 텍스트: ", err.responseText);
            }
        }
    });
}


function createAlbum(list) { //앨범 리스트 생성
    const addList = document.getElementById('albumList');
    let textAlbum = '';
    let albumList = list.resultList;


    albumList.forEach(albumList => {
        textAlbum +=
            `<div class="p-2 border rounded shadow-lg cursor-pointer AlbumCard_card__group" data-value="${albumList.id}">
            <img src="/codeenator${albumList.thumbnail}" alt="thumbnail" class="h-[200px] w-[100%]">
                                <p class="AlbumCard_title__custom">${albumList.name}</p>
                                <p class="AlbumCard_writer__custom">${albumList.user_nickname}<span> 코디네이터</span></p>
                                <div class="AlbumCard_tag__custom">
                                    <div class="AlbumCard_mainTag__custom">`
        if (!(albumList.user_tag === "")) { //user_tag의 값이 빈 값이 아닐 때만 실행
            let userTagsArray = (albumList.user_tag).split(',');
            for (let i = 0; i < userTagsArray.length; i++){
                textAlbum +=
                                        `<div
                                            class="inline-block pl-2 pr-2 mr-1 text-xs text-blue-900 border border-blue-900 rounded-xl m2">
                                            <p class="inline-block break-all "># ${userTagsArray[i]}</p>
                                        </div>`
                                        }
        }

        textAlbum += `</div><br>
                                    <div
                                        class="rounded-xl border border-gray-400 inline-block pl-2 pr-2 pt-[1px] text-xs mr-1 text-gray-400">
                                        🙍‍♀️ ${albumList.listen_count*1 <= 10 ? '10명 이하' : albumList.listen_count*1 <= 50 ? '50명 이하' : albumList.listen_count*1 <= 100 ? '100명 이하' : "500명 이하"}</div>
                                    <div
                                        class="rounded-xl border border-gray-400 inline-block pl-2 pr-2 pt-[1px] text-xs mr-1 text-gray-400">
                                        💜 ${albumList.recommend_count}</div>
                                </div>
                            </div>`

    })
        addList.innerHTML = textAlbum;
}


const searchSelect = document.getElementById('searchSelect');
searchSelect.addEventListener('change', function () { //제목 유형 변경
    search_value = searchSelect.value;
    getAlbumList();
})

const searchText = document.getElementById('searchText'); //text박스 검색(엔터 등 input박스에서 포커스가 사라지면 검색함)
searchText.addEventListener('change', function () {
    searchText_value = searchText.value;
    getAlbumList();
})

const searchTextImg = document.getElementById('searchTextImg'); //돋보기 이미지를 클릭 시 text박스의 값 검색
searchTextImg.addEventListener('click', function () {
    searchText_value = searchText.value;
    getAlbumList();
})



const category_mobile = document.getElementById('category_mobile')
category_mobile.addEventListener('change', function () { //pc가 아닐 때 : category 선택
    page_value = 1;
    category_value = category_mobile.value;
    getAlbumList();
})

const category_pc = document.querySelectorAll('#category_pc li'); //pc일 때 :cqtegory 선택
category_pc.forEach(category_select => {
    category_select.addEventListener('click', function () {
                page_value = 1;
                category_value = this.getAttribute('value');
                getAlbumList();
            });
});


function createPageBtn(totalPageNumber) {   //페이지를 넘길 수 있는 버튼 생성
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
        <li class="hover:cursor-pointer" value=${pageEndNumber} style="color:gray;" id="lastBtn">››</li>`;

    pagination.innerHTML = textpage;
}


function clickPagination(e) { //생성된 페이지 버튼 클릭 이벤트
    if (e.target.tagName === 'LI' && e.target.closest('ul').id === 'pagination') {  //클릭된 요소가 id가 pagination인 ul 요소 내의 LI 태그인지 확인
        if (e.target.id === 'firstBtn') {  //처음으로 페이지로 이동
            page_value = 1;// 첫 페이지 값으로 설정
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

        window.scrollTo({ top: 0 }); //페이지 맨 위로 이동
        getAlbumList();
    }
}
