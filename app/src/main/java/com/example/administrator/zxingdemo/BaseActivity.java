package com.example.administrator.zxingdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.net.URL;
import java.security.PrivateKey;

public class BaseActivity extends AppCompatActivity {

    private Button open_btn;
    private TextView tv_result;
    private Button open_result;
    private WebView webView;
    private String scanRes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        open_btn = (Button) findViewById(R.id.openDesc);
        tv_result = (TextView) findViewById(R.id.mResult);
        open_result = (Button) findViewById(R.id.getResult);
        webView = (WebView) findViewById(R.id.mWebView);
        initWebView();
        initEvent();
    }

    private void initEvent(){
        open_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open_result.setVisibility(View.GONE);
                zXingScannerUtil(BaseActivity.this);
            }
        });

        open_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTheResult(scanRes);
            }
        });
    }

    /**
    * 将二维码解析的字符串显示在textView上
    * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult resultIntent = IntentIntegrator.parseActivityResult(
                requestCode, resultCode, data);
        if (resultIntent != null) {
            if (resultIntent.getContents() == null) {
                tv_result.setText("扫描结果未知，请确定条码正确！");
            } else {
                //获取扫描结果字符串
                scanRes = resultIntent.getContents();
                tv_result.setText(scanRes);
                open_result.setVisibility(View.VISIBLE);
            }
        } else{
        super.onActivityResult(requestCode, resultCode, data);
    }
    }

    /**
    * 启动zxing开始扫描
    * */
    private void zXingScannerUtil(Activity fromActivity){
        IntentIntegrator integrator = new IntentIntegrator(fromActivity);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
                .setPrompt("请将二维码/条码放入方框内，即可自动扫描")//提示
                .setOrientationLocked(false)//扫描方向固定为false
                .setCaptureActivity(MainActivity.class)//
                .initiateScan();
    }
    /**
     * 初始化webView的基本属性，包括可输入，可缩放，可缓存
     * */
    private void initWebView(){
        WebSettings settings = webView.getSettings();
        //支持javascript(默认支持)
        settings.setJavaScriptEnabled(true);
        //缓存
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        //不缓存
//        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        //支持打开的网页缩放
        settings.setBuiltInZoomControls(true);
        settings.setSupportZoom(true);
        //网页需要手动输入用户名等内容时，需要获取手势焦点
        webView.requestFocusFromTouch();
        //显示滚动条(此处为在内容显示内部)
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        //判断页面加载过程
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100){
                    //网页加载完成
                }else {
                    //加载中
                }
            }
        });
        //监听加载的是否是一个下载链接
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimeType, long contentLength) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });


    }

    private void openTheResult(String url){
        if (url != null && url.contains("http")) {
            //如果是安装包，就调用本地浏览器打开链接
            if (false){
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(url);
                intent.setData(content_url);
                startActivity(intent);
            }else {
                //如果是网页，也在本地webview中打开
                webView.loadUrl(url);
                webView.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                        view.loadUrl(url);
                        return true;
                    }
                });
            }
        }else {
            Toast.makeText(BaseActivity.this,"解析出现错误",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            if (webView.canGoBack()){
                //如果希望浏览的网页后退而不是退出浏览器，需要WebView覆盖URL加载，
                // 让它自动生成历史访问记录，那样就可以通过前进或后退访问已访问过的站点。
                webView.goBack();
                return true;
            }else {
                finish();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
