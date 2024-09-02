<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>Sign Up</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <meta charset="utf-8"/>
    <meta property="twitter:card" content="summary_large_image"/>

    <%--   제이쿼리 --%>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

    <style data-tag="reset-style-sheet">
        html {
            line-height: 1.15;
        }

        body {
            margin: 0;
        }

        * {
            box-sizing: border-box;
            border-width: 0;
            border-style: solid;
        }

        p, li, ul, pre, div, h1, h2, h3, h4, h5, h6, figure, blockquote, figcaption {
            margin: 0;
            padding: 0;
        }

        button {
            background-color: transparent;
        }

        button, input, optgroup, select, textarea {
            font-family: inherit;
            font-size: 100%;
            line-height: 1.15;
            margin: 0;
        }

        button, select {
            text-transform: none;
        }

        button, [type="button"], [type="reset"], [type="submit"] {
            -webkit-appearance: button;
        }

        button::-moz-focus-inner, [type="button"]::-moz-focus-inner, [type="reset"]::-moz-focus-inner, [type="submit"]::-moz-focus-inner {
            border-style: none;
            padding: 0;
        }

        button:focus, [type="button"]:focus, [type="reset"]:focus, [type="submit"]:focus {
            outline: 1px dotted ButtonText;
        }

        a {
            color: inherit;
            text-decoration: inherit;
        }

        input {
            padding: 2px 4px;
        }

        img {
            display: block;
        }

        html {
            scroll-behavior: smooth
        }
    </style>
    <style data-tag="default-style-sheet">
        html {
            font-family: Inter;
            font-size: 16px;
        }

        body {
            font-weight: 400;
            font-style: normal;
            text-decoration: none;
            text-transform: none;
            letter-spacing: normal;
            line-height: 1.15;
            color: var(--dl-color-theme-neutral-dark);
            background: var(--dl-color-theme-neutral-light);
            fill: var(--dl-color-theme-neutral-dark);
        }
    </style>

    <script>
        //비번 일치 확인.
        $(document).ready(function () {
            $('#password, #confirmPassword').on('input', function () {
                var password = $('#password').val();
                var confirmPassword = $('#confirmPassword').val();

                if (password === "" && confirmPassword === "") {
                    $('#errorMessage').hide();
                    $('#confirmPassword').removeClass('input-error');
                } else if (password !== confirmPassword) {
                    $('#errorMessage').show();
                    $('#confirmPassword').addClass('input-error');
                } else {
                    $('#errorMessage').hide();
                    $('#confirmPassword').removeClass('input-error');
                }
            });

            // 폼 제출 시 비밀번호 일치 여부 확인
            $('#form').on('submit', function (event) {
                var password = $('#password').val();
                var confirmPassword = $('#confirmPassword').val();

                if (password !== confirmPassword) {
                    event.preventDefault(); // 폼 제출 막기
                    $('#errorMessage').show();
                    $('#confirmPassword').addClass('input-error');
                    alert("password not match!");
                }
            });
        });
    </script>

    <link
            rel="stylesheet"
            href="https://unpkg.com/animate.css@4.1.1/animate.css"
    />
    <link
            rel="stylesheet"
            href="https://fonts.googleapis.com/css2?family=Inter:ital,wght@0,100;0,200;0,300;0,400;0,500;0,600;0,700;0,800;0,900;1,100;1,200;1,300;1,400;1,500;1,600;1,700;1,800;1,900&amp;display=swap"
            data-tag="font"
    />
    <link
            rel="stylesheet"
            href="https://fonts.googleapis.com/css2?family=STIX+Two+Text:ital,wght@0,400;0,500;0,600;0,700;1,400;1,500;1,600;1,700&amp;display=swap"
            data-tag="font"
    />
    <link
            rel="stylesheet"
            href="https://fonts.googleapis.com/css2?family=Noto+Sans:ital,wght@0,100;0,200;0,300;0,400;0,500;0,600;0,700;0,800;0,900;1,100;1,200;1,300;1,400;1,500;1,600;1,700;1,800;1,900&amp;display=swap"
            data-tag="font"
    />
    <link
            rel="stylesheet"
            href="/css/style.css"
    />
