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
                    <div class="flex min-h-[80vh]">
                        <div
                            class="self-center shadow-md border m-auto w-[550px] rounded bg-white align-center p-4 pt-5">
                            <div class="mb-10">
                                <div><img src="${servletPath}/image/logo.png" alt="logo" class="inline-block h-6"><span
                                        class="float-right text-white bg-[#220053] pr-14 pl-14 ">Join</span></div>
                                <div class="flex mt-3 mb-3"><span class="w-[130px] leading-10">아이디</span><input
                                        type="text" class="w-[250px] rounded border mr-2 p-1 h-10"
                                        placeholder="6~12자리 영문or영문+숫자" id="idCheckInput">
                                    <div class="w-[130px]"><button class="rounded p-2 text-white 
                Button_fullSize__custom
                Button_grayColor" id="idCheck">아이디 중복확인</button></div>
                                </div>
                                <div class="flex mt-3 mb-3"><span class="w-[130px] leading-10">E-mail</span><input
                                        type="text" class="w-[250px] rounded border mr-2 p-1 h-10" id="numberSendInput">
                                    <div class="w-[130px]"><button class="rounded p-2 text-white 
                Button_fullSize__custom
                Button_grayColor" id="numberSend">인증번호 전송</button></div>
                                </div>
                                <div class="flex mt-3 mb-3"><span class="w-[130px] leading-10">인증코드</span><input
                                        type="text" class="w-[250px] rounded border mr-2 p-1 h-10"
                                        id="numberCheckInput">
                                    <div class="w-[130px]"><button class="rounded p-2 text-white 
                Button_fullSize__custom
                Button_grayColor" id="numberCheck">인증번호 확인</button></div>
                                </div>
                                <div class="flex mt-3 mb-3"><span class="w-[130px] leading-10">닉네임</span><input
                                        type="text" class="w-[250px] rounded border mr-2 p-1 h-10"
                                        placeholder="문자 or 숫자만 사용 가능합니다" id="nicknameCheckInput">
                                    <div class="w-[130px]"><button class="rounded p-2 text-white 
                Button_fullSize__custom
                Button_grayColor" id="nicknameCheck">닉네임 중복확인</button></div>
                                </div>
                                <div class="flex mt-3 mb-3"><span class="w-[130px] leading-10">비밀번호</span>
                                    <input type="password"
                                        class="w-[388px] rounded border p-1 h-10 focus:outline-0 null false"
                                        placeholder="8~12자리 영문+숫자+특수문자 중 2개 조합" id="password">
                                </div>
                                <div class="flex mt-3 mb-3"><span class="w-[130px] leading-10">비밀번호
                                        확인</span>
                                    <input type="password"
                                        class="w-[388px] rounded border p-1 h-10 focus:outline-0 null false"
                                        id="passwordCheck">
                                </div>
                            </div>
                            <div class="mb-10">서비스 이용약관<div class="border h-[200px] overflow-auto mt-2 mb-2">
                                    제20조(개인정보보호)
                                    <br>① 사업자는
                                    개인정보의 수집ㆍ이용, 제공 등 개인정보의 처리 및 보호와 관련하여 「개인정보 보호법」을 준수합니다. <br>② 개인정보의 열람, 정정, 처리정지 등
                                    이용자의
                                    권리는 「개인정보
                                    보호법」에 따라 보호받습니다. <br><br>제9조(미성년자 등의 회원가입과 법정대리인 등의 취소 등) <br>① 만 19세 미만의 이용자가
                                    법정대리인의
                                    동의를 받지 아니하고,
                                    사이버몰의 회원으로 가입을 한 경우에 이용자ㆍ법정대리인 또는 승계인은 사이버몰 이용계약을 취소할 수 있습니다. 다만, 「민법」 등에 따라 미성년자가
                                    취소할 수
                                    없는 경우에는
                                    그러하지 않습니다.<br>② 제1항본문의 경우에 사업자는 이용자의 법정대리인이 추인하기 전까지 사이버몰 이용계약을 철회할 수 있습니다. 다만, 사업자가
                                    회원이
                                    미성년자라는 사실을
                                    알았을 경우에는 철회할 수 없습니다.<br><br>③ 제1항 또는 제2항에 따라 사이버몰 이용계약이 취소 또는 철회된 경우에 사업자는 회원으로부터 받은
                                    대금을
                                    환급하며, 위약금
                                    등을 청구하지 않습니다. 다만, 사업자가 악의인 경우에 대금을 받은 날로부터 환급할 때까지의 기간에 대한 이자를 더하여 환급하며, 회원에게 손해가 있는
                                    경우에
                                    그 손해에 대해
                                    배상하여야 합니다.<br><br>④ 제1항 또는 제2항에 따라 사이버몰 이용계약이 취소 또는 철회된 경우에 사업자는 그 회원의 정보를 지체없이
                                    삭제합니다.
                                    다만, 「전자상거래
                                    등에서의 소비자보호에 관한 법률」 등에 따라 법정기간 동안 정보를 보존하여야 하는 경우에는 그러하지 않습니다.<br><br>제10조(아이디 및 비밀번호
                                    관리의무 등) ① 회원은
                                    아이디와 비밀번호를 선량한 관리자의 주의의무로 관리하며, 제3자에게 이용허락할 수 없습니다. 다만, 사업자가 동의한 경우에는 그러하지
                                    않습니다.<br>②
                                    제1항본문을 위반한 경우에
                                    사업자는 회원에게 발생한 손해에 대해 책임을 지지 않습니다. 다만, 회원의 손해발생에 대해 사업자에게 책임이 있는 경우에 사업자는 그 범위내에서 책임을
                                    부담합니다.<br><br>③
                                    회원은 아이디 및 비밀번호가 도용되거나 제3자에 의해 사용되고 있음을 알게 된 경우에 즉시 사업자에게 그 사실을 통지하고, 사업자의 안내에 따라야
                                    합니다.<br><br>④ 제3항의
                                    경우, 회원이 사업자에게 그 사실을 통지하지 않거나 통지한 경우에도 사업자의 안내에 따르지 않아 발생한 회원의 손해에 대해 사업자는 책임을 지지
                                    않습니다.
                                    다만, 사업자에게
                                    책임있는 경우에는 그 범위내에서 책임을 부담합니다.<br><br>⑤ 회원은 도용 등을 방지하기 위해 주기적으로 비밀번호를 변경하며, 사업자는 회원에게
                                    비밀번호의 변경을 권고할 수
                                    있습니다.<br><br>제11조(회원에 대한 통지) ① 개별 회원에 대한 통지를 하는 경우, 사업자는 회원이 지정한 전자우편주소로 그 내용을
                                    전송합니다.<br>② 전체 회원에게
                                    통지할 경우, 사업자는 회원이 쉽게 알 수 있도록 사이버몰의 게시판 등에 7일 이상 그 내용을 게시합니다. 다만, 회원의 사이버몰 이용계약과 관련하여
                                    영향을
                                    미치는 사항에 대하여는
                                    제1항에 따른 통지를 합니다.<br><br>③ 제1항 및 제2항단서에도 불구하고, 회원이 지정한 전자우편주소가 없는 경우 또는 회원이 지정한
                                    전자우편주소로
                                    통지할 수 없는 경우에
                                    사업자는 제2항본문에서 정한 방법으로 통지할 수 있습니다. 이 경우, 사업자가 회원의 전화번호를 알고 있는 때에는 그 내용을 확인할 수 있는 방법을
                                    전화 또는
                                    문자로 안내합니다.
                                </div><input type="checkbox" name="term" id="termCheck"
                                    class=" translate-y-[1px]"><label for="term" class="p-2">상기 약관에 동의합니다(필수)</label>
                            </div>
                            <div class="flex mb-3 flex-col mt-10 gap-3"><button class="rounded p-2 text-white 
                Button_fullSize__custom
                Button_subColor__custom" id="joinBtn">회원가입</button></div>
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
        <script src="${servletPath}/js/join.js"></script>
        <script src="${servletPath}/js/movePage.js"></script>
    </body>

    </html>