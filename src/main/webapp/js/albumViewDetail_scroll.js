const urlCopyBtn = document.getElementById('urlCopy');
const url = new URL(window.location.href); //url값 저장
let url_value = url.searchParams.get("id") || ""; // URL 파라미터에서 'id' 값 추출
let id_value = url_value.substring(0, 6);  // 첫 6글자
let pk_value = url_value.length > 6 ? url_value.substring(6, 12) : "";
let recommend_value = { "status": "" };
const originalPathData = 'm1692.48 910.647-732.762 687.36-731.182-685.779c-154.616-156.875-154.616-412.122 0-568.997 74.542-75.558 173.704-117.233 279.304-117.233h.113c105.487 0 204.65 41.675 279.078 117.233l.113.113c74.767 75.783 116.103 176.865 116.103 284.385h112.941c0-107.52 41.224-208.602 116.104-284.498 74.428-75.558 173.59-117.233 279.19-117.233h.113c105.487 0 204.763 41.675 279.19 117.233 154.617 156.875 154.617 412.122 1.695 567.416m78.833-646.701c-95.887-97.355-223.737-150.89-359.718-150.89h-.113c-136.094 0-263.83 53.535-359.604 150.777-37.61 38.061-68.443 80.979-92.16 127.398-23.718-46.42-54.664-89.337-92.16-127.285-95.774-97.355-223.51-150.89-359.605-150.89h-.113c-135.981 0-263.83 53.535-359.83 150.89-197.648 200.696-197.648 526.983 1.694 729.035l810.014 759.868L1771.313 991.4c197.647-200.47 197.647-526.758 0-727.454';
const newPathData = 'M1771.731 291.037C1675.709 193.659 1547.944 140 1411.818 140h-.113c-136.125 0-263.777 53.66-359.573 150.924-37.618 38.07-68.571 80.997-92.294 127.426-23.61-46.429-54.563-89.356-92.068-127.313C771.86 193.659 644.208 140 507.97 140h-.113c-136.012 0-263.777 53.66-359.8 151.037-197.691 200.629-197.691 527.103 1.695 729.088l810.086 760.154 811.893-761.736c197.692-200.403 197.692-526.877 0-727.506';
const bookIcon = document.getElementById('bookIcon');
const user_commentInput = document.getElementById('user_commentInput');


function recommendAlbum(recommendNumber) {
    $.ajax({
        type: 'POST',
        url: `/codeenator/api/album/recommendAlbum.do?user_agency=web&id=${id_value}`,
        data: JSON.stringify(recommendNumber),
        contentType : 'application/json',
        dataType: 'json', 
        success: function (data) {
            if (data.msg === "앨범 추천을 취소하였습니다.") {
                
            } else if (data.msg === "앨범을 추천하였습니다.") {
                
            }
        },
        error: function (err) {
            console.error("실패: ", err);

        }
    });
}

function getAlbum() {
    $.ajax({
        type: 'GET',
        url: `/codeenator/api/album/getAlbum.do?user_agent=web&id=${id_value}`,
        dataType: 'json', 
        success: function (data) {
            console.log(data)
            createTitle(data.resultMap);
            createContent(data.resultMap.chapter);
            createSideMenu(data.resultMap.chapter);
            getRecommendStatus(data.resultMap.enable_recommend);
        },
        error: function (err) {
            console.error("요청 실패: ", err);
            if (err.responseText) {
                console.error("응답 텍스트: ", err.responseText);
            }
        }
    });
}
getAlbum();




