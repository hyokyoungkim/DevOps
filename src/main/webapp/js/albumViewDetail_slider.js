const urlCopyBtn = document.getElementById('urlCopy');
const url = new URL(window.location.href); //url값 저장
const id_value = url.href.split('id=')[1];
console.log(id_value)
let albumList = []
let idArr = [];
let nameArr = [];
let currentPageNumber = 0;
let lastPageNumber;
let albumTitleName;
let content_id;
let current_id_value = 0;

function getAlbum() { //앨범 정보 가져오기
    $.ajax({
        type: 'GET',
        url: `/codeenator/api/album/getAlbum.do?user_agent=web&id=${id_value}`,
        dataType: 'json', 
        success: function (data) {
            sortId(data.resultMap.chapter); 
            albumTitleName = data.resultMap.name; //앨범 이름
            albumName.innerText = `${albumTitleName} / ${data.resultMap.chapter[0].content[0].name}`
            content_id = data.resultMap.chapter[0].content[0].id;
            getContent(content_id)
                if (idArr.length === 1) { //idArr의 길이가 1일 때는 다음 버튼 숨김
                nextBtn.classList.add('hidden');
                }
        },
        error: function (err) {
            console.error("요청 실패: ", err);
            if (err.responseText) {
                console.error("응답 텍스트: ", err.responseText);
            }
        }
    });
}
getAlbum(); //초기실행


function sortId(albumList) { //id값이랑 name값 추출
    albumList.forEach(albumContent => {
        for (let i = 0; i < albumContent.content.length; i++){ //챕터->콘텐츠의 길이만큼 반복해서 id값과 name값을 저장
            idArr.push(albumContent.content[i].id)
            nameArr.push(albumContent.content[i].name)
        }
    });
    lastPageNumber = nameArr.length; //총 페이지수
}

function getContent(ID) { //콘텐츠 정보 가져오기
    $.ajax({
        type: 'GET',
        url: `/codeenator/api/album/getContent.do?user_agent=web&id=${ID}`,
        dataType: 'json', 
        success: function (data) {
            createContent(data.resultMap) //콘텐츠 생성
        },
        error: function (err) {
            console.error("요청 실패: ", err);
            if (err.responseText) {
                console.error("응답 텍스트: ", err.responseText);
            }
        }
    });
}

function createContent(list) {

    const contentView = document.getElementById('contentView')
    let textContent = '';
    switch (list.type) { //1:동영상, 2:pdf, 3:링크, 4:파일, 5:텍스트
        case 1:
            textContent += `
            <div>
            <div class="m-auto player-wrapper">
                <div class="react-player" poster="" style="width: 100%; height: 94vh;">
                            <div style="width: 100%; height: 100%;">
                            <iframe frameborder="0" allowfullscreen=""
                        allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share"
                        referrerpolicy="strict-origin-when-cross-origin" title="YouTube video player" width="100%"
                        height="100%"
            `
                    let siteUrl = '';
                    let videoId = '';
                    if (list.link.includes('youtu')) { //유튜브인지 확인
                        if (list.link.includes('shorts')) {  //숏츠면
                            videoId = list.link.split('shorts/');
                        }else {
                            videoId = list.link.split('v='); //일반 영상이면
                            }
                        siteUrl = `https://www.youtube.com/embed/${videoId[1]}`
                    textContent += `<iframe frameborder="0" allowfullscreen=""
                        allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share"
                        referrerpolicy="strict-origin-when-cross-origin" title="YouTube video player" width="100%"
                        height="100%" src="${siteUrl}"></iframe>`
                    } else if(list.link.includes('tv.naver')) { //네이버 tv
                        videoId = list.link.split('/v/');
                        siteUrl = `https://tv.naver.com/v/${videoId[1]}`
                        textContent +=`<iframe title="video" src="${siteUrl}" width="100%" height="100%" do-not-allow="autoplay"></iframe>`
                    } else {
                        console.log('네이버/유튜브 외 허용을 안 합니다 ^^')
                    }   
            textContent += `</div></div>
                    </div></div>`
            break;
        case 2:
            textContent += `
                <div><embed src="/codeenator${list.link}" width="100%" height="900px"></div>
            `
            break;
        case 3:
            // console.log(data[i].content[j].id)
            textContent += `
                <div class="m-auto text-center"><h3 class="text-xl">새 탭에서 링크 열기<br><br></h3>
                <div class=" break-all">${list.link}</div>
                    <button class="rounded p-2 text-white Button_normalSize__custom Button_subColor__custom" id="linkBtn_${list.id}"  onclick="window.open('${list.link}', '_blank');">go</button>
                </div>

                <div class="mt-5 text-center">
                
                </div>
                    `
            break;
        case 4:
            textContent += `
                    <div class="m-3 text-center">
        <p class="text-lg "></p>
        <div><embed src="/codeenator${list.link}" 
                alt="미리보기" class="m-auto"><span>(미리보기)</span>
                </div> 
                <button class="block p-2 m-auto mb-5 font-bold underline" id="downloadBtn_${list.id}" data-download="${list.link}">파일 다운로드(클릭)</button></div>  
            `
            break;
        case 5:
            const convertedHTML = markdownToHTML(list.link); //linkl안에 있는 마크다운 변형 전 값을 마크다운 형식으로 변환
                    textContent += `
                    <div class="mb-10 p-3 w-full [&amp;_p]:whitespace-pre-line [&amp;_p]:break-all wmde-markdown wmde-markdown-color ">
                        ${convertedHTML}
                    </p>
                    </div>
                    `
            break;
    }
    contentView.innerHTML = textContent;
    }


