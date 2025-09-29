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
        <link rel="stylesheet" href="${servletPath}/css/markdownStyle.css">
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
                    <div class="flex pt-5 m-auto desktop:max-w-7xl laptop:max-w-4xl tablet:max-w-3xl">
                        <nav class="min-w[150px] h-full border shadow rounded-sm w-40 tablet:hidden border-none">
                            <ul class="tablet:hidden">
                                <li class="p-2 cursor-pointer"
                                    onclick="location.href='${servletPath}/page/board_notice.do';">
                                    <div class="cursor-pointer">공지사항</div>
                                </li>
                                <li class="p-2 cursor-pointer"
                                    onclick="location.href='${servletPath}/page/board_qna.do';">
                                    <div class="cursor-pointer">QnA</div>
                                </li>
                            </ul>
                            <div class="hidden tablet:block"><select name="" id=""
                                    class=" tablet: border ml-[2px] my-[5px] h-10 w-[99%]">
                                    <option value="/board/notice/list"
                                        onclick="location.href='${servletPath}/page/board_notice.do';">공지사항
                                    </option>
                                    <option value="/board/qna/list"
                                        onclick="location.href='${servletPath}/page/board_qna.do';">QnA</option>
                                </select></div>
                        </nav>
                        <div class="w-full desktop:pl-5 laptop:pl-5">
                            <div class="Title_box__custom">공지사항</div>
                            <div id="notice_form">



                            </div>
                            <div class="my-[100px]">
                                <div>
                                    <div class="hidden gap-3 overflow-hidden" id="commentArea">
                                        <textarea rows="3" class="w-full p-2 border resize-none "
                                            id="commentBox"></textarea>
                                        <div class="float-right">
                                            <button
                                                class="p-2 text-white rounded Button_normalSize__custom Button_subColor__custom"
                                                id="registrationBtn">등록</button>
                                        </div>
                                    </div>
                                    <div id="commentList"></div>
                                </div>
                            </div>
                        </div>
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
        <script src="${servletPath}/js/movePage.js"></script>
        <script src="${servletPath}/js/board_notice_veiw.js"></script>
        <script>
            document.addEventListener('DOMContentLoaded', function () {
                document.addEventListener('click', clickBtn);
            })
        </script>
    </body>

    </html>