package com.luojilab.componentdemo.msg;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.view.View;

import com.luojilab.component.componentlib.msg.AriseAt;
import com.luojilab.component.componentlib.msg.ConsumeOn;
import com.luojilab.component.componentlib.msg.EventListener;
import com.luojilab.component.componentlib.msg.EventManager;
import com.luojilab.component.componentlib.router.ui.UIRouter;
import com.luojilab.componentdemo.msg.event.EventA;
import com.luojilab.componentdemo.msg.event.EventB;
import com.luojilab.router.facade.annotation.RouteNode;

@RouteNode(path = "/msg/demo/1", desc = "主进程页面1")
public class Msg1Activity extends Foo {

    EventListener<EventA> eventAEventListener;
    EventListener<EventB> eventBEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventAEventListener = new EventListener<EventA>() {
            @Override
            public void onEvent(EventA event) {
                tvMsg.setText(event.getMsg());
            }
        };

        eventBEventListener = new EventListener<EventB>() {
            @Override
            public void onEvent(EventB event) {
                tvMsg.setText(event.getMsg());
            }
        };

        EventManager.getInstance().subscribe(EventA.class, eventAEventListener);
        EventManager.getInstance().subscribe(EventB.class,
                AriseAt.remote("com.luojilab.componentdemo.application:remote")
                , ConsumeOn.Main, eventBEventListener);
    }

    @Override
    protected CharSequence getBtnText() {
        SpannableStringBuilder ssb = new SpannableStringBuilder();

        SpannableString spans1 = new SpannableString("启动同进程页面");
        spans1.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                UIRouter.getInstance().openUri(widget.getContext(),
                        "JIMU://app/msg/demo/2",null);
            }
        },0,spans1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.append(spans1).append("\r\n\r\n");

        SpannableString spans2 = new SpannableString("启动remote进程页面");
        spans2.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                UIRouter.getInstance().openUri(widget.getContext(),
                        "JIMU://app/msg/demo/3",null);
            }
        },0,spans2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.append(spans2);



        return ssb;
    }

    @Override
    protected void onDestroy() {
        EventManager.getInstance().unsubscribe(eventBEventListener);
        EventManager.getInstance().unsubscribe(eventAEventListener);
        super.onDestroy();
    }
}