function createTitle(content) {
    const contentThumbnail = document.getElementById('contentThumbnail');
    const contentTitle = document.getElementById('contentTitle');

    let textThumbnail = '';
    let textAlbum = '';
    let textEmail=''
    textThumbnail = ` <img src="/codeenator/${content.thumbnail}" alt="" class="h-full"></img>` //@@ img
    contentThumbnail.innerHTML = textThumbnail;
    if (pk_value !== '') {
        textAlbum += `
        <div class="absolute cursor-pointer top-4" style="right:4.5rem">
            <img src="../image/lock.png" alt="lock" style="width:40px">
        </div>
        `
    }
    if (user === 1 || admin ===2) { //로그인 상태일 때 좋아요버튼 생성

        textAlbum += `<div class="absolute cursor-pointer top-4 right-5">
                    <svg fill="red" width="40" height="40" viewBox="0 0 1920 1920" xmlns="http://www.w3.org/2000/svg" id="heartIcon">
                                        <path
                                            d="m1692.48 910.647-732.762 687.36-731.182-685.779c-154.616-156.875-154.616-412.122 0-568.997 74.542-75.558 173.704-117.233 279.304-117.233h.113c105.487 0 204.65 41.675 279.078 117.233l.113.113c74.767 75.783 116.103 176.865 116.103 284.385h112.941c0-107.52 41.224-208.602 116.104-284.498 74.428-75.558 173.59-117.233 279.19-117.233h.113c105.487 0 204.763 41.675 279.19 117.233 154.617 156.875 154.617 412.122 1.695 567.416m78.833-646.701c-95.887-97.355-223.737-150.89-359.718-150.89h-.113c-136.094 0-263.83 53.535-359.604 150.777-37.61 38.061-68.443 80.979-92.16 127.398-23.718-46.42-54.664-89.337-92.16-127.285-95.774-97.355-223.51-150.89-359.605-150.89h-.113c-135.981 0-263.83 53.535-359.83 150.89-197.648 200.696-197.648 526.983 1.694 729.035l810.014 759.868L1771.313 991.4c197.647-200.47 197.647-526.758 0-727.454"
                                            fill-rule="evenodd" id="heartColor"></path>
                                    </svg>
                                </div>
                                `
    }



    textAlbum += `
                <h4 class="text-sm">${content.category}</h4>
                <h2 class="mb-3 text-2xl font-bold">${content.name}</h2>
                <p class="mb-2 max-h-[100px] overflow-auto tablet:max-h-full">${content.introduction}</p>
                <div class="text-xl "><span class="text-[#9333EA]">${content.user_nickname}</span> 코디네이터 <br></div>`

        if (!(content.user_tag === "")) { //user_tag의 값이 빈 값이 아닐 때만 실행
            let userTagsArray = (content.user_tag).split(',');
            for (let i = 0; i < userTagsArray.length; i++){
                textAlbum +=
                                        `<div class="inline-block pl-2 pr-2 mr-1 text-xs text-blue-900 border border-blue-900 rounded-xl m2">
                                            <p class="inline-block break-all "># ${userTagsArray[i]}</p>
                                        </div>`
                                        }
        }
                    
    textAlbum +=`
                <div class="absolute left-0 flex gap-3 bottom-3 tablet:flex-col tablet:static tablet:my-3">
                <div><button class="p-2 text-white rounded Button_wideSize__custom Button_subColor__custom" id="urlCopy">링크 복사</button>
                </div>`
    if (user === 1 || admin === 1) {
        textAlbum += `<div>
            <button class="rounded p-2 text-white Button_wideSize__custom Button_grayColor"  id="sendEmailBtn">링크 이메일 전송</button>
            </div>
        `
    }
    textAlbum+=  `
                </div>
                `
    contentTitle.innerHTML = textAlbum;

}

