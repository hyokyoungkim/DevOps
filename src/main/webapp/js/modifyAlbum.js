const url = new URL(window.location.href); // 현재 URL 객체 생성
// const params = new URLSearchParams(url.search); // URL의 쿼리 문자열을 파싱
const id_value = url.href.split('id=')[1] // 'id' 파라미터의 값을 가져옴
let selectedChapterIndex = null;
let selectedContentIndex = null;
const fileUrls = {}; // 파일 URL을 저장할 객체
let private_value;
let formParams = { 
    "name": "",
    "introduction": "",
    "chapter":[],
    "thumbnail": "",
    "category": "",
    "tag": "",
    "pk":""
}

function getAlbum() { //앨범 조회
        $.ajax({
        type: 'GET',
        url: `/codeenator/api/album/getAlbum.do?user_agent=web&id=${id_value}`,
        dataType: 'json', 
        success: function (data) {
            formParams.name = data.resultMap.name
            formParams.introduction= data.resultMap.introduction
            formParams.chapter=data.resultMap.chapter
            formParams.thumbnail= data.resultMap.thumbnail
            formParams.category= data.resultMap.category
            formParams.tag= data.resultMap.user_tag
            createForm(data.resultMap) //앨범 정보 생성
        },
        error: function (err) {
            console.error("요청 실패: ", err);
            if (err.responseText) {
                console.error("응답 텍스트: ", err.responseText);
            }
        }
    });
}



//--------------------------------------------페이지 설정----------------------------------------------------//
const form = document.getElementById('createForm');
const checkImg = document.getElementById('checkImg'); //앨범 제작 3단계 이미지 표시->좌측 체크
let form_value = 1; //1페이지

