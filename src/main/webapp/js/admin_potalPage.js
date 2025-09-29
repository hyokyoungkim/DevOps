
const bannerImg1Text = document.getElementById('bannerImg1Text'); //url input박스
const bannerImg2Text = document.getElementById('bannerImg2Text');
const bannerImg3Text = document.getElementById('bannerImg3Text');
const bannerImg1 = document.getElementById('bannerImg1'); //이미지 미리보기 div
const bannerImg2 = document.getElementById('bannerImg2');
const bannerImg3 = document.getElementById('bannerImg3');
const popupEnableStatus = document.getElementById('popupEnableStatus'); //팝업 활성화 여부
const popupWidth = document.getElementById('popupWidth'); //팝업 너비
const popupHeight = document.getElementById('popupHeight'); //팝업 높이
const popupText = document.getElementById('popupText'); //팝업 텍스트박스
const saveBtn = document.getElementById('saveBtn'); //저장 버튼

let page_value = {
    "banner": [],
    "popup_enable": "",
    "popup_width": "",
    "popup_height": "",
    "popup_content":""
}


function getPotalPage() { //배너 조회
    $.ajax({
        type: 'GET',
        url: '/codeenator/api/admin/portal/getSetting.do?user_agency=web',
        dataType: 'json', 
        success: function (data) {
            if(data.result === 'success'){
                setCurrentInfo(data.resultMap)
                page_value = {
                "banner": data.resultMap.banner,
                "popup_enable": data.resultMap.enable_popup,
                "popup_width": data.resultMap.popup_width,
                "popup_height": data.resultMap.popup_height,
                "popup_content":data.resultMap.popup_content
}  
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

function setCurrentInfo(info) {
    console.log(info)
    bannerImg1Text.value = info.banner[0].link;
    bannerImg2Text.value = info.banner[1].link;
    bannerImg3Text.value = info.banner[2].link;
    bannerImg1.style.backgroundImage = info.banner[0].file;
    bannerImg2.style.backgroundImage = info.banner[1].file;
    bannerImg3.style.backgroundImage = info.banner[2].file;
    popupEnableStatus.value = info.enable_popup;
    popupWidth.value = info.popup_width;
    popupHeight.value = info.popup_height;
    popupText.value = info.popup_content;
    bannerImg1.src = `/codeenator/${info.banner[0].file}`
    bannerImg2.src = `/codeenator/${info.banner[1].file}`
    bannerImg3.src = `/codeenator/${info.banner[2].file}`
}



saveBtn.addEventListener('click', savePortalInfo)

bannerImg1Text.addEventListener('change', function () {
    page_value.banner[0].link = bannerImg1Text.value;
})
bannerImg2Text.addEventListener('change', function () {
    page_value.banner[1].link = bannerImg2Text.value;
})
bannerImg3Text.addEventListener('change', function () {
    page_value.banner[2].link = bannerImg3Text.value;
})
popupEnableStatus.addEventListener('change', function () {
    page_value.popup_enable = popupEnableStatus.value;
})
popupWidth.addEventListener('change', function () {
    page_value.popup_width = popupWidth.value;
})
popupHeight.addEventListener('change', function () {
    page_value.popup_height = popupHeight.value;
})
popupText.addEventListener('change', function () {
    page_value.popup_content = popupText.value;
})
const bannerImg1Btn = document.getElementById('bannerImg1Btn'); //파일 선택 버튼
const bannerImg2Btn = document.getElementById('bannerImg2Btn');
const bannerImg3Btn = document.getElementById('bannerImg3Btn');
bannerImg1Btn.addEventListener('change', (e) => addImg(e, 0));  // 각 버튼에 인덱스를 넘김
bannerImg2Btn.addEventListener('change', (e) => addImg(e, 1));
bannerImg3Btn.addEventListener('change', (e) => addImg(e, 2));






function fileUpload(formData, index) {
    $.ajax({
        type: 'POST',
        url: '/codeenator/api/file/upload.do?user_agent=web',
        data: formData,
        dataType: 'JSON',
        processData: false,
        contentType: false,
        success: function (data) {
                console.log(data.resultMap)
            if (data.result === 'success' && data.resultMap && data.resultMap.stored_name) {
                // 서버 응답에 따라 페이지 값 업데이트
                page_value.banner[index].file = `${data.resultMap.upload_path}/${data.resultMap.stored_name}`;
                console.log(`Updated page_value: `, page_value);
            }
        },
        error: function (err) {
            console.error(err);
        }
    });
}


// 이미지 미리보기 및 파일 업로드 함수
function addImg(e, index) {
    const files = e.target.files;
    if (files.length > 0) {
        const file = files[0];
        const formData = new FormData();
        formData.append('file', file);

        // FileReader 객체 생성 및 파일 미리보기
        const reader = new FileReader();
        reader.onload = function (event) {
            const imageUrl = event.target.result;

            // 이미지 미리보기 설정
            if (index === 0) {
                bannerImg1.src = imageUrl;
            } else if (index === 1) {
                bannerImg2.src = imageUrl;
            } else if (index === 2) {
                bannerImg3.src = imageUrl;
            }
        };
        reader.readAsDataURL(file);

        // 파일 업로드
        fileUpload(formData, index);
    } else {
        console.log('No file selected');
    }
}



function savePortalInfo() { //저장 버튼 클릭

    console.log(page_value)
    
        $.ajax({
        type: 'POST',
        url: `/codeenator/api/admin/portal/setSetting.do?user_agency=web`,
        data: JSON.stringify(page_value),
        contentType: 'application/json',
        dataType: 'json',
        success: function (data) {
            console.log(data);
            alert(data.msg);
        },
        error: function (err) {
            console.error("실패: ", err);
        }
    });
}