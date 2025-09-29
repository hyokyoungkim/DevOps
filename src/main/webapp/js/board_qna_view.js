const url = new URL(window.location.href); //url값 저장
const seq_value = url.href.split('seq=')[1]; //?제거
let comment_page = 1;


function getQnA() {
        $.ajax({
        type: 'GET',
        url: `/codeenator/api/board/getQuestion.do?user_agent=web&seq=${seq_value}`,
        dataType: 'json',
            success: function (data) {
            createQnA(data.resultMap);
        },
        error: function (err) {
            console.error("요청 실패: ", err);
            if (err.responseText) {
                console.error("응답 텍스트: ", err.responseText);
            }
        }
    });
}
getQnA()
function createQnA(list) {

    const notice_form = document.getElementById('notice_form');
    if (!notice_form) {
        console.error("Element with ID 'notice_form' not found.");
        return;
    }

    let textList = `
        <div class="m-3 mt-10 border-b">
            <b class="text-lg">${list.title}</b> <br>
            <span class="text-sm text-gray-500">${list.datetime}</span>
            <span class="ml-5 text-sm text-gray-500">${list.user_nickname}</span>
        </div>
        <div class="m-3 min-h-[300px]">
            <div class="mb-10">${list.content}</div>
        </div>`
    if(list.file){
        if (Array.isArray(list.file) && list.file.length > 0) {
        textList += `
        <div class="m-3">
        <b>첨부파일</b>`;

        list.file.forEach(file => {

        textList += `
        <div>
            <a href="/codeenator/${file.path}" download="${file.name}">${file.name}</a>
        </div>`;
    });


    textList += `</div>`;
        }
    }

        textList+=`<div class="flex justify-center gap-3 mb-[50px]">
            <button class="p-2 text-white rounded Button_normalSize__custom Button_subColor__custom"
                                    onclick="history.back()">목록</button>`

    if (list.user_nickname === user_name || admin === 1) {
    textList += `<button class="rounded p-2 text-white Button_normalSize__custom Button_grayColor " id=pageDeleteBtn-${seq_value}>삭제</button>`
    }
    
        textList +=  `</div>`

    notice_form.innerHTML = textList;
}




function getComment() {
    $.ajax({
        type: 'GET',
        url: `/codeenator/api/board/getCommentList.do?user_agent=web&content=${seq_value}&page=${comment_page}`,
        dataType: 'json',
        success: function (data) {
            createComment(data.resultList);
        },
        error: function (err) {
            console.error("요청 실패: ", err);
            if (err.responseText) {
                console.error("응답 텍스트: ", err.responseText);
            }
        }
    });
}
getComment(); //코멘트 목록 불러오기

function createComment(list) {
    const commentList = document.getElementById('commentList');
    let commentText = '';

    list.forEach(comment => {

        commentText += `
            <div class="border shadow-sm p-2 mt-3">
                <div class="relative mb-5">`
        if (comment.user_nickname === user_name || admin === 1) {
            commentText += `    
                <div class="absolute flex gap-2 right-3 text-grey" >
                    <span class="cursor-pointer" id="deleteBtn-${comment.seq}">삭제</span>
                </div>`
        }
        commentText +=  `<p class="text-sm text-gray-400">${comment.datetime}</p>
                <p class="block mb-2 font-semibold">${comment.user_nickname}</p>
                <p>${comment.comment}</p>
                </div>
            ` 
        if (user === 1 || admin === 1) {
            commentText += `
            <div class="gap-3 overflow-hidden">
                <textarea rows="3" class="resize-none p-2 border w-full" id="recommentInput-${comment.seq}"></textarea>
                <div class="float-right">
                    <button class="rounded p-2 text-white Button_normalSize__custom Button_subColor__custom" id="recommentBtn-${comment.seq}">등록</button>
                </div>
            </div>
            
            `
        }
        




        if (comment.reply.length > 0) {         
            comment.reply.forEach(adminReply => {
            commentText += `
            <div class="relative mt-4 mb-2 bg-zinc-100 p-3 rounded shadow" id="adminReply-${adminReply.seq}">`
                
                if (adminReply.user_nickname === user_name || admin === 1) {
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
        commentText+=`</div>`
    });
    commentList.innerHTML = commentText;
}



const commentArea = document.getElementById('commentArea');

getUser().done(function () {     // Ajax 요청이 완료된 후 바로 실행

    if (user === 1 || admin === 1) {
        commentArea.classList.remove('hidden');
    } else {
        console.log('로그인 상태가 아닐 때는 댓글을 입력할 수 없습니다.');
    }
});

function registerComment() {
    const commentBox = document.getElementById('commentBox');
    let comment_value = {"comment": commentBox.value};
    console.log(comment_value)

    $.ajax({
        type: 'POST',
        url: `/codeenator/api/board/setComment.do?user_agency=web&content=${seq_value}`,
        data: JSON.stringify(comment_value),
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
        console.log('Button ID number:', idNumber);

        // 해당 숫자와 일치하는 input 박스의 값을 가져오기
        const inputBox = document.getElementById(`recommentInput-${idNumber}`);
        if (inputBox) {
            const commentValue = inputBox.value;
            console.log('Input value:', commentValue);

            // 댓글 등록 함수 호출
            registerReComment(idNumber, commentValue);
        }
    }


            if (e.target.id.startsWith('pageDeleteBtn-')) {
            const parts = e.target.id.split('-');
            const idNumber = parts[1];
            deletePage(idNumber)
    }
}

function deleteComment(comment_value) {
    
        $.ajax({
        type: 'POST',
        url: `/codeenator/api/board/deleteComment.do?user_agency=web&seq=${comment_value}`,
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

    $.ajax({
        type: 'POST',
        url: `/codeenator/api/board/setComment.do?user_agency=web&content=${seq_value}`,
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




function deletePage(seq) {
    
        $.ajax({
        type: 'POST',
        url: `/codeenator/api/board/deleteQuestion.do?user_agency=web&seq=${seq}`,
        contentType: 'application/json',
        dataType: 'json', 
        success: function (data) {
            console.log(data);
            if(data.result === 'success'){
                alert(data.msg);
                location.href=`/codeenator/page/board_qna.do`;
            } else {
                alert(data.msg);
            }
        },
        error: function (err) {
            console.error("실패: ", err);
        }
        });
    
}