function createForm(album_data) { //현재 페이지 값에 따라 양식이 바뀜. 기본값은 1=1Page
    
    console.log(formParams)
    let textForm = '';
    let textImg = '';

    if (form_value === 1) {
        
        textImg += `
        <li class="mt-3 mb-3"><img src="../image/CheckCircleOutlineFilled.png" alt=""
                                        class="inline">앨범 정보</li>
                                <li class="mt-3 mb-3"><img src="../image/CheckCircleOutlineFilled.png" alt=""
                                        class="inline">콘텐츠 입력</li>
                                <li class="mt-3 mb-3"><img src="../image/CheckCircleOutlineFilled.png" alt=""
                                        class="inline">동의 및 발행</li>
        `
        textForm += `
        <div class="relative">
                                <div class="Title_box__custom">앨범 정보</div>
                                <div class="p-3">
                                    <h3 class="mt-5 mb-2">앨범 제목</h3><input type="text" class="w-full h-10 p-2 border"
                                        maxlength="100" placeholder="최대 100글자" value="${formParams.name}" id="form_title">
                                </div>
                                <div class="p-3 mt-5">
                                    <h3 class="mb-2">앨범 소개</h3><textarea rows="2"
                                        class="w-full h-20 p-2 border resize-none" maxlength="500"
                                        placeholder="최대 500글자" id="form_introduction">${formParams.introduction}</textarea></div>
                                <div class="flex p-3 mt-5">
                                    <div class="w-2/3">
                                        <h3 class="mb-2">썸네일</h3><input type="file" id="thumbnailBtn">
                                    </div>
                                    <div class="w-1/3 border h-44" id="thumbnailImg" style="background-image: url('/codeenator/${formParams.thumbnail}');background-size: cover;"></div>
                                </div>
                                <div class="p-3 mt-5">
                                    <h3 class="mb-2" id=category>카테고리</h3>
                                    <div>
                                        <div class="category border-2 pr-5 pl-5 pt-1 pb-1 inline-block rounded-sm m-1 cursor-pointer false" data-category="IT">
    IT
</div>
<div class="category border-2 pr-5 pl-5 pt-1 pb-1 inline-block rounded-sm m-1 cursor-pointer false" data-category="자기계발">
    자기계발
</div>
<div class="category border-2 pr-5 pl-5 pt-1 pb-1 inline-block rounded-sm m-1 cursor-pointer false" data-category="여행">
    여행
</div>
<div class="category border-2 pr-5 pl-5 pt-1 pb-1 inline-block rounded-sm m-1 cursor-pointer false" data-category="경제">
    경제
</div>
<div class="category border-2 pr-5 pl-5 pt-1 pb-1 inline-block rounded-sm m-1 cursor-pointer false" data-category="취미">
    취미
</div>
<div class="category border-2 pr-5 pl-5 pt-1 pb-1 inline-block rounded-sm m-1 cursor-pointer false" data-category="외국어">
    외국어
</div>
<div class="category border-2 pr-5 pl-5 pt-1 pb-1 inline-block rounded-sm m-1 cursor-pointer false" data-category="맛집">
    맛집
</div>
<div class="category border-2 pr-5 pl-5 pt-1 pb-1 inline-block rounded-sm m-1 cursor-pointer false" data-category="기타">
    기타
</div>
                                    </div>
                                </div>
                                <div class="p-3 mt-5">
                                    <h3 class="mb-2">태그(최대 3개)</h3><input type="text"
                                        class="w-full h-10 p-2 border resize-none" placeholder="ex)  트랜드,취미,KPOP "
                                        value="${formParams.tag}" id="form_tag">
                                </div>
                                <div class="p-3 mt-5" id="radioContainer">
                                    <h3 class="mb-2">공개여부 (비공개 선택 시 key를 발급해주세요)</h3><label>
                                    <input type="radio" name="auth" class="mr-2 AlbumInfo_radioButton__custom" value="public" checked="">공개</label><label>
                                    <input type="radio" name="auth" class="ml-5 mr-2 AlbumInfo_radioButton__custom" value="private">비공개</label>
                                            <button class="AlbumInfo_keyButton__custom" id="issueKeyBtn">key 발급</button>
                                            <div class="hidden" id="issueKeyText">발급 키 :  </div>
                                    <div>
                                </div>
                                </div>
                                <div class="mt-24 text-center ">
                                <button class="p-2 text-white rounded Button_normalSize__custom Button_subColor__custom" id="nextBtn">저장 후 다음 이동</button></div>
                            </div>
                            
        `
    }
    if (form_value === 2) {

        textImg += `
        <li class="mt-3 mb-3"><img src="../image/CheckCircleOutlineFilledPurple.png" alt=""
                                        class="inline">앨범 정보</li>
                                <li class="mt-3 mb-3"><img src="../image/CheckCircleOutlineFilled.png" alt=""
                                        class="inline">콘텐츠 입력</li>
                                <li class="mt-3 mb-3"><img src="../image/CheckCircleOutlineFilled.png" alt=""
                                        class="inline">동의 및 발행</li>
                `

        textForm += `
            <div class="relative">
                <div class="Title_box__custom">콘텐츠 입력</div>
                <button class="float-right mt-10 mb-3 mr-5"  id="addContentBtn">
                <img src="../image/AddFilled.png" alt="플러스아이콘" class="inline-block">챕터 추가하기</button>
                <div id="chapterLocation">`
        
        formParams.chapter.forEach(chapter => {

        textForm += `
            <div class="flex-col clear-both p-3 m-5 border-2 chapter" id="chapter-${chapter.id}" data-chapter-id="${chapter.id}">
            <button class="moveContentUp">↑ 위로 이동</button>
            <button class="ml-4 moveContentDown">↓ 아래로 이동</button>
            <button class="float-right mb-3 chapterRemove">
                <img src="../image/DeleteFilled.png" alt="삭제 아이콘" class="inline-block">챕터 삭제
            </button>
            <button class="float-right mb-3 mr-3 chapterAdd">
                <img src="../image/AddFilled.png" alt="추가 아이콘" class="inline-block">콘텐츠 추가
            </button>
            <div class="flex clear-both">
                <div class="w-full p-1 border border-dashed">
                    <input type="text" placeholder="챕터의 제목을 입력해주세요" class="w-full p-1 border chapterTitleInput" data-chapter-id="${chapter.id}" value="${chapter.name}">
                </div>
                <div class="flex gap-3 ml-2">
                </div>
            </div>
            <div id="contentAdd-${chapter.id}">`
            
            
            chapter.content.forEach(content => {

            textForm +=`
    <div class="groupContent" id="${content.id}">
        <div class="flex p-2 groupTitle">
            <div>
                <img class="w-[20px] mt-[3px] mr-3" src="../image/VideocamFilled.png" alt="Video">
            </div>
            <div class="text-black chapterTitleText">제목을 입력해주세요</div>
            <div class="flex gap-3 ml-auto">
                <button class="inline-block w-6 px-1 border upIcon">↑</button>
                <button class="inline-block w-6 px-1 border downIcon">↓</button>
                <button class="inline-block w-6 px-1 border foldIcon">
                    <img src="../image/arrow-up.png" alt="윗방향화살표" class="inline-block">
                </button>
                <button class="inline-block w-6 border p-[1px] deleteIcon">
                    <img src="../image/DeleteFilled.png" alt="삭제아이콘" class="inline-block">
                </button>
            </div>
        </div>
        <div class="w-[full] bg-[#eee] shadow-sm border rounded-sm top-[10vh] p-2 pt-1 viewContent">
            <div class="flex gap-4 mt-3 mb-3">`
                if (content.type) {
                    textForm+=`
                <div>
                    <input type="radio" class="translate-y-[3px] mr-1" id="video-${content.id}" value="1" name="content-${content.id}" ${content.type === 1 ? 'checked' : ''}>
                    <label for="video-${content.id}">동영상</label>
                </div>
                <div>
                    <input type="radio" class="translate-y-[3px] mr-1" id="pdf-${content.id}" value="2" name="content-${content.id}" ${content.type === 2 ? 'checked' : ''}>
                    <label for="pdf-${content.id}">PDF파일</label>
                </div>
                <div>
                    <input type="radio" class="translate-y-[3px] mr-1" id="url-${content.id}" value="3" name="content-${content.id}" ${content.type === 3 ? 'checked' : ''}>
                    <label for="url-${content.id}">Page Link</label>
                </div>
                <div>
                    <input type="radio" class="translate-y-[3px] mr-1" id="file-${content.id}" value="4" name="content-${content.id}" ${content.type === 4 ? 'checked' : ''}>
                    <label for="file-${content.id}">일반 파일</label>
                </div>
                <div>
                    <input type="radio" class="translate-y-[3px] mr-1" id="text-${content.id}" value="5" name="content-${content.id}" ${content.type === 5 ? 'checked' : ''}>
                    <label for="text-${content.id}">텍스트 입력</label>
                </div>
                `
                }
            textForm +=`</div>
            <div class="content-details">`
                if(content.type === 1){
                    textForm +=`
                    <div class="pb-2 border-b">
                        <input type="text" class="w-full h-10 p-1 border chapterTitleInput" placeholder="콘텐츠 제목 입력" name="name" value="${content.name}">
                    </div>
                    <div class="flex gap-2 pt-1 mt-1 mb-1">
                        <input type="text" class="w-full p-1 border" placeholder="동영상 페이지 URL을 입력해주세요" name="content" value="${content.link}">
                        <button class="p-2 text-white rounded Button_normalSize__custom Button_mainColor__purple" name="urlData" value=""
                        onclick="handleUrlCheckClick(event)">URL 확인</button>
                    </div>
                    <p class="mt-10 mb-10 leading-8">
                        · 동영상 사이트에서 연결 링크를 복사해 옵니다 (유튜브, 네이버tv) <br>
                        · URL 확인을 눌러 링크를 확인해보실 수 있습니다.
                    </p>
                    <div class="mt-5 mb-5 text-center"></div>
                    `
                } else if (content.type === 2) {
                    textForm +=`
                    <div class="pb-2 border-b">
                        <input type="text" class="w-full h-10 p-1 border chapterTitleInput" placeholder="콘텐츠 제목 입력" name="name" value="${content.name}">
                    </div>
                    <div class="flex gap-2 pt-1 mt-1 mb-1">
                        <input type="file" class="w-full p-1 border" accept="application/pdf" onchange="handleFileUpload(event)" data-url="${content.link}">
                    </div>
                    <p class="mt-10 mb-10 leading-8">
                        · PDF 파일을 업로드 해주세요.
                    </p>
                    `
                } else if (content.type === 3) {
                    textForm +=`
                    <div class="pb-2 border-b">
                        <input type="text" class="w-full h-10 p-1 border chapterTitleInput" placeholder="콘텐츠 제목 입력" name="name" value="${content.name}">
                    </div>
                    <div class="flex gap-2 pt-1 mt-1 mb-1">
                        <input type="text" class="w-full p-1 border" placeholder="URL을 입력해주세요" name="content" value="${content.link}">
                    </div>
                    <p class="mt-10 mb-10 leading-8">
                        · 웹사이트의 URL을 입력해주세요.
                    </p>
                    `
                } else if (content.type === 4) {
                    textForm +=`
                    <div class="pb-2 border-b">
                        <input type="text" class="w-full h-10 p-1 border chapterTitleInput" placeholder="콘텐츠 제목 입력" name="name" value="${content.name}">
                    </div>
                    <div class="flex gap-2 pt-1 mt-1 mb-1">
                        <input type="file" class="w-full p-1 border" onchange="handleFileUpload(event)" data-url="${content.link}">
                    </div>
                    <p class="mt-10 mb-10 leading-8">
                        · 파일을 업로드 해주세요.
                    </p>
                    `
                } else if(content.type === 5){
                    textForm+=`
                    <div class="pb-2 border-b">
                        <input type="text" class="w-full h-10 p-1 border chapterTitleInput" placeholder="콘텐츠 제목 입력" name="name" value="${content.name}">
                    </div>
                    <div class="wmde-markdown-var w-md-editor w-md-editor-show-live"
                                        style="height: 200px;">
                                        <div class="w-md-editor-toolbar">
                                            <ul>
                                                <li class=""><button aria-label="Insert italic" id="italicIcon">
                                                        <svg data-name="italic" width="12" height="12" role="img"
                                                            viewBox="0 0 320 512">
                                                            <path fill="currentColor"
                                                                d="M204.758 416h-33.849l62.092-320h40.725a16 16 0 0 0 15.704-12.937l6.242-32C297.599 41.184 290.034 32 279.968 32H120.235a16 16 0 0 0-15.704 12.937l-6.242 32C96.362 86.816 103.927 96 113.993 96h33.846l-62.09 320H46.278a16 16 0 0 0-15.704 12.935l-6.245 32C22.402 470.815 29.967 480 40.034 480h158.479a16 16 0 0 0 15.704-12.935l6.245-32c1.927-9.88-5.638-19.065-15.704-19.065z">
                                                            </path>
                                                        </svg></button></li>
                                                <li class=""><button aria-label="Insert code" id="slashIcon"> <svg
                                                            width="14" height="14" role="img" viewBox="0 0 640 512">
                                                            <path fill="currentColor"
                                                                d="M278.9 511.5l-61-17.7c-6.4-1.8-10-8.5-8.2-14.9L346.2 8.7c1.8-6.4 8.5-10 14.9-8.2l61 17.7c6.4 1.8 10 8.5 8.2 14.9L293.8 503.3c-1.9 6.4-8.5 10.1-14.9 8.2zm-114-112.2l43.5-46.4c4.6-4.9 4.3-12.7-.8-17.2L117 256l90.6-79.7c5.1-4.5 5.5-12.3.8-17.2l-43.5-46.4c-4.5-4.8-12.1-5.1-17-.5L3.8 247.2c-5.1 4.7-5.1 12.8 0 17.5l144.1 135.1c4.9 4.6 12.5 4.4 17-.5zm327.2.6l144.1-135.1c5.1-4.7 5.1-12.8 0-17.5L492.1 112.1c-4.8-4.5-12.4-4.3-17 .5L431.6 159c-4.6 4.9-4.3 12.7.8 17.2L523 256l-90.6 79.7c-5.1 4.5-5.5 12.3-.8 17.2l43.5 46.4c4.5 4.9 12.1 5.1 17 .6z">
                                                            </path>
                                                        </svg></button></li>
                                                <li class=""><button aria-label="Insert code" id="colonIcon"><svg
                                                            width="14" height="14" role="img" viewBox="0 0 640 512">
                                                            <path fill="currentColor"
                                                                d="M520,95.75 L520,225.75 C520,364.908906 457.127578,437.050625 325.040469,472.443125 C309.577578,476.586875 294.396016,464.889922 294.396016,448.881641 L294.396016,414.457031 C294.396016,404.242891 300.721328,395.025078 310.328125,391.554687 C377.356328,367.342187 414.375,349.711094 414.375,274.5 L341.25,274.5 C314.325781,274.5 292.5,252.674219 292.5,225.75 L292.5,95.75 C292.5,68.8257812 314.325781,47 341.25,47 L471.25,47 C498.174219,47 520,68.8257812 520,95.75 Z M178.75,47 L48.75,47 C21.8257813,47 0,68.8257812 0,95.75 L0,225.75 C0,252.674219 21.8257813,274.5 48.75,274.5 L121.875,274.5 C121.875,349.711094 84.8563281,367.342187 17.828125,391.554687 C8.22132813,395.025078 1.89601563,404.242891 1.89601563,414.457031 L1.89601563,448.881641 C1.89601563,464.889922 17.0775781,476.586875 32.5404687,472.443125 C164.627578,437.050625 227.5,364.908906 227.5,225.75 L227.5,95.75 C227.5,68.8257812 205.674219,47 178.75,47 Z">
                                                            </path>
                                                        </svg></button></li>
                                                <li class=""><button type="button" data-name="title"
                                                        aria-label="Insert title" id="titleIcon"><svg width="12"
                                                            height="12" viewBox="0 0 520 520">
                                                            <path fill="currentColor"
                                                                d="M15.7083333,468 C7.03242448,468 0,462.030833 0,454.666667 L0,421.333333 C0,413.969167 7.03242448,408 15.7083333,408 L361.291667,408 C369.967576,408 377,413.969167 377,421.333333 L377,454.666667 C377,462.030833 369.967576,468 361.291667,468 L15.7083333,468 Z M21.6666667,366 C9.69989583,366 0,359.831861 0,352.222222 L0,317.777778 C0,310.168139 9.69989583,304 21.6666667,304 L498.333333,304 C510.300104,304 520,310.168139 520,317.777778 L520,352.222222 C520,359.831861 510.300104,366 498.333333,366 L21.6666667,366 Z M136.835938,64 L136.835937,126 L107.25,126 L107.25,251 L40.75,251 L40.75,126 L-5.68434189e-14,126 L-5.68434189e-14,64 L136.835938,64 Z M212,64 L212,251 L161.648438,251 L161.648438,64 L212,64 Z M378,64 L378,126 L343.25,126 L343.25,251 L281.75,251 L281.75,126 L238,126 L238,64 L378,64 Z M449.047619,189.550781 L520,189.550781 L520,251 L405,251 L405,64 L449.047619,64 L449.047619,189.550781 Z">
                                                            </path>
                                                        </svg></button>
                                                    <div class="w-md-editor-toolbar-child" id="titleGroupIcon"
                                                        style="display: none;">
                                                        <div class="w-md-editor-toolbar undefined">
                                                            <ul>
                                                                <li class="" id="titleIcon1"><button type="button"
                                                                        data-name="title1"
                                                                        aria-label="Insert title1 (ctrl + 1)"
                                                                        title="Insert title1 (ctrl + 1)">
                                                                        <div style="font-size: 18px; text-align: left;">
                                                                            Title 1
                                                                        </div>
                                                                    </button></li>
                                                                <li class="" id="titleIcon2"><button type="button"
                                                                        data-name="title2"
                                                                        aria-label="Insert title2 (ctrl + 2)"
                                                                        title="Insert title2 (ctrl + 2)">
                                                                        <div style="font-size: 16px; text-align: left;">
                                                                            Title 2
                                                                        </div>
                                                                    </button></li>
                                                                <li class="" id="titleIcon3"><button type="button"
                                                                        data-name="title3"
                                                                        aria-label="Insert title3 (ctrl + 3)"
                                                                        title="Insert title3 (ctrl + 3)">
                                                                        <div style="font-size: 15px; text-align: left;">
                                                                            Title 3
                                                                        </div>
                                                                    </button></li>
                                                                <li class="" id="titleIcon4"><button type="button"
                                                                        data-name="title4"
                                                                        aria-label="Insert title4 (ctrl + 4)"
                                                                        title="Insert title4 (ctrl + 4)">
                                                                        <div style="font-size: 14px; text-align: left;">
                                                                            Title 4
                                                                        </div>
                                                                    </button></li>
                                                                <li class="" id="titleIcon5"><button type="button"
                                                                        data-name="title5"
                                                                        aria-label="Insert title5 (ctrl + 5)"
                                                                        title="Insert title5 (ctrl + 5)">
                                                                        <div style="font-size: 12px; text-align: left;">
                                                                            Title 5
                                                                        </div>
                                                                    </button></li>
                                                                <li class="" id="titleIcon6"><button type="button"
                                                                        data-name="title6"
                                                                        aria-label="Insert title6 (ctrl + 6)"
                                                                        title="Insert title6 (ctrl + 6)">
                                                                        <div style="font-size: 12px; text-align: left;">
                                                                            Title 6
                                                                        </div>
                                                                    </button></li>
                                                            </ul>
                                                        </div>
                                                    </div>
                                                </li>
                                                <li class="w-md-editor-toolbar-divider"></li>
                                            </ul>
                                            <ul>
                                                <li class="" id="leftView"><button type="button" data-name="edit"
                                                        aria-label="Edit code (ctrl + 7)"
                                                        title="Edit code (ctrl + 7)"><svg width="12" height="12"
                                                            viewBox="0 0 520 520">
                                                            <polygon fill="currentColor"
                                                                points="0 71.293 0 122 319 122 319 397 0 397 0 449.707 372 449.413 372 71.293">
                                                            </polygon>
                                                            <polygon fill="currentColor"
                                                                points="429 71.293 520 71.293 520 122 481 123 481 396 520 396 520 449.707 429 449.413">
                                                            </polygon>
                                                        </svg></button></li>
                                                <li class="active" id="bothView"><button type="button" data-name="live"
                                                        aria-label="Live code (ctrl + 8)"
                                                        title="Live code (ctrl + 8)"><svg width="12" height="12"
                                                            viewBox="0 0 520 520">
                                                            <polygon fill="currentColor"
                                                                points="0 71.293 0 122 179 122 179 397 0 397 0 449.707 232 449.413 232 71.293">
                                                            </polygon>
                                                            <polygon fill="currentColor"
                                                                points="289 71.293 520 71.293 520 122 341 123 341 396 520 396 520 449.707 289 449.413">
                                                            </polygon>
                                                        </svg></button></li>
                                                <li class="" id="rightView"><button type="button" data-name="preview"
                                                        aria-label="Preview code (ctrl + 9)"
                                                        title="Preview code (ctrl + 9)"><svg width="12" height="12"
                                                            viewBox="0 0 520 520">
                                                            <polygon fill="currentColor"
                                                                points="0 71.293 0 122 38.023 123 38.023 398 0 397 0 449.707 91.023 450.413 91.023 72.293">
                                                            </polygon>
                                                            <polygon fill="currentColor"
                                                                points="148.023 72.293 520 71.293 520 122 200.023 124 200.023 397 520 396 520 449.707 148.023 450.413">
                                                            </polygon>
                                                        </svg></button></li>
                                                <li class="w-md-editor-toolbar-divider"></li>
                                                <li class="" id="fullView"><button type="button" data-name="fullscreen"
                                                        aria-label="Toggle fullscreen (ctrl + 0)"
                                                        title="Toggle fullscreen (ctrl+ 0)"><svg width="12" height="12"
                                                            viewBox="0 0 520 520">
                                                            <path fill="currentColor"
                                                                d="M118 171.133334L118 342.200271C118 353.766938 126.675 365.333605 141.133333 365.333605L382.634614 365.333605C394.201281 365.333605 405.767948 356.658605 405.767948 342.200271L405.767948 171.133334C405.767948 159.566667 397.092948 148 382.634614 148L141.133333 148C126.674999 148 117.999999 156.675 118 171.133334zM465.353591 413.444444L370 413.444444 370 471.222222 474.0221 471.222222C500.027624 471.222222 520.254143 451 520.254143 425L520.254143 321 462.464089 321 462.464089 413.444444 465.353591 413.444444zM471.0221 43L367 43 367 100.777778 462.353591 100.777778 462.353591 196.111111 520.143647 196.111111 520.143647 89.2222219C517.254144 63.2222219 497.027624 43 471.0221 43zM57.7900547 100.777778L153.143646 100.777778 153.143646 43 46.2320439 43C20.2265191 43 0 63.2222219 0 89.2222219L0 193.222222 57.7900547 193.222222 57.7900547 100.777778zM57.7900547 321L0 321 0 425C0 451 20.2265191 471.222222 46.2320439 471.222223L150.254143 471.222223 150.254143 413.444445 57.7900547 413.444445 57.7900547 321z">
                                                            </path>
                                                        </svg></button></li>
                                            </ul>
                                        </div>
                                        <div class="w-md-editor-content" id="allArea">
                                            <div class="w-md-editor-area w-md-editor-input">
                                                <div class="w-md-editor-text" style="min-height: 100px;">
                                                    <pre class="w-md-editor-text-pre wmde-markdown-color"></pre>
                                                    <textarea autocomplete="off" autocorrect="off" autocapitalize="off"
                            spellcheck="false" class="w-md-editor-text-input"
                            name="" id="contentInput">${content.link}</textarea>
                                                </div>
                                            </div>
                                            <div class="w-md-editor-preview" id="previewArea">
                                                <div class="wmde-markdown wmde-markdown-color ">

                                                </div>
                                            </div>
                                        </div>
                                        <div class="w-md-editor-bar"><svg viewBox="0 0 512 512" height="100%">
                                                <path fill="currentColor"
                                                    d="M304 256c0 26.5-21.5 48-48 48s-48-21.5-48-48 21.5-48 48-48 48 21.5 48 48zm120-48c-26.5 0-48 21.5-48 48s21.5 48 48 48 48-21.5 48-48-21.5-48-48-48zm-336 0c-26.5 0-48 21.5-48 48s21.5 48 48 48 48-21.5 48-48-21.5-48-48-48z">
                                                </path>
                                            </svg></div>
                                    </div>
                    `
                }
            textForm +=`</div>
        </div>
    </div>
    `

    });

        textForm +=`</div>
        </div>
        `
        });
        
        textForm +=
            `</div>
        </div>

        <div class="flex justify-center gap-2 mt-24">
            <button class="p-2 text-white rounded Button_normalSize__custom Button_subColor__custom" id="preBtn">이전단계로 이동</button>
            <button class="p-2 text-white rounded Button_normalSize__custom Button_subColor__custom" id="nextBtn">저장 후 다음 이동</button>
        </div>`
    }
    if (form_value === 3) {
        textImg += `
        <li class="mt-3 mb-3"><img src="../image/CheckCircleOutlineFilledPurple.png" alt=""
                                        class="inline">앨범 정보</li>
                                <li class="mt-3 mb-3"><img src="../image/CheckCircleOutlineFilledPurple.png" alt=""
                                        class="inline">콘텐츠 입력</li>
                                <li class="mt-3 mb-3"><img src="../image/CheckCircleOutlineFilled.png" alt=""
                                        class="inline">동의 및 발행</li>
        `

        textForm += `
        <div class="Title_box__custom">동의 및 발행</div>
                            <div class="p-3 mt-8 leading-9">
                                <ul>
                                    <li> · 제출하는 앨범이 이전에 코디네이터가 등록한 앨범과 유사할 때 등록이 거절될 수 있습니다. </li>
                                    <li> · 사회적으로 좋지 않은 영향을 끼칠 수 있는 컨텐츠가 있는 앨범은 등록이 거절 됩니다. </li>
                                    <li> · 제품의 홍보 목적으로 만들어 진 앨범은 등록이 거절될 수 있습니다.</li>
                                </ul>
                                <div class="mt-4 text-center"><input type="checkbox" name="term1" id="check_agree"
                                        class="translate-y-[2px] mr-1" id="check_submit"><label for="term1">상기 내용에 동의합니다</label></div>
                            </div>
                            <div class="p-3 mt-8 leading-9">
                                <ul>
                                    <li> · 저작권 문제가 있는 컨텐츠 공유시 그에 대한 책임은 코디네이터에 있으며 본 서비스에서는 책임을 지지 아니합니다.</li>
                                    <li> · 개인정보나 주요 기밀 정보를 누설하는 컨텐츠 공유시 그에 대한 책임은 코디네이터에 있으며 본 서비스에서는 책임을 <br> &nbsp; 지지
                                        아니합니다.
                                    </li>
                                    <li> · 상기 문제로 외부 요청이 있을 시 앨범은 등록 이후라도 삭제 될 수 있습니다.</li>
                                </ul>
                                <div class="mt-4 text-center"><input type="checkbox" name="term2" id="check_copyright"
                                        class="translate-y-[2px] mr-1" id="check_copyright"><label for="term2">상기 내용에 동의합니다</label></div>
                            </div>
                            <div class="text-center mt-7"><input type="checkbox" name="termAll" id="termAll"
                                    class="translate-y-[2px] mr-1" id="check_all"><label for="termAll">상기 내용에 모두 동의합니다</label></div>
                            <div class="flex justify-center gap-2 mt-24"><button
                                    class="p-2 text-white rounded Button_normalSize__custom Button_subColor__custom" id="preBtn">이전단계로
                                    이동</button><button
                                    class="p-2 text-white rounded Button_normalSize__custom Button_subColor__custom" id="submitBtn">수정</button>
                            </div>

        `
    }

    form.innerHTML = textForm;
    checkImg.innerHTML = textImg; //좌측 현재 진행정도를 표시하는 이미지
    styleCategory(); //카테고리 스타일 적용

    const form_title = document.getElementById('form_title');
    const form_introduction = document.getElementById('form_introduction');
    const form_tag = document.getElementById('form_tag');

    if (form_title) { //앨범제목
        form_title.addEventListener('change', function () { //1Page 앨범 제목
            formParams.name = form_title.value;
            console.log(formParams);
        });
    }

    if (form_introduction) { //앨범 소개
    form_introduction.addEventListener('change', function () { //1Page 앨범 소개
    formParams.introduction = form_introduction.value;
    console.log(formParams)
    })
        }

    if (form_tag) { //태그
    form_tag.addEventListener('change', function () { //1Page 앨범 태그
    formParams.tag = form_tag.value;
    console.log(formParams)
    })
    }

    const fileInput = document.getElementById('thumbnailBtn');
    if (fileInput) {//썸네일
        fileInput.addEventListener('change', addThumbnail);
    }
    
    if (form_value === 1) { 
    const publicRadio = document.querySelector('input[value="public"]');
    const privateRadio = document.querySelector('input[value="private"]');
    const issueKeyBtn = document.getElementById('issueKeyBtn');
    const issueKeyText = document.getElementById('issueKeyText');

        document.getElementById('radioContainer').addEventListener('change', function () {
            if (privateRadio.checked) {
                issueKeyBtn.disabled = false;             // 비공개 선택 시 key 발급 버튼 활성화

                // 비공개 선택 시 기본 텍스트 "발급 키 :" 표시
                issueKeyText.innerText = '발급 키 :';
                issueKeyText.classList.remove('hidden');  // 비공개 선택 시 키 영역 보이기

            } else if (publicRadio.checked) {
                issueKeyText.classList.add('hidden');     // 공개 선택 시 키 텍스트 숨김
                formParams.pk = '';
                private_value = '';                      // 공개 선택 시 키 초기화
                issueKeyBtn.disabled = true;              // key 발급 버튼 비활성화
            }


            

        // 비공개 상태에서 키 발급 시
        issueKeyBtn.addEventListener('click', function () {
            if (privateRadio.checked) {  // 비공개 상태일 때만 키 발급
                const privateKey = clickIssueKey();         // 랜덤 키 생성
                formParams.pk = privateKey; 
                private_value = privateKey;              // formParams에 키 저장
                issueKeyText.innerText = `발급 키 : ${privateKey}`; // 텍스트 업데이트
                console.log(formParams.pk);
            }
        });

        });
    }




}

