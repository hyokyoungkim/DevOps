let page_value = 1;

document.addEventListener('DOMContentLoaded', () => {
    getMyAlbumCommentList(); // 페이지가 로드되면 댓글 리스트 불러옴
    document.addEventListener('click', clickBtn); //클릭이벤트
});

function getMyAlbumCommentList() { 
    $.ajax({
        type: 'GET',
        url: `/codeenator/api/album/getMyAlbumCommentList.do?user_agency=web&page=${page_value}`,
        dataType: 'json', 
        success: function (data) {
            createMyAlbumCommentList(data.resultList);
        },
        error: function (err) {
            console.error("요청 실패: ", err);
            if (err.responseText) {
                console.error("응답 텍스트: ", err.responseText);
            }
        }
    });
}

function createMyAlbumCommentList(list_value) {
    const commentList = document.getElementById('commentList');
    let commentText = '';

    if (Array.isArray(list_value) && list_value.length > 0) {
        list_value.forEach(comment => {
            commentText += `
                <tr role="row" id="albumId-${comment.album}">
                    <td role="cell" class="pr-5 pl-5 pt-2 pb-2 border-b min-w-[150px]">${comment.album_name}</td>
                    <td role="cell" class="pr-5 pl-5 pt-2 pb-2 border-b min-w-[150px]">${comment.user_nickname}</td>
                    <td role="cell" class="pr-5 pl-5 pt-2 pb-2 border-b min-w-[150px]">${comment.datetime}</td>
                    <td role="cell" class="pr-5 pl-5 pt-2 pb-2 border-b min-w-[150px]" id="commentId-${comment.seq}">${comment.comment}</td>`;

            if (comment.reply === null) {
                commentText += `
                    <td role="cell" class="pr-5 pl-5 pt-2 pb-2 border-b min-w-[150px]"></td>
                    <td class="pt-2 pb-2 pl-5 pr-5 border-b">
                        <button class="p-2 text-white rounded Button_normalSize__custom Button_subColor__custom" id="commentRegisterBtn-${comment.seq}-${comment.album}">답글 등록</button>
                    </td>`;
            } else {
                commentText += `
                    <td role="cell" class="pr-5 pl-5 pt-2 pb-2 border-b min-w-[150px]" id="replyId-${comment.reply.seq}">${comment.reply.comment}</td>
                    <td class="pt-2 pb-2 pl-5 pr-5 border-b">
                        <button class="p-2 text-white rounded Button_normalSize__custom Button_subColor__custom" id="commentModifyBtn-${comment.reply.seq}">답글 편집</button>
                    </td>`;
            }

            commentText += `</tr>`;
        });
    }
    commentList.innerHTML = commentText;
}

function clickBtn(e) {
    const commentPopup = document.getElementById('commentPopup');
    const commentCancelBtn = document.getElementById('commentCancelBtn');
    const commentOkBtn = document.getElementById('commentOkBtn');

    // 취소 이벤트 리스너 등록
    commentCancelBtn.addEventListener('click', cancelPopup);

    if (e.target.id.startsWith('commentRegisterBtn')) { // 답글 등록 클릭
        commentPopup.classList.remove('hidden'); // 팝업 보임
        const parts = e.target.id.split('-');
        const commentSeq = parts[1]; // 댓글번호
        const albumISeq = parts[2]; // 앨범 ID

        // 확인 버튼 클릭
        commentOkBtn.onclick = function() {
            console.log(albumISeq + ',' + commentSeq);
            registerComment(albumISeq, commentSeq);
        };
    }

    if (e.target.id.startsWith('commentModifyBtn')) { // 답글 편집 클릭
        commentPopup.classList.remove('hidden');
        const parts = e.target.id.split('-');
        const commentSeq = parts[1];

        // 확인 버튼 클릭 이벤트 리스너 등록
        commentOkBtn.onclick = function() {
            modifyComment(commentSeq);
        };
    }
}

function cancelPopup() {
    const commentPopup = document.getElementById('commentPopup');
    commentPopup.classList.add('hidden');
}

function registerComment(album_id, comment_seq) { // 댓글 작성
    const commentInput = document.getElementById('commentInput');
    let comment_value = { "comment": commentInput.value, "reply": comment_seq };

    $.ajax({
        type: 'POST',
        url: `/codeenator/api/album/setComment.do?user_agency=web&album=${album_id}`,
        data: JSON.stringify(comment_value),
        contentType: 'application/json',
        dataType: 'json', 
        success: function (data) {
            if (data.result === "success") {
                alert(data.msg);
                location.reload();
            } else {
                alert(data.msg);
            }
        },
        error: function (err) {
            console.error("실패: ", err);
        }
    });
}

function modifyComment(comment_seq) {
    const commentInput = document.getElementById('commentInput');
    let comment_value = { "comment": commentInput.value };

    $.ajax({
        type: 'POST',
        url: `/codeenator/api/album/modifyComment.do?user_agency=web&seq=${comment_seq}`,
        data: JSON.stringify(comment_value),
        contentType: 'application/json',
        dataType: 'json', 
        success: function (data) {
            if (data.result === "success") {
                alert(data.msg);
                location.reload();
            } else {
                alert(data.msg);
            }
        },
        error: function (err) {
            console.error("실패: ", err);
        }
    });
}