function createContent(data) {
    const content = document.getElementById('content');
    textContent = '';


    for (let i = 0; i < data.length; i++) {
        textContent += `<div class="border border-[#333] p-1 mb-[15vh]" data-id="${data[i].id}">
                    <div class="leading-[50px] h-[50px]  p-2 text-lg items-center sticky top-[60px]  bg-white border-b">
                        <div>
                            <h2 class="text-center mb-5 text-[#9333EA] text-2xl">Chapter ${i + 1} 
                            <span class="text-center  text-[#333]">${data[i].name}</span></h2>
                        </div>
        `

        for (let j = 0; j < (data[i].content).length; j++) {
            textContent += `
                                </div>
                            <div class="p-2 py-5 border shadow-md album-content" data-id="${data[i].content[j].id}">
                                <div class="p-2 cursor-pointer bg-slate-300">
                                    <h3 class="my-2 text-lg font-bold text-center album-content-title">${i + 1}-${j + 1}, ${data[i].content[j].name}</h3>
                                </div>
            `

            switch (data[i].content[j].type) {
                case 1:
                    textContent +=`<div data-vjs-player="true">`
                    let siteUrl = '';
                    let videoId = '';
                    if (data[i].content[j].link.includes('youtu')) { //유튜브인지 확인
                        if (data[i].content[j].link.includes('shorts')) {  //숏츠면
                            videoId = data[i].content[j].link.split('shorts/');
                        }else {
                            videoId = data[i].content[j].link.split('v='); //일반 영상이면
                            }
                        siteUrl = `https://www.youtube.com/embed/${videoId[1]}`
                    textContent += `<iframe title="Youtube" src="${siteUrl}?autoplay=0" width="100%" height="800" frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe>`
                    } else if(data[i].content[j].link.includes('tv.naver')) { //네이버 tv
                        videoId = data[i].content[j].link.split('/v/');
                        siteUrl = `https://tv.naver.com/v/${videoId[1]}`
                        textContent +=`<iframe title="Naver TV Video" src="${siteUrl}" frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture" allowfullscreen width="100%" height="800"></iframe>`
                    } else {
                        console.log('네이버/유튜브 외 허용을 안 합니다 ^^')
                    }   
                    textContent += `</div>`
                    break;
                
                case 2:
                    textContent += `
                    <div>
                        <div><embed src="/codeenator${data[i].content[j].link}" width="100%" height="800px"></div>
                    </div>
                    `
                    break;
                
                case 3:
                    textContent += `
                <div class="m-auto text-center"><h3 class="text-xl">새 탭에서 링크 열기<br><br></h3>
                <div class=" break-all">${data[i].content[j].link}</div></div>
                <div class="mt-5 text-center"><button class="rounded p-2 text-white Button_normalSize__custom Button_subColor__custom" id="linkBtn_${data[i].content[j].id}" data-link="${data[i].content[j].link}">go</button></div>
                    `
                    break;
                
                case 4:
                    textContent += `
                    <div class="m-3 text-center">
                        <p class="text-lg ">${data[i].content[j].name}</p>
                    <div>
                    ${data[i].content[j].link.endsWith('.pdf') 
                        ? `<embed src="/codeenator${data[i].content[j].link}" width="100%" height="800px">` 
                        : `<embed src="/codeenator/${data[i].content[j].link}" alt="미리보기" class="m-auto">`
                    }
                    <span>(미리보기)</span>
                    </div>
                    <button class="block p-2 m-auto mb-5 font-bold underline" id="downloadBtn_${data[i].content[j].id}" data-download="${data[i].content[j].link}">파일 다운로드(클릭)
                    </button>
                    </div>
                    `;
                    break;
                
                case 5:
                    const convertedHTML = markdownToHTML(data[i].content[j].link);

                    textContent += `
                    <div class="mb-10 p-3 w-full [&amp;_p]:whitespace-pre-line [&amp;_p]:break-all wmde-markdown wmde-markdown-color ">
                        ${convertedHTML} 
                    </p>
                    </div>
                    `
                    break;

            }
        }
        textContent += `</div></div>`
    }
    content.innerHTML = textContent;
    //↑콘텐츠 생성

}

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



document.addEventListener('DOMContentLoaded', function () {
    
    document.addEventListener('click', copyLink);//카테고리 이동 이벤트
    document.addEventListener('click', slideMenu); //오른쪽메뉴 클릭 이벤트
    document.addEventListener('click', moveContent);
    document.addEventListener('click', clickHeartIcon);
    document.addEventListener('click', clickLinkBtn);
    document.addEventListener('click', clickDownloadBtn);
    document.addEventListener('click', sendLink);
    document.addEventListener('click', clickBtn);

})