//------------------------------------------1Page--------------------------------------------------//

function styleCategory() { //1Page 카테고리 스타일 효과(테두리)
    const selectedCategory = formParams.category;
    const categoryDivs = document.querySelectorAll('.category'); //category 클래스 div 선택

    categoryDivs.forEach(div => {
        const category = div.getAttribute('data-category');
        if (category === selectedCategory) {
            div.classList.add('border-[#9333ea]'); // 선택된 카테고리에 테두리 색상 추가
        } else {
            div.classList.remove('border-[#9333ea]'); // 선택되지 않은 카테고리에는 테두리 색상 제거
        }
    });
}

function setCategory(e) { // 카테고리를 클릭할 때 테두리 효과를 적용하는 함수
    const clickedDiv = e.currentTarget;

    // 모든 카테고리에서 선택 스타일을 제거
    const categoryDivs = document.querySelectorAll('.category');
    categoryDivs.forEach(div => {
        div.classList.remove('border-[#9333ea]');
    });
    
    // 클릭된 카테고리에 스타일을 추가
    clickedDiv.classList.add('border-[#9333ea]');
    
    // formParams.category를 클릭된 카테고리로 업데이트
    formParams.category = clickedDiv.getAttribute('data-category');
}

function clickCategory() { //카테고리 클릭 이벤트를 설정하는 함수
    const categoryDivs = document.querySelectorAll('.category');
    categoryDivs.forEach(div => {
        div.removeEventListener('click', setCategory); // 이전 클릭 이벤트 리스너 제거
        div.addEventListener('click', setCategory); // 새로운 클릭 이벤트 리스너 추가
    });
}

