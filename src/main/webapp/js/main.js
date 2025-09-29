function getBestAlbumList() { //베스트 앨범 구경하기
    $.ajax({
        type: 'GET',
        url: '/codeenator/api/portal/getAlbumList.do?user_agency=web',
        dataType: 'json', 
        success: function (data) {
            creteBestAlbum(data); //성공 시 앨범 만들기
        },
        error: function (err) {
            console.error("요청 실패: ", err);
            if (err.responseText) {
                console.error("응답 텍스트: ", err.responseText);
            }
        }
    });
}

function creteBestAlbum(list) { //베스트 앨범 생성
    const bestAlbum = document.getElementById('bestAlbum');
    let albumList;

    if (list.resultList.length === 10) {
        albumList = (list.resultList).slice(0, -2); //8개만 보여지도록 list가 총 10개면 뒤에서 2개 삭제
    } else if (list.resultList.length === 9) {
        albumList = (list.resultList).slice(0, -1); //8개만 보여지도록 list가 총 9개면 뒤에서 1개 삭제
    } else {
        albumList = list.resultList; //해당되지 않으면 모두 보여주기
    }


    let textBestAlbum = ''
    
    albumList.forEach(albumList => { //@@img 경로 수정 필요
        textBestAlbum +=
            `<div class="p-2 border rounded shadow-lg cursor-pointer AlbumCard_card__group" data-value="${albumList.id}">
            <img src="/codeenator/${albumList.thumbnail}" alt="thumbnail" class="h-[200px] w-[100%]">
                                <p class="AlbumCard_title__custom">${albumList.name}</p>
                                <p class="AlbumCard_writer__custom">${albumList.user_nickname}<span> 코디네이터</span></p>
                                <div class="AlbumCard_tag__custom">
                                    <div class="AlbumCard_mainTag__custom">`
        if (!(albumList.user_tag === "")) { //user_tag의 값이 빈 값이 아닐 때만 실행
            let userTagsArray = (albumList.user_tag).split(','); //태그를 , 기준으로 나눈 후 배열에 넣음
            for (let i = 0; i < userTagsArray.length; i++){ //배열 길이 만큼 반복
                textBestAlbum +=
                                        `<div
                                            class="inline-block pl-2 pr-2 mr-1 text-xs text-blue-900 border border-blue-900 rounded-xl m2">
                                            <p class="inline-block break-all "># ${userTagsArray[i]}</p>
                                        </div>`
                                        }
        }

        textBestAlbum += `</div><br>
                                    <div
                                        class="rounded-xl border border-gray-400 inline-block pl-2 pr-2 pt-[1px] text-xs mr-1 text-gray-400">
                                        🙍‍♀️ ${albumList.listen_count*1 <= 10 ? '10명 이하' : albumList.listen_count*1 <= 50 ? '50명 이하' : albumList.listen_count*1 <= 100 ? '100명 이하' : "500명 이하"}</div>
                                    <div
                                        class="rounded-xl border border-gray-400 inline-block pl-2 pr-2 pt-[1px] text-xs mr-1 text-gray-400">
                                        💜 ${albumList.recommend_count}</div>
                                </div>
                            </div>`
    });
    bestAlbum.innerHTML = textBestAlbum;
}

function getNewAlbumList() { //신규 앨범 구경하기
    $.ajax({
        type: 'GET',
        url: '/codeenator/api/portal/getNewAlbumList.do?user_agency=web',
        dataType: 'json', 
        success: function (data) {
            creteNewAlbum(data);//성공 시 앨범 만들기
        },
        error: function (err) {
            console.error("요청 실패: ", err);
            if (err.responseText) {
                console.error("응답 텍스트: ", err.responseText);
            }
        }
    });
}

