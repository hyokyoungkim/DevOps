let form = {
    title: "",
    content: "",
    file: [] // 여러 개의 파일을 저장할 배열
};

const addNoticeBtn = document.getElementById('addNoticeBtn');
    const titleInput = document.getElementById('titleInput');
    const contentInput = document.getElementById('contentInput');
    const previewArea = document.getElementById('previewArea');
    const fileInput = document.getElementById('fileInput');
    const formContent = document.getElementById('formContent');

// 파일 선택 시 자동으로 업로드 실행
fileInput.addEventListener('change', function() {
    if (fileInput.files.length > 0) {
        uploadFiles(Array.from(fileInput.files)).then(fileNames => {
            // 파일 이름을 기존 form.file 배열에 추가
            form.file = form.file.concat(fileNames);
            console.log("파일 업로드 완료, 파일 이름: ", form.file);
            
        }).catch(err => {
            console.error("파일 업로드 실패:", err);
        });
    }
});

addNoticeBtn.addEventListener('click', function() {
    form.title = titleInput.value;
    
    const markdownText = contentInput.value;
    const htmlContent = marked(markdownText);      
    previewArea.innerHTML = htmlContent;
    form.content = htmlContent;


        if (fileInput.files.length === 0 && form.file.length === 0) {
            form.file = []; // 파일이 선택되지 않았으면 빈 배열로 설정
            createNotice(form);
        } else if (form.file.length === 0) {
            // 파일 업로드가 완료되지 않았으면 대기
            console.log("파일 업로드 진행 중...");

            let checkUploadStatus = setInterval(() => {
                if (form.file.length > 0) {
                    clearInterval(checkUploadStatus);
                    createNotice(form);
                }
            }, 1000); // 업로드 상태를 주기적으로 확인
        } else {
            createNotice(form);
        }

});

function uploadFiles(files) {
    return new Promise((resolve, reject) => {
        let fileNames = [];
        let filesToUpload = files.length;
        let uploadedFiles = 0;

        const fileNameUl = document.getElementById('fileNameUl'); 

        files.forEach(file => {
            const formData = new FormData();
            formData.append('file', file);

            $.ajax({
                type: 'POST',
                url: '/codeenator/api/file/upload.do?user_agent=web',
                data: formData,
                dataType: 'json',
                processData: false,
                contentType: false,
                success: function(data) {
                    console.log(data.resultMap.name);
                    fileNames.push(`${data.resultMap.upload_path}/${data.resultMap.stored_name}`); // 파일 경로 추가

                    const li = document.createElement('li');
                    li.className = 'float-left mr-5 cursor-pointer';
                    li.textContent = data.resultMap.name; // 업로드된 파일의 이름 표시
                    
                    // 파일 이름 리스트에 추가
                    fileNameUl.appendChild(li);

                    uploadedFiles++;
                    if (uploadedFiles === filesToUpload) {
                        resolve(fileNames);
                    }
                },
                error: function(err) {
                    reject(err);
                }
            });
        });
    });
}

function createNotice(form) { 
    $.ajax({
        type: 'POST',
        url: '/codeenator/api/board/setNotice.do?user_agency=web',
        data: JSON.stringify(form),
        contentType: 'application/json',
        dataType: 'json',
        success: function(data) {
            console.log(data);
            alert('공지사항이 등록되었습니다.');
            location.href = '/codeenator/page/board_notice.do';
        },
        error: function(err) {
            console.error("실패: ", err);
        }
    });
}

    // <li class="float-left mr-5 cursor-pointer">let-it-go.pdf</li>
    // <li class="float-left mr-5 cursor-pointer">When you wish upon a star.pdf</li>
