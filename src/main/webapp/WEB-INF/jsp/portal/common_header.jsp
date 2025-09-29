<%@page contentType="text/html" pageEncoding="UTF-8" isELIgnored="false" %>
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
                        <div class="cursor-pointer" onclick="location.href='${servletPath}/page/albumList.do';">앨범 보기
                        </div>
                    </li>
                    <li class="m-5 font-bold hover:cursor-pointer text-slate-700 ">
                        <div id="myAlbumList">내 앨범 보기</div>
                    </li>
                    <li class="m-5 font-bold hover:cursor-pointer text-slate-700 ">
                        <div class="cursor-pointer"
                            onclick="location.href='${servletPath}/page/serviceInformation.do';">서비스 소개</div>
                    </li>
                    <li class="m-5 font-bold hover:cursor-pointer text-slate-700 ">
                        <div class="cursor-pointer" onclick="location.href='${servletPath}/page/board_notice.do';">게시판
                        </div>
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