function addThumbnail(e) { //썸네일 변경
    const fileInput = document.getElementById('thumbnailBtn');
    const file = fileInput.files[0];

    if (file) {
        const formData = new FormData();
        formData.append('file', file);
        // 파일 업로드 함수 호출
        fileUpload(formData);
    } else {
        console.log('파일이 선택되지 않았습니다.');
    }
}

function displayThumbnail(imageUrl) { //썸네일 미리보기
    const thumbnailDiv = document.getElementById('thumbnailImg');
    thumbnailDiv.style.backgroundImage = `url(${imageUrl})`;
    thumbnailDiv.style.backgroundSize = 'cover';
    thumbnailDiv.style.backgroundPosition = 'center';
}

function saveToLocalStorage(imageUrl) { // 1Page 이미지 URL을 로컬 스토리지에 저장하는 함수
    localStorage.setItem('uploadedImage', imageUrl); // 'uploadedImage'라는 키로 이미지 URL 저장
}

function fileUpload(formData){ //파일 업로드 (썸네일)
    console.log(formData);
    $.ajax({
        type: 'post',
        url: '/codeenator/api/file/upload.do?user_agent=web',
        data: formData,
        dataType: 'JSON',
        processData: false,
        contentType: false,
        success: function (data) {
            console.log(data);
            if (form_value === 1) {
                // 업로드된 파일을 미리보기로 설정
                const uploadedImageUrl = URL.createObjectURL(formData.get('file'));
                displayThumbnail(uploadedImageUrl);
                
                // formParams.thumbnail 업데이트
                formParams.thumbnail = data.resultMap.upload_path + '/' + data.resultMap.stored_name;

                // URL을 로컬 스토리지에 저장 (선택사항)
                saveToLocalStorage(uploadedImageUrl);
            }
            

        },
        error:function (err){
            console.log(err);
        }
    });
}

function clickIssueKey() { //키 발급
        const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789'; // 영어 대소문자 + 숫자
    let result = '';
    for (let i = 0; i < 6; i++) {
        const randomIndex = Math.floor(Math.random() * characters.length);
        result += characters[randomIndex];
    }
    return result;
}

function checkUrl(url) { //유튜브/네이버 url인지
    const youtubeRegex = /^https:\/\/www\.youtube\.com\/watch\?v=[\w-]{11}$/;
    const naverTvRegex = /^https:\/\/tv\.naver\.com\/v\/[\d]+$/;
    
    return youtubeRegex.test(url) || naverTvRegex.test(url);
}

function handleUrlCheckClick(e) { //url값이 네이버 또는 유튜브가 맞으면 button에 넣음
    const button = e.target;
    if (!button.classList.contains('Button_mainColor__purple')) return;

    const contentDiv = button.closest('.groupContent');
    if (!contentDiv) return;

    const inputField = contentDiv.querySelector('input[name="content"]');
    const url = inputField.value.trim();
    
    if (checkUrl(url)) {
        button.value = url; // 유효한 URL일 경우 버튼의 value 속성에 URL 설정
        console.log('Valid URL:', url); // 디버깅용
        alert('확인되었습니다.');
    } else {
        inputField.value = ''; // 유효하지 않은 URL일 경우 입력값 비우기
        button.value = ''; // 버튼의 value 속성도 비우기
        alert('유효한 URL을 입력해 주세요. YouTube 또는 Naver TV URL이 필요합니다.');
    }
}

