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
                    <div>
                        <section class="pt-5 m-auto desktop:max-w-7xl laptop:max-w-4xl tablet:max-w-3xl">
                            <div class="BannerSwiper_container__icvvY">
                                <div class="swiper mySwiper2">
                                    <div class="swiper-wrapper">
                                        <div class="swiper-slide" data-swiper-slide-index="0"
                                            style="cursor: pointer; width: 1112px; margin-right: 20px;">
                                            <img alt="banner" id="bannerImg1">
                                        </div>
                                        <div class="swiper-slide" data-swiper-slide-index="1"
                                            style="cursor: pointer; width: 1112px; margin-right: 20px;">
                                            <img alt="banner" id="bannerImg2">
                                        </div>
                                        <div class="swiper-slide" data-swiper-slide-index="2"
                                            style="cursor: pointer; width: 1112px; margin-right: 20px;">
                                            <img alt="banner" id="bannerImg3">
                                        </div>
                                    </div>
                                    <div class="swiper-button-prev"></div>
                                    <div class="swiper-button-next"></div>
                                    <div class="swiper-pagination"></div>
                                </div>
                            </div>
                        </section>
                        <section class="px-1 m-auto mt-10 desktop:max-w-7xl laptop:max-w-4xl tablet:max-w-3xl ">
                            <div>
                                <h3 class="inline-block text-lg">베스트 앨범 구경하기</h3><img
                                    src="${servletPath}/image/Arrow.png" alt="arrow" class="inline-block w-5 pl-1">
                            </div>
                            <div class="flex flex-wrap overflow-hidden labtop:w-[full] labtop:h-[1627px] tablet:h-[1218px]"
                                id="bestAlbum">
                            </div>
                        </section>

                        <!--hidden 상태-->
                        <section
                            class="hidden m-auto mt-10 desktop:max-w-7xl laptop:max-w-4xl tablet:max-w-3xl laptop:mt-14">
                            <div>
                                <h3 class="inline-block text-lg">인기 카테고리 ( IT / 취미 / 여행 )</h3><img
                                    src="${servletPath}/image/Arrow.png" alt="arrow" class="inline-block w-5 pl-1">
                            </div>
                            <div
                                class="flex flex-wrap overflow-hidden labtop:w-[full] labtop:h-[1627px] tablet:h-[1218px]">
                            </div>
                        </section>

                        <section class="m-auto mt-10 desktop:max-w-7xl laptop:max-w-4xl tablet:max-w-3xl laptop:mt-14">
                            <div>
                                <h3 class="inline-block text-lg">신규 앨범 구경하기</h3><img
                                    src="${servletPath}/image/Arrow.png" alt="arrow" class="inline-block w-5 pl-1">
                            </div>
                            <div class="flex flex-wrap overflow-hidden labtop:w-[full] labtop:h-[1627px] tablet:h-[1218px]"
                                id="newAlbum">
                            </div>
                        </section>
                        <section class="mt-10 bg-zinc-100 pt-7 pb-7">
                            <div class="px-1 pb-2 m-auto desktop:max-w-7xl laptop:max-w-4xl tablet:max-w-3xl">
                                <div>
                                    <h3 class="inline-block text-lg">카테고리별로 확인하기</h3><img
                                        src="${servletPath}/image/Arrow.png" alt="arrow" class="inline-block w-5 pl-1">
                                </div>
                            </div>
                            <div>
                                <div class="flex flex-wrap m-auto desktop:max-w-7xl laptop:max-w-4xl tablet:max-w-3xl">
                                    <div class="flex p-2 bg-white rounded shadow-md cursor-pointer CategoryCard_flexItem__custom"
                                        data-value="IT">
                                        <div class="CategoryCard_textDiv__group">
                                            <p class="text-sm">웹개발, 앱개발, DB,
                                                보안, AI 등</p>
                                            <p class="mt-8 font-bold CategoryCard_bottomText">IT A부터 Z까지
                                                배우기</p>
                                        </div><img src="${servletPath}/image/category-IT.png" alt="책 이미지"
                                            class="CategoryCard_image">
                                    </div>
                                    <div class="flex p-2 bg-white rounded shadow-md cursor-pointer CategoryCard_flexItem__custom"
                                        data-value="자기계발">
                                        <div class="CategoryCard_textDiv__group">
                                            <p class="text-sm">명상, 영상편집,
                                                자격증 등</p>
                                            <p class="mt-8 font-bold CategoryCard_bottomText">자기계발을 통해
                                                마음의 양식 쌓기</p>
                                        </div><img src="${servletPath}/image/category-personal-groth.png" alt="책 이미지"
                                            class="CategoryCard_image">
                                    </div>
                                    <div class="flex p-2 bg-white rounded shadow-md cursor-pointer CategoryCard_flexItem__custom"
                                        data-value="여행">
                                        <div class="CategoryCard_textDiv__group">
                                            <p class="text-sm">여행팁, 코스, 추천장소 등</p>
                                            <p class="mt-8 font-bold CategoryCard_bottomText">여행 컨텐츠 모아보기</p>
                                        </div><img src="${servletPath}/image/category-travel.png" alt="책 이미지"
                                            class="CategoryCard_image">
                                    </div>
                                    <div class="flex p-2 bg-white rounded shadow-md cursor-pointer CategoryCard_flexItem__custom"
                                        data-value="경제">
                                        <div class="CategoryCard_textDiv__group">
                                            <p class="text-sm">제2의 월급, 주식, 경제소식,
                                                부동산 등</p>
                                            <p class="mt-8 font-bold CategoryCard_bottomText">경제지식부터 든든한 통장 만들기까지</p>
                                        </div><img src="${servletPath}/image/category-investment.png" alt="책 이미지"
                                            class="CategoryCard_image">
                                    </div>
                                    <div class="flex p-2 bg-white rounded shadow-md cursor-pointer CategoryCard_flexItem__custom"
                                        data-value="취미">
                                        <div class="CategoryCard_textDiv__group">
                                            <p class="text-sm">베이킹, 요리, 운동, 인테리어</p>
                                            <p class="mt-8 font-bold CategoryCard_bottomText">취미생활백서</p>
                                        </div><img src="${servletPath}/image/category-hobby.png" alt="책 이미지"
                                            class="CategoryCard_image">
                                    </div>
                                    <div class="flex p-2 bg-white rounded shadow-md cursor-pointer CategoryCard_flexItem__custom"
                                        data-value="외국어">
                                        <div class="CategoryCard_textDiv__group">
                                            <p class="text-sm">영어, 중국어, 제2외국어 등</p>
                                            <p class="mt-8 font-bold CategoryCard_bottomText">외국어 능력 향상시키기</p>
                                        </div><img src="${servletPath}/image/category-language.png" alt="책 이미지"
                                            class="CategoryCard_image">
                                    </div>
                                    <div class="flex p-2 bg-white rounded shadow-md cursor-pointer CategoryCard_flexItem__custom"
                                        data-value="맛집">
                                        <div class="CategoryCard_textDiv__group">
                                            <p class="text-sm">한식, 양식, 카페 등</p>
                                            <p class="mt-8 font-bold CategoryCard_bottomText">먹잘알들의
                                                맛집추천</p>
                                        </div><img src="${servletPath}/image/category-food.png" alt="책 이미지"
                                            class="CategoryCard_image">
                                    </div>
                                    <div class="flex p-2 bg-white rounded shadow-md cursor-pointer CategoryCard_flexItem__custom"
                                        data-value="기타">
                                        <div class="CategoryCard_textDiv__group">
                                            <p class="text-sm"></p>
                                            <p class="mt-8 font-bold CategoryCard_bottomText">기타 컨텐츠</p>
                                        </div><img src="${servletPath}/image/category-etc.png" alt="책 이미지"
                                            class="CategoryCard_image">
                                    </div>
                                </div>
                            </div>
                        </section>
                        <section class="m-auto mt-32 desktop:max-w-7xl laptop:max-w-4xl tablet:max-w-3xl"><img
                                src="${servletPath}/image/logo.png" alt="logo" class="inline-block h-6 mb-1">에서는 지금<div
                                class="PortalPage_infoSection__custom">
                                <div class="PortalPage_firstDiv__custom" id="albumCount">
                                </div>
                                <div class="PortalPage_secondDiv__group">
                                    <h3 class="PortalPage_boardTitle__custom">후기 모음</h3>
                                    <div class="PortalPage_board__custom" id="commentBoard">

                                    </div>
                                </div>
                            </div>
                        </section>
                    </div>
                    <section id="popupLayer">

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
        <script src="https://unpkg.com/swiper/swiper-bundle.min.js"></script>
        <script>

            document.addEventListener('DOMContentLoaded', function () {
                var swiper = new Swiper('.mySwiper2', {
                    loop: true,
                    autoplay: {
                        delay: 3000, // 3초마다 이미지 자동 전환
                        disableOnInteraction: false, // 상호작용 후에도 자동 전환 유지
                    },
                    pagination: {
                        el: '.swiper-pagination',
                        clickable: true,
                    },
                    navigation: {
                        nextEl: '.swiper-button-next',
                        prevEl: '.swiper-button-prev',
                    },
                });

                getBestAlbumList();
                getNewAlbumList();
                getAlbumCount();
                getAlbumCommentList();
                getBanner();
                checkPopupVisibility();
                getPopup();
            });





        </script>
        <script src="https://code.jquery.com/jquery-3.4.1.js"></script>
        <script src="${servletPath}/js/common_header.js"></script>
        <script src="${servletPath}/js/main.js"></script>
        <script src="${servletPath}/js/movePage.js"></script>
    </body>

    </html>