function sendLink(e) {

    const sendEmailPopup = document.getElementById('sendEmailPopup')
    const sendEmailInput = document.getElementById('sendEmailInput')

    
    if (e.target.id === 'sendEmailBtn') { //링크 이메일 전송 버튼 클릭
    sendEmailInput.value ='' //input 비우기
    sendEmailPopup.classList.remove('hidden'); //팝업 표시

    }
    if (e.target.id === 'sendEmailCancelBtn') { //취소 버튼 클릭
    sendEmailPopup.classList.add('hidden'); //팝업 숨김
    }
    if (e.target.id === 'sendEmailOkBtn') { //확인 버튼 클릭
        let email_form = {"email":sendEmailInput.value, "url":url.href}

    $.ajax({
        type: 'POST',
        url: `/codeenator/api/album/shareLink.do?user_agency=web&id=${id_value}`,
        data: JSON.stringify(email_form),
        contentType : 'application/json',
        dataType: 'json', 
        success: function (data) {
            alert(data.msg)
            location.reload();
        },
        error: function (err) {
            console.error("실패: ", err);

        }
    });

    }
}

function copyLink(e) {
    if (e.target.id === 'urlCopy') {

            // 임시 텍스트 영역 생성
            const tempInput = document.createElement('input');
            tempInput.value = url.href;
            document.body.appendChild(tempInput);

            // 텍스트 영역의 내용 선택 및 복사
            tempInput.select();
            document.execCommand('copy');

            // 임시 텍스트 영역 제거
            document.body.removeChild(tempInput);

            // 복사 완료 알림
            alert('URL이 복사되었습니다.');
    }
}

function createSideMenu(data) {
    const sideMenu = document.getElementById('sideMenu');
    let textMenu = '';

    textMenu += `
    <div class="absolute flex right-[-300px] ease-in duration-300" id="menuContent">
        <div class="cursor-pointer w-[25px] bg-slate-300 text-sm h-[40px] pt-1 text-white mt-10px text-center rounded-l-lg" id=menuBtn>◀</div>
        <div class="h-full bg-white border">
            <ul class="Remote">`;

    for (let i = 0; i < data.length; i++) {
        textMenu += `<li class="text-lg font-bold bg-gray-100 font-lg">${data[i].name}<ul>`;
        for (let j = 0; j < data[i].content.length; j++) {
            textMenu += `<li class="w-[300px] cursor-pointer p-1 h-[30px] hover:font-bold overflow-hidden text-ellipsis whitespace-nowrap font-normal text-base bg-white" data-id="${data[i].content[j].id}">${data[i].content[j].name}</li>`;
        }
        textMenu += `</ul></li>`;
    }

    textMenu += `</ul></div></div>`;

    sideMenu.innerHTML = textMenu;

}

function slideMenu(e) { //좌측 슬라이드 메뉴 펼치기/닫기
    const menuBtn = document.getElementById('menuBtn');
    const menuContent = document.getElementById('menuContent');
    const sideMenu = document.getElementById('sideMenu');

    if (e.target.id === 'menuBtn') {
        if (menuBtn.textContent === '◀') {
        menuBtn.textContent = '▶';
        sideMenu.className = 'fixed top-[250px] right-[50px] w-[350px] overflow-hidden  overflow-y-auto ease-in duration-300 min-h-[40px] max-h-[60vh] h-full tablet:top-[80px] tablet:right-0';
        menuContent.className = 'absolute flex right-[0] ease-in duration-300';
    } else {
        menuBtn.textContent = '◀';
        sideMenu.className = 'fixed top-[250px] right-[50px] w-[40px] overflow-hidden false ease-in duration-300 min-h-[40px] max-h-[60vh] h-full tablet:top-[80px] tablet:right-0';
        menuContent.className = 'absolute flex right-[-300px] ease-in duration-300';
    }
}

}