//------------------------------------------2Page--------------------------------------------------//
function clickRadioBtn(e) { //2페이지 라디오버튼
    if (form_value === 2) {
        const newSelectedId = e.target.id.split('-')[0]; //video,pdf,url,file,text
        const chapterId = e.target.name.split('-')[1];
        const parts = e.target.name.split('-');
        const contentDetails = e.target.closest('.viewContent').querySelector('.content-details');
        const chapterIndex = parseInt(parts[0], 10); // 현재 선택된 챕터 인덱스
        const contentIndex = parseInt(parts[1], 10); // 현재 선택된 콘텐츠 인덱스

        selectedChapterIndex = chapterIndex;
        selectedContentIndex = contentIndex;


        if (!contentDetails) return;

        switch (newSelectedId) { 
            case 'video':
                contentDetails.innerHTML = `
                    <div class="pb-2 border-b">
                        <input type="text" class="w-full h-10 p-1 border chapterTitleInput" placeholder="콘텐츠 제목 입력" name="name" value="">
                    </div>
                    <div class="flex gap-2 pt-1 mt-1 mb-1">
                        <input type="text" class="w-full p-1 border" placeholder="동영상 페이지 URL을 입력해주세요" name="content" value="">
                        <button class="p-2 text-white rounded Button_normalSize__custom Button_mainColor__purple" name="urlData" value=""
                        onclick="handleUrlCheckClick(event)">URL 확인</button>
                    </div>
                    <p class="mt-10 mb-10 leading-8">
                        · 동영상 사이트에서 연결 링크를 복사해 옵니다 (유튜브, 네이버tv) <br>
                        · URL 확인을 눌러 링크를 확인해보실 수 있습니다.
                    </p>
                    <div class="mt-5 mb-5 text-center"></div>
                `;
                break;

            case 'pdf':
                contentDetails.innerHTML = `
                    <div class="pb-2 border-b">
                        <input type="text" class="w-full h-10 p-1 border chapterTitleInput" placeholder="콘텐츠 제목 입력" name="name" value="">
                    </div>
                    <div class="flex gap-2 pt-1 mt-1 mb-1">
                        <input type="file" class="w-full p-1 border" accept="application/pdf" onchange="handleFileUpload(event)" data-url="">

                    </div>
                    <p class="mt-10 mb-10 leading-8">
                        · PDF 파일을 업로드 해주세요.
                    </p>
                `;
                break;

            case 'url':
                contentDetails.innerHTML = `
                    <div class="pb-2 border-b">
                        <input type="text" class="w-full h-10 p-1 border chapterTitleInput" placeholder="콘텐츠 제목 입력" name="name" value="">
                    </div>
                    <div class="flex gap-2 pt-1 mt-1 mb-1">
                        <input type="text" class="w-full p-1 border" placeholder="URL을 입력해주세요" name="content" value="">
                    </div>
                    <p class="mt-10 mb-10 leading-8">
                        · 웹사이트의 URL을 입력해주세요.
                    </p>
                `;
                break;

            case 'file':
                contentDetails.innerHTML = `
                    <div class="pb-2 border-b">
                        <input type="text" class="w-full h-10 p-1 border chapterTitleInput" placeholder="콘텐츠 제목 입력" name="name" value="">
                    </div>
                    <div class="flex gap-2 pt-1 mt-1 mb-1">
                        <input type="file" class="w-full p-1 border" onchange="handleFileUpload(event)" data-url="">
                    </div>
                    <p class="mt-10 mb-10 leading-8">
                        · 파일을 업로드 해주세요.
                    </p>
                `;
                break;
            
            case 'text':
                contentDetails.innerHTML = `
                    <div class="pb-2 border-b">
                        <input type="text" class="w-full h-10 p-1 border chapterTitleInput" placeholder="콘텐츠 제목 입력" name="name" value="">
                    </div>
                    <div class="wmde-markdown-var w-md-editor w-md-editor-show-live"
                                        style="height: 200px;">
                                        <div class="w-md-editor-toolbar">
                                            <ul>
                                                <li class=""><button aria-label="Insert italic" id="italicIcon">
                                                        <svg data-name="italic" width="12" height="12" role="img"
                                                            viewBox="0 0 320 512">
                                                            <path fill="currentColor"
                                                                d="M204.758 416h-33.849l62.092-320h40.725a16 16 0 0 0 15.704-12.937l6.242-32C297.599 41.184 290.034 32 279.968 32H120.235a16 16 0 0 0-15.704 12.937l-6.242 32C96.362 86.816 103.927 96 113.993 96h33.846l-62.09 320H46.278a16 16 0 0 0-15.704 12.935l-6.245 32C22.402 470.815 29.967 480 40.034 480h158.479a16 16 0 0 0 15.704-12.935l6.245-32c1.927-9.88-5.638-19.065-15.704-19.065z">
                                                            </path>
                                                        </svg></button></li>
                                                <li class=""><button aria-label="Insert code" id="slashIcon"> <svg
                                                            width="14" height="14" role="img" viewBox="0 0 640 512">
                                                            <path fill="currentColor"
                                                                d="M278.9 511.5l-61-17.7c-6.4-1.8-10-8.5-8.2-14.9L346.2 8.7c1.8-6.4 8.5-10 14.9-8.2l61 17.7c6.4 1.8 10 8.5 8.2 14.9L293.8 503.3c-1.9 6.4-8.5 10.1-14.9 8.2zm-114-112.2l43.5-46.4c4.6-4.9 4.3-12.7-.8-17.2L117 256l90.6-79.7c5.1-4.5 5.5-12.3.8-17.2l-43.5-46.4c-4.5-4.8-12.1-5.1-17-.5L3.8 247.2c-5.1 4.7-5.1 12.8 0 17.5l144.1 135.1c4.9 4.6 12.5 4.4 17-.5zm327.2.6l144.1-135.1c5.1-4.7 5.1-12.8 0-17.5L492.1 112.1c-4.8-4.5-12.4-4.3-17 .5L431.6 159c-4.6 4.9-4.3 12.7.8 17.2L523 256l-90.6 79.7c-5.1 4.5-5.5 12.3-.8 17.2l43.5 46.4c4.5 4.9 12.1 5.1 17 .6z">
                                                            </path>
                                                        </svg></button></li>
                                                <li class=""><button aria-label="Insert code" id="colonIcon"><svg
                                                            width="14" height="14" role="img" viewBox="0 0 640 512">
                                                            <path fill="currentColor"
                                                                d="M520,95.75 L520,225.75 C520,364.908906 457.127578,437.050625 325.040469,472.443125 C309.577578,476.586875 294.396016,464.889922 294.396016,448.881641 L294.396016,414.457031 C294.396016,404.242891 300.721328,395.025078 310.328125,391.554687 C377.356328,367.342187 414.375,349.711094 414.375,274.5 L341.25,274.5 C314.325781,274.5 292.5,252.674219 292.5,225.75 L292.5,95.75 C292.5,68.8257812 314.325781,47 341.25,47 L471.25,47 C498.174219,47 520,68.8257812 520,95.75 Z M178.75,47 L48.75,47 C21.8257813,47 0,68.8257812 0,95.75 L0,225.75 C0,252.674219 21.8257813,274.5 48.75,274.5 L121.875,274.5 C121.875,349.711094 84.8563281,367.342187 17.828125,391.554687 C8.22132813,395.025078 1.89601563,404.242891 1.89601563,414.457031 L1.89601563,448.881641 C1.89601563,464.889922 17.0775781,476.586875 32.5404687,472.443125 C164.627578,437.050625 227.5,364.908906 227.5,225.75 L227.5,95.75 C227.5,68.8257812 205.674219,47 178.75,47 Z">
                                                            </path>
                                                        </svg></button></li>
                                                <li class=""><button type="button" data-name="title"
                                                        aria-label="Insert title" id="titleIcon"><svg width="12"
                                                            height="12" viewBox="0 0 520 520">
                                                            <path fill="currentColor"
                                                                d="M15.7083333,468 C7.03242448,468 0,462.030833 0,454.666667 L0,421.333333 C0,413.969167 7.03242448,408 15.7083333,408 L361.291667,408 C369.967576,408 377,413.969167 377,421.333333 L377,454.666667 C377,462.030833 369.967576,468 361.291667,468 L15.7083333,468 Z M21.6666667,366 C9.69989583,366 0,359.831861 0,352.222222 L0,317.777778 C0,310.168139 9.69989583,304 21.6666667,304 L498.333333,304 C510.300104,304 520,310.168139 520,317.777778 L520,352.222222 C520,359.831861 510.300104,366 498.333333,366 L21.6666667,366 Z M136.835938,64 L136.835937,126 L107.25,126 L107.25,251 L40.75,251 L40.75,126 L-5.68434189e-14,126 L-5.68434189e-14,64 L136.835938,64 Z M212,64 L212,251 L161.648438,251 L161.648438,64 L212,64 Z M378,64 L378,126 L343.25,126 L343.25,251 L281.75,251 L281.75,126 L238,126 L238,64 L378,64 Z M449.047619,189.550781 L520,189.550781 L520,251 L405,251 L405,64 L449.047619,64 L449.047619,189.550781 Z">
                                                            </path>
                                                        </svg></button>
                                                    <div class="w-md-editor-toolbar-child" id="titleGroupIcon"
                                                        style="display: none;">
                                                        <div class="w-md-editor-toolbar undefined">
                                                            <ul>
                                                                <li class="" id="titleIcon1"><button type="button"
                                                                        data-name="title1"
                                                                        aria-label="Insert title1 (ctrl + 1)"
                                                                        title="Insert title1 (ctrl + 1)">
                                                                        <div style="font-size: 18px; text-align: left;">
                                                                            Title 1
                                                                        </div>
                                                                    </button></li>
                                                                <li class="" id="titleIcon2"><button type="button"
                                                                        data-name="title2"
                                                                        aria-label="Insert title2 (ctrl + 2)"
                                                                        title="Insert title2 (ctrl + 2)">
                                                                        <div style="font-size: 16px; text-align: left;">
                                                                            Title 2
                                                                        </div>
                                                                    </button></li>
                                                                <li class="" id="titleIcon3"><button type="button"
                                                                        data-name="title3"
                                                                        aria-label="Insert title3 (ctrl + 3)"
                                                                        title="Insert title3 (ctrl + 3)">
                                                                        <div style="font-size: 15px; text-align: left;">
                                                                            Title 3
                                                                        </div>
                                                                    </button></li>
                                                                <li class="" id="titleIcon4"><button type="button"
                                                                        data-name="title4"
                                                                        aria-label="Insert title4 (ctrl + 4)"
                                                                        title="Insert title4 (ctrl + 4)">
                                                                        <div style="font-size: 14px; text-align: left;">
                                                                            Title 4
                                                                        </div>
                                                                    </button></li>
                                                                <li class="" id="titleIcon5"><button type="button"
                                                                        data-name="title5"
                                                                        aria-label="Insert title5 (ctrl + 5)"
                                                                        title="Insert title5 (ctrl + 5)">
                                                                        <div style="font-size: 12px; text-align: left;">
                                                                            Title 5
                                                                        </div>
                                                                    </button></li>
                                                                <li class="" id="titleIcon6"><button type="button"
                                                                        data-name="title6"
                                                                        aria-label="Insert title6 (ctrl + 6)"
                                                                        title="Insert title6 (ctrl + 6)">
                                                                        <div style="font-size: 12px; text-align: left;">
                                                                            Title 6
                                                                        </div>
                                                                    </button></li>
                                                            </ul>
                                                        </div>
                                                    </div>
                                                </li>
                                                <li class="w-md-editor-toolbar-divider"></li>
                                            </ul>
                                            <ul>
                                                <li class="" id="leftView"><button type="button" data-name="edit"
                                                        aria-label="Edit code (ctrl + 7)"
                                                        title="Edit code (ctrl + 7)"><svg width="12" height="12"
                                                            viewBox="0 0 520 520">
                                                            <polygon fill="currentColor"
                                                                points="0 71.293 0 122 319 122 319 397 0 397 0 449.707 372 449.413 372 71.293">
                                                            </polygon>
                                                            <polygon fill="currentColor"
                                                                points="429 71.293 520 71.293 520 122 481 123 481 396 520 396 520 449.707 429 449.413">
                                                            </polygon>
                                                        </svg></button></li>
                                                <li class="active" id="bothView"><button type="button" data-name="live"
                                                        aria-label="Live code (ctrl + 8)"
                                                        title="Live code (ctrl + 8)"><svg width="12" height="12"
                                                            viewBox="0 0 520 520">
                                                            <polygon fill="currentColor"
                                                                points="0 71.293 0 122 179 122 179 397 0 397 0 449.707 232 449.413 232 71.293">
                                                            </polygon>
                                                            <polygon fill="currentColor"
                                                                points="289 71.293 520 71.293 520 122 341 123 341 396 520 396 520 449.707 289 449.413">
                                                            </polygon>
                                                        </svg></button></li>
                                                <li class="" id="rightView"><button type="button" data-name="preview"
                                                        aria-label="Preview code (ctrl + 9)"
                                                        title="Preview code (ctrl + 9)"><svg width="12" height="12"
                                                            viewBox="0 0 520 520">
                                                            <polygon fill="currentColor"
                                                                points="0 71.293 0 122 38.023 123 38.023 398 0 397 0 449.707 91.023 450.413 91.023 72.293">
                                                            </polygon>
                                                            <polygon fill="currentColor"
                                                                points="148.023 72.293 520 71.293 520 122 200.023 124 200.023 397 520 396 520 449.707 148.023 450.413">
                                                            </polygon>
                                                        </svg></button></li>
                                                <li class="w-md-editor-toolbar-divider"></li>
                                                <li class="" id="fullView"><button type="button" data-name="fullscreen"
                                                        aria-label="Toggle fullscreen (ctrl + 0)"
                                                        title="Toggle fullscreen (ctrl+ 0)"><svg width="12" height="12"
                                                            viewBox="0 0 520 520">
                                                            <path fill="currentColor"
                                                                d="M118 171.133334L118 342.200271C118 353.766938 126.675 365.333605 141.133333 365.333605L382.634614 365.333605C394.201281 365.333605 405.767948 356.658605 405.767948 342.200271L405.767948 171.133334C405.767948 159.566667 397.092948 148 382.634614 148L141.133333 148C126.674999 148 117.999999 156.675 118 171.133334zM465.353591 413.444444L370 413.444444 370 471.222222 474.0221 471.222222C500.027624 471.222222 520.254143 451 520.254143 425L520.254143 321 462.464089 321 462.464089 413.444444 465.353591 413.444444zM471.0221 43L367 43 367 100.777778 462.353591 100.777778 462.353591 196.111111 520.143647 196.111111 520.143647 89.2222219C517.254144 63.2222219 497.027624 43 471.0221 43zM57.7900547 100.777778L153.143646 100.777778 153.143646 43 46.2320439 43C20.2265191 43 0 63.2222219 0 89.2222219L0 193.222222 57.7900547 193.222222 57.7900547 100.777778zM57.7900547 321L0 321 0 425C0 451 20.2265191 471.222222 46.2320439 471.222223L150.254143 471.222223 150.254143 413.444445 57.7900547 413.444445 57.7900547 321z">
                                                            </path>
                                                        </svg></button></li>
                                            </ul>
                                        </div>
                                        <div class="w-md-editor-content" id="allArea">
                                            <div class="w-md-editor-area w-md-editor-input">
                                                <div class="w-md-editor-text" style="min-height: 100px;">
                                                    <pre class="w-md-editor-text-pre wmde-markdown-color"></pre>
                                                    <textarea autocomplete="off" autocorrect="off" autocapitalize="off"
                            spellcheck="false" class="w-md-editor-text-input"
                            name="" id="contentInput"></textarea>
                                                </div>
                                            </div>
                                            <div class="w-md-editor-preview" id="previewArea">
                                                <div class="wmde-markdown wmde-markdown-color ">

                                                </div>
                                            </div>
                                        </div>
                                        <div class="w-md-editor-bar"><svg viewBox="0 0 512 512" height="100%">
                                                <path fill="currentColor"
                                                    d="M304 256c0 26.5-21.5 48-48 48s-48-21.5-48-48 21.5-48 48-48 48 21.5 48 48zm120-48c-26.5 0-48 21.5-48 48s21.5 48 48 48 48-21.5 48-48-21.5-48-48-48zm-336 0c-26.5 0-48 21.5-48 48s21.5 48 48 48 48-21.5 48-48-21.5-48-48-48z">
                                                </path>
                                            </svg></div>
                                    </div>
                `;
                markdownContent(chapterIndex,contentIndex);
                break;
        }
    }
}