function creteNewAlbum(list) { //신규 앨범 생성
    const newAlbum = document.getElementById('newAlbum');
    let albumList = list.resultList;
    let textNewAlbum = '';
    albumList.forEach(albumList => { //@@img 경로 수정하기
        textNewAlbum +=
            `<div class="p-2 border rounded shadow-lg cursor-pointer AlbumCard_card__group" data-value="${albumList.id}">
            <img src=/codeenator/${albumList.thumbnail} alt="thumbnail" class="h-[200px] w-[100%]">
                                <p class="AlbumCard_title__custom">${albumList.name}</p>
                                <p class="AlbumCard_writer__custom">${albumList.user_nickname}<span> 코디네이터</span></p>
                                <div class="AlbumCard_tag__custom">
                                    <div class="AlbumCard_mainTag__custom">`
        if (!(albumList.user_tag === "")) { //user_tag의 값이 빈 값이 아닐 때만 실행
            let userTagsArray = (albumList.user_tag).split(',');
            for (let i = 0; i < userTagsArray.length; i++){
                textNewAlbum +=
                                        `<div
                                            class="inline-block pl-2 pr-2 mr-1 text-xs text-blue-900 border border-blue-900 rounded-xl m2">
                                            <p class="inline-block break-all "># ${userTagsArray[i]}</p>
                                        </div>`
                                        }
        }

        textNewAlbum += `</div><br>
                                    <div
                                        class="rounded-xl border border-gray-400 inline-block pl-2 pr-2 pt-[1px] text-xs mr-1 text-gray-400">
                                        🙍‍♀️ ${albumList.listen_count*1 <= 10 ? '10명 이하' : albumList.listen_count*1 <= 50 ? '50명 이하' : albumList.listen_count*1 <= 100 ? '100명 이하' : "500명 이하"}</div>
                                    <div
                                        class="rounded-xl border border-gray-400 inline-block pl-2 pr-2 pt-[1px] text-xs mr-1 text-gray-400">
                                        💜 ${albumList.recommend_count}</div>
                                </div>
                            </div>`
    });
    newAlbum.innerHTML = textNewAlbum;
}

function getAlbumCount() { //앨범 총 갯수 및 조회수
        $.ajax({
        type: 'GET',
        url: '/codeenator/api/portal/getAlbumCount.do?user_agency=web',
        dataType: 'json', 
        success: function (data) {
            creteAlbumCount(data); //성공 시 앨범 총 갯수 및 조회수 생성
        },
        error: function (err) {
            console.error("요청 실패: ", err);
            if (err.responseText) {
                console.error("응답 텍스트: ", err.responseText);
            }
        }
    });
}

function creteAlbumCount(value) { //앨범 총 갯수 및 조회수 생성
    const albumCount = document.getElementById('albumCount');
    let albumListNumber = value.resultMap;
    let textCount = ''
    
    if(!(albumListNumber.album_count === null)){ //앨범의 개수가 0개가 아니라면 
    textCount +=`<p><span>${albumListNumber.album_count}개</span>의 앨범이 생성되었습니다.</p>`
    } else {
    textCount +=`<p><span>0개</span>의 앨범이 생성되었습니다.</p>`  
    }

    if (!(albumListNumber.listen_count === null)) { //오늘 컨텐츠 조회수가 0번이 아니라면
    textCount += `<p>컨텐츠가 오늘 <span> ${albumListNumber.listen_count}번</span>조회되었습니다.</p>`
    } else {
    textCount +=`<p>컨텐츠가 오늘 <span> 0번</span>조회되었습니다.</p>`  
    }
    albumCount.innerHTML = textCount;              
}

function getAlbumCommentList() { //후기 모음
    $.ajax({
        type: 'GET',
        url: '/codeenator/api/portal/getAlbumCommentList.do?user_agency=web',
        dataType: 'json', 
        success: function (data) {
            creteReview(data);//성공 시 후기 모음 생성
        },
        error: function (err) {
            console.error("요청 실패: ", err);
            if (err.responseText) {
                console.error("응답 텍스트: ", err.responseText);
            }
        }
    });
}

function creteReview(list) { //후기 모음 생성
    const commentBoard = document.getElementById('commentBoard');
    let commentList = list.resultList;
    let textComment = '';
    commentList.forEach(commentList => { 
        textComment +=
            `<div class="border shadow-sm PortalPage_memo__custom">
                <p class="PortalPage_date">${commentList.datetime}</p>
                <p class="PortalPage_albumTitle">${commentList.album_name}</p>
                <p class="PortalPage_albumComment">${commentList.comment}</p>
                <p class="PortalPage_user">${commentList.user_nickname} 님</p>
        </div>`
        })
        commentBoard.innerHTML = textComment;
}