function moveContent(e) { //slideMenu에서 이동하고 싶은 챕터 부분 클릭시 화면 스크롤 이동
    const target = e.target;

    if (target.tagName === 'LI' && target.hasAttribute('data-id')) {
        const dataId = target.getAttribute('data-id');
        const targetDiv = document.querySelector(`.album-content[data-id="${dataId}"]`);              
        if (targetDiv) {
            targetDiv.scrollIntoView({ behavior: 'smooth', block: 'start' });
        }
    }
}


function getRecommendStatus(status) { //페이지가 로드된 후 현재 추천 상태
    const heartColor = document.getElementById('heartColor');

    if (status === 1) {
        heartColor.setAttribute('d', newPathData); //추천
        recommend_value = { "status": "true" };
        recommendAlbum(recommend_value);
    }
}

function clickHeartIcon(e) {
    const heartColor = document.getElementById('heartColor');

    
    if (e.target.id === 'heartIcon' || e.target.id === 'heartColor') {
            const currentPathData = heartColor.getAttribute('d');
        if (currentPathData === newPathData) {
            heartColor.setAttribute('d', originalPathData); //추천해제
            recommend_value = { "status": "false" };
            recommendAlbum(recommend_value);
        } else {
            heartColor.setAttribute('d', newPathData); //추천
            recommend_value = { "status": "true" };
            recommendAlbum(recommend_value);
        }
    }
}

function clickLinkBtn(e) {
    if (e.target.id.startsWith('linkBtn')) { 
        const link = e.target.getAttribute('data-link');
        window.open(link); 
    }

};

function getFileExtension(filename) { //확장자 추출
    const dotIndex = filename.lastIndexOf('.');
    if (dotIndex === -1 || dotIndex === 0 || dotIndex === filename.length - 1) {
        return ''; 
    }    
    return filename.substring(dotIndex + 1).toLowerCase();
}


function clickDownloadBtn(e) {
    if (e.target.id.startsWith('downloadBtn')) {
        const fileUrl = "/codeenator/"+e.target.getAttribute('data-download');
        downloadFile(fileUrl);
    }
}