function addChapter() { //챕터를 추가
    const chapterLocation = document.getElementById('chapterLocation');
    const chapterId = Date.now(); // 챕터 ID를 현재 시간으로 설정(임시)

    const chapterHTML = `
        <div class="flex-col clear-both p-3 m-5 border-2 chapter" id="chapter-${chapterId}" data-chapter-id="${chapterId}">
            <button class="moveContentUp">↑ 위로 이동</button>
            <button class="ml-4 moveContentDown">↓ 아래로 이동</button>
            <button class="float-right mb-3 chapterRemove">
                <img src="../image/DeleteFilled.png" alt="삭제 아이콘" class="inline-block">챕터 삭제
            </button>
            <button class="float-right mb-3 mr-3 chapterAdd">
                <img src="../image/AddFilled.png" alt="추가 아이콘" class="inline-block">콘텐츠 추가
            </button>
            <div class="flex clear-both">
                <div class="w-full p-1 border border-dashed">
                    <input type="text" placeholder="챕터의 제목을 입력해주세요" class="w-full p-1 border chapterTitleInput" data-chapter-id="${chapterId}">
                </div>
                <div class="flex gap-3 ml-2">
                    <!-- 콘텐츠 추가 위치 -->
                </div>
            </div>
            <div id="contentAdd-${chapterId}"></div>
        </div>
    `;

    chapterLocation.insertAdjacentHTML('beforeend', chapterHTML);

    // 새로 추가된 챕터를 formParams에 추가
    formParams.chapter.push({
        id: chapterId,
        name: '', // 제목이 비어있는 상태로 시작
        content: [] // 콘텐츠는 아직 없음
    });

    // 콘솔 로그를 통해 업데이트된 formParams 확인
    console.log('Updated formParams after adding chapter:', formParams);
}

function addContent(e) { //콘텐츠 추가
    if (!e.target.classList.contains('chapterAdd')) return;

    const chapterDiv = e.target.closest('.chapter');
    const chapterId = chapterDiv.id.split('-')[1];
    const contentAdd = chapterDiv.querySelector(`#contentAdd-${chapterId}`);
    const contentCount = contentAdd.querySelectorAll('.groupContent').length + 1;
    const contentId = `${chapterId}-${contentCount}`;

    // 각 타입에 대한 이미지 경로 매핑
    const imagePaths = {
        1: '../image/VideocamFilled.png',
        2: '../image/PictureasPdfFilled.png',
        3: '../image/LinkFilled.png',
        4: '../image/CloudDownloadFilled.png',
        5: '../image/text.png'
    };

    // 기본 타입 (비디오)로 설정
    const defaultType = 1;
    const imagePath = imagePaths[defaultType];

    const contentHTML = `
    <div class="groupContent" id="${contentId}">
        <div class="flex p-2 groupTitle">
            <div>
                <img class="w-[20px] mt-[3px] mr-3" src="${imagePath}" alt="Content Type">
            </div>
            <div class="text-black chapterTitleText">제목을 입력해주세요</div>
            <div class="flex gap-3 ml-auto">
                <button class="inline-block w-6 px-1 border upIcon">↑</button>
                <button class="inline-block w-6 px-1 border downIcon">↓</button>
                <button class="inline-block w-6 px-1 border foldIcon">
                    <img src="../image/arrow-up.png" alt="윗방향화살표" class="inline-block">
                </button>
                <button class="inline-block w-6 border p-[1px] deleteIcon">
                    <img src="../image/DeleteFilled.png" alt="삭제아이콘" class="inline-block">
                </button>
            </div>
        </div>
        <div class="w-[full] bg-[#eee] shadow-sm border rounded-sm top-[10vh] p-2 pt-1 viewContent">
            <div class="flex gap-4 mt-3 mb-3">
                <div>
                    <input type="radio" class="translate-y-[3px] mr-1" id="video-${contentId}" value="1" name="content-${contentId}" checked>
                    <label for="video-${contentId}">동영상</label>
                </div>
                <div>
                    <input type="radio" class="translate-y-[3px] mr-1" id="pdf-${contentId}" value="2" name="content-${contentId}">
                    <label for="pdf-${contentId}">PDF파일</label>
                </div>
                <div>
                    <input type="radio" class="translate-y-[3px] mr-1" id="url-${contentId}" value="3" name="content-${contentId}">
                    <label for="url-${contentId}">Page Link</label>
                </div>
                <div>
                    <input type="radio" class="translate-y-[3px] mr-1" id="file-${contentId}" value="4" name="content-${contentId}">
                    <label for="file-${contentId}">일반 파일</label>
                </div>
                <div>
                    <input type="radio" class="translate-y-[3px] mr-1" id="text-${contentId}" value="5" name="content-${contentId}">
                    <label for="text-${contentId}">텍스트 입력</label>
                </div>
            </div>
            <div class="content-details">
                <div class="pb-2 border-b">
                    <input type="text" class="w-full h-10 p-1 border chapterTitleInput" placeholder="콘텐츠 제목 입력" name="name" value="">
                </div>
                <div class="flex gap-2 pt-1 mt-1 mb-1">
                    <input type="text" class="w-full p-1 border" placeholder="동영상 페이지 url을 입력해주세요" name="content" value="">
                    <button class="p-2 text-white rounded Button_normalSize__custom Button_mainColor__purple" name="urlData" value=""
                        onclick="handleUrlCheckClick(event)">URL 확인</button>
                </div>
                <p class="mt-10 mb-10 leading-8">
                    · 동영상 사이트에서 연결 링크를 복사해 옵니다 (유튜브, 네이버tv) <br>
                    · URL확인을 눌러 링크를 확인해보실 수 있습니다.
                </p>
                <div class="mt-5 mb-5 text-center"></div>
            </div>
        </div>
    </div>
    `;
    
    contentAdd.insertAdjacentHTML('beforeend', contentHTML); // 콘텐츠 추가

    const newContentDiv = contentAdd.querySelector(`.groupContent:last-child`);
    const inputField = newContentDiv.querySelector('.chapterTitleInput');
    const titleDiv = newContentDiv.querySelector('.chapterTitleText');

    inputField.addEventListener('input', function() {
        titleDiv.textContent = this.value || '제목을 입력해주세요';
    });

    // 라디오 버튼 클릭 시 이미지 변경
    const radioButtons = newContentDiv.querySelectorAll('input[type="radio"]');
    radioButtons.forEach(button => {
        button.addEventListener('change', function() {
            const selectedType = this.value;
            const newImagePath = imagePaths[selectedType] || imagePaths[defaultType];
            newContentDiv.querySelector('img').src = newImagePath;
        });
    });


    
}

document.addEventListener('click', function (e) {
    if (e.target.classList.contains('chapterRemove')) removeChapter(e);
    if (e.target.classList.contains('deleteIcon')) removeGroupContent(e);
    if (e.target.closest('.upIcon') || e.target.closest('.downIcon')) clickArrowBtn(e);
    if (e.target.classList.contains('foldIcon')) hiddenContent(e);
    if (e.target.classList.contains('chapterTitleInput')) updateChapterTitle(e);
    if (e.target.type === 'radio') clickRadioBtn(e);
});

function removeChapter(e) { //챕터 삭제
    const chapterDiv = e.target.closest('.chapter');
    chapterDiv.remove();
}

function removeGroupContent(e) { //콘텐츠 삭제
    const groupContentDiv = e.target.closest('.groupContent');
    if (groupContentDiv) groupContentDiv.remove();
}

