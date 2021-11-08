const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const MiniCssExtractPlugin = require('mini-css-extract-plugin');
require('babel-polyfill');

module.exports = {
  mode: 'development',
  entry: ['babel-polyfill', './src/index.jsx'],
  output: {
    path: path.resolve(__dirname, '/dist'),
    filename: 'main.js',
    clean: true,
  },
  devtool: 'inline-source-map',
  devServer: {
    historyApiFallback: true,
    proxy: {
      '/': {
        target: 'http://localhost:8080',
      },
    },
    static: './dist',
    port: 3000,
  },
  resolve: {
    extensions: ['', '.jsx', '.js', '.css'],
  },
  plugins: [
    new HtmlWebpackPlugin({
      title: 'webpack react build',
      template: path.join(__dirname, 'src', 'index.html'),
    }),
    new MiniCssExtractPlugin(),
  ],
  module: {
    rules: [
      {
        test: /\.(js|jsx)$/,
        exclude: /node_modules/,
        use: ['babel-loader'],
      },
      {
        test: /\.css$/i,
        use: [MiniCssExtractPlugin.loader, 'css-loader'],
      },
      {
        test: /\.(png|svg|jpg|jpeg|gif)$/i,
        type: 'asset/resource',
      },
      {
        test: /\.(woff|woff2|eot|ttf|otf)$/i,
        type: 'asset/resource',
      },
    ],
  },
};