async function downloadFile(url) {
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


bookIcon.addEventListener('click', function () {
    location.href=`/codeenator/page/albumViewDetail_slider.do?id=${id_value}`;
})


getUser().done(function () {  // Ajax 요청이 완료된 후 바로 실행

    if (user === 1 || admin === 1) {
        user_commentInput.classList.remove('hidden');
    } else {
        console.log('로그인 상태가 아님');
    }
});



function getComment() {
    $.ajax({
        type: 'GET',
        url: `/codeenator/api/album/getCommentList.do?user_agent=web&album=${id_value}`,
        dataType: 'json', 
        success: function (data) {
            createComment(data.resultList)
        },
        error: function (err) {
            console.error("요청 실패: ", err);
            if (err.responseText) {
                console.error("응답 텍스트: ", err.responseText);
            }
        }
    });
}
getComment();

function createComment(comment_list) {

    const user_commentView = document.getElementById('user_commentView');
    let commentText = '';

    comment_list.forEach(comment => {
        commentText += `
        <div class="w-full p-3 mt-5 mb-10 border rounded shadow-sm">
        <div class="relative mb-5" id="userComment-${comment.seq}">`
        if (user_name === comment.user_nickname || admin === 1) {
            commentText += ` 
                <div class="flex gap-2 absolute right-3 text-grey ">
                    <span class="cursor-pointer"  id="deleteBtn-${comment.seq}">삭제</span>
                </div>`
        }
        commentText += `<p class="text-sm text-gray-400">${comment.datetime}</p>
            <p class="block mb-2 font-semibold">${comment.user_nickname}</p>
            <p>${comment.comment}</p>
        </div>`

        if (user === 1 || admin === 1) {
            commentText += `
            <div class="gap-3 overflow-hidden">
                <textarea rows="3" class="resize-none p-2 border w-full" id="recommentInput-${comment.seq}"></textarea>
                <div class="float-right">
                    <button class="rounded p-2 text-white Button_normalSize__custom Button_subColor__custom" id="recommentBtn-${comment.seq}">등록</button>
                </div>
            </div>
            
            `
        };

        if (comment.reply.length > 0) { //리플의 값이 빈값이 아니라면 실행됨      
            comment.reply.forEach(adminReply => {
            commentText += `
            <div class="relative mt-4 mb-2 bg-zinc-100 p-3 rounded shadow" id="adminReply-${adminReply.seq}">`
                
                if (adminReply.user_nickname === user_name || admin === 1) { //댓글을 작성한 본인 또는 관리자일 경우 삭제 버튼이 보임
            commentText += `    
                <div class="absolute flex gap-2 right-3 text-grey" >
                    <span class="cursor-pointer" id="deleteBtn-${adminReply.seq}">삭제</span>
                </div>`
        }
            commentText +=    `<p class="text-gray-400 text-sm">${adminReply.datetime}</p>
                <p class="font-semibold block mb-2">${adminReply.user_nickname}</p>
                <p>${adminReply.comment}</p>
            </div>
            `
            });
        }


        commentText += `</div>`
    })
            user_commentView.innerHTML = commentText;
}



function clickBtn(e) {
    if (e.target.id === 'registrationBtn') {
        registerComment();
    }

    if (e.target.id.startsWith('deleteBtn')) {
        const parts = e.target.id.split('-');
        const idNumber = parts[1];
        console.log(idNumber);
        deleteComment(idNumber);
    }

    if (e.target.id.startsWith('recommentBtn-')) {
        const parts = e.target.id.split('-');
        const idNumber = parts[1]; // 버튼의 ID에서 숫자 추출

        // 해당 숫자와 일치하는 input 박스의 값을 가져오기
        const inputBox = document.getElementById(`recommentInput-${idNumber}`);
        if (inputBox) {
            const commentValue = inputBox.value;

            // 댓글 등록 함수 호출
            registerReComment(idNumber, commentValue);
        } 
    }

}

function registerComment() { //댓글작성

    const commnetInputBox = document.getElementById('commnetInputBox')
    let comment_value = { "comment": commnetInputBox.value }
    
        console.log(comment_value)
        $.ajax({
        type: 'POST',
        url: `/codeenator/api/album/setComment.do?user_agency=web&album=${id_value}`,
        data: JSON.stringify(comment_value),
        contentType : 'application/json',
        dataType: 'json', 
        success: function (data) {
            if (data.result === "success") {
                alert(data.msg)
                location.reload();
            } else {
                alert(data.msg)
            }
        },
        error: function (err) {
            console.error("실패: ", err);

        }
    });
    
}


function deleteComment(comment_value) {
    console.log(comment_value)

        $.ajax({
        type: 'POST',
        url: `/codeenator/api/album/deleteComment.do?user_agency=web&seq=${comment_value}`,
        contentType: 'application/json',
        dataType: 'json', 
        success: function (data) {
            console.log(data);
            if(data.result === 'success'){
                alert(data.msg);
                location.reload(); // 성공 시 페이지 새로고침
            } else {
                alert(data.msg);
            }
        },
        error: function (err) {
            console.error("실패: ", err);
        }
        });
    
}

function registerReComment(seq, comment) {
    const commentData = {
        "comment": comment,
        "reply": seq
    };
    console.log(commentData)

    $.ajax({
        type: 'POST',
        url: `/codeenator/api/album/setComment.do?user_agency=web&album=${id_value}`,
        data: JSON.stringify(commentData),
        contentType: 'application/json',
        dataType: 'json',
        success: function (data) {
            console.log(data);
            if (data.result === 'success') {
                alert(data.msg);
                location.reload(); // 성공 시 페이지 새로고침
            } else {
                alert(data.msg);
            }
        },
        error: function (err) {
            console.error("실패: ", err);
        }
    });
}