</head>
<body>
<link rel="stylesheet" href="/css/index.css"/>
<div>
    <div class="splicecombyhtmltodesign-fre-eversion3008202485712gmt-container10">
        <div class="splicecombyhtmltodesign-fre-eversion3008202485712gmt-splicecombyhtmltodesign-fre-eversion3008202485712gmt">
            <div class="splicecombyhtmltodesign-fre-eversion3008202485712gmt-container11">
                <div class="splicecombyhtmltodesign-fre-eversion3008202485712gmt-container12">
                    <div class="splicecombyhtmltodesign-fre-eversion3008202485712gmt-main">
                        <div class="splicecombyhtmltodesign-fre-eversion3008202485712gmt-section">
                            <div class="splicecombyhtmltodesign-fre-eversion3008202485712gmt-background1">
                                <div class="splicecombyhtmltodesign-fre-eversion3008202485712gmt-container13">
                                    <div class="splicecombyhtmltodesign-fre-eversion3008202485712gmt-header">
                                        <div class="splicecombyhtmltodesign-fre-eversion3008202485712gmt-spliceauth03xpng"></div>
                                        <div class="splicecombyhtmltodesign-fre-eversion3008202485712gmt-heading1">
                          <span class="splicecombyhtmltodesign-fre-eversion3008202485712gmt-text10 splice.comInterBold20">
                            <span>Sign up for a new account</span>
                          </span>
                                        </div>
                                    </div>
                                    <div class="splicecombyhtmltodesign-fre-eversion3008202485712gmt-container14">
                                        <form action="../member/doJoin" method="post" id="form"
                                              class="splicecombyhtmltodesign-fre-eversion3008202485712gmt-container15">
                                            <button class="splicecombyhtmltodesign-fre-eversion3008202485712gmt-form-button1">
                                                <div class="splicecombyhtmltodesign-fre-eversion3008202485712gmt-image1">
                                                    <div class="splicecombyhtmltodesign-fre-eversion3008202485712gmt-imagefill1">
                                                        <div class="splicecombyhtmltodesign-fre-eversion3008202485712gmt-image2">
                                                            <div class="splicecombyhtmltodesign-fre-eversion3008202485712gmt-clippathgroup1">
                                                                <div class="splicecombyhtmltodesign-fre-eversion3008202485712gmtb1">
                                                                    <img
                                                                            src="/external/vector7364-6soc.svg"
                                                                            alt="Vector7364"
                                                                            class="splicecombyhtmltodesign-fre-eversion3008202485712gmt-vector10"
                                                                    />
                                                                </div>
                                                                <img
                                                                        src="/external/vector7364-5lyd.svg"
                                                                        alt="Vector7364"
                                                                        class="splicecombyhtmltodesign-fre-eversion3008202485712gmt-vector11"
                                                                />
                                                            </div>
                                                            <div class="splicecombyhtmltodesign-fre-eversion3008202485712gmt-clippathgroup2">
                                                                <div class="splicecombyhtmltodesign-fre-eversion3008202485712gmtb2">
                                                                    <img
                                                                            src="/external/vector7364-hura.svg"
                                                                            alt="Vector7364"
                                                                            class="splicecombyhtmltodesign-fre-eversion3008202485712gmt-vector12"
                                                                    />
                                                                </div>
                                                                <img
                                                                        src="/external/vector7364-ich.svg"
                                                                        alt="Vector7364"
                                                                        class="splicecombyhtmltodesign-fre-eversion3008202485712gmt-vector13"
                                                                />
                                                            </div>
                                                            <div class="splicecombyhtmltodesign-fre-eversion3008202485712gmt-clippathgroup3">
                                                                <div class="splicecombyhtmltodesign-fre-eversion3008202485712gmtb3">
                                                                    <img
                                                                            src="/external/vector7365-qxjg.svg"
                                                                            alt="Vector7365"
                                                                            class="splicecombyhtmltodesign-fre-eversion3008202485712gmt-vector14"
                                                                    />
                                                                </div>
                                                                <img
                                                                        src="/external/vector7365-a2ga.svg"
                                                                        alt="Vector7365"
                                                                        class="splicecombyhtmltodesign-fre-eversion3008202485712gmt-vector15"
                                                                />
                                                            </div>
                                                            <div class="splicecombyhtmltodesign-fre-eversion3008202485712gmt-clippathgroup4">
                                                                <div class="splicecombyhtmltodesign-fre-eversion3008202485712gmtb4">
                                                                    <img
                                                                            src="/external/vector7365-jvdfg.svg"
                                                                            alt="Vector7365"
                                                                            class="splicecombyhtmltodesign-fre-eversion3008202485712gmt-vector16"
                                                                    />
                                                                </div>
                                                                <img
                                                                        src="/external/vector7365-7x8q.svg"
                                                                        alt="Vector7365"
                                                                        class="splicecombyhtmltodesign-fre-eversion3008202485712gmt-vector17"
                                                                />
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="splicecombyhtmltodesign-fre-eversion3008202485712gmt-container16">
                              <span class="splicecombyhtmltodesign-fre-eversion3008202485712gmt-text12 splice.comInterRegular16">
                                <span>Continue with Google</span>
                              </span>
                                                </div>
                                            </button>
                                            <button class="splicecombyhtmltodesign-fre-eversion3008202485712gmt-form-button2">
                                                <div class="splicecombyhtmltodesign-fre-eversion3008202485712gmt-image3">
                                                    <div class="splicecombyhtmltodesign-fre-eversion3008202485712gmt-imagefill2">
                                                        <div class="splicecombyhtmltodesign-fre-eversion3008202485712gmt-image4">
                                                            <img
                                                                    src="/images/spofitylogo7377-9doi-200h.png"
                                                                    alt="spofityLogo7377"
                                                                    class="splicecombyhtmltodesign-fre-eversion3008202485712gmt-spofity-logo"
                                                            />
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="splicecombyhtmltodesign-fre-eversion3008202485712gmt-container17">
                              <span class="splicecombyhtmltodesign-fre-eversion3008202485712gmt-text14 splice.comInterRegular16">
                                <span>Continue with Spotify</span>
                              </span>
                                                </div>
                                            </button>
                                            <div class="splicecombyhtmltodesign-fre-eversion3008202485712gmt-margin1">
                                                <div class="splicecombyhtmltodesign-fre-eversion3008202485712gmt-container18">
                                                    <div class="splicecombyhtmltodesign-fre-eversion3008202485712gmt-container19">
                              <span class="splicecombyhtmltodesign-fre-eversion3008202485712gmt-text16 splice.comInterMedium12upper">
                                <span>Or</span>
                              </span>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="splicecombyhtmltodesign-fre-eversion3008202485712gmt-form">
                                                <div class="splicecombyhtmltodesign-fre-eversion3008202485712gmt-container20">
                                                    <div class="splicecombyhtmltodesign-fre-eversion3008202485712gmt-background2">
                                                        <input
                                                                type="text"
                                                                name="name"
                                                                placeholder="Name"
                                                                class="splicecombyhtmltodesign-fre-eversion3008202485712gmt-input1"
                                                        />
                                                        <div class="splicecombyhtmltodesign-fre-eversion3008202485712gmt-label1"></div>
                                                        <div class="splicecombyhtmltodesign-fre-eversion3008202485712gmt-background3">
                                <span class="splicecombyhtmltodesign-fre-eversion3008202485712gmt-text18 splice.comInterRegular14.08">
                                </span>
                                                        </div>
                                                    </div>
                                                    <div class="splicecombyhtmltodesign-fre-eversion3008202485712gmt-background-border">
                                                        <input
                                                                type="text"
                                                                name="nickname"
                                                                placeholder="Nickname"
                                                                class="splicecombyhtmltodesign-fre-eversion3008202485712gmt-input2"
                                                        />
                                                        <div class="splicecombyhtmltodesign-fre-eversion3008202485712gmt-label2"></div>
                                                        <div class="splicecombyhtmltodesign-fre-eversion3008202485712gmt-background5">
                                <span class="splicecombyhtmltodesign-fre-eversion3008202485712gmt-text20 splice.comInterRegular161">
                                </span>
                                                        </div>
                                                    </div>
                                                    <div class="splicecombyhtmltodesign-fre-eversion3008202485712gmt-background-border">
                                                        <input
                                                                type="email"
                                                                name="email"
                                                                placeholder="Email"
                                                                class="splicecombyhtmltodesign-fre-eversion3008202485712gmt-input2"
                                                        />
                                                        <div class="splicecombyhtmltodesign-fre-eversion3008202485712gmt-label2"></div>
                                                        <div class="splicecombyhtmltodesign-fre-eversion3008202485712gmt-background5">
                                <span class="splicecombyhtmltodesign-fre-eversion3008202485712gmt-text20 splice.comInterRegular161">
                                </span>
                                                        </div>
                                                    </div>
                                                    <div class="splicecombyhtmltodesign-fre-eversion3008202485712gmt-background-border">
                                                        <input
                                                                type="password"
                                                                name="loginPw"
                                                                id="password"
                                                                placeholder="Password"
                                                                class="splicecombyhtmltodesign-fre-eversion3008202485712gmt-input2"
                                                        />
                                                        <div class="splicecombyhtmltodesign-fre-eversion3008202485712gmt-label2"></div>
                                                        <div class="splicecombyhtmltodesign-fre-eversion3008202485712gmt-background5">
                                <span class="splicecombyhtmltodesign-fre-eversion3008202485712gmt-text20 splice.comInterRegular161">
                                </span>
                                                        </div>
                                                    </div>
                                                    <div class="splicecombyhtmltodesign-fre-eversion3008202485712gmt-background5">
                                <span class="splicecombyhtmltodesign-fre-eversion3008202485712gmt-text20 splice.comInterRegular161">
                                </span>
                                                    </div>
                                                </div>
                                                <div class="splicecombyhtmltodesign-fre-eversion3008202485712gmt-background-border">
                                                    <input
                                                            type="password"
                                                            name="ConfirmPassword"
                                                            placeholder="Confirm Password"
                                                            id="confirmPassword"
                                                            class="splicecombyhtmltodesign-fre-eversion3008202485712gmt-input2"
                                                    />
                                                    <div class="splicecombyhtmltodesign-fre-eversion3008202485712gmt-label2"></div>
                                                    <div class="splicecombyhtmltodesign-fre-eversion3008202485712gmt-background5">
                                                        <span class="splicecombyhtmltodesign-fre-eversion3008202485712gmt-text20 splice.comInterRegular161">
                                </span>
                                                    </div>
                                                </div>
                                            </div>
                                            <span id="errorMessage" style="color: red; display: none;">Passwords do not match!</span>

                                            <button class="splicecombyhtmltodesign-fre-eversion3008202485712gmt-button2">
                            <span class="splicecombyhtmltodesign-fre-eversion3008202485712gmt-text24 splice.comInterRegular16">
                              <span>Sign Up</span>
                            </span>
                                            </button>
                                            <div class="splicecombyhtmltodesign-fre-eversion3008202485712gmt-container21">
                                                    <span class="splicecombyhtmltodesign-fre-eversion3008202485712gmt-text22 splice.comInterRegular14">
                              <span>Already have an account?</span>
                              <a href="/usr/member/login">Log in</a>
                            </span>
                                            </div>
                                    </div>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="splicecombyhtmltodesign-fre-eversion3008202485712gmt-margin3">
                <div class="splicecombyhtmltodesign-fre-eversion3008202485712gmt-container22"></div>
            </div>
        </div>
    </div>
</div>
</div>
</body>
</html>
