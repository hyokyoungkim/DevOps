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
                <div class="rounded shadow p-3 m-auto border w-1/3 fixed bg-white top-[50%] left-[50%] translate-x-[-50%] translate-y-[-50%] z-1 hidden"
                    id="checkPopup">
                    기존 비밀번호를 입력해주세요
                    <input class="block w-full h-10 p-1 mt-2 mb-5 border" value="" id="currentPwInput">
                    <div class="flex flex-row-reverse gap-2">
                        <button class="p-2 text-white rounded Button_normalSize__custom Button_grayColor"
                            id="cancelBtn">취소</button>
                        <button class="p-2 text-white rounded Button_normalSize__custom Button_subColor__custom"
                            id="okBtn">확인</button>
                    </div>
                </div>
                <div class="min-h-screen pt-14">
                    <div class="flex pt-5 m-auto desktop:max-w-7xl laptop:max-w-4xl tablet:max-w-3xl">
                        <nav class="min-w[150px] h-full border shadow rounded-sm w-40 tablet:w-full border-none">
                            <ul class="tablet:hidden">
                                <li class="p-2 cursor-pointer"
                                    onclick="location.href='${servletPath}/page/myPage_userInfo.do';">
                                    <div class="cursor-pointer">회원정보 변경</div>
                                </li>
                                <li class="p-2 cursor-pointer"
                                    onclick="location.href='${servletPath}/page/myPage_userPw.do';">
                                    <div class="cursor-pointer">비밀번호 변경</div>
                                </li>
                            </ul>
                            <div class="hidden tablet:block"><select name="" id=""
                                    class=" tablet: border ml-[2px] my-[5px] h-10 w-[99%]">
                                    <option value="/user/modify/info">회원정보 변경</option>
                                    <option value="/user/modify/pw">비밀번호 변경</option>
                                </select></div>
                        </nav>
                        <div class="flex m-auto mt-[20vh]">
                            <div class="self-center shadow-md m-auto w-[500px] rounded bg-white align-center p-4 pt-5">
                                <div><img src="${servletPath}/image/logo.png" alt="logo" class="inline-block h-6">
                                    <span class="float-right">MyPage</span>
                                </div>
                                <div class="flex mt-3 mb-3">
                                    <span class="w-[130px] leading-10">E-mail</span>
                                    <input type="text" class="w-[250px] rounded border mr-2 p-1 h-10" id="emailInput">
                                    <div class="w-[130px]">
                                        <button Button_fullSize__custom class="p-2 text-white rounded Button_grayColor"
                                            id="numberSendBtn">인증번호
                                            전송</button>
                                    </div>
                                </div>
                                <div class="flex mt-3 mb-3"><span class="w-[130px] leading-10">인증번호</span>
                                    <input type="text" class="w-[250px] rounded border mr-2 p-1 h-10" id="numberInput">
                                    <div class="w-[130px]">
                                        <button class="p-2 text-white rounded Button_fullSize__custom Button_grayColor"
                                            id="numberCheckBtn">인증번호
                                            확인</button>
                                    </div>
                                </div>
                                <div class="flex mt-3 mb-3">
                                    <span class="w-[130px] leading-10">닉네임</span>
                                    <input type="text" class="w-[250px] rounded border mr-2 p-1 h-10"
                                        id="nicknameInput">
                                    <div class="w-[130px]"><button
                                            class="p-2 text-white rounded Button_fullSize__custom Button_grayColor"
                                            id="nicknameCheckBtn">중복확인</button>
                                    </div>
                                </div>
                                <div class="flex flex-col gap-3 mt-10">
                                    <button
                                        class="p-2 text-white rounded Button_fullSize__custom Button_subColor__custom"
                                        id="changeBtn">회원정보
                                        변경</button>
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
        <script src="${servletPath}/js/myPage_userInfo.js"></script>
        <script src="${servletPath}/js/movePage.js"></script>
        <script>
            document.addEventListener('DOMContentLoaded', () => {

            })
            document.addEventListener('click', clickBtn);
        </script>
    </body>

    </html>