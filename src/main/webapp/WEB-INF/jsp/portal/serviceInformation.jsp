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
                    <section>
                        <h2 class="mt-5 mb-3 text-lg font-semibold text-center">What is<img
                                src="${servletPath}/image/logo.png" alt="logo" class="inline-block h-7">?</h2>
                        <div class="bg-[#7E38E1] py-8 tablet:h-[350px]">
                            <div class="text-center text-white">Codeenator는 <br class="hidden tablet:block">인터넷에 공유된 <br
                                    class="hidden tablet:block">다양한 형식의 컨텐츠(유튜브, 블로그, 파일)를 <br>원하는 주제로 묶어서<br
                                    class="hidden tablet:block"> 앨범을 만들어 공유하는 서비스입니다.</div>
                            <div class="flex items-center justify-center gap-3 my-7"><img
                                    src="${servletPath}/image/contents.png" alt="이미지" class="tablet:w-[120px]"><img
                                    src="${servletPath}/image/Arrow-white.png" alt="화살표" class="h-6 mr-3"><img
                                    src="${servletPath}/image/album.png" alt="이미지" class="tablet:w-[120px]"></div>
                            <div class="flex justify-center text-white"><a
                                    href="${servletPath}/pdf/Codeenator_manual.pdf" download="Codeenator_manual.pdf"
                                    class="border p-1 rounded">
                                    <button>사용방법 다운로드</button>
                                </a>
                            </div>
                        </div>
                    </section>
                    <section>
                        <div class="m-auto mt-12 text-xl text-center desktop:max-w-7xl">Coordinate : 조직화하다,
                            편성하다.<br>+<br>Code : 성향을 뜻하는
                            한국식 표현<br><br>=<br>Codeenator란 ? <br>의미나 맥락이 맞는 컨텐츠를 앨범처럼 엮어낸다는 의미!<br></div>
                    </section>
                    <section>
                        <div class="flex gap-12 m-auto mt-12 text-center desktop:max-w-7xl tablet:flex-col">
                            <div class="flex-1 border-[2px] border-[#7e38e1] border-dashed  p-4 rounded-md"><img
                                    src="${servletPath}/image/intro-img-01.png" alt="이미지" class="m-auto">
                                <h3 class="text-xl">참여와 이용</h3>
                                <p>소비자로써 앨범을 열람하고, 공급자로써 앨범을 출판</p>
                            </div>
                            <div class="flex-1 border-[2px] border-[#7e38e1] border-dashed p-4 rounded-md"><img
                                    src="${servletPath}/image/intro-img-02.png" alt="이미지" class="m-auto my-14">
                                <h3 class="text-xl">링크와 생성</h3>
                                <p>기존 컨텐츠를 연결하고 새롭게 작성된 컨텐츠를 조합</p>
                            </div>
                            <div class="flex-1 border-[2px] border-[#7e38e1] border-dashed p-4 rounded-md"><img
                                    src="${servletPath}/image/intro-img-03.png" alt="이미지" class="m-auto my-2">
                                <h3 class="text-xl">열람/공유 Free</h3>
                                <p>자유로운 앨범 보기와 만들기</p>
                            </div>
                        </div>
                    </section>
                    <section>
                        <div class="desktop:max-w-7xl gap-16 m-auto my-[150px] text-center">
                            <h3 class="text-xl">이렇게 이용할 수 있어요!</h3>
                            <div class="flex tablet:flex-col">
                                <div class="flex-1"><img src="${servletPath}/image/intro-img-04.png" alt="이미지"
                                        class="inline-block ">
                                </div>
                                <div class="flex-1">
                                    <div class="flex flex-col text-left">
                                        <div class="p-2 mt-10 target:m-2">
                                            <h3 class="text-lg tablet:text-center tablet:mb-2">앨범 펼치기</h3>
                                            <p>보고싶은 앨범을 찾아 클릭하면 누구나 앨범 내 컨텐츠를 볼 수 있습니다 원하는 주제에 맞는 자료를 동영상, 문서, pdf, 파일,
                                                텍스트
                                                등의 다양한 컨텐츠로
                                                즐길 수 있습니다. 컨텐츠별로 챕터와 책갈피가 있어 좀더 편리하게 이용할 수 있습니다. 웹 브라우저는 물론 모바일 앱을 통해서도
                                                플레이
                                                할 수 있습니다.
                                            </p>
                                        </div>
                                        <div class="p-2 mt-10 target:m-2">
                                            <h3 class="text-lg tablet:text-center tablet:mb-2">앨범 만들기</h3>
                                            <p>앨범 만들기는 회원에게만 오픈되는 메뉴입니다. 이미 존재하는 다양한 콘텐츠들과 나의 의견을 엮어서 하나의 앨범을 만들어 정보를
                                                전달할 수
                                                있습니다.</p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </section>
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
    </body>

    </html>