const prevBtn = document.getElementById('prevBtn');
const nextBtn = document.getElementById('nextBtn');
const albumName = document.getElementById('albumName');



function clickBtnEvent() { //이전,다음 버튼 클릭 이벤트

    if (currentPageNumber === 0) { //이전 버튼
        prevBtn.classList.add('hidden'); // 현재 페이지가 첫 페이지일 경우 이전 버튼 숨기기
    } else {
        prevBtn.classList.remove('hidden'); //이전 버튼 보이기
    }


    if (currentPageNumber >= lastPageNumber - 1) { //다음버튼
        nextBtn.classList.add('hidden'); // 현재 페이지가 마지막 페이지일 경우 다음 버튼 숨기기
    } else {
        nextBtn.classList.remove('hidden'); // 다음 버튼 보이기
    }

    albumName.innerText = `${albumTitleName} / ${nameArr[currentPageNumber]}`
}


prevBtn.addEventListener('click', function () { //이전버튼 클릭
    if (currentPageNumber > 0) { // 현재 페이지가 0보다 클 때만 페이지 감소
        currentPageNumber--;
        current_id_value--;
    }
    console.log(idArr[current_id_value])
    getContent(idArr[current_id_value])
    clickBtnEvent();
});


nextBtn.addEventListener('click', function () { //다음버튼 클릭
    if (currentPageNumber < lastPageNumber - 1) { // 현재 페이지가 마지막 페이지보다 작을 때만 페이지 증가
        currentPageNumber++;
        current_id_value++;
    }
        console.log(idArr[current_id_value])
        getContent(idArr[current_id_value])
    clickBtnEvent();
});

clickBtnEvent(); //초기 버튼 세팅

async function downloadFile(url) { //file 다운로드
    try {
        const response = await fetch(url);

        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }
        const blob = await response.blob();
        const objectUrl = URL.createObjectURL(blob);
        const aTag = document.createElement('a');
        aTag.href = objectUrl;
        aTag.download = url.split('/').pop(); 
        document.body.appendChild(aTag);
        aTag.click();
        document.body.removeChild(aTag);
        URL.revokeObjectURL(objectUrl);
    } catch (error) {
        console.error('파일 다운로드 중 오류 발생:', error);
    }
}

function clickDownloadBtn(e) { //@@경로 수정
    if (e.target.id.startsWith('downloadBtn')) {
        const fileUrl = "/codeenator"+e.target.getAttribute('data-download');
        downloadFile(fileUrl);
    }
}

document.addEventListener('DOMContentLoaded', function () {
    document.addEventListener('click', clickDownloadBtn); //다운로드 버튼 클릭 이벤트

})

function markdownToHTML(markdown) { //case 5일 때 마크다운으로 변환
    return markdown
        .replace(/\*\*([^*]+)\*\*/g, '<strong>$1</strong>') // Bold
        .replace(/\*([^*]+)\*/g, '<i>$1</i>')              // Italic
        .replace(/`([^`]+)`/g, '<code>$1</code>')          // Inline code
        .replace(/> (.+)/g, '<blockquote>$1</blockquote>') // Blockquote
        .replace(/^(#{1}) (.+)$/gm, '<h1>$2</h1>')         // Heading 1
        .replace(/^(#{2}) (.+)$/gm, '<h2>$2</h2>')         // Heading 2
        .replace(/^(#{3}) (.+)$/gm, '<h3>$2</h3>')         // Heading 3
        .replace(/^(#{4}) (.+)$/gm, '<h4>$2</h4>')         // Heading 4
        .replace(/^(#{5}) (.+)$/gm, '<h5>$2</h5>')         // Heading 5
        .replace(/^(#{6}) (.+)$/gm, '<h6>$2</h6>')         // Heading 6
        .replace(/\n/g, '<br/>');                          // Line breaks
}