function hiddenContent(e) { //콘텐츠 숨기기
    // groupContent를 찾을 수 없으면 함수 종료
    const groupContent = e.target.closest('.groupContent');
    if (!groupContent) return; // groupContent가 없는 경우 종료

    if (e.target.classList.contains('foldIcon') || e.target.closest('.foldIcon')) { // foldIcon이 클릭되었는지 확인
        // foldIcon이 속한 groupContent에서 viewContent 요소 찾기
        const groupContent = e.target.closest('.groupContent');
        const viewContent = groupContent.querySelector('.viewContent');
        
        // viewContent의 hidden 클래스 토글
        viewContent.classList.toggle('hidden');
        
        // foldIcon 하위의 img 요소 찾기
        const img = groupContent.querySelector('.foldIcon img');
        
        // hidden 클래스가 있으면 화살표 아래 이미지로, 없으면 원래 이미지로 변경
        if (viewContent.classList.contains('hidden')) {
            img.src = '../image/arrow-under.png';  // hidden 상태에서 아래 화살표 이미지
        } else {
            img.src = '../image/arrow-up.png';  // hidden 상태가 아닐 때 윗방향 화살표 이미지
        }
    }
}

function moveUp(element) {
    const previousElement = element.previousElementSibling;
    if (previousElement) element.parentNode.insertBefore(element, previousElement);
}

function moveDown(element) {
    const nextElement = element.nextElementSibling;
    if (nextElement) element.parentNode.insertBefore(nextElement, element);
}

function clickArrowBtn(e) {
    const groupContent = e.target.closest('.groupContent');
    const chapter = e.target.closest('.chapter');
    if (e.target.classList.contains('upIcon')) moveUp(groupContent);
    else if (e.target.classList.contains('downIcon')) moveDown(groupContent);
    else if (e.target.classList.contains('moveContentUp')) moveUp(chapter);
    else if (e.target.classList.contains('moveContentDown')) moveDown(chapter);
}

function updateChapterTitle(e) {
    const chapterInput = e.target;
    const chapterId = chapterInput.getAttribute('data-chapter-id'); // 챕터 ID 가져오기

    // 입력 필드와 같은 챕터 ID를 가진 제목 요소 찾기
    const chapterDiv = chapterInput.closest('.chapter');
    const chapterTitleText = chapterDiv.querySelector('.chapterTitleText');

    // 제목 텍스트가 없는 경우
    if (chapterTitleText) {
        chapterTitleText.textContent = chapterInput.value || '제목을 입력해주세요';

        // formParams.chapter 배열에서 해당 챕터 ID를 가진 챕터를 찾아서 이름을 업데이트
        const chapter = formParams.chapter.find(chap => chap.id == chapterId);
        if (chapter) {
            chapter.name = chapterInput.value; // 입력된 값으로 name을 업데이트
        }
    } else {
        console.error(`챕터 제목 요소를 찾을 수 없습니다: 챕터 ID ${chapterId}`);
    }
}

// 파일 경로 업데이트 및 formParams 업데이트 함수
// function updateFilePath(fileInput, fileUrl) {
//     const contentDiv = fileInput.closest('.groupContent');
//     if (contentDiv) {
//         const contentId = contentDiv.id; // 각 콘텐츠의 고유 ID
//         const contentType = contentDiv.querySelector('input[type="radio"]:checked')?.value || '';

//         if (contentType === '2' || contentType === '4') { // 파일 타입만 처리
//             // 각 contentId에 파일 URL을 저장하여 덮어쓰지 않도록 한다
//             fileUrls[contentId] = fileUrl;
//             console.log(`파일 경로 업데이트: ${contentId}에 ${fileUrl} 저장됨`);
//         }

//         // 파일 업로드 후 formParams에 파일 경로를 업데이트
//         updateFormParamsWithFilePath(fileInput, fileUrl); // 업로드된 파일 경로를 formParams에 반영
//     }
// }

function updateFilePath(fileInput, fileUrl) {
    const contentDiv = fileInput.closest('.groupContent');
    if (contentDiv) {
        const contentId = contentDiv.id; // 각 콘텐츠의 고유 ID
        const contentType = contentDiv.querySelector('input[type="radio"]:checked')?.value || '';

        if (contentType === '2' || contentType === '4') { // 파일 타입만 처리
            // 각 contentId에 파일 URL을 저장하여 덮어쓰지 않도록 한다
            fileUrls[contentId] = fileUrl;
            console.log(`파일 경로 업데이트: ${contentId}에 ${fileUrl} 저장됨`);

            // 기존의 data-url 값이 있으면 덮어쓰고, 없으면 새로 추가
            if (fileInput.hasAttribute('data-url')) {
                fileInput.setAttribute('data-url', fileUrl); // data-url 값 덮어쓰기
            } else {
                fileInput.setAttribute('data-url', fileUrl); // data-url 새 값 추가
            }
        }

        // 파일 업로드 후 formParams에 파일 경로를 업데이트
        updateFormParamsWithFilePath(fileInput, fileUrl); // 업로드된 파일 경로를 formParams에 반영
    }
}



function handleFileUpload(event) {
    const fileInput = event.target;
    const file = fileInput.files[0];

    if (file) {
        const formData = new FormData();
        formData.append('file', file);

        $.ajax({
            url: '/codeenator/api/file/upload.do?user_agent=web',
            type: 'POST',
            data: formData,
            processData: false,
            contentType: false,
            dataType: 'JSON',
            success: function(data) {
                const fileUrl = `${data.resultMap.upload_path}/${data.resultMap.stored_name}`;
                updateFilePath(fileInput, fileUrl); // 파일 경로 업데이트
            },
            error: function(err) {
                console.error('파일 업로드 중 오류 발생:', err);
            }
        });
    }
}

//formParams의 파일 경로 업데이트 함수


function updateFormParamsWithFilePath(fileInput, uploadedFilePath) {
    const contentDiv = fileInput.closest('.groupContent');
    const chapterDiv = fileInput.closest('.chapter');

    if (!chapterDiv) {
        console.error('챕터를 찾을 수 없습니다.');
        return;
    }

    const chapterId = chapterDiv.id.split('-')[1]; // chapterDiv의 id에서 chapterId 추출
    const chapter = formParams.chapter.find(chap => chap.id == chapterId);
    if (!chapter) {
        console.error(`formParams에서 챕터 ID ${chapterId}를 찾을 수 없습니다.`);
        return;
    }

    if (!contentDiv) {
        console.error('groupContent를 찾을 수 없습니다.');
        return;
    }

    // 콘텐츠의 위치를 정확하게 찾기 위한 index 계산
    const contentIndex = Array.from(chapterDiv.querySelectorAll('.groupContent')).indexOf(contentDiv);

    if (chapter.content[contentIndex]) {
        // 업로드된 파일 경로를 content 필드에 저장
        chapter.content[contentIndex].file = uploadedFilePath;
        console.log(`chapter ${chapterId}의 content[${contentIndex}]에 파일 경로가 업데이트되었습니다.`);
    } else {
        console.error(`contentIndex ${contentIndex}에 해당하는 콘텐츠가 없습니다.`);
    }

    // 파일 업로드 후 generateArray 호출
    generateArray(); // updateFormParams 이후 호출하여 formParams 업데이트
}


function contentFileUpload(formData, callback) {
    $.ajax({
        type: 'post',
        url: '/codeenator/api/file/upload.do?user_agent=web',
        data: formData,
        dataType: 'JSON',
        processData: false,
        contentType: false,
        success: function (data) {
            console.log('File upload success response:', data); // Log the entire response

            const uploadedFilePath = data.resultMap.upload_path +'/' + data.resultMap.stored_name;
            if (callback) {
                callback(uploadedFilePath);
            }
        },
        error: function (err) {
            console.error('File upload error:', err);
        }
    });
}

function markdownContent(chapterIndex, contentIndex) {

    const contentInputId = `contentInput`;
    const contentInput = document.getElementById(contentInputId);
    const italicIcon = document.getElementById('italicIcon');
    const slashIcon = document.getElementById('slashIcon');
    const colonIcon = document.getElementById('colonIcon');
    const titleIcon = document.getElementById('titleIcon');
    const titleGroupIcon = document.getElementById('titleGroupIcon');
    const previewArea = document.getElementById('previewArea');

    if (!contentInput || !italicIcon || !slashIcon || !colonIcon || !titleIcon || !titleGroupIcon || !previewArea) {
        console.error('필요한 요소를 찾을 수 없습니다.');
        return;
    }

    italicIcon.addEventListener('click', () => applyStyle('italic'));
    slashIcon.addEventListener('click', () => applyStyle('code'));
    colonIcon.addEventListener('click', () => applyStyle('quote'));

    titleIcon.addEventListener('click', () => {
        titleGroupIcon.style.display = titleGroupIcon.style.display === 'none' ? 'block' : 'none';
    });

    titleGroupIcon.querySelectorAll('button').forEach(button => {
        button.addEventListener('click', (event) => {
            const titleId = event.currentTarget.getAttribute('data-name');
            const level = parseInt(titleId.replace('title', ''), 10);
            applyStyle(`title${level}`);
            titleGroupIcon.style.display = 'none';
        });
    });

    function applyStyle(style) {
        const start = contentInput.selectionStart;
        const end = contentInput.selectionEnd;
        const content = contentInput.value;
        const selectedText = content.substring(start, end);

        let newContent;
        if (style === 'italic') {
            newContent = `${content.substring(0, start)}*${selectedText}*${content.substring(end)}`;
        } else if (style === 'code') {
            newContent = `${content.substring(0, start)}\`${selectedText}\`${content.substring(end)}`;
        } else if (style === 'quote') {
            newContent = `${content.substring(0, start)}> ${selectedText}${content.substring(end)}`;
        } else if (style.startsWith('title')) {
            const level = parseInt(style.replace('title', ''), 10);
            newContent = `${content.substring(0, start)}${'#'.repeat(level)} ${selectedText}${content.substring(end)}`;
        }

        contentInput.value = newContent;
        contentInput.setSelectionRange(start, start + newContent.length - content.length + selectedText.length);
        updatePreview();
    }

    function updatePreview() {
        const markdownContent = contentInput.value;
        previewArea.innerHTML = marked(markdownContent);

        // Markdown 콘텐츠를 formParams에 업데이트
        if (!formParams.chapter[chapterIndex]) {
            formParams.chapter[chapterIndex] = { content: [] };
        }

        if (!formParams.chapter[chapterIndex].content[contentIndex]) {
            formParams.chapter[chapterIndex].content[contentIndex] = {};
        }

        formParams.chapter[chapterIndex].content[contentIndex] = {
            type: '5', // 'markdown' 타입으로 설정
            name: formParams.chapter[chapterIndex].content[contentIndex]?.name || '',
            content: contentInput.value
        };

        console.log("현재 formParams 업데이트:", formParams);
    }

    contentInput.addEventListener('input', updatePreview);
}


