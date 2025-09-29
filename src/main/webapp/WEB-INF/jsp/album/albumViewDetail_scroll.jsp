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
                    <div class="m-auto desktop:max-w-7xl laptop:max-w-4xl tablet:max-w-3xl">
                        <section class="w-full p-3 mt-5 border rounded shadow-sm">
                            <div class="flex tablet:flex-col">
                                <div class="flex-grow p-5 h-96 tablet:h-52 tablet:m-auto" id="contentThumbnail">
                                    <!--상단 대표 이미지-->
                                </div>
                                <div class="relative w-3/5 p-5 tablet:w-full" id="contentTitle">


                                </div>
                            </div>
                        </section>
                        <section class="w-full mt-5 rounded shadow-sm" id="content">
                        </section>


                        <section id="user_commentInput" class="hidden"> <!--댓글 등록-->
                            <div class="gap-3 overflow-hidden"><textarea rows="3" class="w-full p-2 border resize-none"
                                    id="commnetInputBox"></textarea>
                                <div class="float-right">
                                    <button
                                        class="p-2 text-white rounded Button_normalSize__custom Button_subColor__custom"
                                        id="registrationBtn">등록</button>
                                </div>
                            </div>
                        </section>

                        <section id="user_commentView"> <!--댓글 확인-->

                            <!-- <div class="relative mb-5">
                            <p class="text-sm text-gray-400">2024-08-30</p>
                            <p class="block mb-2 font-semibold">관리자</p>
                            <p>etssetstste</p>
                        </div>
                        <div class="relative p-3 mt-4 mb-2 rounded shadow bg-zinc-100" id="adminReply-266">
                            <p class="text-sm text-gray-400">2024-08-30</p>
                            <p class="block mb-2 font-semibold">관리자</p>
                            <p>rt</p>
                        </div> -->
                            <!-- </div> -->
                        </section>

                    </div>

                    <div id="sideMenu"
                        class="fixed top-[250px] right-[50px] w-[40px] overflow-hidden false ease-in duration-300 min-h-[40px] max-h-[60vh] h-full tablet:top-[80px] tablet:right-0 laptop:hidden">
                    </div>


                    <div
                        class="fixed cursor-pointer bottom-10 right-10 w-[60px] h-[60px] border bg-[#9333EA] rounded-md shadow-md 
                            tablet:bottom-3 tablet:right-0 tablet:w-[50px] tablet:h-[50px] tablet:rounded-none tablet:rounded-l-lg">
                        <img src="${servletPath}/image/book.png" alt="book" class="h-auto w-max" id="bookIcon">
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
            <div class="rounded shadow p-3 m-auto border w-1/3 fixed bg-white top-[50%] left-[50%] translate-x-[-50%] translate-y-[-50%] z-1  hidden w-[300px]"
                id="sendEmailPopup">
                전송할 이메일을 입력하세요<input class="block w-full h-10 p-1 mt-2 mb-5 border" value="" id="sendEmailInput">
                <div class="flex flex-row-reverse gap-2"><button
                        class="p-2 text-white rounded Button_normalSize__custom Button_grayColor"
                        id="sendEmailCancelBtn">취소</button><button
                        class="p-2 text-white rounded Button_normalSize__custom Button_subColor__custom"
                        id="sendEmailOkBtn">확인</button></div>
            </div>
        </div>

        <script src="https://code.jquery.com/jquery-3.4.1.js"></script>
        <script src="${servletPath}/js/common_header.js"></script>
        <script src="${servletPath}/js/albumViewDetail_scroll.js"></script>
        <script src="${servletPath}/js/movePage.js"></script>
        <script>
            getAlbum();
            document.addEventListener('DOMContentLoaded', () => {  //페이지가 로드된 후 이벤트 리스너 추가

            })
        </script>
    </body>

    </html>