function getPopup() { //팝업 정보
    $.ajax({
        type: 'GET',
        url: '/codeenator/api/portal/getPopup.do?user_agency=web',
        dataType: 'json',
        success: function(data) {
            const popupHiddenUntil = localStorage.getItem('popupHiddenUntil');
            const today = new Date().toISOString().split('T')[0];

            if (popupHiddenUntil > today) {
                return; // 팝업 숨김 처리
            }

            if (data.resultMap.enable_popup === true) { //값이 true(활성화)일 때
                createPopup(data.resultMap); //팝업 생성
            } else {
                console.log('팝업 비활성화됨');
            }
        },
        error: function(err) {
            console.error("요청 실패: ", err);
            if (err.responseText) {
                console.error("응답 텍스트: ", err.responseText);
            }
        }
    });
}

function createPopup(popupInfo) { //팝업 생성
    const popupLayer = document.getElementById('popupLayer');

    const popupText = `
        <div style="position: fixed; top: 10%; right: 10%; background-color: white; z-index: 10; padding: 20px; box-shadow: 0 4px 8px rgba(0,0,0,0.2); height:${popupInfo.popup_height}px; width:${popupInfo.popup_width}px; box-sizing: border-box; display: flex; flex-direction: column;">
            <div style="overflow: auto; margin-bottom: 20px; flex: 1;">
                <div id="popupContent">
                    <p>${popupInfo.popup_content}</p>
                </div>
            </div>
            <div style="display: flex; gap: 8px; justify-content: center; padding-top: 16px; margin-top: auto;">
                <button onclick="hidePopupForToday()"
                    style="cursor: pointer;" class="rounded p-2 text-white Button_normalSize__custom Button_subColor__custom">하루동안 보지않기</button>
                <button onclick="hidePopup()"
                    style="cursor: pointer;" class="rounded p-2 text-white Button_normalSize__custom Button_mainColor__purple">닫기</button>
            </div>
        </div>
    `;

    popupLayer.innerHTML = popupText;
    popupLayer.style.display = 'block'; // 팝업을 화면에 표시
}

function hidePopup() { //닫기 클릭
    const popupLayer = document.getElementById('popupLayer');
    popupLayer.style.display = 'none'; // 팝업 숨기기
}

function hidePopupForToday() { //하루동안 보지않기 : 내일 날짜 계산
    const popupLayer = document.getElementById('popupLayer');
    popupLayer.style.display = 'none';

    // 내일 날짜를 계산하여 로컬 스토리지에 저장
    const tomorrow = new Date();
    tomorrow.setDate(tomorrow.getDate() + 1); // 오늘 날짜에서 1일을 더함
    const tomorrowString = tomorrow.toISOString().split('T')[0]; // YYYY-MM-DDTHH:mm.sssZ 형식에서 T앞 부분인 YYYY-MM-DD 부분만 tomorrowString에 저장
    localStorage.setItem('popupHiddenUntil', tomorrowString); // 내일 날짜 저장
}

function checkPopupVisibility() { //하루동안 보지않기 : 날짜 비교
    const popupHiddenUntil = localStorage.getItem('popupHiddenUntil'); //저장된 내일 날짜 정보 가져오기
    const today = new Date().toISOString().split('T')[0]; // YYYY-MM-DDTHH:mm.sssZ 형식에서 T앞 부분인 YYYY-MM-DD 부분만 today에 저장

    if (popupHiddenUntil > today) { //popupHiddenUntil이 오늘보다 큰지 확인
        document.getElementById('popupLayer').style.display = 'none'; // 팝업 숨기기
    } else {
        getPopup(); //팝업 표시
    }
}

function getBanner() { //배너 조회 후 이미지 설정 @@img 경로 변경
    $.ajax({
        type: 'GET',
        url: '/codeenator/api/portal/getBanner.do?user_agency=web',
        dataType: 'json', 
        success: function (data) {
            console.log(data);
            bannerImg1.src = `/codeenator${data.resultList[0].file}`
            bannerImg2.src = `/codeenator${data.resultList[1].file}`
            bannerImg3.src = `/codeenator${data.resultList[2].file}`
        },
        error: function (err) {
            console.error("요청 실패: ", err);
            if (err.responseText) {
                console.error("응답 텍스트: ", err.responseText);
            }
        }
    });
}