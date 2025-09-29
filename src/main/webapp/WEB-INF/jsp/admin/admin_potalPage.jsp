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
        <link rel="stylesheet" href="https://unpkg.com/swiper/swiper-bundle.min.css">
    </head>

    <body>
        <div>
            <header class="Header_header__custom">
                <div class="m-auto desktop:max-w-7xl laptop:max-w-4xl tablet:max-w-3xl">
                    <div class="float-left Header_logo__custom">
                        <div class="cursor-pointer">
                            <div class="mt-2 hover:cursor-pointer"><img src="${servletPath}/image/logo.png" alt="logo"
                                    onclick="location.href='${servletPath}/main.do';"></div>
                        </div>
                    </div>
                    <nav>
                        <ul class="hidden float-left ml-3 desktop:flex ">
                            <li class="m-5 font-bold hover:cursor-pointer text-slate-700 ">
                                <div class="cursor-pointer" onclick="location.href='${servletPath}/page/albumList.do';">
                                    앨범 보기</div>
                            </li>
                            <li class="m-5 font-bold hover:cursor-pointer text-slate-700 ">
                                <div id="myAlbumList">내 앨범 보기</div>
                            </li>
                            <li class="m-5 font-bold hover:cursor-pointer text-slate-700 ">
                                <div class="cursor-pointer"
                                    onclick="location.href='${servletPath}/page/serviceInformation.do';">서비스 소개
                                </div>
                            </li>
                            <li class="m-5 font-bold hover:cursor-pointer text-slate-700 ">
                                <div class="cursor-pointer"
                                    onclick="location.href='${servletPath}/page/board_notice.do';">게시판</div>
                            </li>
                        </ul>
                    </nav>
                    <nav>

                        <section id="userLogin">
                        </section>

                        <button class="absolute hidden laptop:block desktop:hidden top-4 right-4"><svg
                                xmlns="http://www.w3.org/2000/svg" class="w-6 h-6" fill="none" viewBox="0 0 24 24"
                                stroke="currentColor" id="openBtn">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    d="M4 6h16M4 12h16M4 18h16">
                                </path>
                            </svg></button>


                        <nav class="hidden desktop:hidden fixed top-0 right-0 bg-[#efefef] border border-r h-[100vh] w-[50vw]"
                            id="menuSpace">
                            <button class="absolute top-4 right-4" id="closeBtn"><svg xmlns="http://www.w3.org/2000/svg"
                                    class="w-6 h-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                        d="M6 18L18 6M6 6l12 12">
                                    </path>
                                </svg></button>
                            <ul class="absolute top-10">
                                <li class="m-5 font-bold text-slate-700 "
                                    onclick="location.href='${servletPath}/page/albumList.do';">
                                    <div class="cursor-pointer">앨범 보기</div>
                                </li>
                                <li class="m-5 font-bold text-slate-700 "
                                    onclick="location.href='${servletPath}/page/board_notice.do';">
                                    <div class="cursor-pointer">게시판</div>
                                </li>
                                <li class="m-5 font-bold text-slate-700 "
                                    onclick="location.href='${servletPath}/page/serviceInformation.do';">
                                    <div class="cursor-pointer">서비스 소개</div>
                                </li>
                                <li class="m-5 font-bold text-slate-700" id="loginMenu">
                                </li>
                            </ul>
                        </nav>


                    </nav>
                </div>
            </header>

            <main>

                <div class="min-h-screen pt-14">
                    <div class="desktop:max-w-7xl laptop:max-w-4xl tablet:max-w-3xl m-auto flex pt-5 mb-[100px]">
                        <nav class="min-w[150px] h-full border shadow rounded-sm w-40 tablet:w-full border-none">
                            <ul class="tablet:hidden">
                                <li class="p-2 cursor-pointer"
                                    onclick="location.href='${servletPath}/page/admin_user.do';">
                                    <div class="cursor-pointer">회원 관리</div>
                                </li>
                                <li class="p-2 cursor-pointer"
                                    onclick="location.href='${servletPath}/page/admin_board.do';">
                                    <div class="cursor-pointer">게시글 관리</div>
                                </li>
                                <li class="p-2 cursor-pointer"
                                    onclick="location.href='${servletPath}/page/admin_album.do';">
                                    <div class="cursor-pointer">앨범 관리</div>
                                </li>
                                <li class="p-2 cursor-pointer"
                                    onclick="location.href='${servletPath}/page/admin_potalPage.do';">
                                    <div class="cursor-pointer">포털페이지 관리</div>
                                </li>
                            </ul>
                            <div class="hidden tablet:block"><select name="" id=""
                                    class=" tablet: border ml-[2px] my-[5px] h-10 w-[99%]">
                                    <option value="/manage/user">회원 관리</option>
                                    <option value="/manage/board">게시글 관리</option>
                                    <option value="/manage/album">앨범 관리</option>
                                    <option value="/manage/portal">포털페이지 관리</option>
                                </select></div>
                        </nav>
                        <div class="w-full p-2">
                            <div class="Title_box__3VRfB">포털페이지 관리</div>
                            <div class="p-4 border-b border-zinc-400">
                                <h2 class="text-lg">배너 편집</h2>
                                <div class="flex mt-3 mb-3">
                                    <h3 class="w-[100px]">이미지1</h3>
                                    <div class="flex gap-5">
                                        <div class="flex-3">
                                            <input type="file" id="bannerImg1Btn">
                                            <input type="text" class="w-full h-10 p-1 mt-2 border"
                                                placeholder="배너와 연결할 url" id="bannerImg1Text">
                                        </div>
                                        <div class="flex-1">
                                            <p>선택된 이미지</p>
                                            <div class="w-[128px] h-[50px] overflow-hidden">
                                                <img class="w-[100%] h-[100%] object-fill" id="bannerImg1">
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="flex mt-3 mb-3">
                                    <h3 class="w-[100px]">이미지2</h3>
                                    <div class="flex gap-5">
                                        <div class="flex-3">
                                            <input type="file" id="bannerImg2Btn">
                                            <input type="text" class="w-full h-10 p-1 mt-2 border"
                                                placeholder="배너와 연결할 url" id="bannerImg2Text">
                                        </div>
                                        <div class="flex-1">
                                            <p>선택된 이미지</p>
                                            <div class="w-[128px] h-[50px] overflow-hidden">
                                                <img class=" w-[100%]  h-[100%] object-fill" id="bannerImg2">
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="flex mt-3 mb-3">
                                    <h3 class="w-[100px]">이미지3</h3>
                                    <div class="flex gap-5">
                                        <div class="flex-3">
                                            <input type="file" id="bannerImg3Btn">
                                            <input type="text" class="w-full h-10 p-1 mt-2 border"
                                                placeholder="배너와 연결할 url" id="bannerImg3Text">
                                        </div>
                                        <div class="flex-1">
                                            <p>선택된 이미지</p>
                                            <div class="w-[128px] h-[50px] overflow-hidden">
                                                <img class=" w-[100%] h-[100%] object-fill" id="bannerImg3">
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="mt-5">
                                <div class="flex mb-3 leading-10 w-[800px]">
                                    <div class="flex flex-1">
                                        <h3 class="w-[100px]">공지 활성화</h3>
                                        <div>
                                            <select class="border h-10 rounded-sm w-[100px]" id="popupEnableStatus">
                                                <option value="true">활성</option>
                                                <option value="false">비활성</option>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="flex flex-1"><label for="width">너비(px)</label>
                                        <input class="border px-1 ml-3 w-[100px]" type="number" name="width" value="0"
                                            id="popupWidth">
                                    </div>
                                    <div class="flex flex-1"><label for="height">높이(px)</label>
                                        <input class="border px-1 ml-3 w-[100px]" type="number" name="height" value="0"
                                            id="popupHeight">
                                    </div>
                                </div>
                                <div style="height: 300px;">
                                    <div class="container" style="height: 100%;">
                                        <div class="wmde-markdown-var w-md-editor w-md-editor-show-live"
                                            style="height: 200px;">
                                            <div class="w-md-editor-toolbar ">
                                                <ul>
                                                    <li class=""><button aria-label="Insert italic"><svg
                                                                data-name="italic" width="12" height="12" role="img"
                                                                viewBox="0 0 320 512">
                                                                <path fill="currentColor"
                                                                    d="M204.758 416h-33.849l62.092-320h40.725a16 16 0 0 0 15.704-12.937l6.242-32C297.599 41.184 290.034 32 279.968 32H120.235a16 16 0 0 0-15.704 12.937l-6.242 32C96.362 86.816 103.927 96 113.993 96h33.846l-62.09 320H46.278a16 16 0 0 0-15.704 12.935l-6.245 32C22.402 470.815 29.967 480 40.034 480h158.479a16 16 0 0 0 15.704-12.935l6.245-32c1.927-9.88-5.638-19.065-15.704-19.065z">
                                                                </path>
                                                            </svg></button></li>
                                                    <li class="">
                                                        <button aria-label="Insert code"><svg width="14" height="14"
                                                                role="img" viewBox="0 0 640 512">
                                                                <path fill="currentColor"
                                                                    d="M278.9 511.5l-61-17.7c-6.4-1.8-10-8.5-8.2-14.9L346.2 8.7c1.8-6.4 8.5-10 14.9-8.2l61 17.7c6.4 1.8 10 8.5 8.2 14.9L293.8 503.3c-1.9 6.4-8.5 10.1-14.9 8.2zm-114-112.2l43.5-46.4c4.6-4.9 4.3-12.7-.8-17.2L117 256l90.6-79.7c5.1-4.5 5.5-12.3.8-17.2l-43.5-46.4c-4.5-4.8-12.1-5.1-17-.5L3.8 247.2c-5.1 4.7-5.1 12.8 0 17.5l144.1 135.1c4.9 4.6 12.5 4.4 17-.5zm327.2.6l144.1-135.1c5.1-4.7 5.1-12.8 0-17.5L492.1 112.1c-4.8-4.5-12.4-4.3-17 .5L431.6 159c-4.6 4.9-4.3 12.7.8 17.2L523 256l-90.6 79.7c-5.1 4.5-5.5 12.3-.8 17.2l43.5 46.4c4.5 4.9 12.1 5.1 17 .6z">
                                                                </path>
                                                            </svg></button>
                                                    </li>
                                                    <li class="">
                                                        <button aria-label="Insert code"><svg width="14" height="14"
                                                                role="img" viewBox="0 0 640 512">
                                                                <path fill="currentColor"
                                                                    d="M520,95.75 L520,225.75 C520,364.908906 457.127578,437.050625 325.040469,472.443125 C309.577578,476.586875 294.396016,464.889922 294.396016,448.881641 L294.396016,414.457031 C294.396016,404.242891 300.721328,395.025078 310.328125,391.554687 C377.356328,367.342187 414.375,349.711094 414.375,274.5 L341.25,274.5 C314.325781,274.5 292.5,252.674219 292.5,225.75 L292.5,95.75 C292.5,68.8257812 314.325781,47 341.25,47 L471.25,47 C498.174219,47 520,68.8257812 520,95.75 Z M178.75,47 L48.75,47 C21.8257813,47 0,68.8257812 0,95.75 L0,225.75 C0,252.674219 21.8257813,274.5 48.75,274.5 L121.875,274.5 C121.875,349.711094 84.8563281,367.342187 17.828125,391.554687 C8.22132813,395.025078 1.89601563,404.242891 1.89601563,414.457031 L1.89601563,448.881641 C1.89601563,464.889922 17.0775781,476.586875 32.5404687,472.443125 C164.627578,437.050625 227.5,364.908906 227.5,225.75 L227.5,95.75 C227.5,68.8257812 205.674219,47 178.75,47 Z">
                                                                </path>
                                                            </svg></button>
                                                    </li>
                                                    <li class="">
                                                        <button type="button" data-name="title"
                                                            aria-label="Insert title"><svg width="12" height="12"
                                                                viewBox="0 0 520 520">
                                                                <path fill="currentColor"
                                                                    d="M15.7083333,468 C7.03242448,468 0,462.030833 0,454.666667 L0,421.333333 C0,413.969167 7.03242448,408 15.7083333,408 L361.291667,408 C369.967576,408 377,413.969167 377,421.333333 L377,454.666667 C377,462.030833 369.967576,468 361.291667,468 L15.7083333,468 Z M21.6666667,366 C9.69989583,366 0,359.831861 0,352.222222 L0,317.777778 C0,310.168139 9.69989583,304 21.6666667,304 L498.333333,304 C510.300104,304 520,310.168139 520,317.777778 L520,352.222222 C520,359.831861 510.300104,366 498.333333,366 L21.6666667,366 Z M136.835938,64 L136.835937,126 L107.25,126 L107.25,251 L40.75,251 L40.75,126 L-5.68434189e-14,126 L-5.68434189e-14,64 L136.835938,64 Z M212,64 L212,251 L161.648438,251 L161.648438,64 L212,64 Z M378,64 L378,126 L343.25,126 L343.25,251 L281.75,251 L281.75,126 L238,126 L238,64 L378,64 Z M449.047619,189.550781 L520,189.550781 L520,251 L405,251 L405,64 L449.047619,64 L449.047619,189.550781 Z">
                                                                </path>
                                                            </svg></button>
                                                        <div class="w-md-editor-toolbar-child ">
                                                            <div class="w-md-editor-toolbar undefined">
                                                                <ul>
                                                                    <li class="">
                                                                        <button type="button" data-name="title1"
                                                                            aria-label="Insert title1 (ctrl + 1)"
                                                                            title="Insert title1 (ctrl + 1)">
                                                                            <div
                                                                                style="font-size: 18px; text-align: left;">
                                                                                Title 1
                                                                            </div>
                                                                        </button>
                                                                    </li>
                                                                    <li class="">
                                                                        <button type="button" data-name="title2"
                                                                            aria-label="Insert title2 (ctrl + 2)"
                                                                            title="Insert title2 (ctrl + 2)">
                                                                            <div
                                                                                style="font-size: 16px; text-align: left;">
                                                                                Title 2
                                                                            </div>
                                                                        </button>
                                                                    </li>
                                                                    <li class="">
                                                                        <button type="button" data-name="title3"
                                                                            aria-label="Insert title3 (ctrl + 3)"
                                                                            title="Insert title3 (ctrl + 3)">
                                                                            <div
                                                                                style="font-size: 15px; text-align: left;">
                                                                                Title 3
                                                                            </div>
                                                                        </button>
                                                                    </li>
                                                                    <li class=""><button type="button"
                                                                            data-name="title4"
                                                                            aria-label="Insert title4 (ctrl + 4)"
                                                                            title="Insert title4 (ctrl + 4)">
                                                                            <div
                                                                                style="font-size: 14px; text-align: left;">
                                                                                Title 4
                                                                            </div>
                                                                        </button></li>
                                                                    <li class=""><button type="button"
                                                                            data-name="title5"
                                                                            aria-label="Insert title5 (ctrl + 5)"
                                                                            title="Insert title5 (ctrl + 5)">
                                                                            <div
                                                                                style="font-size: 12px; text-align: left;">
                                                                                Title 5
                                                                            </div>
                                                                        </button></li>
                                                                    <li class=""><button type="button"
                                                                            data-name="title6"
                                                                            aria-label="Insert title6 (ctrl + 6)"
                                                                            title="Insert title6 (ctrl + 6)">
                                                                            <div
                                                                                style="font-size: 12px; text-align: left;">
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
                                                    <li class=""><button type="button" data-name="edit"
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
                                                    <li class="active"><button type="button" data-name="live"
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
                                                    <li class=""><button type="button" data-name="preview"
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
                                                    <li class=""><button type="button" data-name="fullscreen"
                                                            aria-label="Toggle fullscreen (ctrl + 0)"
                                                            title="Toggle fullscreen (ctrl+ 0)"><svg width="12"
                                                                height="12" viewBox="0 0 520 520">
                                                                <path fill="currentColor"
                                                                    d="M118 171.133334L118 342.200271C118 353.766938 126.675 365.333605 141.133333 365.333605L382.634614 365.333605C394.201281 365.333605 405.767948 356.658605 405.767948 342.200271L405.767948 171.133334C405.767948 159.566667 397.092948 148 382.634614 148L141.133333 148C126.674999 148 117.999999 156.675 118 171.133334zM465.353591 413.444444L370 413.444444 370 471.222222 474.0221 471.222222C500.027624 471.222222 520.254143 451 520.254143 425L520.254143 321 462.464089 321 462.464089 413.444444 465.353591 413.444444zM471.0221 43L367 43 367 100.777778 462.353591 100.777778 462.353591 196.111111 520.143647 196.111111 520.143647 89.2222219C517.254144 63.2222219 497.027624 43 471.0221 43zM57.7900547 100.777778L153.143646 100.777778 153.143646 43 46.2320439 43C20.2265191 43 0 63.2222219 0 89.2222219L0 193.222222 57.7900547 193.222222 57.7900547 100.777778zM57.7900547 321L0 321 0 425C0 451 20.2265191 471.222222 46.2320439 471.222223L150.254143 471.222223 150.254143 413.444445 57.7900547 413.444445 57.7900547 321z">
                                                                </path>
                                                            </svg></button></li>
                                                </ul>
                                            </div>
                                            <div class="w-md-editor-content">
                                                <div class="w-md-editor-area w-md-editor-input">
                                                    <div class="w-md-editor-text" style="min-height: 100px;">
                                                        <pre class="w-md-editor-text-pre wmde-markdown-color"></pre>
                                                        <textarea autocomplete="off" autocorrect="off"
                                                            autocapitalize="off" spellcheck="false"
                                                            class="w-md-editor-text-input " id="popupText"></textarea>
                                                    </div>
                                                </div>
                                                <div class="w-md-editor-preview ">
                                                    <div class="wmde-markdown wmde-markdown-color "></div>
                                                </div>
                                            </div>
                                            <div class="w-md-editor-bar"><svg viewBox="0 0 512 512" height="100%">
                                                    <path fill="currentColor"
                                                        d="M304 256c0 26.5-21.5 48-48 48s-48-21.5-48-48 21.5-48 48-48 48 21.5 48 48zm120-48c-26.5 0-48 21.5-48 48s21.5 48 48 48 48-21.5 48-48-21.5-48-48-48zm-336 0c-26.5 0-48 21.5-48 48s21.5 48 48 48 48-21.5 48-48-21.5-48-48-48z">
                                                    </path>
                                                </svg></div>
                                        </div>
                                    </div>
                                </div><input type="file" class="input-image-file" hidden="">
                            </div>
                        </div>
                    </div>
                    <div class="m-5 text-center"><button
                            class="p-2 text-white rounded Button_normalSize__custom Button_mainColor__purple"
                            id="saveBtn">저장</button>
                    </div>
                </div>

            </main>
            <footer class="Footer_footer__custom">
                <div class="pt-3 m-auto desktop:max-w-7xl laptop:max-w-4xl tablet:max-w-3xl">
                    <div class="cursor-pointer">
                        <div class="hover:cursor-pointer"><img src="${servletPath}/image/logo-bw.png" alt="logo"></div>
                    </div>
                    <p class="pt-3 text-sm">(82)2-494-4707 / soxcom@naver.com <br>301ho, 23, Seokgye-ro 18-gil,
                        Nowon-gu,
                        Seoul,
                        01886 Republic of Korea<br>© 2018-2024 by S.O.X Co.Ltd. All rights reserved<br></p>
                </div>
            </footer>
        </div>

        <script src="https://code.jquery.com/jquery-3.4.1.js"></script>
        <script src="${servletPath}/js/common_header.js"></script>
        <script src="${servletPath}/js/admin_potalPage.js"></script>
        <script src="${servletPath}/js/movePage.js"></script>
        <script>
            getPotalPage();
        </script>
    </body>

    </html>