//------------------------------------------3Page--------------------------------------------------//
function clickTermCheck() { // 마지막 페이지 동의 폼
if(form_value === 3){
    const termAll = document.getElementById('termAll'); // 모두 동의
    const check_agree = document.getElementById('check_agree'); // 첫번째 동의
    const check_copyright = document.getElementById('check_copyright'); // 두번째 동의

    if (termAll && check_agree && check_copyright) {
        // 모두 동의가 체크되면 두 개의 체크박스 모두 체크
        termAll.addEventListener('change', () => {
            if (termAll.checked) {
                check_agree.checked = true;
                check_copyright.checked = true;
            } else {
                check_agree.checked = false;
                check_copyright.checked = false;
            }
        });

        // 첫번째 동의 또는 두번째 동의가 체크될 때
        check_agree.addEventListener('change', () => {
            if (check_agree.checked && check_copyright.checked) {
                termAll.checked = true;
            } else {
                termAll.checked = false;
            }
        });

        check_copyright.addEventListener('change', () => {
            if (check_agree.checked && check_copyright.checked) {
                termAll.checked = true;
            } else {
                termAll.checked = false;
            }
        });
    }

    // termAll이 체크되었는지 여부를 반환
        return termAll.checked;
        }
}

// //------------------------------------------공통부분----------------------------------------------//
document.addEventListener('DOMContentLoaded', () => {  //페이지가 로드된 후 이벤트 리스너 추가
    styleCategory(); // 페이지 로드 시 선택된 카테고리 스타일 적용
    getAlbum();
    if(form_value ===2){

        }
    document.addEventListener('click', clickBtn); //페이지 이동 버튼 이벤트
    document.addEventListener('click', clickCategory); //카테고리 클릭 이벤트
    document.addEventListener('click', clickTermCheck); //3페이지 동의 클릭 이벤트



    document.addEventListener('click', (e) => { //2페이지 챕터 및 콘텐츠 클릭 이벤트
        if (e.target.id === 'addContentBtn') {
            addChapter(); // 챕터 추가
        } else if (e.target.classList.contains('chapterAdd')) {
            addContent(e); // 콘텐츠 추가
        } else if (e.target.classList.contains('chapterRemove')) {
            removeChapter(e); // 챕터 삭제
        } else if (e.target.classList.contains('deleteIcon') || e.target.closest('.deleteIcon')) {
            removeGroupContent(e); // 콘텐츠 삭제
        } else if (e.target.type === 'radio') {
                clickRadioBtn(e); // 라디오 버튼 클릭 이벤트
        }
    });

    document.addEventListener('click', hiddenContent); //콘텐츠 숨기기
    document.addEventListener('click', clickArrowBtn); //콘텐츠 순서 바꾸기

    const fileInput = document.getElementById('thumbnailBtn');


    if (fileInput) {
        // 파일 입력 요소에 change 이벤트 리스너를 추가합니다
        fileInput.addEventListener('change', addThumbnail);
    }

    
});


function modifyAlbum(albumData) { //앨범 수정하기
    $.ajax({
        type: 'POST',
        url: `/codeenator/api/album/modifyAlbum.do?user_agency=web&id=${id_value}`,
        data: JSON.stringify(albumData),
        contentType : 'application/json',
        dataType: 'json',
        success: function (data) {
            console.log(data);
            if(data.result === "success"){
                alert('앨범이 수정되었습니다.')
                location.href = '/codeenator/page/albumList.do';
            } else {
                alert(data.msg);
            }
        },
        error: function (err) {
            console.error("실패: ", err);
            
        }
    });
}

function updateFormParams() {

    formParams.chapter = Array.from(document.querySelectorAll('.chapter')).map(chapterDiv => {
        const chapterId = chapterDiv.id.split('-')[1];
        const chapterName = chapterDiv.querySelector('.chapterTitleInput')?.value || '챕터명';

        const contents = Array.from(chapterDiv.querySelectorAll('.groupContent')).map(contentDiv => {
            const contentType = contentDiv.querySelector('input[type="radio"]:checked')?.value || '';
            const contentTitle = contentDiv.querySelector('.chapterTitleInput')?.value || '';
            let contentPath = '';

            const contentId = contentDiv.id;

            if (['2', '4'].includes(contentType)) { // 파일 타입
                contentPath = fileUrls[contentId] || ''; // fileUrls에서 파일 URL을 가져옵니다.
            } else {
                contentPath = contentDiv.querySelector('input[name="content"]')?.value || '';
            }

            // 저장할 키가 contentType에 따라 달라짐
            let contentKey = ['1', '3', '5'].includes(contentType) ? 'content' : 'file';

            return {
                type: contentType,
                name: contentTitle,
                [contentKey]: contentPath // contentType에 맞는 key를 동적으로 할당
            };
        });

        return {
            id: chapterId,
            name: chapterName,
            content: contents
        };
    });

    console.log("현재 챕터 정보 저장:", formParams);
}


function saveCurrentPageData() {
    generateArray(); // 현재 페이지의 데이터를 formParams에 저장

    // 페이지별로 필요한 데이터를 저장
    localStorage.setItem(`page${form_value}`, JSON.stringify(getPageData(form_value)));
}


function generateArray() {
    if (form_value === 2) { // 2페이지에서만 챕터 데이터 업데이트
        const chapters = document.querySelectorAll('.chapter');

        if (chapters.length === 0) {
            return;
        }

        formParams.chapter = Array.from(chapters).map(chapterDiv => {
            const chapterId = chapterDiv.id.split('-')[1];
            const chapterName = chapterDiv.querySelector('.chapterTitleInput')?.value || '챕터명';

            const contents = Array.from(chapterDiv.querySelectorAll('.groupContent')).map(contentDiv => {
                const contentType = contentDiv.querySelector('input[type="radio"]:checked')?.value || '';
                const contentTitle = contentDiv.querySelector('.chapterTitleInput')?.value || '';
                const contentId = contentDiv.id;

                let contentPath = '';

                if (['2', '4'].includes(contentType)) { // 파일 타입
                    const fileInput = contentDiv.querySelector('input[type="file"]');
                    if (fileInput) {
                        const filePath = fileInput.getAttribute('data-url'); // value 속성에서 파일 경로 가져옴
                        contentPath = filePath || fileUrls[contentId] || ''; // input의 value가 있으면 contentPath에 넣고, 없으면 fileUrls에서 가져옴.
                    } else {
                        contentPath = fileUrls[contentId] || ''; // fileInput이 없으면 fileUrls에서 가져옴
                    }
                } else if (contentType === '5') { // Text type
                    contentPath = contentDiv.querySelector('#contentInput')?.value || '';
                } else { // Other types (1, 3)
                    contentPath = contentDiv.querySelector('input[name="content"]')?.value || '';
                }

                // 저장할 키가 contentType에 따라 달라짐
                let contentKey = ['1', '3', '5'].includes(contentType) ? 'content' : 'file';

                return {
                    id: contentId,
                    type: contentType,
                    name: contentTitle,
                    [contentKey]: contentPath // contentType에 맞는 key를 동적으로 할당
                };
            });

            return {
                id: chapterId,
                name: chapterName,
                content: contents
            };
        });

        console.log("현재 챕터 정보 저장:", formParams);
    }
}


function getPageData(pageNumber) {
    if (pageNumber === 1) {
        return {
            name: formParams.name,
            introduction: formParams.introduction,
            thumbnail: formParams.thumbnail,
            category: formParams.category,
            tag: formParams.tag
        };
    } else if (pageNumber === 2) {
        return {
            chapter: formParams.chapter
        };
    } else if (pageNumber === 3) {
        return {
            name: formParams.name,
            introduction: formParams.introduction,
            chapter: formParams.chapter,
            thumbnail: formParams.thumbnail,
            category: formParams.category,
            tag: formParams.tag
        };
    }
}

// 기본 formParams 구조를 반환하는 함수
function getDefaultFormParams() {
    return {
        "name": "",
        "introduction": "",
        "chapter": [],
        "thumbnail": "",
        "category": "",
        "tag": "",
        "pk": ""
    };
}


function clickBtn(e) {

    // 페이지 이동 전에 현재 페이지 데이터를 저장
    saveCurrentPageData();

    if (e.target.id === 'preBtn') {  
        form_value--; // 페이지값 감소
        generateArray(); // 폼을 재생성하여 데이터를 반영
        createForm(formParams); // 폼을 재생성하여 데이터를 반영

    } else if (e.target.id === 'nextBtn') { 
        form_value++; // 페이지값 증가
        generateArray(); // 폼을 재생성하여 데이터를 반영
        createForm(formParams); // 폼을 재생성하여 데이터를 반영

    }

    if (e.target.id === 'submitBtn') {
        if (!clickTermCheck()) {
            alert("모든 약관에 동의해야 앨범을 생성할 수 있습니다.");
            return; 
        }
        if(private_value === undefined){
            formParams.pk = '';
        } else {
            formParams.pk = private_value;
        }

        formParams.chapter.forEach(function(chapter) {
            // ID가 13자리 숫자인 경우 해당 필드를 삭제
            if (typeof chapter.id === 'string' && chapter.id.length === 13 && !isNaN(chapter.id)) {
                chapter.id = ""; // id 필드를 삭제
            }

            // content 배열을 순회하면서 ID에 '-'가 있는 경우 해당 ID를 빈 문자열로 변경
            chapter.content.forEach(function(content) {
                if (content.id.includes('-')) {
                    content.id = ""; // ID를 빈 문자열로 변경
                }
            });
        });

        console.log(formParams);
        modifyAlbum(formParams); // 최종 제출

    }
}

