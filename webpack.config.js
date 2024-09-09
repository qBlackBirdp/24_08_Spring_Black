const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const { CleanWebpackPlugin } = require('clean-webpack-plugin');
// npx webpack serve -- 웹팩 서버 실행.
// Ctrl + C -- 웹팩 서버 종료.

// 웹팩 설정
module.exports = {
    mode: 'development',
    entry: './src/index.js', // Firebase 초기화 코드가 포함된 파일
    output: {
        filename: 'bundle.js',
        path: path.resolve(__dirname, 'dist'),
    },
    module: {
        rules: [
            {
                test: /\.js$/, // JavaScript 파일을 대상으로 babel-loader 사용
                exclude: /node_modules/,
                use: {
                    loader: 'babel-loader',
                },
            },
        ],
    },
    plugins: [
        new CleanWebpackPlugin(), // dist 폴더 정리
        new HtmlWebpackPlugin({
            template: './src/index.html', // HTML 템플릿 경로
        }),
    ],
    devServer: {
        static: path.join(__dirname, 'dist'), // 정적 파일 경로
        compress: true,
        port: 8082, // 개발 서버 포트
        watchFiles: ['src/**/*'], // src 폴더의 모든 파일을 감시
        hot: false,  // HMR 비활성화
        client: {
            overlay: false,  // overlay를 client 설정으로 이동
        },
    },
    optimization: {
        usedExports: false,  // 트리 셰이킹 비활성화 (모든 모듈이 번들에 포함되도록 함)
    }
};