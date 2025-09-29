<!DOCTYPE html>
<%@page contentType="text/html" pageEncoding="UTF-8" isELIgnored="false" %>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Codeenator</title>
    <link rel="stylesheet" href="${servletPath}/css/output.css">
    <link rel="stylesheet" href="${servletPath}/font/font.css">
    <link rel="shortcut icon" href="${servletPath}/image/favicon/favicon.ico">
</head>

<body style="overflow: visible;">
    <div>
        <main>
            <div class="min-h-screen pt-14">
                <div class="fixed top-0 w-full" id="header">

                    <button class="absolute float-right w-7 right-2 top-2">
                        <img src="${servletPath}/image/hamburger.png" alt="hamburger"></button>
                    <div class="h-10 mr-4 text-2xl font-bold leading-10 bg-white cursor-pointer"
                        onclick="window.history.back()">←스크롤 형식으로 보기</div>
                    <div class="w-full h-10 p-3 text-white bg-black" id="albumName">앨범 이름</div>
                    <button
                        class="text-lg border m-2 w-20 h-8 border-white text-white fixed  top-[36px] right-24 rounded-full cursor-pointer bg-black hidden"
                        id="prevBtn">PREV</button>
                    <button
                        class="text-lg border m-2 w-20 h-8 border-white text-white fixed top-[36px] right-0 rounded-full cursor-pointer bg-black hidden"
                        id="nextBtn">NEXT</button>
                </div>
                <div class="mt-7" id="contentView">
                </div>
            </div>
        </main>
    </div>

    <script src="https://code.jquery.com/jquery-3.4.1.js"></script>
    <script src="${servletPath}/js/albumViewDetail_slider.js"></script>
</body>

</html>