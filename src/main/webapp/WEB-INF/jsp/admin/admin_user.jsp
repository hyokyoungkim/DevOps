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
                                    <option value="/manage/user"
                                        onclick="location.href='${servletPath}/page/admin_user.do';"></option>>회원
                                    관리</option>
                                    <option value="/manage/board"
                                        onclick="location.href='${servletPath}/page/admin_board.do';">게시글 관리
                                    </option>
                                    <option value="/manage/album"
                                        onclick="location.href='${servletPath}/page/admin_album.do';">앨범 관리
                                    </option>
                                    <option value="/manage/portal"
                                        onclick="location.href='${servletPath}/page/admin_potalPage.do';">포털페이지
                                        관리</option>
                                </select></div>
                        </nav>
                        <div class="w-full pl-5">
                            <h3 id="userCount"></h3>
                            <div class="float-left">
                                <ul>
                                    <li class="float-left m-2 cursor-pointer" id="accessSeq">접속순</li>
                                    <li class="float-left m-2 cursor-pointer" id="nameSeq">이름순</li>
                                    <li class="float-left m-2 cursor-pointer" id="albumCountSeq">앨범수 순</li>
                                </ul>
                            </div>
                            <div class="rounded shadow p-3 m-auto border w-1/3 fixed bg-white top-[50%] left-[50%] translate-x-[-50%] translate-y-[-50%] z-1 hidden"
                                id="logPopup">
                                로그 확인<input class="block w-full h-10 p-1 mt-2 mb-5 border" value="" id="logContent">
                                <div class="flex flex-row-reverse gap-2">
                                    <button class="p-2 text-white rounded Button_normalSize__CPxni Button_grayColor"
                                        id="logCancelBtn">취소</button>
                                    <button
                                        class="p-2 text-white rounded Button_normalSize__CPxni Button_subColor__custom"
                                        id="logCheckBtn">확인</button>
                                </div>
                            </div>

                            <div class="fixed border bg-white w-[700px] h-[400px] top-[50%] left-[50%] translate-x-[-50%] translate-y-[-50%] p-3 hidden"
                                id="emailPopup">
                                <input type="text" class="w-full p-1 border resize-none" placeholder="제목을 입력해주세요"
                                    id="emailTitle">
                                <textarea class="resize-none border w-full p-1 h-[280px] my-2" placeholder="내용을 입력해주세요"
                                    id="emailContent"></textarea>
                                <div class="flex justify-center gap-2">
                                    <button class="p-2 text-white rounded Button_normalSize__custom Button_grayColor"
                                        id="emailCancleBtn">취소</button>
                                    <button
                                        class="p-2 text-white rounded Button_normalSize__custom Button_subColor__custom"
                                        id="emailCheckBtn">메일
                                        전송</button>
                                </div>
                            </div>


                            <div
                                class="relative border float-right  w-[439px] h-10 top-0 mr-3 tablet:w-[100%] tablet:float-left">
                                <select name="option"
                                    class="absolute right-[326px] top-[1px] px-2 h-9 tablet:left-0 tablet:w-[130px]"
                                    id="searchSelect">
                                    <option value="1">&nbsp;&nbsp;검&nbsp;&nbsp;&nbsp;&nbsp;색&nbsp;&nbsp;</option>
                                </select>
                                <input type="text" id="searchText"
                                    class="float-right w-[300px] mt-[1px] mr-[1px] text-left space-x-3 px-4 h-9 bg-white  hover:ring-slate-300 focus:outline-none focus:ring-2 focus:ring-black shadow-sm rounded-sm dark:bg-slate-800 dark:ring-0 dark:text-slate-300 dark:highlight-white/5 dark:hover:bg-slate-700 border-none tablet:w-[200px]">
                                <img src="${servletPath}/image/search.png" alt="search"
                                    class="absolute cursor-pointer right-3 top-1" id="searchTextImg">
                            </div>
                            <table role="table" class="w-full text-sm text-center">
                                <thead>
                                    <tr role="row">
                                        <th colspan="1" role="columnheader"
                                            class="pt-3 pb-3 border-b-2 border-b-gray-300">
                                            닉네임</th>
                                        <th colspan="1" role="columnheader"
                                            class="pt-3 pb-3 border-b-2 border-b-gray-300">
                                            아이디</th>
                                        <th colspan="1" role="columnheader"
                                            class="pt-3 pb-3 border-b-2 border-b-gray-300">
                                            가입일</th>
                                        <th colspan="1" role="columnheader"
                                            class="pt-3 pb-3 border-b-2 border-b-gray-300">
                                            로그인 수</th>
                                        <th colspan="1" role="columnheader"
                                            class="pt-3 pb-3 border-b-2 border-b-gray-300">
                                            앨범 구독 수</th>
                                        <th colspan="1" role="columnheader"
                                            class="pt-3 pb-3 border-b-2 border-b-gray-300">
                                            앨범 제작</th>
                                        <th colspan="1" role="columnheader"
                                            class="pt-3 pb-3 border-b-2 border-b-gray-300">
                                            로그</th>
                                        <th colspan="1" role="columnheader"
                                            class="pt-3 pb-3 border-b-2 border-b-gray-300">
                                            기능</th>
                                    </tr>
                                </thead>
                                <tbody role="rowgroup" id="userList">


                                </tbody>
                            </table>
                        </div>
                    </div>
                    <div class="flex justify-center PagiNation_pagination__custom ">
                        <div class="flex mt-10">
                            <ul class="pagination" id="pagination">

                            </ul>
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
        <script src="${servletPath}/js/admin_user.js"></script>
        <script src="${servletPath}/js/movePage.js"></script>
        <script>
            document.addEventListener('DOMContentLoaded', () => {  //페이지가 로드된 후 이벤트 리스너 추가

                document.addEventListener('click', clickPagination); //pagination 클릭 이벤트
                document.addEventListener('click', clickAlign); //pagination 클릭 이벤트
                document.addEventListener('click', clickBtn); //비밀번호 초기화 클릭 이벤트
            })
        </script>
    </body>

    </html>